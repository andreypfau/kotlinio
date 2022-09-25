package com.github.andreypfau.kotlinio.packet.ethernet.header

import com.github.andreypfau.kotlinio.address.MacAddress
import com.github.andreypfau.kotlinio.packet.ethernet.EtherType

internal abstract class AbstractEthernetHeader : EthernetHeader {
    abstract override val dstAddress: MacAddress
    abstract override val srcAddress: MacAddress
    abstract override val type: EtherType
    override val length: Int get() = EthernetHeader.ETHERNET_HEADER_SIZE
    override val rawData: ByteArray
        get() = toByteArray()
    private val _string: String by lazy {
        buildString {
            append("Ethernet ").append(type)
                .append(", Src: ").append(srcAddress)
                .append(", Dst: ").append(dstAddress)
                .appendLine()
        }
    }
    private val _hashCode: Int by lazy {
        var result = dstAddress.hashCode()
        result = 31 * result + srcAddress.hashCode()
        result = 31 * result + type.hashCode()
        result
    }

    abstract override fun toByteArray(destination: ByteArray, offset: Int): ByteArray

    override fun toString(): String = _string
    override fun hashCode(): Int = _hashCode

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EthernetHeader) return false
        if (dstAddress != other.dstAddress) return false
        if (srcAddress != other.srcAddress) return false
        if (type != other.type) return false
        return true
    }
}
