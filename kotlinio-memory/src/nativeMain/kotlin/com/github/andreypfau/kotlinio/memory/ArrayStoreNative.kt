package com.github.andreypfau.kotlinio.memory

import kotlinx.cinterop.*

actual fun Memory.storeShortArray(
    offset: Int,
    source: ShortArray,
    sourceOffset: Int,
    count: Int
) = storeShortArray(offset.toLong(), source, sourceOffset, count)

actual fun Memory.storeShortArray(
    offset: Long,
    source: ShortArray,
    sourceOffset: Int,
    count: Int
) {
    storeArrayIndicesCheck(offset, sourceOffset, count, 2L, source.size, size)
    if (count == 0) return

    if (!Platform.isLittleEndian) {
        copy(source, pointer.plus(offset)!!, sourceOffset, count)
    } else if (Platform.canAccessUnaligned || isAlignedShort(offset)) {
        val destination = pointer.plus(offset)!!.reinterpret<ShortVar>()

        for (index in 0 until count) {
            destination[index] = source[index + sourceOffset].reverseByteOrder()
        }
    } else {
        val destination = pointer.plus(offset)!!

        for (index in 0 until count) {
            storeShortSlowAt(destination.plus(index * 2)!!, source[index + sourceOffset])
        }
    }
}

actual fun Memory.storeIntArray(
    offset: Int,
    source: IntArray,
    sourceOffset: Int,
    count: Int
) = storeIntArray(offset.toLong(), source, sourceOffset, count)

actual fun Memory.storeIntArray(
    offset: Long,
    source: IntArray,
    sourceOffset: Int,
    count: Int
) {
    storeArrayIndicesCheck(offset, sourceOffset, count, 4L, source.size, size)
    if (count == 0) return

    if (!Platform.isLittleEndian) {
        copy(source, pointer.plus(offset)!!, sourceOffset, count)
    } else if (Platform.canAccessUnaligned || isAlignedInt(offset)) {
        val destination = pointer.plus(offset)!!.reinterpret<IntVar>()

        for (index in 0 until count) {
            destination[index] = source[index + sourceOffset].reverseByteOrder()
        }
    } else {
        val destination = pointer.plus(offset)!!

        for (index in 0 until count) {
            storeIntSlowAt(destination.plus(index * 4)!!, source[index + sourceOffset])
        }
    }
}

actual fun Memory.storeLongArray(
    offset: Int,
    source: LongArray,
    sourceOffset: Int,
    count: Int
) = storeLongArray(offset.toLong(), source, sourceOffset, count)

actual fun Memory.storeLongArray(
    offset: Long,
    source: LongArray,
    sourceOffset: Int,
    count: Int
) {
    storeArrayIndicesCheck(offset, sourceOffset, count, 8L, source.size, size)
    if (count == 0) return

    if (!Platform.isLittleEndian) {
        copy(source, pointer.plus(offset)!!, sourceOffset, count)
    } else if (Platform.canAccessUnaligned || isAlignedShort(offset)) {
        val destination = pointer.plus(offset)!!.reinterpret<LongVar>()

        for (index in 0 until count) {
            destination[index] = source[index + sourceOffset].reverseByteOrder()
        }
    } else {
        val destination = pointer.plus(offset)!!

        for (index in 0 until count) {
            storeLongSlowAt(destination.plus(index * 8L)!!, source[index + sourceOffset])
        }
    }
}

actual fun Memory.storeFloatArray(
    offset: Int,
    source: FloatArray,
    sourceOffset: Int,
    count: Int
) = storeFloatArray(offset.toLong(), source, sourceOffset, count)

actual fun Memory.storeFloatArray(
    offset: Long,
    source: FloatArray,
    sourceOffset: Int,
    count: Int
) {
    storeArrayIndicesCheck(offset, sourceOffset, count, 4L, source.size, size)
    if (count == 0) return

    if (!Platform.isLittleEndian) {
        copy(source, pointer.plus(offset)!!, sourceOffset, count)
    } else if (Platform.canAccessUnaligned || isAlignedInt(offset)) {
        val destination = pointer.plus(offset)!!.reinterpret<FloatVar>()

        for (index in 0 until count) {
            destination[index] = source[index + sourceOffset].reverseByteOrder()
        }
    } else {
        val destination = pointer.plus(offset)!!

        for (index in 0 until count) {
            storeFloatSlowAt(destination.plus(index * 4)!!, source[index + sourceOffset])
        }
    }
}

actual fun Memory.storeDoubleArray(
    offset: Int,
    source: DoubleArray,
    sourceOffset: Int,
    count: Int
) = storeDoubleArray(offset.toLong(), source, sourceOffset, count)

actual fun Memory.storeDoubleArray(
    offset: Long,
    source: DoubleArray,
    sourceOffset: Int,
    count: Int
) {
    storeArrayIndicesCheck(offset, sourceOffset, count, 8L, source.size, size)
    if (count == 0) return

    if (!Platform.isLittleEndian) {
        copy(source, pointer.plus(offset)!!, sourceOffset, count)
    } else if (Platform.canAccessUnaligned || isAlignedShort(offset)) {
        val destination = pointer.plus(offset)!!.reinterpret<DoubleVar>()

        for (index in 0 until count) {
            destination[index] = source[index + sourceOffset].reverseByteOrder()
        }
    } else {
        val destination = pointer.plus(offset)!!

        for (index in 0 until count) {
            storeDoubleSlowAt(destination.plus(index * 8L)!!, source[index + sourceOffset])
        }
    }
}
