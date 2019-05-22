package org.workcraft.plugins.circuit.commands;

import org.workcraft.commands.NodeTransformer;
import org.workcraft.commands.AbstractTransformationCommand;
import org.workcraft.dom.Node;
import org.workcraft.dom.visual.ConnectionHelper;
import org.workcraft.dom.visual.VisualModel;
import org.workcraft.dom.visual.VisualNode;
import org.workcraft.dom.visual.connections.VisualConnection;
import org.workcraft.exceptions.InvalidConnectionException;
import org.workcraft.plugins.circuit.*;
import org.workcraft.plugins.circuit.utils.CircuitUtils;
import org.workcraft.utils.Hierarchy;
import org.workcraft.utils.LogUtils;
import org.workcraft.workspace.ModelEntry;
import org.workcraft.workspace.WorkspaceEntry;
import org.workcraft.utils.WorkspaceUtils;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

public class ContractComponentTransformationCommand extends AbstractTransformationCommand implements NodeTransformer {

    @Override
    public String getDisplayName() {
        return "Contract selected single-input/single-output components";
    }

    @Override
    public String getPopupName() {
        return "Contract component";
    }

    @Override
    public boolean isApplicableTo(WorkspaceEntry we) {
        return WorkspaceUtils.isApplicable(we, VisualCircuit.class);
    }

    @Override
    public boolean isApplicableTo(VisualNode node) {
        return node instanceof VisualCircuitComponent;
    }

    @Override
    public boolean isEnabled(ModelEntry me, VisualNode node) {
        boolean result = false;
        if (node instanceof VisualCircuitComponent) {
            VisualCircuitComponent component = (VisualCircuitComponent) node;
            result = component.getReferencedComponent().isSingleInputSingleOutput();
        }
        return result;
    }

    @Override
    public Position getPosition() {
        return Position.TOP_MIDDLE;
    }

    @Override
    public Collection<VisualNode> collect(VisualModel model) {
        Collection<VisualNode> components = new HashSet<>();
        components.addAll(Hierarchy.getDescendantsOfType(model.getRoot(), VisualCircuitComponent.class));
        components.retainAll(model.getSelection());
        return components;
    }

    @Override
    public void transform(VisualModel model, VisualNode node) {
        if ((model instanceof VisualCircuit) && (node instanceof VisualCircuitComponent)) {
            VisualCircuit circuit = (VisualCircuit) model;
            VisualCircuitComponent component = (VisualCircuitComponent) node;
            if (isValidContraction(circuit, component)) {
                VisualContact inputContact = component.getFirstVisualInput();
                for (VisualContact outputContact: component.getVisualOutputs()) {
                    connectContacts(circuit, inputContact, outputContact);
                }
                circuit.remove(component);
            }
        }
    }

    private boolean isValidContraction(VisualCircuit circuit, VisualCircuitComponent component) {
        Collection<VisualContact> inputContacts = component.getVisualInputs();
        String componentName = circuit.getMathName(component);
        if (inputContacts.size() > 2) {
            LogUtils.logError("Cannot contract component '" + componentName + "' with " + inputContacts.size() + " inputs.");
            return false;
        }
        VisualContact inputContact = component.getFirstVisualInput();
        Collection<VisualContact> outputContacts = component.getVisualOutputs();
        if (outputContacts.size() > 2) {
            LogUtils.logError("Cannot contract component '" + componentName + "' with " + outputContacts.size() + " outputs.");
            return false;
        }
        VisualContact outputContact = component.getFirstVisualOutput();

        // Input and output ports
        Circuit mathCircuit = circuit.getMathModel();
        Contact driver = CircuitUtils.findDriver(mathCircuit, inputContact.getReferencedComponent(), true);
        HashSet<Contact> drivenSet = new HashSet<>();
        drivenSet.addAll(CircuitUtils.findDriven(mathCircuit, driver, true));
        drivenSet.addAll(CircuitUtils.findDriven(mathCircuit, outputContact.getReferencedContact(), true));
        int outputPortCount = 0;
        for (Contact driven: drivenSet) {
            if (driven.isOutput() && driven.isPort()) {
                outputPortCount++;
                if (outputPortCount > 1) {
                    LogUtils.logError("Cannot contract component '" + componentName + "' as it leads to fork on output ports.");
                    return false;
                }
                if ((driver != null) && driver.isInput() && driver.isPort()) {
                    LogUtils.logError("Cannot contract component '" + componentName + "' as it leads to direct connection from input port to output port.");
                    return false;
                }
            }
        }

        // Handle zero delay components
        Contact directDriver = CircuitUtils.findDriver(mathCircuit, inputContact.getReferencedComponent(), false);
        Node directDriverParent = directDriver.getParent();
        if (directDriverParent instanceof FunctionComponent) {
            FunctionComponent directDriverComponent = (FunctionComponent) directDriverParent;
            if (directDriverComponent.getIsZeroDelay()) {
                Collection<Contact> directDrivenSet = CircuitUtils.findDriven(mathCircuit, outputContact.getReferencedContact(), false);
                for (Contact directDriven: directDrivenSet) {
                    if (directDriven.isOutput() && directDriven.isPort()) {
                        LogUtils.logError("Cannot contract component '" + componentName + "' as it leads to connection of zero delay component to output port.");
                        return false;
                    }
                    Node directDrivenParent = directDriven.getParent();
                    if (directDrivenParent instanceof FunctionComponent) {
                        FunctionComponent directDrivenComponent = (FunctionComponent) directDrivenParent;
                        if (directDrivenComponent.getIsZeroDelay()) {
                            LogUtils.logError("Cannot contract component '" + componentName + "' as it leads to connection between zero delay components.");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void connectContacts(VisualCircuit circuit, VisualContact inputContact, VisualContact outputContact) {
        for (VisualConnection inputConnection: circuit.getConnections(inputContact)) {
            VisualNode fromNode = inputConnection.getFirst();
            for (VisualConnection outputConnection: new ArrayList<>(circuit.getConnections(outputContact))) {
                VisualNode toNode = outputConnection.getSecond();
                LinkedList<Point2D> locations = ConnectionHelper.getMergedControlPoints(outputContact,
                        inputConnection, outputConnection);
                circuit.remove(outputConnection);
                try {
                    VisualConnection newConnection = circuit.connect(fromNode, toNode);
                    newConnection.mixStyle(inputConnection, outputConnection);
                    ConnectionHelper.addControlPoints(newConnection, locations);
                } catch (InvalidConnectionException e) {
                    LogUtils.logWarning(e.getMessage());
                }
            }
        }
    }

}
