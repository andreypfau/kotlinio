package com.github.andreypfau.kotlinio.packet.ipv4.option

import com.github.andreypfau.kotlinio.packet.ipv4.IPv4Option

object IpV4EndOfOptionList : IPv4Option {
    override val type: IPv4OptionType get() = IPv4OptionType.END_OF_OPTION_LIST
    override val length: Int get() = 1
    override val rawData: ByteArray get() = byteArrayOf(type.value.toByte())
    override fun toString(): String = "[option-type: ${type}]"
}
