package com.github.andreypfau.kotlinio.packet.ipv4.header

import com.github.andreypfau.kotlinio.address.Inet4Address
import com.github.andreypfau.kotlinio.bits.binary
import com.github.andreypfau.kotlinio.bits.setUShortAt
import com.github.andreypfau.kotlinio.bits.toInt
import com.github.andreypfau.kotlinio.packet.ip.IpVersion
import com.github.andreypfau.kotlinio.utils.checksum
import com.github.andreypfau.kotlinio.utils.hex
import com.github.andreypfau.kotlinio.utils.toByteArray
import kotlin.experimental.and

internal abstract class AbstractIpV4Header : IpV4Header {
    abstract override val versionAndIhl: Byte
    override val version: IpVersion
        get() = IpVersion[versionAndIhl]
    override val ihl: UByte
        get() = (versionAndIhl and 0x0F).toUByte()

    @Deprecated("Deprecated", replaceWith = ReplaceWith("toByteArray()"))
    override val rawData: ByteArray
        get() = toByteArray()
    private val _string by lazy {
        buildString {
            append("Internet Protocol Version 4, Src: ")
                .append(srcAddress)
                .append(", Dst: ")
                .append(dstAddress).appendLine()
            append("  ")
                .append(version.value.binary().substring(4, 8))
                .append(" .... = Version: ").append(version.value).appendLine()
            append("  ")
                .append(".... ")
                .append(ihl.binary().substring(4, 8))
                .append(" = Header Length: ")
                .append(ihl * 4u).append(" bytes ")
                .append("(").append(ihl).append(")").appendLine()
            append("  TOS: ").append(tos).appendLine()
            append("  Total Length: ").append(totalLength).appendLine()
            append("  Identification: 0x")
                .append(identification.hex())
                .append(" (").append(identification).append(")").appendLine()

            append("  Flags: 0x").append((flags and 0xE000u).hex())
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
            val fragmentOffsetStr = fragmentOffset.binary()
            append("    ...")
                .append(fragmentOffsetStr[3])
                .append(" ")
                .append(fragmentOffsetStr.substring(4))
                .append(" = Fragment Offset: ")
                .append(fragmentOffset).appendLine()

            append("  Time To Live: ").append(ttl).appendLine()
            append("  Protocol: ").append(protocol).appendLine()
            append("  Header Checksum: 0x")
                .append(headerChecksum.toByteArray().hex())
                .append(" = Valid: ")
                .append(isValidChecksum())
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
    }
    private val _hashCode by lazy {
        var result = 17
        result = 31 * result + version.hashCode()
        result = 31 * result + ihl.hashCode()
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
        result
    }

    override fun calcChecksum(): UShort {
        val data = toByteArray()
        data.setUShortAt(HEADER_CHECKSUM_OFFSET, 0u)
        return data.checksum().toUShort()
    }

    override fun toString(): String = _string
    override fun hashCode(): Int = _hashCode

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IpV4Header) return false

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

    abstract override fun toByteArray(destination: ByteArray, offset: Int): ByteArray

    internal companion object {
        const val VERSION_AND_IHL_OFFSET = 0
        const val VERSION_AND_IHL_SIZE = Byte.SIZE_BYTES
        const val TOS_OFFSET = VERSION_AND_IHL_OFFSET + VERSION_AND_IHL_SIZE
        const val TOS_SIZE = Byte.SIZE_BYTES
        const val TOTAL_LENGTH_OFFSET = TOS_OFFSET + TOS_SIZE
        const val TOTAL_LENGTH_SIZE = Short.SIZE_BYTES
        const val IDENTIFICATION_OFFSET = TOTAL_LENGTH_OFFSET + TOTAL_LENGTH_SIZE
        const val IDENTIFICATION_SIZE = Short.SIZE_BYTES
        const val FLAGS_AND_FRAGMENT_OFFSET_OFFSET = IDENTIFICATION_OFFSET + IDENTIFICATION_SIZE
        const val FLAGS_AND_FRAGMENT_OFFSET_SIZE = Short.SIZE_BYTES
        const val TTL_OFFSET = FLAGS_AND_FRAGMENT_OFFSET_OFFSET + FLAGS_AND_FRAGMENT_OFFSET_SIZE
        const val TTL_SIZE = Byte.SIZE_BYTES
        const val PROTOCOL_OFFSET = TTL_OFFSET + TTL_SIZE
        const val PROTOCOL_SIZE = Byte.SIZE_BYTES
        const val HEADER_CHECKSUM_OFFSET = PROTOCOL_OFFSET + PROTOCOL_SIZE
        const val HEADER_CHECKSUM_SIZE = Short.SIZE_BYTES
        const val SRC_ADDR_OFFSET = HEADER_CHECKSUM_OFFSET + HEADER_CHECKSUM_SIZE
        const val SRC_ADDR_SIZE = Inet4Address.SIZE_BYTES
        const val DST_ADDR_OFFSET = SRC_ADDR_OFFSET + SRC_ADDR_SIZE
        const val DST_ADDR_SIZE = Inet4Address.SIZE_BYTES
        const val OPTIONS_OFFSET = DST_ADDR_OFFSET + DST_ADDR_SIZE

        const val MIN_IPV4_HEADER_SIZE = DST_ADDR_OFFSET + DST_ADDR_SIZE
        const val MAX_IPV4_HEADER_SIZE = MIN_IPV4_HEADER_SIZE + 40
    }
}
