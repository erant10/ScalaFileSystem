package com.rdjvm.scala.oop.commands
import com.rdjvm.scala.oop.files.{Directory, File}
import com.rdjvm.scala.oop.filesystem.State

import scala.annotation.tailrec

class Echo(args: Array[String]) extends Command {
  override def apply(state: State): State = {

    if (args.isEmpty) state
    else if (args.length == 1) state.setMessage(args.head)
    else {
      val operator = args(args.length - 2)
      val filename = args(args.length - 1)
      val contents = createContents(args, args.length - 2)

      if (">>".equals(operator)) doEcho(state, contents, filename, append = true)
      else if (">".equals(operator)) doEcho(state, contents, filename)
      else state.setMessage(createContents(args, args.length))

    }
  }

  def getRootAfterEcho(currentDirectory: Directory, path: List[String], contents: String, append: Boolean): Directory =
  {
    if (path.isEmpty) currentDirectory
    else if (path.tail.isEmpty) {
      val dirEntry = currentDirectory.findEntry(path.head)
      if (dirEntry == null)
        currentDirectory.addEntry(new File(currentDirectory.path, path.head, contents))
      else if (dirEntry.isDirectory) currentDirectory
      else
        currentDirectory.replaceEntry(path.head, dirEntry.asFile.updateContents(contents, append))
    } else {
      // find the next directory to navigate
      val nextDirectory = currentDirectory.findEntry(path.head).asDirectory
      val newNextDirectory = getRootAfterEcho(nextDirectory, path.tail, contents, append)

      if (newNextDirectory == nextDirectory) currentDirectory
      else currentDirectory.replaceEntry(path.head, newNextDirectory)
    }
  }

  def doEcho(state: State, contents: String, filename: String, append: Boolean = false): State = {
    if (filename.contains(Directory.SEPARATOR))
      state.setMessage("filename must not contain separators")
    else {
      val newRoot: Directory = getRootAfterEcho(state.root, state.wd.getAllFoldersInPath :+ filename, contents, append)
      if (newRoot == state.root)
        state.setMessage(s"$filename: no such file")
      else
        State(newRoot, newRoot.findDescendant(state.wd.getAllFoldersInPath))
    }
  }

  def createContents(args: Array[String], topIndex: Int): String = {

    @tailrec
    def createContentHelper(currentindex: Int, accumelator: String): String =
      if (currentindex >= topIndex) accumelator
      else createContentHelper(currentindex+1, accumelator + " " + args(currentindex))

    createContentHelper(0, "")
  }

}
