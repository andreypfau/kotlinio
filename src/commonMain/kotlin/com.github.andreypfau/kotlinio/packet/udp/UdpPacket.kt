package com.github.andreypfau.kotlinio.packet.udp

import com.github.andreypfau.kotlinio.packet.AbstractPacket
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.simple.SimplePacket
import com.github.andreypfau.kotlinio.packet.transport.TransportPacket

fun UdpPacket(builder: UdpBuilder.() -> Unit): UdpPacket =
    UdpBuilder().apply(builder).build()

class UdpPacket : AbstractPacket, TransportPacket {
    override val header: UdpHeader
    override val payload: Packet?

    constructor(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset) {
        header = UdpHeader(rawData, offset, length)
        var payloadLength = header.packetLength.toInt() - header.length
        if (payloadLength < 0) {
            throw IllegalArgumentException()
        }
        if (payloadLength > length - header.length) {
            payloadLength = length - header.length
        }
        payload = if (payloadLength != 0) {
            SimplePacket(rawData, offset + header.length)
        } else {
            null
        }
    }

    constructor(builder: UdpBuilder) {
        payload = builder.payloadBuilder?.build()
        header = UdpHeader(builder, payload?.rawData ?: ByteArray(0))
    }

    override fun builder() = UdpBuilder(
        header.srcPort,
        header.dstPort,
        header.length.toUShort(),
        header.checksum,
        payload?.builder()
    )
}
