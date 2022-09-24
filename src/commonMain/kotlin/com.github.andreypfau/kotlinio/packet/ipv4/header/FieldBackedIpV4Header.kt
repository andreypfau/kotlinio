package com.github.andreypfau.kotlinio.packet.ipv4.header

import com.github.andreypfau.kotlinio.address.Inet4Address
import com.github.andreypfau.kotlinio.bits.set
import com.github.andreypfau.kotlinio.bits.setUShortAt
import com.github.andreypfau.kotlinio.packet.ip.IpProtocol
import com.github.andreypfau.kotlinio.packet.ipv4.option.IPv4Option
import com.github.andreypfau.kotlinio.packet.ipv4.tos.IPv4Tos

internal class FieldBackedIpV4Header(
    override val versionAndIhl: Byte,
    override val tos: IPv4Tos,
    override val totalLength: UShort,
    override val identification: UShort,
    override val flags: UShort,
    override val ttl: UByte,
    override val protocol: IpProtocol,
    override var headerChecksum: UShort,
    override val srcAddress: Inet4Address,
    override val dstAddress: Inet4Address,
    override val options: List<IPv4Option>,
    override val padding: ByteArray
) : AbstractIpV4Header() {
    override fun toByteArray(destination: ByteArray, offset: Int): ByteArray {
        destination[VERSION_AND_IHL_OFFSET + offset] = versionAndIhl
        destination[TOS_OFFSET + offset] = tos.value
        destination.setUShortAt(TOTAL_LENGTH_OFFSET + offset, totalLength)
        destination.setUShortAt(IDENTIFICATION_OFFSET + offset, identification)
        destination.setUShortAt(FLAGS_AND_FRAGMENT_OFFSET_OFFSET + offset, flags)
        destination[TTL_OFFSET + offset] = ttl
        destination[PROTOCOL_OFFSET + offset] = protocol.value
        destination.setUShortAt(HEADER_CHECKSUM_OFFSET + offset, headerChecksum)
        srcAddress.toByteArray(destination, SRC_ADDR_OFFSET + offset)
        dstAddress.toByteArray(destination, DST_ADDR_OFFSET + offset)
        padding.copyInto(destination, OPTIONS_OFFSET + offset)
        return destination
    }
}
