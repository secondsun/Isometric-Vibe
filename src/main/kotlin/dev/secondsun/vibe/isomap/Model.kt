package dev.secondsun.vibe.isomap

import java.awt.Color

enum class TileType { CUBE, RAMP, PYRAMID }
enum class TextureType { NONE, WATER, WALL, LAVA, BRIDGE, WALL_2, ROAD, ROOF }
enum class RampDirection { NORTH, SOUTH, EAST, WEST }
enum class RampAngle(val degrees: Float) {
    ANGLE_15(15f),
    ANGLE_22_5(22.5f),
    ANGLE_45(45f)
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
    val texture: TextureType = TextureType.NONE
)

data class Polygon(
    val vertices: Array<Vector3>,
    val color: Color,
    val texture: TextureType = TextureType.NONE
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

class MapModel {
    val size = 8
    val tiles = Array(size) { Array(size) { Tile(TileType.CUBE, 0, Color.GRAY) } }
    val sprites = mutableListOf<Sprite>()

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
                tiles[i][j] = Tile(TileType.CUBE, 2, Color(0xDAA520), texture = TextureType.ROAD)
            }
        }

        // Center Pillar
        tiles[3][3] = Tile(TileType.CUBE, 5, Color.YELLOW, texture = TextureType.ROOF)
        tiles[4][3] = Tile(TileType.CUBE, 5, Color.YELLOW, texture = TextureType.ROOF)
        tiles[3][4] = Tile(TileType.CUBE, 5, Color.YELLOW, texture = TextureType.ROOF)
        tiles[4][4] = Tile(TileType.CUBE, 5, Color.YELLOW, texture = TextureType.ROOF)

        // Ramps pointing towards the center from all 4 cardinal directions
        // North (moving UP towards +Z)
        tiles[3][1] = Tile(TileType.RAMP, 2, Color(0xDAA520), RampDirection.NORTH, RampAngle.ANGLE_45, TextureType.ROAD)
        tiles[4][1] = Tile(TileType.RAMP, 2, Color(0xDAA520), RampDirection.NORTH, RampAngle.ANGLE_45, TextureType.ROAD)

        // South (moving UP towards -Z)
        tiles[3][6] = Tile(TileType.RAMP, 2, Color(0xDAA520), RampDirection.SOUTH, RampAngle.ANGLE_45, TextureType.ROAD)
        tiles[4][6] = Tile(TileType.RAMP, 2, Color(0xDAA520), RampDirection.SOUTH, RampAngle.ANGLE_45, TextureType.ROAD)

        // East (moving UP towards +X)
        tiles[1][3] = Tile(TileType.RAMP, 2, Color(0xDAA520), RampDirection.EAST, RampAngle.ANGLE_45, TextureType.ROAD)
        tiles[1][4] = Tile(TileType.RAMP, 2, Color(0xDAA520), RampDirection.EAST, RampAngle.ANGLE_45, TextureType.ROAD)

        // West (moving UP towards -X)
        tiles[6][3] = Tile(TileType.RAMP, 2, Color(0xDAA520), RampDirection.WEST, RampAngle.ANGLE_45, TextureType.ROAD)
        tiles[6][4] = Tile(TileType.RAMP, 2, Color(0xDAA520), RampDirection.WEST, RampAngle.ANGLE_45, TextureType.ROAD)

        // Decorative ramps at the corners of the platform
        tiles[2][2] = Tile(TileType.RAMP, 2, Color.RED, RampDirection.NORTH, RampAngle.ANGLE_22_5, TextureType.BRIDGE)
        tiles[5][2] = Tile(TileType.RAMP, 2, Color.RED, RampDirection.NORTH, RampAngle.ANGLE_22_5, TextureType.BRIDGE)
        tiles[2][5] = Tile(TileType.RAMP, 2, Color.BLUE, RampDirection.SOUTH, RampAngle.ANGLE_22_5, TextureType.BRIDGE)
        tiles[5][5] = Tile(TileType.RAMP, 2, Color.BLUE, RampDirection.SOUTH, RampAngle.ANGLE_22_5, TextureType.BRIDGE)

        // Corner water pits
        tiles[0][0] = Tile(TileType.CUBE, 0, Color.BLUE, texture = TextureType.WATER)
        tiles[7][0] = Tile(TileType.CUBE, 0, Color.BLUE, texture = TextureType.WATER)
        tiles[0][7] = Tile(TileType.CUBE, 0, Color.BLUE, texture = TextureType.WATER)
        tiles[7][7] = Tile(TileType.CUBE, 0, Color.BLUE, texture = TextureType.WATER)

        // Floating stones
        tiles[0][3] = Tile(TileType.CUBE, 4, Color.WHITE, texture = TextureType.LAVA) // Now lava stones!
        tiles[0][2] = Tile(TileType.RAMP, 4, Color.WHITE, RampDirection.NORTH, RampAngle.ANGLE_15, TextureType.LAVA)
        tiles[3][0] = Tile(TileType.CUBE, 4, Color.WHITE, texture = TextureType.LAVA)
        tiles[7][4] = Tile(TileType.CUBE, 4, Color.WHITE, texture = TextureType.LAVA)
        tiles[4][7] = Tile(TileType.CUBE, 4, Color.WHITE, texture = TextureType.LAVA)
        
        // Pyramids
        tiles[1][1] = Tile(TileType.PYRAMID, 1, Color.CYAN)
        tiles[6][1] = Tile(TileType.PYRAMID, 2, Color.MAGENTA)
        tiles[1][6] = Tile(TileType.PYRAMID, 3, Color.ORANGE)
        tiles[6][6] = Tile(TileType.PYRAMID, 4, Color.PINK)

        // Add some sprites
        sprites.add(Sprite(FixedMath.fromFloat(1.5f), FixedMath.fromFloat(1.0f), FixedMath.fromFloat(1.5f), SpriteDirection.S, isWalking = true))
    }

    fun generatePolygons(): List<Polygon> {
        val polygons = mutableListOf<Polygon>()
        for (x in 0 until size) {
            for (z in 0 until size) {
                val tile = tiles[x][z]
                when (tile.type) {
                    TileType.CUBE -> polygons.addAll(generateCubePolygons(x.toShort(), z.toShort(), tile.height, tile.color, tile.texture))
                    TileType.RAMP -> polygons.addAll(generateRampPolygons(x.toShort(), z.toShort(), tile))
                    TileType.PYRAMID -> polygons.addAll(generatePyramidPolygons(x.toShort(), z.toShort(), tile))
                }
            }
        }
        return polygons
    }

    fun generatePyramidPolygons(x: Short, z: Short, tile: Tile): List<Polygon> {
        val pyramidPolys = mutableListOf<Polygon>()
        val color = tile.color
        val texture = tile.texture

        // Base cube part (height - 1)
        if (tile.height > 1) {
            pyramidPolys.addAll(generateCubePolygons(x, z, (tile.height - 1).toShort(), color, texture))
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
        val sideTex = if (texture != TextureType.NONE && texture != TextureType.WATER && texture != TextureType.LAVA) {
            if (texture == TextureType.WALL || texture == TextureType.WALL_2) texture else TextureType.WALL
        } else TextureType.NONE

        // Back face
        pyramidPolys.add(Polygon(arrayOf(v[0], v[1], v[4]), color.darker(), sideTex))
        // Right face
        pyramidPolys.add(Polygon(arrayOf(v[1], v[2], v[4]), color.darker().darker(), sideTex))
        // Front face
        pyramidPolys.add(Polygon(arrayOf(v[2], v[3], v[4]), color.darker(), sideTex))
        // Left face
        pyramidPolys.add(Polygon(arrayOf(v[3], v[0], v[4]), color.darker().darker(), sideTex))

        return pyramidPolys
    }

    fun generateCubePolygons(x: Short, z: Short, h: Short, color: Color, texture: TextureType = TextureType.NONE): List<Polygon> {
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
        
        // Side texture: Use WALL for sides if textured, unless it's water/lava
        val sideTex = if (texture != TextureType.NONE && texture != TextureType.WATER && texture != TextureType.LAVA) {
             if (texture == TextureType.WALL || texture == TextureType.WALL_2) texture else TextureType.WALL
        } else TextureType.NONE

        // Top face
        cubePolys.add(Polygon(arrayOf(v[4], v[5], v[6], v[7]), color, texture))
        // Front face (towards increasing Z)
        cubePolys.add(Polygon(arrayOf(v[3], v[2], v[6], v[7]), color.darker(), sideTex))
        // Right face (towards increasing X)
        cubePolys.add(Polygon(arrayOf(v[1], v[2], v[6], v[5]), color.darker().darker(), sideTex))
        // Back face (towards decreasing Z)
        cubePolys.add(Polygon(arrayOf(v[0], v[1], v[5], v[4]), color.darker(), sideTex))
        // Left face (towards decreasing X)
        cubePolys.add(Polygon(arrayOf(v[0], v[3], v[7], v[4]), color.darker().darker(), sideTex))
        // Bottom face (usually not seen)
        // cubePolys.add(Polygon(arrayOf(v[0], v[1], v[2], v[3]), color))

        return cubePolys
    }

    fun generateRampPolygons(x: Short, z: Short, tile: Tile): List<Polygon> {
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
        val texture = tile.texture

        // Side texture
        val sideTex = if (texture != TextureType.NONE && texture != TextureType.WATER && texture != TextureType.LAVA) {
            if (texture == TextureType.WALL || texture == TextureType.WALL_2) texture else TextureType.WALL
        } else TextureType.NONE

        // Top face (the slope)
        rampPolys.add(Polygon(arrayOf(v[4], v[5], v[6], v[7]), color, texture))
        // Front face (towards increasing Z)
        rampPolys.add(Polygon(arrayOf(v[3], v[2], v[6], v[7]), color.darker(), sideTex))
        // Right face (towards increasing X)
        rampPolys.add(Polygon(arrayOf(v[1], v[2], v[6], v[5]), color.darker().darker(), sideTex))
        // Back face (towards decreasing Z)
        rampPolys.add(Polygon(arrayOf(v[0], v[1], v[5], v[4]), color.darker(), sideTex))
        // Left face (towards decreasing X)
        rampPolys.add(Polygon(arrayOf(v[0], v[3], v[7], v[4]), color.darker().darker(), sideTex))

        return rampPolys
    }
}
