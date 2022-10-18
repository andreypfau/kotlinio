@file:Suppress("OVERRIDE_BY_INLINE")

package com.github.andreypfau.kotlinio.crypto.ct.negate

import com.github.andreypfau.kotlinio.crypto.ct.*
import com.github.andreypfau.kotlinio.crypto.ct.select.*
import kotlin.jvm.JvmInline

interface ConditionallyNegatable<T : ConditionallyNegatable<T>> {
    fun conditionalNegate(choise: Choise): T
    operator fun unaryMinus(): T
}
