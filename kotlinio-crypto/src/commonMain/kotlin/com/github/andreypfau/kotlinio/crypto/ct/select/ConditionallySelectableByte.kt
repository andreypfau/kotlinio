@file:Suppress("OVERRIDE_BY_INLINE")

package com.github.andreypfau.kotlinio.crypto.ct.select

import com.github.andreypfau.kotlinio.crypto.ct.Choise
import kotlin.experimental.xor
import kotlin.jvm.JvmInline

@JvmInline
value class ConditionallySelectableByte(
    val value: Byte
) : ConditionallySelectable<ConditionallySelectableByte> {
    override inline fun conditionalSelect(
        other: ConditionallySelectableByte,
        choise: Choise
    ): ConditionallySelectableByte = value.conditionalSelect(other.value, choise).asConditionallySelectable()
}

inline fun Byte.asConditionallySelectable() = ConditionallySelectableByte(this)
inline fun Byte.conditionalSelect(other: Byte, choise: Choise): Byte = toInt().conditionalSelect(other.toInt(), choise).toByte()
inline fun UByte.asConditionallySelectable() = toByte().asConditionallySelectable()
inline fun UByte.conditionalSelect(other: UByte, choise: Choise): UByte = toByte().conditionalSelect(other.toByte(), choise).toUByte()
