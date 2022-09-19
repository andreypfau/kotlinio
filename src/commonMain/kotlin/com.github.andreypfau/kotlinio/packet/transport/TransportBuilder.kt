package com.github.andreypfau.kotlinio.packet.transport

import com.github.andreypfau.kotlinio.address.InetAddress

interface TransportBuilder {
    var srcPort: UShort
    var dstPort: UShort
    var srcAddress: InetAddress?
    var dstAddress: InetAddress?
}
