package org.vexon.editor.frame.component

import imgui.ImGui
import org.vexon.editor.ContentDrawable
import org.vexon.editor.Drawable

class Text(var text: String) : Drawable() {
    override fun draw() {
        ImGui.text(text)
    }
}

fun ContentDrawable.text(text: String): Text {
    val text = Text(text)
    this.addChild(text)
    return text
}