package com.github.andreypfau.kotlinio.socket

import com.github.andreypfau.kotlinio.address.Inet4Address
import com.github.andreypfau.kotlinio.address.InetAddress
import com.github.andreypfau.kotlinio.address.InetSocketAddress
import com.github.andreypfau.kotlinio.packet.ip.IpVersion
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

expect class Socket(fd: Int) {
    constructor(addressFamily: Int, type: Int, protocol: Int)

    val fd: Int
    val address: InetSocketAddress

    fun bind(address: InetAddress = Inet4Address(), port: UShort = 0u)
    fun connect(address: InetAddress, port: UShort)
    fun accept(): Socket?
    fun receive(buffer: ByteArray, offset: Int = 0): Pair<Int, InetSocketAddress>?
    fun read(buffer: ByteArray, offset: Int = 0): Int
    fun write(buffer: ByteArray, offset: Int = 0): Int
    fun close()

    companion object {
        fun udp(version: IpVersion = IpVersion.IPv4): Socket
        fun tcp(version: IpVersion = IpVersion.IPv4): Socket
        fun icmp(version: IpVersion = IpVersion.IPv4): Socket
        fun ip(version: IpVersion = IpVersion.IPv4): Socket
    }
}

fun Socket.readFlow(bufferSize: Int = 1500): Flow<ByteArray> = flow {
    val buffer = ByteArray(bufferSize)
    while (currentCoroutineContext().isActive) {
        val size = read(buffer)
        if (size > 0) {
            emit(buffer.copyOf(size))
        } else {
            delay(1) // yield() not work on native :(
        }
    }
}

fun Socket.acceptFlow(): Flow<Socket> = flow {
    while (currentCoroutineContext().isActive) {
        val socket = accept()
        if (socket != null) {
            emit(socket)
        } else {
            delay(1) // yield() not work on native :(
        }
    }
}

fun Socket.receiveFlow(bufferSize: Int = 1500): Flow<Pair<ByteArray, InetSocketAddress>> = flow {
    val buffer = ByteArray(bufferSize)
    while (currentCoroutineContext().isActive) {
        val receive = receive(buffer)
        if (receive != null) {
            val (size, address) = receive
            emit(buffer.copyOf(size) to address)
        } else {
            delay(1) // yield() not work on native :(
        }
    }
}
