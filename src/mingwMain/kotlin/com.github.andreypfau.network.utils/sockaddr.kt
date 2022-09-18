package com.github.andreypfau.network.utils

import com.github.andreypfau.network.address.Inet4Address
import com.github.andreypfau.network.address.InetAddress
import com.github.andreypfau.network.address.InetSocketAddress
import kotlinx.cinterop.*
import platform.posix.AF_INET
import platform.posix.sockaddr
import platform.posix.sockaddr_in
import platform.windows.htons

internal fun MemScope.sockaddr(address: InetAddress, port: UShort): Pair<CPointer<sockaddr>, Int> {
    return when (address) {
        is Inet4Address -> {
            val sockaddr = alloc<sockaddr_in> {
                sin_family = AF_INET.toShort()
                sin_addr.S_un.S_addr = address.value.toUInt()
                sin_port = htons(port)
            }.ptr.reinterpret<sockaddr>()
            val sockaddrLen = sizeOf<sockaddr_in>().toInt()
            sockaddr to sockaddrLen
        }

        else -> error("Unsupported address: $address")
    }
}

internal fun <T> MemScope.inetSocketAddress(block: (CPointer<sockaddr>, CValuesRef<IntVar>) -> T): Pair<T, InetSocketAddress?> {
    val addr = alloc<sockaddr>()
    val addrLen = alloc<IntVar> {
        value = sizeOf<sockaddr>().convert()
    }
    val result = block(addr.ptr, addrLen.ptr)
    if (result == -1 || result == -1L) {
        return result to null
    }
    val address = if (addr.reinterpret<sockaddr_in>().sin_family.toInt() == AF_INET) {
        val sockaddr4 = addr.reinterpret<sockaddr_in>()
        Inet4Address(sockaddr4.sin_addr.S_un.S_addr.toInt()) to htons(sockaddr4.sin_port)
    } else {
        error("Unsupported address: $addr")
    }
    return result to address
}
