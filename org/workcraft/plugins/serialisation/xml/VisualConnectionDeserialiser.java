package org.workcraft.plugins.serialisation.xml;

import org.w3c.dom.Element;
import org.workcraft.dom.math.MathConnection;
import org.workcraft.dom.visual.VisualComponent;
import org.workcraft.dom.visual.connections.VisualConnection;
import org.workcraft.exceptions.DeserialisationException;
import org.workcraft.serialisation.ReferenceResolver;
import org.workcraft.serialisation.xml.ReferencingXMLDeserialiser;

public class VisualConnectionDeserialiser implements ReferencingXMLDeserialiser {
	public String getClassName() {
		return VisualConnection.class.getName();
	}

	public void deserialise(Element element, Object instance,
			ReferenceResolver internalReferenceResolver,
			ReferenceResolver externalReferenceResolver)
	throws DeserialisationException {
		VisualConnection vcon = (VisualConnection)instance;

		vcon.setDependencies(
				(VisualComponent)internalReferenceResolver.getObject(element.getAttribute("first")),
				(VisualComponent)internalReferenceResolver.getObject(element.getAttribute("second")),
				(MathConnection)externalReferenceResolver.getObject(element.getAttribute("ref"))
		);
	}
}