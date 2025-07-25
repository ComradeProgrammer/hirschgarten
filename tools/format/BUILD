load("@aspect_rules_js//js:defs.bzl", "js_library")
load("@aspect_rules_lint//format:defs.bzl", "format_multirun")
load("@npm//:prettier/package_json.bzl", prettier = "bin")
load("@rules_java//java:defs.bzl", "java_binary")

package(default_visibility = ["//visibility:public"])

format_multirun(
    name = "format",
    java = ":java-format",
    kotlin = ":ktfmt",
    protocol_buffer = "@rules_buf_toolchains//:buf",
    starlark = "@buildifier_prebuilt//:buildifier",
    xml = ":prettier",
    yaml = "@aspect_rules_lint//format:yamlfmt",
)

java_binary(
    name = "java-format",
    jvm_flags = [
        "--add-exports jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
        "--add-exports jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
        "--add-exports jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED",
        "--add-exports jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
        "--add-exports jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED",
    ],
    main_class = "com.google.googlejavaformat.java.Main",
    runtime_deps = ["@google_java_format//jar"],
)

java_binary(
    name = "ktfmt",
    args = ["--format"],
    main_class = "com.pinterest.ktlint.Main",
    runtime_deps = ["@ktlint//jar"],
)

prettier.prettier_binary(
    name = "prettier",
    # Include this js_library and its dependencies in the runfiles (runtime dependencies)
    data = [
        ":prettierrc",
        "//:node_modules/@prettier/plugin-xml",
    ],
    # Allow the binary to be run outside bazel
    env = {"BAZEL_BINDIR": "."},
    fixed_args = [
        # `require` statements in the config file will be resolved relative to its location
        # Therefore to make it hermetic, prettier must be pointed at the copy of the config file
        # in the runfiles folder rather than the one in the source folder.
        "--config=\"$$JS_BINARY__RUNFILES\"/$(rlocationpath :prettierrc)",
        # default log level is "log" which spams on success
        # https://prettier.io/docs/en/cli.html#--log-level
        # NB: prettier 2 names this loglevel, in prettier 3 it's renamed log-level, see
        # https://prettier.io/blog/2023/07/05/3.0.0.html#cli-1
        "--log-level=warn",
    ],
)

js_library(
    name = "prettierrc",
    srcs = ["prettier.config.cjs"],
    deps = [
        "//:node_modules/@prettier/plugin-xml",
    ],
)
