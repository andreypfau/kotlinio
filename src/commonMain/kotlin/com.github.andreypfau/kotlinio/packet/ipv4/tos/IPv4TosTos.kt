package com.github.andreypfau.kotlinio.packet.ipv4.tos

import com.github.andreypfau.kotlinio.utils.NamedValue

class IPv4TosTos private constructor(
    override val value: Byte,
    override val name: String
) : NamedValue<Byte, IPv4TosTos>() {
    override fun compareTo(other: IPv4TosTos): Int = value.compareTo(other.value)

    companion object {
        private val registry = HashMap<Byte, IPv4TosTos>()

        val DEFAULT = set(0x0, "Default")
        val MINIMIZE_MONETARY_COST = set(0x1, "Minimize Monetary Cost")
        val MAXIMIZE_RELIABILITY = set(0x2, "Maximize Reliability")
        val MAXIMIZE_THROUGHPUT = set(0x4, "Maximize Throughput")
        val MINIMIZE_DELAY = set(0x8, "Minimize Delay")
        val MAXIMIZE_SECURITY = set(0xF, "Maximize Security")

        operator fun get(value: Byte): IPv4TosTos = registry[value] ?: IPv4TosTos(value, "unknown")
        operator fun set(value: Byte, name: String): IPv4TosTos {
            val version = IPv4TosTos(value, name)
            registry[value] = version
            return version
        }
    }
}
