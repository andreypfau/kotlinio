package com.github.andreypfau.kotlinio.crypto.digest

import com.github.andreypfau.kotlinio.pool.DirectAllocationCloseablePool
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
        byteArray.usePinned {
            gcry_md_write(handle, it.addressOf(offset), length.convert())
        }
    }

    override fun digest(): ByteArray = digest(
        ByteArray(gcry_md_get_algo_dlen(algorithm).convert())
    )

    override fun digest(byteArray: ByteArray, offset: Int, length: Int): ByteArray {
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
    companion object {
        val POOL = DirectAllocationCloseablePool { Sha1() }
    }
}

actual class Sha256 : GcryptDigest(GCRY_MD_SHA256.convert()), Digest {
    companion object {
        val POOL = DirectAllocationCloseablePool { Sha256() }
    }
}

actual class Sha512 : GcryptDigest(GCRY_MD_SHA512.convert()), Digest {
    companion object {
        val POOL = DirectAllocationCloseablePool { Sha512() }
    }
}

actual class Md5 : GcryptDigest(GCRY_MD_MD5.convert()), Digest {
    companion object {
        val POOL = DirectAllocationCloseablePool { Md5() }
    }
}
