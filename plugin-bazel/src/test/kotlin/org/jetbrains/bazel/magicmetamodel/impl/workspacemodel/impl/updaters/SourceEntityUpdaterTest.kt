package org.jetbrains.bazel.magicmetamodel.impl.workspacemodel.impl.updaters

import com.intellij.platform.workspace.jps.entities.ContentRootEntity
import com.intellij.platform.workspace.jps.entities.SourceRootEntity
import com.intellij.platform.workspace.jps.entities.SourceRootTypeId
import com.intellij.platform.workspace.storage.impl.url.toVirtualFileUrl
import org.jetbrains.bazel.sdkcompat.workspacemodel.entities.GenericSourceRoot
import org.jetbrains.bazel.workspace.model.matchers.entries.ExpectedSourceRootEntity
import org.jetbrains.bazel.workspace.model.matchers.entries.shouldBeEqual
import org.jetbrains.bazel.workspace.model.matchers.entries.shouldContainExactlyInAnyOrder
import org.jetbrains.bazel.workspace.model.test.framework.WorkspaceModelWithParentPythonModuleBaseTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.io.path.Path

@DisplayName("sourceEntityUpdater.addEntity(entityToAdd, parentModuleEntity) tests")
class SourceEntityUpdaterTest : WorkspaceModelWithParentPythonModuleBaseTest() {
  private lateinit var sourceEntityUpdater: SourceEntityUpdater

  @BeforeEach
  override fun beforeEach() {
    // given
    super.beforeEach()

    val workspaceModelEntityUpdaterConfig =
      WorkspaceModelEntityUpdaterConfig(workspaceEntityStorageBuilder, virtualFileUrlManager, projectBasePath, project)
    sourceEntityUpdater = SourceEntityUpdater(workspaceModelEntityUpdaterConfig)
  }

  @Test
  fun `should add one python source root to the workspace model`() {
    // given
    val sourceDir = Path("/root/dir/example/package/")

    val genericSourceRoot =
      GenericSourceRoot(
        sourcePath = sourceDir,
        rootType = SourceRootTypeId("python-source"),
      )

    // when
    val returnedPythonSourceRootEntity =
      runTestWriteAction {
        sourceEntityUpdater.addEntity(genericSourceRoot, parentModuleEntity)
      }

    // then
    val virtualSourceDir = sourceDir.toVirtualFileUrl(virtualFileUrlManager)

    val expectedPythonSourceRootEntity =
      ExpectedSourceRootEntity(
        contentRootEntity =
          ContentRootEntity(
            entitySource = parentModuleEntity.entitySource,
            url = virtualSourceDir,
            excludedPatterns = emptyList(),
          ),
        sourceRootEntity =
          SourceRootEntity(
            entitySource = parentModuleEntity.entitySource,
            url = virtualSourceDir,
            rootTypeId = SourceRootTypeId("python-source"),
          ) {},
        parentModuleEntity = parentModuleEntity,
      )

    returnedPythonSourceRootEntity shouldBeEqual expectedPythonSourceRootEntity
    loadedEntries(SourceRootEntity::class.java) shouldContainExactlyInAnyOrder listOf(expectedPythonSourceRootEntity)
  }

  @Test
  fun `should add multiple python source roots to the workspace model`() {
    // given
    val sourceDir1 = Path("/root/dir/example/package/")

    val genericSourceRoot1 =
      GenericSourceRoot(
        sourcePath = sourceDir1,
        rootType = SourceRootTypeId("python-source"),
      )

    val sourceDir2 = Path("/another/root/dir/another/example/package/")

    val genericSourceRoot2 =
      GenericSourceRoot(
        sourcePath = sourceDir2,
        rootType = SourceRootTypeId("python-test"),
      )

    val pythonSourceRoots = listOf(genericSourceRoot1, genericSourceRoot2)

    // when
    val returnedPythonSourceRootEntities =
      runTestWriteAction {
        sourceEntityUpdater.addEntities(pythonSourceRoots, parentModuleEntity)
      }

    // then
    val virtualSourceDir1 = sourceDir1.toVirtualFileUrl(virtualFileUrlManager)

    val expectedPythonSourceRootEntity1 =
      ExpectedSourceRootEntity(
        contentRootEntity =
          ContentRootEntity(
            entitySource = parentModuleEntity.entitySource,
            url = virtualSourceDir1,
            excludedPatterns = emptyList(),
          ),
        sourceRootEntity =
          SourceRootEntity(
            entitySource = parentModuleEntity.entitySource,
            url = virtualSourceDir1,
            rootTypeId = SourceRootTypeId("python-source"),
          ) {},
        parentModuleEntity = parentModuleEntity,
      )

    val virtualSourceDir2 = sourceDir2.toVirtualFileUrl(virtualFileUrlManager)

    val expectedPythonSourceRootEntity2 =
      ExpectedSourceRootEntity(
        contentRootEntity =
          ContentRootEntity(
            entitySource = parentModuleEntity.entitySource,
            url = virtualSourceDir2,
            excludedPatterns = emptyList(),
          ),
        sourceRootEntity =
          SourceRootEntity(
            entitySource = parentModuleEntity.entitySource,
            url = virtualSourceDir2,
            rootTypeId = SourceRootTypeId("python-test"),
          ) {},
        parentModuleEntity = parentModuleEntity,
      )

    val expectedPythonSourceRootEntities = listOf(expectedPythonSourceRootEntity1, expectedPythonSourceRootEntity2)

    returnedPythonSourceRootEntities shouldContainExactlyInAnyOrder expectedPythonSourceRootEntities
    loadedEntries(SourceRootEntity::class.java) shouldContainExactlyInAnyOrder expectedPythonSourceRootEntities
  }
}
