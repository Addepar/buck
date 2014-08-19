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

import com.facebook.buck.rules.BuildRuleParams;
import com.facebook.buck.rules.BuildRuleResolver;
import com.facebook.buck.rules.BuildRuleType;
import com.facebook.buck.rules.Description;
import com.google.common.base.Optional;

import java.nio.file.Path;

import com.facebook.infer.annotation.SuppressFieldNotInitialized;

public class IosBinaryDescription implements Description<IosBinaryDescription.Arg> {
  public static final BuildRuleType TYPE = new BuildRuleType("ios_binary");

  @Override
  public Arg createUnpopulatedConstructorArg() {
    return new Arg();
  }

  @Override
  public <A extends Arg> IosBinary createBuildRule(
      BuildRuleParams params,
      BuildRuleResolver resolver,
      A args) {
    return new IosBinary(params, args, TargetSources.ofAppleSources(args.srcs));
  }

  @Override
  public BuildRuleType getBuildRuleType() {
    return TYPE;
  }

  @SuppressFieldNotInitialized
  public static class Arg extends AppleNativeTargetDescriptionArg {
    public Optional<Path> infoPlist;
  }
}
