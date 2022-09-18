package com.github.andreypfau.network.packet.dns

import com.github.andreypfau.network.packet.dns.rdata.DnsRData
import com.github.andreypfau.network.utils.getUInt
import com.github.andreypfau.network.utils.getUShort

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

    private abstract class AbstractDnsResourceRecord : DnsResourceRecord {
        override val length: Int by lazy {
            name.length + Short.SIZE_BYTES * 3 + Int.SIZE_BYTES + (rData?.length ?: 0)
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
            DnsResourceRecordType[_rawData.getUShort(_offset + name.length)]
        }
        override val dataClass: DnsClass by lazy {
            DnsClass[_rawData.getUShort(_offset + name.length + Short.SIZE_BYTES)]
        }
        override val ttl: UInt by lazy {
            _rawData.getUInt(_offset + name.length + Short.SIZE_BYTES * 2)
        }
        override val rdLength: UShort by lazy {
            _rawData.getUShort(_offset + name.length + Short.SIZE_BYTES * 2 + Int.SIZE_BYTES)
        }
        override val rData: DnsRData? by lazy {
            val rdLength = rdLength.toInt()
            if (rdLength != 0) {
                dataType.payloadFactory(
                    _rawData,
                    _offset + name.length + Short.SIZE_BYTES * 3 + Int.SIZE_BYTES,
                    rdLength
                )
            } else null
        }

        override fun toByteArray(destination: ByteArray, offset: Int): ByteArray {
            return _rawData.copyInto(destination, offset, 0, length)
        }
    }
}
