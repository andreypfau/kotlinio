package com.github.andreypfau.network.packet.dns

import com.github.andreypfau.network.packet.dns.rdata.*
import kotlin.jvm.JvmStatic

enum class DnsResourceRecordType(
    val value: UShort,
    private val payloadFactory: (ByteArray, Int, Int) -> DnsRData = ::DnsRDataNull
) {
    A(1u, ::DnsRDataA),
    NS(2u),
    MD(3u),
    MF(4u),
    CNAME(5u, ::DnsRDataCName),
    SOA(6u),
    MB(7u),
    MG(8u),
    MR(9u),
    NULL(10u, ::DnsRDataNull),
    WKS(11u),
    PTR(12u),
    HINFO(13u),
    MINFO(14u),
    MX(15u),
    TXT(16u),
    RP(17u),
    AFSDB(18u),
    X25(19u),
    ISDN(20u),
    RT(21u),
    NSAP(22u),
    NSAP_PTR(23u),
    SIG(24u),
    KEY(25u),
    PX(26u),
    GPOS(27u),
    AAAA(28u, ::DnsRDataAAAA),
    LOC(29u),
    NXT(30u),
    EID(31u),
    NIMLOC(32u),
    SRV(33u),
    ATMA(34u),
    NAPTR(35u),
    KX(36u),
    CERT(37u),
    A6(38u),
    DNAME(39u),
    SINK(40u),
    OPT(41u),
    APL(42u),
    DS(43u),
    SSHFP(44u),
    IPSECKEY(45u),
    RRSIG(46u),
    NSEC(47u),
    DNSKEY(48u),
    DHCID(49u),
    NSEC3(50u),
    NSEC3PARAM(51u),
    TLSA(52u),
    SMIMEA(53u),
    HIP(55u),
    NINFO(56u),
    RKEY(57u),
    TALINK(58u),
    CDS(59u),
    CDNSKEY(60u),
    OPENPGPKEY(61u),
    CSYNC(62u),
    SPF(99u),
    UINFO(100u),
    UID(101u),
    GID(102u),
    UNSPEC(103u),
    NID(104u),
    L32(105u),
    L64(106u),
    LP(107u),
    EUI48(108u),
    EUI64(109u),
    TKEY(249u),
    TSIG(250u),
    IXFR(251u),
    AXFR(252u),
    MAILB(253u),
    MAILA(254u),
    ALL_RECORDS(255u),
    URI(256u),
    CAA(257u),
    AVC(258u),
    TA(32768u),
    DLV(32769u),
    ;

    fun getInstance(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset): DnsRData =
        try {
            payloadFactory(rawData, offset, length)
        } catch (e: Exception) {
            DnsRDataNull(rawData, offset, length)
        }

    companion object {
        private val registry = values().associateBy { it.value }

        @JvmStatic
        fun getOrNull(value: UShort): DnsResourceRecordType? = registry[value]

        @JvmStatic
        operator fun get(value: UShort): DnsResourceRecordType = requireNotNull(getOrNull(value)) {
            "Unsupported record type: $value"
        }
    }
}
