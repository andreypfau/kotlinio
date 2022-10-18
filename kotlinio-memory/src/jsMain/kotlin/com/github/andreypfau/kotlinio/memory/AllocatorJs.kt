package com.github.andreypfau.kotlinio.memory

import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.DataView

internal actual object DefaultAllocator : Allocator {
    override fun alloc(size: Int): Memory = Memory(DataView(ArrayBuffer(size)))
    override fun alloc(size: Long): Memory = Memory(DataView(ArrayBuffer(size.toIntOrFail("size"))))
    override fun free(instance: Memory) {
    }
}
