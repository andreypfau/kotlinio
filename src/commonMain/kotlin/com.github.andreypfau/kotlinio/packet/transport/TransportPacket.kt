package com.github.andreypfau.kotlinio.packet.transport

import com.github.andreypfau.kotlinio.packet.Packet

interface TransportPacket : Packet {
    override val header: TransportHeader

    override fun builder(): TransportBuilder
}
