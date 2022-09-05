package com.github.andreypfau.network.packet.icmp

import com.github.andreypfau.network.packet.AbstractPacket

abstract class IcmpIdentifiableBuilder(
    var identifier: UShort = 0u,
    var sequenceNumber: UShort = 0u
) : AbstractPacket.AbstractBuilder()