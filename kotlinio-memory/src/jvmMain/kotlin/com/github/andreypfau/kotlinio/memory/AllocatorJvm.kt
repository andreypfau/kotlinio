package com.github.andreypfau.kotlinio.memory

import java.nio.ByteBuffer

@PublishedApi
internal actual object DefaultAllocator : Allocator {
    override fun alloc(size: Int): Memory = Memory(ByteBuffer.allocate(size))

    override fun alloc(size: Long): Memory = alloc(size.toIntOrFail("size"))

    override fun free(instance: Memory) {
    }
}
