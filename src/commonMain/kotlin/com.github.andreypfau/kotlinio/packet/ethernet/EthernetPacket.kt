package com.github.andreypfau.kotlinio.packet.ethernet

import com.github.andreypfau.kotlinio.packet.AbstractPacket
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.ethernet.header.ByteBackedEthernetHeader
import com.github.andreypfau.kotlinio.packet.ethernet.header.EthernetHeader
import com.github.andreypfau.kotlinio.packet.ethernet.header.FieldBackedEthernetHeader
import com.github.andreypfau.kotlinio.utils.hex

@Suppress("EqualsOrHashCode") // hashCode already has in AbstractPacket. IntelliJ bug?
class EthernetPacket : AbstractPacket {
    private val _padding: ByteArray

    override val header: EthernetHeader
    override val payload: Packet?
    val padding get() = _padding.copyOf()

    constructor(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset) {
        header = ByteBackedEthernetHeader(rawData, offset, length)
        if ((header.type.value and 0xFFFFu).toInt() <= EtherType.IEEE802_3_MAX_LENGTH) {
            val payloadLength = header.type.value.toInt()
            val payloadOffset = offset + header.length
            val paddingLength = length - header.length - payloadLength
            val paddingOffset = payloadOffset + payloadLength
            require(paddingLength >= 0)
            payload = if (payloadLength > 0) {
                header.type.payload(rawData, payloadOffset, payloadLength)
            } else {
                null
            }
            _padding = if (paddingLength > 0) {
                rawData.copyOfRange(paddingOffset, paddingOffset + paddingLength)
            } else {
                byteArrayOf()
            }
        } else {
            val payloadAndPaddingLength = length - header.length
            if (payloadAndPaddingLength > 0) {
                val payloadOffset = offset + header.length
                payload = header.type.payload(rawData, payloadOffset, payloadAndPaddingLength)
                val paddingLength = payloadAndPaddingLength - payload.length
                _padding = if (paddingLength > 0) {
                    val paddingOffset = payloadOffset + payload.length
                    rawData.copyOfRange(paddingOffset, paddingOffset + paddingLength)
                } else {
                    byteArrayOf()
                }
            } else {
                payload = null
                _padding = byteArrayOf()
            }
        }
    }

    constructor(builder: EthernetBuilder) {
        payload = builder.payloadBuilder?.build()
        header = FieldBackedEthernetHeader(
            requireNotNull(builder.dstAddress) { "dstAddress" },
            requireNotNull(builder.srcAddress) { "srcAddress" },
            requireNotNull(builder.type) { "type" }
        )
        val payloadLength = payload?.length ?: 0
        _padding = if (builder.paddingAtBuild) {
            if (payloadLength < MIN_ETHERNET_PAYLOAD_LENGTH) {
                ByteArray(MIN_ETHERNET_PAYLOAD_LENGTH - payloadLength)
            } else {
                byteArrayOf()
            }
        } else {
            requireNotNull(builder.padding) { "padding == null" }.copyOf()
        }
    }

    override fun calcSize(): Int {
        return super.calcSize() + _padding.size
    }

    override fun buildRawData(): ByteArray {
        val rawData = super.buildRawData()
        _padding.copyInto(rawData, rawData.size - _padding.size)
        return rawData
    }

    override fun buildString(): String = buildString {
        append(header)
        payload?.let {
            append(it)
        }
        if (_padding.isNotEmpty()) {
            append("[Ethernet Pad (")
                .append(_padding.size)
                .append(" bytes)]")
                .appendLine()
                .append("  Hex stream: ")
                .append(_padding.hex(" "))
                .appendLine()
        }
    }

    override fun calcHashCode(): Int =
        31 * super.calcHashCode() + _padding.contentHashCode()

    override fun equals(other: Any?): Boolean =
        if (!super.equals(other)) false
        else _padding.contentEquals((other as EthernetPacket)._padding)

    override fun builder() = EthernetBuilder(
        header.dstAddress,
        header.srcAddress,
        header.type,
        _padding,
        payload?.builder()
    )

    companion object {
        const val MIN_ETHERNET_PAYLOAD_LENGTH = 46
    }
}
