package dev.secondsun.vibe.isomap

import java.awt.Color

enum class TileType { CUBE, RAMP, PYRAMID }
enum class TextureType { NONE, WATER, WALL, LAVA, BRIDGE, WALL_2, ROAD, ROOF, WOOD }
enum class RampDirection { NORTH, SOUTH, EAST, WEST }
enum class RampAngle(val degrees: Float) {
    ANGLE_15(15f),
    ANGLE_22_5(22.5f),
    ANGLE_45(45f)
}

enum class Orientation {
    DEG_0, DEG_90, DEG_180, DEG_270
}

enum class SpriteDirection { SW, S, SE, E, NE, N, NW, W }

data class Sprite(
    var x: Short, // World position in Q8.8
    var y: Short,
    var z: Short,
    var direction: SpriteDirection = SpriteDirection.S,
    var isWalking: Boolean = false,
    var animationFrame: Int = 0
)

data class Tile(
    val type: TileType,
    val height: Short,
    val color: Color,
    val rampDirection: RampDirection = RampDirection.NORTH,
    val rampAngle: RampAngle = RampAngle.ANGLE_45,
    val topTexture: TextureType = TextureType.NONE,
    val sideTexture: TextureType = TextureType.NONE
)

data class Polygon(
    val vertices: Array<Vector3>,
    val color: Color,
    val texture: TextureType = TextureType.NONE,
    val uvs: Array<Vector2>? = null
) {
    // For Painter's Algorithm
    var averageZ: Int = 0

    fun calculateAverageZ() {
        var sum = 0
        for (v in vertices) {
            sum += v.z.toInt()
        }
        averageZ = sum / vertices.size
    }
}

class StaticModel(val polygons: List<Polygon>) {
    val rotatedPolygons = mutableMapOf<Orientation, List<Polygon>>()

    init {
        rotatedPolygons[Orientation.DEG_0] = polygons
        rotatedPolygons[Orientation.DEG_90] = rotate(Orientation.DEG_90)
        rotatedPolygons[Orientation.DEG_180] = rotate(Orientation.DEG_180)
        rotatedPolygons[Orientation.DEG_270] = rotate(Orientation.DEG_270)
    }

    private fun rotate(orientation: Orientation): List<Polygon> {
        return polygons.map { poly ->
            val newVertices = poly.vertices.map { v ->
                when (orientation) {
                    Orientation.DEG_90 -> Vector3(v.z, v.y, (-v.x).toShort())
                    Orientation.DEG_180 -> Vector3((-v.x).toShort(), v.y, (-v.z).toShort())
                    Orientation.DEG_270 -> Vector3((-v.z).toShort(), v.y, v.x)
                    else -> v
                }
            }.toTypedArray()
            Polygon(newVertices, poly.color, poly.texture, poly.uvs)
        }
    }
}

data class PlacedObject(
    val model: StaticModel,
    val x: Int, // Tile X
    val z: Int, // Tile Z
    val y: Short, // Height in Q8.8
    val orientation: Orientation = Orientation.DEG_0,
    val widthInTiles: Int = 1,
    val depthInTiles: Int = 1
)

class MapModel {
    val size = 8
    val tiles = Array(size) { Array(size) { Tile(TileType.CUBE, 0, Color.GRAY) } }
    val sprites = mutableListOf<Sprite>()
    val objects = mutableListOf<PlacedObject>()

    init {
        // Base ground: Dark Green
        for (i in 0 until size) {
            for (j in 0 until size) {
                tiles[i][j] = Tile(TileType.CUBE, 1, Color(0x2d5a27))
            }
        }

        // Central platform
        for (i in 2..5) {
            for (j in 2..5) {
                tiles[i][j] = Tile(TileType.CUBE, 2, Color(0xDAA520), topTexture = TextureType.ROAD, sideTexture = TextureType.WALL)
            }
        }

        // Center Pillar
        tiles[3][3] = Tile(TileType.CUBE, 5, Color.YELLOW, topTexture = TextureType.ROOF, sideTexture = TextureType.WALL_2)
        tiles[4][3] = Tile(TileType.CUBE, 5, Color.YELLOW, topTexture = TextureType.ROOF, sideTexture = TextureType.WALL_2)
        tiles[3][4] = Tile(TileType.CUBE, 5, Color.YELLOW, topTexture = TextureType.ROOF, sideTexture = TextureType.WALL_2)
        tiles[4][4] = Tile(TileType.CUBE, 5, Color.YELLOW, topTexture = TextureType.ROOF, sideTexture = TextureType.WALL_2)

        // Ramps pointing towards the center from all 4 cardinal directions
        // North (moving UP towards +Z)
        tiles[3][1] = Tile(TileType.RAMP, 2, Color(0xDAA520), RampDirection.EAST, RampAngle.ANGLE_45, topTexture = TextureType.ROAD, sideTexture = TextureType.WALL)
        tiles[4][1] = Tile(TileType.RAMP, 2, Color(0xDAA520), RampDirection.NORTH, RampAngle.ANGLE_45, topTexture = TextureType.ROAD, sideTexture = TextureType.WALL)

        // South (moving UP towards -Z)
        tiles[3][6] = Tile(TileType.RAMP, 2, Color(0xDAA520), RampDirection.SOUTH, RampAngle.ANGLE_45, topTexture = TextureType.ROAD, sideTexture = TextureType.WALL)
        tiles[4][6] = Tile(TileType.RAMP, 2, Color(0xDAA520), RampDirection.SOUTH, RampAngle.ANGLE_45, topTexture = TextureType.ROAD, sideTexture = TextureType.WALL)

        // East (moving UP towards +X)
        tiles[1][3] = Tile(TileType.RAMP, 2, Color(0xDAA520), RampDirection.EAST, RampAngle.ANGLE_45, topTexture = TextureType.ROAD, sideTexture = TextureType.WALL)
        tiles[1][4] = Tile(TileType.RAMP, 2, Color(0xDAA520), RampDirection.EAST, RampAngle.ANGLE_45, topTexture = TextureType.ROAD, sideTexture = TextureType.WALL)

        // West (moving UP towards -X)
        tiles[6][3] = Tile(TileType.RAMP, 2, Color(0xDAA520), RampDirection.WEST, RampAngle.ANGLE_45, topTexture = TextureType.ROAD, sideTexture = TextureType.WALL)
        tiles[6][4] = Tile(TileType.RAMP, 2, Color(0xDAA520), RampDirection.WEST, RampAngle.ANGLE_45, topTexture = TextureType.ROAD, sideTexture = TextureType.WALL)

        // Decorative ramps at the corners of the platform
        tiles[2][2] = Tile(TileType.RAMP, 2, Color.RED, RampDirection.NORTH, RampAngle.ANGLE_22_5, topTexture = TextureType.BRIDGE, sideTexture = TextureType.WALL)
        tiles[5][2] = Tile(TileType.RAMP, 2, Color.RED, RampDirection.NORTH, RampAngle.ANGLE_22_5, topTexture = TextureType.BRIDGE, sideTexture = TextureType.WALL)
        tiles[2][5] = Tile(TileType.RAMP, 2, Color.BLUE, RampDirection.SOUTH, RampAngle.ANGLE_22_5, topTexture = TextureType.BRIDGE, sideTexture = TextureType.WALL)
        tiles[5][5] = Tile(TileType.RAMP, 2, Color.BLUE, RampDirection.SOUTH, RampAngle.ANGLE_22_5, topTexture = TextureType.BRIDGE, sideTexture = TextureType.WALL)

        // Corner water pits
        tiles[0][0] = Tile(TileType.CUBE, 0, Color.BLUE, topTexture = TextureType.WATER)
        tiles[7][0] = Tile(TileType.CUBE, 0, Color.BLUE, topTexture = TextureType.WATER)
        tiles[0][7] = Tile(TileType.CUBE, 0, Color.BLUE, topTexture = TextureType.WATER)
        tiles[7][7] = Tile(TileType.CUBE, 0, Color.BLUE, topTexture = TextureType.WATER)

        // Floating stones
        tiles[0][3] = Tile(TileType.CUBE, 2, Color.WHITE, topTexture = TextureType.LAVA, sideTexture = TextureType.LAVA) // Now lava stones!
        tiles[0][2] = Tile(TileType.RAMP, 4, Color.WHITE, RampDirection.NORTH, RampAngle.ANGLE_15, topTexture = TextureType.LAVA, sideTexture = TextureType.LAVA)
        tiles[3][0] = Tile(TileType.CUBE, 2, Color.WHITE, topTexture = TextureType.LAVA, sideTexture = TextureType.LAVA)
        tiles[7][4] = Tile(TileType.CUBE, 2, Color.WHITE, topTexture = TextureType.LAVA, sideTexture = TextureType.LAVA)
        tiles[4][7] = Tile(TileType.CUBE, 2, Color.WHITE, topTexture = TextureType.LAVA, sideTexture = TextureType.LAVA)
        
        // Pyramids
        tiles[1][1] = Tile(TileType.PYRAMID, 1, Color.CYAN, topTexture = TextureType.ROOF, sideTexture = TextureType.WALL)
        tiles[6][1] = Tile(TileType.PYRAMID, 2, Color.MAGENTA, topTexture = TextureType.ROOF, sideTexture = TextureType.WALL)
        tiles[1][6] = Tile(TileType.PYRAMID, 3, Color.ORANGE, topTexture = TextureType.ROOF, sideTexture = TextureType.WALL)
        tiles[6][6] = Tile(TileType.PYRAMID, 4, Color.PINK, topTexture = TextureType.ROOF, sideTexture = TextureType.WALL)

        // Add some sprites
        sprites.add(Sprite(FixedMath.fromFloat(1.5f), FixedMath.fromFloat(2.0f), FixedMath.fromFloat(1.5f), SpriteDirection.S, isWalking = true))

        // Example: Add a 3x1 object
        val benchPolys = mutableListOf<Polygon>()
        // Simple bench mesh
        // Seat
        benchPolys.add(Polygon(arrayOf(
            Vector3.fromFloat(0.1f, 0.5f, 0.2f),
            Vector3.fromFloat(2.9f, 0.5f, 0.2f),
            Vector3.fromFloat(2.9f, 0.5f, 0.8f),
            Vector3.fromFloat(0.1f, 0.5f, 0.8f)
        ), Color(0x8B4513), texture = TextureType.BRIDGE, uvs = arrayOf(
            Vector2(0, 0), Vector2(16 shl 8, 0), Vector2(16 shl 8, 16 shl 8), Vector2(0, 16 shl 8)
        )))
        // Backrest (Vertical)
        benchPolys.add(Polygon(arrayOf(
            Vector3.fromFloat(0.1f, 0.5f, 0.8f),
            Vector3.fromFloat(2.9f, 0.5f, 0.8f),
            Vector3.fromFloat(2.9f, 1.5f, 0.8f),
            Vector3.fromFloat(0.1f, 1.5f, 0.8f)
        ), Color(0x8B4513), texture = TextureType.BRIDGE, uvs = arrayOf(
            Vector2(0, 0), Vector2(16 shl 8, 0), Vector2(16 shl 8, 16 shl 8), Vector2(0, 16 shl 8)
        )))

        // Legs
        val legWidth = 0.2f
        val legHeight = 0.5f
        val legPositions = listOf(
            Pair(0.1f, 0.2f), // Front Left
            Pair(2.7f, 0.2f), // Front Right
            Pair(2.7f, 0.6f), // Back Right
            Pair(0.1f, 0.6f)  // Back Left
        )

        for (pos in legPositions) {
            val lx = pos.first
            val lz = pos.second
            // Front face of leg
            benchPolys.add(Polygon(arrayOf(
                Vector3.fromFloat(lx, 0f, lz + legWidth),
                Vector3.fromFloat(lx + legWidth, 0f, lz + legWidth),
                Vector3.fromFloat(lx + legWidth, legHeight, lz + legWidth),
                Vector3.fromFloat(lx, legHeight, lz + legWidth)
            ), Color.GRAY, texture = TextureType.WALL, uvs = arrayOf(
                Vector2(0, 0), Vector2(16 shl 8, 0), Vector2(16 shl 8, 16 shl 8), Vector2(0, 16 shl 8)
            )))
            // Side face of leg
            benchPolys.add(Polygon(arrayOf(
                Vector3.fromFloat(lx + legWidth, 0f, lz),
                Vector3.fromFloat(lx + legWidth, 0f, lz + legWidth),
                Vector3.fromFloat(lx + legWidth, legHeight, lz + legWidth),
                Vector3.fromFloat(lx + legWidth, legHeight, lz)
            ), Color.GRAY, texture = TextureType.WALL, uvs = arrayOf(
                Vector2(0, 0), Vector2(16 shl 8, 0), Vector2(16 shl 8, 16 shl 8), Vector2(0, 16 shl 8)
            )))
        }
        
        val benchModel = StaticModel(benchPolys)
        objects.add(PlacedObject(benchModel, 2, 0, FixedMath.fromFloat(2.0f), widthInTiles = 3, depthInTiles = 1))
        
        // Add another one rotated
        objects.add(PlacedObject(benchModel, 5, 2, FixedMath.fromFloat(2.0f), orientation = Orientation.DEG_90, widthInTiles = 1, depthInTiles = 3))
    }

    fun getColumnHeight(x: Int, z: Int): Int {
        if (x !in 0 until size || z !in 0 until size) return 0
        val tile = tiles[x][z]
        return when (tile.type) {
            TileType.CUBE -> tile.height.toInt()
            TileType.PYRAMID, TileType.RAMP -> (tile.height - 1).coerceAtLeast(0).toInt()
        }
    }

    fun generatePolygons(): List<Polygon> {
        val polygons = mutableListOf<Polygon>()
        for (x in 0 until size) {
            for (z in 0 until size) {
                val tile = tiles[x][z]
                val hFront = getColumnHeight(x, z + 1)
                val hRight = getColumnHeight(x + 1, z)
                val hBack = getColumnHeight(x, z - 1)
                val hLeft = getColumnHeight(x - 1, z)
                when (tile.type) {
                    TileType.CUBE -> polygons.addAll(generateCubePolygons(x.toShort(), z.toShort(), tile.height, tile.color, tile.topTexture, tile.sideTexture, hFront, hRight, hBack, hLeft))
                    TileType.RAMP -> polygons.addAll(generateRampPolygons(x.toShort(), z.toShort(), tile, hFront, hRight, hBack, hLeft))
                    TileType.PYRAMID -> polygons.addAll(generatePyramidPolygons(x.toShort(), z.toShort(), tile, hFront, hRight, hBack, hLeft))
                }
            }
        }
        return polygons
    }

    fun generatePyramidPolygons(x: Short, z: Short, tile: Tile, hFront: Int = 0, hRight: Int = 0, hBack: Int = 0, hLeft: Int = 0): List<Polygon> {
        val pyramidPolys = mutableListOf<Polygon>()
        val color = tile.color

        // Base cube part (height - 1)
        if (tile.height > 1) {
            pyramidPolys.addAll(generateCubePolygons(x, z, (tile.height - 1).toShort(), color, tile.sideTexture, tile.sideTexture, hFront, hRight, hBack, hLeft))
        }

        val x0 = (x * FixedMath.ONE).toShort()
        val x1 = ((x + 1) * FixedMath.ONE).toShort()
        val z0 = (z * FixedMath.ONE).toShort()
        val z1 = ((z + 1) * FixedMath.ONE).toShort()
        val y0 = ((tile.height - 1) * FixedMath.ONE).toShort()
        val y1 = (tile.height * FixedMath.ONE).toShort()
        val centerX = (x0 + x1) / 2
        val centerZ = (z0 + z1) / 2

        val v = arrayOf(
            Vector3(x0, y0, z0), // 0: Bottom-back-left
            Vector3(x1, y0, z0), // 1: Bottom-back-right
            Vector3(x1, y0, z1), // 2: Bottom-front-right
            Vector3(x0, y0, z1), // 3: Bottom-front-left
            Vector3(centerX.toShort(), y1, centerZ.toShort()) // 4: Top apex
        )

        // Side texture
        val sideTex = tile.sideTexture

        // Back face
        pyramidPolys.add(Polygon(arrayOf(v[0], v[1], v[4]), color.darker(), tile.topTexture, 
            arrayOf(Vector2(0, 0), Vector2(16 shl 8, 0), Vector2(8 shl 8, 16 shl 8))))
        // Right face
        pyramidPolys.add(Polygon(arrayOf(v[1], v[2], v[4]), color.darker().darker(), tile.topTexture,
            arrayOf(Vector2(0, 0), Vector2(16 shl 8, 0), Vector2(8 shl 8, 16 shl 8))))
        // Front face
        pyramidPolys.add(Polygon(arrayOf(v[2], v[3], v[4]), color.darker(), tile.topTexture,
            arrayOf(Vector2(0, 0), Vector2(16 shl 8, 0), Vector2(8 shl 8, 16 shl 8))))
        // Left face
        pyramidPolys.add(Polygon(arrayOf(v[3], v[0], v[4]), color.darker().darker(), tile.topTexture,
            arrayOf(Vector2(0, 0), Vector2(16 shl 8, 0), Vector2(8 shl 8, 16 shl 8))))

        return pyramidPolys
    }

    fun generateCubePolygons(x: Short, z: Short, h: Short, color: Color, topTexture: TextureType = TextureType.NONE, sideTexture: TextureType = TextureType.NONE, hFront: Int = 0, hRight: Int = 0, hBack: Int = 0, hLeft: Int = 0): List<Polygon> {
        // Cube vertices
        // Base is at y=0 (or ground level)
        // Height is h.
        
        val x0 = (x * FixedMath.ONE).toShort()
        val x1 = ((x + 1) * FixedMath.ONE).toShort()
        val z0 = (z * FixedMath.ONE).toShort() // z grid to z coord
        val z1 = ((z + 1) * FixedMath.ONE).toShort()
        val y0: Short = 0
        val y1 = (h * FixedMath.ONE).toShort()

        val v = arrayOf(
            Vector3(x0, y0, z0), // 0
            Vector3(x1, y0, z0), // 1
            Vector3(x1, y0, z1), // 2
            Vector3(x0, y0, z1), // 3
            Vector3(x0, y1, z0), // 4
            Vector3(x1, y1, z0), // 5
            Vector3(x1, y1, z1), // 6
            Vector3(x0, y1, z1)  // 7
        )

        val cubePolys = mutableListOf<Polygon>()

        // Top face
        cubePolys.add(Polygon(arrayOf(v[4], v[5], v[6], v[7]), color, topTexture,
            arrayOf(Vector2(0, 0), Vector2(16 shl 8, 0), Vector2(16 shl 8, 16 shl 8), Vector2(0, 16 shl 8))))
        // Front face (towards increasing Z)
        if (hFront < h) {
            val yL = (hFront * FixedMath.ONE).toShort()
            cubePolys.add(Polygon(arrayOf(Vector3(x0, yL, z1), Vector3(x1, yL, z1), v[6], v[7]), color.darker(), sideTexture,
                arrayOf(Vector2(0, hFront shl 12), Vector2(16 shl 8, hFront shl 12), Vector2(16 shl 8, h.toInt() shl 12), Vector2(0, h.toInt() shl 12))))
        }
        // Right face (towards increasing X)
        if (hRight < h) {
            val yL = (hRight * FixedMath.ONE).toShort()
            cubePolys.add(Polygon(arrayOf(Vector3(x1, yL, z0), Vector3(x1, yL, z1), v[6], v[5]), color.darker().darker(), sideTexture,
                arrayOf(Vector2(0, hRight shl 12), Vector2(16 shl 8, hRight shl 12), Vector2(16 shl 8, h.toInt() shl 12), Vector2(0, h.toInt() shl 12))))
        }
        // Back face (towards decreasing Z)
        if (hBack < h) {
            val yL = (hBack * FixedMath.ONE).toShort()
            cubePolys.add(Polygon(arrayOf(Vector3(x0, yL, z0), Vector3(x1, yL, z0), v[5], v[4]), color.darker(), sideTexture,
                arrayOf(Vector2(0, hBack shl 12), Vector2(16 shl 8, hBack shl 12), Vector2(16 shl 8, h.toInt() shl 12), Vector2(0, h.toInt() shl 12))))
        }
        // Left face (towards decreasing X)
        if (hLeft < h) {
            val yL = (hLeft * FixedMath.ONE).toShort()
            cubePolys.add(Polygon(arrayOf(Vector3(x0, yL, z0), Vector3(x0, yL, z1), v[7], v[4]), color.darker().darker(), sideTexture,
                arrayOf(Vector2(0, hLeft shl 12), Vector2(16 shl 8, hLeft shl 12), Vector2(16 shl 8, h.toInt() shl 12), Vector2(0, h.toInt() shl 12))))
        }
        // Bottom face (usually not seen)
        // cubePolys.add(Polygon(arrayOf(v[0], v[1], v[2], v[3]), color))

        return cubePolys
    }

    fun generateRampPolygons(x: Short, z: Short, tile: Tile, hFront: Int = 0, hRight: Int = 0, hBack: Int = 0, hLeft: Int = 0): List<Polygon> {
        val x0 = (x * FixedMath.ONE).toShort()
        val x1 = ((x + 1) * FixedMath.ONE).toShort()
        val z0 = (z * FixedMath.ONE).toShort()
        val z1 = ((z + 1) * FixedMath.ONE).toShort()
        val y0: Short = 0
        val y1 = (tile.height * FixedMath.ONE).toShort()

        val drop = FixedMath.tan(tile.rampAngle.degrees)

        // Vertices 0-3 are base at y=0
        // Vertices 4-7 are top vertices
        val topY = ShortArray(4) { y1 }

        when (tile.rampDirection) {
            RampDirection.NORTH -> { // Up towards +Z. High side at z1 (6,7). Low side at z0 (4,5)
                topY[0] = (y1 - drop).coerceAtLeast(0).toShort() // v[4]
                topY[1] = (y1 - drop).coerceAtLeast(0).toShort() // v[5]
            }
            RampDirection.SOUTH -> { // Up towards -Z. High side at z0 (4,5). Low side at z1 (6,7)
                topY[2] = (y1 - drop).coerceAtLeast(0).toShort() // v[6]
                topY[3] = (y1 - drop).coerceAtLeast(0).toShort() // v[7]
            }
            RampDirection.EAST -> { // Up towards +X. High side at x1 (5,6). Low side at x0 (4,7)
                topY[0] = (y1 - drop).coerceAtLeast(0).toShort() // v[4]
                topY[3] = (y1 - drop).coerceAtLeast(0).toShort() // v[7]
            }
            RampDirection.WEST -> { // Up towards -X. High side at x0 (4,7). Low side at x1 (5,6)
                topY[1] = (y1 - drop).coerceAtLeast(0).toShort() // v[5]
                topY[2] = (y1 - drop).coerceAtLeast(0).toShort() // v[6]
            }
        }

        val v = arrayOf(
            Vector3(x0, y0, z0), // 0
            Vector3(x1, y0, z0), // 1
            Vector3(x1, y0, z1), // 2
            Vector3(x0, y0, z1), // 3
            Vector3(x0, topY[0], z0), // 4
            Vector3(x1, topY[1], z0), // 5
            Vector3(x1, topY[2], z1), // 6
            Vector3(x0, topY[3], z1)  // 7
        )

        val rampPolys = mutableListOf<Polygon>()
        val color = tile.color

        // Top face (the slope)
        rampPolys.add(Polygon(arrayOf(v[4], v[5], v[6], v[7]), color, tile.topTexture,
            arrayOf(Vector2(0, 0), Vector2(16 shl 8, 0), Vector2(16 shl 8, 16 shl 8), Vector2(0, 16 shl 8))))
        // Front face (towards increasing Z)
        if (hFront < tile.height) {
            val yL = (hFront * FixedMath.ONE).toShort()
            if (v[6].y > yL || v[7].y > yL) {
                val v3_c = Vector3(x0, yL.coerceAtMost(v[7].y), z1)
                val v2_c = Vector3(x1, yL.coerceAtMost(v[6].y), z1)
                rampPolys.add(Polygon(arrayOf(v3_c, v2_c, v[6], v[7]), color.darker(), tile.sideTexture,
                    arrayOf(Vector2(0, v3_c.y.toInt() shl 4), Vector2(16 shl 8, v2_c.y.toInt() shl 4), Vector2(16 shl 8, v[6].y.toInt() shl 4), Vector2(0, v[7].y.toInt() shl 4))))
            }
        }
        // Right face (towards increasing X)
        if (hRight < tile.height) {
            val yL = (hRight * FixedMath.ONE).toShort()
            if (v[6].y > yL || v[5].y > yL) {
                val v1_c = Vector3(x1, yL.coerceAtMost(v[5].y), z0)
                val v2_c = Vector3(x1, yL.coerceAtMost(v[6].y), z1)
                rampPolys.add(Polygon(arrayOf(v1_c, v2_c, v[6], v[5]), color.darker().darker(), tile.sideTexture,
                    arrayOf(Vector2(0, v1_c.y.toInt() shl 4), Vector2(16 shl 8, v2_c.y.toInt() shl 4), Vector2(16 shl 8, v[6].y.toInt() shl 4), Vector2(0, v[5].y.toInt() shl 4))))
            }
        }
        // Back face (towards decreasing Z)
        if (hBack < tile.height) {
            val yL = (hBack * FixedMath.ONE).toShort()
            if (v[5].y > yL || v[4].y > yL) {
                val v0_c = Vector3(x0, yL.coerceAtMost(v[4].y), z0)
                val v1_c = Vector3(x1, yL.coerceAtMost(v[5].y), z0)
                rampPolys.add(Polygon(arrayOf(v0_c, v1_c, v[5], v[4]), color.darker(), tile.sideTexture,
                    arrayOf(Vector2(0, v0_c.y.toInt() shl 4), Vector2(16 shl 8, v1_c.y.toInt() shl 4), Vector2(16 shl 8, v[5].y.toInt() shl 4), Vector2(0, v[4].y.toInt() shl 4))))
            }
        }
        // Left face (towards decreasing X)
        if (hLeft < tile.height) {
            val yL = (hLeft * FixedMath.ONE).toShort()
            if (v[7].y > yL || v[4].y > yL) {
                val v0_c = Vector3(x0, yL.coerceAtMost(v[4].y), z0)
                val v3_c = Vector3(x0, yL.coerceAtMost(v[7].y), z1)
                rampPolys.add(Polygon(arrayOf(v0_c, v3_c, v[7], v[4]), color.darker().darker(), tile.sideTexture,
                    arrayOf(Vector2(0, v0_c.y.toInt() shl 4), Vector2(16 shl 8, v3_c.y.toInt() shl 4), Vector2(16 shl 8, v[7].y.toInt() shl 4), Vector2(0, v[4].y.toInt() shl 4))))
            }
        }

        return rampPolys
    }
}
