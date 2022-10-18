package com.github.andreypfau.kotlinio.memory

internal val isLittleEndianPlatform = ByteOrder.native === ByteOrder.LITTLE_ENDIAN
