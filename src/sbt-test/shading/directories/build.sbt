ThisBuild / scalaVersion := "2.12.18"

ThisBuild / assemblyShadeRules := Seq(
  ShadeRule.rename("somepackage.**" -> "shaded.@1").inAll
)

lazy val root = (project in file("."))
  .settings(
    crossPaths := false,

    assembly / assemblyJarName := "assembly.jar",

    TaskKey[Unit]("check") := {
      val expected = "Hello shaded.SomeClass"
      val output = sys.process.Process("java", Seq("-jar", assembly.value.absString)).!!.trim
      if (output != expected) sys.error("Unexpected output: " + output)
    },

    TaskKey[Unit]("unzip") := {
      IO.unzip((assembly / assemblyOutputPath).value, crossTarget.value / "unzipped")
    }
  )
