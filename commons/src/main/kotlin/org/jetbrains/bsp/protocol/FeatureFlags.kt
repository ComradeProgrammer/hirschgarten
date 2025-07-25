package org.jetbrains.bsp.protocol

data class FeatureFlags(
  val isPythonSupportEnabled: Boolean = false,
  val isAndroidSupportEnabled: Boolean = false,
  val isGoSupportEnabled: Boolean = false,
  val isCppSupportEnabled: Boolean = false,
  val isPropagateExportsFromDepsEnabled: Boolean = true,
  val isSharedSourceSupportEnabled: Boolean = false,
  /** Bazel specific */
  val bazelSymlinksScanMaxDepth: Int = 2,
  val bazelShutDownBeforeShardBuild: Boolean = false,
  val isBazelQueryTabEnabled: Boolean = false,
)
