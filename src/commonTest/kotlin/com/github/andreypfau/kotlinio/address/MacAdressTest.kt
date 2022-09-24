package com.github.andreypfau.kotlinio.address

import com.github.andreypfau.kotlinio.packet.ethernet.EthernetPacket
import com.github.andreypfau.kotlinio.utils.hex
import kotlin.test.Test

class MacAdressTest {
    @Test
    fun test() {
        val hex =
            "84144d07864a 0a14bd9200dd 0800 450000930db300006b11363308080808c0a83abc0035a911007fbf3eb1fd8180000100000001000112636f6e6e65637469766974792d636865636b067562756e747503636f6d00001c0001c01f000600010000054c0031036e73310963616e6f6e6963616cc0260a686f73746d6173746572c03f7849168c00002a3000000e1000093a8000000e100000290200000000000000"

        val data = hex(hex.replace(" ", ""))
        val header = EthernetPacket(data)
        println(header)
    }
}
