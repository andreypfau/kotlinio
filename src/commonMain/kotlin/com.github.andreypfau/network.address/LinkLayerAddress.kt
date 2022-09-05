package com.github.andreypfau.network.address

import com.github.andreypfau.network.utils.hex

abstract class LinkLayerAddress {
    val address: ByteArray get() = toByteArray()
    abstract val size: Int

    open fun toByteArray(): ByteArray = toByteArray(ByteArray(size))
    abstract fun toByteArray(destination: ByteArray, offset: Int = 0): ByteArray

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as LinkLayerAddress

        if (!address.contentEquals(other.address)) return false

        return true
    }

    override fun hashCode(): Int = address.contentHashCode()

    override fun toString(): String = address.hex(":")
}