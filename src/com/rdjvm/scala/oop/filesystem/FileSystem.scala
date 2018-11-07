package com.rdjvm.scala.oop.filesystem

import java.util.Scanner

import com.rdjvm.scala.oop.commands.Command
import com.rdjvm.scala.oop.files.Directory

// the entry point of the application
object FileSystem extends App {

  val root = Directory.ROOT
  // create a new empty state with only the root directory
  var state = State(root, root)

  val scanner = new Scanner(System.in)

  while (true) {
    state.show
    val input = scanner.nextLine()
    state = Command.from(input).apply(state)
  }
}
