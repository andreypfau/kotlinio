package com.github.andreypfau.kotlinio.packet.dns

import kotlin.jvm.JvmStatic

enum class DnsClass(
    val value: UShort
) {
    IN(1u),
    CH(2u),
    HS(3u),
    NONE(254u),
    ANY(255u),
    ;

    companion object {
        private val registry = values().associateBy { it.value }

        @JvmStatic
        fun getOrNull(value: UShort): DnsClass? = registry[value]

        @JvmStatic
        operator fun get(value: UShort): DnsClass = requireNotNull(getOrNull(value)) {
            "Unsupported DnsClass: $value"
        }
    }
}
