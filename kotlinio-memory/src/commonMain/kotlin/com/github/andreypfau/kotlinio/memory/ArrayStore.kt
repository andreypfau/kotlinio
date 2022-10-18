@file:Suppress("OPT_IN_USAGE")

package com.github.andreypfau.kotlinio.memory

inline fun Memory.storeByteArray(
    offset: Int,
    source: ByteArray,
    sourceOffset: Int = 0,
    count: Int = source.size - sourceOffset
) = source.useMemory(sourceOffset, count) { sourceMemory ->
    sourceMemory.copyTo(this, 0, count, offset)
}

inline fun Memory.storeByteArray(
    offset: Long,
    source: ByteArray,
    sourceOffset: Int = 0,
    count: Int = source.size - sourceOffset
) = source.useMemory(sourceOffset, count) { sourceMemory ->
    sourceMemory.copyTo(this, 0L, count.toLong(), offset)
}

inline fun Memory.storeUByteArray(
    offset: Int,
    source: UByteArray,
    sourceOffset: Int = 0,
    count: Int = source.size - sourceOffset
) = storeByteArray(offset, source.toByteArray(), sourceOffset, count)

inline fun Memory.storeUByteArray(
    offset: Long,
    source: UByteArray,
    sourceOffset: Int = 0,
    count: Int = source.size - sourceOffset
) = storeByteArray(offset, source.toByteArray(), sourceOffset, count)

expect fun Memory.storeShortArray(
    offset: Int,
    source: ShortArray,
    sourceOffset: Int = 0,
    count: Int = source.size - sourceOffset
)

expect fun Memory.storeShortArray(
    offset: Long,
    source: ShortArray,
    sourceOffset: Int = 0,
    count: Int = source.size - sourceOffset
)

inline fun Memory.storeUShortArray(
    offset: Int,
    source: UShortArray,
    sourceOffset: Int = 0,
    count: Int = source.size - sourceOffset
) = storeShortArray(offset, source.toShortArray(), sourceOffset, count)

inline fun Memory.storeUShortArray(
    offset: Long,
    source: UShortArray,
    sourceOffset: Int = 0,
    count: Int = source.size - sourceOffset
) = storeShortArray(offset, source.toShortArray(), sourceOffset, count)

expect fun Memory.storeIntArray(
    offset: Int,
    source: IntArray,
    sourceOffset: Int = 0,
    count: Int = source.size - sourceOffset
)

expect fun Memory.storeIntArray(
    offset: Long,
    source: IntArray,
    sourceOffset: Int = 0,
    count: Int = source.size - sourceOffset
)

inline fun Memory.storeUIntArray(
    offset: Int,
    source: UIntArray,
    sourceOffset: Int = 0,
    count: Int = source.size - sourceOffset
) = storeIntArray(offset, source.toIntArray(), sourceOffset, count)

inline fun Memory.storeUIntArray(
    offset: Long,
    source: UIntArray,
    sourceOffset: Int = 0,
    count: Int = source.size - sourceOffset
) = storeIntArray(offset, source.toIntArray(), sourceOffset, count)

expect fun Memory.storeLongArray(
    offset: Int,
    source: LongArray,
    sourceOffset: Int = 0,
    count: Int = source.size - sourceOffset
)

expect fun Memory.storeLongArray(
    offset: Long,
    source: LongArray,
    sourceOffset: Int = 0,
    count: Int = source.size - sourceOffset
)

inline fun Memory.storeULongArray(
    offset: Int,
    source: ULongArray,
    sourceOffset: Int = 0,
    count: Int = source.size - sourceOffset
) = storeLongArray(offset, source.toLongArray(), sourceOffset, count)

inline fun Memory.storeULongArray(
    offset: Long,
    source: ULongArray,
    sourceOffset: Int = 0,
    count: Int = source.size - sourceOffset
) = storeLongArray(offset, source.toLongArray(), sourceOffset, count)

expect fun Memory.storeFloatArray(
    offset: Int,
    source: FloatArray,
    sourceOffset: Int = 0,
    count: Int = source.size - sourceOffset
)

expect fun Memory.storeFloatArray(
    offset: Long,
    source: FloatArray,
    sourceOffset: Int = 0,
    count: Int = source.size - sourceOffset
)

expect fun Memory.storeDoubleArray(
    offset: Int,
    source: DoubleArray,
    sourceOffset: Int = 0,
    count: Int = source.size - sourceOffset
)

expect fun Memory.storeDoubleArray(
    offset: Long,
    source: DoubleArray,
    sourceOffset: Int = 0,
    count: Int = source.size - sourceOffset
)
