package com.github.andreypfau.kotlinio.crypto.digest

import com.github.andreypfau.kotlinio.pool.DirectAllocationCloseablePool
import com.github.andreypfau.kotlinio.pool.DirectAllocationPool
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
    companion object {
        val POOL: DirectAllocationPool<Sha1> = DirectAllocationCloseablePool { Sha1() }
    }
}

actual class Sha256 : JvmDigest(MessageDigest.getInstance("SHA-256")), Digest {
    companion object {
        val POOL: DirectAllocationPool<Sha256> = DirectAllocationCloseablePool { Sha256() }
    }
}

actual class Sha512 : JvmDigest(MessageDigest.getInstance("SHA-512")), Digest {
    companion object {
        val POOL: DirectAllocationPool<Sha512> = DirectAllocationCloseablePool { Sha512() }
    }
}

actual class Md5 : JvmDigest(MessageDigest.getInstance("MD5")), Digest {
    companion object {
        val POOL: DirectAllocationPool<Md5> = DirectAllocationCloseablePool { Md5() }
    }
}
