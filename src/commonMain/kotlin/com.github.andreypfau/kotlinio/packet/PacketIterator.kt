package com.github.andreypfau.kotlinio.packet

class PacketIterator(
    private var next: Packet?,
    private var previous: Packet? = null
) : Iterator<Packet> {
    override fun hasNext(): Boolean = next != null

    override fun next(): Packet {
        val nextPrevious = next ?: throw NoSuchElementException()
        previous = nextPrevious
        next = nextPrevious.payload
        return nextPrevious
    }
}
