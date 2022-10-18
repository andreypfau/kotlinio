package com.github.andreypfau.kotlinio.memory

actual inline fun Memory.storeShortAt(offset: Int, value: Short) {
    buffer.putShort(offset, value)
}

actual inline fun Memory.storeShortAt(offset: Long, value: Short) =
    storeShortAt(offset.toIntOrFail("offset"), value)

actual inline fun Memory.storeIntAt(offset: Int, value: Int) {
    buffer.putInt(offset, value)
}

actual inline fun Memory.storeIntAt(offset: Long, value: Int) =
    storeIntAt(offset.toIntOrFail("offset"), value)

actual inline fun Memory.storeLongAt(offset: Int, value: Long) {
    buffer.putLong(offset, value)
}

actual inline fun Memory.storeLongAt(offset: Long, value: Long) =
    storeLongAt(offset.toIntOrFail("offset"), value)

actual inline fun Memory.storeFloatAt(offset: Int, value: Float) {
    buffer.putFloat(offset, value)
}

actual inline fun Memory.storeFloatAt(offset: Long, value: Float) =
    storeFloatAt(offset.toIntOrFail("offset"), value)

actual inline fun Memory.storeDoubleAt(offset: Int, value: Double) {
    buffer.putDouble(offset, value)
}

actual inline fun Memory.storeDoubleAt(offset: Long, value: Double) =
    storeDoubleAt(offset.toIntOrFail("offset"), value)
