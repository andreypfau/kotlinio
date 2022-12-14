package com.github.andreypfau.kotlinio.packet.ipv4.option

object IpV4NoOperationOption : IPv4Option {
    override val type: IPv4OptionType get() = IPv4OptionType.NO_OPERATION
    override val length: Int get() = 1
    override val rawData: ByteArray get() = byteArrayOf(type.value.toByte())
    override fun toString(): String = "[option-type: $type]"
}
