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
  val PWD = "pwd"
  val TOUCH = "touch"
  val CD = "cd"
  val RM = "rm"
  val ECHO = "echo"
  val CAT = "cat"

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
      case PWD => new Pwd
      case TOUCH => {
        if (tokens.length < 2) incompleteCommand(TOUCH)
        new Touch(tokens(1))
      }
      case CD => {
        if (tokens.length < 2) incompleteCommand(CD)
        new Cd(tokens(1))
      }
      case RM => {
        if (tokens.length < 2) incompleteCommand(RM)
        new Rm(tokens(1))
      }
      case ECHO => {
        if (tokens.length < 2) incompleteCommand(RM)
        new Echo(tokens.tail)
      }
      case CAT => {
        if (tokens.length < 2) incompleteCommand(RM)
        new Cat(tokens(1))
      }
      case "" => emptyCommand
      case _ => new UnknownCommand
    }
  }

}
