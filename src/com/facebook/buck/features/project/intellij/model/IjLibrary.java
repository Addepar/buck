/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.facebook.buck.features.project.intellij.model;

import com.facebook.buck.core.model.BuildTarget;
import com.facebook.buck.core.util.immutables.BuckStyleValueWithBuilder;
import com.facebook.buck.features.project.intellij.IjDependencyListBuilder;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.nio.file.Path;
import org.immutables.value.Value;

/** Represents a prebuilt library (.jar or .aar) as seen by IntelliJ. */
@BuckStyleValueWithBuilder
public abstract class IjLibrary implements IjProjectElement {
  /**
   * Types of IjLibrary
   * <li>{@link #DEFAULT}: Generated by a Buck target
   * <li>{@link #KOTLIN_JAVA_RUNTIME}: Generated for Kotlin code support in IDE
   */
  public enum Type {
    DEFAULT,
    KOTLIN_JAVA_RUNTIME
  }

  @Override
  public abstract String getName();

  @Override
  public abstract ImmutableSet<BuildTarget> getTargets();

  /** @return path to the binary (.jar or .aar) the library represents. */
  public abstract ImmutableSet<Path> getBinaryJars();

  /** @return classPath paths */
  public abstract ImmutableSet<Path> getClassPaths();

  /** @return path to the jar containing sources for the library. */
  public abstract ImmutableSet<Path> getSourceJars();

  /** @return url to the javadoc. */
  public abstract ImmutableSet<String> getJavadocUrls();

  /** @return path to the directories containing Java sources for the library. */
  public abstract ImmutableSet<Path> getSourceDirs();

  @Value.Check
  protected void eitherBinaryJarOrClassPathPresent() {
    if (getType() == Type.DEFAULT) {
      // IntelliJ library should have a binary jar or classpath, but we also allow it to have an
      // optional res folder so that resources can be loaded properly.
      boolean hasClasspathsWithoutRes =
          getClassPaths().stream().anyMatch(input -> !input.endsWith("res"));

      Preconditions.checkArgument(!getBinaryJars().isEmpty() ^ hasClasspathsWithoutRes);
    } else if (getType() == Type.KOTLIN_JAVA_RUNTIME) {
      // KotlinJavaRuntime is not generated from a target and it depends on an external template
      // file so all those properties should be empty
      Preconditions.checkArgument(getTargets().isEmpty());
      Preconditions.checkArgument(getBinaryJars().isEmpty());
      Preconditions.checkArgument(getClassPaths().isEmpty());
      Preconditions.checkArgument(getSourceJars().isEmpty());
      Preconditions.checkArgument(getJavadocUrls().isEmpty());
      Preconditions.checkArgument(getSourceJars().isEmpty());
    }
  }

  @Override
  public void addAsDependency(
      DependencyType dependencyType, IjDependencyListBuilder dependencyListBuilder) {
    if (dependencyType.equals(DependencyType.COMPILED_SHADOW)) {
      dependencyListBuilder.addCompiledShadow(getName());
    } else {
      IjDependencyListBuilder.Scope scope = IjDependencyListBuilder.Scope.COMPILE;
      if (dependencyType.equals(DependencyType.TEST)) {
        scope = IjDependencyListBuilder.Scope.TEST;
      } else if (dependencyType.equals(DependencyType.RUNTIME)) {
        scope = IjDependencyListBuilder.Scope.RUNTIME;
      }
      dependencyListBuilder.addLibrary(getName(), scope, false /* exported */);
    }
  }

  @Value.Default
  public Type getType() {
    return Type.DEFAULT;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder extends ImmutableIjLibrary.Builder {}
}