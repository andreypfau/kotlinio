@file:Suppress("NOTHING_TO_INLINE")

package com.github.andreypfau.kotlinio.memory

actual inline fun Memory.loadShortAt(offset: Int): Short = buffer.getShort(offset)

actual inline fun Memory.loadShortAt(offset: Long): Short = buffer.getShort(offset.toIntOrFail("offset"))

actual inline fun Memory.loadIntAt(offset: Int): Int = buffer.getInt(offset)

actual inline fun Memory.loadIntAt(offset: Long): Int = buffer.getInt(offset.toIntOrFail("offset"))

actual inline fun Memory.loadLongAt(offset: Int): Long = buffer.getLong(offset)

actual inline fun Memory.loadLongAt(offset: Long): Long = buffer.getLong(offset.toIntOrFail("offset"))

actual inline fun Memory.loadFloatAt(offset: Int): Float = buffer.getFloat(offset)

actual inline fun Memory.loadFloatAt(offset: Long): Float = buffer.getFloat(offset.toIntOrFail("offset"))

actual inline fun Memory.loadDoubleAt(offset: Int): Double = buffer.getDouble(offset)

actual inline fun Memory.loadDoubleAt(offset: Long): Double = buffer.getDouble(offset.toIntOrFail("offset"))
