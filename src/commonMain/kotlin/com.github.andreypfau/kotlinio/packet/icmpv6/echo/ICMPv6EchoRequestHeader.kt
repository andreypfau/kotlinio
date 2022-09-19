package com.github.andreypfau.kotlinio.packet.icmpv6.echo

import com.github.andreypfau.kotlinio.packet.icmp.IcmpIdentifiableHeader

class ICMPv6EchoRequestHeader : IcmpIdentifiableHeader {
    constructor(
        rawData: ByteArray,
        offset: Int = 0,
        length: Int = rawData.size - offset
    ) : super(rawData, offset, length)

    constructor(builder: ICMPv6EchoRequestBuilder) : super(builder)

    override val headerName: String = "ICMPv6 Echo Request Header"
}
