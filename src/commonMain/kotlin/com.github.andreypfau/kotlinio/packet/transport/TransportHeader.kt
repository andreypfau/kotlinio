package com.github.andreypfau.kotlinio.packet.transport

import com.github.andreypfau.kotlinio.packet.Packet

interface TransportHeader : Packet.Header {
    val srcPort: UShort
    val dstPort: UShort
}
