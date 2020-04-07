 * Copyright 2015-present Facebook, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import com.facebook.buck.io.filesystem.impl.DefaultProjectFilesystemFactory;
import com.facebook.buck.util.unarchive.ArchiveFormat;
import com.facebook.buck.util.unarchive.ExistingFileMode;
import java.nio.file.Files;
  private static final String HG_REPOS_ZIP = "hg_repos.zip";
    reposPath = explodeReposZip();
    repoTwoCmdLine = makeCmdLine(reposPath.resolve(REPO_TWO_DIR));
    repoThreeCmdLine = makeCmdLine(reposPath.resolve(REPO_THREE_DIR));
    exception.expect(VersionControlCommandFailedException.class);
    repoThreeCmdLine.diffBetweenRevisions("adf7a0", "adf7a0").get();
        repoThreeCmdLine.diffBetweenRevisions("b1fd7e", "2911b3").get()) {
  private static Path explodeReposZip() throws InterruptedException, IOException {
    return explodeReposZip(tempFolder.getRoot().toPath());
  private static Path explodeReposZip(Path destination) throws InterruptedException, IOException {
    Path hgRepoZipPath = testDataDir.resolve(HG_REPOS_ZIP);
    Path hgRepoZipCopyPath = destination.resolve(HG_REPOS_ZIP);
    Files.copy(hgRepoZipPath, hgRepoZipCopyPath, REPLACE_EXISTING);

    Path reposPath = destination.resolve(REPOS_DIR);

    ArchiveFormat.ZIP
        .getUnarchiver()
        .extractArchive(
            new DefaultProjectFilesystemFactory(),
            hgRepoZipCopyPath,
            reposPath,
            ExistingFileMode.OVERWRITE_AND_CLEAN_DIRECTORIES);

    return reposPath;