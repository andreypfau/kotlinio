package com.github.andreypfau.kotlinio.packet.icmpv4.echo

import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.icmp.IcmpIdentifiableBuilder

class ICMPv4EchoReplyBuilder(
    identifier: UShort = 0u,
    sequenceNumber: UShort = 0u,
    override var payloadBuilder: Packet.Builder? = null
) : IcmpIdentifiableBuilder(
    identifier, sequenceNumber
) {
    override fun build() = ICMPv4EchoReplyPacket(this)
}
