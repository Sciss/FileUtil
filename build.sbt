lazy val baseName       = "FileUtil"
lazy val baseNameL      = baseName.toLowerCase
lazy val projectVersion = "1.1.4"
lazy val mimaVersion    = "1.1.2"

lazy val commonSettings = Seq(
  name               := baseName,
  version            := projectVersion,
  organization       := "de.sciss",
  scalaVersion       := "2.13.2",
  crossScalaVersions := Seq("2.13.2", "2.12.11"),
  description        := "Simple Scala enrichtments for java.io.File",
  homepage           := Some(url(s"https://git.iem.at/sciss/${name.value}")),
  licenses           := Seq("LGPL v2.1+" -> url("http://www.gnu.org/licenses/lgpl-2.1.txt")),
  initialCommands in console := """import de.sciss.file._""",
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-Xsource:2.13", "-Xlint"),
  mimaPreviousArtifacts := Set("de.sciss" %% baseNameL % mimaVersion),
)

lazy val root = project.in(file("."))
  .settings(commonSettings)
  .settings(publishSettings)

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishTo := {
    Some(if (isSnapshot.value)
      "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
    else
      "Sonatype Releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
    )
  },
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  pomExtra := { val n = name.value
<scm>
  <url>git@git.iem.at:sciss/{n}.git</url>
  <connection>scm:git:git@git.iem.at:sciss/{n}.git</connection>
</scm>
<developers>
  <developer>
    <id>sciss</id>
    <name>Hanns Holger Rutz</name>
    <url>http://www.sciss.de</url>
  </developer>
</developers>
}
)

