package com.github.andreypfau.network.packet

interface ChecksumBuilder<T> {
    var correctChecksumAtBuild: Boolean
    fun build(): T
}