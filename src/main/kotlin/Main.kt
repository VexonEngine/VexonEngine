package org.vexon

import org.vexon.editor.ManagedWindow
import org.vexon.editor.frame.Frame
import org.vexon.editor.frame.component.Text
import org.vexon.editor.frame.component.button
import org.vexon.editor.frame.component.text
import org.vexon.editor.frame.frame


fun main() {
    val mainWindow = ManagedWindow("Vexon") {
        frame("Inspector") {
            val t1 = text("This is an ImGui window in Kotlin.")
            button("Click Me!") {
                text = "Thanks!"
                t1.text = "You Clicked my neighbor!"
            }
        }
        frame("Console") {
            button("Clear") {
                parent!!.addChild(Text("Console cleared!"))
            }
        }
        frame("Debugger") {
            val t1 = text("This is an ImGui window in Kotlin.")
            button("Click Me!") {
                text = "Thanks!"
                t1.text = "You Clicked my neighbor!"
            }
        }
    }

    mainWindow.run()
}