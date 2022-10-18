package com.github.andreypfau.kotlinio.memory

actual fun Memory.storeShortArray(
    offset: Int,
    source: ShortArray,
    sourceOffset: Int,
    count: Int
) {
    buffer.withOffset(offset).asShortBuffer().put(source, sourceOffset, count)
}

actual fun Memory.storeShortArray(
    offset: Long,
    source: ShortArray,
    sourceOffset: Int,
    count: Int
) = storeShortArray(offset.toIntOrFail("offset"), source, sourceOffset, count)

actual fun Memory.storeIntArray(
    offset: Int,
    source: IntArray,
    sourceOffset: Int,
    count: Int
) {
    buffer.withOffset(offset).asIntBuffer().put(source, sourceOffset, count)
}

actual fun Memory.storeIntArray(
    offset: Long,
    source: IntArray,
    sourceOffset: Int,
    count: Int
) = storeIntArray(offset.toIntOrFail("offset"), source, sourceOffset, count)

actual fun Memory.storeLongArray(
    offset: Int,
    source: LongArray,
    sourceOffset: Int,
    count: Int
) {
    buffer.withOffset(offset).asLongBuffer().put(source, sourceOffset, count)
}

actual fun Memory.storeLongArray(
    offset: Long,
    source: LongArray,
    sourceOffset: Int,
    count: Int
) = storeLongArray(offset.toIntOrFail("offset"), source, sourceOffset, count)

actual fun Memory.storeFloatArray(
    offset: Int,
    source: FloatArray,
    sourceOffset: Int,
    count: Int
) {
    buffer.withOffset(offset).asFloatBuffer().put(source, sourceOffset, count)
}

actual fun Memory.storeFloatArray(
    offset: Long,
    source: FloatArray,
    sourceOffset: Int,
    count: Int
) = storeFloatArray(offset.toIntOrFail("offset"), source, sourceOffset, count)

actual fun Memory.storeDoubleArray(
    offset: Int,
    source: DoubleArray,
    sourceOffset: Int,
    count: Int
) {
    buffer.withOffset(offset).asDoubleBuffer().put(source, sourceOffset, count)
}

actual fun Memory.storeDoubleArray(
    offset: Long,
    source: DoubleArray,
    sourceOffset: Int,
    count: Int
) = storeDoubleArray(offset.toIntOrFail("offset"), source, sourceOffset, count)
