package com.github.andreypfau.kotlinio

fun a(a: String): Boolean {
    return if (a != null && a.isBlank()) true else false
}
