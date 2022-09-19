package com.github.andreypfau.kotlinio.packet.ipv6

import com.github.andreypfau.kotlinio.address.Inet6Address
import com.github.andreypfau.kotlinio.packet.AbstractPacket
import com.github.andreypfau.kotlinio.packet.LengthBuilder
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.ip.IpProtocol
import com.github.andreypfau.kotlinio.packet.ip.IpVersion
import com.github.andreypfau.kotlinio.packet.tcp.TcpBuilder
import com.github.andreypfau.kotlinio.packet.transport.TransportBuilder
import com.github.andreypfau.kotlinio.packet.udp.UdpBuilder

class IPv6Builder(
    var version: IpVersion = IpVersion.IPv6,
    var trafficClass: UByte = 0u,
    var flowLabel: UInt = 0u,
    var payloadLength: UShort = 0u,
    var nextHeader: IpProtocol? = null,
    var hopLimit: UByte = 100u,
    var srcAddress: Inet6Address? = null,
    var dstAddress: Inet6Address? = null,
    payloadBuilder: Packet.Builder? = null
) : AbstractPacket.AbstractBuilder(), LengthBuilder<IPv6Packet> {
    override var payloadBuilder: Packet.Builder? = payloadBuilder
        set(value) {
            field = value.also {
                if (it is TransportBuilder) {
                    it.srcAddress = srcAddress
                    it.dstAddress = dstAddress
                }
                if (it is UdpBuilder) nextHeader = IpProtocol.UDP
                if (it is TcpBuilder) nextHeader = IpProtocol.TCP
            }
        }

    override var correctLengthAtBuild: Boolean = true

    override fun build(): IPv6Packet = IPv6Packet(this)
}
