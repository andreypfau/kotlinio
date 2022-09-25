package com.github.andreypfau.kotlinio.packet.tcp

import com.github.andreypfau.kotlinio.packet.AbstractPacket
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.simple.SimplePacket
import com.github.andreypfau.kotlinio.packet.transport.TransportPacket

fun TcpPacket(builder: TcpBuilder.() -> Unit): TcpPacket =
    TcpBuilder().apply(builder).build()

class TcpPacket : AbstractPacket, TransportPacket {
    override val header: TcpHeader
    override val payload: Packet?

    constructor(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset) {
        header = TcpHeader(rawData, offset, length)
        val payloadLength = length - header.length
        payload = if (payloadLength > 0) {
            SimplePacket(rawData, offset + header.length, payloadLength)
        } else {
            null
        }
    }

    constructor(builder: TcpBuilder) {
        payload = builder.payloadBuilder?.build()
        header = TcpHeader(builder, payload?.rawData ?: ByteArray(0))
    }

    override fun builder() = TcpBuilder(
        header.srcPort,
        header.dstPort,
        header.sequenceNumber,
        header.acknowledgmentNumber,
        header.dataOffset,
        header.reserved,
        header.urg,
        header.ack,
        header.psh,
        header.rst,
        header.syn,
        header.fin,
        header.window,
        header.checksum,
        header.urgentPointer,
        header.options.toMutableList(),
        header.padding,
        payload?.builder()
    )
}
