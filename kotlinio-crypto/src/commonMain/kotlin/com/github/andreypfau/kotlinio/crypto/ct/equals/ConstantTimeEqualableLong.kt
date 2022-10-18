@file:Suppress("OPT_IN_USAGE", "OVERRIDE_BY_INLINE")

package com.github.andreypfau.kotlinio.crypto.ct.equals

import com.github.andreypfau.kotlinio.crypto.ct.Choise
import kotlin.jvm.JvmInline

@JvmInline
value class ConstantTimeEqualableLong(
    val value: Long
) : ConstantTimeEqualable<ConstantTimeEqualableLong> {
    override inline fun ctEquals(other: ConstantTimeEqualableLong): Choise = value.ctEquals(other.value)
}

inline fun Long.asConstantTimeEquals(): ConstantTimeEqualableLong = ConstantTimeEqualableLong(this)
inline infix fun Long.ctEquals(other: Long): Choise {
    // x == 0 if and only if this == other
    val x = this xor other
    // If x == 0, then x and -x are both equal to zero;
    // otherwise, one or both will have its high bit set.
    val y = (x or -x) shr (Long.SIZE_BITS - 1)
    // Result is the opposite of the high bit (now shifted to low).
    return Choise(((y xor 1) and 1).toUByte())
}

inline fun ULong.asConstantTimeEquals(): ConstantTimeEqualableLong = ConstantTimeEqualableLong(this.toLong())
inline infix fun ULong.ctEquals(other: ULong): Choise = toLong().ctEquals(other.toLong())


inline infix fun LongArray.ctEquals(other: LongArray): Choise {
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

inline infix fun ULongArray.ctEquals(other: ULongArray): Choise {
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
