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

    val store=FileBackedMethodSignatureStore(textSignatureDirectoryWithParameterNames)

    importPath.listFiles().forEach { solidity_file ->
        solidity_file.reader().readText().lines().forEach { code ->
            val cleanCode = code.replace(" ","").replace("\t","") // ^^-)
            if (cleanCode.startsWith("function")) {
                val codeAfterFunction = code.substringAfter("function")
                val functionName = codeAfterFunction.substringBefore("(").trim()
                val functionParameters = code.substringAfter("(").substringBefore(")").trim()
                val functionParametersClean = functionParameters.split(",").joinToString(",") { it.trim() }
                val functionParametersSignature = functionParameters.split(",").joinToString(",") { it.split(" ").first().trim() }
                val hexSignature= TextMethodSignature("$functionName($functionParametersSignature)").toHexSignature()
                store.upsert(hexSignature.hex,"$functionName($functionParametersClean)")
            }
        }
    }
}
