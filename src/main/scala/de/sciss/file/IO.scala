package de.sciss.file

object IO {
  def revealInFinder(file: File) {
    val cmd = Seq("osascript", "-e", "tell application \"Finder\"", "-e", "activate", "-e",
      "open location \"file:" + file.parent + "\"", "-e", "select file \"" + file.name + "\" of folder of the front window",
      "-e", "end tell"
    )
    import sys.process._
    cmd.run()
  }

  def tempFile(prefix: String = "temp", suffix: String = ".tmp", deleteOnExit: Boolean = true,
               directory: Boolean = false): File = {
    val f = java.io.File.createTempFile(prefix, suffix)
    if (deleteOnExit) f.deleteOnExit()
    if (directory) {
      f.delete()
      f.mkdir()
    }
    f
  }
}