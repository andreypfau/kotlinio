package com.github.andreypfau.kotlinio.packet.tcp

import com.github.andreypfau.kotlinio.address.InetAddress
import com.github.andreypfau.kotlinio.packet.AbstractPacket
import com.github.andreypfau.kotlinio.packet.ChecksumBuilder
import com.github.andreypfau.kotlinio.packet.LengthBuilder
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.transport.TransportBuilder

class TcpBuilder(
    override var srcPort: UShort = 0u,
    override var dstPort: UShort = 0u,
    var sequenceNumber: UInt = 0u,
    var acknowledgmentNumber: UInt = 0u,
    var dataOffset: UByte = 0u,
    var reserved: UByte = 0u,
    var urg: Boolean = false,
    var ack: Boolean = false,
    var psh: Boolean = false,
    var rst: Boolean = false,
    var syn: Boolean = false,
    var fin: Boolean = false,
    var window: UShort = 0u,
    var checksum: UShort = 0u,
    var urgentPointer: UShort = 0u,
    var options: MutableList<TcpOption> = ArrayList(),
    var padding: ByteArray? = null,
    override var payloadBuilder: Packet.Builder? = null,
    override var srcAddress: InetAddress? = null,
    override var dstAddress: InetAddress? = null
) : AbstractPacket.AbstractBuilder(), TransportBuilder, LengthBuilder<TcpPacket>, ChecksumBuilder<TcpPacket> {
    var paddingAtBuild: Boolean = true
    override var correctChecksumAtBuild: Boolean = true
    override var correctLengthAtBuild: Boolean = true

    override fun build(): TcpPacket = TcpPacket(this)
}
