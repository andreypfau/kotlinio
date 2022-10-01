package com.github.andreypfau.kotlinio.pool

import java.lang.reflect.Method

actual typealias Closeable = java.io.Closeable

@PublishedApi
internal actual fun Throwable.addSuppressedInternal(other: Throwable) {
    AddSuppressedMethod?.invoke(this, other)
}

private val AddSuppressedMethod: Method? by lazy {
    try {
        Throwable::class.java.getMethod("addSuppressed", Throwable::class.java)
    } catch (t: Throwable) {
        null
    }
}
