package com.github.andreypfau.kotlinio.bits

import kotlinx.cinterop.*

@PublishedApi
internal val unalignedAccessSupported = UNALIGNED_ACCESS_ALLOWED == 1

actual fun ByteArray.getShortAt(index: Int): Short = usePinned {
    if (unalignedAccessSupported) {
        val pointer = it.addressOf(index)
        pointer.reinterpret<ShortVar>().pointed.value.toBigEndian()
    } else {
        getShortSlowAt(index)
    }
}

actual fun ByteArray.getIntAt(index: Int): Int = usePinned {
    if (unalignedAccessSupported) {
        val pointer = it.addressOf(index)
        pointer.reinterpret<IntVar>().pointed.value.toBigEndian()
    } else {
        getIntSlowAt(index)
    }
}

actual fun ByteArray.getLongAt(index: Int): Long = usePinned {
    if (unalignedAccessSupported) {
        val pointer = it.addressOf(index)
        pointer.reinterpret<LongVar>().pointed.value.toBigEndian()
    } else {
        getLongSlowAt(index)
    }
}

actual fun ByteArray.setShortAt(index: Int, value: Short) = usePinned {
    if (unalignedAccessSupported) {
        val pointer = it.addressOf(index)
        pointer.reinterpret<ShortVar>().pointed.value = value.toBigEndian()
    } else {
        setShortSlowAt(index, value)
    }
}

actual fun ByteArray.setIntAt(index: Int, value: Int) = usePinned {
    if (unalignedAccessSupported) {
        val pointer = it.addressOf(index)
        pointer.reinterpret<IntVar>().pointed.value = value.toBigEndian()
    } else {
        setIntSlowAt(index, value)
    }
}

actual fun ByteArray.setLongAt(index: Int, value: Long) = usePinned {
    if (unalignedAccessSupported) {
        val pointer = it.addressOf(index)
        pointer.reinterpret<LongVar>().pointed.value = value.toBigEndian()
    } else {
        setLongSlowAt(index, value)
    }
}
