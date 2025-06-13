package org.vexon.editor

import imgui.ImGui
import imgui.ImGuiIO
import imgui.flag.ImGuiCol
import imgui.flag.ImGuiConfigFlags
import imgui.flag.ImGuiStyleVar
import imgui.flag.ImGuiWindowFlags
import imgui.gl3.ImGuiImplGl3
import imgui.glfw.ImGuiImplGlfw
import imgui.type.ImBoolean
import org.lwjgl.glfw.GLFW.glfwCreateWindow
import org.lwjgl.glfw.GLFW.glfwInit
import org.lwjgl.glfw.GLFW.glfwMakeContextCurrent
import org.lwjgl.glfw.GLFW.glfwPollEvents
import org.lwjgl.glfw.GLFW.glfwSwapBuffers
import org.lwjgl.glfw.GLFW.glfwWindowShouldClose
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengl.GL11.GL_DEPTH_TEST
import org.lwjgl.opengl.GL11.glClear
import org.lwjgl.opengl.GL11.glClearColor
import org.lwjgl.opengl.GL11.glEnable
import org.lwjgl.system.MemoryUtil.NULL

class ManagedWindow(val name: String, val setupCall: ContentDrawable.() -> Unit) : ContentDrawable(setupCall) {
    var windowHandle: Long? = null

    fun start() {
        val width = 1280
        val height = 720

        if (!glfwInit()) {
            throw IllegalStateException("Unable to initialize GLFW")
        }

        windowHandle = glfwCreateWindow(width, height, name, NULL, NULL)
        if (windowHandle == NULL) {
            throw RuntimeException("Failed to create the GLFW window")
        }

        glfwMakeContextCurrent(windowHandle!!)
        GL.createCapabilities()


        val window = ImGuiImplGlfw()
        val gl3 = ImGuiImplGl3()

        ImGui.createContext()
        val io = ImGui.getIO()

        window.init(windowHandle!!, true)
        gl3.init("#version 130")

        setupStyle()

        io.configFlags = io.configFlags or ImGuiConfigFlags.DockingEnable


        while (!glfwWindowShouldClose(windowHandle!!)) {
            glfwPollEvents()
            window.newFrame()
            ImGui.newFrame()

            setupDockSpace(io)
            draw()

            ImGui.render()

            gl3.renderDrawData(ImGui.getDrawData())

            glfwSwapBuffers(windowHandle!!)
            glClearColor(30 / 255f, 30 / 255f, 46 / 255f, 1f)
            glClear(GL_COLOR_BUFFER_BIT)
        }

        gl3.dispose()
        window.dispose()
        ImGui.destroyContext()
    }

    private fun setupStyle() {
        val style = ImGui.getStyle()
        style.windowRounding = 10f
        style.frameRounding = 10f
        style.popupRounding = 10f
        style.tabRounding = 5f
        style.scrollbarRounding = 10f
        style.grabRounding = 10f

        style.setWindowPadding(12f, 12f)
        style.setFramePadding(8f, 4f)
        style.setItemSpacing(10f, 8f)
        style.setItemInnerSpacing(6f, 4f)
        style.setCellPadding(6f, 6f)

        val colors = style.colors

        fun rgba(r: Int, g: Int, b: Int, a: Int = 255): FloatArray {
            return floatArrayOf(r / 255f, g / 255f, b / 255f, a / 255f)
        }

        val accent = rgba(250, 132, 85)
        val accentHovered = rgba(255, 153, 102)
        val accentActive = rgba(250, 123, 85)

        colors[ImGuiCol.Text]                 = rgba(205, 214, 244)
        colors[ImGuiCol.TextDisabled]         = rgba(166, 173, 200)
        colors[ImGuiCol.WindowBg]             = rgba(30, 30, 46)
        colors[ImGuiCol.ChildBg]              = rgba(30, 30, 46)
        colors[ImGuiCol.PopupBg]              = rgba(30, 30, 46)
        colors[ImGuiCol.Border]               = rgba(88, 91, 112)
        colors[ImGuiCol.FrameBg]              = rgba(49, 50, 68)
        colors[ImGuiCol.FrameBgHovered]       = rgba(88, 91, 112)
        colors[ImGuiCol.FrameBgActive]        = rgba(108, 112, 134)
        colors[ImGuiCol.TitleBg]              = rgba(49, 50, 68)
        colors[ImGuiCol.TitleBgActive]        = rgba(88, 91, 112)
        colors[ImGuiCol.TitleBgCollapsed]     = rgba(49, 50, 68)
        colors[ImGuiCol.CheckMark]            = accent
        colors[ImGuiCol.SliderGrab]           = accent
        colors[ImGuiCol.SliderGrabActive]     = accentActive
        colors[ImGuiCol.Button]               = rgba(49, 50, 68)
        colors[ImGuiCol.ButtonHovered]        = accent
        colors[ImGuiCol.ButtonActive]         = accentActive
        colors[ImGuiCol.Header]               = rgba(49, 50, 68)
        colors[ImGuiCol.HeaderHovered]        = accent
        colors[ImGuiCol.HeaderActive]         = accentActive
        colors[ImGuiCol.SeparatorHovered]     = accent
        colors[ImGuiCol.SeparatorActive]      = accentActive
        colors[ImGuiCol.ResizeGrip]           = accent
        colors[ImGuiCol.ResizeGripHovered]    = accentHovered
        colors[ImGuiCol.ResizeGripActive]     = accentActive
        colors[ImGuiCol.TabHovered]           = accent
        colors[ImGuiCol.TabActive]            = accentActive
        colors[ImGuiCol.TabUnfocusedActive]   = accentHovered
        colors[ImGuiCol.DockingPreview]       = rgba(250, 132, 85, 180)
        colors[ImGuiCol.DockingEmptyBg]       = rgba(24, 24, 37)
        colors[ImGuiCol.TabUnfocused]         = rgba(30, 32, 48)
        colors[ImGuiCol.TabUnfocusedActive]   = rgba(44, 47, 65)
        colors[ImGuiCol.Tab]                  = rgba(36, 39, 58)
        colors[ImGuiCol.TabHovered]           = rgba(250, 132, 85)
        colors[ImGuiCol.TabActive]            = rgba(230, 100, 50)
        colors[ImGuiCol.TabUnfocused]         = rgba(44, 47, 65)
        colors[ImGuiCol.TabUnfocusedActive]   = rgba(30, 32, 48)


        style.colors = colors
    }

    private fun setupDockSpace(io: ImGuiIO) {
        val viewport = ImGui.getMainViewport()

        ImGui.setNextWindowPos(viewport.pos.x, viewport.pos.y)
        ImGui.setNextWindowSize(viewport.size.x, viewport.size.y)
        ImGui.setNextWindowViewport(viewport.id)

        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0f)
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0f)
        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0f, 0f)

        val windowFlags = ImGuiWindowFlags.NoTitleBar or
                ImGuiWindowFlags.NoCollapse or
                ImGuiWindowFlags.NoResize or
                ImGuiWindowFlags.NoMove or
                ImGuiWindowFlags.NoBringToFrontOnFocus or
                ImGuiWindowFlags.NoNavFocus or
                ImGuiWindowFlags.NoBackground or
                ImGuiWindowFlags.NoDecoration or
                ImGuiWindowFlags.NoScrollWithMouse or
                ImGuiWindowFlags.NoSavedSettings

        ImGui.begin("DockSpaceWindow", ImBoolean(true), windowFlags)
        val dockspaceId = ImGui.getID("DockSpace")
        ImGui.dockSpace(dockspaceId, 0f, 0f, 0)
        ImGui.end()

        ImGui.popStyleVar(3)
    }
}