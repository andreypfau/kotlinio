package com.github.andreypfau.network.packet

import com.github.andreypfau.network.utils.concatenate
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

abstract class AbstractPacket : Packet {
    private val _size: Int by lazy {
        calcSize()
    }
    private val _rawData: ByteArray by lazy {
        buildRawData()
    }
    private val _string: String by lazy {
        buildString()
    }
    private val _hashCode: Int by lazy {
        calcHashCode()
    }

    override val header: Packet.Header? = null
    override val payload: Packet? = null

    override val length: Int get() = _size

    override val rawData: ByteArray
        get() = _rawData.copyOf()

    override fun iterator(): Iterator<Packet> = PacketIterator(this)

    override fun <T : Packet> get(clazz: KClass<T>): T? {
        forEach { builder ->
            val result = clazz.safeCast(builder)
            if (result != null) {
                return result
            }
        }
        return null
    }

    override fun getOuterOf(clazz: KClass<out Packet>): Packet? {
        forEach { packet ->
            val result = clazz.safeCast(packet.payload)
            if (result != null) {
                return packet
            }
        }
        return null
    }

    override fun <T : Packet> contains(clazz: KClass<T>): Boolean =
        get(clazz) != null

    abstract override fun builder(): Packet.Builder

    protected open fun calcSize() =
        (header?.length ?: 0) + (payload?.length ?: 0)

    protected open fun buildRawData(): ByteArray {
        val rd = ByteArray(length)
        var dstOffset = 0
        header?.let {
            it.rawData.copyInto(rd, dstOffset)
            dstOffset += it.length
        }
        payload?.let {
            it.rawData.copyInto(rd, dstOffset)
            dstOffset += it.length
        }
        return rd
    }

    protected open fun buildString(): String = buildString {
        header?.let { append(it) }
        payload?.let { append(it) }
    }

    protected open fun calcHashCode(): Int {
        var result = header?.hashCode() ?: 0
        result = 31 * result + (payload?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String = _string

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as AbstractPacket

        if (header != other.header) return false
        if (payload != other.payload) return false

        return true
    }

    override fun hashCode(): Int = _hashCode

    abstract class AbstractBuilder : Packet.Builder {
        override fun iterator(): Iterator<Packet.Builder> = BuilderIterator(this)

        abstract override var payloadBuilder: Packet.Builder?

        override fun <T : Packet.Builder> get(clazz: KClass<T>): T? {
            forEach { builder ->
                val result = clazz.safeCast(builder)
                if (result != null) {
                    return result
                }
            }
            return null
        }

        override fun getOuterOf(clazz: KClass<out Packet.Builder>): Packet.Builder? {
            forEach { builder ->
                val result = clazz.safeCast(builder.payloadBuilder)
                if (result != null) {
                    return builder
                }
            }
            return null
        }

        abstract override fun build(): Packet
    }

    abstract class AbstractHeader : Packet.Header {
        private val _size: Int by lazy {
            calcSize()
        }
        private val _rawData: ByteArray by lazy {
            buildRawData()
        }
        private val _string: String by lazy {
            buildString()
        }
        private val _hashCode: Int by lazy {
            calcHashCode()
        }

        protected abstract val rawFields: List<ByteArray>
        protected open fun calcSize() = rawFields.sumOf { it.size }

        override val length
            get() = _size

        protected open fun buildRawData(): ByteArray = rawFields.concatenate()

        override val rawData: ByteArray get() = _rawData.copyOf()

        protected open fun buildString() = buildString {
            append("[Header (")
            append(this@AbstractHeader.length)
            append(" bytes)]")
            append('\n')
        }

        override fun toString(): String = _string

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as AbstractHeader

            if (!rawData.contentEquals(other.rawData)) return false

            return true
        }

        protected open fun calcHashCode(): Int = rawData.contentHashCode()

        override fun hashCode(): Int = _hashCode
    }
}
