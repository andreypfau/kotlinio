package com.github.andreypfau.network.packet.tcp

import com.github.andreypfau.network.address.Inet4Address
import com.github.andreypfau.network.address.InetAddress
import com.github.andreypfau.network.packet.AbstractPacket
import com.github.andreypfau.network.packet.ip.IpProtocol
import com.github.andreypfau.network.packet.transport.TransportHeader
import com.github.andreypfau.network.utils.*

/*
 *  0                              16                            31
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |          Source Port          |       Destination Port        |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                        Sequence Number                        |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                    Acknowledgment Number                      |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |  Data |           |U|A|P|R|S|F|                               |
 * | Offset| Reserved  |R|C|S|S|Y|I|            Window             |
 * |       |           |G|K|H|T|N|N|                               |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |           Checksum            |         Urgent Pointer        |
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                    Options                    |    Padding    |
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
 * |      PAD      | Protocol(TCP) |            Length             |
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
class TcpHeader : AbstractPacket.AbstractHeader, TransportHeader {
    override val srcPort: UShort
    override val dstPort: UShort
    val sequenceNumber: UInt
    val acknowledgmentNumber: UInt
    val dataOffset: UByte
    val reserved: UByte
    val urg: Boolean
    val ack: Boolean
    val psh: Boolean
    val rst: Boolean
    val syn: Boolean
    val fin: Boolean
    val window: UShort
    val checksum: UShort
    val urgentPointer: UShort
    val options: List<TcpOption>
    val padding: ByteArray

    constructor(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset) {
        require(length >= MIN_TCP_HEADER_SIZE)
        srcPort = rawData.getUShort(SRC_PORT_OFFSET + offset)
        dstPort = rawData.getUShort(DST_PORT_OFFSET + offset)
        sequenceNumber = rawData.getUInt(SEQUENCE_NUMBER_OFFSET + offset)
        acknowledgmentNumber = rawData.getUInt(ACKNOWLEDGMENT_NUMBER_OFFSET + offset)
        val dataOffsetAndReservedAndControlBits =
            rawData.getShort(DATA_OFFSET_AND_RESERVED_AND_CONTROL_BITS_OFFSET + offset).toInt()
        dataOffset = ((dataOffsetAndReservedAndControlBits and 0xF000) shr 12).toUByte()
        reserved = ((dataOffsetAndReservedAndControlBits and 0x0FC0) shr 6).toUByte()
        urg = (dataOffsetAndReservedAndControlBits and 0x0020) != 0
        ack = (dataOffsetAndReservedAndControlBits and 0x0010) != 0
        psh = (dataOffsetAndReservedAndControlBits and 0x0008) != 0
        rst = (dataOffsetAndReservedAndControlBits and 0x0004) != 0
        syn = (dataOffsetAndReservedAndControlBits and 0x0002) != 0
        fin = (dataOffsetAndReservedAndControlBits and 0x0001) != 0

        window = rawData.getUShort(WINDOW_OFFSET + offset)
        checksum = rawData.getUShort(CHECKSUM_OFFSET + offset)
        urgentPointer = rawData.getUShort(URGENT_POINTER_OFFSET + offset)

        val headerLength = dataOffset.toInt() * 4
        require(length >= headerLength)
        require(headerLength >= OPTIONS_OFFSET)
        options = ArrayList()
        val currentOffsetInHeader = OPTIONS_OFFSET
        // TODO: parse options
        val paddingLength = headerLength - currentOffsetInHeader
        padding = if (paddingLength != 0) {
            val paddingOffset = currentOffsetInHeader + offset
            rawData.copyOfRange(paddingOffset, paddingOffset + paddingLength)
        } else {
            byteArrayOf()
        }
    }

    constructor(builder: TcpBuilder, payload: ByteArray) {
        srcPort = builder.srcPort
        dstPort = builder.dstPort
        sequenceNumber = builder.sequenceNumber
        acknowledgmentNumber = builder.acknowledgmentNumber
        reserved = builder.reserved
        urg = builder.urg
        ack = builder.ack
        psh = builder.psh
        rst = builder.rst
        syn = builder.syn
        fin = builder.fin
        window = builder.window
        urgentPointer = builder.urgentPointer
        options = builder.options.toList()
        padding = if (builder.paddingAtBuild) {
            val mod = measureLengthWithoutPadding() % 4
            if (mod != 0) {
                ByteArray(4 - mod)
            } else {
                ByteArray(0)
            }
        } else {
            builder.padding?.copyOf() ?: ByteArray(0)
        }
        dataOffset = if (builder.correctLengthAtBuild) {
            (length / 4).toUByte()
        } else {
            builder.dataOffset
        }
        checksum = if (builder.correctChecksumAtBuild) {
            calcChecksum(
                requireNotNull(builder.srcAddress),
                requireNotNull(builder.dstAddress),
                buildRawData(true),
                payload
            )
        } else {
            builder.checksum
        }
    }

    private fun calcChecksum(
        srcAddress: InetAddress, dstAddress: InetAddress, header: ByteArray, payload: ByteArray
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

        data[destinationOffset] = IpProtocol.TCP.value
        destinationOffset++

        totalLength.toShort().toByteArray(data, destinationOffset)
        destinationOffset += Short.SIZE_BYTES

        return data.checksum().toUShort()
    }

    private fun buildRawData(zeroInsteadOfChecksum: Boolean) = rawFields(zeroInsteadOfChecksum).concatenate()

    private fun measureLengthWithoutPadding() = options.sumOf { it.length } + MIN_TCP_HEADER_SIZE

    override val rawFields: List<ByteArray> get() = rawFields(false)

    private fun rawFields(zeroInsteadOfChecksum: Boolean) = buildList {
        var flags: UShort = 0u
        if (fin) {
            flags = 1.toUShort()
        }
        if (syn) {
            flags = (flags.toInt() or 2).toUShort()
        }
        if (rst) {
            flags = (flags.toInt() or 4).toUShort()
        }
        if (psh) {
            flags = (flags.toInt() or 8).toUShort()
        }
        if (ack) {
            flags = (flags.toInt() or 16).toUShort()
        }
        if (urg) {
            flags = (flags.toInt() or 32).toUShort()
        }
        add(srcPort.toByteArray())
        add(dstPort.toByteArray())
        add(sequenceNumber.toByteArray())
        add(acknowledgmentNumber.toByteArray())
        add(((dataOffset.toUShort() shl 12) or (reserved.toUShort() shl 6) or flags).toByteArray())
        add(window.toByteArray())
        add((if (zeroInsteadOfChecksum) 0u else checksum).toByteArray())
        add(urgentPointer.toByteArray())
        options.forEach { option ->
            add(option.rawData)
        }
        add(padding)
    }

    override fun buildString(): String = buildString {
        append("[TCP Header (").append(length).append(" bytes)]").appendLine()
        append("  Source port: ").append(srcPort).appendLine()
        append("  Destination port: ").append(dstPort).appendLine()
        append("  Sequence Number: ").append(sequenceNumber).appendLine()
        append("  Acknowledgment Number: ").append(acknowledgmentNumber).appendLine()
        append("  Data Offset: ")
            .append(dataOffset)
            .append(" (")
            .append(dataOffset * 4u)
            .append(" [bytes])")
            .appendLine()
        append("  Reserved: ").append(reserved).appendLine()
        append("  URG: ").append(urg).appendLine()
        append("  ACK: ").append(ack).appendLine()
        append("  PSH: ").append(psh).appendLine()
        append("  RST: ").append(rst).appendLine()
        append("  SYN: ").append(syn).appendLine()
        append("  FIN: ").append(fin).appendLine()
        append("  Window: ").append(window).appendLine()
        append("  Checksum: 0x").append(checksum.toString(16)).appendLine()
        append("  Urgent Point: ").append(urgentPointer).appendLine()
        options.forEach { option ->
            append("  Option: ").append(option).appendLine()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        if (!super.equals(other)) return false

        other as TcpHeader

        if (checksum != other.checksum) return false
        if (sequenceNumber != other.sequenceNumber) return false
        if (acknowledgmentNumber != other.acknowledgmentNumber) return false
        if (dataOffset != other.dataOffset) return false
        if (srcPort != other.srcPort) return false
        if (dstPort != other.dstPort) return false
        if (urg != other.urg) return false
        if (ack != other.ack) return false
        if (psh != other.psh) return false
        if (rst != other.rst) return false
        if (syn != other.syn) return false
        if (fin != other.fin) return false
        if (window != other.window) return false
        if (urgentPointer != other.urgentPointer) return false
        if (reserved != other.reserved) return false
        if (options != other.options) return false
        if (!padding.contentEquals(other.padding)) return false

        return true
    }

    override fun calcHashCode(): Int {
        var result = 17
        result = 31 * result + srcPort.hashCode()
        result = 31 * result + dstPort.hashCode()
        result = 31 * result + sequenceNumber.hashCode()
        result = 31 * result + acknowledgmentNumber.hashCode()
        result = 31 * result + dataOffset.hashCode()
        result = 31 * result + reserved.hashCode()
        result = 31 * result + urg.hashCode()
        result = 31 * result + ack.hashCode()
        result = 31 * result + psh.hashCode()
        result = 31 * result + rst.hashCode()
        result = 31 * result + syn.hashCode()
        result = 31 * result + fin.hashCode()
        result = 31 * result + window.hashCode()
        result = 31 * result + checksum.hashCode()
        result = 31 * result + urgentPointer.hashCode()
        result = 31 * result + options.hashCode()
        result = 31 * result + padding.contentHashCode()
        return result
    }

    companion object {
        const val SRC_PORT_OFFSET = 0
        const val SRC_PORT_SIZE = Short.SIZE_BYTES
        const val DST_PORT_OFFSET = SRC_PORT_OFFSET + SRC_PORT_SIZE
        const val DST_PORT_SIZE = Short.SIZE_BYTES
        const val SEQUENCE_NUMBER_OFFSET = DST_PORT_OFFSET + DST_PORT_SIZE
        const val SEQUENCE_NUMBER_SIZE = Int.SIZE_BYTES
        const val ACKNOWLEDGMENT_NUMBER_OFFSET = SEQUENCE_NUMBER_OFFSET + SEQUENCE_NUMBER_SIZE
        const val ACKNOWLEDGMENT_NUMBER_SIZE = Int.SIZE_BYTES
        const val DATA_OFFSET_AND_RESERVED_AND_CONTROL_BITS_OFFSET =
            ACKNOWLEDGMENT_NUMBER_OFFSET + ACKNOWLEDGMENT_NUMBER_SIZE
        const val DATA_OFFSET_AND_RESERVED_AND_CONTROL_BITS_SIZE = Short.SIZE_BYTES
        const val WINDOW_OFFSET = (DATA_OFFSET_AND_RESERVED_AND_CONTROL_BITS_OFFSET
                + DATA_OFFSET_AND_RESERVED_AND_CONTROL_BITS_SIZE)
        const val WINDOW_SIZE = Short.SIZE_BYTES
        const val CHECKSUM_OFFSET = WINDOW_OFFSET + WINDOW_SIZE
        const val CHECKSUM_SIZE = Short.SIZE_BYTES
        const val URGENT_POINTER_OFFSET = CHECKSUM_OFFSET + CHECKSUM_SIZE
        const val URGENT_POINTER_SIZE = Short.SIZE_BYTES
        const val OPTIONS_OFFSET = URGENT_POINTER_OFFSET + URGENT_POINTER_SIZE

        const val MIN_TCP_HEADER_SIZE = URGENT_POINTER_OFFSET + URGENT_POINTER_SIZE

        const val IPV4_PSEUDO_HEADER_SIZE = 12
        const val IPV6_PSEUDO_HEADER_SIZE = 40
    }
}