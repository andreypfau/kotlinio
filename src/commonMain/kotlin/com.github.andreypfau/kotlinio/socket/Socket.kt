package com.github.andreypfau.kotlinio.socket

import com.github.andreypfau.kotlinio.address.Inet4Address
import com.github.andreypfau.kotlinio.address.InetAddress
import com.github.andreypfau.kotlinio.address.InetSocketAddress
import com.github.andreypfau.kotlinio.packet.ip.IpVersion
import com.github.andreypfau.kotlinio.pool.Closeable

expect class Socket(fd: Int) : Closeable {
    constructor(addressFamily: Int, type: Int, protocol: Int)

    val fd: Int
    val address: InetSocketAddress

    fun bind(address: InetAddress = Inet4Address(), port: UShort = 0u)
    fun connect(address: InetAddress, port: UShort)
    fun accept(): Socket?
    fun receive(buffer: ByteArray, offset: Int = 0): Pair<Int, InetSocketAddress>?
    fun read(buffer: ByteArray, offset: Int = 0): Int
    fun write(buffer: ByteArray, offset: Int = 0): Int

    companion object {
        fun udp(version: IpVersion = IpVersion.IPv4): Socket
        fun tcp(version: IpVersion = IpVersion.IPv4): Socket
        fun icmp(version: IpVersion = IpVersion.IPv4): Socket
        fun ip(version: IpVersion = IpVersion.IPv4): Socket
    }
}
