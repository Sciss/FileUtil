package de.sciss

import collection.immutable.{IndexedSeq => IIdxSeq}
import java.io.FileFilter
import language.implicitConversions

package object file {
  type File = java.io.File

  implicit def funToFileFilter(fun: File => Boolean): FileFilter = new FunFileFilter(fun)

  private final class FunFileFilter(fun: File => Boolean) extends FileFilter {
    def accept(f: File) = fun(f)
  }

  implicit final class RichFile(val peer: File) extends AnyVal {
    def /(child: String): File   = new File(peer, child)
    def parent          : File   = {
      val res = peer.getParentFile
      if (res == null) sys.error(s"File $peer does not have a parent")
      res
    }
    def parentOption: Option[File] = Option(peer.getParentFile)

    def path        : String    = peer.getPath
    def absolutePath: String    = peer.getAbsolutePath

    def name: String  = peer.getName
    def base: String  = baseAndExt._1
    def ext : String  = baseAndExt._2

    def replaceExt(s: String): File = {
      val n     = base
      val ext   = if (s.charAt(0) == '.') s else "." + s
      peer.parent / (n + ext)
    }

    /** Returns a tuple consisting of the file's name (without extension), and the extension (period dropped). */
    def baseAndExt: (String, String) = {
      val n = peer.name
      val i = n.lastIndexOf('.')
      if (i < 0) (n, "") else (n.substring(0, i), n.substring(i + 1))
    }

    def children: IIdxSeq[File] = children(_ => true)
    def children(filter: File => Boolean): IIdxSeq[File] = {
      val arr = peer.listFiles(filter)
      if (arr == null) Vector.empty else arr.toIndexedSeq
    }
  }

  def file(path: String): File = new File(path)

  def userHome: File = file(sys.props("user.home"))
}
