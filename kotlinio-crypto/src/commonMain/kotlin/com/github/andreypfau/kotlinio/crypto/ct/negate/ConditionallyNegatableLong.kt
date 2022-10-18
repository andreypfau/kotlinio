@file:Suppress("OVERRIDE_BY_INLINE")

package com.github.andreypfau.kotlinio.crypto.ct.negate

import com.github.andreypfau.kotlinio.crypto.ct.Choise
import com.github.andreypfau.kotlinio.crypto.ct.select.ConditionallySelectable
import com.github.andreypfau.kotlinio.crypto.ct.select.ConditionallySelectableLong
import com.github.andreypfau.kotlinio.crypto.ct.select.asConditionallySelectable
import com.github.andreypfau.kotlinio.crypto.ct.select.conditionalSelect
import kotlin.jvm.JvmInline

@JvmInline
value class ConditionallyNegatableLong(
    val value: ConditionallySelectableLong
) : ConditionallyNegatable<ConditionallyNegatableLong>, ConditionallySelectable<ConditionallySelectableLong> by value {
    override inline fun conditionalNegate(choise: Choise): ConditionallyNegatableLong =
        value.value.conditionalNegate(choise).asConditionallyNegatable()

    override inline fun unaryMinus(): ConditionallyNegatableLong = (-value.value).asConditionallyNegatable()
}

inline fun Long.asConditionallyNegatable(): ConditionallyNegatableLong = ConditionallyNegatableLong(asConditionallySelectable())
inline fun Long.conditionalNegate(choise: Choise): Long = conditionalSelect(-this, choise)
