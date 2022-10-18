package com.github.andreypfau.kotlinio.memory

actual enum class ByteOrder {
    BIG_ENDIAN,
    LITTLE_ENDIAN;

    actual companion object {
        actual val native: ByteOrder = if (Platform.isLittleEndian) LITTLE_ENDIAN else BIG_ENDIAN
    }
}

actual fun Short.reverseByteOrder(): Short = reverseByteOrderGeneric()

actual fun Int.reverseByteOrder(): Int = reverseByteOrderGeneric()

actual fun Long.reverseByteOrder(): Long = reverseByteOrderGeneric()

actual fun Float.reverseByteOrder(): Float = reverseByteOrderGeneric()

actual fun Double.reverseByteOrder(): Double = reverseByteOrderGeneric()
