package com.github.andreypfau.kotlinio.packet.ipv6

import com.github.andreypfau.kotlinio.packet.AbstractPacket
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.ip.IpPacket

fun IpV6Packet(builder: IpV6Builder.()->Unit): IpV6Packet =
    IpV6Builder().apply(builder).build()

class IpV6Packet : AbstractPacket, IpPacket {
    override val header: IpV6Header
    override val payload: Packet?

    constructor(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset) {
        header = IpV6Header(rawData, offset, length)
        val remainingRawDataLength = length - header.length
        var payloadLength: Int
        if (header.payloadLength.toInt() == 0) {
            payloadLength = remainingRawDataLength
        } else {
            payloadLength = header.payloadLength.toInt()
            require(payloadLength >= 0)
            if (payloadLength > remainingRawDataLength) {
                payloadLength = remainingRawDataLength
            }
        }
        payload = if (payloadLength != 0) {
            header.nextHeader.packet(rawData, offset + header.length, payloadLength)
        } else {
            null
        }
    }

    constructor(builder: IpV6Builder) {
        payload = builder.payloadBuilder?.build()
        header = IpV6Header(builder, payload)
    }

    override fun builder() = IpV6Builder(
        header.version,
        header.trafficClass,
        header.flowLabel,
        header.payloadLength,
        header.nextHeader,
        header.hopLimit,
        header.srcAddress,
        header.dstAddress,
        payload?.builder()
    )
}
