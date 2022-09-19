package com.github.andreypfau.kotlinio.packet.icmpv4.echo

import com.github.andreypfau.kotlinio.packet.icmp.IcmpIdentifiableHeader

class ICMPv4EchoReplyHeader : IcmpIdentifiableHeader {
    constructor(
        rawData: ByteArray,
        offset: Int = 0,
        length: Int = rawData.size - offset
    ) : super(rawData, offset, length)

    constructor(builder: ICMPv4EchoReplyBuilder) : super(builder)

    override val headerName: String = "ICMPv4 Echo Reply Header"
}
