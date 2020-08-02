/*
 *  FunFileFilter.scala
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
package file
package impl

import java.io.FileFilter

private[file] final class FunFileFilter(fun: File => Boolean) extends FileFilter {
  def accept(f: File): Boolean = fun(f)
}