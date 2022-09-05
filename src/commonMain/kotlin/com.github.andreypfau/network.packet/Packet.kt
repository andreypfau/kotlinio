package com.github.andreypfau.network.packet

import kotlin.reflect.KClass

interface Packet : Iterable<Packet> {
    val header: Header?
    val payload: Packet?
    val length: Int
    val rawData: ByteArray

    operator fun <T : Packet> get(clazz: KClass<T>): T?
    fun getOuterOf(clazz: KClass<out Packet>): Packet?
    operator fun <T : Packet> contains(clazz: KClass<T>): Boolean

    fun builder(): Builder

    interface Builder : Iterable<Builder> {
        var payloadBuilder: Builder?

        operator fun <T : Builder> get(clazz: KClass<T>): T?
        fun getOuterOf(clazz: KClass<out Builder>): Builder?

        fun build(): Packet
    }

    interface Header {
        val length: Int
        val rawData: ByteArray
    }
}