package com.github.andreypfau.kotlinio.base64

import com.github.andreypfau.kotlinio.base64.internal.BASE64
import com.github.andreypfau.kotlinio.base64.internal.commonDecodeBase64ToArray
import com.github.andreypfau.kotlinio.base64.internal.commonEncodeBse64

object Base64 {
    fun encode(value: ByteArray): String = value.commonEncodeBse64(BASE64)

    fun decode(value: String): ByteArray =
        value.commonDecodeBase64ToArray()
            ?: throw IllegalArgumentException("Can't decode string: '${value.replace("\n", "")}'")
}
