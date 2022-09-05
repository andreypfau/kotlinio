package com.github.andreypfau.network.packet.icmpv6.echo

import com.github.andreypfau.network.packet.Packet
import com.github.andreypfau.network.packet.icmp.IcmpIdentifiableBuilder

class ICMPv6EchoRequestBuilder(
    identifier: UShort,
    sequenceNumber: UShort,
    override var payloadBuilder: Packet.Builder? = null
) : IcmpIdentifiableBuilder(
    identifier, sequenceNumber
) {
    override fun build() = ICMPv6EchoRequestPacket(this)
}