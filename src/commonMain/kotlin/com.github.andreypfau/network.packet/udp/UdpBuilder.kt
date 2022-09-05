package com.github.andreypfau.network.packet.udp

import com.github.andreypfau.network.address.InetAddress
import com.github.andreypfau.network.packet.AbstractPacket
import com.github.andreypfau.network.packet.ChecksumBuilder
import com.github.andreypfau.network.packet.LengthBuilder
import com.github.andreypfau.network.packet.Packet
import com.github.andreypfau.network.packet.transport.TransportBuilder

class UdpBuilder(
    override var srcPort: UShort = 0u,
    override var dstPort: UShort = 0u,
    var length: UShort = 0u,
    var checksum: UShort = 0u,
    override var payloadBuilder: Packet.Builder? = null,
    override var srcAddress: InetAddress? = null,
    override var dstAddress: InetAddress? = null
) : AbstractPacket.AbstractBuilder(), TransportBuilder, LengthBuilder<UdpPacket>, ChecksumBuilder<UdpPacket> {
    override var correctChecksumAtBuild: Boolean = true
    override var correctLengthAtBuild: Boolean = true

    override fun build(): UdpPacket = UdpPacket(this)
}