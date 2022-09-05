package com.github.andreypfau.network.packet.ip

enum class IpVersion constructor(
    val value: Byte,
) {
    IPv4(4),
    IPv6(6);

    companion object {
        operator fun get(value: Byte): IpVersion = when (value.toInt()) {
            4 -> IPv4
            6 -> IPv6
            else -> throw UnsupportedOperationException(
                "Unsupported version: 0x${
                    value.toUByte().toString(16).padStart(2, '0')
                }"
            )
        }
    }
}