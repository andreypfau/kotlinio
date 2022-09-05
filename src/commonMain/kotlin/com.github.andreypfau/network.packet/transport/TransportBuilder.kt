package com.github.andreypfau.network.packet.transport

import com.github.andreypfau.network.address.InetAddress

interface TransportBuilder {
    var srcPort: UShort
    var dstPort: UShort
    var srcAddress: InetAddress?
    var dstAddress: InetAddress?
}