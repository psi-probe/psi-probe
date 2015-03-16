

---

# General #

## What is PSI Probe? ##
PSI Probe is a fork of [Lambda Probe](http://www.lambdaprobe.org/).  It can do almost anything Tomcat Manager does, plus [a lot more](Features.md).

## Why fork Lambda Probe? ##
At the time of our fork, there had been no releases or commits for more than 2 years.  Many people on the Lambda Probe forums feared the project was dead.  We contacted the developers and received no reply, so we tried to revive the project ourselves.

Since then, we've learned that [MuleSoft](http://www.mulesoft.com/) purchased a [commercial license](http://www.theserverside.com/news/thread.tss?thread_id=57531#321130) for Lambda Probe from its creators and incorporated it into their [Tcat Server](http://www.mulesoft.com/tcat-server-enterprise-tomcat-application-server) product.  MuleSoft has [no plans](http://www.theserverside.com/news/thread.tss?thread_id=58164#323370) to contribute their changes back to the open-source community, as they were added to a commercially-licensed version of Lambda Probe, not the GPL version.  In light of this, we strongly suspect that open development of Lambda Probe by its creators will not resume.

Lambda Probe was created and maintained by Vlad Ilyushchenko (turbomonkey) and Andy Shapoval.  We want to extend a _very_ special thanks to both of them.

## What should I do if my question was not answered here? ##
Please join our [Google Group](http://groups.google.com/group/psi-probe-discuss) and ask it!


---

# Usage #

## How secure is PSI Probe? ##
PSI Probe comes with no warranty, but web access should be as secure as access to Tomcat Manager.

You can also secure the remote JMX connection if you choose to [enable it](InstallationApacheTomcat#Enable_Remote_JMX_(Optional).md).

## How do I install PSI Probe onto my server? ##
For the most part, it's just like deploying any other Java web application.  We have more detailed instructions for installing to [Apache Tomcat](InstallationApacheTomcat.md) and [JBoss Application Server](InstallationJBossAS.md).

## I found a bug in PSI Probe. What should I do? ##
Please see our [Troubleshooting](Troubleshooting.md) page.  If you can't find your problem there, please [report it](https://code.google.com/p/psi-probe/issues/entry), and include as much information as possible about your environment and the circumstances that led to the bug.  Snippets of log files, stack traces, and the like are incredibly helpful.  Please star the issue you submit and/or check it regularly, as we may ask for more details.

## I have a great idea. Can you add it to PSI Probe? ##
We'd love to hear your idea, whether it's for a [change](http://code.google.com/p/psi-probe/issues/entry?template=Enhance%20an%20existing%20feature) or [addition](http://code.google.com/p/psi-probe/issues/entry?template=Add%20a%20new%20feature), but we make no guarantees.  The chances improve dramatically if you include a patch.


---

# Development #

## When will the next version be released? ##
Please check the [development roadmap](DevelopmentRoadmap.md).  We don't have a fixed schedule yet, but so far we've averaged about two releases per year, not including patches.

## How are issues prioritized? ##
We use four metrics to assign priority:

  1. **Popularity:** We prioritize issues with many stars over issues with few stars.
  1. **Severity:** We prioritize defects over enhancements or features.
  1. **Simplicity:** We prioritize minor changes over major ones.
  1. **Unity:** We try to establish a theme for each release to help focus our efforts.

## Can you translate PSI Probe into my language? ##
Probably not.  The developers only speak English, but we welcome any and all translations.

## How can I contribute to this project? ##
Our goal with creating PSI Probe was not only to revive the project but also to allow a community to develop around it and help support it.  We welcome any help you are willing to provide.  Here are some suggestions:

  * [Download it](http://code.google.com/p/psi-probe/downloads/list) and use it.  It is free, after all.
  * Use the [issue tracker](http://code.google.com/p/psi-probe/issues/list) here to...
    * "Star" issues that you would like to see fixed.  This will help us assign priority.
    * Report any bugs you encounter.
    * Request enhancements or new features.
    * Submit translations of the program's [messages](http://psi-probe.googlecode.com/svn/trunk/web/src/main/conf/WEB-INF/messages.properties).
    * Submit patches for [unclaimed issues](http://code.google.com/p/psi-probe/issues/list?can=2&q=-has:owner) or ones labeled with [HelpWanted](http://code.google.com/p/psi-probe/issues/list?can=1&q=label:HelpWanted).  You can check out the source code [here](http://code.google.com/p/psi-probe/source/checkout).
    * Verify [fixed issues](http://code.google.com/p/psi-probe/issues/list?can=1&q=Status:FixedInSource+-Verified:Yes) in the latest trunk build.
  * Join our [Google Group](http://groups.google.com/group/psi-probe-discuss) and participate in discussions.

Anyone who provides a patch (or even just a detailed description of what changes need to be made to the code) will be made a Contributor of this project.  We want to acknowledge all such contributions, and that's our way of saying thanks.

## How do I become a project committer? ##
We are a bit skeptical about granting commit privileges to just anyone.  As the community grows, our hope is that experts and leaders will emerge.  Contributors who make frequent high-quality contributions may be invited to become Committers.


---

# Other #

## Why PSI? ##
It's a [Greek letter](http://en.wikipedia.org/wiki/Psi_(letter)) just like [lambda](http://en.wikipedia.org/wiki/Lambda_(letter)).

#### Okay, but why is it in all caps?  Is it an acronym or something? ####

Sure, why not?  Let's say it stands for Packaged Server Introspector.

For what it's worth, a capital psi (Ψ) looks like a [fork](http://en.wikipedia.org/wiki/Fork), and PSI Probe is a [forked](http://en.wikipedia.org/wiki/Fork_(software_development)) project.  Does that count for anything?

#### No, and you should be ashamed of yourself for even mentioning that. ####

I am. :(