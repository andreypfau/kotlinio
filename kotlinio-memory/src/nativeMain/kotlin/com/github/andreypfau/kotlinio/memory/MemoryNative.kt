package com.github.andreypfau.kotlinio.memory

import kotlinx.cinterop.*
import platform.posix.memcpy
import platform.posix.memset
import platform.posix.size_t
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

actual class Memory(
    val pointer: CPointer<ByteVar>,
    actual inline val size: Long
) {
    actual val size32: Int get() = size.toIntOrFail("size")

    actual inline fun loadAt(index: Int): Byte = pointer[assertIndex(index, 1)]
    actual inline fun loadAt(index: Long): Byte = pointer[assertIndex(index, 1)]

    actual inline fun storeAt(index: Int, value: Byte) {
        pointer[assertIndex(index, 1)] = value
    }

    actual inline fun storeAt(index: Long, value: Byte) {
        pointer[assertIndex(index, 1)] = value
    }

    actual fun slice(offset: Int, length: Int): Memory {
        assertIndex(offset, length)
        if (offset == 0 && length.toLong() == size) {
            return this
        }

        return Memory(pointer.plus(offset)!!, length.toLong())
    }

    actual fun slice(offset: Long, length: Long): Memory {
        assertIndex(offset, length)
        if (offset == 0L && length == size) {
            return this
        }

        return Memory(pointer.plus(offset)!!, length)
    }

    actual fun fill(offset: Int, count: Int, value: Byte) {
        requirePositiveIndex(offset, "offset")
        requirePositiveIndex(count, "count")
        requireRange(offset.toLong(), count.toLong(), size, "fill")

        if (count.toULong() > size_t.MAX_VALUE) {
            throw IllegalArgumentException("count is too big: it shouldn't exceed size_t.MAX_VALUE")
        }

        memset(pointer + offset, value.toInt(), count.convert())
    }

    actual fun fill(offset: Long, count: Long, value: Byte) {
        requirePositiveIndex(offset, "offset")
        requirePositiveIndex(count, "count")
        requireRange(offset, count, size, "fill")
        if (count.toULong() > size_t.MAX_VALUE.toULong()) {
            throw IllegalArgumentException("count is too big: it shouldn't exceed size_t.MAX_VALUE")
        }

        memset(pointer + offset, value.toInt(), count.convert())
    }

    actual fun copyTo(
        destination: Memory,
        offset: Int,
        length: Int,
        destinationOffset: Int
    ) {
        require(offset >= 0) { "offset shouldn't be negative: $offset" }
        require(length >= 0) { "length shouldn't be negative: $length" }
        require(destinationOffset >= 0) { "destinationOffset shouldn't be negative: $destinationOffset" }

        if (offset + length > size) {
            throw IndexOutOfBoundsException("offset + length > size: $offset + $length > $size")
        }
        if (destinationOffset + length > destination.size) {
            throw IndexOutOfBoundsException(
                "dst offset + length > size: $destinationOffset + $length > ${destination.size}"
            )
        }

        if (length == 0) return

        memcpy(
            destination.pointer + destinationOffset,
            pointer + offset,
            length.convert()
        )
    }

    actual fun copyTo(
        destination: Memory,
        offset: Long,
        length: Long,
        destinationOffset: Long
    ) {
        require(offset >= 0L) { "offset shouldn't be negative: $offset" }
        require(length >= 0L) { "length shouldn't be negative: $length" }
        require(destinationOffset >= 0L) { "destinationOffset shouldn't be negative: $destinationOffset" }

        if (offset + length > size) {
            throw IndexOutOfBoundsException("offset + length > size: $offset + $length > $size")
        }
        if (destinationOffset + length > destination.size) {
            throw IndexOutOfBoundsException(
                "dst offset + length > size: $destinationOffset + $length > ${destination.size}"
            )
        }

        if (length == 0L) return

        memcpy(
            destination.pointer + destinationOffset,
            pointer + offset,
            length.convert()
        )
    }

    actual fun copyTo(
        destination: ByteArray,
        offset: Int,
        length: Int,
        destinationOffset: Int
    ) {
        if (destination.isEmpty() && destinationOffset == 0 && length == 0) {
            return
        }

        destination.usePinned { pinned ->
            copyTo(
                destination = Memory(pinned.addressOf(0), destination.size.toLong()),
                offset = offset,
                length = length,
                destinationOffset = destinationOffset
            )
        }
    }

    actual fun copyTo(
        destination: ByteArray,
        offset: Long,
        length: Int,
        destinationOffset: Int
    ) {
        if (destination.isEmpty() && destinationOffset == 0 && length == 0) {
            return
        }

        destination.usePinned { pinned ->
            copyTo(
                destination = Memory(pinned.addressOf(0), destination.size.toLong()),
                offset = offset,
                length = length.toLong(),
                destinationOffset = destinationOffset.toLong()
            )
        }
    }

    actual companion object {
        actual val Empty: Memory = Memory(nativeHeap.allocArray(0), 0)
    }
}

@OptIn(ExperimentalContracts::class)
actual inline fun <R> ByteArray.useMemory(offset: Int, length: Int, block: (Memory) -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    return usePinned { pinned ->
        val memory = when {
            isEmpty() && offset == 0 && length == 0 -> Memory.Empty
            else -> Memory(pinned.addressOf(offset), length.toLong())
        }

        block(memory)
    }
}
