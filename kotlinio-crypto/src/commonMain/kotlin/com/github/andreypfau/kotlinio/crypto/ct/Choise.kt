package com.github.andreypfau.kotlinio.crypto.ct

import com.github.andreypfau.kotlinio.crypto.ct.equals.ConstantTimeEqualable
import com.github.andreypfau.kotlinio.crypto.ct.equals.ctEquals
import com.github.andreypfau.kotlinio.crypto.ct.select.ConditionallySelectable
import com.github.andreypfau.kotlinio.crypto.ct.select.conditionalSelect
import kotlin.jvm.JvmInline

/**
 * The [Choice] represents a choice for use in conditional assignment.
 *
 * It is a wrapper around a [UByte], which should have the value either 1 (true) or 0 (false).
 * The conversion from u8 to Choice passes the value through an optimization barrier, as a best-effort
 * attempt to prevent the compiler from inferring that the Choice value is a boolean.
 * The Choice struct implements operators for [and], [or], [xor], and [inv], to allow combining Choice values.
 * These operations do not short-circuit.
 */
@JvmInline
value class Choise(
    val value: UByte
) : ConditionallySelectable<Choise>, ConstantTimeEqualable<Choise> {
    infix fun and(other: Choise): Choise = Choise(value and other.value)
    infix fun or(other: Choise): Choise = Choise(value or other.value)
    infix fun xor(other: Choise): Choise = Choise(value xor other.value)
    fun inv(): Choise = Choise(1.toUByte() and value.inv())

    override fun conditionalSelect(other: Choise, choise: Choise): Choise =
        Choise(value.conditionalSelect(other.value, choise))

    override fun ctEquals(other: Choise): Choise =
        value.ctEquals(other.value)

    fun toByte(): Byte = value.toByte()
    fun toUByte(): UByte = value
    fun toBoolean(): Boolean = this == TRUE

    companion object {
        val FALSE = Choise(0u)
        val TRUE = Choise(1u)
    }
}
