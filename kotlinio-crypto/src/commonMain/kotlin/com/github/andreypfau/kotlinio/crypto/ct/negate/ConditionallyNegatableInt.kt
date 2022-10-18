@file:Suppress("OVERRIDE_BY_INLINE")

package com.github.andreypfau.kotlinio.crypto.ct.negate

import com.github.andreypfau.kotlinio.crypto.ct.Choise
import com.github.andreypfau.kotlinio.crypto.ct.select.ConditionallySelectable
import com.github.andreypfau.kotlinio.crypto.ct.select.ConditionallySelectableInt
import com.github.andreypfau.kotlinio.crypto.ct.select.asConditionallySelectable
import com.github.andreypfau.kotlinio.crypto.ct.select.conditionalSelect
import kotlin.jvm.JvmInline

@JvmInline
value class ConditionallyNegatableInt(
    val value: ConditionallySelectableInt
) : ConditionallyNegatable<ConditionallyNegatableInt>, ConditionallySelectable<ConditionallySelectableInt> by value {
    override inline fun conditionalNegate(choise: Choise): ConditionallyNegatableInt =
        value.value.conditionalNegate(choise).asConditionallyNegatable()

    override inline fun unaryMinus(): ConditionallyNegatableInt = (-value.value).asConditionallyNegatable()
}

inline fun Int.asConditionallyNegatable(): ConditionallyNegatableInt = ConditionallyNegatableInt(asConditionallySelectable())
inline fun Int.conditionalNegate(choise: Choise): Int = conditionalSelect(-this, choise)
