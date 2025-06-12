package org.vexon

import org.lwjgl.opengl.ARBFramebufferObject.*
import org.lwjgl.opengl.GL11.*
import org.vexon.editor.ManagedWindow
import org.vexon.editor.frame.component.Text
import org.vexon.editor.frame.component.button
import org.vexon.editor.frame.component.glBufferImage
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
        frame("Game View") {
            glBufferImage {
                width, height, fb ->
                renderToFramebuffer(width.toInt(), height.toInt())
            }
        }
    }

    mainWindow.start()
}

fun renderToFramebuffer(width: Int, height: Int) {
    glViewport(0, 0, width, height)

    glEnable(GL_DEPTH_TEST)
    glDepthFunc(GL_LESS)

    glClearColor(0f, 0f, 0f, 1f)
    glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()
    val aspect = width.toFloat() / height
    gluPerspective(45f, aspect, 0.1f, 100f)

    glMatrixMode(GL_MODELVIEW)
    glLoadIdentity()
    glTranslatef(0f, 0f, -3f) // Move the cube into view

    // Animate rotation based on time
    val time = System.nanoTime() / 1_000_000_000.0
    val angle = (time * 45.0) % 360.0 // 45 degrees per second
    glRotated(angle, 1.0, 1.0, 0.0) // Rotate for visibility

    glBegin(GL_QUADS)

    // Front face (red)
    glColor3f(1f, 0f, 0f)
    glVertex3f(-0.5f, -0.5f, -0.5f)
    glVertex3f( 0.5f, -0.5f, -0.5f)
    glVertex3f( 0.5f,  0.5f, -0.5f)
    glVertex3f(-0.5f,  0.5f, -0.5f)

    // Back face (green)
    glColor3f(0f, 1f, 0f)
    glVertex3f(-0.5f, -0.5f, 0.5f)
    glVertex3f( 0.5f, -0.5f, 0.5f)
    glVertex3f( 0.5f,  0.5f, 0.5f)
    glVertex3f(-0.5f,  0.5f, 0.5f)

    // Bottom face (blue)
    glColor3f(0f, 0f, 1f)
    glVertex3f(-0.5f, -0.5f, -0.5f)
    glVertex3f( 0.5f, -0.5f, -0.5f)
    glVertex3f( 0.5f, -0.5f,  0.5f)
    glVertex3f(-0.5f, -0.5f,  0.5f)

    // Top face (yellow)
    glColor3f(1f, 1f, 0f)
    glVertex3f(-0.5f, 0.5f, -0.5f)
    glVertex3f( 0.5f, 0.5f, -0.5f)
    glVertex3f( 0.5f, 0.5f,  0.5f)
    glVertex3f(-0.5f, 0.5f,  0.5f)

    // Left face (magenta)
    glColor3f(1f, 0f, 1f)
    glVertex3f(-0.5f, -0.5f, -0.5f)
    glVertex3f(-0.5f,  0.5f, -0.5f)
    glVertex3f(-0.5f,  0.5f,  0.5f)
    glVertex3f(-0.5f, -0.5f,  0.5f)

    // Right face (cyan)
    glColor3f(0f, 1f, 1f)
    glVertex3f(0.5f, -0.5f, -0.5f)
    glVertex3f(0.5f,  0.5f, -0.5f)
    glVertex3f(0.5f,  0.5f,  0.5f)
    glVertex3f(0.5f, -0.5f,  0.5f)

    glEnd()
}

// Helper for gluPerspective using classic math
fun gluPerspective(fovY: Float, aspect: Float, zNear: Float, zFar: Float) {
    val fH = kotlin.math.tan(Math.toRadians((fovY / 2).toDouble())).toFloat() * zNear
    val fW = fH * aspect
    glFrustum(-fW.toDouble(), fW.toDouble(), -fH.toDouble(), fH.toDouble(), zNear.toDouble(), zFar.toDouble())
}