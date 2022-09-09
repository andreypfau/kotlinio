package com.github.andreypfau.network.address

typealias InetSocketAddress = Pair<InetAddress, UShort>

val InetSocketAddress.address: InetAddress get() = first
val InetSocketAddress.port: UShort get() = second