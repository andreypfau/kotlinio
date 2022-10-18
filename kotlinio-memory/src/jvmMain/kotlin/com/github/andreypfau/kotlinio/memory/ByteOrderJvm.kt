@file:Suppress("NOTHING_TO_INLINE")

package com.github.andreypfau.kotlinio.memory

actual enum class ByteOrder(
    val java: java.nio.ByteOrder
) {
    BIG_ENDIAN(java.nio.ByteOrder.BIG_ENDIAN),
    LITTLE_ENDIAN(java.nio.ByteOrder.LITTLE_ENDIAN);

    actual companion object {
        actual val native: ByteOrder = orderOf(java.nio.ByteOrder.nativeOrder())

        @JvmStatic
        fun of(nioOrder: java.nio.ByteOrder): ByteOrder = orderOf(nioOrder)
    }
}

private fun orderOf(nioOrder: java.nio.ByteOrder): ByteOrder =
    if (nioOrder === java.nio.ByteOrder.BIG_ENDIAN) ByteOrder.BIG_ENDIAN else ByteOrder.LITTLE_ENDIAN

val java.nio.ByteOrder.kotlin get() = ByteOrder.of(this)

actual inline fun Short.reverseByteOrder(): Short = java.lang.Short.reverseBytes(this)
actual inline fun Int.reverseByteOrder(): Int = Integer.reverseBytes(this)
actual inline fun Long.reverseByteOrder(): Long = java.lang.Long.reverseBytes(this)
actual inline fun Float.reverseByteOrder(): Float =
    java.lang.Float.intBitsToFloat(Integer.reverseBytes(java.lang.Float.floatToRawIntBits(this)))
actual inline fun Double.reverseByteOrder(): Double =
    java.lang.Double.longBitsToDouble(java.lang.Long.reverseBytes(java.lang.Double.doubleToRawLongBits(this)))
