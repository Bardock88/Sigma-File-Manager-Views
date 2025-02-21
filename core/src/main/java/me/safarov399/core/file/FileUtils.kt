package me.safarov399.core.file

import me.safarov399.core.storage.StorageConstants.INVALID_CHARS

fun fileAndPathMerger(fileName: String, path: String): String {
    if (path.last().toString() == "/") {
        return path + fileName
    }
    return "$path/$fileName"
}

fun isNameInvalid(name: String): Boolean {
    if (name.count { it == '/' } == 1 && name.endsWith("/")) {
        return false
    }
    return INVALID_CHARS.any { name.contains(it) }
}