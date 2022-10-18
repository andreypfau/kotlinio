package com.github.andreypfau.kotlinio.memory

import kotlinx.cinterop.*
import platform.posix.memcpy

actual fun Memory.loadShortArray(
    offset: Int,
    destination: ShortArray,
    destinationOffset: Int,
    count: Int
) = loadShortArray(offset.toLong(), destination, destinationOffset, count)

actual fun Memory.loadShortArray(
    offset: Long,
    destination: ShortArray,
    destinationOffset: Int,
    count: Int
) {
    requirePositiveIndex(offset, "offset")
    requirePositiveIndex(destinationOffset, "destinationOffset")
    requirePositiveIndex(count, "count")

    if (count == 0) return

    requireRange(destinationOffset, count, destination.size, "destination")
    requireRange(offset, count * 2L, size, "memory")

    if (!Platform.isLittleEndian) {
        destination.usePinned { pinned ->
            memcpy(pinned.addressOf(destinationOffset), pointer + offset, (count * 2L).convert())
        }
    } else if (Platform.canAccessUnaligned || isAlignedShort(offset)) {
        val source = pointer.plus(offset)!!.reinterpret<ShortVar>()

        for (index in 0 until count) {
            destination[index + destinationOffset] = source[index].reverseByteOrder()
        }
    } else {
        destination.usePinned { pinned ->
            memcpy(pinned.addressOf(destinationOffset), pointer + offset, (count * 2L).convert())
        }

        for (index in destinationOffset until destinationOffset + count) {
            destination[index] = destination[index].reverseByteOrder()
        }
    }
}

actual fun Memory.loadIntArray(
    offset: Int,
    destination: IntArray,
    destinationOffset: Int,
    count: Int
) = loadIntArray(offset.toLong(), destination, destinationOffset, count)

actual fun Memory.loadIntArray(
    offset: Long,
    destination: IntArray,
    destinationOffset: Int,
    count: Int
) {
    requirePositiveIndex(offset, "offset")
    requirePositiveIndex(destinationOffset, "destinationOffset")
    requirePositiveIndex(count, "count")

    if (count == 0) return

    requireRange(destinationOffset, count, destination.size, "destination")
    requireRange(offset, count * 4L, size, "memory")

    if (!Platform.isLittleEndian) {
        destination.usePinned { pinned ->
            memcpy(pinned.addressOf(destinationOffset), pointer + offset, (count * 4L).convert())
        }
        return
    }

    if (Platform.canAccessUnaligned || isAlignedInt(offset)) {
        val source = pointer.plus(offset)!!.reinterpret<IntVar>()

        for (index in 0 until count) {
            destination[index + destinationOffset] = source[index].reverseByteOrder()
        }
        return
    }

    destination.usePinned { pinned ->
        memcpy(pinned.addressOf(destinationOffset), pointer + offset, (count * 4L).convert())
    }

    for (index in destinationOffset until destinationOffset + count) {
        destination[index] = destination[index].reverseByteOrder()
    }
}

actual fun Memory.loadLongArray(
    offset: Int,
    destination: LongArray,
    destinationOffset: Int,
    count: Int
) = loadLongArray(offset.toLong(), destination, destinationOffset, count)

actual fun Memory.loadLongArray(
    offset: Long,
    destination: LongArray,
    destinationOffset: Int,
    count: Int
) {
    requirePositiveIndex(offset, "offset")
    requirePositiveIndex(destinationOffset, "destinationOffset")
    requirePositiveIndex(count, "count")

    if (count == 0) return

    requireRange(destinationOffset, count, destination.size, "destination")
    requireRange(offset, count * 8L, size, "memory")

    if (!Platform.isLittleEndian) {
        destination.usePinned { pinned ->
            memcpy(pinned.addressOf(destinationOffset), pointer + offset, (count * 8L).convert())
        }
        return
    }

    if (Platform.canAccessUnaligned || isAlignedLong(offset)) {
        val source = pointer.plus(offset)!!.reinterpret<LongVar>()

        for (index in 0 until count) {
            destination[index + destinationOffset] = source[index].reverseByteOrder()
        }
        return
    }

    destination.usePinned { pinned ->
        memcpy(pinned.addressOf(destinationOffset), pointer + offset, (count * 8L).convert())
    }

    for (index in destinationOffset until destinationOffset + count) {
        destination[index] = destination[index].reverseByteOrder()
    }
}

actual fun Memory.loadFloatArray(
    offset: Int,
    destination: FloatArray,
    destinationOffset: Int,
    count: Int
) = loadFloatArray(offset.toLong(), destination, destinationOffset, count)

actual fun Memory.loadFloatArray(
    offset: Long,
    destination: FloatArray,
    destinationOffset: Int,
    count: Int
) {
    requirePositiveIndex(offset, "offset")
    requirePositiveIndex(destinationOffset, "destinationOffset")
    requirePositiveIndex(count, "count")

    if (count == 0) return

    requireRange(destinationOffset, count, destination.size, "destination")
    requireRange(offset, count * 4L, size, "memory")

    if (!Platform.isLittleEndian) {
        destination.usePinned { pinned ->
            memcpy(pinned.addressOf(destinationOffset), pointer + offset, (count * 4L).convert())
        }
    } else if (Platform.canAccessUnaligned || isAlignedInt(offset)) {
        val source = pointer.plus(offset)!!.reinterpret<FloatVar>()

        for (index in 0 until count) {
            destination[index + destinationOffset] = source[index].reverseByteOrder()
        }
    } else {
        destination.usePinned { pinned ->
            memcpy(pinned.addressOf(destinationOffset), pointer + offset, (count * 4L).convert())
        }

        for (index in destinationOffset until destinationOffset + count) {
            destination[index] = destination[index].reverseByteOrder()
        }
    }
}

actual fun Memory.loadDoubleArray(
    offset: Int,
    destination: DoubleArray,
    destinationOffset: Int,
    count: Int
) = loadDoubleArray(offset.toLong(), destination, destinationOffset, count)

actual fun Memory.loadDoubleArray(
    offset: Long,
    destination: DoubleArray,
    destinationOffset: Int,
    count: Int
) {
    requirePositiveIndex(offset, "offset")
    requirePositiveIndex(destinationOffset, "destinationOffset")
    requirePositiveIndex(count, "count")

    if (count == 0) return

    requireRange(destinationOffset, count, destination.size, "destination")
    requireRange(offset, count * 8L, size, "memory")

    if (!Platform.isLittleEndian) {
        destination.usePinned { pinned ->
            memcpy(pinned.addressOf(destinationOffset), pointer + offset, (count * 8L).convert())
        }
    } else if (Platform.canAccessUnaligned || isAlignedLong(offset)) {
        val source = pointer.plus(offset)!!.reinterpret<DoubleVar>()

        for (index in 0 until count) {
            destination[index + destinationOffset] = source[index].reverseByteOrder()
        }
    } else {
        destination.usePinned { pinned ->
            memcpy(pinned.addressOf(destinationOffset), pointer + offset, (count * 8L).convert())
        }

        for (index in destinationOffset until destinationOffset + count) {
            destination[index] = destination[index].reverseByteOrder()
        }
    }
}
