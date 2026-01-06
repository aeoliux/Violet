package com.github.aeoliux.violet.utils

internal val crcTable = (0u..<256u).fold(Array(256) { 0u }) { crc, i ->
    val rem = (0..<8).fold(i) { rem, _  ->
        if ((rem and 1u) != 0u)
            (rem shr 1) xor 0xEDB88320u
        else
            rem shr 1
    }

    crc[i.toInt()] = rem
    crc
}

fun crc32(data: ByteArray): UInt =
    (data.fold(0u.inv()) { crc, char ->
        crcTable[((crc and 0xFFu).toUByte() xor char.toUByte()).toInt()] xor (crc shr 8)
    }).inv()