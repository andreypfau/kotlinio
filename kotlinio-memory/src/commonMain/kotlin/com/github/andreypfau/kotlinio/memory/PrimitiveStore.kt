package com.github.andreypfau.kotlinio.memory

expect inline fun Memory.storeShortAt(offset: Int, value: Short)

expect inline fun Memory.storeShortAt(offset: Long, value: Short)

inline fun Memory.storeUShortAt(offset: Int, value: UShort) = storeShortAt(offset, value.toShort())

inline fun Memory.storeUShortAt(offset: Long, value: UShort) = storeShortAt(offset, value.toShort())

expect inline fun Memory.storeIntAt(offset: Int, value: Int)

expect inline fun Memory.storeIntAt(offset: Long, value: Int)

inline fun Memory.storeUIntAt(offset: Int, value: UInt) = storeIntAt(offset, value.toInt())

inline fun Memory.storeUIntAt(offset: Long, value: UInt) = storeIntAt(offset, value.toInt())

expect inline fun Memory.storeLongAt(offset: Int, value: Long)

expect inline fun Memory.storeLongAt(offset: Long, value: Long)

inline fun Memory.storeULongAt(offset: Int, value: ULong) = storeLongAt(offset, value.toLong())

inline fun Memory.storeULongAt(offset: Long, value: ULong) = storeLongAt(offset, value.toLong())

expect inline fun Memory.storeFloatAt(offset: Int, value: Float)

expect inline fun Memory.storeFloatAt(offset: Long, value: Float)

expect inline fun Memory.storeDoubleAt(offset: Int, value: Double)

expect inline fun Memory.storeDoubleAt(offset: Long, value: Double)
