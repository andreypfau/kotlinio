package com.github.andreypfau.kotlinio.packet.ipv4

import com.github.andreypfau.kotlinio.address.Inet4Address
import com.github.andreypfau.kotlinio.packet.AbstractPacket
import com.github.andreypfau.kotlinio.packet.ChecksumBuilder
import com.github.andreypfau.kotlinio.packet.LengthBuilder
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.ip.IpPacket
import com.github.andreypfau.kotlinio.packet.ip.IpProtocol
import com.github.andreypfau.kotlinio.packet.ip.IpVersion
import com.github.andreypfau.kotlinio.packet.ipv4.header.AbstractIpV4Header
import com.github.andreypfau.kotlinio.packet.ipv4.header.FieldBackedIpV4Header
import com.github.andreypfau.kotlinio.packet.ipv4.header.IpV4Header.Companion.DONT_FRAGMENT_MASK
import com.github.andreypfau.kotlinio.packet.ipv4.header.IpV4Header.Companion.FLAGS_MASK
import com.github.andreypfau.kotlinio.packet.ipv4.header.IpV4Header.Companion.FRAGMENT_OFFSET_MASK
import com.github.andreypfau.kotlinio.packet.ipv4.header.IpV4Header.Companion.IHL_MASK
import com.github.andreypfau.kotlinio.packet.ipv4.header.IpV4Header.Companion.MORE_FRAGMENTS_MASK
import com.github.andreypfau.kotlinio.packet.ipv4.header.IpV4Header.Companion.RESERVED_BIT_MASK
import com.github.andreypfau.kotlinio.packet.ipv4.header.IpV4Header.Companion.VERSION_MASK
import com.github.andreypfau.kotlinio.packet.ipv4.option.IPv4Option
import com.github.andreypfau.kotlinio.packet.ipv4.tos.IPv4Rfc1349Tos
import com.github.andreypfau.kotlinio.packet.ipv4.tos.IPv4Tos
import com.github.andreypfau.kotlinio.packet.tcp.TcpBuilder
import com.github.andreypfau.kotlinio.packet.transport.TransportBuilder
import com.github.andreypfau.kotlinio.packet.udp.UdpBuilder

class IpV4Builder(
    var versionAndIhl: Byte = 0x40,
    var tos: IPv4Tos = IPv4Rfc1349Tos(0),
    var totalLength: UShort = 0u,
    var identification: UShort = 0u,
    var flags: UShort = 0u,
    var ttl: UByte = 100u,
    override var protocol: IpProtocol? = null,
    var headerChecksum: UShort = 0u,
    srcAddress: Inet4Address = Inet4Address(),
    dstAddress: Inet4Address = Inet4Address(),
    var options: MutableList<IPv4Option> = ArrayList(),
    var padding: ByteArray = byteArrayOf(),
    payloadBuilder: Packet.Builder? = null,
) : AbstractPacket.AbstractBuilder(),
    IpPacket.IpBuilder<Inet4Address>,
    ChecksumBuilder<IpV4Packet>,
    LengthBuilder<IpV4Packet> {
    override var version: IpVersion
        get() = IpVersion[((versionAndIhl.toInt() and VERSION_MASK) ushr 4).toByte()]
        set(value) {
            versionAndIhl = ((value.value.toInt() shl 4) or
                (versionAndIhl.toInt() and IHL_MASK)).toByte()
        }
    var ihl: UByte
        get() = (versionAndIhl.toInt() and IHL_MASK).toUByte()
        set(value) {
            versionAndIhl = ((versionAndIhl.toInt() and VERSION_MASK) or
                (value.toInt() and IHL_MASK)).toByte()
        }
    var reservedFlag: Boolean
        get() = (flags.toInt() and RESERVED_BIT_MASK) != 0
        set(value) {
            flags = if (value) {
                flags or RESERVED_BIT_MASK.toUShort()
            } else {
                flags and RESERVED_BIT_MASK.toUShort().inv()
            }
        }
    var dontFragmentFlag: Boolean
        get() = (flags.toInt() and DONT_FRAGMENT_MASK) != 0
        set(value) {
            flags = if (value) {
                flags or DONT_FRAGMENT_MASK.toUShort()
            } else {
                flags and DONT_FRAGMENT_MASK.toUShort().inv()
            }
        }
    var moreFragmentFlag: Boolean
        get() = (flags.toInt() and MORE_FRAGMENTS_MASK) != 0
        set(value) {
            flags = if (value) {
                flags or MORE_FRAGMENTS_MASK.toUShort()
            } else {
                flags and MORE_FRAGMENTS_MASK.toUShort().inv()
            }
        }
    var fragmentOffset: UShort
        get() = (flags.toInt() and FRAGMENT_OFFSET_MASK).toUShort()
        set(value) {
            flags = ((flags.toInt() and FLAGS_MASK) or (value.toInt() and FRAGMENT_OFFSET_MASK)).toUShort()
        }

    override var correctChecksumAtBuild: Boolean = true
    override var correctLengthAtBuild: Boolean = true
    override var payloadBuilder: Packet.Builder? = payloadBuilder
        set(value) {
            field = value.also {
                if (it is TransportBuilder) {
                    it.srcAddress = srcAddress
                    it.dstAddress = dstAddress
                }
                if (it is UdpBuilder) protocol = IpProtocol.UDP
                if (it is TcpBuilder) protocol = IpProtocol.TCP
            }
        }
    override var srcAddress: Inet4Address = srcAddress
        set(value) {
            field = value
            payloadBuilder?.also {
                if (it is TransportBuilder) {
                    it.srcAddress = value
                }
            }
        }
    override var dstAddress: Inet4Address = dstAddress
        set(value) {
            field = value
            payloadBuilder?.also {
                if (it is TransportBuilder) {
                    it.dstAddress = value
                }
            }
        }

    override fun build(): IpV4Packet {
        val payload = payloadBuilder?.build()
        if (correctLengthAtBuild) {
            val length = AbstractIpV4Header.MIN_IPV4_HEADER_SIZE + options.sumOf { it.length } + padding.size
            ihl = (length / 4).toUByte()
            totalLength = (length + (payload?.length ?: 0)).toUShort()
        }
        val header = FieldBackedIpV4Header(
            versionAndIhl,
            tos,
            totalLength,
            identification,
            flags,
            ttl,
            requireNotNull(protocol) { "protocol == null" },
            headerChecksum,
            srcAddress,
            dstAddress,
            options.toList(),
            padding.copyOf()
        )
        if (correctChecksumAtBuild) {
            header.headerChecksum = header.calcChecksum()
        }
        return FieldBackedIpV4Packet(header, payload)
    }
}
