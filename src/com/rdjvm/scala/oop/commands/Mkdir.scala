package com.rdjvm.scala.oop.commands
import com.rdjvm.scala.oop.files.{DirEntry, Directory}
import com.rdjvm.scala.oop.filesystem.State

class Mkdir(name: String) extends CreateEntry(name) {
  override def createSpecificEntry(state: State): DirEntry =
    Directory.empty(state.wd.path, name)
}
