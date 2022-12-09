# RegexStaticAnalysis
A tool to perform static analysis on regexes to determine whether they are vulnerable to ReDoS.

We downloaded the tool from https://github.com/NicolaasWeideman/RegexStaticAnalysis

There are three options for running the code:
   1. `./run.sh <command line args>`
   2. `./RegexStaticAnalysis.jar <command line args>` (if you created the executable jar in the compilation step)
   3. `java -cp ./bin driver.Main <command line args>` (which is basically the bash script inside run.sh)

To learn how to use the code, you can read the usage statement that is printed when the code is run without any command line arguments.

We modified the tool so that we do not consider the mismatch analysis it does after finding EDA/IDA for the NFA of the regex.

