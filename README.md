<p align="center">
  <img src="https://buck.build/static/og.png" alt="logo" width="20%" />
</p>
<h1 align="center">
  Buck
</h1>
<p align="center">
  Buck is a build tool. To see what Buck can do for you, check out the documentation at http://buck.build/.
</p>

[![Build Status](https://circleci.com/gh/facebook/buck.svg?style=svg)](https://circleci.com/gh/facebook/buck)

Installation
------------

Since Buck is used to build Buck, the initial build process involves 2 phases:

##### 1. Clone the Buck repository and bootstrap it with ant:

    git clone --depth 1 https://github.com/facebook/buck.git
    cd buck
    ant

You must be using Java 8 or 11 for this to compile successfully. If you see compilation errors from ant, check your `JAVA_HOME` is pointing at one of these versions.

##### 2. Use bootstrapped version of Buck to build Buck:

    ./bin/buck build --show-output buck
    # output will contain something like
    # //programs:buck buck-out/gen/programs/buck.pex
    buck-out/gen/programs/buck.pex --help

##### Prebuilt buck binaries

Pre-built binaries of buck for any buck `sha` can be downloaded from `https://jitpack.io/com/github/facebook/buck/<sha>/buck-<sha>.pex`. The very first time a version of buck is requested, it is built via [jitpack](https://jitpack.io/). As a result, it could take a few minutes for this initial binary to become available. Every subsequent request will just serve the built artifact directly. This functionality is available for any fork of buck as well, so you can fetch `https://jitpack.io/com/github/<github-user-or-org>/buck/<sha>/buck-<sha>.pex`

For buck binaries built for JDK 11, modify end of the url to `buck-<sha>-java11.pex`.

Feature Deprecation
-------------------

Buck tries to move fast with respect to its internals. However, for user facing features (build rules, command line interface, etc), the Buck team tries to have a graceful deprecation process. Note that this generally applies only to documented functionality, or functionality that is less documented, but appears to be in wide use. That process is:

- An issue is opened on Github suggesting what will be deprecated, and when it will be removed. For larger features that are deprecated, there may be a period when the default is the new setting, and the old behavior may only be used with a configuration change.
  - [In-progress deprecation issues](https://github.com/facebook/buck/issues?utf8=%E2%9C%93&q=is%3Aopen+label%3Aannouncement+label%3Adeprecation) are tagged with 'announcement' and 'deprecation'
- A change is submitted to Buck that puts the old behavior behind a configuration flag and sets the default to the old behavior. These flags can be found at https://buck.build/concept/buckconfig.html#incompatible.
- For larger features, a change eventually is put in place that sets the default to the new behavior. e.g. when Skylark becomes the default build file parser.
- When the removal date is reached, a change is submitted to remove the feature. At this point, the configuration value will still parse, but will not be used by Buck internally.

Developing with the Buck executable in AMP
-----------------------------------

To build and use a developmental `buck.pex` in AMP temporarily, run the following in this repo:

    ant
    ./bin/buck build --config java.target_level=11 --config java.source_level=11  --config python.interpreter=python2 --config python.pex_flags='' buck --show-output
    cp ./buck-out/gen/<hash>/programs/buck.pex <location-of-AMP-directory>
    touch <location-of-AMP-directory>/.ignore-buck-version

Note: `<hash>` is an 8 letter SHA prefix that will depend on which buck commit you're on. `.ignore-buck-version` is important since without that file, AMP will try to download the version of buck specified in `.buckversion_` instead of using the one you just copied into AMP. 

If ant fails with compilation errors on the latest release, try running

    ant clean all

Releasing a new version of Buck in AMP
-----------------------------------

Once you're satisfied with a new version of Buck, you can release it to AMP by following these steps:

##### 1. Merge the new release into github. 

Raise a PR against the branch `v2022.05.05.01` in https://github.com/Addepar/buck. Get it approved and merged. 

##### 2. Create a new release

Go to https://github.com/Addepar/buck/releases and draft a new release, incrementing the version by 1, e.g. v7 to v8. 
Make sure to compute the buck.pex SHA1 hash by running `shasum buck.pex` in this repo and make a note of this hash in the release notes.  
Also include a link to the PR created in step 1 above.

##### 2. Update the version and buck SHA1 hash in AMP

Raise a PR in AMP updating the buck version and SHA1 hash in `buck.sh` in the AMP repo. 
See [this PR](https://github.com/Addepar/AMP/pull/68478/files#diff-dcd8a834dc1ea51b6d597c2247ada269b6e35bdd254a2907925d703d506f1872) for an example. 

License
-------
Apache License 2.0
