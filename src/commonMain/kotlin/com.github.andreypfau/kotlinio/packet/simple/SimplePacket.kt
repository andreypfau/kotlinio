package com.github.andreypfau.kotlinio.packet.simple

import com.github.andreypfau.kotlinio.packet.AbstractPacket
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.utils.hex

class SimplePacket(
    private val _rawData: ByteArray,
    private val offset: Int = 0,
    override val length: Int = _rawData.size - offset
) : AbstractPacket() {
    override fun buildRawData(): ByteArray =
        _rawData.copyOfRange(offset, _rawData.size)

    override fun buildString(): String = buildString {
        append("[data (")
        append(this@SimplePacket.length)
        append(" bytes)]").appendLine()
        append("  Hex stream: ")
            .append(rawData.hex(" "))
            .appendLine()
    }

    override fun builder() = Builder(rawData.copyOf())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        if (!super.equals(other)) return false

        other as SimplePacket

        if (!rawData.contentEquals(other.rawData)) return false

        return true
    }

    override fun calcHashCode(): Int = rawData.contentHashCode()

    class Builder(
        var rawData: ByteArray = byteArrayOf()
    ) : AbstractBuilder() {
        override var payloadBuilder: Packet.Builder? = null

        override fun build(): Packet = SimplePacket(rawData)
    }
}
