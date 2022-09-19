@file:Suppress("NOTHING_TO_INLINE", "KotlinRedundantDiagnosticSuppress")

package com.github.andreypfau.kotlinio.bits

expect enum class ByteOrder {
    BIG_ENDIAN,
    LITTLE_ENDIAN;

    companion object {
        val native: ByteOrder
    }
}

expect fun Short.toBigEndian(): Short
expect fun Int.toBigEndian(): Int
expect fun Long.toBigEndian(): Long

expect fun Short.toLittleEndian(): Short
expect fun Int.toLittleEndian(): Int
expect fun Long.toLittleEndian(): Long

internal inline fun swap(s: Short): Short = (((s.toInt() and 0xff) shl 8) or ((s.toInt() and 0xffff) ushr 8)).toShort()

internal inline fun swap(s: Int): Int =
    (swap((s and 0xffff).toShort()).toInt() shl 16) or (swap((s ushr 16).toShort()).toInt() and 0xffff)

internal inline fun swap(s: Long): Long =
    (swap((s and 0xffffffff).toInt()).toLong() shl 32) or (swap((s ushr 32).toInt()).toLong() and 0xffffffff)
