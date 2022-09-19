package com.github.andreypfau.kotlinio.packet.icmpv4.common

import com.github.andreypfau.kotlinio.packet.AbstractPacket
import com.github.andreypfau.kotlinio.packet.ChecksumBuilder
import com.github.andreypfau.kotlinio.packet.Packet
import com.github.andreypfau.kotlinio.packet.icmpv4.ICMPv4Code
import com.github.andreypfau.kotlinio.packet.icmpv4.ICMPv4Type

class ICMPv4CommonBuilder(
    var type: ICMPv4Type = ICMPv4Type.ECHO_REPLY,
    var code: ICMPv4Code = ICMPv4Code.NO_CODE,
    var checksum: UShort = 0u,
    override var payloadBuilder: Packet.Builder? = null
) : AbstractPacket.AbstractBuilder(), ChecksumBuilder<ICMPv4CommonPacket> {
    override var correctChecksumAtBuild: Boolean = true

    override fun build() = ICMPv4CommonPacket(this)
}
