package com.github.andreypfau.network.packet.dns

interface DnsHeader {
    val id: UShort
    val response: Boolean
    val opCode: DnsOpCode
    val authoritativeAnswer: Boolean
    val truncated: Boolean
    val recursionDesired: Boolean
    val recursionAvailable: Boolean
    val reserved: Boolean
    val authenticData: Boolean
    val checkingDisabled: Boolean
    val rCode: DnsRCode
    val qdCount: UShort
    val anCount: UShort
    val nsCount: UShort
    val arCount: UShort
    val questions: List<DnsQuestion>
    val answers: List<DnsResourceRecord>
    val authorities: List<DnsResourceRecord>
    val additionalInfo: List<DnsResourceRecord>
}
