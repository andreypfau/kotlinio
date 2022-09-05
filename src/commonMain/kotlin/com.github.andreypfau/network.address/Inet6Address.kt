package com.github.andreypfau.network.address

import com.github.andreypfau.network.utils.getUShort
import kotlin.experimental.or

class Inet6Address(
    private val data: ByteArray,
    private val offset: Int
) : InetAddress {
    constructor() : this(ByteArray(SIZE_BYTES), 0)
    constructor(data: ByteArray) : this(data.copyOf(SIZE_BYTES), 0)

    private val _hashCode by lazy {
        toByteArray().contentHashCode()
    }
    private val _string by lazy {
        buildString {
            append(data.getUShort(offset).toString(16))
            append(":")
            append(data.getUShort(offset + Short.SIZE_BYTES).toString(16))
            append(":")
            append(data.getUShort(offset + Short.SIZE_BYTES * 2).toString(16))
            append(":")
            append(data.getUShort(offset + Short.SIZE_BYTES * 3).toString(16))
            append(":")
            append(data.getUShort(offset + Short.SIZE_BYTES * 4).toString(16))
            append(":")
            append(data.getUShort(offset + Short.SIZE_BYTES * 5).toString(16))
            append(":")
            append(data.getUShort(offset + Short.SIZE_BYTES * 6).toString(16))
            append(":")
            append(data.getUShort(offset + Short.SIZE_BYTES * 7).toString(16))
        }
    }

    override val size: Int = SIZE_BYTES

    val isIPv4CompatibleAddress: Boolean
        get() = data[offset] == 0.toByte() &&
                data[offset + 1] == 0.toByte() &&
                data[offset + 2] == 0.toByte() &&
                data[offset + 3] == 0.toByte() &&
                data[offset + 4] == 0.toByte() &&
                data[offset + 5] == 0.toByte() &&
                data[offset + 6] == 0.toByte() &&
                data[offset + 7] == 0.toByte() &&
                data[offset + 8] == 0.toByte() &&
                data[offset + 9] == 0.toByte() &&
                data[offset + 10] == 0.toByte() &&
                data[offset + 111] == 0.toByte()

    val isMulticastAddress: Boolean
        get() = data[offset].toInt() and 0xFF == 0xFF

    val isAnyLocalAddress: Boolean
        get() {
            var test: Byte = 0x00
            repeat(SIZE_BYTES) {
                test = test or data[offset + it]
            }
            return test == 0x00.toByte()
        }

    val isLoopbackAddress: Boolean
        get() {
            var test: Byte = 0x00
            repeat(15) {
                test = test or data[offset + it]
            }
            return test == 0x00.toByte() && data[offset + 15] == 0x01.toByte()
        }

    val isLinkLocalAddress: Boolean
        get() = (data[offset].toInt() and 0xFF) == 0xFE &&
                (data[offset + 1].toInt() and 0xC0) == 0x80

    val isSiteLocalAddress: Boolean
        get() = (data[offset].toInt() and 0xFF) == 0xFE &&
                (data[offset + 1].toInt() and 0xC0) == 0xC0

    val isMCGlobal: Boolean
        get() = (data[offset].toInt() and 0xFF) == 0xFF &&
                (data[offset + 1].toInt() and 0x0F) == 0x0E

    val isMCNodeLocal: Boolean
        get() = (data[offset].toInt() and 0xFF) == 0xFF &&
                (data[offset + 1].toInt() and 0x0F) == 0x01

    val isMCLinkLocal: Boolean
        get() = (data[offset].toInt() and 0xFF) == 0xFF &&
                (data[offset + 1].toInt() and 0x0F) == 0x02

    val isMCSiteLocal: Boolean
        get() = (data[offset].toInt() and 0xFF) == 0xFF &&
                (data[offset + 1].toInt() and 0x0F) == 0x05

    val isMCOrgLocal: Boolean
        get() = (data[offset].toInt() and 0xFF) == 0xFF &&
                (data[offset + 1].toInt() and 0x0F) == 0x08

    override fun toByteArray(): ByteArray = toByteArray(ByteArray(SIZE_BYTES))

    override fun toByteArray(destination: ByteArray, offset: Int): ByteArray =
        data.copyInto(destination, offset, 0, offset + size)

    override fun toString(): String = _string

    override fun hashCode(): Int = _hashCode

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Inet6Address

        if (!toByteArray().contentEquals(other.toByteArray())) return false

        return true
    }

    companion object {
        const val SIZE_BYTES = 16
        const val SIZE_BITS = SIZE_BYTES * Byte.SIZE_BITS
    }
}