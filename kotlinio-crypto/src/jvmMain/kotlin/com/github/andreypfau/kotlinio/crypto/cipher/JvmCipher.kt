package com.github.andreypfau.kotlinio.crypto.cipher

import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

abstract class JvmCipher internal constructor(
    private val algorithm: String,
    final override val mode: CipherMode
) : Cipher {
    private var encryptCipher = javax.crypto.Cipher.getInstance("$algorithm/$mode/NoPadding")
    private var decryptCipher = javax.crypto.Cipher.getInstance("$algorithm/$mode/NoPadding")
    private var key = KeyGenerator.getInstance(algorithm).generateKey()
    private var iv = decryptCipher.parameters.getParameterSpec(IvParameterSpec::class.java)

    override fun setKey(buf: ByteArray, offset: Int, length: Int) {
        key = SecretKeySpec(buf, offset, length, algorithm)
        encryptCipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key, iv)
        decryptCipher.init(javax.crypto.Cipher.DECRYPT_MODE, key, iv)
    }

    override fun setIv(buf: ByteArray, offset: Int, length: Int) {
        iv = IvParameterSpec(buf, offset, length)
        encryptCipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key, iv)
        decryptCipher.init(javax.crypto.Cipher.DECRYPT_MODE, key, iv)
    }

    override fun encrypt(
        output: ByteArray,
        outputOffset: Int,
        outputLength: Int,
        input: ByteArray,
        inputOffset: Int,
        inputLength: Int
    ) {
        update(encryptCipher, input, inputOffset, inputLength, output, outputOffset, outputLength)
    }

    override fun decrypt(
        output: ByteArray,
        outputOffset: Int,
        outputLength: Int,
        input: ByteArray,
        inputOffset: Int,
        inputLength: Int
    ) {
        update(decryptCipher, input, inputOffset, inputLength, output, outputOffset, outputLength)
    }

    private fun update(
        cipher: javax.crypto.Cipher,
        input: ByteArray,
        inputOffset: Int,
        inputLength: Int,
        output: ByteArray,
        outputOffset: Int,
        outputLength: Int,
    ) {
        val padding = inputLength % 16
        if (padding == 0) {
            cipher.update(input, inputOffset, inputLength, output, outputOffset)
        } else {
            val newInput = ByteArray(input.size + (16 - padding))
            input.copyInto(newInput, 0, inputOffset, inputOffset + inputLength)
            val newOutput = cipher.update(newInput)
            newOutput.copyInto(output, outputOffset, 0, minOf(newOutput.size, outputLength))
        }
    }

    override fun close() {
        decryptCipher.doFinal()
    }
}
