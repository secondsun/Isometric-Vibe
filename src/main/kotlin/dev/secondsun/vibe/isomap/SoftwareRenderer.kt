package dev.secondsun.vibe.isomap

class SoftwareRenderer(val width: Int, val height: Int, val pixels: IntArray = IntArray(width * height)) {

    val bitmask = ByteArray(height * ((width + 7) shr 3))

    fun clear(color: Int) {
        pixels.fill(color)
    }

    fun clearMask() {
        bitmask.fill(0)
    }

    private fun isPixelSet(x: Int, y: Int): Boolean {
        val byteIdx = y * ((width + 7) shr 3) + (x shr 3)
        val bitIdx = 7 - (x and 7) // MSB is left-most, LSB is right-most
        return (bitmask[byteIdx].toInt() and (1 shl bitIdx)) != 0
    }

    private fun setPixelBit(x: Int, y: Int) {
        val byteIdx = y * ((width + 7) shr 3) + (x shr 3)
        val bitIdx = 7 - (x and 7)
        bitmask[byteIdx] = (bitmask[byteIdx].toInt() or (1 shl bitIdx)).toByte()
    }

    fun setPixel(x: Int, y: Int, color: Int) {
        if (x in 0 until width && y in 0 until height) {
            if (!isPixelSet(x, y)) {
                pixels[y * width + x] = color
                setPixelBit(x, y)
            }
        }
    }

    fun drawLine(x0: Int, y0: Int, x1: Int, y1: Int, color: Int) {
        var x = x0
        var y = y0
        val dx = Math.abs(x1 - x0)
        val dy = Math.abs(y1 - y0)
        val sx = if (x0 < x1) 1 else -1
        val sy = if (y0 < y1) 1 else -1
        var err = dx - dy

        while (true) {
            setPixel(x, y, color)
            if (x == x1 && y == y1) break
            val e2 = 2 * err
            if (e2 > -dy) {
                err -= dy
                x += sx
            }
            if (e2 < dx) {
                err += dx
                y += sy
            }
        }
    }

    fun fillPolygon(xPoints: IntArray, yPoints: IntArray, nPoints: Int, color: Int) {
        if (nPoints < 3) return

        var minY = yPoints[0]
        var maxY = yPoints[0]
        for (i in 1 until nPoints) {
            if (yPoints[i] < minY) minY = yPoints[i]
            if (yPoints[i] > maxY) maxY = yPoints[i]
        }

        minY = Math.max(0, minY)
        maxY = Math.min(height - 1, maxY)

        for (y in minY..maxY) {
            var minX = Int.MAX_VALUE
            var maxX = Int.MIN_VALUE

            for (i in 0 until nPoints) {
                val j = (i + 1) % nPoints
                val y0 = yPoints[i]
                val y1 = yPoints[j]
                val x0 = xPoints[i]
                val x1 = xPoints[j]

                if ((y0 <= y && y < y1) || (y1 <= y && y < y0)) {
                    val x = x0 + (y - y0) * (x1 - x0) / (y1 - y0)
                    if (x < minX) minX = x
                    if (x > maxX) maxX = x
                }
            }

            if (minX <= maxX) {
                val startX = Math.max(0, minX)
                val endX = Math.min(width - 1, maxX)
                for (x in startX..endX) {
                    if (!isPixelSet(x, y)) {
                        pixels[y * width + x] = color
                        setPixelBit(x, y)
                    }
                }
            }
        }
    }

    fun drawTexturedPoly(
        xPoints: IntArray, yPoints: IntArray,
        uPoints: IntArray, vPoints: IntArray,
        nPoints: Int, texture: IntArray
    ) {
        if (nPoints < 3) return

        var minY = yPoints[0]
        var maxY = yPoints[0]
        for (i in 1 until nPoints) {
            if (yPoints[i] < minY) minY = yPoints[i]
            if (yPoints[i] > maxY) maxY = yPoints[i]
        }

        minY = Math.max(0, minY)
        maxY = Math.min(height - 1, maxY)

        for (y in minY..maxY) {
            var minX = Int.MAX_VALUE
            var maxX = Int.MIN_VALUE
            var minU = 0
            var maxU = 0
            var minV = 0
            var maxV = 0

            for (i in 0 until nPoints) {
                val j = (i + 1) % nPoints
                val y0 = yPoints[i]
                val y1 = yPoints[j]

                if ((y0 <= y && y < y1) || (y1 <= y && y < y0)) {
                    val dy = y1 - y0
                    // Using Int for intermediate calculation to avoid overflow before shift
                    val f = ((y - y0) shl 8) / dy

                    val x = xPoints[i] + (((xPoints[j] - xPoints[i]) * f) shr 8)
                    val u = uPoints[i] + (((uPoints[j] - uPoints[i]) * f) shr 8)
                    val v = vPoints[i] + (((vPoints[j] - vPoints[i]) * f) shr 8)

                    if (x < minX) {
                        minX = x
                        minU = u
                        minV = v
                    }
                    if (x > maxX) {
                        maxX = x
                        maxU = u
                        maxV = v
                    }
                }
            }

            if (minX < maxX) {
                val startX = Math.max(0, minX)
                val endX = Math.min(width - 1, maxX)
                val dx = maxX - minX

                for (x in startX..endX) {
                    if (isPixelSet(x, y)) continue

                    val f = ((x - minX) shl 8) / dx
                    val u = (minU + (((maxU - minU) * f) shr 8))
                    val v = (minV + (((maxV - minV) * f) shr 8))

        // u, v are Q8.8, texture is 16x16
                    val tu = (u shr 8) and 15
                    val tv = (v shr 8) and 15
                    pixels[y * width + x] = texture[tv * 16 + tu]
                    setPixelBit(x, y)
                }
            } else if (minX == maxX && minX in 0 until width) {
                if (!isPixelSet(minX, y)) {
                    val tu = (minU shr 8) and 15
                    val tv = (minV shr 8) and 15
                    pixels[y * width + minX] = texture[tv * 16 + tu]
                    setPixelBit(minX, y)
                }
            }
        }
    }

    fun drawSprite(xStart: Int, yStart: Int, spriteWidth: Int, spriteHeight: Int, spritePixels: IntArray, scale: Int = 256) {
        if (scale == 256) {
            // Fast path for 1:1 scale
            for (sy in 0 until spriteHeight) {
                val py = yStart + sy
                if (py < 0 || py >= height) continue
                for (sx in 0 until spriteWidth) {
                    val px = xStart + sx
                    if (px < 0 || px >= width) continue
                    if (isPixelSet(px, py)) continue

                    val spritePixel = spritePixels[sy * spriteWidth + sx]
                    if ((spritePixel shr 24) != 0) {
                        pixels[py * width + px] = spritePixel
                        setPixelBit(px, py)
                    }
                }
            }
        } else {
            val sw = (spriteWidth * scale) shr 8
            val sh = (spriteHeight * scale) shr 8
            if (sw <= 0 || sh <= 0) return

            for (dy in 0 until sh) {
                val py = yStart + dy
                if (py < 0 || py >= height) continue
                val sy = (dy shl 8) / scale
                if (sy >= spriteHeight) continue

                for (dx in 0 until sw) {
                    val px = xStart + dx
                    if (px < 0 || px >= width) continue
                    if (isPixelSet(px, py)) continue

                    val sx = (dx shl 8) / scale
                    if (sx >= spriteWidth) continue

                    val spritePixel = spritePixels[sy * spriteWidth + sx]
                    if ((spritePixel shr 24) != 0) {
                        pixels[py * width + px] = spritePixel
                        setPixelBit(px, py)
                    }
                }
            }
        }
    }

    fun punchHole(xStart: Int, yStart: Int, spriteWidth: Int, spriteHeight: Int, spritePixels: IntArray, scale: Int = 256) {
        if (scale == 256) {
            for (sy in 0 until spriteHeight) {
                val py = yStart + sy
                if (py < 0 || py >= height) continue
                for (sx in 0 until spriteWidth) {
                    val px = xStart + sx
                    if (px < 0 || px >= width) continue
                    // punchHole ignores bitmask because it's meant to CLEAR pixels
                    // But if it's meant to clear them, should it also clear the bitmask?
                    // "It should also be checked before a pixel is written and skip the write if it is set."
                    // If we are punching a hole, we are writing a pixel (transparent).
                    // If the bitmask is already set, we skip it. This seems correct for front-to-back.
                    if (isPixelSet(px, py)) continue

                    val spritePixel = spritePixels[sy * spriteWidth + sx]
                    if ((spritePixel shr 24) != 0) {
                        pixels[py * width + px] = 0 // Fully transparent
                        setPixelBit(px, py)
                    }
                }
            }
        } else {
            val sw = (spriteWidth * scale) shr 8
            val sh = (spriteHeight * scale) shr 8
            if (sw <= 0 || sh <= 0) return

            for (dy in 0 until sh) {
                val py = yStart + dy
                if (py < 0 || py >= height) continue
                val sy = (dy shl 8) / scale
                if (sy >= spriteHeight) continue

                for (dx in 0 until sw) {
                    val px = xStart + dx
                    if (px < 0 || px >= width) continue
                    if (isPixelSet(px, py)) continue

                    val sx = (dx shl 8) / scale
                    if (sx >= spriteWidth) continue

                    val spritePixel = spritePixels[sy * spriteWidth + sx]
                    if ((spritePixel shr 24) != 0) {
                        pixels[py * width + px] = 0
                        setPixelBit(px, py)
                    }
                }
            }
        }
    }
}
