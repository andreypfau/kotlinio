package com.github.andreypfau.kotlinio.crypto.digest

import com.github.andreypfau.kotlinio.pool.DirectAllocationCloseablePool
import com.github.andreypfau.kotlinio.pool.ObjectPool
import com.github.andreypfau.kotlinio.pool.useInstance
import gcrypt.*
import kotlinx.cinterop.*
import platform.posix.memcpy

abstract class GcryptDigest(
    private val algorithm: Int
) : Digest {
    private val handle = memScoped {
        val handlePtr = allocPointerTo<gcry_md_handle>()
        gcry_md_open(handlePtr.ptr, algorithm, 0)
        requireNotNull(handlePtr.value)
    }

    override fun update(byteArray: ByteArray, offset: Int, length: Int) {
        if (length == 0) return
        byteArray.usePinned {
            gcry_md_write(handle, it.addressOf(offset), length.convert())
        }
    }

    override fun digest(): ByteArray = digest(
        ByteArray(gcry_md_get_algo_dlen(algorithm).convert())
    )

    override fun digest(byteArray: ByteArray, offset: Int, length: Int): ByteArray {
        if (length == 0) return byteArray
        byteArray.usePinned {
            val output = gcry_md_read(handle, algorithm)
            memcpy(it.addressOf(offset), output, length.convert())
        }
        gcry_md_reset(handle)
        return byteArray
    }

    override fun close() {
        gcry_md_close(handle)
    }
}

actual class Sha1 : GcryptDigest(GCRY_MD_SHA1.convert()), Digest {
    actual companion object {
        actual val POOL: ObjectPool<Sha1> = DirectAllocationCloseablePool { Sha1() }

        actual fun hash(byteArray: ByteArray): ByteArray = POOL.useInstance {
            it.update(byteArray)
            it.digest()
        }
    }
}

actual class Sha256 : GcryptDigest(GCRY_MD_SHA256.convert()), Digest {
    actual companion object {
        actual val POOL: ObjectPool<Sha256> = DirectAllocationCloseablePool { Sha256() }

        actual fun hash(byteArray: ByteArray): ByteArray = POOL.useInstance {
            it.update(byteArray)
            it.digest()
        }
    }
}

actual class Sha512 : GcryptDigest(GCRY_MD_SHA512.convert()), Digest {
    actual companion object {
        actual val POOL: ObjectPool<Sha512> = DirectAllocationCloseablePool { Sha512() }

        actual fun hash(byteArray: ByteArray): ByteArray = POOL.useInstance {
            it.update(byteArray)
            it.digest()
        }
    }
}

actual class Md5 : GcryptDigest(GCRY_MD_MD5.convert()), Digest {
    actual companion object {
        actual val POOL: ObjectPool<Md5> = DirectAllocationCloseablePool { Md5() }

        actual fun hash(byteArray: ByteArray): ByteArray = POOL.useInstance {
            it.update(byteArray)
            it.digest()
        }
    }
}
