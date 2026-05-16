package dev.secondsun.vibe.isomap

import kotlin.math.*

object FixedMath {
    const val SHIFT = 8
    const val ONE: Short = (1 shl SHIFT).toShort()
    const val HALF: Short = (1 shl (SHIFT - 1)).toShort()

    fun fromFloat(f: Float): Short = (f * ONE).toInt().toShort()
    
    fun toFloat(s: Short): Float = s.toFloat() / ONE

    fun multiply(a: Short, b: Short): Short {
        val res = (a.toInt() * b.toInt()) shr SHIFT
        return res.toShort()
    }

    fun divide(a: Short, b: Short): Short {
        if (b.toInt() == 0) return 32767
        val res = (a.toInt() shl SHIFT) / b.toInt()
        return res.toShort()
    }

    // Sine table or Math.sin wrapper for Q8.8
    // Since we can use built-in math for trig
    fun sin(degrees: Int): Short {
        return fromFloat(kotlin.math.sin(Math.toRadians(degrees.toDouble())).toFloat())
    }

    fun cos(degrees: Int): Short {
        return fromFloat(kotlin.math.cos(Math.toRadians(degrees.toDouble())).toFloat())
    }

    fun tan(degrees: Float): Short {
        return fromFloat(kotlin.math.tan(Math.toRadians(degrees.toDouble())).toFloat())
    }
}

data class Vector3(val x: Short, val y: Short, val z: Short) {
    companion object {
        fun fromFloat(x: Float, y: Float, z: Float) = Vector3(
            FixedMath.fromFloat(x),
            FixedMath.fromFloat(y),
            FixedMath.fromFloat(z)
        )
    }
}

data class Vector2(val x: Int, val y: Int) // Screen coordinates, usually Int
