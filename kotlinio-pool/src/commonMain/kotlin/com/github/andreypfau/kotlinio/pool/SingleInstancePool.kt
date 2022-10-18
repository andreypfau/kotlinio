package com.github.andreypfau.kotlinio.pool

class SingleInstancePool<T : Any> : ObjectPool<T> {
    private val borrowed =

    override val capacity: Int
        get() = 1

    override fun borrow(): T {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }

    override fun recycle(instance: T) {
        TODO("Not yet implemented")
    }

}
