package com.github.andreypfau.network.packet.tcp

interface TcpOption {
    val kind: UByte
    val length: Int
    val rawData: ByteArray
}