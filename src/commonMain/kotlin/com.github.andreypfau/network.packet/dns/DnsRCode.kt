package com.github.andreypfau.network.packet.dns

import kotlin.jvm.JvmStatic

enum class DnsRCode(
    val value: Byte
) {
    NO_ERROR(0),
    FORM_ERR(1),
    SERV_FAIL(2),
    NX_DOMAIN(3),
    NOT_IMP(4),
    REFUSED(5),
    YX_DOMAIN(6),
    YX_RR_SET(7),
    NX_RR_SET(8),
    NOT_AUTH(9),
    NOT_ZONE(10)
    ;

    companion object {
        private val registry = values().associateBy { it.value }

        @JvmStatic
        fun getOrNull(value: Byte): DnsRCode? = registry[value]

        @JvmStatic
        operator fun get(value: Byte): DnsRCode = requireNotNull(getOrNull(value)) {
            "Unsupported R Code: 0x${
                value.toUByte().toString(16).padStart(2, '0')
            }"
        }
    }
}
