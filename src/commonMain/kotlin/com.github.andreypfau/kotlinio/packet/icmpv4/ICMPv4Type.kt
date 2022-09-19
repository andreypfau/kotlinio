package com.github.andreypfau.kotlinio.packet.icmpv4

enum class ICMPv4Type(
    val value: Byte,
    vararg codes: ICMPv4Code
) {
    ECHO_REPLY(0),
    DESTINATION_UNREACHABLE(
        3,
        ICMPv4Code.NETWORK_UNREACHABLE,
        ICMPv4Code.HOST_UNREACHABLE,
        ICMPv4Code.PROTOCOL_UNREACHABLE,
        ICMPv4Code.PORT_UNREACHABLE,
        ICMPv4Code.FRAGMENTATION_BLOCKED,
        ICMPv4Code.SRC_ROUTE_FAILED,
        ICMPv4Code.DST_NETWORK_UNKNOWN,
        ICMPv4Code.DST_HOST_UNKNOWN,
        ICMPv4Code.SRC_HOST_ISOLATED,
        ICMPv4Code.DST_NETWORK_PROHIBITED,
        ICMPv4Code.DST_HOST_PROHIBITED,
        ICMPv4Code.DST_NETWORK_UNREACHABLE_FOR_TOS,
        ICMPv4Code.DST_HOST_UNREACHABLE_FOR_TOS,
        ICMPv4Code.COMMUNICATION_PROHIBITED,
        ICMPv4Code.HOST_PRECEDENCE_VIOLATION,
        ICMPv4Code.PRECEDENCE_CUTOFF_IN_EFFECT
    ),
    SOURCE_QUENCH(4),
    REDIRECT(
        5,
        ICMPv4Code.REDIRECT_DATAGRAMS_FOR_NETWORK,
        ICMPv4Code.REDIRECT_DATAGRAMS_FOR_HOST,
        ICMPv4Code.REDIRECT_DATAGRAMS_FOR_TOS_AND_NETWORK,
        ICMPv4Code.REDIRECT_DATAGRAMS_FOR_TOS_AND_HOST
    ),
    ALTERNATE_HOST_ADDRESS(
        6,
        ICMPv4Code.ALTERNATE_ADDRESS_FOR_HOST
    ),
    ECHO(8),
    ROUTER_ADVERTISEMENT(
        9,
        ICMPv4Code.NORMAL_ROUTER_ADVERTISEMENT,
        ICMPv4Code.DOES_NOT_ROUTE_COMMON_TRAFFIC
    ),
    ROUTER_SOLICITATION(10),
    TIME_EXCEEDED(
        11,
        ICMPv4Code.TIME_TO_LIVE_EXCEEDED,
        ICMPv4Code.FRAGMENT_REASSEMBLY_TIME_EXCEEDED
    ),
    PARAMETER_PROBLEM(
        12,
        ICMPv4Code.POINTER_INDICATES_ERROR,
        ICMPv4Code.MISSING_REQUIRED_OPTION,
        ICMPv4Code.BAD_LENGTH
    ),
    TIMESTAMP(13),
    TIMESTAMP_REPLY(14),
    INFORMATION_REQUEST(15),
    INFORMATION_REPLY(16),
    ADDRESS_MASK_REQUEST(17),
    ADDRESS_MASK_REPLY(18),
    TRACEROUTE(30),
    DATAGRAM_CONVERSION_ERROR(31),
    MOBILE_HOST_REDIRECT(32),
    IPV6_WHERE_ARE_YOU(33),
    IPV6_I_AM_HERE(34),
    MOBILE_REGISTRATION_REQUEST(35),
    MOBILE_REGISTRATION_REPLY(36),
    DOMAIN_NAME_REQUEST(37),
    DOMAIN_NAME_REPLY(38),
    SKIP(39),
    PHOTURIS(
        40,
        ICMPv4Code.BAD_SPI,
        ICMPv4Code.AUTHENTICATION_FAILED,
        ICMPv4Code.DECOMPRESSION_FAILED,
        ICMPv4Code.DECRYPTION_FAILED,
        ICMPv4Code.NEED_AUTHENTICATION,
        ICMPv4Code.NEED_AUTHORIZATION
    );

    private val registry = HashMap<Byte, ICMPv4Code>().apply {
        codes.forEach {
            put(it.value, it)
        }
        if (!containsKey(0)) {
            put(0, ICMPv4Code.NO_CODE)
        }
    }

    override fun toString(): String = "$value ($name)"

    operator fun get(code: Byte) = requireNotNull(registry[code]) { "Unknown code: $code" }

    companion object {
        private val registry = values().associateBy { it.value }

        operator fun get(value: Byte) = requireNotNull(registry[value]) { "Unknown type: $value" }
    }
}
