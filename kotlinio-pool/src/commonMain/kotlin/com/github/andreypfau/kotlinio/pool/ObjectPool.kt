package com.github.andreypfau.kotlinio.pool

interface ObjectPool<T : Any> : Closeable {
    val capacity: Int

    fun borrow(): T

    fun recycle(instance: T)
}

inline fun <T : Any, R> ObjectPool<T>.useInstance(block: (T) -> R): R {
    val instance = borrow()
    try {
        return block(instance)
    } finally {
        recycle(instance)
    }
}
