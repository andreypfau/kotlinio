package com.github.andreypfau.kotlinio.packet.ipv4

import com.github.andreypfau.kotlinio.address.Inet4Address
import com.github.andreypfau.kotlinio.bits.getShortAt
import com.github.andreypfau.kotlinio.bits.getUShortAt
import com.github.andreypfau.kotlinio.packet.AbstractPacket
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.ip.IpPacket
import com.github.andreypfau.kotlinio.packet.ip.IpProtocol
import com.github.andreypfau.kotlinio.packet.ip.IpVersion
import com.github.andreypfau.kotlinio.packet.ipv4.tos.IPv4Rfc1349Tos
import com.github.andreypfau.kotlinio.utils.*
import kotlin.experimental.and
import kotlin.experimental.or

class IPv4Header : AbstractPacket.AbstractHeader, IpPacket.IpHeader {
    override val version: IpVersion
    val ihl: Byte
    val tos: IPv4Tos
    val totalLength: UShort
    val identification: UShort
    val reservedFlag: Boolean
    val dontFragmentFlag: Boolean
    val moreFragmentFlag: Boolean
    val fragmentOffset: Short
    val ttl: UByte
    override val protocol: IpProtocol
    val headerChecksum: UShort
    override val srcAddress: Inet4Address
    override val dstAddress: Inet4Address
    val options: List<IPv4Option>
    val padding: ByteArray

    constructor(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset) {
        val versionAndIhl = rawData[VERSION_AND_IHL_OFFSET + offset].toInt()
        version = IpVersion[((versionAndIhl and 0xF0) shr 4).toByte()]
        require(version == IpVersion.IPv4)
        ihl = (versionAndIhl and 0x0F).toByte()
        tos = IPv4Rfc1349Tos(rawData[TOS_OFFSET + offset])
        totalLength = rawData.getUShortAt(TOTAL_LENGTH_OFFSET + offset)
        identification = rawData.getUShortAt(IDENTIFICATION_OFFSET + offset)
        val flagsAndFragmentOffset = rawData.getShortAt(FLAGS_AND_FRAGMENT_OFFSET_OFFSET + offset)
        reservedFlag = (flagsAndFragmentOffset and 0x8000.toShort()) != 0.toShort()
        dontFragmentFlag = (flagsAndFragmentOffset and 0x4000.toShort()) != 0.toShort()
        moreFragmentFlag = (flagsAndFragmentOffset and 0x2000.toShort()) != 0.toShort()
        fragmentOffset = (flagsAndFragmentOffset and 0x1FFF.toShort())
        ttl = rawData[TTL_OFFSET + offset].toUByte()
        protocol = IpProtocol[rawData[PROTOCOL_OFFSET + offset]]
        headerChecksum = rawData.getUShortAt(HEADER_CHECKSUM_OFFSET + offset)
        srcAddress = Inet4Address(rawData, SRC_ADDR_OFFSET + offset)
        dstAddress = Inet4Address(rawData, DST_ADDR_OFFSET + offset)
        val headerLength = ihl.toUByte().toInt() * 4
        if (length < headerLength) {
            throw IllegalArgumentException()
        }
        if (headerLength < OPTIONS_OFFSET) {
            throw IllegalArgumentException()
        }
        val options = ArrayList<IPv4Option>()
        val currentOffsetInHeader = OPTIONS_OFFSET
//            try {
//                while (currentOffsetInHeader < headerLength) {
//                    TODO()
//                }
//            } catch (e: Exception) {
//
//            }
        this.options = options
        val paddingLength = headerLength - currentOffsetInHeader
        padding = if (paddingLength != 0) {
            rawData.copyOfRange(currentOffsetInHeader + offset, paddingLength)
        } else {
            byteArrayOf()
        }
    }

    constructor(
        builder: IPv4Builder,
        payload: Packet?
    ) {
        version = builder.version
        tos = builder.tos
        identification = builder.identification
        reservedFlag = builder.reservedFlag
        dontFragmentFlag = builder.dontFragmentFlag
        moreFragmentFlag = builder.moreFragmentFlag
        fragmentOffset = builder.fragmentOffset
        ttl = builder.ttl
        protocol = requireNotNull(builder.protocol) { "protocol == null" }
        srcAddress = requireNotNull(builder.srcAddress) { "srcAddress == null" }
        dstAddress = requireNotNull(builder.dstAddress) { "dstAddress == null" }
        options = builder.options.toList()
        padding = if (builder.paddingAtBuild) {
            val mod = measureLengthWithoutPadding() % 4
            if (mod != 0) {
                ByteArray(4 - mod)
            } else {
                byteArrayOf()
            }
        } else {
            builder.padding?.copyOf() ?: byteArrayOf()
        }
        if (builder.correctLengthAtBuild) {
            ihl = (length / 4).toByte()
            totalLength = ((payload?.length ?: 0) + length).toUShort()
        } else {
            ihl = builder.ihl
            totalLength = builder.totalLength
        }
        headerChecksum = if (builder.correctChecksumAtBuild) {
            calcHeaderChecksum(true)
        } else {
            builder.headerChecksum
        }
    }

    private fun calcHeaderChecksum(zeroInsteadOfChecksum: Boolean) =
        buildRawData(zeroInsteadOfChecksum).checksum().toUShort()

    override val rawFields: List<ByteArray>
        get() = rawFields(false)

    private fun rawFields(zeroInsteadOfChecksum: Boolean): List<ByteArray> {
        var flags: Byte = 0
        if (moreFragmentFlag) {
            flags = 1.toByte()
        }
        if (dontFragmentFlag) {
            flags = (flags.toInt() or 2).toByte()
        }
        if (reservedFlag) {
            flags = (flags.toInt() or 4).toByte()
        }
        return buildList {
            add(((version.value shl 4) or ihl).toByteArray())
            add(tos.value.toByteArray())
            add(totalLength.toByteArray())
            add(identification.toByteArray())
            add(((flags.toShort() shl 13) or fragmentOffset).toByteArray())
            add(ttl.toByte().toByteArray())
            add(protocol.value.toByteArray())
            add((if (zeroInsteadOfChecksum) 0u else headerChecksum).toByteArray())
            add(srcAddress.toByteArray())
            add(dstAddress.toByteArray())
            options.forEach {
                add(it.rawData)
            }
            add(padding)
        }
    }

    private fun buildRawData(zeroInsteadOfChecksum: Boolean) =
        rawFields(zeroInsteadOfChecksum).concatenate()

    private fun measureLengthWithoutPadding() = options.sumOf { it.length } + MIN_IPV4_HEADER_SIZE

    override fun buildString(): String = buildString {
        append("[IPv4 Header (").append(this@IPv4Header.length).append(" bytes)]").appendLine()
        append("  Version: ").append(version).appendLine()
        append("  IHL: ").append(ihl).append(" (").append(ihl * 4).append(" [bytes])").appendLine()
        append("  TOS: ").append(tos).appendLine()
        append("  Total length: ").append(totalLength).append(" [bytes]").appendLine()
        append("  Identification: ").append(identification).appendLine()
        append("  Flags: (Reserved, Don't Fragment, More Fragment) = (")
            .append(reservedFlag)
            .append(", ")
            .append(dontFragmentFlag)
            .append(", ")
            .append(moreFragmentFlag)
            .append(")")
            .appendLine()
        append("  Fragment offset: ")
            .append(fragmentOffset)
            .append(" (")
            .append(fragmentOffset * 8)
            .append(" [bytes])")
            .appendLine()
        append("  TTL: ").append(ttl).appendLine()
        append("  Protocol: ").append(protocol).appendLine()
        append("  Header checksum: 0x")
            .append(headerChecksum.toByteArray().hex())
            .appendLine()
        options.forEach { option ->
            append("  Option: ").append(option).appendLine()
        }
        if (padding.isNotEmpty()) {
            append("  Padding: 0x").append(padding.hex(" ")).appendLine()
        }
        append("  Src Address: ").append(srcAddress).appendLine()
        append("  Dst Address: ").append(dstAddress).appendLine()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        if (!super.equals(other)) return false

        other as IPv4Header

        if (identification != other.identification) return false
        if (headerChecksum != other.headerChecksum) return false
        if (srcAddress != other.srcAddress) return false
        if (dstAddress != other.dstAddress) return false
        if (totalLength != other.totalLength) return false
        if (protocol != other.protocol) return false
        if (ttl != other.ttl) return false
        if (fragmentOffset != other.fragmentOffset) return false
        if (dontFragmentFlag != other.dontFragmentFlag) return false
        if (moreFragmentFlag != other.moreFragmentFlag) return false
        if (tos != other.tos) return false
        if (ihl != other.ihl) return false
        if (version != other.version) return false
        if (reservedFlag != other.reservedFlag) return false
        if (options != other.options) return false
        if (!padding.contentEquals(other.padding)) return false

        return true
    }

    override fun calcHashCode(): Int {
        var result = 17
        result = 31 * result + version.hashCode()
        result = 31 * result + ihl
        result = 31 * result + tos.hashCode()
        result = 31 * result + totalLength.hashCode()
        result = 31 * result + identification.hashCode()
        result = 31 * result + reservedFlag.hashCode()
        result = 31 * result + dontFragmentFlag.hashCode()
        result = 31 * result + moreFragmentFlag.hashCode()
        result = 31 * result + fragmentOffset
        result = 31 * result + ttl.hashCode()
        result = 31 * result + protocol.hashCode()
        result = 31 * result + headerChecksum.hashCode()
        result = 31 * result + srcAddress.hashCode()
        result = 31 * result + dstAddress.hashCode()
        result = 31 * result + options.hashCode()
        result = 31 * result + padding.contentHashCode()
        return result
    }

    companion object {
        private const val VERSION_AND_IHL_OFFSET = 0
        private const val VERSION_AND_IHL_SIZE = Byte.SIZE_BYTES
        private const val TOS_OFFSET = VERSION_AND_IHL_OFFSET + VERSION_AND_IHL_SIZE
        private const val TOS_SIZE = Byte.SIZE_BYTES
        private const val TOTAL_LENGTH_OFFSET = TOS_OFFSET + TOS_SIZE
        private const val TOTAL_LENGTH_SIZE = Short.SIZE_BYTES
        private const val IDENTIFICATION_OFFSET = TOTAL_LENGTH_OFFSET + TOTAL_LENGTH_SIZE
        private const val IDENTIFICATION_SIZE = Short.SIZE_BYTES
        private const val FLAGS_AND_FRAGMENT_OFFSET_OFFSET = IDENTIFICATION_OFFSET + IDENTIFICATION_SIZE
        private const val FLAGS_AND_FRAGMENT_OFFSET_SIZE = Short.SIZE_BYTES
        private const val TTL_OFFSET = FLAGS_AND_FRAGMENT_OFFSET_OFFSET + FLAGS_AND_FRAGMENT_OFFSET_SIZE
        private const val TTL_SIZE = Byte.SIZE_BYTES
        private const val PROTOCOL_OFFSET = TTL_OFFSET + TTL_SIZE
        private const val PROTOCOL_SIZE = Byte.SIZE_BYTES
        private const val HEADER_CHECKSUM_OFFSET = PROTOCOL_OFFSET + PROTOCOL_SIZE
        private const val HEADER_CHECKSUM_SIZE = Short.SIZE_BYTES
        private const val SRC_ADDR_OFFSET = HEADER_CHECKSUM_OFFSET + HEADER_CHECKSUM_SIZE
        private const val SRC_ADDR_SIZE = Inet4Address.SIZE_BYTES
        private const val DST_ADDR_OFFSET = SRC_ADDR_OFFSET + SRC_ADDR_SIZE
        private const val DST_ADDR_SIZE = Inet4Address.SIZE_BYTES
        private const val OPTIONS_OFFSET = DST_ADDR_OFFSET + DST_ADDR_SIZE

        private const val MIN_IPV4_HEADER_SIZE = DST_ADDR_OFFSET + DST_ADDR_SIZE
    }
}
