package com.github.andreypfau.kotlinio.crypto.cipher

import com.github.andreypfau.hex.encodeHex
import com.github.andreypfau.kotlinio.pool.DirectAllocationCloseablePool
import com.github.andreypfau.kotlinio.pool.useInstance

class Aes128 private constructor(mode: CipherMode) : JvmCipher("AES", mode) {
    companion object {
        val ECB = DirectAllocationCloseablePool { Aes128(CipherMode.ECB) }
        val CFB = DirectAllocationCloseablePool { Aes128(CipherMode.CFB) }
        val OFB = DirectAllocationCloseablePool { Aes128(CipherMode.OFB) }
        val CTR = DirectAllocationCloseablePool { Aes128(CipherMode.CTR) }
        val GCM = DirectAllocationCloseablePool { Aes128(CipherMode.GCM) }
    }
}

fun main() {
    Aes128.CFB.useInstance {
        it.setKey(ByteArray(16) { 33 })
        it.setIv(ByteArray(16) { 11 })
        println(it.encrypt(ByteArray(16)).encodeHex())
        println(it.encrypt(ByteArray(32)).encodeHex())
        println(it.encrypt(ByteArray(8)).encodeHex())
    }
}
