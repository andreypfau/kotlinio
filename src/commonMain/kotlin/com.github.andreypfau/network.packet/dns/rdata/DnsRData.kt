package com.github.andreypfau.network.packet.dns.rdata

interface DnsRData {
    val length: Int

    fun toByteArray(): ByteArray = toByteArray(ByteArray(length))
    fun toByteArray(buf: ByteArray, offset: Int = 0): ByteArray
}
