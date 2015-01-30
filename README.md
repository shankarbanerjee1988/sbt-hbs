# sbt-hbs [![Build Status](https://travis-ci.org/bicouy0/sbt-hbs.svg?branch=master)](https://travis-ci.org/bicouy0/sbt-hbs)
[sbt-web] plugin for precompiling handlebars templates to javascript files, using the [handlebarsjs] compiler.

Installation
------------

To use this plugin use the addSbtPlugin command within your project's `plugins.sbt` file:

```scala
resolvers += Resolver.sbtPluginRepo("releases")

addSbtPlugin("com.bicou.sbt" % "sbt-hbs" % "1.0.1")
```

Your project's build file also needs to enable sbt-web plugins. For example with `build.sbt`:

```scala
lazy val root = (project in file(".")).enablePlugins(SbtWeb)
```

Install handlebarsjs, either globally with npm:

```shell
npm install handlebars -g
```

Or locally in your project with a `package.json` file:
```json
{
  "devDependencies": {
    "handlebars": "~2"
  }
}
```

Supported settings
------------------

Option           | Description                                                            | Default
-----------------|------------------------------------------------------------------------|---------
`amd`            | When set, generates JavaScript with the [AMD wrapper]                  | `false`
`commonjs`       | Exports CommonJS style, path to Handlebars module                      | `""`
`handlebarPath`  | Path to handlebar.js (only valid for amd-style)                        | `""`
`min`            | Minimize output                                                        | `false`
`known`          | Known helpers                                                          | `[]`
`knownOnly`      | Known helpers only                                                     | `false`
`namespace`      | Template namespace                                                     | `"Handlebars.templates"`
`root`           | Template root (base value that will be stripped from template names)   | `""`
`data`           | Include data when compiling                                            | `false`
`bom`            | Removes the BOM (Byte Order Mark) from the beginning of the templates  | `false`
`simple`         | Output template function only                                          | `false`


The following sbt code illustrates how to generate templates with AMD wrapper 

```scala
HbsKeys.amd := true
```

Usage
-----

Once configured, any `*.hbs` or `*.handlebars` files placed in `app/assets` will be compiled to JavaScript code in `target/web/public`, the handlebars name is build from file path, relative to `root` option.

For example the name of the template located in `app/assets/javascripts/templates/index.hbs` will be `javascripts/templates/index`, unless you set `HbsKeys.root := "javascripts/templates/"` then it will be `index` (it's the handlebars id, not the javascript path).

If a file name begins with an underscore, it will be handled as a partial.

Don't forget to include the handlebars runtime to your public javascripts, you can grab a copy in `node_modules/handlebars/dist/handlebars.runtime.js`.

Example
-------

Browse [play-rjs-coffee-hbs](https://github.com/bicouy0/play-rjs-coffee-hbs) repository to see usage with play framework.

License
-------

This code is licensed under the [MIT License].

[sbt-web]:https://github.com/sbt/sbt-web
[handlebarsjs]:http://handlebarsjs.com/
[AMD wrapper]:http://requirejs.org/docs/whyamd.html
[MIT License]:http://opensource.org/licenses/MIT
