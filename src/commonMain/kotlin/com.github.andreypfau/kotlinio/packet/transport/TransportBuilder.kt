package com.github.andreypfau.kotlinio.packet.transport

import com.github.andreypfau.kotlinio.address.InetAddress
import com.github.andreypfau.kotlinio.packet.Packet

interface TransportBuilder : Packet.Builder {
    var srcPort: UShort
    var dstPort: UShort
    var srcAddress: InetAddress?
    var dstAddress: InetAddress?
}
