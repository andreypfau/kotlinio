package com.github.andreypfau.network.address

class Inet4Address(
    val value: Int = 0
) : InetAddress {
    constructor(data: ByteArray, offset: Int = 0) : this(
        (data[offset + 3].toInt() and 0xFF) or
                ((data[offset + 2].toInt() and 0xFF) shl 8) or
                ((data[offset + 1].toInt() and 0xFF) shl 16) or
                ((data[offset].toInt() and 0xFF) shl 24)
    )

    private val _string by lazy {
        toString(toByteArray())
    }

    override val size: Int = SIZE_BYTES

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