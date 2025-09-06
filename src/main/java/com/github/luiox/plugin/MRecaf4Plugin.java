package com.github.luiox.plugin;

import com.github.luiox.morpher.transformer.AbstractPass;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.slf4j.Logger;
import software.coley.recaf.analytics.logging.Logging;
import software.coley.recaf.plugin.Plugin;
import software.coley.recaf.plugin.PluginInformation;
import software.coley.recaf.services.cell.context.ContextMenuProviderService;
import software.coley.recaf.services.workspace.WorkspaceManager;
import software.coley.recaf.ui.control.FontIconView;
import software.coley.recaf.ui.menubar.MainMenu;
import software.coley.recaf.util.FxThreadUtil;

import java.util.List;
import java.util.Map;

@Dependent
@PluginInformation(id = "##ID##", version = "##VERSION##", name = "##NAME##", description = "##DESC##")
public class MRecaf4Plugin implements Plugin {
    private static final Logger logger = Logging.get(MRecaf4Plugin.class);
    private final Instance<MainMenu> mainMenus;
    WorkspaceManager workspaceManager;
    RecafAdapterPassContext passContext;
    Map<String, List<AbstractPass>> passMap;
    Map<AbstractPass, ClassPassAdapterTransformer> classPassTransformerMap;
    Map<AbstractPass, MethodPassAdapterTransformer> methodPassTransformerMap;
    ContextMenuProviderService contextMenuProviderService;

    // You can inject Recaf's services in this constructor.
    //  - https://recaf.coley.software/dev/plugins-and-scripts/plugins.html
    @Inject
    public MRecaf4Plugin(@Nonnull Instance<MainMenu> mainMenus,
                         @Nonnull WorkspaceManager workspaceManager,
                         @Nonnull ContextMenuProviderService contextMenuProviderService) {
        this.mainMenus = mainMenus;
        this.workspaceManager = workspaceManager;
        this.passContext = new RecafAdapterPassContext(workspaceManager.getCurrent(), workspaceManager);
        this.contextMenuProviderService = contextMenuProviderService;
    }

    @Override
    public void onEnable() {
        logger.info("Hello from the example plugin");
        passMap = new PluginClassLoader().scanAndLoadPassPlugin();
        FxThreadUtil.run(() -> {
            var ins = mainMenus.get();

            var pluginMenu = new Menu("Morpher", new FontIconView(CarbonIcons.DEVELOPMENT));
            pluginMenu.getItems().add(new MenuItem("ControlFlow", new FontIconView(CarbonIcons.DEVELOPMENT)));
            pluginMenu.getItems().add(new MenuItem("Transform", new FontIconView(CarbonIcons.DEVELOPMENT)));
            ins.getMenus().add(pluginMenu);

            if (passMap.isEmpty()) {
                logger.warn("No pass plugin found");
                return;
            }
//            for (var entry : passMap.entrySet()) {
//                ins.getMenus().add(new MPluginMenu(workspaceManager, entry.getKey(), entry.getValue()));
//            }

//			MenuItem itemViewImport = new ActionMenuItem("importPass", () -> {

//
//			});
//
//			ins.getItems().add(itemViewImport);
        });
    }

    @Override
    public void onDisable() {
        logger.info("goodbye from the example plugin");
    }
}
