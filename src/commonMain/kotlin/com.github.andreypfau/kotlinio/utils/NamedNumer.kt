package com.github.andreypfau.kotlinio.utils

abstract class NamedValue<T : Comparable<T>, U : NamedValue<T, U>> : Comparable<U> {
    abstract val value: T
    abstract val name: String

    override fun toString(): String = buildString {
        append(value)
        append(" (")
        append(name)
        append(")")
    }

    override fun compareTo(other: U): Int = value.compareTo(other.value)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as NamedValue<*, *>

        if (value != other.value) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}
