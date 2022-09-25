package com.github.andreypfau.kotlinio.packet.ethernet.header

import com.github.andreypfau.kotlinio.address.MacAddress
import com.github.andreypfau.kotlinio.bits.getUShortAt
import com.github.andreypfau.kotlinio.packet.ethernet.EtherType

internal class ByteBackedEthernetHeader(
    private val _rawData: ByteArray,
    private val _offset: Int,
    _length: Int
) : AbstractEthernetHeader() {
    init {
        require(_length - _offset >= EthernetHeader.ETHERNET_HEADER_SIZE) { "rawData too small" }
    }

    override val dstAddress: MacAddress = MacAddress(_rawData, EthernetHeader.DST_ADDR_OFFSET)
    override val srcAddress: MacAddress = MacAddress(_rawData, EthernetHeader.SRC_ADDR_OFFSET)
    override val type: EtherType
        get() =
            EtherType[_rawData.getUShortAt(EthernetHeader.TYPE_OFFSET)]

    override fun toByteArray(destination: ByteArray, offset: Int): ByteArray =
        _rawData.copyInto(destination, offset, _offset, _offset + length)
}
