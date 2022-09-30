package com.github.andreypfau.kotlinio.packet.ethernet

import com.github.andreypfau.kotlinio.address.MacAddress
import com.github.andreypfau.kotlinio.packet.AbstractPacket
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.ipv4.IpV4Builder
import com.github.andreypfau.kotlinio.packet.ipv6.IpV6Builder

class EthernetBuilder(
    var dstAddress: MacAddress? = null,
    var srcAddress: MacAddress? = null,
    var type: EtherType? = null,
    var padding: ByteArray? = null,
    payloadBuilder: Packet.Builder? = null
) : AbstractPacket.AbstractBuilder() {
    override var payloadBuilder: Packet.Builder? = payloadBuilder
        set(value) {
            field = value.also {
                if (it is IpV4Builder) type = EtherType.IPv4
                if (it is IpV6Builder) type = EtherType.IPV6
            }
        }
    var paddingAtBuild: Boolean = true

    override fun build(): EthernetPacket = EthernetPacket(this)
}
