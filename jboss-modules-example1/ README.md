## JBoss-Modules Example1: Solving Jar Hell ##
Suggested JBoss-Modules based solution to [JAR Hell](https://en.wikipedia.org/wiki/Java_Classloader#JAR_hell), inspired by this [question](stackoverflow.com/questions/6909306/jar-hell-how-to-use-a-classloader-to-replace-one-jar-library-version-with-anoth) in Stack Overflow.

The problem consist in running two different versions of a class named `Library3`, first of them printing `This is version 1.` and the second one printing `This is version 2.`

First version being in jar : `lib/lib3-v1/lib3.jar`
And second one being in jar: `lib/lib3-v2/lib3.jar`

Building
-------
Using maven:

    mvn compile

Running
-------
Using maven:

    mvn -q exec:java

You'll see the expected results:

    This is version 1.
    This is version 2.

