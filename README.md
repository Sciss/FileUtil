# FileUtil

[![Flattr this](http://api.flattr.com/button/flattr-badge-large.png)](https://flattr.com/submit/auto?user_id=sciss&url=https%3A%2F%2Fgithub.com%2FSciss%2FFileUtil&title=FileUtil&language=Scala&tags=github&category=software)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.sciss/fileutil_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.sciss/fileutil_2.11)

## statement

FileUtil is a small library for Scala to provide enrichment and utility methods for `java.io.File`. Where possible and useful, it tries to use the same method names as sbt, e.g. `base` and `ext` to obtain base file name and extension.

It is (C)opyright 2013 by Hanns Holger Rutz. All rights reserved. FileUtil is released under the [GNU Lesser General Public License](https://raw.github.com/Sciss/FileUtil/master/LICENSE) v2.1+ and comes with absolutely no warranties. To contact the author, send an email to `contact at sciss.de`.

## requirements / installation

This project compiles against Scala 2.11, 2.10 using sbt 0.13.

To use the library in your project:

    "de.sciss" %% "fileutil" % v

The current version `v` is `"1.1.1+"`

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

The regular Java methods on files are still available, such as `.lastModified`, `.isFile`, `.isHidden`, `.length` etc.

### API docs

The API docs can be generated via `sbt doc`. The latest docs [can be browsed online here](http://sciss.github.io/FileUtil/latest/api/).
