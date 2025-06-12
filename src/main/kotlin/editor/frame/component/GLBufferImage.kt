package org.vexon.editor.frame.component

import imgui.ImGui
import org.lwjgl.opengl.ARBFramebufferObject.GL_COLOR_ATTACHMENT0
import org.lwjgl.opengl.ARBFramebufferObject.GL_DEPTH_ATTACHMENT
import org.lwjgl.opengl.ARBFramebufferObject.GL_FRAMEBUFFER
import org.lwjgl.opengl.ARBFramebufferObject.GL_FRAMEBUFFER_COMPLETE
import org.lwjgl.opengl.ARBFramebufferObject.GL_RENDERBUFFER
import org.lwjgl.opengl.ARBFramebufferObject.glBindFramebuffer
import org.lwjgl.opengl.ARBFramebufferObject.glBindRenderbuffer
import org.lwjgl.opengl.ARBFramebufferObject.glCheckFramebufferStatus
import org.lwjgl.opengl.ARBFramebufferObject.glFramebufferRenderbuffer
import org.lwjgl.opengl.ARBFramebufferObject.glFramebufferTexture2D
import org.lwjgl.opengl.ARBFramebufferObject.glGenFramebuffers
import org.lwjgl.opengl.ARBFramebufferObject.glGenRenderbuffers
import org.lwjgl.opengl.ARBFramebufferObject.glRenderbufferStorage
import org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT
import org.lwjgl.opengl.GL11.GL_LINEAR
import org.lwjgl.opengl.GL11.GL_RGBA
import org.lwjgl.opengl.GL11.GL_RGBA8
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER
import org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER
import org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE
import org.lwjgl.opengl.GL11.glBindTexture
import org.lwjgl.opengl.GL11.glGenTextures
import org.lwjgl.opengl.GL11.glTexImage2D
import org.lwjgl.opengl.GL11.glTexParameteri
import org.vexon.editor.ContentDrawable
import org.vexon.editor.Drawable

class GLBufferImage(var texIdProvider: (Float, Float, Framebuffer) -> Unit) : Drawable() {
    var fb: Framebuffer? = null

    override fun draw() {
        val width = ImGui.getContentRegionAvailX()
        val height = ImGui.getContentRegionAvailY()

        if (fb == null) {
            fb = createFramebuffer()
        }

        fb!!.update(width.toInt(), height.toInt())

        glBindFramebuffer(GL_FRAMEBUFFER, fb!!.fbo)
        texIdProvider(width, height, fb!!)
        glBindFramebuffer(GL_FRAMEBUFFER, 0)

        ImGui.image(fb!!.texId, width, height)
    }
}

class Framebuffer(val fbo: Int, var texId: Int, val depthBuffer: Int) {
    fun update(width: Int, height: Int) {
        glBindTexture(GL_TEXTURE_2D, texId)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        glBindFramebuffer(GL_FRAMEBUFFER, fbo)
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texId, 0)
        glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer)
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height)
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBuffer)

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw RuntimeException("Framebuffer incomplete after resize")
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }
}

fun createFramebuffer(): Framebuffer {
    val fbo = glGenFramebuffers()
    val texId = glGenTextures()
    val depthBuffer = glGenRenderbuffers()


    return Framebuffer(fbo, texId, depthBuffer)
}



fun ContentDrawable.glBufferImage(texIdProvider: (Float, Float, Framebuffer) -> Unit): GLBufferImage {
    val image = GLBufferImage(texIdProvider)
    this.addChild(image)
    return image
}