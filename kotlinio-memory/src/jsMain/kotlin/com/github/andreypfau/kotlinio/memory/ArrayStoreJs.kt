package com.github.andreypfau.kotlinio.memory

import org.khronos.webgl.*

actual fun Memory.storeShortArray(
    offset: Int,
    source: ShortArray,
    sourceOffset: Int,
    count: Int
) {
    val typed = Int16Array(view.buffer, view.byteOffset + offset, count)

    if (isLittleEndianPlatform) {
        // TODO investigate this implementation vs DataView.getInt16(...)
        repeat(count) { index ->
            typed[index] = source[index + sourceOffset].reverseByteOrder()
        }
    } else {
        repeat(count) { index ->
            typed[index] = source[index + sourceOffset]
        }
    }
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
    val typed = Int32Array(view.buffer, view.byteOffset + offset, count)

    if (isLittleEndianPlatform) {
        // TODO investigate this implementation vs DataView.getInt32(...)
        repeat(count) { index ->
            typed[index] = source[index + sourceOffset].reverseByteOrder()
        }
    } else {
        repeat(count) { index ->
            typed[index] = source[index + sourceOffset]
        }
    }
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
    val typed = Int32Array(view.buffer, view.byteOffset + offset, count * 2)

    if (isLittleEndianPlatform) {
        for (index in 0 until count * 2 step 2) {
            val sourceIndex = index / 2 + sourceOffset
            val sourceValue = source[sourceIndex]
            typed[index] = (sourceValue ushr 32).toInt().reverseByteOrder()
            typed[index + 1] = (sourceValue and 0xffffffffL).toInt().reverseByteOrder()
        }
    } else {
        for (index in 0 until count * 2 step 2) {
            val sourceIndex = index / 2 + sourceOffset
            val sourceValue = source[sourceIndex]
            typed[index] = (sourceValue ushr 32).toInt()
            typed[index + 1] = (sourceValue and 0xffffffffL).toInt()
        }
    }
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
    val typed = Float32Array(view.buffer, view.byteOffset + offset, count)

    if (isLittleEndianPlatform) {
        repeat(count) { index ->
            typed[index] = source[index + sourceOffset].reverseByteOrder()
        }
    } else {
        repeat(count) { index ->
            typed[index] = source[index + sourceOffset]
        }
    }
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
    val typed = Float64Array(view.buffer, view.byteOffset + offset, count)

    if (isLittleEndianPlatform) {
        repeat(count) { index ->
            typed[index] = source[index + sourceOffset].reverseByteOrder()
        }
    } else {
        repeat(count) { index ->
            typed[index] = source[index + sourceOffset]
        }
    }
}

actual fun Memory.storeDoubleArray(
    offset: Long,
    source: DoubleArray,
    sourceOffset: Int,
    count: Int
) = storeDoubleArray(offset.toIntOrFail("offset"), source, sourceOffset, count)
