package com.github.andreypfau.kotlinio.packet.ipv4

import com.github.andreypfau.kotlinio.address.Inet4Address
import com.github.andreypfau.kotlinio.packet.AbstractPacket
import com.github.andreypfau.kotlinio.packet.ChecksumBuilder
import com.github.andreypfau.kotlinio.packet.LengthBuilder
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.ip.IpPacket
import com.github.andreypfau.kotlinio.packet.ip.IpProtocol
import com.github.andreypfau.kotlinio.packet.ip.IpVersion
import com.github.andreypfau.kotlinio.packet.ipv4.tos.IPv4Rfc1349Tos
import com.github.andreypfau.kotlinio.packet.tcp.TcpBuilder
import com.github.andreypfau.kotlinio.packet.transport.TransportBuilder
import com.github.andreypfau.kotlinio.packet.udp.UdpBuilder

class IPv4Builder(
    override var version: IpVersion? = IpVersion.IPv4,
    var ihl: Byte = 0,
    var tos: IPv4Tos = IPv4Rfc1349Tos(0),
    var totalLength: UShort = 0u,
    var identification: UShort = 0u,
    var reservedFlag: Boolean = false,
    var dontFragmentFlag: Boolean = false,
    var moreFragmentFlag: Boolean = false,
    var fragmentOffset: UShort = 0u,
    var ttl: UByte = 100u,
    override var protocol: IpProtocol? = null,
    var headerChecksum: UShort = 0u,
    override var srcAddress: Inet4Address? = null,
    override var dstAddress: Inet4Address? = null,
    var options: MutableList<IPv4Option> = ArrayList(),
    var padding: ByteArray? = null,
    payloadBuilder: Packet.Builder? = null,
) : IpPacket.IpBuilder<Inet4Address>, AbstractPacket.AbstractBuilder(), ChecksumBuilder<IPv4Packet>,
    LengthBuilder<IPv4Packet> {
    override var correctChecksumAtBuild: Boolean = true
    override var correctLengthAtBuild: Boolean = true
    var paddingAtBuild: Boolean = true
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

    override fun build(): IPv4Packet =
        IPv4Packet(this)
}
