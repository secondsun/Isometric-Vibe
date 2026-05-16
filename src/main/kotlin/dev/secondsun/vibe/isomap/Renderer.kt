package dev.secondsun.vibe.isomap

import java.awt.Graphics
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.awt.Color

class ProjectedSprite(val sprite: Sprite, val projectedPos: Vector3)

class Renderer {
    private var softwareRenderer: SoftwareRenderer? = null
    private var image: BufferedImage? = null
    private var pixelData: IntArray? = null

    private var spriteRenderer: SoftwareRenderer? = null
    private var spriteImage: BufferedImage? = null
    private var spritePixelData: IntArray? = null

    fun render(g: Graphics, model: MapModel, camera: Camera, width: Int, height: Int) {
        if (softwareRenderer == null || softwareRenderer!!.width != width || softwareRenderer!!.height != height) {
            val img = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
            val data = (img.raster.dataBuffer as DataBufferInt).data
            softwareRenderer = SoftwareRenderer(width, height, data)
            image = img
            pixelData = data

            val sImg = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
            val sData = (sImg.raster.dataBuffer as DataBufferInt).data
            spriteRenderer = SoftwareRenderer(width, height, sData)
            spriteImage = sImg
            spritePixelData = sData
        }
        
        val sw = softwareRenderer!!
        val spriteSw = spriteRenderer!!
        
        sw.clear(0xFF000000.toInt()) // Solid black background for 3D render
        spriteSw.clear(0) // Transparent background for sprites

        val centerX = width / 2
        val centerY = height / 2

        // Determine grid iteration order based on yaw for back-to-front rendering
        val yaw = ((camera.yaw % 360) + 360) % 360
        
        val xRange = if (yaw in 0 until 180) 0..7 else 7 downTo 0
        val zRange = if (yaw in 90 until 270) 0..7 else 7 downTo 0

        for (x in xRange) {
            for (z in zRange) {
                val tile = model.tiles[x][z]
                val tilePolys = when (tile.type) {
                    TileType.CUBE -> model.generateCubePolygons(x.toShort(), z.toShort(), tile.height, tile.color, tile.texture)
                    TileType.RAMP -> model.generateRampPolygons(x.toShort(), z.toShort(), tile)
                    TileType.PYRAMID -> model.generatePyramidPolygons(x.toShort(), z.toShort(), tile)
                }
                
                // Find sprites in this cell
                val cellSprites = model.sprites.filter { s ->
                    val sx = s.x.toInt() shr FixedMath.SHIFT
                    val sz = s.z.toInt() shr FixedMath.SHIFT
                    sx == x && sz == z
                }

                val renderables = mutableListOf<Any>()
                renderables.addAll(tilePolys.map { poly ->
                    val projVerts = poly.vertices.map { camera.project(it) }.toTypedArray()
                    val newPoly = Polygon(projVerts, poly.color, poly.texture)
                    newPoly.calculateAverageZ()
                    newPoly
                })
                renderables.addAll(cellSprites.map { sprite ->
                    val projPos = camera.project(Vector3(sprite.x, sprite.y, sprite.z))
                    ProjectedSprite(sprite, projPos)
                })

                val sorted = renderables.sortedByDescending { 
                    when (it) {
                        is Polygon -> it.averageZ
                        is ProjectedSprite -> it.projectedPos.z.toInt() - 2 // Offset slightly closer (smaller Z)
                        else -> 0
                    }
                }

                for (obj in sorted) {
                    if (obj is Polygon) {
                        renderPolygon(sw, obj, centerX, centerY)
                    } else if (obj is ProjectedSprite) {
                        renderSprite(sw, spriteSw, obj, camera.yaw, camera.zoom, centerX, centerY)
                    }
                }
            }
        }
        
        g.drawImage(spriteImage, 0, 0, null)
        g.drawImage(image, 0, 0, null)
    }

    private fun renderPolygon(sw: SoftwareRenderer, poly: Polygon, centerX: Int, centerY: Int) {
        val n = poly.vertices.size
        val xPoints = IntArray(n)
        val yPoints = IntArray(n)
        for (i in 0 until n) {
            val v = poly.vertices[i]
            xPoints[i] = centerX + v.x.toInt()
            yPoints[i] = centerY - v.y.toInt()
        }

        val texture = TextureLoader.getTexture(poly.texture)
        if (texture != null && n >= 3) {
            val uPoints = IntArray(n)
            val vPoints = IntArray(n)
            uPoints[0] = 0; vPoints[0] = 0
            uPoints[1] = 16 shl 8; vPoints[1] = 0
            if (n >= 4) {
                uPoints[2] = 16 shl 8; vPoints[2] = 16 shl 8
                uPoints[3] = 0; vPoints[3] = 16 shl 8
            } else {
                uPoints[2] = 16 shl 8; vPoints[2] = 16 shl 8
            }
            sw.drawTexturedPoly(xPoints, yPoints, uPoints, vPoints, n, texture)
        } else {
            sw.fillPolygon(xPoints, yPoints, n, poly.color.rgb)
        }
        
        for (i in 0 until n) {
            val j = (i + 1) % n
            sw.drawLine(xPoints[i], yPoints[i], xPoints[j], yPoints[j], 0xFF000000.toInt())
        }
    }

    private fun renderSprite(sw: SoftwareRenderer, spriteSw: SoftwareRenderer, ps: ProjectedSprite, cameraYaw: Int, zoom: Short, centerX: Int, centerY: Int) {
        // Calculate display direction based on camera yaw
        // User directions: sw, s, se, e, ne, n, nw, w
        // At yaw=45, we are looking from SW. If sprite faces SW, we see its front.
        // Let's simplify: the sprite direction in the sheet is its absolute world direction.
        // We need to offset which row we pick based on camera yaw.
        
        val yawOffset = ((cameraYaw + 22) / 45) % 8
        val displayDirIndex = (ps.sprite.direction.ordinal - yawOffset + 8) % 8
        val displayDir = SpriteDirection.values()[displayDirIndex]
        
        val fd = SpriteLoader.getFrame(displayDir, ps.sprite.isWalking, ps.sprite.animationFrame)
        if (fd != null) {
            val fw = SpriteLoader.frameWidth
            val fh = SpriteLoader.frameHeight
            
            // Scale the anchor point
            val sax = (fd.anchorX * zoom.toInt()) shr 8
            val say = (fd.anchorY * zoom.toInt()) shr 8
            
            // Align anchor point to the projected world position
            val sx = centerX + ps.projectedPos.x.toInt() - sax
            val sy = centerY - ps.projectedPos.y.toInt() - say
            
            sw.punchHole(sx, sy, fw, fh, fd.pixels)
            spriteSw.drawSprite(sx, sy, fw, fh, fd.pixels)
        }
    }
}
