package com.github.andreypfau.network.packet.icmpv6.echo

import com.github.andreypfau.network.packet.Packet
import com.github.andreypfau.network.packet.icmp.IcmpIdentifiablePacket
import com.github.andreypfau.network.packet.simple.SimplePacket

class ICMPv6EchoReplyPacket : IcmpIdentifiablePacket {
    override val header: ICMPv6EchoReplyHeader
    override val payload: Packet?

    constructor(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset) {
        header = ICMPv6EchoReplyHeader(rawData, offset, length)
        val payloadLength = length - header.length
        payload = if (payloadLength > 0) {
            SimplePacket(rawData, offset + header.length, payloadLength)
        } else {
            null
        }
    }

    constructor(builder: ICMPv6EchoReplyBuilder) {
        payload = builder.payloadBuilder?.build()
        header = ICMPv6EchoReplyHeader(builder)
    }

    override fun builder() = ICMPv6EchoReplyBuilder(header.identifier, header.sequenceNumber)
}