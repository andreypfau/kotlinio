package com.github.andreypfau.kotlinio.packet.tcp

import com.github.andreypfau.kotlinio.packet.ethernet.EthernetPacket
import com.github.andreypfau.kotlinio.packet.ethernet.header.EthernetHeader
import com.github.andreypfau.kotlinio.packet.ip.IpPacket
import com.github.andreypfau.kotlinio.utils.hex

fun main() {
    val hex = hex("000000024500004000004000400688300a08000111f89687ca1e01bbdf1f178d00000000b0c2ffff880b0000020405b4010303060101080a3d6ffbb20000000004020000")
    repeat(100) {
        println("offset: $it")
        try {
            val ip = IpPacket.newInstance(hex, it)
            println(ip)
            return
        } catch(e: Exception) {
        }
    }
}
