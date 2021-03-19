package org.ethereum.lists.methodsignatures

import org.kethereum.abi.EthereumABI
import org.kethereum.abi.getAllFunctions
import org.kethereum.methodsignatures.toTextMethodSignature
import java.io.File

fun main() {
    import()
}

private fun import() {
    File("/home/ligi/git/etherscan_contract_analyzer/contracts/1").walk().filter { it.extension == "abi" }.forEach { abi ->
        val json = abi.readText()

        EthereumABI(json).methodList.getAllFunctions().forEach { signature ->
            if (store.upsert(signature)) {
                println("found new signature: " + signature.toTextMethodSignature().normalizedSignature)
            }

        }
    }
}
