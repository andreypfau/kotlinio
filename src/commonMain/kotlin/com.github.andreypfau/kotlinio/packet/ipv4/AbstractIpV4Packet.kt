package com.github.andreypfau.kotlinio.packet.ipv4

import com.github.andreypfau.kotlinio.packet.AbstractPacket
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.ipv4.header.IpV4Header
import com.github.andreypfau.kotlinio.packet.transport.TransportBuilder

internal abstract class AbstractIpV4Packet : AbstractPacket(), IpV4Packet {
    abstract override val header: IpV4Header
    abstract override val payload: Packet?
    override val length: Int by lazy {
        header.length + (payload?.length ?: 0)
    }

    override val rawData: ByteArray
        get() = toByteArray()

    abstract override fun toByteArray(destination: ByteArray, offset: Int): ByteArray

    override fun builder(): IpV4Builder = IpV4Builder(
        header.versionAndIhl,
        header.tos,
        header.totalLength,
        header.identification,
        header.flags,
        header.ttl,
        header.protocol,
        header.headerChecksum,
        header.srcAddress,
        header.dstAddress,
        header.options.toMutableList(),
        header.padding,
        payload?.builder()?.also {
            if (it is TransportBuilder) {
                it.srcAddress = header.srcAddress
                it.dstAddress = header.dstAddress
            }
        }
    )
}
