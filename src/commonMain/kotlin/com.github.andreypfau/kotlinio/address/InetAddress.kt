package com.github.andreypfau.kotlinio.address

interface InetAddress {
    val size: Int
    fun toByteArray(): ByteArray
    fun toByteArray(destination: ByteArray, offset: Int = 0): ByteArray
}
