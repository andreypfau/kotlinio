package com.github.andreypfau.network.packet.dns.rdata

interface DnsRData {
    val length: Int

    fun getByteArray(): ByteArray = getByteArray(ByteArray(length))
    fun getByteArray(buf: ByteArray, offset: Int = 0): ByteArray
}
