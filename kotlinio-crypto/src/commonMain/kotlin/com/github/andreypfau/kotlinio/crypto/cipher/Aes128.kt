package com.github.andreypfau.kotlinio.crypto.cipher

import com.github.andreypfau.kotlinio.pool.ObjectPool

expect class Aes128 : Cipher {
    override val mode: CipherMode

    companion object {
        val ECB: ObjectPool<Aes128>
        val CFB: ObjectPool<Aes128>
        val OFB: ObjectPool<Aes128>
        val CTR: ObjectPool<Aes128>
        val GCM: ObjectPool<Aes128>
    }
}
