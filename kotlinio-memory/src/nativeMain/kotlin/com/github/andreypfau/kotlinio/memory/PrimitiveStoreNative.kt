package com.github.andreypfau.kotlinio.memory

import kotlinx.cinterop.*
import kotlin.experimental.and

actual inline fun Memory.storeShortAt(offset: Int, value: Short) {
    assertIndex(offset, 2)
    val pointer = pointer.plus(offset)!!

    if (Platform.canAccessUnaligned) {
        pointer.reinterpret<ShortVar>().pointed.value = value.toBigEndian()
    } else {
        storeShortSlowAt(pointer, value)
    }
}

actual inline fun Memory.storeShortAt(offset: Long, value: Short) {
    assertIndex(offset, 2)
    val pointer = pointer.plus(offset)!!

    if (Platform.canAccessUnaligned) {
        pointer.reinterpret<ShortVar>().pointed.value = value.toBigEndian()
    } else {
        storeShortSlowAt(pointer, value)
    }
}

actual inline fun Memory.storeIntAt(offset: Int, value: Int) {
    assertIndex(offset, 4)
    val pointer = pointer.plus(offset)!!

    if (Platform.canAccessUnaligned) {
        pointer.reinterpret<IntVar>().pointed.value = value.toBigEndian()
    } else {
        storeIntSlowAt(pointer, value)
    }
}

actual inline fun Memory.storeIntAt(offset: Long, value: Int) {
    assertIndex(offset, 4)
    val pointer = pointer.plus(offset)!!

    if (Platform.canAccessUnaligned) {
        pointer.reinterpret<IntVar>().pointed.value = value.toBigEndian()
    } else {
        storeIntSlowAt(pointer, value)
    }
}

actual inline fun Memory.storeLongAt(offset: Int, value: Long) {
    assertIndex(offset, 8)
    val pointer = pointer.plus(offset)!!

    if (Platform.canAccessUnaligned) {
        pointer.reinterpret<LongVar>().pointed.value = value.toBigEndian()
    } else {
        storeLongSlowAt(pointer, value)
    }
}

actual inline fun Memory.storeLongAt(offset: Long, value: Long) {
    assertIndex(offset, 8)
    val pointer = pointer.plus(offset)!!

    if (Platform.canAccessUnaligned) {
        pointer.reinterpret<LongVar>().pointed.value = value.toBigEndian()
    } else {
        storeLongSlowAt(pointer, value)
    }
}

actual inline fun Memory.storeFloatAt(offset: Int, value: Float) {
    assertIndex(offset, 4)
    val pointer = pointer.plus(offset)!!

    if (Platform.canAccessUnaligned) {
        pointer.reinterpret<FloatVar>().pointed.value = value.toBigEndian()
    } else {
        storeFloatSlowAt(pointer, value)
    }
}

actual inline fun Memory.storeFloatAt(offset: Long, value: Float) {
    assertIndex(offset, 4)
    val pointer = pointer.plus(offset)!!

    if (Platform.canAccessUnaligned) {
        pointer.reinterpret<FloatVar>().pointed.value = value.toBigEndian()
    } else {
        storeFloatSlowAt(pointer, value)
    }
}

actual inline fun Memory.storeDoubleAt(offset: Int, value: Double) {
    assertIndex(offset, 8)
    val pointer = pointer.plus(offset)!!

    if (Platform.canAccessUnaligned) {
        pointer.reinterpret<DoubleVar>().pointed.value = value.toBigEndian()
    } else {
        storeDoubleSlowAt(pointer, value)
    }
}

actual inline fun Memory.storeDoubleAt(offset: Long, value: Double) {
    assertIndex(offset, 8)
    val pointer = pointer.plus(offset)!!

    if (Platform.canAccessUnaligned) {
        pointer.reinterpret<DoubleVar>().pointed.value = value.toBigEndian()
    } else {
        storeDoubleSlowAt(pointer, value)
    }
}

@PublishedApi
internal inline fun storeShortSlowAt(pointer: CPointer<ByteVar>, value: Short) {
    pointer[0] = (value.toInt() ushr 8).toByte()
    pointer[1] = (value and 0xff).toByte()
}

@PublishedApi
internal inline fun storeIntSlowAt(pointer: CPointer<ByteVar>, value: Int) {
    pointer[0] = (value ushr 24).toByte()
    pointer[1] = (value ushr 16).toByte()
    pointer[2] = (value ushr 8).toByte()
    pointer[3] = (value and 0xff).toByte()
}

@PublishedApi
internal inline fun storeLongSlowAt(pointer: CPointer<ByteVar>, value: Long) {
    pointer[0] = (value ushr 56).toByte()
    pointer[1] = (value ushr 48).toByte()
    pointer[2] = (value ushr 40).toByte()
    pointer[3] = (value ushr 32).toByte()
    pointer[4] = (value ushr 24).toByte()
    pointer[5] = (value ushr 16).toByte()
    pointer[6] = (value ushr 8).toByte()
    pointer[7] = (value and 0xff).toByte()
}

@PublishedApi
internal inline fun storeFloatSlowAt(pointer: CPointer<ByteVar>, value: Float) {
    storeIntSlowAt(pointer, value.toRawBits())
}

@PublishedApi
internal inline fun storeDoubleSlowAt(pointer: CPointer<ByteVar>, value: Double) {
    storeLongSlowAt(pointer, value.toRawBits())
}
