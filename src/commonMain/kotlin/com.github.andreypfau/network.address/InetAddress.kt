package com.github.andreypfau.network.address

interface InetAddress {
    val size: Int
    fun toByteArray(): ByteArray
    fun toByteArray(destination: ByteArray, offset: Int = 0): ByteArray
}