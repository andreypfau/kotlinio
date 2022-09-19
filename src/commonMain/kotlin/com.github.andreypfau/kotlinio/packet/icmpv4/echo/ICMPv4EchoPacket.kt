package com.github.andreypfau.kotlinio.packet.icmpv4.echo

import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.icmp.IcmpIdentifiablePacket
import com.github.andreypfau.kotlinio.packet.simple.SimplePacket

class ICMPv4EchoPacket : IcmpIdentifiablePacket {
    override val header: ICMPv4EchoHeader
    override val payload: Packet?

    constructor(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset) {
        header = ICMPv4EchoHeader(rawData, offset, length)
        val payloadLength = length - header.length
        payload = if (payloadLength > 0) {
            SimplePacket(rawData, offset + header.length, payloadLength)
        } else {
            null
        }
    }

    constructor(builder: ICMPv4EchoBuilder) {
        payload = builder.payloadBuilder?.build()
        header = ICMPv4EchoHeader(builder)
    }

    override fun builder() = ICMPv4EchoBuilder(header.identifier, header.sequenceNumber)
}
