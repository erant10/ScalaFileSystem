package com.rdjvm.scala.oop.files

import com.rdjvm.scala.oop.filesystem.FileSystemException

import scala.annotation.tailrec

/**
  *
  * @param parentPath - the path of the parent
  * @param name - the name of the directory
  * @param contents - a list of entries inside the directory
  */
class Directory(override val parentPath: String, override val name: String, val contents: List[DirEntry])
  extends DirEntry(parentPath, name) {

  def hasEntry(name: String): Boolean = findEntry(name) != null

  def getAllFoldersInPath: List[String] =
    Directory.tokenizePath(path).filter(x => !x.isEmpty)

  def findDescendant(dirs: List[String]): Directory =
    if (dirs.isEmpty) this
    else findEntry(dirs.head).asDirectory.findDescendant(dirs.tail)

  def findDescendant(relativePath: String): Directory =
    if (relativePath.isEmpty) this
    else findDescendant(Directory.tokenizePath(relativePath, true))

  def removeEntry(entryName: String): Directory =
    if (!hasEntry(entryName)) this
    else new Directory(parentPath, name, contents.filter(x => !x.name.equals(entryName)))

  def addEntry(newEntry: DirEntry): Directory =
    new Directory(parentPath, name, contents :+ newEntry)

  // find a dirEntry in the current directory's content
  def findEntry(entryName: String): DirEntry = {
    @tailrec
    def findEntryHelper(name: String, contentList: List[DirEntry]): DirEntry =
      if (contentList.isEmpty) null
      else if (contentList.head.name.equals(name)) contentList.head
      else findEntryHelper(name, contentList.tail)

    findEntryHelper(entryName, contents)
  }

  def replaceEntry(entryName: String, newEntry: DirEntry): Directory =
    new Directory(parentPath, name, contents.filter(e => !e.name.equals(entryName)) :+ newEntry)

  def isRoot: Boolean = parentPath.isEmpty


  def isDirectory: Boolean = true
  def isFile: Boolean = false

  def asDirectory: Directory = this
  def asFile: File = throw new FileSystemException("A Directory cannot be converted to a File")

  def getType: String = "Directory"

}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  // creates an empty Directory with @parentPath and @name
  def empty(parentPath: String, name: String): Directory = new Directory(parentPath, name, List())

  def ROOT: Directory = Directory.empty("", "")

  def tokenizePath(path: String, isRelative: Boolean = false): List[String] =
    path.substring(if (isRelative) 0 else 1).split(Directory.SEPARATOR).toList

}