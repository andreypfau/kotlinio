package com.github.andreypfau.kotlinio.packet

interface LengthBuilder<T> {
    var correctLengthAtBuild: Boolean
    fun build(): T
}
