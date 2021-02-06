package org.ethereum.lists.methodsignatures

import okio.GzipSource
import okio.buffer
import okio.source
import org.kethereum.methodsignatures.FileBackedMethodSignatureStore
import java.io.File
import java.math.BigDecimal
import java.math.BigInteger
import java.math.BigInteger.ZERO
import java.math.MathContext

fun main() {
    val store = FileBackedMethodSignatureStore(signatureDirectory)

    var found = 0
    var missed = 0

    var missedCalls = ZERO
    var foundCalls = ZERO

    val bufferedSource = GzipSource(File("signatures_sorted_counted.lst.gz").source()).buffer()
    while (true) {
        val line = bufferedSource.readUtf8Line()
        if (line != null) {
            val (count, signatureInfo) = line.trim().split(" ")
            val (signature, _) = signatureInfo.split("-")
            val countBigInt = BigInteger(count)
            if (store.has(signature.replace("0x", ""))) {
                found++
                foundCalls += countBigInt
            } else {
                missed++
                missedCalls += countBigInt
            }
        } else {
            break
        }

    }
    val ONE_HUNDRED = BigDecimal(100)

    val totalSignatures = found + missed

    val foundSignaturesPercent = found.toBigDecimal().divide(totalSignatures.toBigDecimal(), MathContext.DECIMAL32)* ONE_HUNDRED

    println("signatures: found $found signatures ($foundSignaturesPercent%) - missed $missed")

    val totalCalls = foundCalls + missedCalls
    val foundPercent = foundCalls.toBigDecimal().divide(totalCalls.toBigDecimal(), MathContext.DECIMAL32)* ONE_HUNDRED

    println("calls: found $foundCalls ($foundPercent) - missed $missedCalls")
}