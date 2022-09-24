package com.github.andreypfau.kotlinio.packet.ethernet

import com.github.andreypfau.kotlinio.address.MacAddress
import com.github.andreypfau.kotlinio.bits.getUShortAt
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.utils.toByteArray
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

internal abstract class AbstractEthernetHeader : EthernetHeader {
    abstract override val dstAddress: MacAddress
    abstract override val srcAddress: MacAddress
    abstract override val type: EtherType
    override val length: Int get() = EthernetHeader.ETHERNET_HEADER_SIZE
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

internal class ByteBackedEthernetHeader(
    private val _rawData: ByteArray,
    private val _offset: Int,
    _length: Int
) : AbstractEthernetHeader() {
    init {
        require(_length - _offset >= EthernetHeader.ETHERNET_HEADER_SIZE) { "rawData too small" }
    }

    override val dstAddress: MacAddress by lazy {
        MacAddress(_rawData, EthernetHeader.DST_ADDR_OFFSET)
    }
    override val srcAddress: MacAddress by lazy {
        MacAddress(_rawData, EthernetHeader.SRC_ADDR_OFFSET)
    }
    override val type: EtherType by lazy {
        EtherType[_rawData.getUShortAt(EthernetHeader.TYPE_OFFSET)]
    }
    override val rawData: ByteArray
        get() = _rawData.copyOfRange(_offset, _offset + EthernetHeader.ETHERNET_HEADER_SIZE)
}

internal class FieldBackedEthernetHeader(
    override val dstAddress: MacAddress,
    override val srcAddress: MacAddress,
    override val type: EtherType
) : AbstractEthernetHeader() {
    override val rawData: ByteArray
        get() = ByteArray(EthernetHeader.ETHERNET_HEADER_SIZE).apply {
            dstAddress.toByteArray(this, EthernetHeader.DST_ADDR_OFFSET)
            srcAddress.toByteArray(this, EthernetHeader.SRC_ADDR_OFFSET)
            type.value.toByteArray(this, EthernetHeader.TYPE_OFFSET)
        }
}
