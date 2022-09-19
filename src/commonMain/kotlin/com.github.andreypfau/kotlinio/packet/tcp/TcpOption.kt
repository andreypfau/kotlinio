package com.github.andreypfau.kotlinio.packet.tcp

interface TcpOption {
    val kind: UByte
    val length: Int
    val rawData: ByteArray
}
