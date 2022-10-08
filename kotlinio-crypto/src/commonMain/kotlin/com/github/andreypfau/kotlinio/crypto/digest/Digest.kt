package com.github.andreypfau.kotlinio.crypto.digest

import com.github.andreypfau.kotlinio.pool.Closeable
import com.github.andreypfau.kotlinio.pool.ObjectPool

interface Digest : Closeable {
    fun update(byteArray: ByteArray, offset: Int = 0, length: Int = byteArray.size - offset)

    fun digest(): ByteArray
    fun digest(byteArray: ByteArray, offset: Int = 0, length: Int = byteArray.size - offset): ByteArray

    override fun close()
}

operator fun <T : Digest> T.plusAssign(buf: ByteArray) {
    update(buf)
}

operator fun <T : Digest> T.plus(buf: ByteArray): T = apply {
    update(buf)
}

expect class Sha1 private constructor() : Digest {
    companion object {
        val POOL: ObjectPool<Sha1>

        fun hash(byteArray: ByteArray): ByteArray
    }
}

expect class Sha256 private constructor() : Digest {
    companion object {
        val POOL: ObjectPool<Sha256>

        fun hash(byteArray: ByteArray): ByteArray
    }
}

expect class Sha512 private constructor() : Digest {
    companion object {
        val POOL: ObjectPool<Sha512>

        fun hash(byteArray: ByteArray): ByteArray
    }
}

expect class Md5 private constructor() : Digest {
    companion object {
        val POOL: ObjectPool<Md5>

        fun hash(byteArray: ByteArray): ByteArray
    }
}
