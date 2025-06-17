package org.vexon.renderer.dim3d.obj

import java.io.File

class ObjFile(path: String) {
    private val vertices: MutableList<Float> = mutableListOf()
    private val normals: MutableList<Float> = mutableListOf()
    private val textureCoords: MutableList<Float> = mutableListOf()
    private val faces: MutableList<Int> = mutableListOf()
    private val normalIndices: MutableList<Int> = mutableListOf()

    init {
        loadObjFile(path)
    }

    private fun loadObjFile(path: String) {
        val file = File(path)
        if (!file.exists()) {
            println("Error: OBJ file not found at path: $path")
            return
        }

        try {
            file.bufferedReader().use { reader ->
                reader.lines().forEach { line ->
                    val parts = line.trim().split("\\s+".toRegex())

                    when {
                        line.startsWith("v ") -> {
                            // Parse vertex
                            if (parts.size >= 4) {
                                vertices.add(parts[1].toFloat())
                                vertices.add(parts[2].toFloat())
                                vertices.add(parts[3].toFloat())
                            }
                        }
                        line.startsWith("vn ") -> {
                            // Parse normal
                            if (parts.size >= 4) {
                                normals.add(parts[1].toFloat())
                                normals.add(parts[2].toFloat())
                                normals.add(parts[3].toFloat())
                            }
                        }
                        line.startsWith("vt ") -> {
                            // Parse texture coordinate
                            if (parts.size >= 3) {
                                textureCoords.add(parts[1].toFloat())
                                textureCoords.add(parts[2].toFloat())
                                // Some OBJ files have 3D texture coordinates
                                if (parts.size >= 4) {
                                    textureCoords.add(parts[3].toFloat())
                                }
                            }
                        }
                        line.startsWith("f ") -> {
                            // Parse face
                            // OBJ face format: f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3 ...

                            // Handle different face formats
                            // Skip the first part which is the "f" prefix
                            for (i in 1 until parts.size) {
                                val indices = parts[i].split("/")
                                if (indices.isNotEmpty()) {
                                    // OBJ indices are 1-based, convert to 0-based
                                    val vertexIndex = indices[0].toInt() - 1
                                    faces.add(vertexIndex)

                                    // Extract normal index if available (position 2 in the split)
                                    if (indices.size >= 3 && indices[2].isNotEmpty()) {
                                        val normalIndex = indices[2].toInt() - 1
                                        normalIndices.add(normalIndex)
                                    } else if (normals.isNotEmpty()){
                                        // If no normal index is provided, use the vertex index as a fallback
                                        // This ensures the normalIndices list stays in sync with faces list
                                        normalIndices.add(vertexIndex % (normals.size / 3))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            println("OBJ file loaded successfully: $path")
            println("Vertices: ${vertices.size / 3}, Normals: ${normals.size / 3}, Texture Coords: ${textureCoords.size / 2}, Face Indices: ${faces.size}, Normal Indices: ${normalIndices.size}")

            // Print a sample of the first few normal indices to help with debugging
            if (normalIndices.isNotEmpty()) {
                val sampleSize = minOf(10, normalIndices.size)
                println("Sample of first $sampleSize normal indices: ${normalIndices.take(sampleSize)}")
            }
        } catch (e: Exception) {
            println("Error loading OBJ file: ${e.message}")
            e.printStackTrace()
        }
    }

    fun getVertices(): List<Float> = vertices
    fun getNormals(): List<Float> = normals
    fun getTextureCoords(): List<Float> = textureCoords
    fun getFaces(): List<Int> = faces
    fun getNormalIndices(): List<Int> = normalIndices
}
