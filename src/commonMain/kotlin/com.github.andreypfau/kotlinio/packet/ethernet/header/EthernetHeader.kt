package com.github.andreypfau.kotlinio.packet.ethernet.header

import com.github.andreypfau.kotlinio.address.MacAddress
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.ethernet.EtherType
import kotlin.jvm.JvmStatic

interface EthernetHeader : Packet.Header {
    val dstAddress: MacAddress
    val srcAddress: MacAddress
    val type: EtherType

    companion object {
        const val DST_ADDR_OFFSET = 0
        const val DST_ADDR_SIZE = MacAddress.SIZE_BYTES
        const val SRC_ADDR_OFFSET = DST_ADDR_OFFSET + DST_ADDR_SIZE
        const val SRC_ADDR_SIZE = MacAddress.SIZE_BYTES
        const val TYPE_OFFSET = SRC_ADDR_OFFSET + SRC_ADDR_SIZE
        const val TYPE_SIZE = Short.SIZE_BYTES
        const val ETHERNET_HEADER_SIZE = TYPE_OFFSET + TYPE_SIZE

        @JvmStatic
        fun newInstance(rawData: ByteArray): EthernetHeader = newInstance(rawData.copyOf(), 0)

        @JvmStatic
        fun newInstance(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset): EthernetHeader =
            ByteBackedEthernetHeader(rawData, offset, length)
    }
}
