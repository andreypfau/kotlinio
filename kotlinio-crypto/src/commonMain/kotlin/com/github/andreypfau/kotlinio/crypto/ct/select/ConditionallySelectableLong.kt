@file:Suppress("OVERRIDE_BY_INLINE")

package com.github.andreypfau.kotlinio.crypto.ct.select

import com.github.andreypfau.kotlinio.crypto.ct.Choise
import kotlin.jvm.JvmInline

@JvmInline
value class ConditionallySelectableLong(
    val value: Long
) : ConditionallySelectable<ConditionallySelectableLong> {
    override inline fun conditionalSelect(
        other: ConditionallySelectableLong,
        choise: Choise
    ): ConditionallySelectableLong = value.conditionalSelect(other.value, choise).asConditionallySelectable()
}

inline fun Long.asConditionallySelectable() = ConditionallySelectableLong(this)
inline fun Long.conditionalSelect(other: Long, choise: Choise): Long {
    // if choice = 0, mask = (-0) = 0000...0000
    // if choice = 1, mask = (-1) = 1111...1111
    val mask = -(choise.value.toByte().toLong())
    return this xor (mask and (this xor other))
}
inline fun ULong.asConditionallySelectable() = toLong().asConditionallySelectable()
inline fun ULong.conditionalSelect(other: ULong, choise: Choise): ULong = toLong().conditionalSelect(other.toLong(), choise).toULong()
