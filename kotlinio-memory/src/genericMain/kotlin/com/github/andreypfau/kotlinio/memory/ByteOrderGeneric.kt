package com.github.andreypfau.kotlinio.memory

internal inline fun Short.reverseByteOrderGeneric(): Short =
    (((this.toInt() and 0xff) shl 8) or ((this.toInt() and 0xffff) ushr 8)).toShort()

internal inline fun Int.reverseByteOrderGeneric(): Int =
    (((this and 0xffff).toShort()).reverseByteOrder().toInt() shl 16) or (((this ushr 16).toShort()).reverseByteOrder()
        .toInt() and 0xffff)

internal inline fun Long.reverseByteOrderGeneric(): Long =
    (((this and 0xffffffff).toInt()).reverseByteOrder().toLong() shl 32) or (((this ushr 32).toInt()).reverseByteOrder()
        .toLong() and 0xffffffff)

internal inline fun Float.reverseByteOrderGeneric(): Float = toRawBits().reverseByteOrder().toFloat()

internal inline fun Double.reverseByteOrderGeneric(): Double = toRawBits().reverseByteOrder().toDouble()
