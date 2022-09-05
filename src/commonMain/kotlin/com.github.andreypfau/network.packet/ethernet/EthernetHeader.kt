package com.github.andreypfau.network.packet.ethernet

import com.github.andreypfau.network.address.MacAddress
import com.github.andreypfau.network.packet.AbstractPacket
import com.github.andreypfau.network.utils.getShort
import com.github.andreypfau.network.utils.toByteArray

class EthernetHeader : AbstractPacket.AbstractHeader {
    val dstAddress: MacAddress
    val srcAddress: MacAddress
    val type: EtherType

    constructor(rawData: ByteArray, offset: Int, length: Int) {
        require(length >= ETHERNET_HEADER_SIZE)
        dstAddress = MacAddress(rawData, DST_ADDR_OFFSET + offset)
        srcAddress = MacAddress(rawData, SRC_ADDR_OFFSET + offset)
        type = EtherType[rawData.getShort(TYPE_OFFSET + offset).toUShort()]
    }

    constructor(builder: EthernetPacket.EthernetBuilder) {
        dstAddress = requireNotNull(builder.dstAddress) { "dstAddress == null" }
        srcAddress = requireNotNull(builder.srcAddress) { "srcAddress == null" }
        type = requireNotNull(builder.type) { "type == null" }
    }

    override val rawFields: List<ByteArray>
        get() = buildList {
            add(dstAddress.toByteArray())
            add(srcAddress.toByteArray())
            add(type.value.toByteArray())
        }

    override fun buildString(): String = buildString {
        append("[Ethernet Header (").append(this@EthernetHeader.length).append(" bytes)]").appendLine()
        append("  Destination address: ").append(dstAddress).appendLine()
        append("  Source address: ").append(srcAddress).appendLine()
        append("  Type: ").append(type).appendLine()
    }

    override fun calcHashCode(): Int {
        var result = 17
        result = 31 * result + dstAddress.hashCode()
        result = 31 * result + srcAddress.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        if (!super.equals(other)) return false

        other as EthernetHeader

        if (dstAddress != other.dstAddress) return false
        if (srcAddress != other.srcAddress) return false
        if (type != other.type) return false

        return true
    }

    companion object {
        const val DST_ADDR_OFFSET = 0
        const val DST_ADDR_SIZE = MacAddress.SIZE_BYTES
        const val SRC_ADDR_OFFSET = DST_ADDR_OFFSET + DST_ADDR_SIZE
        const val SRC_ADDR_SIZE = MacAddress.SIZE_BYTES
        const val TYPE_OFFSET = SRC_ADDR_OFFSET + SRC_ADDR_SIZE
        const val TYPE_SIZE = MacAddress.SIZE_BYTES
        const val ETHERNET_HEADER_SIZE = TYPE_OFFSET + TYPE_SIZE
    }
}