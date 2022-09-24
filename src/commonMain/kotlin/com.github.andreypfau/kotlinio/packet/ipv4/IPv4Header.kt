package com.github.andreypfau.kotlinio.packet.ipv4

import com.github.andreypfau.kotlinio.address.Inet4Address
import com.github.andreypfau.kotlinio.bits.getUShortAt
import com.github.andreypfau.kotlinio.bits.toInt
import com.github.andreypfau.kotlinio.packet.AbstractPacket
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.ip.IpPacket
import com.github.andreypfau.kotlinio.packet.ip.IpProtocol
import com.github.andreypfau.kotlinio.packet.ip.IpVersion
import com.github.andreypfau.kotlinio.packet.ipv4.tos.IPv4Rfc1349Tos
import com.github.andreypfau.kotlinio.utils.*
import kotlin.experimental.or

class IPv4Header : AbstractPacket.AbstractHeader, IpPacket.IpHeader {
    override val version: IpVersion
    val ihl: Byte
    val tos: IPv4Tos
    val totalLength: UShort
    val identification: UShort
    val flags: UShort
    val ttl: UByte
    override val protocol: IpProtocol
    val headerChecksum: UShort
    override val srcAddress: Inet4Address
    override val dstAddress: Inet4Address
    val options: List<IPv4Option>
    val padding: ByteArray

    val reservedBit get() = (flags.toInt() and 0x8000) != 0
    val dontFragment get() = (flags.toInt() and 0x4000) != 0
    val moreFragments get() = (flags.toInt() and 0x2000) != 0
    val fragmentOffset get() = (flags.toInt() and 0x1FFF).toUShort()
    val isFragmented: Boolean get() = moreFragments || fragmentOffset.toInt() != 0

    constructor(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset) {
        val versionAndIhl = rawData[VERSION_AND_IHL_OFFSET + offset].toInt()
        version = IpVersion[((versionAndIhl and 0xF0) shr 4).toByte()]
        require(version == IpVersion.IPv4)
        ihl = (versionAndIhl and 0x0F).toByte()
        tos = IPv4Rfc1349Tos(rawData[TOS_OFFSET + offset])
        totalLength = rawData.getUShortAt(TOTAL_LENGTH_OFFSET + offset)
        identification = rawData.getUShortAt(IDENTIFICATION_OFFSET + offset)
        flags = rawData.getUShortAt(FLAGS_AND_FRAGMENT_OFFSET_OFFSET + offset)
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
        var currentOffsetInHeader = OPTIONS_OFFSET
//        try {
//            while (currentOffsetInHeader < headerLength) {
//                val type = IPv4OptionType[rawData[currentOffsetInHeader + offset].toUByte()]
//                val o = type(rawData, currentOffsetInHeader + offset, headerLength - currentOffsetInHeader)
//                options.add(o)
//                currentOffsetInHeader += o.length
//                if (o.type == IPv4OptionType.END_OF_OPTION_LIST) {
//                    break
//                }
//            }
//        } catch (_: Exception) {
//        }
        this.options = options
        val paddingLength = headerLength - currentOffsetInHeader
        val startIndex = currentOffsetInHeader + offset
        val endIndex = currentOffsetInHeader + offset + paddingLength
        padding = if (paddingLength != 0) {
            rawData.copyOfRange(startIndex, endIndex)
        } else {
            byteArrayOf()
        }
    }

    constructor(
        builder: IPv4Builder,
        payload: Packet?
    ) {
        version = requireNotNull(builder.version)
        tos = builder.tos
        identification = builder.identification
        var flags: UShort = (fragmentOffset shl 3) shr 3
        if (builder.reservedFlag) {
            flags = flags or (1.toUShort() shl 15)
        }
        if (builder.dontFragmentFlag) {
            flags = flags or (1.toUShort() shl 14)
        }
        if (builder.moreFragmentFlag) {
            flags = flags or (1.toUShort() shl 13)
        }
        this.flags = flags
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
        return buildList {
            add(((version.value shl 4) or ihl).toByteArray())
            add(tos.value.toByteArray())
            add(totalLength.toByteArray())
            add(identification.toByteArray())
            add(flags.toByteArray())
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
        append("Internet Protocol Version 4, Src: ")
            .append(srcAddress)
            .append(", Dst: ")
            .append(dstAddress).appendLine()
        append("  ")
            .append(version.value.toString(2).padStart(4, '0'))
            .append(" .... = Version: ").append(version.value).appendLine()
        append("  ")
            .append(".... ")
            .append(ihl.toString(2).padStart(4, '0'))
            .append(" = Header Length: ")
            .append(ihl * 4).append(" bytes ")
            .append("(").append(ihl).append(")").appendLine()
        append("  TOS: ").append(tos).appendLine()
        append("  Total Length: ").append(totalLength).appendLine()
        append("  Identification: 0x")
            .append(identification.toString(16).padStart(4, '0'))
            .append(" (").append(identification).append(")").appendLine()

        append("  Flags: 0x").append((flags and 0xE000u).toString(16).padStart(2, '0'))
        if (reservedBit) append(", Reserved bit")
        if (dontFragment) append(", Don't fragment")
        if (moreFragments) append(", More fragments")
        appendLine()

        append("    ").append(reservedBit.toInt()).append("... .... = Reserved bit: ")
            .append(reservedBit).appendLine()
        append("    .").append(dontFragment.toInt()).append(".. .... = Don't fragment: ")
            .append(dontFragment).appendLine()
        append("    ..").append(moreFragments.toInt()).append(". .... = More fragments: ")
            .append(moreFragments).appendLine()
        append("    ...")
            .append(fragmentOffset.toString(2).padStart(13, '0'))
            .append(" = Fragment Offset: ")
            .append(fragmentOffset).appendLine()

        append("  Time To Live: ").append(ttl).appendLine()
        append("  Protocol: ").append(protocol).appendLine()
        append("  Header Checksum: 0x")
            .append(headerChecksum.toByteArray().hex())
            .appendLine()
        append("  Source Address: ").append(srcAddress).appendLine()
        append("  Destination Address: ").append(dstAddress).appendLine()
        options.forEach { option ->
            append("  Option: ").append(option).appendLine()
        }
        if (padding.isNotEmpty()) {
            append("  Padding: 0x").append(padding.hex(" ")).appendLine()
        }
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
        if (flags != other.flags) return false
        if (tos != other.tos) return false
        if (ihl != other.ihl) return false
        if (version != other.version) return false
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
        result = 31 * result + flags.hashCode()
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
