package com.rdjvm.scala.oop.commands

import com.rdjvm.scala.oop.filesystem.State

// commands will have the property to transform the state of the filesystem
trait Command {
  // an abstract `apply` method. every command will have a different implementation for it
  def apply(state: State): State
}

object Command {
  val MKDIR =  "mkdir"
  val LS = "ls"

  def emptyCommand: Command = new Command {
    override def apply(state: State): State = state.setMessage("")
  }

  def incompleteCommand(name: String): Command = new Command {
    override def apply(state: State): State = state.setMessage(s"$name: Incomplete Command")
  }

  def from(input: String): Command = {

    val tokens: Array[String] = input.split(" ")

    tokens.head match {
      case MKDIR => {
        if (tokens.length < 2) incompleteCommand(MKDIR)
        else new Mkdir(tokens(1))
      }
      case LS => new Ls
      case "" => emptyCommand
      case _ => new UnknownCommand
    }
  }

}
