package com.github.andreypfau.kotlinio.packet.ipv4

import com.github.andreypfau.kotlinio.packet.ethernet.EthernetPacket
import com.github.andreypfau.kotlinio.utils.hex
import kotlin.test.Test

class IpV4PacketTest {
    @Test
    fun test() {
        val dump =
            hex("0a14bd9200dd84144d07864a080045000050abca00004011c35ec0a83abc08080808d5460035003c0bc2a4d201000001000000000001097265736f7572636573096a6574627261696e7303636f6d000001000100002905c0000000000000")
        val ether = EthernetPacket(dump)
        println(ether)
        println("=============")
        val ether2 = ether.builder().build()
        println(ether2)
    }
}
