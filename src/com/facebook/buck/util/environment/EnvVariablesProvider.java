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

package com.facebook.buck.util.environment;

import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Provides access system environment variables of the current process. */
public class EnvVariablesProvider {
  private static final List<String> BUCK_ENV_VARS_TO_EXCLUDE = Arrays.asList(
    System.getenv().getOrDefault("BUCK_ENV_VARS_TO_EXCLUDE", "").split(","));

  @SuppressWarnings("PMD.BlacklistedSystemGetenv")
  public static ImmutableMap<String, String> getSystemEnv() {
    return getAllSystemEnv()
      .entrySet()
      .stream()
      .filter(e -> !BUCK_ENV_VARS_TO_EXCLUDE.contains(e.getKey()))
      .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private static Map<String, String> getAllSystemEnv() {
    return isWindows() ? getWindowsEnv() : System.getenv();
  }

  private static boolean isWindows() {
    return Platform.detect().getType() == PlatformType.WINDOWS;
  }

  private static Map<String, String> getWindowsEnv() {
    return System.getenv().entrySet().stream()
      .collect(Collectors.toMap(e -> e.getKey().toUpperCase(), Map.Entry::getValue));
  }
}
