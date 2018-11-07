package com.rdjvm.scala.oop.filesystem

import com.rdjvm.scala.oop.files.Directory

// a class the will hold the "State" of the file system (working directory & root directory)
class State(val root: Directory, val wd: Directory, val output: String) {

  def show: Unit = {
    println(output)
    print(State.SHELL_TOKEN)
  }

  // build a new state with a new output message
  def setMessage(message: String): State = State(root, wd, message)

}

object State {
  val SHELL_TOKEN: String = "$ "

  /**
    * A factory for creating a new state
    */
  def apply(root: Directory, wd: Directory, output: String = ""): State = new State(root, wd, output)
}
