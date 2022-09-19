package com.github.andreypfau.kotlinio.packet

class BuilderIterator(
    private var next: Packet.Builder?,
    private var previous: Packet.Builder? = null
) : Iterator<Packet.Builder> {
    override fun hasNext(): Boolean = next != null

    override fun next(): Packet.Builder {
        val nextPrevious = next ?: throw NoSuchElementException()
        previous = nextPrevious
        next = nextPrevious.payloadBuilder
        return nextPrevious
    }
}
