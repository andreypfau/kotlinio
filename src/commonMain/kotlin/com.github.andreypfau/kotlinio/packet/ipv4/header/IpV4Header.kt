package com.github.andreypfau.kotlinio.packet.ipv4.header

import com.github.andreypfau.kotlinio.address.Inet4Address
import com.github.andreypfau.kotlinio.packet.ip.IpPacket
import com.github.andreypfau.kotlinio.packet.ip.IpProtocol
import com.github.andreypfau.kotlinio.packet.ip.IpVersion
import com.github.andreypfau.kotlinio.packet.ipv4.option.IPv4Option
import com.github.andreypfau.kotlinio.packet.ipv4.tos.IPv4Tos
import kotlin.jvm.JvmStatic

@Suppress("NOTHING_TO_INLINE")
inline fun IpV4Header(byteArray: ByteArray): IpV4Header =
    IpV4Header.newInstance(byteArray)

@Suppress("NOTHING_TO_INLINE")
inline fun IpV4Header(rawData: ByteArray, offset: Int, length: Int = rawData.size - offset): IpV4Header =
    IpV4Header.newInstance(rawData, offset, length)

interface IpV4Header : IpPacket.IpHeader {
    val versionAndIhl: Byte
    override val version: IpVersion
    val ihl: UByte
    val tos: IPv4Tos
    val totalLength: UShort
    val identification: UShort
    val flags: UShort
    val ttl: UByte
    override val protocol: IpProtocol
    val headerChecksum: UShort
    override val srcAddress: Inet4Address
    override val dstAddress: Inet4Address
    val options: List<IPv4Option>
    val padding: ByteArray
    override val length: Int get() = ihl.toInt() * 4

    val reservedBit: Boolean get() = (flags.toInt() and RESERVED_BIT_MASK) != 0
    val dontFragment: Boolean get() = (flags.toInt() and DONT_FRAGMENT_MASK) != 0
    val moreFragments: Boolean get() = (flags.toInt() and MORE_FRAGMENTS_MASK) != 0
    val fragmentOffset: UShort get() = (flags.toInt() and FRAGMENT_OFFSET_MASK).toUShort()
    val isFragmented: Boolean get() = moreFragments || fragmentOffset.toInt() != 0

    fun calcChecksum(): UShort
    fun isValidChecksum(): Boolean = calcChecksum() == headerChecksum

    companion object {
        const val VERSION_MASK = 0xF0
        const val IHL_MASK = 0x0F
        const val RESERVED_BIT_MASK = 0x8000
        const val DONT_FRAGMENT_MASK = 0x4000
        const val MORE_FRAGMENTS_MASK = 0x2000
        const val FLAGS_MASK = 0xE000
        const val FRAGMENT_OFFSET_MASK = 0x1FFF

        @JvmStatic
        fun newInstance(byteArray: ByteArray): IpV4Header =
            newInstance(byteArray.copyOf(), 0)

        @JvmStatic
        fun newInstance(rawData: ByteArray, offset: Int, length: Int = rawData.size - offset): IpV4Header {
            return ByteBackedIpV4Header(rawData, offset, length)
        }
    }
}
