package com.github.andreypfau.kotlinio.memory

import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.DataView
import org.khronos.webgl.Int32Array
import org.khronos.webgl.set

actual enum class ByteOrder {
    BIG_ENDIAN,
    LITTLE_ENDIAN;

    actual companion object {
        actual val native: ByteOrder

        init {
            val buffer = ArrayBuffer(4)
            val arr = Int32Array(buffer)
            val view = DataView(buffer)

            arr[0] = 0x11223344

            native = if (view.getInt32(0, true) == 0x11223344) LITTLE_ENDIAN else BIG_ENDIAN
        }
    }
}

actual fun Short.reverseByteOrder(): Short = reverseByteOrderGeneric()

actual fun Int.reverseByteOrder(): Int = reverseByteOrderGeneric()

actual fun Long.reverseByteOrder(): Long = reverseByteOrderGeneric()

actual fun Float.reverseByteOrder(): Float = reverseByteOrderGeneric()

actual fun Double.reverseByteOrder(): Double = reverseByteOrderGeneric()
