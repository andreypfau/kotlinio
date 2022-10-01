package com.github.andreypfau.kotlinio.socket

import com.github.andreypfau.kotlinio.address.InetAddress
import com.github.andreypfau.kotlinio.address.InetSocketAddress
import com.github.andreypfau.kotlinio.packet.ip.IpVersion
import com.github.andreypfau.kotlinio.pool.Closeable
import com.github.andreypfau.kotlinio.utils.check
import com.github.andreypfau.kotlinio.utils.inetSocketAddress
import com.github.andreypfau.kotlinio.utils.sockaddr
import kotlinx.cinterop.*
import platform.posix.*

actual class Socket actual constructor(
    actual val fd: Int
) : Closeable {
    actual constructor(addressFamily: Int, type: Int, protocol: Int) : this(
        socket(addressFamily, type, protocol).toInt().check()
    )

    init {
        memScoped {
            val timeout = alloc<timeval> {
                tv_sec = 0
                tv_usec = 10
            }
            val timeoutSize = sizeOf<timeval>()
            setsockopt(
                fd.convert(),
                SOL_SOCKET,
                SO_RCVTIMEO,
                timeout.ptr.readBytes(timeoutSize.convert()).toKString(),
                timeoutSize.convert()
            )
        }
    }

    actual val address: Pair<InetAddress, UShort>
        get() = memScoped {
            inetSocketAddress { addr, addrLen ->
                getsockname(fd.convert(), addr, addrLen).check()
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
        return result
    }

    actual fun write(buffer: ByteArray, offset: Int): Int {
        val result = buffer.usePinned {
            write(
                fd,
                it.addressOf(offset),
                (buffer.size - offset).convert(),
            )
        }
        return result
    }

    override fun close() {
        close(fd)
    }

    actual fun bind(address: InetAddress, port: UShort) {
        memScoped {
            val (sockaddr, sockaddrLen) = sockaddr(address, port)
            bind(fd.convert(), sockaddr, sockaddrLen).check()
        }
    }

    actual fun connect(address: InetAddress, port: UShort) {
        memScoped {
            val (sockaddr, sockaddrLen) = sockaddr(address, port)
            connect(fd.convert(), sockaddr, sockaddrLen).check()
        }
    }

    actual fun accept(): Socket? {
        memScoped {
            val (fd, address) = inetSocketAddress { cPointer, cValuesRef ->
                accept(fd.convert(), cPointer, cValuesRef)
            }
            if (fd > 0u) {
                return Socket(fd.convert())
            } else {
                return null
            }
        }
    }

    actual fun receive(buffer: ByteArray, offset: Int): Pair<Int, InetSocketAddress>? {
        memScoped {
            buffer.usePinned {
                val (result, address) = inetSocketAddress { addr, addrLen ->
                    recvfrom(fd.convert(), it.addressOf(offset), (buffer.size - offset).convert(), 0, addr, addrLen)
                }
                if (result > 0 && address != null) {
                    return result to address
                } else {
                    return null
                }
            }
        }
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
