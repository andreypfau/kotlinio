package com.github.andreypfau.kotlinio.memory

@PublishedApi
internal expect object DefaultAllocator : Allocator

interface Allocator {
    fun alloc(size: Int): Memory

    fun alloc(size: Long): Memory

    fun free(instance: Memory)
}
