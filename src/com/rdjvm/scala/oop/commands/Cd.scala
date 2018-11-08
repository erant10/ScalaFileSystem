package com.rdjvm.scala.oop.commands
import com.rdjvm.scala.oop.files.{DirEntry, Directory}
import com.rdjvm.scala.oop.filesystem.State

import scala.annotation.tailrec

class Cd(dir: String) extends Command {
  override def apply(state: State): State = {

    // find the root
    val root = state.root
    val wd = state.wd

    // find the absolute path of the directory I want to cd to
    val absolutePath =
      if (dir.startsWith(Directory.SEPARATOR)) dir
      else s"${wd.path}${if (wd.isRoot) "" else Directory.SEPARATOR}$dir"

    // find the directory to cd to, given the path
    val destinationDirectory = doFindEntry(root, absolutePath)

    // change the state given the directory
    if (destinationDirectory == null || !destinationDirectory.isDirectory)
      state.setMessage(s"$dir: No such directory")
    else
      State(root, destinationDirectory.asDirectory)

  }

  def doFindEntry(root: Directory, path: String): DirEntry = {

    @tailrec
    def findEntryHelper(currentDirectory: Directory, path: List[String]): DirEntry = {
      if (path.isEmpty || path.head.isEmpty) currentDirectory
      else {
        val nextDir = currentDirectory.findEntry(path.head)
        if (path.tail.isEmpty) nextDir
        else if (nextDir == null || !nextDir.isDirectory) null
        else findEntryHelper(nextDir.asDirectory, path.tail)
      }
    }

    @tailrec
    def collapseRelativeTokens(path: List[String], result: List[String]): List[String] = {
      if (path.isEmpty) result
      else if (".".equals(path.head)) collapseRelativeTokens(path.tail, result)
      else if ("..".equals(path.head)) {
        if (result.isEmpty) null
        else collapseRelativeTokens(path.tail, result.init)
      } else collapseRelativeTokens(path.tail, result :+ path.head)
    }

    // get tokens
    val tokens: List[String] = Directory.tokenizePath(path)

    // eliminate/collapse relative tokens
    val finalTokens = collapseRelativeTokens(tokens, List())

    // navigate to the correct entry
    if (finalTokens == null) null
    else findEntryHelper(root, finalTokens)
  }
}
