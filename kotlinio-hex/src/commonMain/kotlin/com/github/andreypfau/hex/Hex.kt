package com.github.andreypfau.hex

import com.github.andreypfau.hex.internal.commonEncodeHex

object Hex {
    fun encode(byteArray: ByteArray, separator: String = ""): String = byteArray.commonEncodeHex(separator)
}

fun ByteArray.encodeHex(): String = Hex.encode(this)
