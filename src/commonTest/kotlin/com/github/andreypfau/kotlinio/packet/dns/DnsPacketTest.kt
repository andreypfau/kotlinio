package com.github.andreypfau.kotlinio.packet.dns

import com.github.andreypfau.kotlinio.address.Inet4Address
import com.github.andreypfau.kotlinio.address.Inet6Address
import com.github.andreypfau.kotlinio.packet.dns.rdata.DnsRDataA
import com.github.andreypfau.kotlinio.packet.dns.rdata.DnsRDataAAAA
import com.github.andreypfau.kotlinio.packet.dns.rdata.DnsRDataCName
import kotlin.test.Test
import kotlin.test.assertEquals

class DnsPacketTest {
    private val tonDomain = listOf(
        "dns", "ton", "org"
    )
    private val www = listOf("www")

    private val questions = listOf(
        DnsQuestion {
            qName {
                labels += tonDomain
            }
            qType = DnsResourceRecordType.A
            qClass = DnsClass.IN
        }
    )
    private val answers = listOf(
        DnsResourceRecord {
            name = DnsDomainName {
                labels += tonDomain
            }
            dataType = DnsResourceRecordType.A
            dataClass = DnsClass.ANY
            ttl = 123u
            rData = DnsRDataA(Inet4Address("1.2.3.4"))
        },
        DnsResourceRecord {
            name = DnsDomainName {
                labels += tonDomain
            }
            dataType = DnsResourceRecordType.AAAA
            dataClass = DnsClass.ANY
            ttl = 123u
            rData = DnsRDataAAAA(Inet6Address())
        },
        DnsResourceRecord {
            name = DnsDomainName {
                labels += tonDomain
            }
            dataType = DnsResourceRecordType.CNAME
            dataClass = DnsClass.ANY
            ttl = 1231u
            rData = DnsRDataCName(
                DnsDomainName {
                    labels += www
                    pointer = 12u
                }
            )
        }
    )
    private val id: UShort = 12233u
    private val response = true
    private val opCode = DnsOpCode.STATUS
    private val authoritativeAnswer = false
    private val truncated = false
    private val recursionDesired = true
    private val recursionAvailable = true
    private val reserved = false
    private val authenticData = false
    private val checkingDisabled = false
    private val rCode = DnsRCode.NOT_AUTH
    private val authorities = emptyList<DnsResourceRecord>()
    private val additionalInfo = emptyList<DnsResourceRecord>()
    private val qdCount = questions.size.toUShort()
    private val anCount = answers.size.toUShort()
    private val nsCount = authorities.size.toUShort()
    private val arCount = additionalInfo.size.toUShort()

    private val packet = DnsPacket.Builder().also {
        it.id = id
        it.isResponse = response
        it.opCode = opCode
        it.isAuthoritativeAnswer = authoritativeAnswer
        it.isTruncated = truncated
        it.isRecursionDesired = recursionDesired
        it.isRecursionAvailable = recursionAvailable
        it.reservedBit = reserved
        it.isAuthenticData = authenticData
        it.isCheckingDisabled = checkingDisabled
        it.rCode = rCode
        it.questions += questions
        it.answers += answers
        it.authorities += authorities
        it.additionalInfo += additionalInfo
    }.build()

    private fun DnsPacket.testGetHeader() {
        assertEquals(id, header.id)
        assertEquals(response, header.isResponse)
        assertEquals(opCode, header.opCode)
        assertEquals(authoritativeAnswer, header.isAuthoritativeAnswer)
        assertEquals(truncated, header.isTruncated)
        assertEquals(recursionDesired, header.isRecursionDesired)
        assertEquals(recursionAvailable, header.isRecursionAvailable)
        assertEquals(reserved, header.reservedBit)
        assertEquals(authenticData, header.isAuthenticData)
        assertEquals(checkingDisabled, header.isCheckingDisabled)
        assertEquals(rCode, header.rCode)
        assertEquals(qdCount, header.qdCount)
        assertEquals(anCount, header.anCount)
        assertEquals(nsCount, header.nsCount)
        assertEquals(arCount, header.arCount)
        assertEquals(questions, header.questions)
        assertEquals(answers, header.answers)
        assertEquals(authorities, header.authorities)
        assertEquals(additionalInfo, header.additionalInfo)
    }

    @Test
    fun testPacket() {
        packet.testGetHeader()
        val p = DnsPacket(packet.toByteArray())
        p.testGetHeader()
        assertEquals(packet, p)
        val p2 = DnsPacket(p.toByteArray())
        p2.testGetHeader()
        assertEquals(packet, p2)
        val p3 = p2.builder().build()
        p3.testGetHeader()
        assertEquals(packet, p3)
    }
}
