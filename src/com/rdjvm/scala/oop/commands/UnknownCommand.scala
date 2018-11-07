package com.rdjvm.scala.oop.commands
import com.rdjvm.scala.oop.filesystem.State

class UnknownCommand extends Command {
  override def apply(state: State): State = state.setMessage(UnknownCommand.COMMAND_NOT_FOUND)
}

object UnknownCommand {
  val COMMAND_NOT_FOUND = "Command not found!"
}
