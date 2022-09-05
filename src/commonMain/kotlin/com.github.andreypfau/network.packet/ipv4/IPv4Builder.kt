package com.github.andreypfau.network.packet.ipv4

import com.github.andreypfau.network.address.Inet4Address
import com.github.andreypfau.network.packet.AbstractPacket
import com.github.andreypfau.network.packet.ChecksumBuilder
import com.github.andreypfau.network.packet.LengthBuilder
import com.github.andreypfau.network.packet.Packet
import com.github.andreypfau.network.packet.ip.IpProtocol
import com.github.andreypfau.network.packet.ip.IpVersion
import com.github.andreypfau.network.packet.ipv4.tos.IPv4Rfc1349Tos
import com.github.andreypfau.network.packet.tcp.TcpBuilder
import com.github.andreypfau.network.packet.transport.TransportBuilder
import com.github.andreypfau.network.packet.udp.UdpBuilder

class IPv4Builder(
    var version: IpVersion = IpVersion.IPv4,
    var ihl: Byte = 0,
    var tos: IPv4Tos = IPv4Rfc1349Tos(0),
    var totalLength: UShort = 0u,
    var identification: UShort = 0u,
    var reservedFlag: Boolean = false,
    var dontFragmentFlag: Boolean = false,
    var moreFragmentFlag: Boolean = false,
    var fragmentOffset: Short = 0,
    var ttl: UByte = 100u,
    var protocol: IpProtocol? = null,
    var headerChecksum: UShort = 0u,
    var srcAddress: Inet4Address? = null,
    var dstAddress: Inet4Address? = null,
    var options: MutableList<IPv4Option> = ArrayList(),
    var padding: ByteArray? = null,
    payloadBuilder: Packet.Builder? = null,
) : AbstractPacket.AbstractBuilder(), ChecksumBuilder<IPv4Packet>, LengthBuilder<IPv4Packet> {
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