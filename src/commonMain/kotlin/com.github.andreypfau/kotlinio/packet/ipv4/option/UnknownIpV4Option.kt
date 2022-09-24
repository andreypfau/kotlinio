package com.github.andreypfau.kotlinio.packet.ipv4.option

import com.github.andreypfau.kotlinio.utils.hex

class UnknownIpV4Option(
    private val _rawData: ByteArray,
    private val _offset: Int,
    private val _length: Int
) : IPv4Option {
    override val type: IPv4OptionType = IPv4OptionType[_rawData[_offset].toUByte()]
    val dataLength: Int = _rawData[1 + _offset].toUByte().toInt()
    val data: ByteArray = _rawData.copyOfRange(_offset + 2, _offset + 2 + dataLength)
    override val length: Int get() = data.size + 2
    override val rawData: ByteArray
        get() = ByteArray(length).apply {
            set(0, type.value.toByte())
            set(1, dataLength.toByte())
            data.copyInto(this, 2)
        }

    override fun toString(): String =
        "[option-type: $type] [option-length: $dataLength bytes] [option-data: 0x${data.hex()}]"
}
