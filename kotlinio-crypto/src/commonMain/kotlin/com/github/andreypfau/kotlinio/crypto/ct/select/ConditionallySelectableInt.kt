@file:Suppress("OVERRIDE_BY_INLINE")

package com.github.andreypfau.kotlinio.crypto.ct.select

import com.github.andreypfau.kotlinio.crypto.ct.Choise
import kotlin.jvm.JvmInline

@JvmInline
value class ConditionallySelectableInt(
    val value: Int
) : ConditionallySelectable<ConditionallySelectableInt> {
    override inline fun conditionalSelect(
        other: ConditionallySelectableInt,
        choise: Choise
    ): ConditionallySelectableInt = value.conditionalSelect(other.value, choise).asConditionallySelectable()
}

inline fun Int.asConditionallySelectable() = ConditionallySelectableInt(this)
inline fun Int.conditionalSelect(other: Int, choise: Choise): Int {
    // if choice = 0, mask = (-0) = 0000...0000
    // if choice = 1, mask = (-1) = 1111...1111
    val mask = -(choise.value.toByte().toInt())
    return this xor (mask and (this xor other))
}
inline fun UInt.asConditionallySelectable() = toInt().asConditionallySelectable()
inline fun UInt.conditionalSelect(other: UInt, choise: Choise): UInt = toInt().conditionalSelect(other.toInt(), choise).toUInt()
