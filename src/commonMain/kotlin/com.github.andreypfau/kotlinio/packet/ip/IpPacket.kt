package com.github.andreypfau.kotlinio.packet.ip

import com.github.andreypfau.kotlinio.address.InetAddress
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.ipv4.IPv4Packet
import com.github.andreypfau.kotlinio.packet.ipv6.IPv6Packet

interface IpPacket : Packet {

    override val header: IpHeader

    override fun builder(): IpBuilder<out InetAddress>

    interface IpHeader : Packet.Header {
        val version: IpVersion
        val protocol: IpProtocol
        val srcAddress: InetAddress
        val dstAddress: InetAddress
    }

    interface IpBuilder<T : InetAddress> : Packet.Builder {
        var version: IpVersion?
        var protocol: IpProtocol?
        var srcAddress: T?
        var dstAddress: T?

        override fun build(): IpPacket
    }

    companion object {
        fun newInstance(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset): IpPacket {
            return when (IpVersion[((rawData[offset].toInt() and 0xF0) shr 4).toByte()]) {
                IpVersion.IPv4 -> IPv4Packet(rawData, offset, length)
                IpVersion.IPv6 -> IPv6Packet(rawData, offset, length)
            }
        }
    }
}
