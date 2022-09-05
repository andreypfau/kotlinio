package com.github.andreypfau.network.packet.transport

import com.github.andreypfau.network.packet.Packet

interface TransportHeader : Packet.Header {
    val srcPort: UShort
    val dstPort: UShort
}