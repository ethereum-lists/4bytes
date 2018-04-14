package org.ethereum.lists.methodsignatures

import org.kethereum.keccakshortcut.keccak
import org.kethereum.methodsignatures.FileBackedMethodSignatureStore
import org.walleth.khex.toNoPrefixHexString
import java.io.File

fun main(args: Array<String>) {
    var totalProcessed = 0
    var withUnderscore = 0
    var withCamelCase = 0
    var maxParams = 0
    var startsWiUpperCase = 0
    val store = FileBackedMethodSignatureStore(signatureDirectory)
    val methodSet = HashSet<String>()
    val paramSet = HashSet<String>()
    var jsonElements= arrayOf<String>()

    store.all().forEach { signatureHash ->
        val get = store.get(signatureHash)
        get.map { it.signature }.forEach { signatureText ->
            val methodName = signatureText.substringBefore("(")
            val params = signatureText
                    .substringAfter("(")
                    .substringBefore(")")
                    .split(",")
                    .filter { !it.isBlank() }
                    .toHashSet()
            methodSet.add(methodName)
            if (methodName.contains("_")) {
                withUnderscore++
            } else if (methodName != methodName.toLowerCase()) {
                withCamelCase++
            }
            if (methodName.first().toLowerCase() != methodName.first()) {
                startsWiUpperCase++
            }
            if (params.size > maxParams) {
                maxParams = params.size
            }
            val textHash = signatureText.toByteArray().keccak().toNoPrefixHexString()
            if (!textHash.startsWith(signatureHash)) {
                error("signature fail $signatureHash $signatureText $textHash")
            }
            val newParams = params.map { it.substringBefore("[") }.filter { it.isNotEmpty() }
            paramSet.addAll(newParams)
            totalProcessed++
            val jsonParams = params.map { "\"$it\"" }.joinToString()
            jsonElements+="{\"id\":\"$signatureHash\",\"name\":\"$methodName\",\"argumentType\":[$jsonParams]}"
        }
    }

    println("totalProcessed: $totalProcessed")
    println("unique method names: ${methodSet.size}")
    println("names with underscore: $withUnderscore")
    println("names with CamelCase: $withCamelCase")
    println("names starting with upper case char: $startsWiUpperCase")
    val max = methodSet.maxBy { it.length }?.length
    val min = methodSet.minBy { it.length }?.length
    println("min method name length: $min")
    println("max method name length: $max")
    val size = paramSet.size
    println("max param count: $maxParams")
    println("param types used: $size")
    println("param types: ${paramSet.joinToString()}")

    val outDir = File("output")
    outDir.mkdir()
    val outFile = File(outDir, "methods.json")
    outFile.createNewFile()
    outFile.writeText("[\n${jsonElements.joinToString (",\n")}\n]")

}
