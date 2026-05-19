package dev.secondsun.vibe.isomap

import java.awt.Graphics
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.awt.Color

class ProjectedSprite(val sprite: Sprite, val projectedPos: Vector3)
class ProjectedObject(val obj: PlacedObject, val polygons: List<Polygon>)

class Renderer {
    companion object {
        const val RENDER_WIDTH = 256
        const val RENDER_HEIGHT = 172
        const val TARGET_FPS = 30
    }
    private var softwareRenderer: SoftwareRenderer? = null
    private var image: BufferedImage? = null
    private var pixelData: IntArray? = null

    private var spriteRenderer: SoftwareRenderer? = null
    private var spriteImage: BufferedImage? = null
    private var spritePixelData: IntArray? = null

    fun render(g: Graphics, model: MapModel, camera: Camera, viewportWidth: Int, viewportHeight: Int) {
        if (softwareRenderer == null) {
            val img = BufferedImage(RENDER_WIDTH, RENDER_HEIGHT, BufferedImage.TYPE_INT_ARGB)
            val data = (img.raster.dataBuffer as DataBufferInt).data
            softwareRenderer = SoftwareRenderer(RENDER_WIDTH, RENDER_HEIGHT, data)
            image = img
            pixelData = data

            val sImg = BufferedImage(RENDER_WIDTH, RENDER_HEIGHT, BufferedImage.TYPE_INT_ARGB)
            val sData = (sImg.raster.dataBuffer as DataBufferInt).data
            spriteRenderer = SoftwareRenderer(RENDER_WIDTH, RENDER_HEIGHT, sData)
            spriteImage = sImg
            spritePixelData = sData
        }
        
        val sw = softwareRenderer!!
        val spriteSw = spriteRenderer!!
        
        sw.clear(0xFF000000.toInt()) // Solid black background for 3D render
        spriteSw.clear(0) // Transparent background for sprites

        val centerX = RENDER_WIDTH / 2
        val centerY = RENDER_HEIGHT / 2

        // Determine grid iteration order based on yaw for back-to-front rendering
        val yaw = ((camera.yaw % 360) + 360) % 360
        
        val xRange = if (yaw in 0 until 180) 0..7 else 7 downTo 0
        val zRange = if (yaw in 90 until 270) 0..7 else 7 downTo 0

        for (x in xRange) {
            for (z in zRange) {
                val tile = model.tiles[x][z]
                val hF = model.getColumnHeight(x, z + 1)
                val hR = model.getColumnHeight(x + 1, z)
                val hB = model.getColumnHeight(x, z - 1)
                val hL = model.getColumnHeight(x - 1, z)
                val tilePolys = when (tile.type) {
                    TileType.CUBE -> model.generateCubePolygons(x.toShort(), z.toShort(), tile.height, tile.color, tile.topTexture, tile.sideTexture, hF, hR, hB, hL)
                    TileType.RAMP -> model.generateRampPolygons(x.toShort(), z.toShort(), tile, hF, hR, hB, hL)
                    TileType.PYRAMID -> model.generatePyramidPolygons(x.toShort(), z.toShort(), tile, hF, hR, hB, hL)
                }
                
                // Find sprites in this cell
                val cellSprites = model.sprites.filter { s ->
                    val sx = s.x.toInt() shr FixedMath.SHIFT
                    val sz = s.z.toInt() shr FixedMath.SHIFT
                    sx == x && sz == z
                }

                // Find objects whose "base" tile is (x, z)
                // Base tile depends on camera yaw to ensure it's the "frontmost" one.
                val cellObjects = model.objects.filter { obj ->
                    // For now, let's use a simpler logic: an object is associated with its (x, z) coordinate.
                    // This works if we iterate in an order that respects the object's footprint.
                    // Since objects are immutable and we don't have complex interleaving, 
                    // rendering at (x, z) should be fine if (x, z) is the front-most tile of the object's footprint
                    // relative to the camera.
                    
                    val frontX = if (yaw in 0 until 180) obj.x + obj.widthInTiles - 1 else obj.x
                    val frontZ = if (yaw in 90 until 270) obj.z + obj.depthInTiles - 1 else obj.z
                    
                    frontX == x && frontZ == z
                }

                val renderables = mutableListOf<Any>()
                renderables.addAll(tilePolys.map { poly ->
                    val projVerts = poly.vertices.map { camera.project(it) }.toTypedArray()
                    val newPoly = Polygon(projVerts, poly.color, poly.texture, poly.uvs)
                    newPoly.calculateAverageZ()
                    newPoly
                })
                renderables.addAll(cellSprites.map { sprite ->
                    val projPos = camera.project(Vector3(sprite.x, sprite.y, sprite.z))
                    ProjectedSprite(sprite, projPos)
                })
                renderables.addAll(cellObjects.map { obj ->
                    val modelPolys = obj.model.rotatedPolygons[obj.orientation] ?: obj.model.polygons
                    val projectedPolys = modelPolys.map { poly ->
                        val worldVerts = poly.vertices.map { v ->
                            Vector3(
                                (v.x + (obj.x shl FixedMath.SHIFT)).toShort(),
                                (v.y + obj.y).toShort(),
                                (v.z + (obj.z shl FixedMath.SHIFT)).toShort()
                            )
                        }
                        val projVerts = worldVerts.map { camera.project(it) }.toTypedArray()
                        val newPoly = Polygon(projVerts, poly.color, poly.texture, poly.uvs)
                        newPoly.calculateAverageZ()
                        newPoly
                    }
                    ProjectedObject(obj, projectedPolys)
                })

                val sorted = renderables.flatMap { 
                    when (it) {
                        is Polygon -> listOf(it)
                        is ProjectedSprite -> listOf(it)
                        is ProjectedObject -> it.polygons // Add all polygons of the object
                        else -> emptyList()
                    }
                }.sortedByDescending { 
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
        
        // Calculate aspect ratio scaling
        val renderAspect = RENDER_WIDTH.toDouble() / RENDER_HEIGHT.toDouble()
        val viewportAspect = viewportWidth.toDouble() / viewportHeight.toDouble()

        var drawWidth = viewportWidth
        var drawHeight = viewportHeight
        var drawX = 0
        var drawY = 0

        if (viewportAspect > renderAspect) {
            // Viewport is wider than render resolution: pillarbox
            drawWidth = (viewportHeight * renderAspect).toInt()
            drawX = (viewportWidth - drawWidth) / 2
        } else {
            // Viewport is taller than render resolution: letterbox
            drawHeight = (viewportWidth / renderAspect).toInt()
            drawY = (viewportHeight - drawHeight) / 2
        }

        // Draw background (black) to fill the entire viewport
        g.color = Color.BLACK
        g.fillRect(0, 0, viewportWidth, viewportHeight)

        g.drawImage(spriteImage, drawX, drawY, drawWidth, drawHeight, null)
        g.drawImage(image, drawX, drawY, drawWidth, drawHeight, null)
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
            if (poly.uvs != null && poly.uvs.size == n) {
                for (i in 0 until n) {
                    uPoints[i] = poly.uvs[i].x
                    vPoints[i] = poly.uvs[i].y
                }
            } else {
                // Fallback or default UVs if not provided
                uPoints[0] = 0; vPoints[0] = 0
                uPoints[1] = 16 shl 8; vPoints[1] = 0
                if (n >= 4) {
                    uPoints[2] = 16 shl 8; vPoints[2] = 16 shl 8
                    uPoints[3] = 0; vPoints[3] = 16 shl 8
                } else {
                    uPoints[2] = 16 shl 8; vPoints[2] = 16 shl 8
                }
            }
            sw.drawTexturedPoly(xPoints, yPoints, uPoints, vPoints, n, texture)
        } else {
            sw.fillPolygon(xPoints, yPoints, n, poly.color.rgb)
        }
        
        for (i in 0 until n) {
            val j = (i + 1) % n
            //sw.drawLine(xPoints[i], yPoints[i], xPoints[j], yPoints[j], 0xFF000000.toInt())
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
