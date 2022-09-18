package com.github.andreypfau.network.packet.dns.rdata

import com.github.andreypfau.network.address.Inet6Address
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic

@JvmSynthetic
inline fun DnsRDataAAAA(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset): DnsRDataAAAA =
    DnsRDataAAAA.newInstance(rawData, offset, length)

@JvmSynthetic
inline fun DnsRDataAAAA(address: Inet6Address): DnsRDataAAAA = DnsRDataAAAA.newInstance(address)

interface DnsRDataAAAA : DnsRData {
    val address: Inet6Address

    companion object {
        @JvmStatic
        fun newInstance(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset): DnsRDataAAAA =
            ByteBacked(rawData, offset, length)

        @JvmStatic
        fun newInstance(address: Inet6Address): DnsRDataAAAA = FieldBacked(address)
    }

    private abstract class AbstractDnsRDataAAAA : DnsRDataAAAA {
        override val length: Int get() = Inet6Address.SIZE_BYTES
        abstract override val address: Inet6Address
        private val _string by lazy {
            buildString {
                append("AAAA RDATA:").appendLine()
                append("  ADDRESS: ").append(address).appendLine()
            }
        }

        override fun toByteArray(buf: ByteArray, offset: Int): ByteArray {
            address.toByteArray(buf, offset)
            return buf
        }

        override fun toString(): String = _string

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is DnsRDataAAAA) return false
            if (address != other.address) return false
            return true
        }

        override fun hashCode(): Int = address.hashCode()
    }

    private class ByteBacked(
        private val _rawData: ByteArray,
        private val _offset: Int,
        _length: Int
    ) : AbstractDnsRDataAAAA() {
        init {
            require(_length >= Inet6Address.SIZE_BYTES) {
                "The data is too short to build ${DnsRDataAAAA::class.simpleName}"
            }
        }

        override val address: Inet6Address by lazy {
            Inet6Address(_rawData, _offset)
        }
    }

    private class FieldBacked(
        override val address: Inet6Address
    ) : AbstractDnsRDataAAAA()
}
