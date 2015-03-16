

---


# Major Release #

  * _Same as Minor Release._

# Minor Release #
  1. **Branch**
    1. Create branch from trunk.
      * Commit message: _Creating x.x release branch._
    1. Check out branch.
    1. In branch, remove "SNAPSHOT" from version number and update SCM URLs.
      * e.g. `1.0.0-SNAPSHOT` becomes `1.0.0`
      * e.g. `scm:svn:.../trunk/` becomes `scm:svn:.../branches/1.0/`
      * Commit message: _Removing "SNAPSHOT" from version number and updating SCM URLs._
  1. **Build**
    1. Create probe.war from branch: `mvn -Prelease clean package`
      * Be sure to use JDK 1.4 and Maven 2.0.9+.
    1. Create Changelog.txt file from [Changelogs](Changelogs.md) and [DevelopmentRoadmap](DevelopmentRoadmap.md).
    1. Create ZIP from probe.war and Changelog.txt.
  1. **Release**
    1. Create tag from branch.
      * Commit message: _Tagging x.x.x release_
    1. Upload ZIP to GC as a Featured Download.
    1. Remove Featured label from previous release.
    1. Change status of issues to FixReleased.
      * Issue comment: _x.x.x released._
    1. Update [Changelogs](Changelogs.md) and [DevelopmentRoadmap](DevelopmentRoadmap.md).
      * Commit message: _Adding x.x.x changelog link._
    1. Announce.
  1. **Prep**
    1. In trunk, increment version numbers.
      * e.g. `1.0.0-SNAPSHOT` becomes `1.1.0-SNAPSHOT`
      * Commit message: _Changing version number to x.x.x_

# Patch Release #
  1. **Merge**
    1. In branch, increment version number and add "SNAPSHOT."
      * e.g. `1.0.0` becomes `1.0.1-SNAPSHOT`
      * Commit message: _Preparing for patch._
    1. Merge changes from trunk into branch (or vice versa if applicable).
      * Commit message: _Merging trunk:x into branches/x.x._
    1. In branch, remove "SNAPSHOT" from version number.
      * e.g. `1.0.1-SNAPSHOT` becomes `1.0.1`
      * Commit message: _Changing version number to x.x.x_
  1. **Build**
    * _Same as Minor Release._
  1. **Release**
    * _Same as Minor Release._