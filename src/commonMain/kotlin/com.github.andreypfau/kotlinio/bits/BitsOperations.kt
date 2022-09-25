package com.github.andreypfau.kotlinio.bits

fun ByteArray.getByteAt(index: Int): Byte = get(index)
expect fun ByteArray.getShortAt(index: Int): Short
expect fun ByteArray.getIntAt(index: Int): Int
expect fun ByteArray.getLongAt(index: Int): Long

fun ByteArray.getUByteAt(index: Int): UByte = get(index).toUByte()
fun ByteArray.getUShortAt(index: Int): UShort = getShortAt(index).toUShort()
fun ByteArray.getUIntAt(index: Int): UInt = getIntAt(index).toUInt()
fun ByteArray.getULongAt(index: Int): ULong = getLongAt(index).toULong()

fun ByteArray.setByteAt(index: Int, value: Byte) = set(index, value)
expect fun ByteArray.setShortAt(index: Int, value: Short)
expect fun ByteArray.setIntAt(index: Int, value: Int)
expect fun ByteArray.setLongAt(index: Int, value: Long)

fun ByteArray.setUByteAt(index: Int, value: UByte) = set(index, value.toByte())
fun ByteArray.setUShortAt(index: Int, value: UShort) = setShortAt(index, value.toShort())
fun ByteArray.setUIntAt(index: Int, value: UInt) = setIntAt(index, value.toInt())
fun ByteArray.setULongAt(index: Int, value: ULong) = setLongAt(index, value.toLong())

fun Boolean.toInt(): Int = if (this) 1 else 0
operator fun ByteArray.set(index: Int, value: UByte) = set(index, value.toByte())

@Suppress("NOTING_TO_INLINE")
private inline fun bitMask(index: Int) = (1 shl (7 - (index % Byte.SIZE_BITS)))

fun Byte.setBitAt(index: Int, value: Boolean): Byte {
    val bitMask = bitMask(index)
    return if (value) {
        toInt() or bitMask
    } else {
        toInt() and bitMask.inv()
    }.toByte()
}

fun UByte.setBitAt(index: Int, value: Boolean): UByte {
    val bitMask = bitMask(index)
    return if (value) {
        toInt() or bitMask
    } else {
        toInt() and bitMask.inv()
    }.toUByte()
}

fun Byte.getBitAt(index: Int): Boolean {
    val bitMask = bitMask(index)
    return (toInt() and bitMask) != 0
}

fun UByte.getBitAt(index: Int): Boolean {
    val bitMask = bitMask(index)
    return (toInt() and bitMask) != 0
}

fun UByte.binary(): String = toString(2).padStart(Byte.SIZE_BITS, '0')
fun Byte.binary(): String = toUByte().binary()

fun UShort.binary(): String = toString(2).padStart(Short.SIZE_BITS, '0')
fun Short.binary(): String = toUShort().binary()

fun UInt.binary(): String = toString(2).padStart(Int.SIZE_BITS, '0')
fun Int.binary(): String = toUInt().binary()

fun ULong.binary(): String = toString(2).padStart(Long.SIZE_BITS, '0')
fun Long.binary(): String = toUInt().binary()
