package com.github.andreypfau.kotlinio.address

import kotlin.experimental.and

class MacAddress(
    private val _rawData: ByteArray,
    private val _offset: Int = 0,
) : LinkLayerAddress() {
    constructor(data: ByteArray) : this(data.copyOf(SIZE_BYTES), 0)
    constructor(string: String) : this(
        try {
            string.split(":", "-").let { octets ->
                require(octets.size == 6)
                octets.map { it.toInt(16).toByte() }.toByteArray()
            }
        } catch(e: Exception) {
            throw IllegalArgumentException("Can't parse MacAddress: '$string'", e)
        }
    )

    init {
        require(_offset + SIZE_BYTES <= _rawData.size)
    }

    val isUnicast get() = (_rawData[_offset] and 1) == 0.toByte()
    val isGloballyUnique get() = (_rawData[_offset] and 2) == 0.toByte()
    override val size: Int = SIZE_BYTES

    override fun toByteArray(destination: ByteArray, offset: Int) =
        _rawData.copyInto(destination, offset, _offset, _offset + SIZE_BYTES)

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
