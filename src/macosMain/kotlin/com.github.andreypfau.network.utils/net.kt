package com.github.andreypfau.network.utils

import com.github.andreypfau.network.address.Inet4Address
import com.github.andreypfau.network.address.InetAddress
import com.github.andreypfau.network.address.InetSocketAddress
import kotlinx.cinterop.*
import platform.posix.*

internal fun MemScope.sockaddr(address: InetAddress, port: UShort): Pair<CPointer<sockaddr>, UInt> {
    return when (address) {
        is Inet4Address -> {
            val sockaddr = alloc<sockaddr_in> {
                sin_family = AF_INET.toUByte()
                sin_addr.s_addr = address.toByteArray().reversedArray().getUInt(0)
                sin_port = posix_htons(port.convert()).convert()
            }.ptr.reinterpret<sockaddr>()
            val sockaddrLen = sockaddr_in.size.toUInt()
            sockaddr to sockaddrLen
        }

        else -> error("Unsupported address: $address")
    }
}

internal fun <T> MemScope.inetSocketAddress(block: (CPointer<sockaddr>, CValuesRef<socklen_tVar>) -> T): Pair<T, InetSocketAddress?> {
    val addr = alloc<sockaddr>()
    val addrLen = alloc<socklen_tVar> {
        value = sockaddr.size.convert()
    }
    val result = block(addr.ptr, addrLen.ptr)
    if (result == -1 || result == -1L) {
        return result to null
    }
    val address = if (addr.reinterpret<sockaddr_in>().sin_family.toInt() == AF_INET) {
        val sockaddr4 = addr.reinterpret<sockaddr_in>()
        Inet4Address(sockaddr4.sin_addr.s_addr.toInt()) to posix_htons(sockaddr4.sin_port.convert()).toUShort()
    } else {
        error("Unsupported address: $addr")
    }
    return result to address
}
