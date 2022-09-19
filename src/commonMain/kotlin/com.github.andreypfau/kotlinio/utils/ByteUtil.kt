package com.github.andreypfau.kotlinio.utils

internal infix fun Byte.shr(bitSize: Int): Byte = toInt().shr(bitSize).toByte()
internal infix fun Byte.ushr(bitSize: Int): Byte = toInt().ushr(bitSize).toByte()
internal infix fun Byte.shl(bitSize: Int): Byte = toInt().shl(bitSize).toByte()

internal infix fun UByte.shr(bitSize: Int): UByte = toInt().shr(bitSize).toUByte()
internal infix fun UByte.ushr(bitSize: Int): UByte = toInt().ushr(bitSize).toUByte()
internal infix fun UByte.shl(bitSize: Int): UByte = toInt().shl(bitSize).toUByte()

internal infix fun Short.shr(bitSize: Int): Short = toInt().shr(bitSize).toShort()
internal infix fun Short.ushr(bitSize: Int): Short = toInt().ushr(bitSize).toShort()
internal infix fun Short.shl(bitSize: Int): Short = toInt().shl(bitSize).toShort()

internal infix fun UShort.shr(bitSize: Int): UShort = toInt().shr(bitSize).toUShort()
internal infix fun UShort.ushr(bitSize: Int): UShort = toInt().ushr(bitSize).toUShort()
internal infix fun UShort.shl(bitSize: Int): UShort = toInt().shl(bitSize).toUShort()

internal infix fun UInt.shr(bitSize: Int): UInt = toInt().shr(bitSize).toUInt()
internal infix fun UInt.ushr(bitSize: Int): UInt = toInt().ushr(bitSize).toUInt()
internal infix fun UInt.shl(bitSize: Int): UInt = toInt().shl(bitSize).toUInt()
