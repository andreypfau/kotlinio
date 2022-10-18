@file:Suppress("NOTHING_TO_INLINE")

package com.github.andreypfau.kotlinio.memory

import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@Suppress("ACTUAL_WITHOUT_EXPECT")
@JvmInline
actual value class Memory(
    val buffer: ByteBuffer
) {
    actual inline val size: Long
        get() = buffer.limit().toLong()

    actual inline val size32: Int
        get() = buffer.limit()

    actual inline fun loadAt(index: Int): Byte = buffer.get(index)

    actual inline fun loadAt(index: Long): Byte = buffer.get(index.toIntOrFail("index"))

    actual inline fun storeAt(index: Int, value: Byte) {
        buffer.put(index, value)
    }

    actual inline fun storeAt(index: Long, value: Byte) {
        buffer.put(index.toIntOrFail("index"), value)
    }

    actual fun slice(offset: Int, length: Int): Memory =
        Memory(buffer.sliceSafe(offset, length))

    actual fun slice(offset: Long, length: Long): Memory =
        slice(offset.toIntOrFail("offset"), length.toIntOrFail("length"))

    actual fun fill(offset: Int, count: Int, value: Byte) {
        for (index in offset until offset + count) {
            buffer.put(index, value)
        }
    }

    actual fun fill(offset: Long, count: Long, value: Byte) =
        fill(offset.toIntOrFail("offset"), count.toIntOrFail("count"), value)

    actual fun copyTo(
        destination: Memory,
        offset: Int,
        length: Int,
        destinationOffset: Int
    ) {
        if (buffer.hasArray() && destination.buffer.hasArray() &&
            !buffer.isReadOnly && !destination.buffer.isReadOnly
        ) {
            System.arraycopy(
                buffer.array(),
                buffer.arrayOffset() + offset,
                destination.buffer.array(),
                destination.buffer.arrayOffset() + destinationOffset,
                length
            )
            return
        }

        // NOTE: it is ok here to make copy since it will be escaped by JVM
        // while temporary moving position/offset makes memory concurrent unsafe that is unacceptable

        val srcCopy = buffer.duplicate().apply {
            position(offset)
            limit(offset + length)
        }
        val dstCopy = destination.buffer.duplicate().apply {
            position(destinationOffset)
        }

        dstCopy.put(srcCopy)
    }

    actual fun copyTo(
        destination: Memory,
        offset: Long,
        length: Long,
        destinationOffset: Long
    ) = copyTo(
        destination,
        offset.toIntOrFail("offset"),
        length.toIntOrFail("length"),
        destinationOffset.toIntOrFail("destinationOffset")
    )

    actual fun copyTo(
        destination: ByteArray,
        offset: Int,
        length: Int,
        destinationOffset: Int
    ) {
        if (buffer.hasArray() && !buffer.isReadOnly) {
            System.arraycopy(
                buffer.array(),
                buffer.arrayOffset() + offset,
                destination,
                destinationOffset,
                length
            )
            return
        }

        // we need to make a copy to prevent moving position
        buffer.duplicate().get(destination, destinationOffset, length)
    }

    actual fun copyTo(
        destination: ByteArray,
        offset: Long,
        length: Int,
        destinationOffset: Int
    ) = copyTo(destination, offset.toIntOrFail("offset"), length, destinationOffset)

    actual companion object {
        actual val Empty: Memory = Memory(ByteBuffer.allocate(0).order(ByteOrder.BIG_ENDIAN))
    }
}

@OptIn(ExperimentalContracts::class)
actual inline fun <R> ByteArray.useMemory(offset: Int, length: Int, block: (Memory) -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return Memory(ByteBuffer.wrap(this, offset, length).slice().order(ByteOrder.BIG_ENDIAN)).let(block)
}
