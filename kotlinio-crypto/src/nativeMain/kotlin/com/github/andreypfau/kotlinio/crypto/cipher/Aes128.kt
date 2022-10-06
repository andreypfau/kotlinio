package com.github.andreypfau.kotlinio.crypto.cipher

import com.github.andreypfau.kotlinio.pool.DirectAllocationCloseablePool
import gcrypt.GCRY_CIPHER_AES128

class Aes128(
    mode: CipherMode
) : GcryptCipher(GCRY_CIPHER_AES128, mode) {
    companion object {
        val ECB = DirectAllocationCloseablePool { Aes128(CipherMode.ECB) }
        val CFB = DirectAllocationCloseablePool { Aes128(CipherMode.CFB) }
        val OFB = DirectAllocationCloseablePool { Aes128(CipherMode.OFB) }
        val CTR = DirectAllocationCloseablePool { Aes128(CipherMode.CTR) }
        val GCM = DirectAllocationCloseablePool { Aes128(CipherMode.GCM) }
    }
}
