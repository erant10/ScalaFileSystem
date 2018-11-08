package com.rdjvm.scala.oop.commands
import com.rdjvm.scala.oop.files.Directory
import com.rdjvm.scala.oop.filesystem.State

class Rm(name: String) extends Command {
  def apply(state: State): State = {
    // get working directory
    val wd = state.wd

    // get the absolute path
    val absolutePath = if (name.startsWith(Directory.SEPARATOR)) name
    else s"${wd.path}${if (wd.isRoot) "" else Directory.SEPARATOR}$name"

    // do some checks
    if (Directory.ROOT_PATH.equals(absolutePath)) state.setMessage("rm of root path is not supported")
    else {
      doRm(state, absolutePath)
    }

  }
  def doRm(state: State, path: String): State = {

    def rmHelper(currentDirectory: Directory, path: List[String]): Directory = {
      if (path.isEmpty) currentDirectory
      else if (path.tail.isEmpty) currentDirectory.removeEntry(path.head)
      else {
        val nextDirectory = currentDirectory.findEntry(path.head)
        if (!nextDirectory.isDirectory) currentDirectory
        else {
          val newNextDirectory = rmHelper(nextDirectory.asDirectory, path.tail)
          if (newNextDirectory == nextDirectory) currentDirectory
          else currentDirectory.replaceEntry(path.head, newNextDirectory)
        }
      }
    }

    // find the entry to remove & update structure
    val tokens = Directory.tokenizePath(path)

    val newRoot: Directory = rmHelper(state.root, tokens)

    // if the root directory did not change that means that remove has failed
    if (newRoot == state.root) state.setMessage(s"$path: no such file or directory")
    else State(newRoot, newRoot.findDescendant(state.wd.path.substring(1)))
  }
}
