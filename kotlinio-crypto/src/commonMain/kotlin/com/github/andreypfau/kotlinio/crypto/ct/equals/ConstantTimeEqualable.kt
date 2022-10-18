package com.github.andreypfau.kotlinio.crypto.ct.equals

import com.github.andreypfau.kotlinio.crypto.ct.Choise

interface ConstantTimeEqualable<T : ConstantTimeEqualable<T>> {
    infix fun ctEquals(other: T): Choise
}
