package com.de.sbt.hbs

import sbt._
import sbt.Keys._
import com.typesafe.sbt.web._
import com.typesafe.sbt.jse.{SbtJsEngine, SbtJsTask}
import spray.json._

object Import {

  object HbsKeys {
    val handlebars = TaskKey[Seq[File]]("handlebars", "Precompile handlebar templates.")

    val amd = SettingKey[Boolean]("handlebars-amd", "Exports amd style (require.js)")
    val commonjs = SettingKey[String]("handlebars-commonjs", "Exports CommonJS style, path to Handlebars module")
    val handlebarPath = SettingKey[String]("handlebars-handlebarPath", "Path to handlebar.js (only valid for amd-style)")
    val known = SettingKey[Seq[String]]("handlebars-known", "Known helpers")
    val knownOnly = SettingKey[Boolean]("handlebars-knownOnly", "Known helpers only")
    val namespace = SettingKey[String]("handlebars-namespace", "Template namespace")
    val root = SettingKey[String]("handlebars-root", "Template root (base value that will be stripped from template names)")
    val data = SettingKey[Boolean]("handlebars-data", "Include data when compiling")
    val bom = SettingKey[Boolean]("handlebars-bom", "Removes the BOM (Byte Order Mark) from the beginning of the templates")
    val simple = SettingKey[Boolean]("handlebars-simple", "Output template function only")
    val map = SettingKey[Boolean]("handlebars-map", "Generates source maps")
  }

}

object SbtHbs extends AutoPlugin {

  override def requires = SbtJsTask

  override def trigger = AllRequirements

  val autoImport = Import

  import autoImport.HbsKeys._
  import SbtWeb.autoImport._
  import WebKeys._
  import SbtJsEngine.autoImport.JsEngineKeys._
  import SbtJsTask.autoImport.JsTaskKeys._

  val hbsUnscopedSettings = Seq(

    excludeFilter := HiddenFileFilter,
    includeFilter := "*.hbs" || "*.handlebars",

    jsOptions := JsObject(
      "amd" -> JsBoolean(amd.value),
      "commonjs" -> JsString(commonjs.value),
      "handlebarPath" -> JsString(handlebarPath.value),
      "known" -> JsArray(known.value.toList.map(JsString(_))),
      "knownOnly" -> JsBoolean(knownOnly.value),
      "namespace" -> JsString(namespace.value),
      "root" -> JsString(root.value),
      "data" -> JsBoolean(data.value),
      "bom" -> JsBoolean(bom.value),
      "simple" -> JsBoolean(simple.value),
      "map" -> JsBoolean(map.value)
    ).toString()
  )

  override def projectSettings = Seq(
    amd := false,
    commonjs := "",
    handlebarPath := "",
    known := Seq(),
    knownOnly := false,
    namespace := "",
    root := "",
    data := false,
    bom := false,
    simple := false,
    map := false

  ) ++ inTask(handlebars)(
    SbtJsTask.jsTaskSpecificUnscopedSettings ++
      inConfig(Assets)(hbsUnscopedSettings) ++
      inConfig(TestAssets)(hbsUnscopedSettings) ++
      Seq(
        moduleName := "handlebars",
        shellFile := getClass.getClassLoader.getResource("handlebars-shell.js"),

        taskMessage in Assets := "Handlebars compiling",
        taskMessage in TestAssets := "Handlebars test compiling"
      )
  ) ++ SbtJsTask.addJsSourceFileTasks(handlebars) ++ Seq(
    handlebars in Assets := (handlebars in Assets).dependsOn(nodeModules in Assets).value,
    handlebars in TestAssets := (handlebars in TestAssets).dependsOn(nodeModules in TestAssets).value
  )

}

// vim: set ts=2 sw=2 et:
