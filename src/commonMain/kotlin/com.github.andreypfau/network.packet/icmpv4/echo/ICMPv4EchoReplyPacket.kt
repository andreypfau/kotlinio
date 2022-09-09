package com.github.andreypfau.network.packet.icmpv4.echo

import com.github.andreypfau.network.packet.Packet
import com.github.andreypfau.network.packet.icmp.IcmpIdentifiablePacket
import com.github.andreypfau.network.packet.simple.SimplePacket

class ICMPv4EchoReplyPacket : IcmpIdentifiablePacket {
    override val header: ICMPv4EchoReplyHeader
    override val payload: Packet?

    constructor(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset) {
        header = ICMPv4EchoReplyHeader(rawData, offset, length)
        val payloadLength = length - header.length
        payload = if (payloadLength > 0) {
            SimplePacket(rawData, offset + header.length, payloadLength)
        } else {
            null
        }
    }

    constructor(builder: ICMPv4EchoReplyBuilder) {
        payload = builder.payloadBuilder?.build()
        header = ICMPv4EchoReplyHeader(builder)
    }

    override fun builder() = ICMPv4EchoReplyBuilder(header.identifier, header.sequenceNumber)
}