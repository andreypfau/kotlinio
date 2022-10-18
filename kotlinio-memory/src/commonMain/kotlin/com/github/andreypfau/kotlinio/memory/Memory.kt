
package com.github.andreypfau.kotlinio.memory

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

expect class Memory {
    val size: Long
    val size32: Int

    inline fun loadAt(index: Int): Byte
    inline fun loadAt(index: Long): Byte

    inline fun storeAt(index: Int, value: Byte)
    inline fun storeAt(index: Long, value: Byte)

    fun slice(offset: Int, length: Int): Memory
    fun slice(offset: Long, length: Long): Memory

    fun fill(offset: Int, count: Int, value: Byte)
    fun fill(offset: Long, count: Long, value: Byte)

    fun copyTo(destination: Memory, offset: Int, length: Int, destinationOffset: Int)
    fun copyTo(destination: Memory, offset: Long, length: Long, destinationOffset: Long)

    fun copyTo(destination: ByteArray, offset: Int, length: Int, destinationOffset: Int)
    fun copyTo(destination: ByteArray, offset: Long, length: Int, destinationOffset: Int)

    companion object {
        val Empty: Memory
    }
}

inline operator fun Memory.get(index: Int) = loadAt(index)
inline operator fun Memory.get(index: Long) = loadAt(index)

inline operator fun Memory.set(index: Int, value: Byte) = storeAt(index, value)
inline operator fun Memory.set(index: Long, value: Byte) = storeAt(index, value)

inline fun Memory.storeAt(index: Int, value: UByte) = storeAt(index, value.toByte())
inline fun Memory.storeAt(index: Long, value: UByte) = storeAt(index, value.toByte())

fun Memory.copyTo(destination: ByteArray, offset: Int, length: Int) = copyTo(destination, offset, length, destinationOffset = 0)
fun Memory.copyTo(destination: ByteArray, offset: Long, length: Int) = copyTo(destination, offset, length, destinationOffset = 0)

expect inline fun <R> ByteArray.useMemory(offset: Int = 0, length: Int = size, block: (Memory) -> R): R

@OptIn(ExperimentalContracts::class)
inline fun <R> withMemory(size: Long, block: (Memory) -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val allocator = DefaultAllocator
    val memory = allocator.alloc(size)
    return try {
        block(memory)
    } finally {
        allocator.free(memory)
    }
}

@OptIn(ExperimentalContracts::class)
inline fun <R> withMemory(size: Int, block: (Memory) -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    return withMemory(size.toLong(), block)
}
