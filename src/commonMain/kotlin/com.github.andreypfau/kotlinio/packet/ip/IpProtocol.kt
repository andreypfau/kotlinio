package com.github.andreypfau.kotlinio.packet.ip

import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.simple.SimplePacket
import com.github.andreypfau.kotlinio.packet.tcp.TcpPacket
import com.github.andreypfau.kotlinio.packet.udp.UdpPacket
import com.github.andreypfau.kotlinio.utils.NamedValue

class IpProtocol private constructor(
    override val value: Byte,
    override val name: String,
    val factory: (ByteArray, Int, Int) -> Packet = ::SimplePacket
) : NamedValue<Byte, IpProtocol>() {
    override fun compareTo(other: IpProtocol): Int = value.compareTo(other.value)

    fun packet(rawData: ByteArray, offset: Int, length: Int) = factory(rawData, offset, length)

    companion object {
        private val registry = HashMap<Byte, IpProtocol>()

        val IPV6_HOPOPT = set(0, "IPv6 Hop-by-Hop Option")
        val ICMPV4 = set(1, "ICMPv4")
        val IGMP = set(2, "IGMP")
        val GGP = set(3, "GGP")
        val IPV4 = set(4, "IPv4 encapsulation")
        val ST = set(5, "Stream")
        val TCP = set(6, "TCP", ::TcpPacket)
        val CBT = set(7, "SBT")
        val EGP = set(8, "EGP")
        val IGP = set(9, "IGP")
        val BBN_RCC_MON = set(10, "BBN RCC Monitoring")
        val NVP_II = set(11, "NVP-II")
        val PUP = set(12, "PUP")
        val ARGUS = set(13, "ARGUS")
        val EMCON = set(14, "EMCON")
        val XNET = set(15, "XNET")
        val CHAOS = set(16, "Chaos")
        val UDP = set(17, "UDP", ::UdpPacket)
        val MUX = set(18, "Multiplexing")

        // TODO other ip numbers

        val ICMPV6 = set(58, "ICMPv6")

        operator fun get(value: Byte): IpProtocol =
            registry[value] ?: IpProtocol(value, "unknown")

        fun set(value: Byte, name: String, factory: (ByteArray, Int, Int) -> Packet = ::SimplePacket): IpProtocol {
            val protocol = IpProtocol(value, name, factory)
            registry[value] = protocol
            return protocol
        }
    }
}
