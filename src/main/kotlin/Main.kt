package org.vexon

import org.lwjgl.opengl.GL11.*
import org.vexon.editor.ManagedWindow
import org.vexon.editor.frame.component.Text
import org.vexon.editor.frame.component.button
import org.vexon.editor.frame.component.glBufferImage
import org.vexon.editor.frame.component.text
import org.vexon.editor.frame.frame
import org.vexon.renderer.dim3d.obj.ObjFile
import org.vexon.renderer.dim3d.obj.ObjFileRenderer
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

private var loadedObjFile: ObjFile? = null

fun main() {
    loadedObjFile = ObjFile("src/main/resources/monkey.obj")

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
        frame("Project Settings") {
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
    glTranslatef(0f, 0f, -3f) // Move the model into view

    val time = System.nanoTime() / 500_000_000.0
    val bounce = sin(2*cos(time) * 2.0f) * 0.7f // Bounce amplitude 0.5
    val rotation = time * 30.0f // Rotate at 50 degrees per second

    if (loadedObjFile != null) {
        val rotX = 0f
        val rotY = 0f
        val rotZ = 180f

        val posX = 0f
        val posY = bounce.toFloat()
        val posZ = 0f

        ObjFileRenderer.renderObjWithTransform(
            objFile = loadedObjFile!!,
            scale = (bounce.toFloat() * 1.5f),
            rotationAngles = Triple(rotation.toFloat(), rotation.toFloat(), rotZ),
            position = Triple(posX, posY, posZ),
            color = Triple((rotation.toFloat()/100)%1, 1 - (rotation.toFloat()/100)%1, (rotation.toFloat()*rotation.toFloat()/10)%1)
        )
    }
}

fun gluPerspective(fovY: Float, aspect: Float, zNear: Float, zFar: Float) {
    val fH = tan(Math.toRadians((fovY / 2).toDouble())).toFloat() * zNear
    val fW = fH * aspect
    glFrustum(-fW.toDouble(), fW.toDouble(), -fH.toDouble(), fH.toDouble(), zNear.toDouble(), zFar.toDouble())
}
