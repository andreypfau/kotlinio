package com.github.andreypfau.kotlinio.pool

abstract class NoPoolImp<T : Any> : ObjectPool<T> {
    override val capacity: Int
        get() = 0

    override fun recycle(instance: T) {
    }

    override fun close() {
    }
}
