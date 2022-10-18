@file:Suppress("OPT_IN_USAGE", "OVERRIDE_BY_INLINE")

package com.github.andreypfau.kotlinio.crypto.ct.equals

import com.github.andreypfau.kotlinio.crypto.ct.Choise
import kotlin.experimental.or
import kotlin.experimental.xor
import kotlin.jvm.JvmInline

@JvmInline
value class ConstantTimeEqualableByte(
    val value: Byte
) : ConstantTimeEqualable<ConstantTimeEqualableByte> {
    override inline fun ctEquals(other: ConstantTimeEqualableByte): Choise = value.ctEquals(other.value)
}

inline fun Byte.asConstantTimeEquals(): ConstantTimeEqualableByte = ConstantTimeEqualableByte(this)
inline infix fun Byte.ctEquals(other: Byte): Choise =toInt().ctEquals(other.toInt())

inline fun UByte.asConstantTimeEquals(): ConstantTimeEqualableByte = ConstantTimeEqualableByte(this.toByte())
inline infix fun UByte.ctEquals(other: UByte): Choise = toByte().ctEquals(other.toByte())


inline infix fun ByteArray.ctEquals(other: ByteArray): Choise {
    // Short-circuit on the *size* of the arrays, not their
    // contents.
    if (size != other.size) {
        return Choise.FALSE
    }
    var x = Choise.TRUE
    for (i in indices) {
        x = x and (this[i].ctEquals(other[i]))
    }
    return x
}

inline infix fun UByteArray.ctEquals(other: UByteArray): Choise {
    // Short-circuit on the *size* of the arrays, not their
    // contents.
    if (size != other.size) {
        return Choise.FALSE
    }
    var x = Choise.TRUE
    for (i in indices) {
        x = x and (this[i].ctEquals(other[i]))
    }
    return x
}
