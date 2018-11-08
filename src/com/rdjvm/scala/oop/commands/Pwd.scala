package com.rdjvm.scala.oop.commands
import com.rdjvm.scala.oop.filesystem.State

class Pwd extends Command {
  override def apply(state: State): State =
    state.setMessage(state.wd.path)
}
