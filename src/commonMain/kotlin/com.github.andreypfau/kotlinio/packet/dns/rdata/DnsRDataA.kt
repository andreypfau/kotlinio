package com.github.andreypfau.kotlinio.packet.dns.rdata

import com.github.andreypfau.kotlinio.address.Inet4Address
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic

@JvmSynthetic
inline fun DnsRDataA(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset): DnsRDataA =
    DnsRDataA.newInstance(rawData, offset, length)

@JvmSynthetic
inline fun DnsRDataA(address: Inet4Address): DnsRDataA = DnsRDataA.newInstance(address)

interface DnsRDataA : DnsRData {
    val address: Inet4Address

    companion object {
        @JvmStatic
        fun newInstance(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset): DnsRDataA =
            ByteBacked(rawData, offset, length)

        @JvmStatic
        fun newInstance(address: Inet4Address): DnsRDataA = FieldBacked(address)
    }

    private abstract class AbstractDnsRDataA : DnsRDataA {
        override val length: Int get() = Inet4Address.SIZE_BYTES
        abstract override val address: Inet4Address
        private val _string by lazy {
            buildString {
                append("A RDATA:").appendLine()
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
            if (other !is DnsRDataA) return false
            if (address != other.address) return false
            return true
        }

        override fun hashCode(): Int = address.hashCode()
    }

    private class ByteBacked(
        private val rawData: ByteArray,
        private val rawDataOffset: Int,
        override val length: Int
    ) : AbstractDnsRDataA() {
        override val address: Inet4Address by lazy {
            if (length == Inet4Address.SIZE_BYTES) {
                Inet4Address(rawData, rawDataOffset)
            } else {
                Inet4Address(rawData.decodeToString(rawDataOffset, rawDataOffset + length))
            }
        }
    }

    private class FieldBacked(
        override val address: Inet4Address
    ) : AbstractDnsRDataA()
}
