package me.zeroeightsix.kami.gui

import glm_.vec2.Vec2
import glm_.vec2.Vec2d
import imgui.ImGui
import imgui.classes.Context
import imgui.classes.IO
import imgui.font.FontConfig
import imgui.impl.gl.ImplGL3
import imgui.impl.glfw.ImplGlfw
import me.zeroeightsix.kami.gui.widgets.EnabledWidgets
import me.zeroeightsix.kami.gui.widgets.PinnableWidget
import net.minecraft.client.MinecraftClient
import uno.glfw.GlfwWindow

object KamiHud {

    internal var implGl3: ImplGL3
    internal val context: Context
    private val implGlfw: ImplGlfw
    private val io: IO

    init {
        val window = GlfwWindow.from(MinecraftClient.getInstance().window.handle)
        window.makeContextCurrent()
        context = Context()
        implGlfw = ImplGlfw(window, false, null)
        implGl3 = ImplGL3()
        io = ImGui.io
        io.iniFilename = "kami-imgui.ini"

        val fontCfg = FontConfig()
        fontCfg.oversample put 1
        fontCfg.pixelSnapH = true
        fontCfg.glyphOffset = Vec2(0, -2)
        ImGui.io.fonts.addFontFromFileTTF("assets/kami/Minecraftia.ttf", 12f, fontCfg)
        ImGui.io.fonts.addFontDefault()
    }

    fun renderHud() {
        frame {
            if (!EnabledWidgets.hideAll) {
                PinnableWidget.drawFadedBackground = false
                for ((widget, open) in EnabledWidgets.widgets) {
                    if (open.get() && widget.pinned) {
                        widget.showWindow(open)
                    }
                }
                PinnableWidget.drawFadedBackground = true
            }
        }
    }

    internal fun frame(block: () -> Unit) {
        implGl3.newFrame()
        implGlfw.newFrame()
        ImGui.newFrame()

        try { block() } finally {
            ImGui.render()
            implGl3.renderDrawData(ImGui.drawData!!)
        }
    }

    fun mouseScroll(d: Double, e: Double) {
        ImplGlfw.scrollCallback(Vec2d(d, e))
    }

}