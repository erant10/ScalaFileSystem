package com.rdjvm.scala.oop.files

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
    path.substring(1).split(Directory.SEPARATOR).toList.filter(x => !x.isEmpty)

  def findDescendant(dirs: List[String]): Directory =
    if (dirs.isEmpty) this
    else findEntry(dirs.head).asDirectory.findDescendant(dirs.tail)

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

  def asDirectory: Directory = this

  def getType: String = "Directory"
}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  // creates an empty Directory with @parentPath and @name
  def empty(parentPath: String, name: String): Directory = new Directory(parentPath, name, List())

  def ROOT: Directory = Directory.empty("", "")
}