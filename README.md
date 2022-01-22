### pplication classes vs. library classes
Classes that Soot actually processes are called application classes. 
This is opposed to library classes, which Soot does not process but only uses for type resolution. Application classes are usually those explicitly stated on the command line or those classes that reside in a directory referred to via ```–process-dir```.

When you use the ```-app``` option, however, then Soot also processes all classes referenced by these classes.
 It will not, however, process any classes in the JDK, i.e. classes in one of the java.* and com.sun.* packages. 
If you wish to include those too you have to use the special ```–i ```option, e.g. -i java. See the guide on command line options for this and other command line options.

