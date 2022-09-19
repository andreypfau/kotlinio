package com.github.andreypfau.kotlinio.bits

internal inline fun ByteArray.getShortSlowAt(index: Int): Short {
    return ((get(index).toInt() shl 8) or (get(index + 1).toInt() and 0xFF)).toShort()
}

internal inline fun ByteArray.getIntSlowAt(index: Int): Int {
    return (get(index).toInt() and 0xFF shl 24) or
        (get(index + 1).toInt() and 0xFF shl 16) or
        (get(index + 2).toInt() and 0xFF shl 8) or
        (get(index + 3).toInt() and 0xFF)
}

internal inline fun ByteArray.getLongSlowAt(index: Int): Long {
    return ((get(index).toLong() and 0xFF shl 56) or
        (get(index + 1).toLong() and 0xFF shl 48) or
        (get(index + 2).toLong() and 0xFF shl 40) or
        (get(index + 3).toLong() and 0xFF shl 32) or
        (get(index + 4).toLong() and 0xFF shl 24) or
        (get(index + 5).toLong() and 0xFF shl 16) or
        (get(index + 6).toLong() and 0xFF shl 8) or
        (get(index + 7).toLong() and 0xFF))
}

internal inline fun ByteArray.setShortSlowAt(index: Int, value: Short) {
    set(index, (value.toInt() ushr 8).toByte())
    set(index + 1, (value.toInt() and 0xFF).toByte())
}

internal inline fun ByteArray.setIntSlowAt(index: Int, value: Int) {
    set(index, (value ushr 24).toByte())
    set(index + 1, (value ushr 16).toByte())
    set(index + 2, (value ushr 8).toByte())
    set(index + 3, (value and 0xFF).toByte())
}

internal inline fun ByteArray.setLongSlowAt(index: Int, value: Long) {
    set(index, (value ushr 56).toByte())
    set(index + 1, (value ushr 48).toByte())
    set(index + 2, (value ushr 40).toByte())
    set(index + 3, (value ushr 32).toByte())
    set(index + 4, (value ushr 24).toByte())
    set(index + 5, (value ushr 16).toByte())
    set(index + 6, (value ushr 8).toByte())
    set(index + 7, (value and 0xFF).toByte())
}
