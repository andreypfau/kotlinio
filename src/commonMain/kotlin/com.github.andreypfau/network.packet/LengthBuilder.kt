package com.github.andreypfau.network.packet

interface LengthBuilder<T> {
    var correctLengthAtBuild: Boolean
    fun build(): T
}