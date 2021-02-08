package me.zeroeightsix.kami.gui

import io.github.fablabsmc.fablabs.api.fiber.v1.annotation.Setting
import kotlin.reflect.KMutableProperty0
import me.zeroeightsix.kami.BaritoneIntegration
import me.zeroeightsix.kami.gui.ImguiDSL.mainMenuBar
import me.zeroeightsix.kami.gui.ImguiDSL.menu
import me.zeroeightsix.kami.gui.ImguiDSL.menuItem
import me.zeroeightsix.kami.gui.widgets.EnabledWidgets
import me.zeroeightsix.kami.gui.widgets.VoidContextMenu
import me.zeroeightsix.kami.gui.windows.Settings
import me.zeroeightsix.kami.gui.windows.modules.ModuleWindowsEditor
import me.zeroeightsix.kami.setting.KamiConfig
import me.zeroeightsix.kami.setting.guiService

object View {

    @Setting
    var modulesOpen = true

    @Setting
    var demoWindowVisible = false

    init {
        KamiConfig.register(guiService("view"), this)
    }

    operator fun invoke() = menu("View") {
        fun toggleWindow(title: String, setting: KMutableProperty0<Boolean>, shortcut: String = "") =
            menuItem(title, shortcut, selected = setting()) { setting.set(!setting()) }

        toggleWindow("Settings", Settings::settingsWindowOpen)
        toggleWindow("Modules", ::modulesOpen)
        toggleWindow("Module window editor", ModuleWindowsEditor::open)
        if (Settings.demoWindowInView) toggleWindow("Demo window", ::demoWindowVisible)
    }
}

object MenuBar {
    operator fun invoke() = mainMenuBar {
        EnabledWidgets()
        View()
        BaritoneIntegration.menu()

        // imgui needs a window to add the void popup to, so we disgustingly add it to the only window that will always be there: the main menu bar
        VoidContextMenu()
    }
}