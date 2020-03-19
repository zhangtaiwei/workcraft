package org.workcraft.commands;

import org.workcraft.dom.visual.VisualModel;
import org.workcraft.dom.visual.VisualNode;
import org.workcraft.utils.CommandUtils;
import org.workcraft.workspace.WorkspaceEntry;
import org.workcraft.utils.WorkspaceUtils;

import java.util.Collection;
import java.util.HashSet;

public abstract class AbstractTransformationCommand implements ScriptableCommand<Void>, MenuOrdering {

    public static final String SECTION_TITLE = CommandUtils.makePromotedSectionTitle("Transformation", 2);

    @Override
    public final String getSection() {
        return SECTION_TITLE;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public Position getPosition() {
        return null;
    }

    @Override
    public Void execute(WorkspaceEntry we) {
        VisualModel visualModel = WorkspaceUtils.getAs(we, VisualModel.class);
        Collection<? extends VisualNode> nodes = collect(visualModel);
        if (!nodes.isEmpty()) {
            we.saveMemento();
            transform(visualModel, nodes);
        }
        return null;
    }

    public Collection<? extends VisualNode> collect(VisualModel model) {
        return new HashSet<>(model.getSelection());
    }

    public void transform(VisualModel model, Collection<? extends VisualNode> nodes) {
        model.selectNone();
        for (VisualNode node: nodes) {
            transform(model, node);
        }
    }

    public abstract void transform(VisualModel model, VisualNode node);

}