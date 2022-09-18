package com.github.andreypfau.network.packet.dns

import kotlin.jvm.JvmStatic

enum class DnsOpCode(
    val value: Byte
) {
    QUERY(0),
    IQUERY(1),
    STATUS(2),
    NOTIFY(3),
    UPDATE(4);

    companion object {
        private val registry = values().associateBy { it.value }

        @JvmStatic
        fun getOrNull(value: Byte): DnsOpCode? = registry[value]

        @JvmStatic
        operator fun get(value: Byte): DnsOpCode = requireNotNull(getOrNull(value)) {
            "Unsupported opcode: 0x${
                value.toUByte().toString(16).padStart(2, '0')
            }"
        }
    }
}
