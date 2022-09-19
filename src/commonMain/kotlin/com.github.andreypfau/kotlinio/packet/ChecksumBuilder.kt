package com.github.andreypfau.kotlinio.packet

interface ChecksumBuilder<T> {
    var correctChecksumAtBuild: Boolean
    fun build(): T
}
