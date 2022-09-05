package com.github.andreypfau.network.packet.ip

import com.github.andreypfau.network.address.InetAddress
import com.github.andreypfau.network.packet.Packet

interface IpPacket : Packet {

    override val header: IpHeader

    interface IpHeader : Packet.Header {
        val version: IpVersion
        val protocol: IpProtocol
        val srcAddress: InetAddress
        val dstAddress: InetAddress
    }
}