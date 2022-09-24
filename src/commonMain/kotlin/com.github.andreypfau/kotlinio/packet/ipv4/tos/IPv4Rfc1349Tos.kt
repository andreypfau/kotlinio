package com.github.andreypfau.kotlinio.packet.ipv4.tos

import com.github.andreypfau.kotlinio.utils.shl
import com.github.andreypfau.kotlinio.utils.shr
import kotlin.experimental.and
import kotlin.experimental.or

class IPv4Rfc1349Tos(
    override val value: Byte
) : IPv4Tos {
    val precedence by lazy {
        IPv4TosPrecedence[(value and 0xE0.toByte()) shr 5]
    }
    val tos by lazy {
        IPv4TosTos[(value shr 1) and 0x0F.toByte()]
    }
    val mbz by lazy {
        (value and 0x01) != 0.toByte()
    }
    private val _string by lazy {
        buildString {
            append("[precedence: ")
            append(precedence)
            append("] [tos: ")
            append(tos)
            append("] [mbz: ")
            append(if (mbz) 1 else 0)
            append("]")
        }
    }

    fun builder(): Builder = Builder(precedence, tos, mbz)

    override fun toString(): String = _string

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as IPv4Rfc1349Tos

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int = value.toInt()

    data class Builder(
        var precedence: IPv4TosPrecedence = IPv4TosPrecedence.ROUTINE,
        var tos: IPv4TosTos = IPv4TosTos.DEFAULT,
        var mbz: Boolean = false
    ) {
        fun build(): IPv4Rfc1349Tos {
            var value = precedence.value shl 5
            value = value or (tos.value shl 1)
            if (mbz) {
                value = value or 0x01
            }
            return IPv4Rfc1349Tos(value)
        }
    }
}
