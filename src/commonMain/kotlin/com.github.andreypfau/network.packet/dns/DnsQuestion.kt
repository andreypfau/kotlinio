package com.github.andreypfau.network.packet.dns

import com.github.andreypfau.network.utils.getUShort
import com.github.andreypfau.network.utils.toByteArray

interface DnsQuestion {
    val qName: DnsDomainName
    val qType: DnsResourceRecordType
    val qClass: DnsClass
    val length: Int get() = qName.length + Short.SIZE_BYTES * 2

    fun toByteArray(): ByteArray = toByteArray(ByteArray(length))

    fun toByteArray(buf: ByteArray, offset: Int = 0): ByteArray

    fun builder(): Builder = Builder(qName, qType, qClass)

    companion object {
        fun newInstance(
            rawData: ByteArray,
            offset: Int = 0,
            length: Int = rawData.size - offset
        ): DnsQuestion = ByteBacked(rawData, offset, length)

        fun build(builder: Builder.() -> Unit): DnsQuestion =
            Builder().apply(builder).build()
    }

    data class Builder(
        val qName: DnsDomainName? = null,
        val qType: DnsResourceRecordType? = null,
        val qClass: DnsClass? = null,
    ) {
        fun build(): DnsQuestion = FieldBacked(
            requireNotNull(qName),
            requireNotNull(qType),
            requireNotNull(qClass),
        )
    }

    private abstract class AbstractDnsQuestion : DnsQuestion {
        abstract override val qName: DnsDomainName
        abstract override val qType: DnsResourceRecordType
        abstract override val qClass: DnsClass

        private val _hashCode: Int by lazy {
            var result = qName.hashCode()
            result = 31 * result + qType.hashCode()
            result = 31 * result + qClass.hashCode()
            result
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is DnsQuestion) return false

            if (qName != other.qName) return false
            if (qType != other.qType) return false
            if (qClass != other.qClass) return false

            return true
        }

        override fun hashCode(): Int = _hashCode
    }

    private class ByteBacked(
        private val rawData: ByteArray,
        private val offset: Int,
        private val rawDataLength: Int
    ) : AbstractDnsQuestion() {
        override val qName: DnsDomainName by lazy {
            DnsDomainName.newInstance(rawData, offset, rawDataLength)
        }
        override val qType: DnsResourceRecordType by lazy {
            DnsResourceRecordType[rawData.getUShort(offset + qName.length)]
        }
        override val qClass: DnsClass by lazy {
            DnsClass[rawData.getUShort(qName.length + Short.SIZE_BYTES)]
        }

        override fun toByteArray(buf: ByteArray, offset: Int): ByteArray {
            return rawData.copyInto(buf, offset, 0, rawDataLength)
        }
    }

    private class FieldBacked(
        override val qName: DnsDomainName,
        override val qType: DnsResourceRecordType,
        override val qClass: DnsClass
    ) : AbstractDnsQuestion() {
        override fun toByteArray(buf: ByteArray, offset: Int): ByteArray {
            var cursor = offset
            qName.toByteArray(buf, cursor)
            cursor += qName.length
            qType.value.toByteArray(buf, cursor)
            cursor += Short.SIZE_BYTES
            qClass.value.toByteArray(buf, cursor)
            return buf
        }
    }
}
