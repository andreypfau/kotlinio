package com.github.andreypfau.hex.internal

internal fun ByteArray.commonEncodeHex(separator: String = ""): String =
    joinToString(separator) { it.toUByte().toString(16).padStart(2, '0') }
