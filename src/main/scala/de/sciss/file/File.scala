package de.sciss.file

import collection.immutable.{IndexedSeq => Vec}

object File {
  /** Shows a file in the Finder (OS X only). */
  def revealInFinder(file: File): Unit = {
    val abs   = file.absolute
    val tell  = "tell application \"Finder\""
    val reveal= "reveal POSIX file \"" + abs.path + "\""
    val end   = "end tell"
    val cmd   = Seq("osascript", "-e", tell, "-e", "activate", "-e", reveal, "-e", end)
    import sys.process._
    cmd.run(ProcessLogger.apply(_ => (), Console.err.println))  // sucky osascript prints file path
  }

  /** Lists the available filesystem roots. */
  def roots(): Vec[File] = java.io.File.listRoots().toIndexedSeq

  /** Creates a temporary file or directory.
    *
    * @param prefix       prefix to use in the file name. must be at least three characters long
    * @param suffix       suffix to use in the file name.
    * @param deleteOnExit if `true` file is marked for deletion upon JVM exit.
    * @param directory    if `true` creates an empty directory instead of a file
    */
  def createTemp(prefix: String = "temp", suffix: String = ".tmp", deleteOnExit: Boolean = true,
                 directory: Boolean = false): File = {
    val f = java.io.File.createTempFile(prefix, suffix)
    if (deleteOnExit) f.deleteOnExit()
    if (directory) {
      f.delete()
      f.mkdir()
    }
    f
  }

  /** The separator character of the platform, used to separate sub directories of a file. */
 	val sep: Char = java.io.File.separatorChar

  /** The character of the platform usable to separate entire paths. */
  val pathSep: Char = java.io.File.pathSeparatorChar
}