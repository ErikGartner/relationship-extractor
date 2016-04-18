import sbt._

object MyBuild extends Build {

  lazy val root = Project("root", file(".")) dependsOn(soundPlayerProject)
  lazy val soundPlayerProject = RootProject(uri("git://github.com/alvinj/SoundFilePlayer.git#master"))
  // lazy val soundPlayerProject = RootProject( file("/Users/axel/utveckling/scala/SoundFilePlayer") )


}