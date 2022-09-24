package com.github.andreypfau.kotlinio.packet.tcp

import com.github.andreypfau.kotlinio.address.*
import com.github.andreypfau.kotlinio.packet.dns.*
import com.github.andreypfau.kotlinio.packet.dns.rdata.DnsRDataA
import com.github.andreypfau.kotlinio.packet.dns.rdata.DnsRDataAAAA
import com.github.andreypfau.kotlinio.packet.ip.IpPacket
import com.github.andreypfau.kotlinio.packet.ip.IpProtocol
import com.github.andreypfau.kotlinio.packet.ipv4.IPv4Builder
import com.github.andreypfau.kotlinio.packet.ipv4.IPv4Packet
import com.github.andreypfau.kotlinio.packet.ipv6.IPv6Builder
import com.github.andreypfau.kotlinio.packet.ipv6.IPv6Packet
import com.github.andreypfau.kotlinio.packet.udp.UdpBuilder
import com.github.andreypfau.kotlinio.packet.udp.UdpPacket
import com.github.andreypfau.kotlinio.utils.hex
import org.pcap4j.packet.IpV4Packet
import kotlin.test.Test

class TcpTest {
    @Test
    fun test() {
        val a = hex("45000075953600004011cb290a08000108080808c5790035006132091eb4010000010000000000013775746f6c6a6a79653679346978617a65736a6f6669646c6b72687969616b6977726d6573336d356874686c6336696532683732676c6c740461646e6c000001000100002905c0000000000000")
        val query = IpPacket.newInstance(a)
        val response = DnsHandler.handleLocalPacket(query)
        val rawData = response!!.rawData
        val packet = IpV4Packet.newPacket(rawData, 0, rawData.size)
    }

    @Test
    fun a() {
        val h = hex("3775746f6c6a6a79653679346978617a65736a6f6669646c6b72687969616b6977726d6573336d356874686c6336696532683732676c6c740461646e6c0000010001")
        val dnsQuestion = DnsQuestion(h)
        println(h.hex())
        val question = dnsQuestion.builder().build()
        val toByteArray = dnsQuestion.toByteArray()
        println(toByteArray.hex())
    }
}


object DnsHandler {
    fun handleLocalPacket(ipPacket: IpPacket?): IpPacket? {
        if (ipPacket == null) return null
        val udpPacket = ipPacket[UdpPacket::class] ?: return ipPacket
        if (udpPacket.header.dstPort != 53.toUShort()) return ipPacket
        val payload = udpPacket.payload?.rawData ?: return ipPacket
        val dnsPacket = try {
            DnsPacket(payload)
        } catch (e: Exception) {
            return ipPacket
        }
        println(payload.hex())
        println(dnsPacket.toByteArray().hex())
        val toByteArray = dnsPacket.builder().build().toByteArray()
        println(toByteArray.hex())
        val adnlQuestions = dnsPacket.header.questions.filter {
            it.qName.name.endsWith(".adnl")
        }
        if (adnlQuestions.isEmpty()) return ipPacket
        val adnlAnswers = adnlQuestions.map(::resolveAdnlQuestion)

        val dnsPacketBuilder = DnsPacket.Builder().apply {
            id = dnsPacket.header.id
            isResponse = true
            opCode = DnsOpCode.QUERY
            rCode = DnsRCode.NO_ERROR
            questions += adnlQuestions
            answers += adnlAnswers
        }
        val udpPacketResponse = UdpBuilder().apply {
            srcAddress = ipPacket.header.dstAddress
            dstAddress = ipPacket.header.srcAddress
            srcPort = udpPacket.header.dstPort
            dstPort = udpPacket.header.srcPort
            payloadBuilder = dnsPacketBuilder
        }
        val dstAddress = ipPacket.header.dstAddress
        return when (ipPacket) {
            is IPv4Packet -> (
                IPv4Builder(
                    srcAddress = ipPacket.header.dstAddress,
                    dstAddress = ipPacket.header.srcAddress,
                    protocol = IpProtocol.UDP,
                    payloadBuilder = udpPacketResponse
                ).build()
                )
            is IPv6Packet ->
                IPv6Builder(
                    srcAddress = ipPacket.header.dstAddress,
                    dstAddress = ipPacket.header.srcAddress,
                    nextHeader = IpProtocol.UDP,
                    payloadBuilder = udpPacketResponse
                ).build()
            else -> throw IllegalArgumentException("Unsupported address: $dstAddress")
        }
    }

    fun resolveAdnlQuestion(adnlQuestion: DnsQuestion): DnsResourceRecord {
        val (address, _) = HttpHandler.getRandomProxy()
        return DnsResourceRecord.Builder().apply {
            name = adnlQuestion.qName
            dataClass = DnsClass.IN
            ttl = 10u
            when (address) {
                is Inet4Address -> {
                    dataType = DnsResourceRecordType.A
                    rData = DnsRDataA(address)
                }

                is Inet6Address -> {
                    dataType = DnsResourceRecordType.AAAA
                    rData = DnsRDataAAAA(address)
                }

                else -> throw IllegalArgumentException("Unsupported address: $address")
            }
        }.build()
    }
}



class HttpHandler(
) {
    fun handleLocal(ipPacket: IpPacket?): IpPacket? {
        if (ipPacket == null) return null
        var payloadBuilder = (ipPacket.payload as? TcpPacket)?.builder() ?: return ipPacket
        payloadBuilder = handleLocal(payloadBuilder) ?: return ipPacket
        return ipPacket.builder().apply {
            this.payloadBuilder = payloadBuilder
        }.build()
    }

    fun handleLocal(tcp: TcpBuilder?): TcpBuilder? {
        if (tcp == null) return null
        val proxy = getProxyByAddress(tcp.dstAddress) ?: return null
        if (tcp.dstPort == proxy.port) return null
        tcp.dstPort = proxy.port
        return tcp
    }

    fun handleRemote(ipPacket: IpPacket?): IpPacket? {
        if (ipPacket == null) return null
        var payloadBuilder = (ipPacket.payload as? TcpPacket)?.builder() ?: return ipPacket
        payloadBuilder = handleRemote(payloadBuilder) ?: return ipPacket
        return ipPacket.builder().apply {
            this.payloadBuilder = payloadBuilder
        }.build()
    }

    fun handleRemote(tcp: TcpBuilder?): TcpBuilder? {
        if (tcp == null) return null
        val proxy = getProxyByAddress(tcp.srcAddress) ?: return null
        tcp.srcPort = proxy.port
        return tcp
    }

    companion object {
        val PROXY_LIST: List<InetSocketAddress> = listOf(
            Inet4Address(ubyteArrayOf(164u, 92u, 157u, 214u).toByteArray()) to 8080u
        )

        fun getProxyByAddress(address: InetAddress?): InetSocketAddress? {
            if (address == null) return null
            return PROXY_LIST.find { it.first == address }
        }

        // TODO: check availability
        fun getRandomProxy(): InetSocketAddress {
            return PROXY_LIST.random()
        }
    }
}

