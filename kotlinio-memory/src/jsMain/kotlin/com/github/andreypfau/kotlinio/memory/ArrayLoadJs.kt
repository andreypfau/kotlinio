package com.github.andreypfau.kotlinio.memory

import org.khronos.webgl.*

actual fun Memory.loadShortArray(
    offset: Long,
    destination: ShortArray,
    destinationOffset: Int,
    count: Int
) = loadShortArray(offset.toIntOrFail("offset"), destination, destinationOffset, count)

actual fun Memory.loadShortArray(
    offset: Int,
    destination: ShortArray,
    destinationOffset: Int,
    count: Int
) {
    val typed = Int16Array(view.buffer, view.byteOffset + offset, count)

    if (isLittleEndianPlatform) {
        repeat(count) { index ->
            destination[index + destinationOffset] = typed[index].reverseByteOrder()
        }
    } else {
        repeat(count) { index ->
            destination[index + destinationOffset] = typed[index]
        }
    }
}

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
    val typed = Int32Array(view.buffer, view.byteOffset + offset, count)

    if (isLittleEndianPlatform) {
        repeat(count) { index ->
            destination[index + destinationOffset] = typed[index].reverseByteOrder()
        }
    } else {
        repeat(count) { index ->
            destination[index + destinationOffset] = typed[index]
        }
    }
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
    val typed = Int32Array(view.buffer, view.byteOffset + offset, count * 2)

    if (isLittleEndianPlatform) {
        repeat(count) { index ->
            destination[index + destinationOffset] = typed[index * 2].toLong() or (typed[index * 2 + 1].toLong() shl 32)
        }
    } else {
        repeat(count) { index ->
            destination[index + destinationOffset] = typed[index * 2 + 1].toLong() or (typed[index * 2].toLong() shl 32)
        }
    }
}

actual fun Memory.loadFloatArray(
    offset: Int,
    destination: FloatArray,
    destinationOffset: Int,
    count: Int
) {
    val typed = Float32Array(view.buffer, view.byteOffset + offset, count)

    if (isLittleEndianPlatform) {
        repeat(count) { index ->
            destination[index + destinationOffset] = typed[index].reverseByteOrder()
        }
    } else {
        repeat(count) { index ->
            destination[index + destinationOffset] = typed[index]
        }
    }
}

actual fun Memory.loadFloatArray(
    offset: Long,
    destination: FloatArray,
    destinationOffset: Int,
    count: Int
) = loadFloatArray(offset.toIntOrFail("offset"), destination, destinationOffset, count)

actual fun Memory.loadDoubleArray(
    offset: Int,
    destination: DoubleArray,
    destinationOffset: Int,
    count: Int
) {
    val typed = Float64Array(view.buffer, view.byteOffset + offset, count)

    if (isLittleEndianPlatform) {
        repeat(count) { index ->
            destination[index + destinationOffset] = typed[index].reverseByteOrder()
        }
    } else {
        repeat(count) { index ->
            destination[index + destinationOffset] = typed[index]
        }
    }
}

actual fun Memory.loadDoubleArray(
    offset: Long,
    destination: DoubleArray,
    destinationOffset: Int,
    count: Int
) = loadDoubleArray(offset.toIntOrFail("offset"), destination, destinationOffset, count)
