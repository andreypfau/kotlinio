package com.github.andreypfau.kotlinio.packet.icmp

import com.github.andreypfau.kotlinio.packet.AbstractPacket

abstract class IcmpIdentifiablePacket : AbstractPacket() {
    abstract override val header: IcmpIdentifiableHeader

    abstract override fun builder(): IcmpIdentifiableBuilder
}
