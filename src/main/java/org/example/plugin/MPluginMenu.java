package org.example.plugin;

import com.github.luiox.morpher.transformer.AbstractPass;
import com.github.luiox.morpher.transformer.ClassPass;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import javafx.scene.control.Menu;
import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.slf4j.Logger;
import software.coley.recaf.analytics.logging.Logging;
import software.coley.recaf.info.JvmClassInfo;
import software.coley.recaf.services.inheritance.InheritanceGraph;
import software.coley.recaf.services.inheritance.InheritanceGraphService;
import software.coley.recaf.services.inheritance.InheritanceVertex;
import software.coley.recaf.services.transform.JvmClassTransformer;
import software.coley.recaf.services.transform.JvmTransformerContext;
import software.coley.recaf.services.transform.TransformationException;
import software.coley.recaf.services.workspace.WorkspaceManager;
import software.coley.recaf.workspace.model.Workspace;
import software.coley.recaf.workspace.model.bundle.JvmClassBundle;
import software.coley.recaf.workspace.model.resource.WorkspaceResource;

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

    static class ClassPassAdapterTransformer implements JvmClassTransformer {
        private ClassPass classPass;

        public ClassPassAdapterTransformer(@Nonnull ClassPass classPass) {
            this.classPass = classPass;
        }

        @Override
        public void transform(@Nonnull JvmTransformerContext context, @Nonnull Workspace workspace,
                              @Nonnull WorkspaceResource resource, @Nonnull JvmClassBundle bundle,
                              @Nonnull JvmClassInfo initialClassState) throws TransformationException {

        }

        @Nonnull
        @Override
        public String name() {
            return "ClassPassAdapterTransformer";
        }
    }
}
