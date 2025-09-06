package com.github.luiox.plugin;

import com.github.luiox.morpher.transformer.BasicPassContext;
import com.github.luiox.morpher.transformer.PassHelper;
import software.coley.recaf.services.workspace.WorkspaceManager;
import software.coley.recaf.workspace.model.Workspace;

public class RecafAdapterPassContext extends BasicPassContext {

    Workspace workspace;
    WorkspaceManager workspaceManager;

    public RecafAdapterPassContext(Workspace workspace, WorkspaceManager workspaceManager) {
        this.workspace = workspace;
        this.workspaceManager = workspaceManager;
    }

    @Override
    public PassHelper getPassHelper() {
        return RecafPassHelperImpl.getInstance();
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public WorkspaceManager getWorkspaceManager() {
        return workspaceManager;
    }
}
