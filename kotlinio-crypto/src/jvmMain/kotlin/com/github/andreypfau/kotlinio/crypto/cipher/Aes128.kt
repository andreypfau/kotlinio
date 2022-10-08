package com.github.andreypfau.kotlinio.crypto.cipher

import com.github.andreypfau.kotlinio.pool.DirectAllocationCloseablePool
import com.github.andreypfau.kotlinio.pool.ObjectPool

actual class Aes128 private constructor(mode: CipherMode) : Cipher, JvmCipher("AES", mode) {
    actual companion object {
        actual val ECB: ObjectPool<Aes128> = DirectAllocationCloseablePool { Aes128(CipherMode.ECB) }
        actual val CFB: ObjectPool<Aes128> = DirectAllocationCloseablePool { Aes128(CipherMode.CFB) }
        actual val OFB: ObjectPool<Aes128> = DirectAllocationCloseablePool { Aes128(CipherMode.OFB) }
        actual val CTR: ObjectPool<Aes128> = DirectAllocationCloseablePool { Aes128(CipherMode.CTR) }
        actual val GCM: ObjectPool<Aes128> = DirectAllocationCloseablePool { Aes128(CipherMode.GCM) }
    }
}
