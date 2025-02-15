package me.safarov399.data.repo

import me.safarov399.domain.repo.AbstractFileFolderOperationRepository
import java.io.File

class FileFolderOperationRepository : AbstractFileFolderOperationRepository {

    override fun view(path: String) {
    }

    override fun verifySignature(path: String) {
    }

    override fun checkDependencies(path: String) {
    }

    override fun properties(path: String) {
    }

    override fun compress(path: List<String>) {
    }

    override fun delete(paths: List<String>): Boolean {
        var success = true
        for(path in paths) {
            val file = File(path)
            success = success && file.deleteRecursively()
        }
        return success
    }

    override fun shred(path: List<String>) {
    }

    override fun rename(path: String, newName: String) {
    }

    override fun copy(items: List<String>, oldPath: String, newPath: String) {
    }

    override fun move(items: List<String>, oldPath: String, newPath: String) {
    }

    override fun extract(path: String) {
    }

    override fun checksum(path: String) {
    }

    override fun changePermissions(path: String) {
    }

}