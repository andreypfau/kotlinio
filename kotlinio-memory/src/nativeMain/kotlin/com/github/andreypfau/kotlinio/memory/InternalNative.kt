package com.github.andreypfau.kotlinio.memory

import kotlinx.cinterop.*
import platform.posix.memcpy


@PublishedApi
internal inline fun Memory.assertIndex(offset: Int, valueSize: Int): Int {
    assert(offset in 0..size - valueSize) {
        throw IndexOutOfBoundsException("offset $offset outside of range [0; ${size - valueSize})")
    }
    return offset
}

@PublishedApi
internal inline fun Memory.assertIndex(offset: Long, valueSize: Long): Long {
    assert(offset in 0..size - valueSize) {
        throw IndexOutOfBoundsException("offset $offset outside of range [0; ${size - valueSize})")
    }
    return offset
}

internal inline fun requirePositiveIndex(value: Int, name: String) {
    if (value < 0) {
        throw IndexOutOfBoundsException("$name shouldn't be negative: $value")
    }
}

internal inline fun requirePositiveIndex(value: Long, name: String) {
    if (value < 0L) {
        throw IndexOutOfBoundsException("$name shouldn't be negative: $value")
    }
}

internal inline fun requireRange(offset: Int, length: Int, size: Int, name: String) {
    if (offset + length > size) {
        throw IndexOutOfBoundsException("Wrong offset/count for $name: offset $offset, length $length, size $size")
    }
}

internal inline fun requireRange(offset: Long, length: Long, size: Long, name: String) {
    if (offset + length > size) {
        throw IndexOutOfBoundsException("Wrong offset/count for $name: offset $offset, length $length, size $size")
    }
}

internal inline fun storeArrayIndicesCheck(
    offset: Long,
    sourceOffset: Int,
    count: Int,
    itemSize: Long,
    sourceSize: Int,
    memorySize: Long
) {
    requirePositiveIndex(offset, "offset")
    requirePositiveIndex(sourceOffset, "destinationOffset")
    requirePositiveIndex(count, "count")

    requireRange(sourceOffset, count, sourceSize, "source")
    requireRange(offset, count * itemSize, memorySize, "memory")
}

internal inline fun Memory.isAlignedShort(offset: Long) = (pointer.toLong() + offset) and 0b1 == 0L
internal inline fun Memory.isAlignedInt(offset: Long) = (pointer.toLong() + offset) and 0b11 == 0L
internal inline fun Memory.isAlignedLong(offset: Long) = (pointer.toLong() + offset) and 0b111 == 0L

internal fun copy(
    source: ShortArray,
    destinationPointer: CPointer<ByteVar>,
    sourceOffset: Int,
    count: Int
) {
    source.usePinned { pinned ->
        memcpy(destinationPointer, pinned.addressOf(sourceOffset), (count * 2L).convert())
    }
}

internal fun copy(
    source: IntArray,
    destinationPointer: CPointer<ByteVar>,
    sourceOffset: Int,
    count: Int
) {
    source.usePinned { pinned ->
        memcpy(destinationPointer, pinned.addressOf(sourceOffset), (count * 4L).convert())
    }
}

internal fun copy(
    source: LongArray,
    destinationPointer: CPointer<ByteVar>,
    sourceOffset: Int,
    count: Int
) {
    source.usePinned { pinned ->
        memcpy(destinationPointer, pinned.addressOf(sourceOffset), (count * 8L).convert())
    }
}

internal fun copy(
    source: FloatArray,
    destinationPointer: CPointer<ByteVar>,
    sourceOffset: Int,
    count: Int
) {
    source.usePinned { pinned ->
        memcpy(destinationPointer, pinned.addressOf(sourceOffset), (count * 4L).convert())
    }
}

internal fun copy(
    source: DoubleArray,
    destinationPointer: CPointer<ByteVar>,
    sourceOffset: Int,
    count: Int
) {
    source.usePinned { pinned ->
        memcpy(destinationPointer, pinned.addressOf(sourceOffset), (count * 8L).convert())
    }
}
