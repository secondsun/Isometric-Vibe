package dev.secondsun.vibe.isomap

import java.awt.image.BufferedImage
import javax.imageio.ImageIO

object TextureLoader {
    private val textures = mutableMapOf<TextureType, IntArray>()

    fun loadTextures() {
        val stream = javaClass.getResourceAsStream("/img.png")
        if (stream == null) {
            System.err.println("Warning: Texture map /img.png not found")
            return
        }
        val atlas = ImageIO.read(stream)
        
        // Row 1: water, wall, lava, bridge, wall_2, road, roof
        val textureList = listOf(
            TextureType.WATER, TextureType.WALL, TextureType.LAVA,
            TextureType.BRIDGE, TextureType.WALL_2, TextureType.ROAD, TextureType.ROOF
        )
        
        for (i in textureList.indices) {
            if (i * 16 + 16 <= atlas.width) {
                val img = atlas.getSubimage(i * 16, 0, 16, 16)
                val pixels = IntArray(256)
                img.getRGB(0, 0, 16, 16, pixels, 0, 16)
                textures[textureList[i]] = pixels
            }
        }
    }

    fun getTexture(type: TextureType): IntArray? = textures[type]
}
