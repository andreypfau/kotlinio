package com.github.andreypfau.network.packet.udp

import com.github.andreypfau.network.address.Inet4Address
import com.github.andreypfau.network.address.InetAddress
import com.github.andreypfau.network.packet.AbstractPacket
import com.github.andreypfau.network.packet.ip.IpProtocol
import com.github.andreypfau.network.packet.transport.TransportHeader
import com.github.andreypfau.network.utils.*

/*
    *  0                              16                            31
    * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    * |           Src Port            |           Dst Port            |
    * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    * |            Length             |           Checksum            |
    * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    */

/*
 *                        IPv4 Pseudo Header
 *
 * 0                               16                            31
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                       Src IP Address                          |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                       Dst IP Address                          |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |      PAD      | Protocol(UDP) |            Length             |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *
 *                      IPv6 Pseudo Header
 *
 *  0                              16                            31
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                                                               |
 * +                                                               +
 * |                                                               |
 * +                         Source Address                        +
 * |                                                               |
 * +                                                               +
 * |                                                               |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                                                               |
 * +                                                               +
 * |                                                               |
 * +                      Destination Address                      +
 * |                                                               |
 * +                                                               +
 * |                                                               |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                   Upper-Layer Packet Length                   |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                      zero                     |  Next Header  |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 */
class UdpHeader : AbstractPacket.AbstractHeader, TransportHeader {
    override val srcPort: UShort
    override val dstPort: UShort
    val packetLength: UShort
    val checksum: UShort
    override val length: Int = UDP_HEADER_SIZE

    constructor(rawData: ByteArray, offset: Int, length: Int) {
        require(length >= UDP_HEADER_SIZE)
        srcPort = rawData.getUShort(SRC_PORT_OFFSET + offset)
        dstPort = rawData.getUShort(DST_PORT_OFFSET + offset)
        packetLength = rawData.getUShort(LENGTH_OFFSET + offset)
        checksum = rawData.getUShort(CHECKSUM_OFFSET + offset)
    }

    constructor(builder: UdpBuilder, payload: ByteArray) {
        srcPort = builder.srcPort
        dstPort = builder.dstPort
        packetLength = if (builder.correctLengthAtBuild) {
            (payload.size + length).toUShort()
        } else {
            builder.length
        }
        checksum = if (builder.correctChecksumAtBuild) {
            calcChecksum(
                requireNotNull(builder.srcAddress) { "srcAddress == null" },
                requireNotNull(builder.dstAddress) { "dstAddress == null" },
                buildRawData(true),
                payload
            )
        } else {
            builder.checksum
        }
    }

    private fun calcChecksum(
        srcAddress: InetAddress,
        dstAddress: InetAddress,
        header: ByteArray,
        payload: ByteArray
    ): UShort {
        val data: ByteArray
        var destinationOffset: Int
        val totalLength = payload.size + length
        val lowerLayerIsIpV4 = srcAddress is Inet4Address
        val pseudoHeaderSize = if (lowerLayerIsIpV4) IPV4_PSEUDO_HEADER_SIZE else IPV6_PSEUDO_HEADER_SIZE
        if (totalLength % 2 != 0) {
            data = ByteArray(totalLength + 1 + pseudoHeaderSize)
            destinationOffset = totalLength + 1
        } else {
            data = ByteArray(totalLength + pseudoHeaderSize)
            destinationOffset = totalLength
        }
        header.copyInto(data, 0)
        payload.copyInto(data, header.size)

        srcAddress.toByteArray(data, destinationOffset)
        destinationOffset += srcAddress.size
        dstAddress.toByteArray(data, destinationOffset)
        destinationOffset += dstAddress.size

        if (lowerLayerIsIpV4) {
            destinationOffset++
        } else {
            destinationOffset += 3
        }

        data[destinationOffset] = IpProtocol.UDP.value
        destinationOffset++

        totalLength.toShort().toByteArray(data, destinationOffset)
        destinationOffset += Short.SIZE_BYTES

        return data.checksum().toUShort()
    }

    override val rawFields: List<ByteArray>
        get() = rawFields(false)

    private fun buildRawData(zeroInsteadOfChecksum: Boolean) = rawFields(zeroInsteadOfChecksum).concatenate()

    private fun rawFields(zeroInsteadOfChecksum: Boolean) = buildList {
        add(srcPort.toByteArray())
        add(dstPort.toByteArray())
        add(packetLength.toByteArray())
        add((if (zeroInsteadOfChecksum) 0u else checksum).toByteArray())
    }

    override fun buildString(): String = buildString {
        append("[UDP Header (").append(this@UdpHeader.length).append(" bytes)]").appendLine()
        append("  Source port: ").append(srcPort).appendLine()
        append("  Destination port: ").append(dstPort).appendLine()
        append("  Length: ").append(packetLength).appendLine()
        append("  Checksum: 0x").append(checksum.toByteArray().hex()).appendLine()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        if (!super.equals(other)) return false

        other as UdpHeader

        if (checksum != other.checksum) return false
        if (packetLength != other.packetLength) return false
        if (srcPort != other.srcPort) return false
        if (dstPort != other.dstPort) return false

        return true
    }

    override fun calcHashCode(): Int {
        var result = 17
        result = 31 * result + srcPort.hashCode()
        result = 31 * result + dstPort.hashCode()
        result = 31 * result + packetLength.hashCode()
        result = 31 * result + checksum.hashCode()
        return result
    }

    companion object {
        private const val SRC_PORT_OFFSET = 0
        private const val SRC_PORT_SIZE = Short.SIZE_BYTES
        private const val DST_PORT_OFFSET = SRC_PORT_OFFSET + SRC_PORT_SIZE
        private const val DST_PORT_SIZE = Short.SIZE_BYTES
        private const val LENGTH_OFFSET = DST_PORT_OFFSET + DST_PORT_SIZE
        private const val LENGTH_SIZE = Short.SIZE_BYTES
        private const val CHECKSUM_OFFSET = LENGTH_OFFSET + LENGTH_SIZE
        private const val CHECKSUM_SIZE = Short.SIZE_BYTES
        private const val UDP_HEADER_SIZE = CHECKSUM_OFFSET + CHECKSUM_SIZE

        private const val IPV4_PSEUDO_HEADER_SIZE = 12
        private const val IPV6_PSEUDO_HEADER_SIZE = 40
    }
}