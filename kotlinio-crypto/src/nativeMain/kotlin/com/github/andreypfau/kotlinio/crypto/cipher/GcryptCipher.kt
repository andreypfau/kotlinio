package com.github.andreypfau.kotlinio.crypto.cipher

import cnames.structs.gcry_cipher_handle
import gcrypt.*
import kotlinx.cinterop.*

abstract class GcryptCipher(
    private val gcryptAlgorithm: Int,
    override val mode: CipherMode
) : Cipher {
    private val handle = memScoped {
        val handlePtr = allocPointerTo<gcry_cipher_handle>()
        val mode = when (mode) {
            CipherMode.NONE -> GCRY_CIPHER_MODE_NONE
            CipherMode.ECB -> GCRY_CIPHER_MODE_ECB
            CipherMode.CFB -> GCRY_CIPHER_MODE_CFB
            CipherMode.CBC -> GCRY_CIPHER_MODE_CBC
            CipherMode.STREAM -> GCRY_CIPHER_MODE_STREAM
            CipherMode.OFB -> GCRY_CIPHER_MODE_OFB
            CipherMode.CTR -> GCRY_CIPHER_MODE_CTR
            CipherMode.GCM -> GCRY_CIPHER_MODE_GCM
        }
        gcry_cipher_open(handlePtr.ptr, gcryptAlgorithm, mode.convert(), 0)
        requireNotNull(handlePtr.value)
    }

    override fun setKey(buf: ByteArray, offset: Int, length: Int) {
        if (length == 0) return
        buf.usePinned {
            gcry_cipher_setkey(handle, it.addressOf(offset), length.convert())
        }
    }

    override fun setIv(buf: ByteArray, offset: Int, length: Int) {
        if (length == 0) return
        buf.usePinned {
            if (mode == CipherMode.CTR) {
                gcry_cipher_setctr(handle, it.addressOf(offset), length.convert())
            } else {
                gcry_cipher_setiv(handle, it.addressOf(offset), length.convert())
            }
        }
    }

    override fun encrypt(
        output: ByteArray,
        outputOffset: Int,
        outputLength: Int,
        input: ByteArray,
        inputOffset: Int,
        inputLength: Int
    ) {
        if (inputLength == 0) return
        output.usePinned { outputPin ->
            input.usePinned { inputPin ->
                gcry_cipher_encrypt(
                    handle,
                    outputPin.addressOf(outputOffset),
                    outputLength.convert(),
                    inputPin.addressOf(inputOffset),
                    inputLength.convert()
                )
            }
        }
    }

    override fun decrypt(
        output: ByteArray,
        outputOffset: Int,
        outputLength: Int,
        input: ByteArray,
        inputOffset: Int,
        inputLength: Int
    ) {
        if (inputLength == 0) return
        output.usePinned { outputPin ->
            input.usePinned { inputPin ->
                gcry_cipher_decrypt(
                    handle,
                    outputPin.addressOf(outputOffset),
                    outputLength.convert(),
                    inputPin.addressOf(inputOffset),
                    inputLength.convert()
                )
            }
        }
    }

    override fun close() {
        gcry_cipher_close(handle)
    }
}
