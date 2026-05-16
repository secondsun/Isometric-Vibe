package dev.secondsun.vibe.isomap

import java.awt.image.BufferedImage
import javax.imageio.ImageIO

object SpriteLoader {
    private val frames = mutableMapOf<Int, IntArray>() // key = row * 8 + col
    private val anchorX = mutableMapOf<Int, Int>()
    private val anchorY = mutableMapOf<Int, Int>()
    var frameWidth: Int = 0
    var frameHeight: Int = 0

    fun loadSprites() {
        val stream = javaClass.getResourceAsStream("/helix_full_sheet2.png")
        if (stream == null) {
            System.err.println("Warning: Sprite sheet /helix_full_sheet2.png not found")
            return
        }
        val atlas = ImageIO.read(stream)
        val sheetWidth = atlas.width
        val sheetHeight = atlas.height
        frameWidth = sheetWidth / 8
        frameHeight = sheetHeight / 16
        
        for (row in 0 until 16) {
            val cols = if (row < 8) 1 else 8
            for (col in 0 until cols) {
                val img = atlas.getSubimage(col * frameWidth, row * frameHeight, frameWidth, frameHeight)
                val pixels = IntArray(frameWidth * frameHeight)
                img.getRGB(0, 0, frameWidth, frameHeight, pixels, 0, frameWidth)
                val key = row * 8 + col
                frames[key] = pixels
                
                // Calculate anchor (bottom center of non-transparent pixels)
                var minX = frameWidth
                var maxX = -1
                var maxY = -1
                for (y in 0 until frameHeight) {
                    for (x in 0 until frameWidth) {
                        val argb = pixels[y * frameWidth + x]
                        if ((argb shr 24) != 0) {
                            if (x < minX) minX = x
                            if (x > maxX) maxX = x
                            if (y > maxY) maxY = y
                        }
                    }
                }
                if (maxX != -1) {
                    anchorX[key] = (minX + maxX) / 2
                    anchorY[key] = maxY
                } else {
                    anchorX[key] = frameWidth / 2
                    anchorY[key] = frameHeight - 1
                }
            }
        }
    }

    data class FrameData(val pixels: IntArray, val anchorX: Int, val anchorY: Int)

    fun getFrame(direction: SpriteDirection, isWalking: Boolean, frame: Int): FrameData? {
        val row = if (isWalking) direction.ordinal + 8 else direction.ordinal
        val col = if (isWalking) frame % 8 else 0
        val key = row * 8 + col
        
        // Use anchor from the standing frame (column 0) of the corresponding direction
        val anchorKey = (direction.ordinal) * 8
        
        val p = frames[key] ?: frames[row * 8] ?: return null
        val ax = anchorX[anchorKey] ?: (frameWidth / 2)
        val ay = anchorY[anchorKey] ?: (frameHeight - 1)
        return FrameData(p, ax, ay)
    }
}
