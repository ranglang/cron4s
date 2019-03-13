import sbt._
import Keys._

object ScalaMeterPlugin extends AutoPlugin {

  object autoImport {
    lazy val Benchmark = config("bench") extend Test

    lazy val scalaMeterVersion = settingKey[String]("ScalaMeter version")
  }
  import autoImport._

  override def projectConfigurations = Seq(Benchmark)

  override def projectSettings = Defaults.coreDefaultSettings ++ benchmarkSettings ++ Seq(
    scalaMeterVersion := "0.17",
    libraryDependencies ++= Seq(
      "com.storm-enroute" %% "scalameter" % scalaMeterVersion.value % Benchmark
    ),
  )

  private lazy val benchmarkSettings = inConfig(Benchmark)(Defaults.testSettings) ++ inConfig(Benchmark)(Seq(
    testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework"),
    parallelExecution := false,
    publishArtifact := false,
    logBuffered := false,
  ))

}
