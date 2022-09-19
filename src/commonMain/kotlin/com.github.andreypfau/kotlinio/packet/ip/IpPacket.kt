package com.github.andreypfau.kotlinio.packet.ip

import com.github.andreypfau.kotlinio.address.InetAddress
import com.github.andreypfau.kotlinio.packet.Packet

interface IpPacket : Packet {

    override val header: IpHeader

    interface IpHeader : Packet.Header {
        val version: IpVersion
        val protocol: IpProtocol
        val srcAddress: InetAddress
        val dstAddress: InetAddress
    }
}
