package com.github.andreypfau.kotlinio.packet.dns.rdata

import com.github.andreypfau.kotlinio.utils.hex
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic

@JvmSynthetic
inline fun DnsRDataNull(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset): DnsRDataNull =
    DnsRDataNull.newInstance(rawData, offset, length)

interface DnsRDataNull : DnsRData {
    val rawData: ByteArray

    companion object {
        @JvmStatic
        fun newInstance(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset): DnsRDataNull =
            ByteBacked(rawData, offset, length)
    }

    private abstract class AbstractDnsRDataNull : DnsRDataNull {
        abstract override val rawData: ByteArray
        override val length: Int get() = rawData.size

        private val _string by lazy {
            buildString {
                append("NULL DATA:").appendLine()
                append("  data: ")
                append(rawData.hex()).appendLine()
            }
        }
        private val _hashCode by lazy {
            rawData.contentHashCode()
        }


        override fun toString(): String = _string
        override fun hashCode(): Int = _hashCode

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is DnsRDataNull) return false
            if (!rawData.contentEquals(other.rawData)) return false
            return true
        }
    }

    private class ByteBacked(
        private val _rawData: ByteArray,
        private val _offset: Int,
        private val _length: Int
    ) : AbstractDnsRDataNull() {
        override val rawData: ByteArray get() = _rawData.copyOfRange(_offset, _offset + _length)

        override fun toByteArray(buf: ByteArray, offset: Int): ByteArray {
            return _rawData.copyInto(buf)
        }
    }
}
