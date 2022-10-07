package com.github.andreypfau.kotlinio.crypto.cipher

import com.github.andreypfau.kotlinio.pool.Closeable

interface Cipher : Closeable {
    val mode: CipherMode

    fun setKey(key: ByteArray) = setKey(key, 0, key.size)
    fun setKey(buf: ByteArray, offset: Int, length: Int = buf.size - offset)

    fun setIv(iv: ByteArray) = setIv(iv, 0, iv.size)
    fun setIv(buf: ByteArray, offset: Int, length: Int = buf.size - offset)

    fun encrypt(buf: ByteArray): ByteArray {
        val output = ByteArray(buf.size)
        encrypt(output = output, input = buf)
        return output
    }

    fun encrypt(
        output: ByteArray,
        outputOffset: Int = 0,
        outputLength: Int = output.size - outputOffset,
        input: ByteArray,
        inputOffset: Int = 0,
        inputLength: Int = input.size - inputOffset
    )

    fun decrypt(buf: ByteArray): ByteArray {
        val output = ByteArray(buf.size)
        decrypt(output = output, input = buf)
        return output
    }

    fun decrypt(
        output: ByteArray,
        outputOffset: Int = 0,
        outputLength: Int = output.size - outputOffset,
        input: ByteArray,
        inputOffset: Int = 0,
        inputLength: Int = input.size - inputOffset
    )

    override fun close()
}

enum class CipherMode {
    NONE,
    ECB,
    CFB,
    CBC,
    STREAM,
    OFB,
    CTR,
    GCM
}
