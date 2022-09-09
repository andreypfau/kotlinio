package com.github.andreypfau.network.packet.icmpv6.echo

import com.github.andreypfau.network.packet.Packet
import com.github.andreypfau.network.packet.icmp.IcmpIdentifiablePacket
import com.github.andreypfau.network.packet.simple.SimplePacket

class ICMPv6EchoRequestPacket : IcmpIdentifiablePacket {
    override val header: ICMPv6EchoRequestHeader
    override val payload: Packet?

    constructor(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset) {
        header = ICMPv6EchoRequestHeader(rawData, offset, length)
        val payloadLength = length - header.length
        payload = if (payloadLength > 0) {
            SimplePacket(rawData, offset + header.length, payloadLength)
        } else {
            null
        }
    }

    constructor(builder: ICMPv6EchoRequestBuilder) {
        payload = builder.payloadBuilder?.build()
        header = ICMPv6EchoRequestHeader(builder)
    }

    override fun builder() = ICMPv6EchoRequestBuilder(header.identifier, header.sequenceNumber)
}