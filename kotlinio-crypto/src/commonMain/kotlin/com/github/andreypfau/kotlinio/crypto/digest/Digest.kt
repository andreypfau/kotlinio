package com.github.andreypfau.kotlinio.crypto.digest

import com.github.andreypfau.kotlinio.pool.Closeable
import com.github.andreypfau.kotlinio.pool.DirectAllocationPool

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

internal fun interface DigestPool<T : Digest> : DirectAllocationPool<T> {
    override fun borrow(): T
    override fun recycle(instance: T) = instance.close()
}

expect class Sha1 private constructor() : Digest
expect class Sha256 private constructor() : Digest
expect class Sha512 private constructor() : Digest
expect class Md5 private constructor() : Digest
