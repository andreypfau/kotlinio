package com.github.andreypfau.kotlinio.packet.dns

import com.github.andreypfau.kotlinio.bits.getUShortAt
import com.github.andreypfau.kotlinio.packet.AbstractPacket
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.utils.hex
import com.github.andreypfau.kotlinio.utils.shl
import com.github.andreypfau.kotlinio.utils.toByteArray
import kotlin.jvm.JvmStatic

interface DnsHeader : Packet.Header {
    val id: UShort
    val isResponse: Boolean
    val opCode: DnsOpCode
    val isAuthoritativeAnswer: Boolean
    val isTruncated: Boolean
    val isRecursionDesired: Boolean
    val isRecursionAvailable: Boolean
    val reservedBit: Boolean
    val isAuthenticData: Boolean
    val isCheckingDisabled: Boolean
    val rCode: DnsRCode
    val qdCount: UShort
    val anCount: UShort
    val nsCount: UShort
    val arCount: UShort
    val questions: List<DnsQuestion>
    val answers: List<DnsResourceRecord>
    val authorities: List<DnsResourceRecord>
    val additionalInfo: List<DnsResourceRecord>

    companion object {
        private const val ID_OFFSET = 0
        private const val ID_SIZE = Short.SIZE_BYTES
        private const val FLAGS_OFFSET = ID_OFFSET + ID_SIZE
        private const val FLAGS_SIZE = Short.SIZE_BYTES
        private const val QDCOUNT_OFFSET = FLAGS_OFFSET + FLAGS_SIZE
        private const val QDCOUNT_SIZE = Short.SIZE_BYTES
        private const val ANCOUNT_OFFSET = QDCOUNT_OFFSET + QDCOUNT_SIZE
        private const val ANCOUNT_SIZE = Short.SIZE_BYTES
        private const val NSCOUNT_OFFSET = ANCOUNT_OFFSET + ANCOUNT_SIZE
        private const val NSCOUNT_SIZE = Short.SIZE_BYTES
        private const val ARCOUNT_OFFSET = NSCOUNT_OFFSET + NSCOUNT_SIZE
        private const val ARCOUNT_SIZE = Short.SIZE_BYTES
        private const val DNS_MIN_HEADER_SIZE = ARCOUNT_OFFSET + ARCOUNT_SIZE

        @JvmStatic
        fun newInstance(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset): DnsHeader =
            ByteBacked(rawData, offset, length)
    }

    private class ByteBacked(
        private val _rawData: ByteArray,
        private val _offset: Int,
        private val _length: Int
    ) : AbstractDnsHeader() {
        override val id: UShort = run  {
            _rawData.getUShortAt(ID_OFFSET + _offset)
        }
        private val flags: Int = run  {
            _rawData.getUShortAt(FLAGS_OFFSET + _offset).toInt()
        }
        override val isResponse: Boolean = run  {
            flags and 0x8000 != 0
        }
        override val opCode: DnsOpCode = run  {
            DnsOpCode[((flags shr 11) and 0x0F).toByte()]
        }
        override val isAuthoritativeAnswer: Boolean = run  {
            flags and 0x0400 != 0
        }
        override val isTruncated: Boolean = run  {
            flags and 0x0200 != 0
        }
        override val isRecursionDesired: Boolean = run  {
            flags and 0x0100 != 0
        }
        override val isRecursionAvailable: Boolean = run  {
            flags and 0x0080 != 0
        }
        override val reservedBit: Boolean = run  {
            flags and 0x0040 != 0
        }
        override val isAuthenticData: Boolean = run  {
            flags and 0x0020 != 0
        }
        override val isCheckingDisabled: Boolean = run  {
            flags and 0x0010 != 0
        }
        override val rCode: DnsRCode = run  {
            DnsRCode[(flags and 0x0F).toByte()]
        }
        override val qdCount: UShort = run  {
            _rawData.getUShortAt(QDCOUNT_OFFSET + _offset)
        }
        override val anCount: UShort = run  {
            _rawData.getUShortAt(ANCOUNT_OFFSET + _offset)
        }
        override val nsCount: UShort = run  {
            _rawData.getUShortAt(NSCOUNT_OFFSET + _offset)
        }
        override val arCount: UShort = run  {
            _rawData.getUShortAt(ARCOUNT_OFFSET + _offset)
        }
        override val questions: List<DnsQuestion> = run  {
            var cursor = _offset + DNS_MIN_HEADER_SIZE
            val remainingLen = _length - cursor
            List(qdCount.toInt()) {
                val question = DnsQuestion.newInstance(_rawData, cursor, remainingLen)
                cursor += question.length
                question
            }
        }
        private val _questionsLength = run  {
            questions.sumOf { it.length }
        }
        override val answers: List<DnsResourceRecord> = run  {
            var cursor = _offset + DNS_MIN_HEADER_SIZE + _questionsLength
            val remainingLen = _length - cursor

            List(anCount.toInt()) {


                val answer = DnsResourceRecord.newInstance(_rawData, cursor, remainingLen)


                cursor += answer.length
                answer
            }
        }
        private val _answersLength = run  {
            answers.sumOf { it.length }
        }
        override val authorities: List<DnsResourceRecord> = run  {
            var cursor = _offset + DNS_MIN_HEADER_SIZE + _questionsLength + _answersLength
            val remainingLen = _length - cursor
            List(nsCount.toInt()) {
                val authority = DnsResourceRecord.newInstance(_rawData, cursor, remainingLen)
                cursor += authority.length
                authority
            }
        }
        private val _authoritiesLength = run  {
            authorities.sumOf { it.length }
        }
        override val additionalInfo: List<DnsResourceRecord> = run  {
            var cursor = _offset + DNS_MIN_HEADER_SIZE + _questionsLength + _answersLength + _authoritiesLength
            val remainingLen = _length - cursor
            List(arCount.toInt()) {
                val authority = DnsResourceRecord.newInstance(_rawData, cursor, remainingLen)
                cursor += authority.length
                authority
            }
        }
        private val _infoLength = run  {
            additionalInfo.sumOf { it.length }
        }
        override val length: Int = run {
            DNS_MIN_HEADER_SIZE + _questionsLength + _answersLength + _authoritiesLength + _infoLength
        }

        override fun toByteArray(destination: ByteArray, offset: Int): ByteArray {
            return _rawData.copyInto(destination, offset, _offset, _offset + length)
        }
    }
}

internal abstract class AbstractDnsHeader : DnsHeader, AbstractPacket.AbstractHeader() {
    abstract override val id: UShort
    abstract override val isResponse: Boolean
    abstract override val opCode: DnsOpCode
    abstract override val isAuthoritativeAnswer: Boolean
    abstract override val isTruncated: Boolean
    abstract override val isRecursionDesired: Boolean
    abstract override val isRecursionAvailable: Boolean
    abstract override val reservedBit: Boolean
    abstract override val isAuthenticData: Boolean
    abstract override val isCheckingDisabled: Boolean
    abstract override val rCode: DnsRCode
    abstract override val qdCount: UShort
    abstract override val anCount: UShort
    abstract override val nsCount: UShort
    abstract override val arCount: UShort
    abstract override val questions: List<DnsQuestion>
    abstract override val answers: List<DnsResourceRecord>
    abstract override val authorities: List<DnsResourceRecord>
    abstract override val additionalInfo: List<DnsResourceRecord>

    private val _hashCode by lazy {
        var result = super.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + isResponse.hashCode()
        result = 31 * result + opCode.hashCode()
        result = 31 * result + isAuthoritativeAnswer.hashCode()
        result = 31 * result + isTruncated.hashCode()
        result = 31 * result + isRecursionDesired.hashCode()
        result = 31 * result + isRecursionAvailable.hashCode()
        result = 31 * result + reservedBit.hashCode()
        result = 31 * result + isAuthenticData.hashCode()
        result = 31 * result + isCheckingDisabled.hashCode()
        result = 31 * result + rCode.hashCode()
        result = 31 * result + qdCount.hashCode()
        result = 31 * result + anCount.hashCode()
        result = 31 * result + nsCount.hashCode()
        result = 31 * result + arCount.hashCode()
        result = 31 * result + questions.hashCode()
        result = 31 * result + answers.hashCode()
        result = 31 * result + authorities.hashCode()
        result = 31 * result + additionalInfo.hashCode()
        result
    }
    private val _string by lazy {
        buildString {
            append("[DNS Header (").append(length).append(" bytes)]").appendLine()
            append("  ID: ").append("0x").append(id.toByteArray().hex()).appendLine()
            append("  QR: ").append(if (isResponse) "response" else "query").appendLine()
            append("  OPCODE: ").append(opCode).appendLine()
            append("  Authoritative Answer: ").append(isAuthoritativeAnswer).appendLine()
            append("  Truncated: ").append(isTruncated).appendLine()
            append("  Recursion Desired: ").append(isRecursionDesired).appendLine()
            append("  Recursion Available: ").append(isRecursionAvailable).appendLine()
            append("  Reserved Bit: ").append(if (reservedBit) 1 else 0).appendLine()
            append("  Authentic Data: ").append(isAuthenticData).appendLine()
            append("  RCODE: ").append(rCode).appendLine()
            append("  QDCOUNT: ").append(qdCount).appendLine()
            append("  ANCOUNT: ").append(anCount).appendLine()
            append("  NSCOUNT: ").append(nsCount).appendLine()
            append("  ARCOUNT: ").append(arCount).appendLine()
            questions.forEach {
                append("  Question:").appendLine()
                append("    ").append(it)
            }
            answers.forEach {
                append("  Answer:").appendLine()
                append("    ").append(it)
            }
            authorities.forEach {
                append("  Authority:").appendLine()
                append("    ").append(it)
            }
            additionalInfo.forEach {
                append("  Additional:").appendLine()
                append("    ").append(it)
            }
        }
    }

    override val rawFields: List<ByteArray>
        get() = buildList {
            add(id.toByteArray())
            val flags = ByteArray(2)
            flags[0] = opCode.value shl 3
            if (isResponse) {
                flags[0] = (flags[0].toInt() or 0x80).toByte()
            }
            if (isAuthoritativeAnswer) {
                flags[0] = (flags[0].toInt() or 0x04).toByte()
            }
            if (isTruncated) {
                flags[0] = (flags[0].toInt() or 0x02).toByte()
            }
            if (isRecursionDesired) {
                flags[0] = (flags[0].toInt() or 0x01).toByte()
            }
            flags[1] = rCode.value
            if (isRecursionAvailable) {
                flags[1] = (flags[1].toInt() or 0x80).toByte()
            }
            if (reservedBit) {
                flags[1] = (flags[1].toInt() or 0x40).toByte()
            }
            if (isAuthenticData) {
                flags[1] = (flags[1].toInt() or 0x20).toByte()
            }
            if (isCheckingDisabled) {
                flags[1] = (flags[1].toInt() or 0x10).toByte()
            }
            add(flags)
            add(qdCount.toByteArray())
            add(anCount.toByteArray())
            add(nsCount.toByteArray())
            add(arCount.toByteArray())
            questions.forEach {
                add(it.toByteArray())
            }
            answers.forEach {
                add(it.toByteArray())
            }
            authorities.forEach {
                add(it.toByteArray())
            }
            additionalInfo.forEach {
                add(it.toByteArray())
            }
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DnsHeader) return false

        if (id != other.id) return false
        if (isResponse != other.isResponse) return false
        if (opCode != other.opCode) return false
        if (isAuthoritativeAnswer != other.isAuthoritativeAnswer) return false
        if (isTruncated != other.isTruncated) return false
        if (isRecursionDesired != other.isRecursionDesired) return false
        if (isRecursionAvailable != other.isRecursionAvailable) return false
        if (reservedBit != other.reservedBit) return false
        if (isAuthenticData != other.isAuthenticData) return false
        if (isCheckingDisabled != other.isCheckingDisabled) return false
        if (rCode != other.rCode) return false
        if (qdCount != other.qdCount) return false
        if (anCount != other.anCount) return false
        if (nsCount != other.nsCount) return false
        if (arCount != other.arCount) return false
        if (questions != other.questions) return false
        if (answers != other.answers) return false
        if (authorities != other.authorities) return false
        if (additionalInfo != other.additionalInfo) return false

        return true
    }

    override fun hashCode(): Int = _hashCode

    override fun toString(): String = _string
}

internal class FieldBackedDnsHeader(
    override val id: UShort,
    override val isResponse: Boolean,
    override val opCode: DnsOpCode,
    override val isAuthoritativeAnswer: Boolean,
    override val isTruncated: Boolean,
    override val isRecursionDesired: Boolean,
    override val isRecursionAvailable: Boolean,
    override val reservedBit: Boolean,
    override val isAuthenticData: Boolean,
    override val isCheckingDisabled: Boolean,
    override val rCode: DnsRCode,
    override val qdCount: UShort,
    override val anCount: UShort,
    override val nsCount: UShort,
    override val arCount: UShort,
    override val questions: List<DnsQuestion>,
    override val answers: List<DnsResourceRecord>,
    override val authorities: List<DnsResourceRecord>,
    override val additionalInfo: List<DnsResourceRecord>
) : AbstractDnsHeader()
