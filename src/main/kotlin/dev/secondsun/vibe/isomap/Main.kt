package dev.secondsun.vibe.isomap

import javax.swing.*
import java.awt.*
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

class ViewerPanel(val model: MapModel, val camera: Camera) : JPanel() {
    private val renderer = Renderer()

    init {
        isFocusable = true
        addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                val step = FixedMath.fromFloat(0.2f)
                val sY = FixedMath.sin(camera.yaw)
                val cY = FixedMath.cos(camera.yaw)

                when (e.keyCode) {
                    KeyEvent.VK_W -> {
                        // Forward: dx = -sin(yaw), dz = cos(yaw)
                        camera.panX = (camera.panX - FixedMath.multiply(sY, step)).toShort()
                        camera.panZ = (camera.panZ + FixedMath.multiply(cY, step)).toShort()
                    }
                    KeyEvent.VK_S -> {
                        // Backward: dx = sin(yaw), dz = -cos(yaw)
                        camera.panX = (camera.panX + FixedMath.multiply(sY, step)).toShort()
                        camera.panZ = (camera.panZ - FixedMath.multiply(cY, step)).toShort()
                    }
                    KeyEvent.VK_A -> {
                        // Left: dx = -cos(yaw), dz = -sin(yaw)
                        camera.panX = (camera.panX - FixedMath.multiply(cY, step)).toShort()
                        camera.panZ = (camera.panZ - FixedMath.multiply(sY, step)).toShort()
                    }
                    KeyEvent.VK_D -> {
                        // Right: dx = cos(yaw), dz = sin(yaw)
                        camera.panX = (camera.panX + FixedMath.multiply(cY, step)).toShort()
                        camera.panZ = (camera.panZ + FixedMath.multiply(sY, step)).toShort()
                    }
                    KeyEvent.VK_Q -> camera.yaw = (camera.yaw + 5) % 360
                    KeyEvent.VK_E -> camera.yaw = (camera.yaw - 5) % 360
                    KeyEvent.VK_EQUALS, KeyEvent.VK_PLUS -> {
                        camera.zoom = (camera.zoom.toInt() + 8).toShort()
                    }
                    KeyEvent.VK_MINUS -> {
                        camera.zoom = (camera.zoom.toInt() - 8).coerceAtLeast(8).toShort()
                    }
                }
                repaint()
            }
        })
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        renderer.render(g, model, camera, width, height)
    }
}

fun main() {
    TextureLoader.loadTextures()
    SpriteLoader.loadSprites()
    val frame = JFrame("Isometric Map Viewer")
    val model = MapModel()
    val camera = Camera()
    
    // Default zoom: 0.25 in Q8.8 is 64
    camera.zoom = 64.toShort()
    // Center of 8x8 map
    camera.panX = FixedMath.fromFloat(4.0f)
    camera.panZ = FixedMath.fromFloat(4.0f)
    camera.yaw = 45

    val panel = ViewerPanel(model, camera)
    frame.add(panel)
    frame.setSize(800, 600)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.setLocationRelativeTo(null)
    frame.isVisible = true

    // Animation Timer
    val timer = Timer(125) {
        for (sprite in model.sprites) {
            if (sprite.isWalking) {
                sprite.animationFrame++
            }
        }
        panel.repaint()
    }
    timer.start()
}
