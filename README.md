Buck
====

Buck is a build tool. To see what Buck can do for you,
check out the documentation at <http://buckbuild.com/>.

[![Build Status](https://travis-ci.org/facebook/buck.svg)](https://travis-ci.org/facebook/buck)

Installation
------------

To build Buck, run the following:

    git clone https://github.com/facebook/buck.git
    cd buck
    ant
    ./bin/buck --help

Updating the Buck executable in AMP
-----------------------------------

To update `buck.pex` from AMP, run the following in this repo:

    ant
    ./bin/buck build buck
    cp buck-out/gen/programs/buck.pex <location-of-AMP-directory>

License
-------
Apache License 2.0
