package org.workcraft.commands;

import org.workcraft.Framework;
import org.workcraft.dom.visual.VisualModel;
import org.workcraft.gui.MainWindow;
import org.workcraft.gui.editor.GraphEditorPanel;
import org.workcraft.utils.WorkspaceUtils;
import org.workcraft.workspace.WorkspaceEntry;

public abstract class AbstractLayoutCommand implements ScriptableCommand<Void> {

    private static final String SECTION_TITLE = "Graph layout";

    @Override
    public final String getSection() {
        return SECTION_TITLE;
    }

    @Override
    public boolean isApplicableTo(WorkspaceEntry we) {
        return WorkspaceUtils.isApplicable(we, VisualModel.class);
    }

    @Override
    public final void run(WorkspaceEntry we) {
        we.saveMemento();
        VisualModel model = WorkspaceUtils.getAs(we, VisualModel.class);
        layout(model);
        final Framework framework = Framework.getInstance();
        final MainWindow mainWindow = framework.getMainWindow();
        if (framework.isInGuiMode() && (mainWindow != null)) {
            final GraphEditorPanel editor = mainWindow.getCurrentEditor();
            editor.zoomFit();
        }
    }

    @Override
    public final Void execute(WorkspaceEntry we) {
        run(we);
        return null;
    }

    public abstract void layout(VisualModel model);

}
