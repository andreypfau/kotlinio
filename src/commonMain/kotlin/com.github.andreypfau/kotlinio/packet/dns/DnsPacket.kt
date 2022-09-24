package com.github.andreypfau.kotlinio.packet.dns

import com.github.andreypfau.kotlinio.packet.AbstractPacket
import com.github.andreypfau.kotlinio.packet.Packet
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic

@JvmSynthetic
inline fun DnsPacket(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset): DnsPacket =
    DnsPacket.newInstance(rawData, offset, length)

@JvmSynthetic
inline fun DnsPacket(builder: DnsPacket.Builder.() -> Unit): DnsPacket =
    DnsPacket.Builder().apply(builder).build()

interface DnsPacket : Packet {
    override val header: DnsHeader
    override val payload: Packet?

    override fun builder(): Builder

    companion object {
        @JvmStatic
        fun newInstance(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset): DnsPacket =
            ByteBackedDnsPacket(rawData, offset, length)

        @JvmStatic
        fun build(builder: Builder.() -> Unit): DnsPacket = Builder().apply(builder).build()
    }

    data class Builder(
        var id: UShort = 0u,
        var isResponse: Boolean = false,
        var opCode: DnsOpCode? = null,
        var isAuthoritativeAnswer: Boolean = false,
        var isTruncated: Boolean = false,
        var isRecursionDesired: Boolean = false,
        var isRecursionAvailable: Boolean = false,
        var reservedBit: Boolean = false,
        var isAuthenticData: Boolean = false,
        var isCheckingDisabled: Boolean = true,
        var rCode: DnsRCode? = null,
        var qdCount: UShort? = null,
        var anCount: UShort? = null,
        var nsCount: UShort? = null,
        var arCount: UShort? = null,
        var questions: MutableList<DnsQuestion> = ArrayList(),
        var answers: MutableList<DnsResourceRecord> = ArrayList(),
        var authorities: MutableList<DnsResourceRecord> = ArrayList(),
        var additionalInfo: MutableList<DnsResourceRecord> = ArrayList(),
    ) : AbstractPacket.AbstractBuilder() {
        override var payloadBuilder: Packet.Builder? = null

        override fun build(): DnsPacket {
            val header = FieldBackedDnsHeader(
                id,
                isResponse,
                requireNotNull(opCode),
                isAuthoritativeAnswer,
                isTruncated,
                isRecursionDesired,
                isRecursionAvailable,
                reservedBit,
                isAuthenticData,
                isCheckingDisabled,
                requireNotNull(rCode),
                qdCount ?: questions.size.toUShort(),
                anCount ?: answers.size.toUShort(),
                nsCount ?: authorities.size.toUShort(),
                arCount ?: additionalInfo.size.toUShort(),
                questions, answers, authorities, additionalInfo
            )

            return FieldBackedDnsPacket(header)
        }
    }
}

internal abstract class AbstractDnsPacket : AbstractPacket(), DnsPacket {
    abstract override val header: DnsHeader
    override val payload: Packet? = null
    override val length: Int get() = header.length

    override val rawData: ByteArray get() = toByteArray()

    override fun builder(): DnsPacket.Builder = DnsPacket.Builder(
        header.id,
        header.isResponse,
        header.opCode,
        header.isAuthoritativeAnswer,
        header.isTruncated,
        header.isRecursionDesired,
        header.isRecursionAvailable,
        header.reservedBit,
        header.isAuthenticData,
        header.isCheckingDisabled,
        header.rCode,
        header.qdCount,
        header.anCount,
        header.nsCount,
        header.arCount,
        header.questions.toMutableList(),
        header.answers.toMutableList(),
        header.authorities.toMutableList(),
        header.additionalInfo.toMutableList(),
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DnsPacket) return false
        if (header != other.header) return false
        return true
    }

    override fun hashCode(): Int = header.hashCode()
}

internal class ByteBackedDnsPacket(
    private val _rawData: ByteArray,
    private val _offset: Int,
    private val _length: Int
) : AbstractDnsPacket() {
    override val header: DnsHeader by lazy {
        DnsHeader.newInstance(_rawData, _offset, _length)
    }

    override fun toByteArray(destination: ByteArray, offset: Int): ByteArray {
        return _rawData.copyInto(destination, offset, _offset, _offset + _length)
    }
}

internal class FieldBackedDnsPacket(
    override val header: DnsHeader
) : AbstractDnsPacket() {
    override fun toByteArray(destination: ByteArray, offset: Int): ByteArray {
        return header.toByteArray(destination, offset)
    }
}
