package com.github.andreypfau.network.packet.icmp

import com.github.andreypfau.network.packet.AbstractPacket
import com.github.andreypfau.network.utils.getUShort
import com.github.andreypfau.network.utils.toByteArray

abstract class IcmpIdentifiableHeader : AbstractPacket.AbstractHeader {
    val identifier: UShort
    val sequenceNumber: UShort

    constructor(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset) {
        require(length >= ICMP_IDENTIFIABLE_HEADER_SIZE)
        identifier = rawData.getUShort(IDENTIFIER_OFFSET + offset)
        sequenceNumber = rawData.getUShort(SEQUENCE_NUMBER_OFFSET + offset)
    }

    constructor(builder: IcmpIdentifiableBuilder) {
        identifier = builder.identifier
        sequenceNumber = builder.sequenceNumber
    }

    abstract val headerName: String

    override val rawFields: List<ByteArray>
        get() = buildList {
            add(identifier.toByteArray())
            add(sequenceNumber.toByteArray())
        }

    override val length: Int = ICMP_IDENTIFIABLE_HEADER_SIZE

    override fun buildString(): String = buildString {
        append("[")
            .append(headerName)
            .append(" (")
            .append(this@IcmpIdentifiableHeader.length)
            .append(" bytes)]")
            .appendLine()
        append("  Identifier: ").append(identifier).appendLine()
        append("  Sequence Number: ").append(sequenceNumber).appendLine()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        if (!super.equals(other)) return false

        other as IcmpIdentifiableHeader

        if (identifier != other.identifier) return false
        if (sequenceNumber != other.sequenceNumber) return false

        return true
    }

    override fun calcHashCode(): Int {
        var result = 17
        result = 31 * result + identifier.hashCode()
        result = 31 * result + sequenceNumber.hashCode()
        return result
    }

    companion object {
        private const val IDENTIFIER_OFFSET = 0
        private const val IDENTIFIER_SIZE = Short.SIZE_BYTES
        private const val SEQUENCE_NUMBER_OFFSET = IDENTIFIER_OFFSET + IDENTIFIER_SIZE
        private const val SEQUENCE_NUMBER_SIZE = Short.SIZE_BYTES
        const val ICMP_IDENTIFIABLE_HEADER_SIZE = SEQUENCE_NUMBER_OFFSET + SEQUENCE_NUMBER_SIZE
    }
}