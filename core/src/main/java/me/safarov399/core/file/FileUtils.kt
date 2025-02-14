package me.safarov399.core.file

fun fileAndPathMerger(fileName: String, path: String): String {
    if(path.last().toString() == "/") {
        return path + fileName
    }
    return "$path/$fileName"
}