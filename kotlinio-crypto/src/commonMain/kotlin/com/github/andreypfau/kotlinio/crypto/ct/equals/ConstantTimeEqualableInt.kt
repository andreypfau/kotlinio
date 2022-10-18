@file:Suppress("OPT_IN_USAGE", "OVERRIDE_BY_INLINE")

package com.github.andreypfau.kotlinio.crypto.ct.equals

import com.github.andreypfau.kotlinio.crypto.ct.Choise
import kotlin.jvm.JvmInline

@JvmInline
value class ConstantTimeEqualableInt(
    val value: Int
) : ConstantTimeEqualable<ConstantTimeEqualableInt> {
    override inline fun ctEquals(other: ConstantTimeEqualableInt): Choise = value.ctEquals(other.value)
}

inline fun Int.asConstantTimeEquals(): ConstantTimeEqualableInt = ConstantTimeEqualableInt(this)
inline infix fun Int.ctEquals(other: Int): Choise {
    // x == 0 if and only if this == other
    val x = this xor other
    // If x == 0, then x and -x are both equal to zero;
    // otherwise, one or both will have its high bit set.
    val y = (x or -x) shr (Int.SIZE_BITS - 1)
    // Result is the opposite of the high bit (now shifted to low).
    return Choise(((y xor 1) and 1).toUByte())
}

inline fun UInt.asConstantTimeEquals(): ConstantTimeEqualableInt = ConstantTimeEqualableInt(this.toInt())
inline infix fun UInt.ctEquals(other: UInt): Choise = toInt().ctEquals(other.toInt())


inline infix fun IntArray.ctEquals(other: IntArray): Choise {
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

inline infix fun UIntArray.ctEquals(other: UIntArray): Choise {
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
