package dev.secondsun.vibe.isomap

class Camera {
    var yaw: Int = 45 // degrees
    var zoom: Short = FixedMath.fromFloat(1.0f)
    var panX: Short = FixedMath.fromFloat(4.0f)
    var panZ: Short = FixedMath.fromFloat(4.0f)

    private val SIN45 = FixedMath.sin(45)
    private val COS45 = FixedMath.cos(45)

    fun project(v: Vector3): Vector3 {
        // 1. Translate relative to focal point
        var x = (v.x - panX).toShort()
        var y = v.y
        var z = (v.z - panZ).toShort()

        // 2. Rotate around Y (Yaw)
        val sY = FixedMath.sin(yaw)
        val cY = FixedMath.cos(yaw)
        
        val x1 = (FixedMath.multiply(x, cY) + FixedMath.multiply(z, sY)).toShort()
        val z1 = (-FixedMath.multiply(x, sY) + FixedMath.multiply(z, cY)).toShort()
        
        // 3. Pitch 45 degrees around X
        // y'' = y * cos45 + z1 * sin45
        // z'' = -y * sin45 + z1 * cos45
        val y2 = (FixedMath.multiply(y, COS45) + FixedMath.multiply(z1, SIN45)).toShort()
        val z2 = (-FixedMath.multiply(y, SIN45) + FixedMath.multiply(z1, COS45)).toShort()
        
        // 4. Zoom
        val x3 = FixedMath.multiply(x1, zoom)
        val y3 = FixedMath.multiply(y2, zoom)
        val z3 = FixedMath.multiply(z2, zoom)
        
        return Vector3(x3, y3, z3)
    }
}
