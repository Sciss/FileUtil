/*
 *  package.scala
 *  (FileUtil)
 *
 *  Copyright (c) 2013-2020 Hanns Holger Rutz. All rights reserved.
 *
 *  This software is published under the GNU Lesser General Public License v2.1+
 *
 *
 *  For further information, please contact Hanns Holger Rutz at
 *  contact@sciss.de
 */

package de.sciss

import java.io.FileFilter
import java.util.Locale

import scala.annotation.tailrec
import scala.collection.immutable.{IndexedSeq => Vec}
import scala.language.implicitConversions

package object file {
  type File = java.io.File

  implicit def funToFileFilter(fun: File => Boolean): FileFilter = new impl.FunFileFilter(fun)

  implicit final class RichFile(val peer: File) extends AnyVal {
    /** Constructs a sub-file of this file by appending a new path component. */
    def / (child: String): File   = new File(peer, child)

    def parent: File = {
      val res = peer.getParentFile
      if (res == null) sys.error(s"File $peer does not have a parent")
      res
    }

    def parentOption: Option[File] = Option(peer.getParentFile)

    def absolute    : File      = new File(peer.toURI.normalize).getAbsoluteFile

    /** The string representation of this file. */
    def path        : String    = peer.getPath

    /** The string representation of the absolute location of this file. */
    def absolutePath: String    = peer.getAbsolutePath

    /** Returns the name part of the file. */
    def name        : String    = peer.getName

    /** Returns the name part of the file and drops the extension (if any). */
    def base        : String    = baseAndExt._1

    /** Returns the extension of the file (period dropped). Returns and empty string
      * if no extension is given.
      */
    def ext         : String    = baseAndExt._2

    /** Returns the extension of the file (period dropped) in lower case letters.
      * Returns and empty string if no extension is given.
      */
    def extL        : String    = ext.toLowerCase(Locale.US)

    /** Replaces the extension by a given string. If the file did not have any extension,
      * it will be added by this call.
      *
      * @param s  the extension to use in replacement. May or may not contain a leading period.
      */
    def replaceExt(s: String): File = {
      val b     = base
      val ext   = if (s.charAt(0) == '.') s else "." + s
      val name  = b + ext
      peer.parentOption.fold(file(name))(_ / name)
    }

    /** Replaces the name part of this file, keeping the parent directory. */
    def replaceName(s: String): File =
      parentOption.fold[File](file(s))(_ / s)

    /** Returns a tuple consisting of the file's name (without extension), and the extension (period dropped). */
    def baseAndExt: (String, String) = {
      val n = peer.name
      val i = n.lastIndexOf('.')
      if (i < 0) (n, "") else (n.substring(0, i), n.substring(i + 1))
    }

    /** Lists all children of this directory. Returns an empty sequence if this file
      * does not denote a directory.
      */
    def children: Vec[File] = children(_ => true)

    /** Lists all children of this directory which satisfy a predicate. Returns an empty sequence if this file
      * does not denote a directory.
      *
      * @param  filter  a filter predicate to select children
      */
    def children(filter: File => Boolean): Vec[File] = {
      val arr = peer.listFiles(filter)
      if (arr == null) Vector.empty else arr.toIndexedSeq
    }

    /** Creates from this file a new file instance relative to a given `parent` directory.
      * Throws an exception if if `parent` does not constitute a parent directory.
      */
    def relativize(parent: File): File = relativizeOption(parent)
        .getOrElse(throw new IllegalArgumentException(s"File $peer is not in a subdirectory of $parent"))

    /** Creates from this file a new file instance relative to a given `parent` directory.
      *
      * @return `Some` if this file is relative to `parent`, `None` if `parent` does not constitute a parent directory
      */
    def relativizeOption(parent: File): Option[File] = {
      // Note: .getCanonicalFile will resolve symbolic links.
      // In order to support artifacts being symbolic links
      // inside a parent folder, we must not resolve them!

      val can     = peer  .absolute // .getCanonicalFile
      val base    = parent.absolute // .getCanonicalFile

      @tailrec def loop(res: File, leftOpt: Option[File]): Option[File] =
        leftOpt match {
          case Some(left) =>
            if (left == base) Some(res)
            else loop(file(left.name) / res.path, left.parentOption)
          case _ => None
        }

      if (can == base) Some(file(""))
      else loop(file(can.name), can.parentOption)
    }
  }

  /** Constructs a file from a path string. */
  def file(path: String): File = new File(path)

  /** A convenience method for the user's home directory according to system properties. */
  def userHome: File = file(sys.props("user.home"))
}
