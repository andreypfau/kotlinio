package com.github.andreypfau.kotlinio.memory

import java.nio.ByteBuffer

fun Memory.copyTo(destination: ByteBuffer, offset: Long) =
    copyTo(destination, offset.toIntOrFail("offset"))

fun Memory.copyTo(
    destination: ByteBuffer,
    offset: Int
) {
    val size = destination.remaining()

    if (buffer.hasArray() && !buffer.isReadOnly &&
        destination.hasArray() && !destination.isReadOnly
    ) {
        val dstPosition = destination.position()

        System.arraycopy(
            buffer.array(),
            buffer.arrayOffset() + offset,
            destination.array(),
            destination.arrayOffset() + dstPosition,
            size
        )
        destination.position(dstPosition + size)
        return
    }

    // we need to make a copy to prevent moving position
    val source = buffer.duplicate().apply {
        limit(offset + size)
        position(offset)
    }
    destination.put(source)
}

fun ByteBuffer.copyTo(destination: Memory, offset: Int) {
    if (hasArray() && !isReadOnly) {
        destination.storeByteArray(offset, array(), arrayOffset() + position(), remaining())
        position(limit())
        return
    }

    destination.buffer.sliceSafe(offset, remaining()).put(this)
}
