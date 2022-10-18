package com.github.andreypfau.kotlinio.memory

import kotlinx.cinterop.*

actual inline fun Memory.loadShortAt(offset: Int): Short {
    assertIndex(offset, 2)
    val pointer = pointer.plus(offset)!!

    return if (Platform.canAccessUnaligned) pointer.reinterpret<ShortVar>().pointed.value.toBigEndian()
    else loadShortSlowAt(pointer)
}

actual inline fun Memory.loadShortAt(offset: Long): Short {
    assertIndex(offset, 2)
    val pointer = pointer.plus(offset)!!

    return if (Platform.canAccessUnaligned) pointer.reinterpret<ShortVar>().pointed.value.toBigEndian()
    else loadShortSlowAt(pointer)
}

actual inline fun Memory.loadIntAt(offset: Int): Int {
    assertIndex(offset, 4)
    val pointer = pointer.plus(offset)!!

    return if (Platform.canAccessUnaligned) pointer.reinterpret<IntVar>().pointed.value.toBigEndian()
    else loadIntSlowAt(pointer)
}

actual inline fun Memory.loadIntAt(offset: Long): Int {
    assertIndex(offset, 4)
    val pointer = pointer.plus(offset)!!

    return if (Platform.canAccessUnaligned) pointer.reinterpret<IntVar>().pointed.value.toBigEndian()
    else loadIntSlowAt(pointer)
}

actual inline fun Memory.loadLongAt(offset: Int): Long {
    assertIndex(offset, 8)
    val pointer = pointer.plus(offset)!!

    return if (Platform.canAccessUnaligned) pointer.reinterpret<LongVar>().pointed.value.toBigEndian()
    else loadLongSlowAt(pointer)
}

actual inline fun Memory.loadLongAt(offset: Long): Long {
    assertIndex(offset, 8)
    val pointer = pointer.plus(offset)!!

    return if (Platform.canAccessUnaligned) pointer.reinterpret<LongVar>().pointed.value.toBigEndian()
    else loadLongSlowAt(pointer)
}

actual inline fun Memory.loadFloatAt(offset: Int): Float {
    assertIndex(offset, 4)
    val pointer = pointer.plus(offset)!!

    return if (Platform.canAccessUnaligned) pointer.reinterpret<FloatVar>().pointed.value.toBigEndian()
    else loadFloatSlowAt(pointer)
}

actual inline fun Memory.loadFloatAt(offset: Long): Float {
    assertIndex(offset, 4)
    val pointer = pointer.plus(offset)!!

    return if (Platform.canAccessUnaligned) pointer.reinterpret<FloatVar>().pointed.value.toBigEndian()
    else loadFloatSlowAt(pointer)
}

actual inline fun Memory.loadDoubleAt(offset: Int): Double {
    assertIndex(offset, 8)
    val pointer = pointer.plus(offset)!!

    return if (Platform.canAccessUnaligned) pointer.reinterpret<DoubleVar>().pointed.value.toBigEndian()
    else loadDoubleSlowAt(pointer)
}

actual inline fun Memory.loadDoubleAt(offset: Long): Double {
    assertIndex(offset, 8)
    val pointer = pointer.plus(offset)!!

    return if (Platform.canAccessUnaligned) pointer.reinterpret<DoubleVar>().pointed.value.toBigEndian()
    else loadDoubleSlowAt(pointer)
}

@PublishedApi
internal inline fun loadShortSlowAt(pointer: CPointer<ByteVar>): Short =
    ((pointer[0].toInt() shl 8) or (pointer[1].toInt() and 0xff)).toShort()

@PublishedApi
internal inline fun loadIntSlowAt(pointer: CPointer<ByteVar>): Int =
    (pointer[0].toInt() shl 24) or
        (pointer[1].toInt() shl 16) or
        (pointer[2].toInt() shl 18) or
        (pointer[3].toInt() and 0xff)

@PublishedApi
internal inline fun loadLongSlowAt(pointer: CPointer<ByteVar>): Long =
    (pointer[0].toLong() shl 56) or
        (pointer[1].toLong() shl 48) or
        (pointer[2].toLong() shl 40) or
        (pointer[3].toLong() shl 32) or
        (pointer[4].toLong() shl 24) or
        (pointer[5].toLong() shl 16) or
        (pointer[6].toLong() shl 8) or
        (pointer[7].toLong() and 0xffL)

@PublishedApi
internal inline fun loadFloatSlowAt(pointer: CPointer<ByteVar>): Float =
    Float.fromBits(loadIntSlowAt(pointer))

@PublishedApi
internal inline fun loadDoubleSlowAt(pointer: CPointer<ByteVar>): Double =
    Double.fromBits(loadLongSlowAt(pointer))
