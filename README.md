# Teamscale JaCoCo Verifier [![Build Status](https://travis-ci.com/cqse/teamscale-jacoco-verifier.svg?branch=master)](https://travis-ci.com/cqse/teamscale-jacoco-verifier)

Little tool to verify the contents JaCoCo XML reports for common error conditions.

## Download

* [Binary Distribution](https://github.com/cqse/teamscale-jacoco-verifier/releases)

## Documentation

* [Documentation](documentation/README.md)

## Development

### Build locally

* Import in Eclipse/IntelliJ as Gradle project
* Command line: `./gradlew assemble`

### Contributing

* Create a GitHub issue for changes
* Use pull requests. Complete the "Definition of Done" for every pull request.
* There's a Teamscale project, please fix all findings before submitting your pull request for review. The Teamscale coding guidelines and Definition of Done apply as far as possible with the available tooling.

### Publishing

When master has accumulated changes you want to release, please perform the following on master in a single commit:

- update [the changelog](CHANGELOG.md) and move all changes from the _Next release_ section to a new version, e.g. `v8.1.0`
- update the [build.gradle](build.gradle)'s `version` accordingly
- create a GitHub Release tag with the same version number and the text from the changleog.

Releases are numbered according to semantic versioning (see full [changelog](CHANGELOG.md)).

All tags are built automatically using [Travis CI](https://travis-ci.com/cqse/teamscale-jacoco-agent) with the release binaries being uploaded to the GitHub Releases.

