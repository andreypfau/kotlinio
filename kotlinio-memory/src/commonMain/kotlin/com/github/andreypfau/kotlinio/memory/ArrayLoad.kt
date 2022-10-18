@file:Suppress("UseExpressionBody", "OPT_IN_USAGE")

package com.github.andreypfau.kotlinio.memory

inline fun Memory.loadByteArray(
    offset: Int,
    destination: ByteArray,
    destinationOffset: Int = 0,
    count: Int = destination.size - destinationOffset
) = copyTo(destination, offset, count, destinationOffset)

inline fun Memory.loadByteArray(
    offset: Long,
    destination: ByteArray,
    destinationOffset: Int = 0,
    count: Int = destination.size - destinationOffset
) = copyTo(destination, offset, count, destinationOffset)

inline fun Memory.loadUByteArray(
    offset: Int,
    destination: UByteArray,
    destinationOffset: Int = 0,
    count: Int = destination.size - destinationOffset
) = loadByteArray(offset, destination.toByteArray(), destinationOffset, count)

inline fun Memory.loadUByteArray(
    offset: Long,
    destination: UByteArray,
    destinationOffset: Int = 0,
    count: Int = destination.size - destinationOffset
) = loadByteArray(offset, destination.toByteArray(), destinationOffset, count)

expect fun Memory.loadShortArray(
    offset: Int,
    destination: ShortArray,
    destinationOffset: Int = 0,
    count: Int = destination.size - destinationOffset
)

expect fun Memory.loadShortArray(
    offset: Long,
    destination: ShortArray,
    destinationOffset: Int = 0,
    count: Int = destination.size - destinationOffset
)

inline fun Memory.loadUShortArray(
    offset: Int,
    destination: UShortArray,
    destinationOffset: Int = 0,
    count: Int = destination.size - destinationOffset
) = loadShortArray(offset, destination.toShortArray(), destinationOffset, count)

inline fun Memory.loadUShortArray(
    offset: Long,
    destination: UShortArray,
    destinationOffset: Int = 0,
    count: Int = destination.size - destinationOffset
) = loadShortArray(offset, destination.toShortArray(), destinationOffset, count)

expect fun Memory.loadIntArray(
    offset: Int,
    destination: IntArray,
    destinationOffset: Int = 0,
    count: Int = destination.size - destinationOffset
)

expect fun Memory.loadIntArray(
    offset: Long,
    destination: IntArray,
    destinationOffset: Int = 0,
    count: Int = destination.size - destinationOffset
)

inline fun Memory.loadUIntArray(
    offset: Int,
    destination: UIntArray,
    destinationOffset: Int = 0,
    count: Int = destination.size - destinationOffset
) = loadIntArray(offset, destination.toIntArray(), destinationOffset, count)

inline fun Memory.loadUIntArray(
    offset: Long,
    destination: UIntArray,
    destinationOffset: Int = 0,
    count: Int = destination.size - destinationOffset
) = loadIntArray(offset, destination.toIntArray(), destinationOffset, count)

expect fun Memory.loadLongArray(
    offset: Int,
    destination: LongArray,
    destinationOffset: Int = 0,
    count: Int = destination.size - destinationOffset
)

expect fun Memory.loadLongArray(
    offset: Long,
    destination: LongArray,
    destinationOffset: Int = 0,
    count: Int = destination.size - destinationOffset
)

inline fun Memory.loadULongArray(
    offset: Int,
    destination: ULongArray,
    destinationOffset: Int = 0,
    count: Int = destination.size - destinationOffset
) = loadLongArray(offset, destination.toLongArray(), destinationOffset, count)

inline fun Memory.loadULongArray(
    offset: Long,
    destination: ULongArray,
    destinationOffset: Int = 0,
    count: Int = destination.size - destinationOffset
) = loadLongArray(offset, destination.toLongArray(), destinationOffset, count)

expect fun Memory.loadFloatArray(
    offset: Int,
    destination: FloatArray,
    destinationOffset: Int = 0,
    count: Int = destination.size - destinationOffset
)

expect fun Memory.loadFloatArray(
    offset: Long,
    destination: FloatArray,
    destinationOffset: Int = 0,
    count: Int = destination.size - destinationOffset
)

expect fun Memory.loadDoubleArray(
    offset: Int,
    destination: DoubleArray,
    destinationOffset: Int = 0,
    count: Int = destination.size - destinationOffset
)

expect fun Memory.loadDoubleArray(
    offset: Long,
    destination: DoubleArray,
    destinationOffset: Int = 0,
    count: Int = destination.size - destinationOffset
)
