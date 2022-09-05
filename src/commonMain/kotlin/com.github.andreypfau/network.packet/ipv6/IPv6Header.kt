package com.github.andreypfau.network.packet.ipv6

import com.github.andreypfau.network.address.Inet6Address
import com.github.andreypfau.network.packet.AbstractPacket
import com.github.andreypfau.network.packet.Packet
import com.github.andreypfau.network.packet.ip.IpPacket
import com.github.andreypfau.network.packet.ip.IpProtocol
import com.github.andreypfau.network.packet.ip.IpVersion
import com.github.andreypfau.network.utils.getUInt
import com.github.andreypfau.network.utils.getUShort
import com.github.andreypfau.network.utils.toByteArray
import com.github.andreypfau.network.utils.ushr

class IPv6Header : AbstractPacket.AbstractHeader, IpPacket.IpHeader {
    override val version: IpVersion
    val trafficClass: UByte
    val flowLabel: UInt
    val payloadLength: UShort
    val nextHeader: IpProtocol
    val hopLimit: UByte
    override val srcAddress: Inet6Address
    override val dstAddress: Inet6Address
    override val protocol: IpProtocol
        get() = nextHeader

    constructor(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size) {
        require(length >= IPV6_HEADER_SIZE)
        val versionAndTrafficClassAndFlowLabel =
            rawData.getUInt(VERSION_AND_TRAFFIC_CLASS_AND_FLOW_LABEL_OFFSET + offset)
        version = IpVersion[(versionAndTrafficClassAndFlowLabel ushr 28).toByte()]
        trafficClass = (versionAndTrafficClassAndFlowLabel shr 20).toUByte()
        flowLabel = rawData.getUInt(VERSION_AND_TRAFFIC_CLASS_AND_FLOW_LABEL_OFFSET + offset)
        payloadLength = rawData.getUShort(PAYLOAD_LENGTH_OFFSET + offset)
        nextHeader = IpProtocol[rawData[NEXT_HEADER_OFFSET + offset]]
        hopLimit = rawData[HOP_LIMIT_OFFSET + offset].toUByte()
        srcAddress = Inet6Address(rawData, SRC_ADDR_OFFSET + offset)
        dstAddress = Inet6Address(rawData, DST_ADDR_OFFSET + offset)
    }

    constructor(builder: IPv6Builder, payload: Packet?) {
        version = builder.version
        trafficClass = builder.trafficClass
        flowLabel = builder.flowLabel
        nextHeader = requireNotNull(builder.nextHeader) { "nextHeader == null" }
        hopLimit = builder.hopLimit
        srcAddress = requireNotNull(builder.srcAddress) { "srcAddress == null" }
        dstAddress = requireNotNull(builder.dstAddress) { "dstAddress == null" }
        payloadLength = if (builder.correctLengthAtBuild) {
            payload?.length?.toUShort() ?: builder.payloadLength
        } else {
            builder.payloadLength
        }
    }

    override val rawFields: List<ByteArray>
        get() = buildList {
            add(((version.value.toUInt() shl 28) or (trafficClass.toUInt() shl 20) or flowLabel).toByteArray())
            add(payloadLength.toByteArray())
            add(nextHeader.value.toByteArray())
            add(hopLimit.toByte().toByteArray())
            add(srcAddress.toByteArray())
            add(dstAddress.toByteArray())
        }

    override val length: Int = IPV6_HEADER_SIZE

    override fun buildString(): String = buildString {
        append("[IPv6 Header (").append(length).append(" bytes)]").appendLine()
        append("  Version: ").append(version).appendLine()
        append("  Traffic Class: ").append(trafficClass).appendLine()
        append("  Flow Label: ").append(flowLabel).appendLine()
        append("  Payload length: ").append(payloadLength).append(" [bytes]").appendLine()
        append("  Next Header: ").append(nextHeader).appendLine()
        append("  Hop Limit: ").append(hopLimit).appendLine()
        append("  Source address: ").append(srcAddress).appendLine()
        append("  Destination address: ").append(dstAddress).appendLine()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        if (!super.equals(other)) return false

        other as IPv6Header

        if (srcAddress != other.srcAddress) return false
        if (dstAddress != other.dstAddress) return false
        if (payloadLength != other.payloadLength) return false
        if (hopLimit != other.hopLimit) return false
        if (nextHeader != other.nextHeader) return false
        if (trafficClass != other.trafficClass) return false
        if (flowLabel != other.flowLabel) return false
        if (version != other.version) return false

        return true
    }

    override fun calcHashCode(): Int {
        var result = 17
        result = 31 * result + version.hashCode()
        result = 31 * result + trafficClass.hashCode()
        result = 31 * result + flowLabel.hashCode()
        result = 31 * result + payloadLength.hashCode()
        result = 31 * result + nextHeader.hashCode()
        result = 31 * result + hopLimit.hashCode()
        result = 31 * result + srcAddress.hashCode()
        result = 31 * result + dstAddress.hashCode()
        return result
    }

    companion object {
        private const val VERSION_AND_TRAFFIC_CLASS_AND_FLOW_LABEL_OFFSET = 0
        private const val VERSION_AND_TRAFFIC_CLASS_AND_FLOW_LABEL_SIZE = Int.SIZE_BYTES
        private const val PAYLOAD_LENGTH_OFFSET =
            (VERSION_AND_TRAFFIC_CLASS_AND_FLOW_LABEL_OFFSET + VERSION_AND_TRAFFIC_CLASS_AND_FLOW_LABEL_SIZE)
        private const val PAYLOAD_LENGTH_SIZE = Short.SIZE_BYTES
        private const val NEXT_HEADER_OFFSET = PAYLOAD_LENGTH_OFFSET + PAYLOAD_LENGTH_SIZE
        private const val NEXT_HEADER_SIZE = Byte.SIZE_BYTES
        private const val HOP_LIMIT_OFFSET = NEXT_HEADER_OFFSET + NEXT_HEADER_SIZE
        private const val HOP_LIMIT_SIZE = Byte.SIZE_BYTES
        private const val SRC_ADDR_OFFSET = HOP_LIMIT_OFFSET + HOP_LIMIT_SIZE
        private const val SRC_ADDR_SIZE = Inet6Address.SIZE_BYTES
        private const val DST_ADDR_OFFSET = SRC_ADDR_OFFSET + SRC_ADDR_SIZE
        private const val DST_ADDR_SIZE = Inet6Address.SIZE_BYTES
        private const val IPV6_HEADER_SIZE = DST_ADDR_OFFSET + DST_ADDR_SIZE
    }
}