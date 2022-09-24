package com.github.andreypfau.kotlinio.address

class Inet4Address(
    val value: Int = 0
) : InetAddress {
    constructor(data: ByteArray, offset: Int = 0) : this(
        (data[offset + 3].toInt() and 0xFF) or
            ((data[offset + 2].toInt() and 0xFF) shl 8) or
            ((data[offset + 1].toInt() and 0xFF) shl 16) or
            ((data[offset].toInt() and 0xFF) shl 24)
    )

    constructor(string: String) : this(
        string.split('.', limit = 4).let { stringOctets ->
            require(stringOctets.size == SIZE_BYTES) { "Invalid address: $string" }
            byteArrayOf(
                stringOctets[0].toUByte().toByte(),
                stringOctets[1].toUByte().toByte(),
                stringOctets[2].toUByte().toByte(),
                stringOctets[3].toUByte().toByte(),
            )
        }
    )

    private val _string by lazy {
        toString(toByteArray())
    }

    override val size: Int = SIZE_BYTES

    private val address by lazy {
        toByteArray()
    }
    val isMulticastAddress: Boolean
        get() = address[0].toInt() and 0xF0 == 0xE0
    val isAnyLocalAddress: Boolean
        get() = value == 0
    val isLinkLocalAddress: Boolean
        get() = ((address[0].toInt() and 255) == 169) && ((address[1].toInt() and 255) == 254)
    val isSiteLocalAddress: Boolean
        get() = ((address[0].toInt() and 255) == 10) || ((address[0].toInt() and 255) == 172)
            && (((address[1].toInt() and 255) > 15) && (address[1].toInt() and 255) < 32)
            || ((address[0].toInt() and 255) == 192)
            && ((address[1].toInt() and 255) == 168)
    val isMCGlobal: Boolean
        get() {
            if (!isMulticastAddress) return false
            if (value ushr 8 < 0xE00001) return false
            if (value ushr 24 > 0xEE) return false
            return true
        }
    val isMCLinkLocal: Boolean
        get() = value ushr 8 == 0xE00000
    val isMCSiteLocal: Boolean
        get() = value ushr 16 == 0xEFFF
    val isMCOrgLocal: Boolean
        get() {
            val prefix = value ushr 16
            return prefix in 0xEFC0..0xEFC3
        }

    override fun toByteArray(): ByteArray = toByteArray(ByteArray(SIZE_BYTES))

    override fun toByteArray(destination: ByteArray, offset: Int): ByteArray {
        destination[offset] = ((value ushr 24) and 0xFF).toByte()
        destination[offset + 1] = ((value ushr 16) and 0xFF).toByte()
        destination[offset + 2] = ((value ushr 8) and 0xFF).toByte()
        destination[offset + 3] = (value and 0xFF).toByte()
        return destination
    }

    override fun toString(): String = _string

    override fun hashCode(): Int = value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Inet4Address

        if (value != other.value) return false

        return true
    }

    companion object {
        const val SIZE_BYTES = 4
        const val SIZE_BITS = SIZE_BYTES * Byte.SIZE_BITS

        fun toString(src: ByteArray) = buildString {
            append(src[0].toUByte())
            append('.')
            append(src[1].toUByte())
            append('.')
            append(src[2].toUByte())
            append('.')
            append(src[3].toUByte())
        }
    }
}
