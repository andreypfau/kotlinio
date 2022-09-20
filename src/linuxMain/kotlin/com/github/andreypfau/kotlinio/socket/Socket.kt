package com.github.andreypfau.kotlinio.socket

import com.github.andreypfau.kotlinio.address.InetAddress
import com.github.andreypfau.kotlinio.address.InetSocketAddress
import com.github.andreypfau.kotlinio.packet.ip.IpVersion
import com.github.andreypfau.kotlinio.utils.check
import com.github.andreypfau.kotlinio.utils.inetSocketAddress
import com.github.andreypfau.kotlinio.utils.sockaddr
import kotlinx.cinterop.*
import platform.posix.*

actual class Socket actual constructor(
    actual val fd: Int
) {
    actual constructor(
        addressFamily: Int,
        type: Int,
        protocol: Int
    ) : this(
        socket(addressFamily, type, protocol).check()
    )

    init {
        memScoped {
            val timeout = alloc<timeval> {
                tv_sec = 0
                tv_usec = 10
            }
            setsockopt(fd, SOL_SOCKET, SO_RCVTIMEO, timeout.ptr, timeval.size.convert())
        }
    }

    actual val address: InetSocketAddress
        get() = memScoped {
            inetSocketAddress { addr, addrLen ->
                getsockname(fd, addr, addrLen).check()
            }.second ?: error("Can't get socket address")
        }

    actual fun read(buffer: ByteArray, offset: Int): Int {
        val result = buffer.usePinned {
            read(
                fd,
                it.addressOf(offset),
                (buffer.size - offset).convert()
            )
        }
        return result.toInt()
    }

    actual fun write(
        buffer: ByteArray,
        offset: Int
    ): Int {
        val result = buffer.usePinned {
            write(
                fd,
                it.addressOf(offset),
                (buffer.size - offset).convert(),
            )
        }
        return result.toInt()
    }

    actual fun bind(address: InetAddress, port: UShort) {
        memScoped {
            val (sockaddr, sockaddrLen) = sockaddr(address, port)
            bind(fd, sockaddr, sockaddrLen).check()
        }
    }

    actual fun connect(address: InetAddress, port: UShort) {
        memScoped {
            val (sockaddr, sockaddrLen) = sockaddr(address, port)
            connect(fd, sockaddr, sockaddrLen).check()
        }
    }

    actual fun accept(): Socket? {
        memScoped {
            val (fd, address) = inetSocketAddress { cPointer, cValuesRef ->
                accept(fd, cPointer, cValuesRef)
            }
            if (fd > 0) {
                return Socket(fd)
            } else {
                return null
            }
        }
    }

    actual fun receive(buffer: ByteArray, offset: Int): Pair<Int, InetSocketAddress>? {
        memScoped {
            buffer.usePinned {
                val (result, address) = inetSocketAddress { addr, addrLen ->
                    recvfrom(fd, it.addressOf(offset), (buffer.size - offset).convert(), 0, addr, addrLen)
                }
                if (result > 0 && address != null) {
                    return result.toInt() to address
                } else {
                    return null
                }
            }
        }
    }

    actual fun close() {
        close(fd)
    }

    actual companion object {
        actual fun udp(version: IpVersion): Socket =
            when (version) {
                IpVersion.IPv4 -> Socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP)
                IpVersion.IPv6 -> Socket(AF_INET6, SOCK_DGRAM, IPPROTO_UDP)
            }

        actual fun tcp(version: IpVersion): Socket =
            when (version) {
                IpVersion.IPv4 -> Socket(AF_INET, SOCK_STREAM, IPPROTO_TCP)
                IpVersion.IPv6 -> Socket(AF_INET6, SOCK_STREAM, IPPROTO_TCP)
            }

        actual fun icmp(version: IpVersion): Socket =
            when (version) {
                IpVersion.IPv4 -> Socket(AF_INET, SOCK_RAW, IPPROTO_ICMP)
                IpVersion.IPv6 -> Socket(AF_INET6, SOCK_RAW, IPPROTO_ICMPV6)
            }

        actual fun ip(version: IpVersion): Socket =
            when (version) {
                IpVersion.IPv4 -> Socket(AF_INET, SOCK_RAW, IPPROTO_RAW)
                IpVersion.IPv6 -> Socket(AF_INET6, SOCK_RAW, IPPROTO_RAW)
            }
    }
}
