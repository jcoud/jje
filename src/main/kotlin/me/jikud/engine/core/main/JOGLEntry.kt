package me.jikud.engine.core.main

import com.jogamp.newt.NewtFactory
import com.jogamp.newt.event.WindowAdapter
import com.jogamp.newt.event.WindowEvent
import com.jogamp.newt.opengl.GLWindow
import com.jogamp.opengl.GL.*
import com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.GLCapabilities
import com.jogamp.opengl.GLEventListener
import com.jogamp.opengl.GLProfile
import com.jogamp.opengl.fixedfunc.GLLightingFunc
import com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH
import com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW
import com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION
import com.jogamp.opengl.util.awt.TextRenderer
import me.jikud.engine.core.gui.GuiCore
import me.jikud.engine.core.helpers.FFont
import me.jikud.engine.core.main.GLRenderHelper.JGL
import java.awt.Toolkit
import kotlin.properties.Delegates

class JOGLEntry : GLEventListener {

    private val pixelTallness = 10


    override fun init(drawable: GLAutoDrawable) {
        val gl = drawable.gl.gL2 // get the OpenGL graphics context
//        val glu = GLU() // get GL Utilities
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f) // set background (clear) color
        gl.glClearDepth(1.0) // set clear depth value to farthest
        gl.glEnable(GL_DEPTH_TEST) // enables depth testing
        gl.glDepthFunc(GL_LEQUAL) // the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST) // best perspective correction
        gl.glShadeModel(GL_SMOOTH) // blends colors nicely, and smoothes out lighting
        gl.glEnable(GLLightingFunc.GL_LIGHT0) // Enable default light (quick and dirty)
        gl.glEnable(GLLightingFunc.GL_LIGHTING) // Enable lighting
        gl.glEnable(GLLightingFunc.GL_COLOR_MATERIAL) // Enable coloring of material

        GLRenderHelper.FontRendering.fontRenderer = TextRenderer(FONT)

        gl.glMatrixMode(GL_PROJECTION)                        // Select The Projection Matrix
        gl.glPushMatrix()                                    // Store The Projection Matrix
        gl.glLoadIdentity()                                  // Reset The Projection Matrix
        // Set Up An Ortho Screen
        gl.glOrtho(0.0, INIT_WIDTH.toDouble(), 0.0, INIT_HEIGHT.toDouble(), -1.0, 1.0)
        gl.glMatrixMode(GL_MODELVIEW)                         // Select The Modelview Matrix
        gl.glPushMatrix()                                     // Store The Modelview Matrix
        gl.glLoadIdentity()

    }

    override fun reshape(drawable: GLAutoDrawable, x: Int, y: Int, w: Int, h: Int) {
//        val gl = drawable.gl.gL2
//        gl.glMatrixMode(GL_PROJECTION)
//        gl.glLoadIdentity()
//
//        val u = h / (w / pixelTallness).toDouble()
//        gl.glOrtho(0.0, w.toDouble(), 0.0, h.toDouble(), -u, 1.0)
//        gl.glMatrixMode(GL_MODELVIEW)
//        var height = h
//        val gl = drawable.gl.gL2 // get the OpenGL 2 graphics context
//        if (height == 0) height = 1 // prevent divide by zero
//        val aspect = w.toFloat() / height
//
//        // Set the view port (display area) to cover the entire window
//        gl.glViewport(0, 0, w, height)
//
//        // Setup perspective projection, with aspect ratio matches viewport
//        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION) // choose projection matrix
//        gl.glLoadIdentity() // reset projection matrix
//        glu!!.gluPerspective(45.0, aspect.toDouble(), 0.1, 100.0) // fovy, aspect, zNear, zFar
//
//        // Enable the model-view transform
//        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW)
//        gl.glLoadIdentity() // reset
    }

    /*
     * Called back by the animator to perform rendering.
     */
    override fun display(drawable: GLAutoDrawable) {
        val gl = drawable.gl.gL2
        JGL = gl
        gl.glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer
        gl.glLoadIdentity()

        gl.glEnable(GL_BLEND)
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        unitFunc.run()
        GuiCore.processDraw()


        gl.glDisable(GL_BLEND)
        gl.glFinish()
    }


    /*
     * Called back before the OpenGL context is destroyed. Release resource such as buffers.
     */
    override fun dispose(drawable: GLAutoDrawable?) {

    }

    companion object {

        private var unitFunc by Delegates.notNull<Runnable>()
        var FONT by Delegates.notNull<FFont>()
        var INIT_WIDTH by Delegates.notNull<Int>()
        var INIT_HEIGHT by Delegates.notNull<Int>()
        var FPS by Delegates.notNull<Int>()
        var TITLE by Delegates.notNull<String>()

//        private lateinit var canvas: GLCanvas
        private lateinit var window: GLWindow

        fun init() {
            //TODO: попробовать через GLCanvas и GLFrame чтобы отрисовывать текст
            GLProfile.initSingleton()
            val glp = GLProfile.get(GLProfile.GL2)
            val caps = GLCapabilities(glp)
            caps.doubleBuffered = true
            caps.hardwareAccelerated = true
            window = GLWindow.create(NewtFactory.createWindow(caps))
            window.addGLEventListener(JOGLEntry())
            window.setSize(INIT_WIDTH, INIT_HEIGHT)
//            window.defaultCloseOperation = WindowClosingProtocol.WindowClosingMode.DO_NOTHING_ON_CLOSE
            window.addWindowListener(object : WindowAdapter() {
                override fun windowDestroyed(e: WindowEvent?) {
                    Loop.stop()
                }
            })
            window.title = TITLE
            val d = Toolkit.getDefaultToolkit().screenSize
            window.setPosition((d.width - INIT_WIDTH) / 2, (d.height - INIT_HEIGHT) / 2)
            window.isResizable = false
            window.isVisible = true
//            canvas = GLCanvas(caps)
//            canvas.preferredSize = Dimension(INIT_WIDTH, INIT_HEIGHT)

//            canvas.addGLEventListener(JOGLEntry())
//            val window = JFrame()
//            window.contentPane.add(canvas)
//            window.defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE
//            window.addWindowListener(object : WindowAdapter() {
//                override fun windowClosing(e: WindowEvent?) {
//                    Loop.stop()
//                }
//            })
//            window.title = TITLE
//            window.pack()
//            window.setLocationRelativeTo(null)
//            window.isVisible = true
//            window.isResizable = false

            IOTransfer(window)
        }

        fun renderCanvasUpdate() {
            window.display()
//            window.display()
        }

        fun render(render: Runnable) {
            unitFunc = render
        }
    }
}