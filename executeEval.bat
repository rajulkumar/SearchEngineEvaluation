:set jdk bin path of the system here
set path=%PATH%;C:\Program Files\Java\jdk1.8.0_05\bin
set CLASSPATH=%CLASSPATH%;./src/com/eval;./src;.

javac ./src/com/eval/Evaluator.java

java com/eval/Evaluator
