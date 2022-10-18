@file:Suppress("NOTHING_TO_INLINE")

package com.github.andreypfau.kotlinio.memory

actual inline fun Memory.storeShortAt(offset: Int, value: Short) =
    view.setInt16(offset, value, false)

actual inline fun Memory.storeShortAt(offset: Long, value: Short) =
    storeShortAt(offset.toIntOrFail("offset"), value)

actual inline fun Memory.storeIntAt(offset: Int, value: Int) =
    view.setInt32(offset, value, false)

actual inline fun Memory.storeIntAt(offset: Long, value: Int) =
    storeIntAt(offset.toIntOrFail("offset"), value)

actual inline fun Memory.storeLongAt(offset: Int, value: Long) {
    view.setUint32(offset, (value ushr 32).toInt(), false)
    view.setUint32(offset + 4, value.toInt(), false)
}

actual inline fun Memory.storeLongAt(offset: Long, value: Long) =
    storeLongAt(offset.toIntOrFail("offset"), value)

actual inline fun Memory.storeFloatAt(offset: Int, value: Float) =
    view.setFloat32(offset, value, false)

actual inline fun Memory.storeFloatAt(offset: Long, value: Float) =
    storeFloatAt(offset.toIntOrFail("offset"), value)

actual inline fun Memory.storeDoubleAt(offset: Int, value: Double) =
    view.setFloat64(offset, value, false)

actual inline fun Memory.storeDoubleAt(offset: Long, value: Double) =
    storeDoubleAt(offset.toIntOrFail("offset"), value)
