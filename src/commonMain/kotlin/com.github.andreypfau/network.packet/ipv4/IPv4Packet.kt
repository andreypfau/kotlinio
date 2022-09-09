package com.github.andreypfau.network.packet.ipv4

import com.github.andreypfau.network.packet.AbstractPacket
import com.github.andreypfau.network.packet.Packet
import com.github.andreypfau.network.packet.ip.IpPacket
import com.github.andreypfau.network.packet.transport.TransportBuilder

class IPv4Packet : AbstractPacket, IpPacket {
    override val header: IPv4Header
    override val payload: Packet?

    constructor(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset) {
        header = IPv4Header(rawData, offset, length)
        val remainingRawDataLength = length - header.length
        val totalLength = header.totalLength.toUShort().toInt()
        var payloadLength: Int
        if (totalLength == 0) {
            payloadLength = remainingRawDataLength
        } else {
            payloadLength = totalLength - header.length
            if (payloadLength < 0) {
                throw IllegalArgumentException()
            }
            if (payloadLength > remainingRawDataLength) {
                payloadLength = remainingRawDataLength
            }
        }
        payload = if (payloadLength != 0) {
            if (header.moreFragmentFlag || header.fragmentOffset.toUShort().toInt() != 0) {
                TODO()
            } else {
                header.protocol.packet(rawData, header.length + offset, payloadLength)
            }
        } else {
            null
        }
    }

    constructor(
        builder: IPv4Builder
    ) {
        payload = builder.payloadBuilder?.build()
        header = IPv4Header(builder, payload)
    }

    override fun builder() = IPv4Builder(
        header.version,
        header.ihl,
        header.tos,
        header.totalLength,
        header.identification,
        header.reservedFlag,
        header.dontFragmentFlag,
        header.moreFragmentFlag,
        header.fragmentOffset,
        header.ttl,
        header.protocol,
        header.headerChecksum,
        header.srcAddress,
        header.dstAddress,
        header.options.toMutableList(),
        header.padding,
        payload?.builder()?.also {
            if (it is TransportBuilder) {
                it.srcAddress = header.srcAddress
                it.dstAddress = header.dstAddress
            }
        }
    )

    companion object {
        fun builder() = IPv4Builder()
    }
}