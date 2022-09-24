package com.github.andreypfau.kotlinio.packet.ipv4.header

import com.github.andreypfau.kotlinio.address.Inet4Address
import com.github.andreypfau.kotlinio.bits.getUShortAt
import com.github.andreypfau.kotlinio.packet.ip.IpProtocol
import com.github.andreypfau.kotlinio.packet.ipv4.option.IPv4Option
import com.github.andreypfau.kotlinio.packet.ipv4.tos.IPv4Rfc1349Tos
import com.github.andreypfau.kotlinio.packet.ipv4.tos.IPv4Tos

internal class ByteBackedIpV4Header(
    private val _rawData: ByteArray,
    private val _offset: Int,
    length: Int
) : AbstractIpV4Header() {
    init {
        require(length > MIN_IPV4_HEADER_SIZE) {
            "expected: length > $MIN_IPV4_HEADER_SIZE, actual: $length"
        }
    }

    override val versionAndIhl: Byte
        get() = _rawData[VERSION_AND_IHL_OFFSET + _offset]
    override val tos: IPv4Tos
        get() = IPv4Rfc1349Tos(_rawData[TOS_OFFSET + _offset])
    override val totalLength: UShort
        get() = _rawData.getUShortAt(TOTAL_LENGTH_OFFSET + _offset)
    override val identification: UShort
        get() = _rawData.getUShortAt(IDENTIFICATION_OFFSET + _offset)
    override val flags: UShort
        get() = _rawData.getUShortAt(FLAGS_AND_FRAGMENT_OFFSET_OFFSET + _offset)
    override val ttl: UByte
        get() = _rawData[TTL_OFFSET + _offset].toUByte()
    override val protocol: IpProtocol
        get() = IpProtocol[_rawData[PROTOCOL_OFFSET + _offset]]
    override val headerChecksum: UShort
        get() = _rawData.getUShortAt(HEADER_CHECKSUM_OFFSET + _offset)
    override val srcAddress = Inet4Address(_rawData, SRC_ADDR_OFFSET + _offset)
    override val dstAddress = Inet4Address(_rawData, DST_ADDR_OFFSET + _offset)
    override val options: List<IPv4Option>
        get() = emptyList()
    override val padding: ByteArray
        get() {
            val paddingLength = length - OPTIONS_OFFSET
            val startIndex = OPTIONS_OFFSET + _offset
            val endIndex = OPTIONS_OFFSET + _offset + paddingLength
            return if (paddingLength != 0) {
                _rawData.copyOfRange(startIndex, endIndex)
            } else {
                byteArrayOf()
            }
        }

    override fun toByteArray(destination: ByteArray, offset: Int): ByteArray {
        return _rawData.copyInto(destination, offset, _offset, _offset + length)
    }
}
