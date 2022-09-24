package com.github.andreypfau.kotlinio.packet.ipv4

import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.ipv4.header.IpV4Header

internal class ByteBackedIpV4Packet(
    private val _rawData: ByteArray,
    private val _offset: Int,
    private val _length: Int
) : AbstractIpV4Packet(), IpV4Packet {
    override val header: IpV4Header = IpV4Header(_rawData, _offset, _length)
    override val payload: Packet? by lazy {
        val payloadOffset = header.length + _offset
        if (payloadOffset < _length) {
            header.protocol.packet(_rawData, payloadOffset, _length)
        } else null
    }

    override fun toByteArray(destination: ByteArray, offset: Int): ByteArray {
        return _rawData.copyInto(destination, offset, _offset, _offset + length)
    }
}
