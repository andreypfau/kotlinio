package com.github.andreypfau.kotlinio.packet.icmpv4.common

import com.github.andreypfau.kotlinio.packet.AbstractPacket
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.simple.SimplePacket

class ICMPv4CommonPacket : AbstractPacket {
    override val header: ICMPv4CommonHeader
    override val payload: Packet?

    constructor(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset) {
        header = ICMPv4CommonHeader(rawData, offset, length)
        val payloadLength = length - header.length
        payload = if (payloadLength > 0) {
            SimplePacket(rawData, offset + header.length, payloadLength)
        } else {
            null
        }
    }

    constructor(builder: ICMPv4CommonBuilder) {
        payload = builder.payloadBuilder?.build()
        header = ICMPv4CommonHeader(builder, payload?.rawData ?: ByteArray(0))
    }

    override fun builder() = ICMPv4CommonBuilder(
        header.type, header.code, header.checksum, payload?.builder()
    )
}
