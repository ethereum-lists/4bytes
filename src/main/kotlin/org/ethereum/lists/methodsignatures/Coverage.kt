package org.ethereum.lists.methodsignatures

import okhttp3.internal.io.FileSystem.SYSTEM
import okio.GzipSource
import okio.Okio.buffer
import org.kethereum.methodsignatures.FileBackedMethodSignatureStore
import java.io.File

fun main(args: Array<String>) {
    val store = FileBackedMethodSignatureStore(signatureDirectory)

    var found = 0
    var missed = 0

    val bufferedSource = buffer(GzipSource(SYSTEM.source(File("signatures_sorted_counted.lst.gz"))))
    while (true) {
        val line = bufferedSource.readUtf8Line()
        if (line != null) {
            val (count, signatureInfo) = line.trim().split(" ")
            val (signature, calldataLength) = signatureInfo.split("-")
            if (store.has(signature.replace("0x", ""))) {
                found++
            } else {
                missed++
            }
        } else {
            break
        }

    }
    println("finished - found $found - missed $missed")
}