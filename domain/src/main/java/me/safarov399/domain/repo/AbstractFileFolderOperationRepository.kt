package me.safarov399.domain.repo

interface AbstractFileFolderOperationRepository {
    fun view(path: String)
    fun verifySignature(path: String)
    fun checkDependencies(path: String)
    fun properties(path: String)
    fun compress(path: List<String>)
    fun delete(path: List<String>)
    fun shred(path: List<String>)
    fun rename(path: String, newName: String)
    fun copy(items:List<String>, oldPath: String, newPath: String)
    fun move(items:List<String>, oldPath: String, newPath: String)
    fun extract(path: String)
    fun checksum(path: String)
    fun changePermissions(path: String)
}