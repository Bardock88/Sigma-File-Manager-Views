package me.safarov399.home

import me.safarov399.domain.models.adapter.FileFolderModel
import me.safarov399.domain.models.adapter.FileModel
import me.safarov399.domain.models.adapter.FolderModel

class MockData {
    companion object {
        val file1 = FileModel(name = "Fluid Mechanics.pdf", size = 981921)
        val file2 = FileModel(name = "Fluid Mechanics.pdf", size = 981921)
        val file3 = FileModel(name = "Fluid Mechanics.pdf", size = 981921)
        val file4 = FileModel(name = "Fluid Mechanics.pdf", size = 981921)
        val file5 = FileModel(name = "Fluid Mechanics.pdf", size = 981921)
        val file6 = FileModel(name = "Fluid Mechanics.pdf", size = 981921)
        val file7 = FileModel(name = "Fluid Mechanics.pdf", size = 981921)
        val file8 = FileModel(name = "Fluid Mechanics.pdf", size = 981921)
        val file9 = FileModel(name = "Fluid Mechanics.pdf", size = 981921)
        val file10 = FileModel(name = "Fluid Mechanics.pdf", size = 981921)

        val folder1 = FolderModel(name = "Android", itemCount = 3)
        val folder2 = FolderModel(name = "Android", itemCount = 3)
        val folder3 = FolderModel(name = "Android", itemCount = 3)
        val folder4 = FolderModel(name = "Android", itemCount = 3)
        val folder5 = FolderModel(name = "Android", itemCount = 3)
        val folder6 = FolderModel(name = "Android", itemCount = 3)
        val folder7 = FolderModel(name = "Android", itemCount = 3)
        val folder8 = FolderModel(name = "Android", itemCount = 3)
        val folder9 = FolderModel(name = "Android", itemCount = 3)

        val list = listOf<FileFolderModel>(file1, file2, file3,file1, file2, file3,file1, file2, file3,file1, file2, file3, folder1,folder1,folder1,folder1,folder1,folder1,folder1,folder1,folder1,)
    }
}