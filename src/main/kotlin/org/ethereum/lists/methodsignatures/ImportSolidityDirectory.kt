package org.ethereum.lists.methodsignatures

import org.kethereum.methodsignatures.FileBackedMethodSignatureStore
import org.kethereum.methodsignatures.model.TextMethodSignature
import org.kethereum.methodsignatures.toHexSignature
import java.io.File

val textSignatureDirectoryWithParameterNames = File("with_parameter_names").apply {
    mkdir()
}

fun main(args: Array<String>) {

    if (args.isEmpty()) {
        error("importPath not specified :-)\nPlease execute like this: ./gradlew importFromSolidityDirectory -PimportPath=/path/to/your/solidity/files")
    }

    val importPath = File(args[0])

    if (!importPath.isDirectory) {
        error("importPath ($importPath) is not a directory")
    }

    val textStore = FileBackedMethodSignatureStore(textSignatureDirectoryWithParameterNames)

    importPath.listFiles()?.forEach { solidity_file ->
        solidity_file.reader().readText().lines().forEach { code ->
            val cleanCode = code.replace(" ", "").replace("\t", "") // ^^-)
            if (cleanCode.startsWith("function")) {

                val codeAfterFunction = code.substringAfter("function")
                val functionName = codeAfterFunction.substringBefore("(").trim()
                val functionParameters = code.substringAfter("(").substringBefore(")").trim()
                val paramsSplit = functionParameters.split(",").map { it.trim() }
                val functionParametersClean = paramsSplit.joinToString(",")
                val functionParametersSignature = paramsSplit.joinToString(",") { it.split(" ").first().trim() }
                val textMethodSignature = TextMethodSignature("$functionName($functionParametersSignature)")
                val hexSignature = textMethodSignature.toHexSignature()

                if (textMethodSignature.functionName.isNotBlank()) { // constructor
                    textStore.upsert(hexSignature.hex, "$functionName($functionParametersClean)")

                    store.upsert(hexSignature.hex, textMethodSignature.normalizedSignature)
                }
            }
        }
    }
}
