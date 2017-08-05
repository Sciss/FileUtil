/*
 *  File.scala
 *  (FileUtil)
 *
 *  Copyright (c) 2013-2017 Hanns Holger Rutz. All rights reserved.
 *
 *  This software is published under the GNU Lesser General Public License v2.1+
 *
 *
 *  For further information, please contact Hanns Holger Rutz at
 *  contact@sciss.de
 */

package de.sciss.file

import scala.collection.immutable.{IndexedSeq => Vec}

object File {
  /** Shows a file in the Finder (OS X only). */
  def revealInFinder(file: File): Unit = {
    val abs   = file.absolute
    val tell  = "tell application \"Finder\""
    val reveal= "reveal POSIX file \"" + abs.path + "\""
    val end   = "end tell"
    val cmd   = Seq("osascript", "-e", tell, "-e", "activate", "-e", reveal, "-e", end)
    import sys.process._
    cmd.run(ProcessLogger.apply(_ => (), Console.err.println))  // because osascript prints file path
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
                 directory: Boolean = false): File =
    createTempImpl(prefix = prefix, suffix = suffix, deleteOnExit = deleteOnExit, directory = directory,
      parentOrNull = null)

  /** Creates a temporary file or directory within a given directory.
    *
    * @param parent       the directory within which to create the temporary file
    * @param prefix       prefix to use in the file name. must be at least three characters long
    * @param suffix       suffix to use in the file name.
    * @param deleteOnExit if `true` file is marked for deletion upon JVM exit.
    * @param directory    if `true` creates an empty directory instead of a file
    */
  def createTempIn(parent: File, prefix: String = "temp", suffix: String = ".tmp", deleteOnExit: Boolean = true,
                   directory: Boolean = false): File =
    createTempImpl(prefix = prefix, suffix = suffix, deleteOnExit = deleteOnExit, directory = directory,
      parentOrNull = parent)

  private def createTempImpl(prefix: String, suffix: String, deleteOnExit: Boolean, directory: Boolean,
                             parentOrNull: File): File = {
    val f = java.io.File.createTempFile(prefix, suffix, parentOrNull)
    if (deleteOnExit) f.deleteOnExit()
    if (directory) {
      f.delete()
      f.mkdir()
    }
    f
  }

  /** The system's default temporary directory */
  def tempDir: File = file(sys.props("java.io.tmpdir"))

  /** The separator character of the platform, used to separate sub directories of a file. */
 	val sep    : Char = java.io.File.separatorChar

  /** The character of the platform usable to separate entire paths. */
  val pathSep: Char = java.io.File.pathSeparatorChar

  object NameOrdering extends Ordering[File] {
    def compare(f1: File, f2: File): Int =
      compareName(f1.name, f2.name)
  }

  /** Compares strings insensitive to case but sensitive to integer numbers. */
  def compareName(s1: String, s2: String): Int = {
    // this is a quite ugly direct translation from a Java snippet I wrote,
    // could use some scala'fication

    val n1  = s1.length
    val n2  = s2.length
    val min = math.min(n1, n2)

    var i = 0
    while (i < min) {
      var c1 = s1.charAt(i)
      var c2 = s2.charAt(i)
      var d1 = Character.isDigit(c1)
      var d2 = Character.isDigit(c2)

      if (d1 && d2) {
        // Enter numerical comparison
        var c3, c4 = ' '
        do {
          i += 1
          c3 = if (i < n1) s1.charAt(i) else 'x'
          c4 = if (i < n2) s2.charAt(i) else 'x'
          if (c1 == c2 && c3 != c4) {
            c1 = c3
            c2 = c4
          }
          d1 = Character.isDigit(c3)
          d2 = Character.isDigit(c4)
        }
        while (d1 && d2)

        if (d1 != d2) return if (d1) 1 else -1
        if (c1 != c2) return c1 - c2
        i -= 1

      }
      else if (c1 != c2) {
        c1 = Character.toUpperCase(c1)
        c2 = Character.toUpperCase(c2)

        if (c1 != c2) {
          c1 = Character.toLowerCase(c1)
          c2 = Character.toLowerCase(c2)

          if (c1 != c2) {
            // No overflow because of numeric promotion
            return c1 - c2
          }
        }
      }

      i += 1
    }
    n1 - n2
  }
}