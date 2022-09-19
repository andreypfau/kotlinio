package com.github.andreypfau.kotlinio.packet.icmp

import com.github.andreypfau.kotlinio.packet.AbstractPacket

abstract class IcmpIdentifiableBuilder(
    var identifier: UShort = 0u,
    var sequenceNumber: UShort = 0u
) : AbstractPacket.AbstractBuilder()
