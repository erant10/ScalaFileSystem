package com.rdjvm.scala.oop.filesystem

import com.rdjvm.scala.oop.commands.Command
import com.rdjvm.scala.oop.files.Directory

// the entry point of the application
object FileSystem extends App {

  val root = Directory.ROOT
  val state = State(root, root)
  state.show

  io.Source.stdin.getLines().foldLeft(state)((currentState,newLine) => {
    val state = Command.from(newLine).apply(currentState)
    state.show
    state
  })

}
