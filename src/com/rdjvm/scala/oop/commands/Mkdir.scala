package com.rdjvm.scala.oop.commands
import com.rdjvm.scala.oop.files.{DirEntry, Directory}
import com.rdjvm.scala.oop.filesystem.State

class Mkdir(name: String) extends Command {
  override def apply(state: State): State = {
    val wd = state.wd
    if (wd.hasEntry(name)) {
      state.setMessage(s"Entry $name already exists")
    } else if (name.contains(Directory.SEPARATOR)) state.setMessage(s"$name must not contain separators")
    else if (checkIllegal(name)) state.setMessage(s"$name: illegal entry name")
    else {
      doMkdir(state, name)
    }
  }

  def updateStructure(currentDir: Directory, path: List[String], newEntry: DirEntry): Directory = {
    if (path.isEmpty) currentDir.addEntry(newEntry)
    else {
      val oldEntry = currentDir.findEntry(path.head).asDirectory
      currentDir.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
    }
  }

  def checkIllegal(str: String) : Boolean = {
    str.contains(".")
  }

  def doMkdir(state: State, name: String): State = {
    val wd = state.wd

    // get all the directories in the fullpath
    val dirsInPath = wd.getAllFoldersInPath

    // create new directory entry in the wd
    val newDir = Directory.empty(wd.path, name)

    // update the whole directory structure starting from the root
    val newRoot = updateStructure(state.root, dirsInPath, newDir)

    // find new wd INSTANCE given wd's full path in the new directory structure
    val newWd = newRoot.findDescendant(dirsInPath)

    State(newRoot, newWd)
  }
}
