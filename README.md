# FileUtil

[![Build Status](https://travis-ci.org/Sciss/FileUtil.svg?branch=main)](https://travis-ci.org/Sciss/FileUtil)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.sciss/fileutil_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.sciss/fileutil_2.13)

## statement

FileUtil is a small library for Scala to provide enrichment and utility methods for `java.io.File`. Where possible and useful, it tries to use the same method names as sbt, e.g. `base` and `ext` to obtain base file name and extension.

It is (C)opyright 2013&ndash;2020 by Hanns Holger Rutz. All rights reserved. FileUtil is released under the [GNU Lesser General Public License](https://raw.github.com/Sciss/FileUtil/main/LICENSE) v2.1+ and comes with absolutely no warranties. To contact the author, send an email to `contact at sciss.de`.

## requirements / installation

This project compiles against Scala 2.13, 2.12 using sbt. The last version to support Scala 2.11 was 1.1.3.

To use the library in your project:

    "de.sciss" %% "fileutil" % v

The current version `v` is `"1.1.4"`

## contributing

Please see the file [CONTRIBUTING.md](CONTRIBUTING.md)

## documentation

### overview

Typically you will import the contents of package `de.sciss.file`. This includes a type alias `File` for `java.io.File`, a simple constructor `file("path")`, and enrichtments for `File`, which allow for example to construct files in the manner `file("base") / "sub" / "sub"`.

```scala
    import de.sciss.file._              // import type alias File for java.io.File, and enrichments

    (userHome / "Desktop").isDirectory  // userHome is predefined, slash operator creates sub-files
    file(".").absolute                  // file(<string>) method creates file
    val x = file("foo/bar.baz")
    val (base, ext) = x.baseAndExt      // split name and extension
    x.replaceExt("pdf")                 // -> foo/bar.pdf
    val tmp = File.createTemp()         // temporary file (by default deleted upon exit)
    userHome.children(_.length > 4096)  // list files in directory, using filter predicate
```

Further methods on files such as `relativize` are available, as well as utility methods and object in the companion object,
such as `NameOrdering`. See the API docs for a full overview.
The regular Java methods on files are still available, such as `.lastModified`, `.isFile`, `.isHidden`, `.length` etc.

### API docs

The API docs can be generated via `sbt doc`.
