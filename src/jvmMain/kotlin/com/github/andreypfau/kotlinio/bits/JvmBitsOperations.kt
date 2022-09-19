package com.github.andreypfau.kotlinio.bits

actual fun ByteArray.getShortAt(index: Int): Short = getShortSlowAt(index)
actual fun ByteArray.getIntAt(index: Int): Int = getIntSlowAt(index)
actual fun ByteArray.getLongAt(index: Int): Long = getLongSlowAt(index)

actual fun ByteArray.setShortAt(index: Int, value: Short) = setShortSlowAt(index, value)
actual fun ByteArray.setIntAt(index: Int, value: Int) = setIntSlowAt(index, value)
actual fun ByteArray.setLongAt(index: Int, value: Long) = setLongSlowAt(index, value)
