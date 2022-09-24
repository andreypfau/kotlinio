package com.github.andreypfau.kotlinio.packet.ip

import com.github.andreypfau.kotlinio.utils.hex
import com.github.andreypfau.kotlinio.utils.toByteArray

enum class IpVersion constructor(
    val value: Byte,
) {
    IPv4(4),
    IPv6(6);

    companion object {
        operator fun get(value: Byte): IpVersion =
            when ((value.toInt() and 0xF0 shr 4)) {
                4 -> IPv4
                6 -> IPv6
                else -> when (value.toInt()) {
                    4 -> IPv4
                    6 -> IPv6
                    else -> throw IllegalArgumentException("Unknown IP Version: 0x${value.toByteArray().hex()}")
                }
            }
    }
}
