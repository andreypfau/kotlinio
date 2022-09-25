package com.github.andreypfau.kotlinio.packet.ethernet.header

import com.github.andreypfau.kotlinio.address.MacAddress
import com.github.andreypfau.kotlinio.bits.setUShortAt
import com.github.andreypfau.kotlinio.packet.ethernet.EtherType

internal class FieldBackedEthernetHeader(
    override val dstAddress: MacAddress,
    override val srcAddress: MacAddress,
    override val type: EtherType
) : AbstractEthernetHeader() {
    override fun toByteArray(destination: ByteArray, offset: Int): ByteArray {
        dstAddress.toByteArray(destination, EthernetHeader.DST_ADDR_OFFSET + offset)
        srcAddress.toByteArray(destination, EthernetHeader.SRC_ADDR_OFFSET + offset)
        destination.setUShortAt(EthernetHeader.TYPE_OFFSET + offset, type.value)
        return destination
    }
}
