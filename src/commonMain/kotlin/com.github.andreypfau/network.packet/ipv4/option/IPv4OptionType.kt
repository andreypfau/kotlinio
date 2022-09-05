package com.github.andreypfau.network.packet.ipv4.option

import com.github.andreypfau.network.utils.NamedValue

class IPv4OptionType private constructor(
    override val value: Byte,
    override val name: String
) : NamedValue<Byte, IPv4OptionType>() {
    companion object {
        private val registry = HashMap<Byte, IPv4OptionType>()

        val END_OF_OPTION_LIST = set(0, "End of Option List")

        // TODO: All IPv4 Option Types

        operator fun get(value: Byte): IPv4OptionType = registry[value] ?: IPv4OptionType(value, "unknown")
        operator fun set(value: Byte, name: String): IPv4OptionType {
            val version = IPv4OptionType(value, name)
            registry[value] = version
            return version
        }
    }
}