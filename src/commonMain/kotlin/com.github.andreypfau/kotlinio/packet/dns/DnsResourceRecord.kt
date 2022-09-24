package com.github.andreypfau.kotlinio.packet.dns

import com.github.andreypfau.kotlinio.bits.getUIntAt
import com.github.andreypfau.kotlinio.bits.getUShortAt
import com.github.andreypfau.kotlinio.packet.dns.rdata.DnsRData
import com.github.andreypfau.kotlinio.utils.toByteArray
import kotlin.jvm.JvmStatic

inline fun DnsResourceRecord(
    rawData: ByteArray,
    offset: Int = 0,
    length: Int = rawData.size - offset
): DnsResourceRecord =
    DnsResourceRecord.newInstance(rawData, offset, length)

inline fun DnsResourceRecord(builder: DnsResourceRecord.Builder.() -> Unit): DnsResourceRecord =
    DnsResourceRecord.Builder().apply(builder).build()

interface DnsResourceRecord {
    val name: DnsDomainName
    val dataType: DnsResourceRecordType
    val dataClass: DnsClass
    val ttl: UInt
    val rdLength: UShort
    val rData: DnsRData?
    val length: Int

    fun toByteArray(): ByteArray = toByteArray(ByteArray(length))
    fun toByteArray(destination: ByteArray, offset: Int = 0): ByteArray
    fun builder(): Builder = Builder(
        name, dataType, dataClass, ttl, rdLength, rData
    )

    data class Builder(
        var name: DnsDomainName? = null,
        var dataType: DnsResourceRecordType? = null,
        var dataClass: DnsClass? = null,
        var ttl: UInt = UInt.MAX_VALUE,
        var rdLength: UShort? = null,
        var rData: DnsRData? = null
    ) {
        fun build(): DnsResourceRecord = FieldBacked(
            requireNotNull(name),
            requireNotNull(dataType),
            requireNotNull(dataClass),
            ttl,
            rdLength ?: rData?.length?.toUShort() ?: 0u,
            rData
        )

        fun name(builder: DnsDomainName.Builder.() -> Unit) = apply {
            name = DnsDomainName(builder)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset): DnsResourceRecord =
            ByteBacked(rawData, offset, length)

        @JvmStatic
        fun build(builder: Builder.() -> Unit): DnsResourceRecord =
            Builder().apply(builder).build()
    }

    private abstract class AbstractDnsResourceRecord : DnsResourceRecord {
        abstract override val name: DnsDomainName
        abstract override val dataType: DnsResourceRecordType
        abstract override val dataClass: DnsClass
        abstract override val ttl: UInt
        abstract override val rdLength: UShort
        abstract override val rData: DnsRData?

        override val length: Int by lazy {
            name.length + Short.SIZE_BYTES * 3 + Int.SIZE_BYTES + (rData?.length ?: 0)
        }

        private val _string by lazy {
            buildString {
                append("NAME: ").append(name).appendLine()
                append("TYPE: ").append(dataType).appendLine()
                append("CLASS: ").append(dataClass).appendLine()
                append("TTL: ").append(ttl).appendLine()
                append("RD LENGTH: ").append(rdLength)
                if (rData != null) {
                    append("RDATA: ").appendLine()
                    append(rData)
                }
            }
        }

        private val _hashCode by lazy {
            var result = name.hashCode()
            result = 31 * result + dataType.hashCode()
            result = 31 * result + dataClass.hashCode()
            result = 31 * result + ttl.hashCode()
            result = 31 * result + rdLength.hashCode()
            result = 31 * result + (rData?.hashCode() ?: 0)
            result
        }

        override fun toString(): String = _string

        override fun hashCode(): Int = _hashCode

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is DnsResourceRecord) return false
            if (name != other.name) return false
            if (dataType != other.dataType) return false
            if (dataClass != other.dataClass) return false
            if (ttl != other.ttl) return false
            if (rdLength != other.rdLength) return false
            if (rData != other.rData) return false
            return true
        }
    }

    private class ByteBacked(
        private val _rawData: ByteArray,
        private val _offset: Int,
        private val _length: Int
    ) : AbstractDnsResourceRecord() {
        override val name: DnsDomainName by lazy {
            DnsDomainName(_rawData, _offset, _length)
        }
        override val dataType: DnsResourceRecordType by lazy {
            DnsResourceRecordType[_rawData.getUShortAt(_offset + name.length)]
        }
        override val dataClass: DnsClass by lazy {
            DnsClass[_rawData.getUShortAt(_offset + name.length + Short.SIZE_BYTES)]
        }
        override val ttl: UInt by lazy {
            _rawData.getUIntAt(_offset + name.length + Short.SIZE_BYTES * 2)
        }
        override val rdLength: UShort by lazy {
            _rawData.getUShortAt(_offset + name.length + Short.SIZE_BYTES * 2 + Int.SIZE_BYTES)
        }
        override val rData: DnsRData? by lazy {
            val rdLength = rdLength.toInt()
            val o = _offset + name.length + Short.SIZE_BYTES * 3 + Int.SIZE_BYTES
            if (rdLength > 0) {
                dataType.getInstance(
                    _rawData,
                    o,
                    rdLength
                )
            } else null
        }

        override fun toByteArray(destination: ByteArray, offset: Int): ByteArray {
            return _rawData.copyInto(destination, offset, _offset, _offset + length)
        }
    }

    private class FieldBacked(
        override val name: DnsDomainName,
        override val dataType: DnsResourceRecordType,
        override val dataClass: DnsClass,
        override val ttl: UInt,
        override val rdLength: UShort,
        override val rData: DnsRData?
    ) : AbstractDnsResourceRecord() {
        override fun toByteArray(destination: ByteArray, offset: Int): ByteArray {
            var cursor = offset
            name.toByteArray(destination, cursor)
            cursor += name.length
            dataType.value.toByteArray(destination, cursor)
            cursor += Short.SIZE_BYTES
            dataClass.value.toByteArray(destination, cursor)
            cursor += Short.SIZE_BYTES
            ttl.toByteArray(destination, cursor)
            cursor += Int.SIZE_BYTES
            val rdLength = rdLength
            rdLength.toByteArray(destination, cursor)
            val rData = rData
            if (rData != null && rdLength > 0u) {
                cursor += Short.SIZE_BYTES
                rData.toByteArray(destination, cursor)
            }
            return destination
        }
    }
}
