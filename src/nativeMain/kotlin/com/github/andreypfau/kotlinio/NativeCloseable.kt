package com.github.andreypfau.kotlinio

actual interface Closeable {
    actual fun close()
}

@PublishedApi
internal actual fun Throwable.addSuppressedInternal(other: Throwable) {
}
