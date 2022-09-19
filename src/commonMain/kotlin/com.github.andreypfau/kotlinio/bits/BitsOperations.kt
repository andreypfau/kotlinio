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
