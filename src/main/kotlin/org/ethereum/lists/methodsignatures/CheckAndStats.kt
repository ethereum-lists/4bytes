package org.ethereum.lists.methodsignatures

import org.kethereum.keccakshortcut.keccak
import org.walleth.khex.toNoPrefixHexString

fun main(args: Array<String>) {
    var totalProcessed = 0
    val store = FileBackedStore(signatureDirectory)
    val methodSet = HashSet<String>()
    val paramSet = HashSet<String>()

    store.all().forEach { signatureHash ->
        val get = store.get(signatureHash)
        get.forEach { signatureText ->
            val methodName = signatureText.substringBefore("(")
            val params = signatureText.substringAfter("(").substringBefore(")").split(",").toHashSet()
            methodSet.add(methodName)
            val textHash = signatureText.toByteArray().keccak().toNoPrefixHexString()
            if (!textHash.startsWith(signatureHash)) {
                error("signature fail $signatureHash $signatureText $textHash")
            }
            val newParams = params.map { it.substringBefore("[") }.filter { it.isNotEmpty() }
            paramSet.addAll(newParams)
            totalProcessed++
        }
    }

    println("totalProcessed: $totalProcessed")
    println("unique method names: ${methodSet.size}")
    val max = methodSet.maxBy { it.length }?.length
    val min = methodSet.minBy { it.length }?.length
    println("min method name length: $min")
    println("max method name length: $max")
    println("param types used: ${paramSet.size}")
    println("param types: ${paramSet.joinToString()}")

}
