package com.github.andreypfau.network.packet.icmpv4.echo

import com.github.andreypfau.network.packet.icmp.IcmpIdentifiableHeader

class ICMPv4EchoHeader : IcmpIdentifiableHeader {
    constructor(
        rawData: ByteArray,
        offset: Int = 0,
        length: Int = rawData.size
    ) : super(rawData, offset, length)

    constructor(builder: ICMPv4EchoBuilder) : super(builder)

    override val headerName: String = "ICMPv4 Echo Header"
}
