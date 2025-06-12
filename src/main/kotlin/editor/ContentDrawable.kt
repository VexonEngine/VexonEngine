package org.vexon.editor

open class ContentDrawable(setupCall: ContentDrawable.() -> Unit) : Drawable() {
    private var children: MutableList<Drawable> = mutableListOf()
    private var childrenQueue: MutableList<Drawable> = mutableListOf()
    private var removalQueue: MutableList<Drawable> = mutableListOf()

    init {
        this.setupCall()
    }

    override fun draw() {
        beforeDraw()

        for (child in children) {
            child.draw()
        }

        while (childrenQueue.isNotEmpty()) {
            val child = childrenQueue.removeAt(0)
            children.add(child)
            child.parent = this
        }

        while (removalQueue.isNotEmpty()) {
            val child = removalQueue.removeAt(0)
            children.remove(child)
            child.parent = null
        }

        afterDraw()
    }

    open fun beforeDraw() {

    }

    open fun afterDraw() {

    }

    fun addChild(child: Drawable) {
        childrenQueue.add(child)
    }

    fun removeChild(child: Drawable) {
        if (children.contains(child)) {
            removalQueue.add(child)
        } else {
            throw IllegalArgumentException("Child not found in this ContentDrawable")
        }
    }
}