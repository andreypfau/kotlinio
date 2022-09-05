package com.github.andreypfau.network.address

import kotlin.experimental.and

class MacAddress(
    private val data: ByteArray,
    private val offset: Int,
) : LinkLayerAddress() {
    constructor(data: ByteArray) : this(data.copyOf(SIZE_BYTES), 0)

    init {
        require(offset + SIZE_BYTES <= data.size)
    }

    val isUnicast get() = (data[offset] and 1) == 0.toByte()
    val isGloballyUnique get() = (data[offset] and 2) == 0.toByte()
    override val size: Int = SIZE_BYTES

    override fun toByteArray(destination: ByteArray, offset: Int) =
        data.copyInto(destination, offset)

    companion object {
        val ETHER_BROADCAST_ADDRESS = MacAddress(
            byteArrayOf(
                255.toByte(),
                255.toByte(),
                255.toByte(),
                255.toByte(),
                255.toByte(),
                255.toByte(),
            ), 0
        )
        const val SIZE_BYTES = 6
        const val SIZE_BITS = SIZE_BYTES * Byte.SIZE_BITS
    }
}