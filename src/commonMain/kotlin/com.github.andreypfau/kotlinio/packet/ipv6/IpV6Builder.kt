package com.github.andreypfau.kotlinio.packet.ipv6

import com.github.andreypfau.kotlinio.address.Inet6Address
import com.github.andreypfau.kotlinio.packet.AbstractPacket
import com.github.andreypfau.kotlinio.packet.LengthBuilder
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.ip.IpPacket
import com.github.andreypfau.kotlinio.packet.ip.IpProtocol
import com.github.andreypfau.kotlinio.packet.ip.IpVersion
import com.github.andreypfau.kotlinio.packet.tcp.TcpBuilder
import com.github.andreypfau.kotlinio.packet.transport.TransportBuilder
import com.github.andreypfau.kotlinio.packet.udp.UdpBuilder

class IpV6Builder(
    override var version: IpVersion = IpVersion.IPv6,
    var trafficClass: UByte = 0u,
    var flowLabel: UInt = 0u,
    var payloadLength: UShort = 0u,
    var nextHeader: IpProtocol? = null,
    var hopLimit: UByte = 100u,
    override var srcAddress: Inet6Address = Inet6Address(),
    override var dstAddress: Inet6Address = Inet6Address(),
    payloadBuilder: Packet.Builder? = null
) : AbstractPacket.AbstractBuilder(), LengthBuilder<IpV6Packet>, IpPacket.IpBuilder<Inet6Address> {
    override var protocol: IpProtocol?
        get() = nextHeader
        set(value) {
            nextHeader = value
        }
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

    override fun build(): IpV6Packet = IpV6Packet(this)
}
