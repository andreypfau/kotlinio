package com.github.andreypfau.kotlinio.packet.ipv4.option

interface IPv4Option {
    val type: IPv4OptionType
    val length: Int
    val rawData: ByteArray
}
