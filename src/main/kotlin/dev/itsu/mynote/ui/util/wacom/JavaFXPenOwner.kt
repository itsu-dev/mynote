package dev.itsu.mynote.ui.util.wacom

import javafx.scene.Node
import javafx.scene.canvas.Canvas
import jpen.PenProvider
import jpen.owner.AbstractPenOwner
import jpen.owner.PenClip
import jpen.provider.osx.CocoaProvider
import jpen.provider.system.SystemProvider
import jpen.provider.wintab.WintabProvider
import jpen.provider.xinput.XinputProvider
import java.lang.ref.WeakReference

class JavaFXPenOwner(private val canvas: Canvas) : AbstractPenOwner() {

    private val javaFXPenClip = JavaFXPenClip(this)
    private val unpauser = Unpauser()

    override fun getPenProviderConstructors(): Collection<PenProvider.Constructor> {
        return listOf(
            SystemProvider.Constructor(),
            XinputProvider.Constructor(),
            WintabProvider.Constructor(),
            CocoaProvider.Constructor()
        )
    }

    override fun getPenClip(): PenClip {
        return this.javaFXPenClip
    }

    protected fun pause() {
        this.unpauser.disable()
        this.penManagerHandle.setPenManagerPaused(true)
    }

    override fun draggingOutDisengaged() {
        this.pause()
    }

    override fun init() {
        this.canvas.setOnMouseExited { e ->
            synchronized(this.getPenSchedulerLock(e.source as Node)) {
                if (!this.startDraggingOut()) {
                    this.pause()
                }
            }
        }

        this.canvas.setOnMouseEntered { e ->
            synchronized(this.getPenSchedulerLock(e.source as Node)) {
                if (!this.stopDraggingOut()) {
                    this.unpauser.enable()
                }
            }
        }
    }

    fun getActiveCanvas() = this.canvas

    protected fun getPenSchedulerLock(node: Node?): Any {
        if (node != null) {
            Thread.currentThread()
        }
        return this.penManagerHandle.penSchedulerLock
    }

    private inner class Unpauser {
        @Volatile private var enabled = false
        private var myActiveCanvasRef: WeakReference<Canvas>? = null

        @Synchronized
        fun enable() {
            if (this.enabled) return

            val myActiveCanvas = this@JavaFXPenOwner.getActiveCanvas()
            this.myActiveCanvasRef = WeakReference(myActiveCanvas)
            myActiveCanvas.setOnMouseMoved { this.unpause() }
            this.enabled = true
        }

        @Synchronized
        fun disable() {
            if (!this.enabled) return
            myActiveCanvasRef?.get()?.setOnMouseMoved { }
            this.enabled = false
        }

        fun unpause() {
            synchronized(this@JavaFXPenOwner.penManagerHandle.penSchedulerLock) {
                if (this.enabled) {
                    this@JavaFXPenOwner.penManagerHandle.setPenManagerPaused(false)
                    this.disable()
                }
            }
        }
    }

}