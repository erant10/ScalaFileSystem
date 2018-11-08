package com.rdjvm.scala.oop.commands
import com.rdjvm.scala.oop.filesystem.State

class Cat(filename: String) extends Command {
  def apply(state: State): State = {
    val entry = state.wd.findEntry(filename)
    if (entry == null || !entry.isFile) state.setMessage(s"$filename: no such file")
    else state.setMessage(entry.asFile.contents)
  }
}
