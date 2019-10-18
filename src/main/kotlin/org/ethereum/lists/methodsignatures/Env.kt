package org.ethereum.lists.methodsignatures

import org.kethereum.methodsignatures.FileBackedMethodSignatureStore
import java.io.File

val signatureDirectory = File("signatures")


val outDir = signatureDirectory.apply { mkdirs() }

val store = BlacklistAwareFileBackedMethodSignatureStore(File("black.lst"), FileBackedMethodSignatureStore(outDir))
