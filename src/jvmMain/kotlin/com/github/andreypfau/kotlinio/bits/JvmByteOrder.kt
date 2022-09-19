package com.github.andreypfau.kotlinio.bits

actual enum class ByteOrder {
    BIG_ENDIAN,
    LITTLE_ENDIAN;

    actual companion object {
        actual val native: ByteOrder = when (java.nio.ByteOrder.nativeOrder()) {
            java.nio.ByteOrder.BIG_ENDIAN -> BIG_ENDIAN
            else -> LITTLE_ENDIAN
        }
    }
}

actual fun Short.toBigEndian(): Short = when (ByteOrder.native) {
    ByteOrder.BIG_ENDIAN -> this
    else -> swap(this)
}

actual fun Int.toBigEndian(): Int = when (ByteOrder.native) {
    ByteOrder.BIG_ENDIAN -> this
    else -> swap(this)
}

actual fun Long.toBigEndian(): Long = when (ByteOrder.native) {
    ByteOrder.BIG_ENDIAN -> this
    else -> swap(this)
}

actual fun Short.toLittleEndian(): Short = when (ByteOrder.native) {
    ByteOrder.BIG_ENDIAN -> swap(this)
    else -> this
}

actual fun Int.toLittleEndian(): Int = when (ByteOrder.native) {
    ByteOrder.BIG_ENDIAN -> swap(this)
    else -> this
}

actual fun Long.toLittleEndian(): Long = when (ByteOrder.native) {
    ByteOrder.BIG_ENDIAN -> swap(this)
    else -> this
}
