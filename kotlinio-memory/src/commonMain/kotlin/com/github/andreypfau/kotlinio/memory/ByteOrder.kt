package com.github.andreypfau.kotlinio.memory

expect enum class ByteOrder {
    BIG_ENDIAN,
    LITTLE_ENDIAN;

    companion object {
        val native: ByteOrder
    }
}

expect fun Short.reverseByteOrder(): Short
expect fun Int.reverseByteOrder(): Int
expect fun Long.reverseByteOrder(): Long
expect fun Float.reverseByteOrder(): Float
expect fun Double.reverseByteOrder(): Double

fun UShort.reverseByteOrder(): UShort = toShort().reverseByteOrder().toUShort()
fun UInt.reverseByteOrder(): UInt = toInt().reverseByteOrder().toUInt()
fun ULong.reverseByteOrder(): ULong = toLong().reverseByteOrder().toULong()

inline val Short.highByte: Byte get() = (toInt() ushr 8).toByte()
inline val Short.lowByte: Byte get() = (toInt() and 0xff).toByte()
inline val Int.highShort: Short get() = (this ushr 16).toShort()
inline val Int.lowShort: Short get() = (this and 0xffff).toShort()
inline val Long.highInt: Int get() = (this ushr 32).toInt()
inline val Long.lowInt: Int get() = (this and 0xffffffffL).toInt()

inline fun Short.toBigEndian(): Short = when (ByteOrder.native) {
    ByteOrder.BIG_ENDIAN -> this
    else -> reverseByteOrder()
}

inline fun Short.toLittleEndian(): Short = when (ByteOrder.native) {
    ByteOrder.LITTLE_ENDIAN -> this
    else -> reverseByteOrder()
}

inline fun UShort.toBigEndian(): UShort = toShort().toBigEndian().toUShort()

inline fun UShort.toLittleEndian(): UShort = toShort().toLittleEndian().toUShort()

inline fun Int.toBigEndian(): Int = when (ByteOrder.native) {
    ByteOrder.BIG_ENDIAN -> this
    else -> reverseByteOrder()
}

inline fun Int.toLittleEndian(): Int = when (ByteOrder.native) {
    ByteOrder.LITTLE_ENDIAN -> this
    else -> reverseByteOrder()
}

inline fun UInt.toBigEndian(): UInt = toInt().toBigEndian().toUInt()

inline fun UInt.toLittleEndian(): UInt = toInt().toLittleEndian().toUInt()

inline fun Long.toBigEndian(): Long = when (ByteOrder.native) {
    ByteOrder.BIG_ENDIAN -> this
    else -> reverseByteOrder()
}

inline fun Long.toLittleEndian(): Long = when (ByteOrder.native) {
    ByteOrder.LITTLE_ENDIAN -> this
    else -> reverseByteOrder()
}

inline fun ULong.toBigEndian(): ULong = toLong().toBigEndian().toULong()

inline fun ULong.toLittleEndian(): ULong = toLong().toLittleEndian().toULong()

inline fun Float.toBigEndian(): Float = when (ByteOrder.native) {
    ByteOrder.BIG_ENDIAN -> this
    else -> reverseByteOrder()
}

inline fun Float.toLittleEndian(): Float = when (ByteOrder.native) {
    ByteOrder.LITTLE_ENDIAN -> this
    else -> reverseByteOrder()
}

inline fun Double.toBigEndian(): Double = when (ByteOrder.native) {
    ByteOrder.BIG_ENDIAN -> this
    else -> reverseByteOrder()
}

inline fun Double.toLittleEndian(): Double = when (ByteOrder.native) {
    ByteOrder.LITTLE_ENDIAN -> this
    else -> reverseByteOrder()
}
