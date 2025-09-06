package com.github.luiox.plugin;

import com.github.luiox.morpher.transformer.AbstractPass;
import jakarta.annotation.Nonnull;
import javafx.scene.control.Menu;
import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.slf4j.Logger;
import software.coley.recaf.analytics.logging.Logging;
import software.coley.recaf.services.workspace.WorkspaceManager;

import java.util.List;

import static software.coley.recaf.util.Menus.action;

public class MPluginMenu extends Menu {
    private static final Logger logger = Logging.get(MPluginMenu.class);
    WorkspaceManager workspaceManager;

    public MPluginMenu(@Nonnull WorkspaceManager workspaceManager, String title, List<AbstractPass> passList) {
        super(title);
        this.workspaceManager = workspaceManager;

        logger.info("Found {} passes", passList.size());
        for (AbstractPass pass : passList) {
            logger.info("Found pass {}", pass.getClass().getName());


            getItems().add(action(pass.getClass().getSimpleName(), CarbonIcons.DEVELOPMENT, () -> {

            }));
        }


    }
}
