package com.rdjvm.scala.oop.files

import com.rdjvm.scala.oop.filesystem.FileSystemException

class File(override val parentPath: String, override val name: String, val contents: String) extends DirEntry(parentPath, name) {

  def asDirectory: Directory = throw new FileSystemException("A File cannot be converted to a directory")
  def asFile: File = this

  def isDirectory: Boolean = false
  def isFile: Boolean = true

  def getType: String = "File"

  def updateContents(newContents: String, append: Boolean): File = {
    new File(parentPath, name, contents = s"${if (append) contents+"\n" else ""}$newContents" )
  }
}

object File {
  def empty(parentPath:String, name: String): File =
    new File(parentPath, name, "")

}