package com.github.andreypfau.network.packet.icmp

import com.github.andreypfau.network.packet.AbstractPacket

abstract class IcmpIdentifiablePacket : AbstractPacket() {
    abstract override val header: IcmpIdentifiableHeader

    abstract override fun builder(): IcmpIdentifiableBuilder
}