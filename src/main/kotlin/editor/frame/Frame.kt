package org.vexon.editor.frame

import imgui.ImGui
import org.vexon.editor.ContentDrawable

class Frame(var title: String, setupCall: ContentDrawable.() -> Unit): ContentDrawable(setupCall) {
    override fun beforeDraw() {
        ImGui.begin(title)
    }

    override fun afterDraw() {
        ImGui.end()
    }
}

fun ContentDrawable.frame(title: String, setupCall: ContentDrawable.() -> Unit): Frame {
    val frame = Frame(title, setupCall)
    this.addChild(frame)
    return frame
}