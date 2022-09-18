package com.github.andreypfau.network.packet.dns

import com.github.andreypfau.network.utils.getUShort
import com.github.andreypfau.network.utils.toByteArray
import kotlin.experimental.or
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic

@JvmSynthetic
inline fun DnsDomainName(rawData: ByteArray, offset: Int = 0, length: Int = rawData.size - offset): DnsDomainName =
    DnsDomainName.newInstance(rawData, offset, length)

@JvmSynthetic
inline fun DnsDomainName(builder: DnsDomainName.Builder.() -> Unit): DnsDomainName =
    DnsDomainName.Builder().apply(builder).build()

interface DnsDomainName {
    val labels: List<String>
    val name: String
    val pointer: UShort?
    val length: Int

    fun builder(): Builder = Builder(labels.toMutableList(), pointer)

    fun toByteArray() = toByteArray(ByteArray(length))

    fun toByteArray(buf: ByteArray, offset: Int = 0): ByteArray

    companion object {
        @JvmStatic
        fun newInstance(
            rawData: ByteArray,
            offset: Int = 0,
            length: Int = rawData.size - offset
        ): DnsDomainName = ByteBacked(rawData, offset, length)

        @JvmStatic
        fun create(builder: Builder.() -> Unit): DnsDomainName = Builder().apply(builder).build()
    }

    data class Builder(
        var labels: MutableList<String> = ArrayList(),
        var pointer: UShort? = null
    ) {
        fun build(): DnsDomainName = FieldBacked(
            labels.toList(), pointer
        )
    }

    private class ByteBacked(
        rawData: ByteArray,
        offset: Int,
        length: Int
    ) : AbstractDnsDomainName() {
        override val labels: List<String>
        override val pointer: UShort?

        init {
            var cursor = 0
            var foundPointer: UShort? = null
            var terminated = false
            val labels = ArrayList<String>()

            while (cursor < length) {
                val len = rawData[offset + cursor].toUByte().toInt()
                val flag = len and 0xC0
                if (flag == 0x00) {
                    if (len == 0) {
                        terminated = true
                        break
                    }

                    cursor++
                    require(length - cursor >= len) { "The data is too short to build a DnsDomainName" }
                    val labelOffset = offset + cursor
                    labels.add(rawData.decodeToString(labelOffset, labelOffset + len))
                    cursor += len
                } else if (flag == 0xC0) {
                    require(length - cursor >= Short.SIZE_BYTES) { "The data is too short to build a DnsDomainName" }
                    foundPointer = rawData.getUShort(offset + cursor) and 0x3FFFu
                    terminated = true
                    break
                } else {
                    throw IllegalArgumentException("A label must start with 00 or 11")
                }
            }

            if (!terminated) {
                throw IllegalArgumentException("No null termination nor pointer")
            }
            this.labels = labels
            this.pointer = foundPointer
        }

        override fun toByteArray(buf: ByteArray, offset: Int): ByteArray {
            var cursor = offset
            labels.forEach { label ->
                val labelBytes = label.encodeToByteArray()
                buf[cursor] = labelBytes.size.toByte()
                cursor++
                labelBytes.copyInto(buf, cursor)
                cursor += labelBytes.size
            }
            val pointer = pointer
            if (pointer != null) {
                val offsetBytes = pointer.toByteArray()
                offsetBytes[0] = (offsetBytes[0].toInt() or 0xC0).toByte()
                offsetBytes.copyInto(buf, cursor)
            }
            return buf
        }
    }

    private class FieldBacked(
        override val labels: List<String>,
        override val pointer: UShort?
    ) : AbstractDnsDomainName() {
        override fun toByteArray(buf: ByteArray, offset: Int): ByteArray {
            var cursor = 0
            labels.forEach { label ->
                val labelBytes = label.encodeToByteArray()
                buf[offset + cursor] = labelBytes.size.toByte()
                cursor++
                labelBytes.copyInto(buf, offset + cursor)
                cursor += labelBytes.size
            }
            val pointer = pointer
            if (pointer != null) {

                pointer.toByteArray(buf, offset + cursor)

                buf[offset + cursor] = buf[offset + cursor] or 0xC0.toByte()

            }
            return buf
        }
    }

    private abstract class AbstractDnsDomainName : DnsDomainName {
        abstract override val pointer: UShort?
        override val name: String by lazy {
            labels.joinToString(".")
        }

        override val length by lazy {
            var len = labels.sumOf { it.length + 1 }
            if (pointer != null) {
                len += 2
            } else {
                len++
            }
            len
        }

        private val _string by lazy {
            if (labels.isEmpty() && pointer == null) {
                "<ROOT>"
            } else if (pointer == null) {
                name
            } else {
                "[name: $name, pointer: $pointer]"
            }
        }

        private val _hashCode by lazy {
            var result = pointer?.hashCode() ?: 0
            result = 31 * result + name.hashCode()
            result
        }

        override fun toString(): String = _string

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is AbstractDnsDomainName) return false

            if (pointer != other.pointer) return false
            if (name != other.name) return false

            return true
        }

        override fun hashCode(): Int = _hashCode
    }
}
