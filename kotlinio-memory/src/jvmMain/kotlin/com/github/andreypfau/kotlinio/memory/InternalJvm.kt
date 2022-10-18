@file:Suppress("NOTHING_TO_INLINE")

package com.github.andreypfau.kotlinio.memory

import java.nio.ByteBuffer

internal fun ByteBuffer.sliceSafe(offset: Int, length: Int): ByteBuffer =
    duplicate().withOffset(offset).apply { limit(offset + length) }.slice()

internal inline fun ByteBuffer.withOffset(offset: Int): ByteBuffer =
    duplicate().apply { position(offset) }
