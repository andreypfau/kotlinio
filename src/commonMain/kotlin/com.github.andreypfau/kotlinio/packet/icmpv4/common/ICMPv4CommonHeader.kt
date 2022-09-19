package com.github.andreypfau.kotlinio.packet.icmpv4.common

import com.github.andreypfau.kotlinio.bits.getUShortAt
import com.github.andreypfau.kotlinio.packet.AbstractPacket
import com.github.andreypfau.kotlinio.packet.icmpv4.ICMPv4Code
import com.github.andreypfau.kotlinio.packet.icmpv4.ICMPv4Type
import com.github.andreypfau.kotlinio.utils.checksum
import com.github.andreypfau.kotlinio.utils.concatenate
import com.github.andreypfau.kotlinio.utils.hex
import com.github.andreypfau.kotlinio.utils.toByteArray

class ICMPv4CommonHeader : AbstractPacket.AbstractHeader {
    val type: ICMPv4Type
    val code: ICMPv4Code
    val checksum: UShort

    constructor(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset) {
        require(length >= ICMPV4_COMMON_HEADER_SIZE)
        type = ICMPv4Type[rawData[TYPE_OFFSET + offset]]
        code = type[rawData[CODE_OFFSET + offset]]
        checksum = rawData.getUShortAt(CHECKSUM_OFFSET + offset)
    }

    constructor(builder: ICMPv4CommonBuilder, payload: ByteArray) {
        type = builder.type
        code = builder.code
        checksum = if (builder.correctChecksumAtBuild) {
            calcChecksum(buildRawData(true), payload)
        } else {
            builder.checksum
        }
    }

    override val rawFields: List<ByteArray>
        get() = buildRawFields(false)

    private fun calcChecksum(header: ByteArray, payload: ByteArray): UShort {
        val packetLength = payload.size + length
        val data = if ((packetLength % 2) != 0) {
            ByteArray(packetLength + 1)
        } else {
            ByteArray(packetLength)
        }
        header.copyInto(data)
        payload.copyInto(data, header.size)
        return data.checksum().toUShort()
    }

    private fun buildRawData(zeroInsteadOfChecksum: Boolean) =
        buildRawFields(zeroInsteadOfChecksum).concatenate()

    private fun buildRawFields(zeroInsteadOfChecksum: Boolean) = buildList {
        add(type.value.toByteArray())
        add(code.value.toByteArray())
        add((if (zeroInsteadOfChecksum) 0u else checksum).toByteArray())
    }

    override fun buildString(): String = buildString {
        append("[ICMPv4 Common Header (")
            .append(this@ICMPv4CommonHeader.length)
            .append(" bytes)]")
            .appendLine()
        append("  Type: ").append(type).appendLine()
        append("  Code: ").append(code).appendLine()
        append("  Checksum: 0x").append(checksum.toByteArray().hex()).appendLine()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        if (!super.equals(other)) return false

        other as ICMPv4CommonHeader

        if (checksum != other.checksum) return false
        if (type != other.type) return false
        if (code != other.code) return false

        return true
    }

    override fun calcHashCode(): Int {
        var result = 17
        result = 31 * result + type.hashCode()
        result = 31 * result + code.hashCode()
        result = 31 * result + checksum.hashCode()
        return result
    }

    companion object {
        private const val TYPE_OFFSET = 0
        private const val TYPE_SIZE = Byte.SIZE_BYTES
        private const val CODE_OFFSET = TYPE_OFFSET + TYPE_SIZE
        private const val CODE_SIZE = Byte.SIZE_BYTES
        private const val CHECKSUM_OFFSET = CODE_OFFSET + CODE_SIZE
        private const val CHECKSUM_SIZE = Short.SIZE_BYTES
        private const val ICMPV4_COMMON_HEADER_SIZE = CHECKSUM_OFFSET + CHECKSUM_SIZE
    }
}
