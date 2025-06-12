package org.vexon.editor.frame.component

import imgui.ImGui
import org.vexon.editor.ContentDrawable
import org.vexon.editor.Drawable

class Button(var text: String, var onClick: () -> Unit = {}) : Drawable() {
    override fun draw() {
        if (ImGui.button(text)) {
            onClick()
        }
    }
}

fun ContentDrawable.button(text: String, onNewClick: Button.() -> Unit): Button {
    val button = Button(text)
    button.onClick = { button.onNewClick() }
    this.addChild(button)
    return button
}