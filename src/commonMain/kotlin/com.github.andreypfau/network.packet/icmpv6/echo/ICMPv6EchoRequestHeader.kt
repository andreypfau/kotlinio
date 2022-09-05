package com.github.andreypfau.network.packet.icmpv6.echo

import com.github.andreypfau.network.packet.icmp.IcmpIdentifiableHeader

class ICMPv6EchoRequestHeader : IcmpIdentifiableHeader {
    constructor(
        rawData: ByteArray,
        offset: Int = 0,
        length: Int = rawData.size
    ) : super(rawData, offset, length)

    constructor(builder: ICMPv6EchoRequestBuilder) : super(builder)

    override val headerName: String = "ICMPv6 Echo Request Header"
}
