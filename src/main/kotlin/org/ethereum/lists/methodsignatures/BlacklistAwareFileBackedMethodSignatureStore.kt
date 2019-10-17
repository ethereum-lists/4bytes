package org.ethereum.lists.methodsignatures

import org.kethereum.methodsignatures.FileBackedMethodSignatureStore
import java.io.File

class BlacklistAwareFileBackedMethodSignatureStore(blackListFile: File,
                                                   val fileBackedStore: FileBackedMethodSignatureStore) {

    private val blackList by lazy { blackListFile.readLines().toSet() }

    fun upsert(signatureHash: String, signatureText: String) = if (blackList.contains(signatureHash)) {
        false
    } else {
        fileBackedStore.upsert(signatureHash, signatureText)
    }

}