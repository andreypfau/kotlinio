@file:Suppress("OVERRIDE_BY_INLINE")

package com.github.andreypfau.kotlinio.crypto.ct.select

import com.github.andreypfau.kotlinio.crypto.ct.Choise
import kotlin.experimental.xor
import kotlin.jvm.JvmInline

@JvmInline
value class ConditionallySelectableShort(
    val value: Short
) : ConditionallySelectable<ConditionallySelectableShort> {
    override inline fun conditionalSelect(
        other: ConditionallySelectableShort,
        choise: Choise
    ): ConditionallySelectableShort = value.conditionalSelect(other.value, choise).asConditionallySelectable()
}

inline fun Short.asConditionallySelectable() = ConditionallySelectableShort(this)
inline fun Short.conditionalSelect(other: Short, choise: Choise): Short = toInt().conditionalSelect(other.toInt(), choise).toShort()
inline fun UShort.asConditionallySelectable() = toShort().asConditionallySelectable()
inline fun UShort.conditionalSelect(other: UShort, choise: Choise): UShort = toShort().conditionalSelect(other.toShort(), choise).toUShort()
