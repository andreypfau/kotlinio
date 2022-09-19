package com.github.andreypfau.kotlinio.packet.dns.rdata

import com.github.andreypfau.kotlinio.packet.dns.DnsDomainName
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic

@JvmSynthetic
inline fun DnsRDataCName(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size): DnsRDataCName =
    DnsRDataCName.newInstance(rawData, offset, length)

@JvmSynthetic
inline fun DnsRDataCName(cName: DnsDomainName): DnsRDataCName =
    DnsRDataCName.newInstance(cName)

interface DnsRDataCName : DnsRData {
    val cName: DnsDomainName

    companion object {
        @JvmStatic
        fun newInstance(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size): DnsRDataCName =
            ByteBacked(rawData, offset, length)

        @JvmStatic
        fun newInstance(cName: DnsDomainName): DnsRDataCName =
            FieldBacked(cName)
    }

    private abstract class AbstractDnsRDataCName : DnsRDataCName {
        abstract override val cName: DnsDomainName
        override val length: Int get() = cName.length
        private val _string by lazy {
            buildString {
                append("CNAME RDATA:").appendLine()
                append("  CNAME: ")
                append(cName)
                appendLine()
            }
        }

        override fun toByteArray(buf: ByteArray, offset: Int): ByteArray {
            return cName.toByteArray(buf, offset)
        }

        override fun toString(): String = _string
        override fun hashCode(): Int = cName.length

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is DnsRDataCName) return false
            if (cName != other.cName) return false
            return true
        }
    }

    private class ByteBacked(
        private val _rawData: ByteArray,
        private val _offset: Int,
        private val _length: Int
    ) : AbstractDnsRDataCName() {
        override val cName: DnsDomainName by lazy {
            DnsDomainName(_rawData, _offset, _length)
        }
    }

    private class FieldBacked(
        override val cName: DnsDomainName
    ) : AbstractDnsRDataCName()
}
