package com.github.andreypfau.network.socket

import com.github.andreypfau.network.address.Inet4Address
import com.github.andreypfau.network.address.InetAddress
import com.github.andreypfau.network.address.InetSocketAddress
import com.github.andreypfau.network.packet.ip.IpVersion

actual class Socket actual constructor(fd: Int) {
    init {
        TODO("Not yet implemented")
    }

    actual constructor(addressFamily: Int, type: Int, protocol: Int) : this(0) {
    }

    actual val fd: Int
        get() = 0

    actual val address: Pair<InetAddress, UShort>
        get() = Inet4Address() to 0u

    actual fun read(buffer: ByteArray, offset: Int): Int = 0

    actual fun write(buffer: ByteArray, offset: Int): Int = 0

    actual fun close() {
    }

    actual fun bind(address: InetAddress, port: UShort) {
    }

    actual fun connect(address: InetAddress, port: UShort) {
    }

    actual fun accept(): Socket? = null

    actual fun receive(buffer: ByteArray, offset: Int): Pair<Int, InetSocketAddress>? = null

    actual companion object {
        actual fun udp(version: IpVersion): Socket {
            TODO("Not yet implemented")
        }

        actual fun tcp(version: IpVersion): Socket {
            TODO("Not yet implemented")
        }

        actual fun icmp(version: IpVersion): Socket {
            TODO("Not yet implemented")
        }

        actual fun ip(version: IpVersion): Socket {
            TODO("Not yet implemented")
        }
    }
}