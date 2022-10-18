@file:Suppress("OPT_IN_USAGE", "OVERRIDE_BY_INLINE")

package com.github.andreypfau.kotlinio.crypto.ct.equals

import com.github.andreypfau.kotlinio.crypto.ct.Choise
import kotlin.jvm.JvmInline

@JvmInline
value class ConstantTimeEqualableShort(
    val value: Short
) : ConstantTimeEqualable<ConstantTimeEqualableShort> {
    override inline fun ctEquals(other: ConstantTimeEqualableShort): Choise = value.ctEquals(other.value)
}

inline fun Short.asConstantTimeEquals(): ConstantTimeEqualableShort = ConstantTimeEqualableShort(this)
inline infix fun Short.ctEquals(other: Short): Choise = toInt().ctEquals(other.toInt())

inline fun UShort.asConstantTimeEquals(): ConstantTimeEqualableShort = ConstantTimeEqualableShort(this.toShort())
inline infix fun UShort.ctEquals(other: UShort): Choise = toShort().ctEquals(other.toShort())


inline infix fun ShortArray.ctEquals(other: ShortArray): Choise {
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

inline infix fun UShortArray.ctEquals(other: UShortArray): Choise {
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
