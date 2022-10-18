@file:Suppress("NOTHING_TO_INLINE")

package com.github.andreypfau.kotlinio.memory

import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.ArrayBufferView
import org.khronos.webgl.DataView
import org.khronos.webgl.Int8Array
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

actual class Memory(
    val view: DataView
) {
    constructor(
        buffer: ArrayBuffer,
        offset: Int = 0,
        length: Int = buffer.byteLength - offset
    ) : this(DataView(buffer, offset, length))

    constructor(
        view: ArrayBufferView,
        offset: Int = 0,
        length: Int = view.byteLength
    ) : this(view.buffer, view.byteOffset + offset, length)

    constructor(
        array: ByteArray,
        offset: Int = 0,
        length: Int = array.size - offset
    ) : this(array.asDynamic() as Int8Array, offset, length)

    actual inline val size: Long get() = view.byteLength.toLong()
    actual inline val size32: Int get() = view.byteLength

    actual inline fun loadAt(index: Int): Byte = view.getInt8(index)

    actual inline fun loadAt(index: Long): Byte = view.getInt8(index.toIntOrFail("index"))

    actual inline fun storeAt(index: Int, value: Byte) {
        view.setInt8(index, value)
    }

    actual inline fun storeAt(index: Long, value: Byte) {
        view.setInt8(index.toIntOrFail("index"), value)
    }

    actual fun slice(offset: Int, length: Int): Memory {
        require(offset >= 0) { "offset shouldn't be negative: $offset" }
        require(length >= 0) { "length shouldn't be negative: $length" }
        if (offset + length > size) {
            throw IndexOutOfBoundsException("offset + length > size: $offset + $length > $size")
        }

        return Memory(
            DataView(
                view.buffer,
                view.byteOffset + offset,
                length
            )
        )
    }

    actual fun slice(offset: Long, length: Long): Memory =
        slice(offset.toIntOrFail("offset"), length.toIntOrFail("length"))

    actual fun fill(offset: Int, count: Int, value: Byte) {
        for (index in offset until offset + count) {
            this[index] = value
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
        val src = Int8Array(view.buffer, view.byteOffset + offset, length)
        val dst = Int8Array(destination.view.buffer, destination.view.byteOffset + destinationOffset, length)

        dst.set(src)
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
        @Suppress("UnsafeCastFromDynamic")
        val to: Int8Array = destination.asDynamic()
        val from = Int8Array(view.buffer, view.byteOffset + offset, length)
        to.set(from, destinationOffset)
    }

    actual fun copyTo(
        destination: ByteArray,
        offset: Long,
        length: Int,
        destinationOffset: Int
    ) = copyTo(destination, offset.toIntOrFail("offset"), length, destinationOffset)

    actual companion object {
        actual val Empty: Memory = Memory(DataView(ArrayBuffer(0)))
    }
}

@OptIn(ExperimentalContracts::class)
actual inline fun <R> ByteArray.useMemory(offset: Int, length: Int, block: (Memory) -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return Memory(this, offset, length).let(block)
}
