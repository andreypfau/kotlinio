package com.github.andreypfau.kotlinio.packet.ipv4.option

import com.github.andreypfau.kotlinio.packet.ipv4.IPv4Option
import com.github.andreypfau.kotlinio.utils.NamedValue

class IPv4OptionType private constructor(
    override val value: UByte,
    override val name: String,
    private val optionFactory: (ByteArray, Int, Int) -> IPv4Option = ::UnknownIpV4Option
) : NamedValue<UByte, IPv4OptionType>() {

    operator fun invoke(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset): IPv4Option =
        optionFactory(rawData, offset, length)

    companion object {
        private val registry = HashMap<UByte, IPv4OptionType>()

        val END_OF_OPTION_LIST = set(0u, "End of Option List") { _, _, _ ->
            IpV4EndOfOptionList
        }
        val NO_OPERATION = set(1u, "No Operation") { _, _, _ ->
            IpV4NoOperationOption
        }
        val SECURITY = set(130u, "Security")
        val LOOSE_SOURCE_ROUTING = set(131u, "Loose Source Routing")
        val INTERNET_TIMESTAMP = set(68u, "Internet Timestamp")
        val EXTENDED_SECURITY = set(133u, "Extended Security")
        val CIPSO = set(134u, "CIPSO")
        val RECORD_ROUTE = set(7u, "Record Route")
        val STREAM_ID = set(136u, "Stream ID")
        val STRICT_SOURCE_ROUTING = set(137u, "Strict Source Routing")
        val ZSU = set(10u, "ZSU")
        val MTUP = set(11u, "MTUP")
        val MTUR = set(12u, "MTUR")
        val FINN = set(205u, "FINN")
        val VISA = set(142u, "VISA")
        val ENCODE = set(15u, "ENCODE")
        val IMITD = set(144u, "IMITD")
        val EIP = set(145u, "EIP")
        val TRACEROUTE = set(82u, "Traceroute")
        val ADDRESS_EXTENSION = set(147u, "Address Extension")
        val ROUTER_ALERT = set(148u, "Router Alert")
        val SELECTIVE_DIRECTED_BROADCAST = set(149u, "Selective Directed Broadcast")
        val DYNAMIC_PACKET_STATE = set(151u, "Dynamic Packet State")
        val UPSTREAM_MULTICAST_PACKET = set(152u, "Upstream Multicast Packet")
        val QUICK_START = set(25u, "Quick-Start")

        // TODO: All IPv4 Option Types

        operator fun get(value: UByte): IPv4OptionType = registry[value] ?: IPv4OptionType(value, "unknown")
        fun set(
            value: UByte,
            name: String,
            optionFactory: (ByteArray, Int, Int) -> IPv4Option = ::UnknownIpV4Option
        ): IPv4OptionType {
            val version = IPv4OptionType(value, name, optionFactory)
            registry[value] = version
            return version
        }
    }
}
