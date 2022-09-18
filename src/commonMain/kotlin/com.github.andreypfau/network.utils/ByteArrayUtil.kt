package com.github.andreypfau.network.utils

internal fun ByteArray.hex(separator: String = "") =
    joinToString(separator) { it.toUByte().toString(16).padStart(2, '0') }

internal fun Collection<ByteArray>.concatenate(): ByteArray {
    val size = sumOf { it.size }
    val result = ByteArray(size)
    var destOffset = 0
    forEach { array ->
        array.copyInto(result, destOffset)
        destOffset += array.size
    }
    return result
}

internal fun Byte.toByteArray() = ByteArray(Byte.SIZE_BYTES).also {
    toByteArray(it)
}

internal fun Byte.toByteArray(destination: ByteArray, destinationOffset: Int = 0) {
    destination[destinationOffset] = this
}


internal fun Short.toByteArray() =
    toByteArray(ByteArray(Short.SIZE_BYTES))

internal fun UShort.toByteArray() =
    toShort().toByteArray(ByteArray(Short.SIZE_BYTES))

internal fun UShort.toByteArray(destination: ByteArray, destinationOffset: Int = 0) =
    toShort().toByteArray(destination, destinationOffset)

internal fun Short.toByteArray(destination: ByteArray, destinationOffset: Int = 0): ByteArray {
    destination[destinationOffset] = shr(Byte.SIZE_BITS).toByte()
    destination[destinationOffset + 1] = toByte()
    return destination
}

internal fun ByteArray.getUShort(offset: Int): UShort = getShort(offset).toUShort()
internal fun ByteArray.getShort(offset: Int): Short =
    ((get(offset).toInt() shl Byte.SIZE_BITS) or (0xFF and get(offset + 1).toInt())).toShort()


internal fun ByteArray.getUInt(offset: Int): UInt = getInt(offset).toUInt()
internal fun ByteArray.getInt(offset: Int): Int =
    (get(offset).toInt() shl (Byte.SIZE_BITS * 3)) or
            ((get(offset + 1).toInt() and 0xFF) shl (Byte.SIZE_BITS * 2)) or
            ((get(offset + 2).toInt() and 0xFF) shl (Byte.SIZE_BITS * 1)) or
            (get(offset + 3).toInt() and 0xFF)

internal fun Int.toByteArray(): ByteArray = toByteArray(ByteArray(Int.SIZE_BYTES))
internal fun Int.toByteArray(destination: ByteArray, destinationOffset: Int = 0): ByteArray {
    destination[destinationOffset] = shr(Byte.SIZE_BITS * 3).toByte()
    destination[destinationOffset + 1] = shr(Byte.SIZE_BITS * 2).toByte()
    destination[destinationOffset + 2] = shr(Byte.SIZE_BITS * 1).toByte()
    destination[destinationOffset + 3] = toByte()
    return destination
}

internal fun UInt.toByteArray(): ByteArray = toInt().toByteArray()
internal fun UInt.toByteArray(destination: ByteArray, destinationOffset: Int = 0): ByteArray =
    toInt().toByteArray(destination, destinationOffset)

internal fun ByteArray.checksum(): Short {
    var sum: Long = 0
    var i = 1
    while (i < size) {
        sum += (0xFFFFL and getShort(i - 1).toLong())
        i += Short.SIZE_BYTES
    }
    if (size % 2 != 0) {
        sum += 0xFFFFL and (get(size - 1).toInt() shl Byte.SIZE_BITS).toLong()
    }
    while ((sum shr (Byte.SIZE_BITS * Short.SIZE_BYTES)) != 0L) {
        sum = (0xFFFFL and sum) + (sum ushr (Byte.SIZE_BITS * Short.SIZE_BYTES))
    }
    return sum.inv().toShort()
}
