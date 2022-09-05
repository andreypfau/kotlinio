package com.github.andreypfau.network.packet.transport

import com.github.andreypfau.network.packet.Packet

interface TransportPacket : Packet {
    override val header: TransportHeader
}