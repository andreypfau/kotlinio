package com.github.andreypfau.kotlinio.packet.ipv4

import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.ipv4.header.IpV4Header

internal class FieldBackedIpV4Packet(
    override val header: IpV4Header,
    override val payload: Packet?
) : AbstractIpV4Packet() {
    override fun toByteArray(destination: ByteArray, offset: Int): ByteArray {
        header.toByteArray(destination, offset)
        payload?.toByteArray(destination, header.length + offset)
        return destination
    }
}
