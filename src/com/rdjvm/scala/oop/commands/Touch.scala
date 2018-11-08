package com.rdjvm.scala.oop.commands
import com.rdjvm.scala.oop.files.{DirEntry, File}
import com.rdjvm.scala.oop.filesystem.State

class Touch(name: String) extends CreateEntry(name) {
  override def createSpecificEntry(state: State): DirEntry = File.empty(state.wd.path, name)
}
