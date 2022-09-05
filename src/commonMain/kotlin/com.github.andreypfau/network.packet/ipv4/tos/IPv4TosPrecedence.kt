package com.github.andreypfau.network.packet.ipv4.tos

import com.github.andreypfau.network.utils.NamedValue

class IPv4TosPrecedence private constructor(
    override val value: Byte,
    override val name: String
) : NamedValue<Byte, IPv4TosPrecedence>() {
    override fun compareTo(other: IPv4TosPrecedence): Int = value.compareTo(other.value)

    companion object {
        private val registry = HashMap<Byte, IPv4TosPrecedence>()

        val ROUTINE = set(0, "Routine")
        val PRIORITY = set(1, "Priority")
        val IMMEDIATE = set(2, "Immediate")
        val FLASH = set(3, "Flash")
        val FLASH_OVERRIDE = set(4, "Flash Override")
        val CRITIC_ECP = set(5, "CRITIC/ECP")
        val INTERNETWORK_CONTROL = set(6, "Internetwork Control/ECP")
        val NETWORK_CONTROL = set(7, "Network Control")

        operator fun get(value: Byte): IPv4TosPrecedence = registry[value] ?: IPv4TosPrecedence(value, "unknown")
        operator fun set(value: Byte, name: String): IPv4TosPrecedence {
            val version = IPv4TosPrecedence(value, name)
            registry[value] = version
            return version
        }
    }
}