package com.github.andreypfau.kotlinio.memory

actual fun Memory.loadShortArray(
    offset: Int,
    destination: ShortArray,
    destinationOffset: Int,
    count: Int
) {
    buffer.withOffset(offset).asShortBuffer().get(destination, destinationOffset, count)
}

actual fun Memory.loadShortArray(
    offset: Long,
    destination: ShortArray,
    destinationOffset: Int,
    count: Int
) = loadShortArray(offset.toIntOrFail("offset"), destination, destinationOffset, count)

actual fun Memory.loadIntArray(
    offset: Long,
    destination: IntArray,
    destinationOffset: Int,
    count: Int
) = loadIntArray(offset.toIntOrFail("offset"), destination, destinationOffset, count)

actual fun Memory.loadIntArray(
    offset: Int,
    destination: IntArray,
    destinationOffset: Int,
    count: Int
) {
    buffer.withOffset(offset).asIntBuffer().get(destination, destinationOffset, count)
}

actual fun Memory.loadLongArray(
    offset: Long,
    destination: LongArray,
    destinationOffset: Int,
    count: Int
) = loadLongArray(offset.toIntOrFail("offset"), destination, destinationOffset, count)

actual fun Memory.loadLongArray(
    offset: Int,
    destination: LongArray,
    destinationOffset: Int,
    count: Int
) {
    buffer.withOffset(offset).asLongBuffer().get(destination, destinationOffset, count)
}

actual fun Memory.loadFloatArray(
    offset: Long,
    destination: FloatArray,
    destinationOffset: Int,
    count: Int
) = loadFloatArray(offset.toIntOrFail("offset"), destination, destinationOffset, count)

actual fun Memory.loadFloatArray(
    offset: Int,
    destination: FloatArray,
    destinationOffset: Int,
    count: Int
) {
    buffer.withOffset(offset).asFloatBuffer().get(destination, destinationOffset, count)
}

actual fun Memory.loadDoubleArray(
    offset: Long,
    destination: DoubleArray,
    destinationOffset: Int,
    count: Int
) = loadDoubleArray(offset.toIntOrFail("offset"), destination, destinationOffset, count)

actual fun Memory.loadDoubleArray(
    offset: Int,
    destination: DoubleArray,
    destinationOffset: Int,
    count: Int
) {
    buffer.withOffset(offset).asDoubleBuffer().get(destination, destinationOffset, count)
}
