package com.github.andreypfau.kotlinio.packet.ethernet

import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.ipv4.IPv4Packet
import com.github.andreypfau.kotlinio.packet.ipv6.IPv6Packet
import com.github.andreypfau.kotlinio.packet.simple.SimplePacket
import com.github.andreypfau.kotlinio.utils.NamedValue

class EtherType private constructor(
    override val value: UShort,
    override val name: String,
    val payloadFactory: (ByteArray, Int, Int) -> Packet = ::SimplePacket
) : NamedValue<UShort, EtherType>() {
    override fun compareTo(other: EtherType): Int = value.compareTo(other.value)

    companion object {
        private val registry = HashMap<UShort, EtherType>()

        const val IEEE802_3_MAX_LENGTH = 1500
        val IPv4 = set(0x0800u, "IPv4", ::IPv4Packet)
        val ARP = set(0x0806u, "ARP")
        val DOT1Q_VLAN_TAGGED_FRAMES = set(0x8100u, "IEEE 802.1Q VLAN-tagged frames")
        val RARP = set(0x8035u, "RARP")
        val APPLETALK = set(0x809bu, "Appletalk")
        val IPV6 = set(0x86ddu, "IPv6", ::IPv6Packet)
        val PPP = set(0x880bu, "PPP")
        val MPLS = set(0x8847u, "MPLS")
        val PPPOE_DISCOVERY_STAGE = set(0x8863u, "PPPoE Discovery Stage")
        val PPPOE_SESSION_STAGE = set(0x8864u, "PPPoE Session Stage")

        operator fun get(value: UShort): EtherType = registry[value] ?: EtherType(value, "unknown")

        fun set(
            value: UShort,
            name: String,
            payloadFactory: (ByteArray, Int, Int) -> Packet = ::SimplePacket
        ): EtherType {
            val version = EtherType(value, name, payloadFactory)
            registry[value] = version
            return version
        }
    }
}
