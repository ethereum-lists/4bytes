package org.ethereum.lists.methodsignatures

import org.kethereum.abi.model.EthereumFunction
import org.kethereum.methodsignatures.FileBackedMethodSignatureStore
import org.kethereum.methodsignatures.toHexSignature
import org.kethereum.methodsignatures.toTextMethodSignature
import java.io.File

class BlacklistAwareFileBackedMethodSignatureStore(blackListFile: File,
                                                   val fileBackedStore: FileBackedMethodSignatureStore) {

    private val blackList by lazy { blackListFile.readLines().toSet() }

    fun upsert(signatureHash: String, signatureText: String) = if (blackList.contains(signatureText)) {
        false
    } else {
        fileBackedStore.upsert(signatureHash, signatureText)
    }

    fun upsert(function: EthereumFunction) = function.toTextMethodSignature().let {
        upsert(it.toHexSignature().hex,it.normalizedSignature)
    }

}