package com.github.andreypfau.kotlinio.utils

import com.github.andreypfau.kotlinio.bits.getShortAt
import com.github.andreypfau.kotlinio.bits.setIntAt
import com.github.andreypfau.kotlinio.bits.setLongAt
import com.github.andreypfau.kotlinio.bits.setShortAt

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
    destination.setShortAt(destinationOffset, this)
    return destination
}

internal fun Int.toByteArray(): ByteArray = toByteArray(ByteArray(Int.SIZE_BYTES))
internal fun Int.toByteArray(destination: ByteArray, destinationOffset: Int = 0): ByteArray {
    destination.setIntAt(destinationOffset, this)
    return destination
}

internal fun UInt.toByteArray(): ByteArray = toInt().toByteArray()
internal fun UInt.toByteArray(destination: ByteArray, destinationOffset: Int = 0): ByteArray =
    toInt().toByteArray(destination, destinationOffset)

internal fun Long.toByteArray(): ByteArray = toByteArray(ByteArray(Long.SIZE_BYTES))
internal fun Long.toByteArray(destination: ByteArray, destinationOffset: Int = 0): ByteArray {
    destination.setLongAt(destinationOffset, this)
    return destination
}

internal fun ULong.toByteArray(): ByteArray = toLong().toByteArray()
internal fun ULong.toByteArray(destination: ByteArray, destinationOffset: Int = 0): ByteArray =
    toLong().toByteArray(destination, destinationOffset)

internal fun ByteArray.checksum(): Short {
    var sum: Long = 0
    var i = 1
    while (i < size) {
        sum += (0xFFFFL and getShortAt(i - 1).toLong())
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
