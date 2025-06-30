package org.example.plugin;

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
import software.coley.recaf.services.workspace.WorkspaceManager;
import software.coley.recaf.ui.control.ActionMenuItem;
import software.coley.recaf.ui.control.FontIconView;
import software.coley.recaf.ui.menubar.AnalysisMenu;
import software.coley.recaf.ui.menubar.MainMenu;
import software.coley.recaf.ui.window.DeobfuscationWindow;
import software.coley.recaf.util.FxThreadUtil;

import java.util.List;

import static software.coley.recaf.util.Lang.getBinding;
import static software.coley.recaf.util.Menus.action;

@Dependent
@PluginInformation(id = "##ID##", version = "##VERSION##", name = "##NAME##", description = "##DESC##")
public class ExamplePlugin implements Plugin {
	private static final Logger logger = Logging.get(ExamplePlugin.class);
	private final Instance<MainMenu> mainMenus;
	MPluginMenu pluginMenu;
	private List<AbstractPass> passList;
	WorkspaceManager workspaceManager;

	// You can inject Recaf's services in this constructor.
	//  - https://recaf.coley.software/dev/plugins-and-scripts/plugins.html
	@Inject
	public ExamplePlugin(@Nonnull Instance<MainMenu> mainMenus,
						 @Nonnull WorkspaceManager workspaceManager) {
		this.mainMenus = mainMenus;
		this.workspaceManager = workspaceManager;

	}

	@Override
	public void onEnable() {
		logger.info("Hello from the example plugin");
		var passMap = new PluginClassLoader().scanAndLoadPassPlugin();
		FxThreadUtil.run(() -> {
			var ins =  mainMenus.get();

			if (passMap.isEmpty()) {
				logger.warn("No pass plugin found");
				return;
			}
			for(var entry : passMap.entrySet()) {
				ins.getMenus().add(new MPluginMenu(workspaceManager, entry.getKey(), entry.getValue()));
			}

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
