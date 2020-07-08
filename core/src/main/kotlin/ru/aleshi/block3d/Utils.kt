package ru.aleshi.block3d

/**
 * Returns clamped value between [min] and [max]
 */
fun Float.clamp(min: Float, max: Float): Float {
    if (this < min) return min
    if (this > max) return max
    return this
}

/**
 * Repeats value over range.
 *
 * For ex. 361f.repeat(360f) == 1f, (-1f).repeat(360f) == 359f
 */
fun Float.repeat(range: Float): Float = if (this < 0f) this % range + range else this % range
