package com.github.andreypfau.kotlinio.utils

private val DIGITS = "0123456789abcdef".toCharArray()

/**
 * Encode [bytes] as a HEX string with no spaces, newlines and `0x` prefixes.
 */
fun hex(bytes: ByteArray): String = buildString(bytes.size * 2) {
    bytes.forEach { byte ->
        val b = byte.toInt() and 0xFF
        append(DIGITS[b shr 4])
        append(DIGITS[b and 0x0F])
    }
}

fun hex(bytes: Iterable<Byte>): String = buildString {
    bytes.forEach { byte ->
        val b = byte.toInt() and 0xFF
        append(DIGITS[b shr 4])
        append(DIGITS[b and 0x0F])
    }
}

fun hex(vararg longs: Long): String = buildString {
    longs.forEach {
        append(it.toString(16))
    }
}

/**
 * Decode bytes from HEX string. It should be no spaces and `0x` prefixes.
 */
fun hex(s: String): ByteArray {
    val result = ByteArray(s.length / 2)
    for (idx in result.indices) {
        val srcIdx = idx * 2
        val high = s[srcIdx].toString().toInt(16) shl 4
        val low = s[srcIdx + 1].toString().toInt(16)
        result[idx] = (high or low).toByte()
    }
    return result
}

@Suppress("NOTHING_TO_INLINE")
inline fun String.decodeHex(): ByteArray = hex(this)

@Suppress("NOTHING_TO_INLINE")
inline fun ByteArray.encodeHex(): String = hex(this)


fun UByte.hex(): String = toString(16).padStart(Byte.SIZE_BYTES * 2, '0')
fun Byte.hex(): String = toUByte().hex()

fun UShort.hex(): String = toString(16).padStart(Short.SIZE_BYTES * 2, '0')
fun Short.hex(): String = toUShort().hex()

fun UInt.hex(): String = toString(16).padStart(Int.SIZE_BYTES * 2, '0')
fun Int.hex(): String = toUInt().hex()

fun ULong.hex(): String = toString(16).padStart(Long.SIZE_BYTES * 2, '0')
fun Long.hex(): String = toULong().hex()
