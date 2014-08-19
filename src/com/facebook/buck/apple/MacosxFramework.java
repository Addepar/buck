/*
 * Copyright 2013-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.apple;

import com.facebook.buck.cxx.CompilerStep;
import com.facebook.buck.rules.BuildRuleParams;
import com.facebook.buck.step.Step;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;

import java.nio.file.Path;

public class MacosxFramework extends AbstractAppleNativeTargetBuildRule {

  private final Optional<Path> infoPlist;

  public MacosxFramework(
      BuildRuleParams params,
      MacosxFrameworkDescription.Arg arg,
      TargetSources targetSources) {
    super(params, arg, targetSources);
    this.infoPlist = arg.infoPlist;
  }

  public Optional<Path> getInfoPlist() {
    return infoPlist;
  }

  @Override
  protected ImmutableList<Step> getFinalBuildSteps(
      ImmutableSortedSet<Path> files,
      Path outputFile) {
    if (files.isEmpty()) {
      return ImmutableList.of();
    } else {
      // TODO(user): This needs to create a dylib, not a static library.
      return ImmutableList.<Step>of(
          new CompilerStep(
              /* compiler */ getCompiler(),
              /* shouldLink */ true,
              /* srcs */ files,
              /* outputFile */ outputFile,
              /* shouldAddProjectRootToIncludePaths */ false,
              /* includePaths */ ImmutableSortedSet.<Path>of(),
              /* commandLineArgs */ ImmutableList.<String>of()));

    }
  }

  @Override
  protected String getOutputFileNameFormat() {
    return "%s";
  }
}
