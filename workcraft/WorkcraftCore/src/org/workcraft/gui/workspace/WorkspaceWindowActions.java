package org.workcraft.gui.workspace;

import org.workcraft.Framework;
import org.workcraft.exceptions.OperationCancelledException;
import org.workcraft.gui.actions.Action;

public class WorkspaceWindowActions {

    public static final Action ADD_FILES_TO_WORKSPACE_ACTION = new Action("Link files to the root of workspace...",
            () -> Framework.getInstance().getMainWindow().getWorkspaceView().addToWorkspace(Path.root("")));

    public static final Action OPEN_WORKSPACE_ACTION = new Action("Open workspace...",
            () -> Framework.getInstance().getMainWindow().getWorkspaceView().openWorkspace());

    public static final Action SAVE_WORKSPACE_ACTION = new Action("Save workspace",
            () -> {
                try {
                    Framework.getInstance().getMainWindow().getWorkspaceView().saveWorkspace();
                } catch (OperationCancelledException e) {
                }
            });

    public static final Action SAVE_WORKSPACE_AS_ACTION = new Action("Save workspace as...",
            () -> {
                try {
                    Framework.getInstance().getMainWindow().getWorkspaceView().saveWorkspaceAs();
                } catch (OperationCancelledException e) {
                }
            });

    public static final Action NEW_WORKSPACE_AS_ACTION = new Action("New workspace",
            () -> Framework.getInstance().getMainWindow().getWorkspaceView().newWorkspace());

}
