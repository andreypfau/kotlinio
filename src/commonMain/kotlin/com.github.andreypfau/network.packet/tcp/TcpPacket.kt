package com.github.andreypfau.network.packet.tcp

import com.github.andreypfau.network.packet.AbstractPacket
import com.github.andreypfau.network.packet.Packet
import com.github.andreypfau.network.packet.simple.SimplePacket
import com.github.andreypfau.network.packet.transport.TransportPacket

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