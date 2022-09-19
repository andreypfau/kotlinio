package com.github.andreypfau.kotlinio.bits

import kotlinx.cinterop.*

actual enum class ByteOrder {
    BIG_ENDIAN, LITTLE_ENDIAN;

    actual companion object {
        actual val native: ByteOrder = memScoped {
            val i = alloc<IntVar>()
            i.value = 1
            val bytes = i.reinterpret<ByteVar>()
            if (bytes.value == 0.toByte()) BIG_ENDIAN else LITTLE_ENDIAN
        }
    }
}

actual fun Short.toBigEndian(): Short = when (PLATFORM_BIG_ENDIAN) {
    1 -> this
    else -> swap(this)
}

actual fun Int.toBigEndian(): Int = when (PLATFORM_BIG_ENDIAN) {
    1 -> this
    else -> swap(this)
}

actual fun Long.toBigEndian(): Long = when (PLATFORM_BIG_ENDIAN) {
    1 -> this
    else -> swap(this)
}

actual fun Short.toLittleEndian(): Short = when (PLATFORM_BIG_ENDIAN) {
    1 -> swap(this)
    else -> this
}

actual fun Int.toLittleEndian(): Int = when (PLATFORM_BIG_ENDIAN) {
    1 -> swap(this)
    else -> this
}

actual fun Long.toLittleEndian(): Long = when (PLATFORM_BIG_ENDIAN) {
    1 -> swap(this)
    else -> this
}
