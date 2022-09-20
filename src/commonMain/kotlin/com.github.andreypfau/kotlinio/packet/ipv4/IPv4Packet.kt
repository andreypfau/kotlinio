package com.github.andreypfau.kotlinio.packet.ipv4

import com.github.andreypfau.kotlinio.packet.AbstractPacket
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.ip.IpPacket
import com.github.andreypfau.kotlinio.packet.simple.SimplePacket
import com.github.andreypfau.kotlinio.packet.transport.TransportBuilder
import kotlin.jvm.JvmStatic

class IPv4Packet : AbstractPacket, IpPacket {
    override val header: IPv4Header
    override val payload: Packet?

    constructor(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset) {
        header = IPv4Header(rawData, offset, length)
        val remainingRawDataLength = length - header.length
        val totalLength = header.totalLength.toInt()
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
            if (header.isFragmented) {
                SimplePacket(rawData, header.length + offset, payloadLength)
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
        @JvmStatic
        fun build(builder: IPv4Builder.() -> Unit): IPv4Packet =
            IPv4Builder().apply(builder).build()

        @JvmStatic
        fun fragment(packet: IPv4Packet, mtu: Int): List<IPv4Packet> {
            if (packet.length <= mtu) {
                return listOf(packet)
            }
            val header = packet.header
            val payload = requireNotNull(packet.payload).rawData
            val maxPayloadLength = mtu - header.length
            val actualMaxPayloadLength = if (maxPayloadLength % 8 == 0) {
                maxPayloadLength
            } else {
                maxPayloadLength - maxPayloadLength % 8
            }
            val list = ArrayList<IPv4Packet>()
            var restLength = payload.size
            var srcPos = 0
            while (restLength > 0) {
                if (restLength > maxPayloadLength) {
                    val fragmentedPayload = ByteArray(actualMaxPayloadLength)
                    payload.copyInto(
                        destination = fragmentedPayload,
                        destinationOffset = 0,
                        startIndex = srcPos,
                        endIndex = actualMaxPayloadLength
                    )
                    val builder = packet.builder()
                    builder.apply {
                        moreFragmentFlag = true
                        fragmentOffset = (srcPos / 8).toShort()
                        payloadBuilder = SimplePacket.Builder(fragmentedPayload)
                    }
                    list.add(builder.build())
                    restLength -= fragmentedPayload.size
                    srcPos += fragmentedPayload.size
                } else {
                    val fragmentedPayload = ByteArray(restLength)
                    payload.copyInto(
                        destination = fragmentedPayload,
                        destinationOffset = 0,
                        startIndex = srcPos,
                        endIndex = restLength
                    )
                    val builder = packet.builder()
                    builder.apply {
                        moreFragmentFlag = false
                        fragmentOffset = (srcPos / 8).toShort()
                        payloadBuilder = SimplePacket.Builder(fragmentedPayload)
                    }
                    list.add(builder.build())
                    break
                }
            }
            return list
        }

        @JvmStatic
        fun defragment(vararg packets: IPv4Packet): IPv4Packet {
            if (packets.size == 1) return packets.first()
            return defragment(packets.toList())
        }

        @JvmStatic
        fun defragment(packets: Collection<IPv4Packet>): IPv4Packet {
            if (packets.size == 1) return packets.first()
            val sorted = packets.sortedBy { it.header.fragmentOffset }
            val lastPacketHeader = sorted.last().header
            val payloadLength = lastPacketHeader.fragmentOffset * 8 +
                lastPacketHeader.totalLength.toInt() -
                lastPacketHeader.ihl * 4
            require(payloadLength > 0) { "Can't defragment: $sorted" }
            val defragmentedPayload = ByteArray(payloadLength)
            var destPos = 0
            try {
                sorted.forEach { packet ->
                    val payload = requireNotNull(packet.payload).rawData
                    payload.copyInto(defragmentedPayload, destPos)
                    destPos += payload.size
                }
            } catch (e: Exception) {
                throw IllegalArgumentException("Can't defragment: $sorted")
            }
            val firstPacket = sorted.first()
            val builder = firstPacket.builder()
            builder.apply {
                moreFragmentFlag = false
                fragmentOffset = 0
                payloadBuilder = firstPacket.header.protocol.packet(defragmentedPayload).builder()
            }
            return builder.build()
        }
    }
}
