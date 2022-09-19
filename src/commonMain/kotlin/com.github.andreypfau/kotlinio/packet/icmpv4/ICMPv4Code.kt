package com.github.andreypfau.kotlinio.packet.icmpv4

enum class ICMPv4Code(
    val value: Byte
) {
    NO_CODE(0),

    // **** Type 3 — Destination Unreachable ****//
    NETWORK_UNREACHABLE(0),
    HOST_UNREACHABLE(1),
    PROTOCOL_UNREACHABLE(2),
    PORT_UNREACHABLE(3),
    FRAGMENTATION_BLOCKED(4),
    SRC_ROUTE_FAILED(5),
    DST_NETWORK_UNKNOWN(6),
    DST_HOST_UNKNOWN(7),
    SRC_HOST_ISOLATED(8),
    DST_NETWORK_PROHIBITED(9),
    DST_HOST_PROHIBITED(10),
    DST_NETWORK_UNREACHABLE_FOR_TOS(11),
    DST_HOST_UNREACHABLE_FOR_TOS(12),
    COMMUNICATION_PROHIBITED(13),
    HOST_PRECEDENCE_VIOLATION(14),
    PRECEDENCE_CUTOFF_IN_EFFECT(15),

    // **** Type 5 — Redirect ****//
    REDIRECT_DATAGRAMS_FOR_NETWORK(0),
    REDIRECT_DATAGRAMS_FOR_HOST(1),
    REDIRECT_DATAGRAMS_FOR_TOS_AND_NETWORK(2),
    REDIRECT_DATAGRAMS_FOR_TOS_AND_HOST(3),

    // **** Type 6 — Alternate Host Address ****//
    ALTERNATE_ADDRESS_FOR_HOST(0),

    // **** Type 9 — Router Advertisement ****//
    NORMAL_ROUTER_ADVERTISEMENT(0),
    DOES_NOT_ROUTE_COMMON_TRAFFIC(16),

    // **** Type 11 — Time Exceeded ****//
    TIME_TO_LIVE_EXCEEDED(0),
    FRAGMENT_REASSEMBLY_TIME_EXCEEDED(1),

    // **** Type 12 — Parameter Problem ****//
    POINTER_INDICATES_ERROR(0),
    MISSING_REQUIRED_OPTION(1),
    BAD_LENGTH(2),

    // **** Type 40 — Photuris ****//
    BAD_SPI(0),
    AUTHENTICATION_FAILED(1),
    DECOMPRESSION_FAILED(2),
    DECRYPTION_FAILED(3),
    NEED_AUTHENTICATION(4),
    NEED_AUTHORIZATION(5)
}