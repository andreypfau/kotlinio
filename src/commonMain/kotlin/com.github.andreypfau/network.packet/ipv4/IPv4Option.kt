package com.github.andreypfau.network.packet.ipv4

import com.github.andreypfau.network.packet.ipv4.option.IPv4OptionType

interface IPv4Option {
    val type: IPv4OptionType
    val length: Int
    val rawData: ByteArray
}