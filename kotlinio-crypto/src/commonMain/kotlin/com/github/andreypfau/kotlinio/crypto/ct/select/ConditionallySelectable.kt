@file:Suppress("OVERRIDE_BY_INLINE")

package com.github.andreypfau.kotlinio.crypto.ct.select

import com.github.andreypfau.kotlinio.crypto.ct.Choise

fun interface ConditionallySelectable<T : ConditionallySelectable<T>> {
    fun conditionalSelect(other: T, choise: Choise): T
}
