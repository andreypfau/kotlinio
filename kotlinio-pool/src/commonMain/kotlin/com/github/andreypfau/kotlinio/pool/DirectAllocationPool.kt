package com.github.andreypfau.kotlinio.pool

/**
 * A pool implementation of zero capacity that always creates new instances
 */
fun interface DirectAllocationPool<T : Any> : ObjectPool<T> {
    override val capacity: Int get() = 0

    override fun recycle(instance: T) = Unit

    override fun close() = Unit
}

fun interface DirectAllocationCloseablePool<T : Closeable> : DirectAllocationPool<T> {
    override fun recycle(instance: T) {
        instance.close()
    }
}
