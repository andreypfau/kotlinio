@file:Suppress("NOTHING_TO_INLINE")

package com.github.andreypfau.kotlinio.memory

actual inline fun Memory.loadShortAt(offset: Int): Short = view.getInt16(offset, false)

actual inline fun Memory.loadShortAt(offset: Long): Short = view.getInt16(offset.toIntOrFail("offset"), false)

actual inline fun Memory.loadIntAt(offset: Int): Int = view.getInt32(offset, false)

actual inline fun Memory.loadIntAt(offset: Long): Int = view.getInt32(offset.toIntOrFail("offset"), false)

actual inline fun Memory.loadLongAt(offset: Int): Long =
    (view.getUint32(offset, false).toLong() shl 32) or view.getUint32(offset + 4, false).toLong()

actual inline fun Memory.loadLongAt(offset: Long): Long = loadLongAt(offset.toIntOrFail("offset"))

actual inline fun Memory.loadFloatAt(offset: Int): Float = view.getFloat32(offset, false)

actual inline fun Memory.loadFloatAt(offset: Long): Float = view.getFloat32(offset.toIntOrFail("offset"), false)

actual inline fun Memory.loadDoubleAt(offset: Int): Double = view.getFloat64(offset, false)

actual inline fun Memory.loadDoubleAt(offset: Long): Double = view.getFloat64(offset.toIntOrFail("offset"), false)
