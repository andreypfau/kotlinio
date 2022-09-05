package com.github.andreypfau.network.packet.icmpv4.echo

import com.github.andreypfau.network.packet.Packet
import com.github.andreypfau.network.packet.icmp.IcmpIdentifiableBuilder

class ICMPv4EchoBuilder(
    identifier: UShort,
    sequenceNumber: UShort,
    override var payloadBuilder: Packet.Builder? = null
) : IcmpIdentifiableBuilder(
    identifier, sequenceNumber
) {
    override fun build() = ICMPv4EchoPacket(this)
}