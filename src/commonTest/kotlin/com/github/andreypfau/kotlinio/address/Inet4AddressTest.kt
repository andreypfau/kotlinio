package com.github.andreypfau.kotlinio.address

import kotlin.test.Test

class Inet4AddressTest {
    @Test
    fun test() {
        val address = Inet4Address("65.21.101.233")
        println(address)
    }
}
