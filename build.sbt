lazy val baseName       = "FileUtil"
lazy val baseNameL      = baseName.toLowerCase
lazy val projectVersion = "1.1.5"
lazy val mimaVersion    = "1.1.2"

// sonatype plugin requires that these are in global
ThisBuild / version      := projectVersion
ThisBuild / organization := "de.sciss"

lazy val commonSettings = Seq(
  name               := baseName,
//  version            := projectVersion,
//  organization       := "de.sciss",
  scalaVersion       := "2.13.4",
  crossScalaVersions := Seq("3.0.0-M3", "2.13.4", "2.12.12"),
  description        := "Simple Scala enrichtments for java.io.File",
  homepage           := Some(url(s"https://git.iem.at/sciss/${name.value}")),
  licenses           := Seq("LGPL v2.1+" -> url("http://www.gnu.org/licenses/lgpl-2.1.txt")),
  initialCommands in console := """import de.sciss.file._""",
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-Xsource:2.13", "-Xlint"),
  mimaPreviousArtifacts := Set("de.sciss" %% baseNameL % mimaVersion),
  sources in (Compile, doc) := {
    if (isDotty.value) Nil else (sources in (Compile, doc)).value  // https://github.com/lampepfl/dotty/issues/8634 
  },
)

lazy val root = project.in(file("."))
  .settings(commonSettings)
  .settings(publishSettings)

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  developers := List(
    Developer(
      id    = "sciss",
      name  = "Hanns Holger Rutz",
      email = "contact@sciss.de",
      url   = url("https://www.sciss.de")
    )
  ),
  scmInfo := {
    val h = "git.iem.at"
    val a = s"sciss/${name.value}"
    Some(ScmInfo(url(s"https://$h/$a"), s"scm:git@$h:$a.git"))
  },
)

