package com.github.andreypfau.kotlinio.memory

expect inline fun Memory.loadShortAt(offset: Int): Short

expect inline fun Memory.loadShortAt(offset: Long): Short

inline fun Memory.loadUShortAt(offset: Int): UShort = loadShortAt(offset).toUShort()

inline fun Memory.loadUShortAt(offset: Long): UShort = loadShortAt(offset).toUShort()

expect inline fun Memory.loadIntAt(offset: Int): Int

expect inline fun Memory.loadIntAt(offset: Long): Int

inline fun Memory.loadUIntAt(offset: Int): UInt = loadIntAt(offset).toUInt()

inline fun Memory.loadUIntAt(offset: Long): UInt = loadIntAt(offset).toUInt()

expect inline fun Memory.loadLongAt(offset: Int): Long

expect inline fun Memory.loadLongAt(offset: Long): Long

inline fun Memory.loadULongAt(offset: Int): ULong = loadLongAt(offset).toULong()

inline fun Memory.loadULongAt(offset: Long): ULong = loadLongAt(offset).toULong()

expect inline fun Memory.loadFloatAt(offset: Int): Float

expect inline fun Memory.loadFloatAt(offset: Long): Float

expect inline fun Memory.loadDoubleAt(offset: Int): Double

expect inline fun Memory.loadDoubleAt(offset: Long): Double
