package org.vexon.renderer.obj

import org.lwjgl.opengl.GL11.*

object ObjFileRenderer {
    private fun objFileToDrawCalls(objFile: ObjFile, color: Triple<Float, Float, Float> = Triple(0.8f, 0.8f, 0.8f)) {
        val vertices = objFile.getVertices()
        val normals = objFile.getNormals()
        val faces = objFile.getFaces()
        val normalIndices = objFile.getNormalIndices()

        // Set material properties for better lighting
        val matAmbient = floatArrayOf(0.2f, 0.2f, 0.2f, 1.0f)
        val matDiffuse = floatArrayOf(0.8f, 0.8f, 0.8f, 1.0f)
        //val matSpecular = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)
        val matShininess = floatArrayOf(5.0f, 0.0f, 0.0f, 0.0f)

        glMaterialfv(GL_FRONT, GL_AMBIENT, matAmbient)
        glMaterialfv(GL_FRONT, GL_DIFFUSE, matDiffuse)
        //glMaterialfv(GL_FRONT, GL_SPECULAR, matSpecular)
        glMaterialfv(GL_FRONT, GL_SHININESS, matShininess)

        glBegin(GL_TRIANGLES)

        var i = 0
        while (i < faces.size) {
            for (j in 0 until 3) {
                if (i + j >= faces.size) break

                val vertexIndex = faces[i + j]

                if (normals.isNotEmpty()) {
                    val normalIndex = if (i + j < normalIndices.size) normalIndices[i + j] else 0

                    if (normalIndex * 3 + 2 < normals.size) {
                        glNormal3f(
                            normals[normalIndex * 3],
                            normals[normalIndex * 3 + 1],
                            normals[normalIndex * 3 + 2]
                        )
                    }
                }

                if (vertexIndex * 3 + 2 < vertices.size) {
                    glColor3f(color.first, color.second, color.third)

                    glVertex3f(
                        vertices[vertexIndex * 3],
                        vertices[vertexIndex * 3 + 1],
                        vertices[vertexIndex * 3 + 2]
                    )
                }
            }

            i += 3
        }

        glEnd()
    }

    fun renderObjWithTransform(
        objFile: ObjFile,
        scale: Float = 1.0f,
        rotationAngles: Triple<Float, Float, Float> = Triple(0f, 0f, 0f),
        position: Triple<Float, Float, Float> = Triple(0f, 0f, 0f),
        color: Triple<Float, Float, Float> = Triple(0.8f, 0.8f, 0.8f)
    ) {
        glPushMatrix()

        glTranslatef(position.first, position.second, position.third)
        glRotatef(rotationAngles.first, 1f, 0f, 0f)
        glRotatef(rotationAngles.second, 0f, 1f, 0f)
        glRotatef(rotationAngles.third, 0f, 0f, 1f)
        glScalef(scale, scale, scale)


        glEnable(GL_LIGHTING)
        glEnable(GL_LIGHT0)

        glEnable(GL_COLOR_MATERIAL)
        glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE)

        val lightPos = floatArrayOf(1.0f, 1.0f, 1.0f, 0.0f) // Directional light from top-right-front
        val lightAmbient = floatArrayOf(0.3f, 0.3f, 0.3f, 1.0f) // Slightly brighter ambient
        val lightDiffuse = floatArrayOf(0.9f, 0.9f, 0.9f, 1.0f) // Slightly brighter diffuse
        val lightSpecular = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f) // Add specular component

        glLightfv(GL_LIGHT0, GL_POSITION, lightPos)
        glLightfv(GL_LIGHT0, GL_AMBIENT, lightAmbient)
        glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuse)
        glLightfv(GL_LIGHT0, GL_SPECULAR, lightSpecular)

        objFileToDrawCalls(objFile, color)

        // Disable all lighting features we enabled
        glDisable(GL_COLOR_MATERIAL)
        glDisable(GL_LIGHTING)
        glDisable(GL_LIGHT0)


        glPopMatrix()
    }
}
