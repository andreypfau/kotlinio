package com.github.andreypfau.kotlinio.memory

import kotlinx.cinterop.NativeFreeablePlacement
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.nativeHeap

internal value class PlacementAllocator(
    private val placement: NativeFreeablePlacement
) : Allocator {
    override fun alloc(size: Int): Memory = alloc(size.toLong())

    override fun alloc(size: Long): Memory = Memory(placement.allocArray(size), size)

    override fun free(instance: Memory) = placement.free(instance.pointer.rawValue)
}

internal actual object DefaultAllocator : Allocator by PlacementAllocator(nativeHeap)
