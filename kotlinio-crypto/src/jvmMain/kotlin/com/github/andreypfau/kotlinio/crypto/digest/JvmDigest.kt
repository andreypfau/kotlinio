package com.github.andreypfau.kotlinio.crypto.digest

import com.github.andreypfau.kotlinio.pool.DirectAllocationCloseablePool
import com.github.andreypfau.kotlinio.pool.ObjectPool
import com.github.andreypfau.kotlinio.pool.useInstance
import java.security.MessageDigest

abstract class JvmDigest internal constructor(
    private val digest: MessageDigest
) : Digest {
    override fun update(byteArray: ByteArray, offset: Int, length: Int) {
        digest.update(byteArray, offset, length)
    }

    override fun digest(): ByteArray = digest(ByteArray(digest.digestLength))

    override fun digest(byteArray: ByteArray, offset: Int, length: Int): ByteArray {
        digest.digest(byteArray, offset, length)
        return byteArray
    }

    override fun close() {
        digest.reset()
    }
}

actual class Sha1 : JvmDigest(MessageDigest.getInstance("SHA-1")), Digest {
    actual companion object {
        actual val POOL: ObjectPool<Sha1> = DirectAllocationCloseablePool { Sha1() }

        actual fun hash(byteArray: ByteArray): ByteArray = POOL.useInstance {
            it.update(byteArray)
            it.digest()
        }
    }
}

actual class Sha256 : JvmDigest(MessageDigest.getInstance("SHA-256")), Digest {
    actual companion object {
        actual val POOL: ObjectPool<Sha256> = DirectAllocationCloseablePool { Sha256() }

        actual fun hash(byteArray: ByteArray): ByteArray = POOL.useInstance {
            it.update(byteArray)
            it.digest()
        }
    }
}

actual class Sha512 : JvmDigest(MessageDigest.getInstance("SHA-512")), Digest {
    actual companion object {
        actual val POOL: ObjectPool<Sha512> = DirectAllocationCloseablePool { Sha512() }

        actual fun hash(byteArray: ByteArray): ByteArray = POOL.useInstance {
            it.update(byteArray)
            it.digest()
        }
    }
}

actual class Md5 : JvmDigest(MessageDigest.getInstance("MD5")), Digest {
    actual companion object {
        actual val POOL: ObjectPool<Md5> = DirectAllocationCloseablePool { Md5() }

        actual fun hash(byteArray: ByteArray): ByteArray = POOL.useInstance {
            it.update(byteArray)
            it.digest()
        }
    }
}
