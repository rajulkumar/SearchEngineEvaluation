#Set the jdk bin path of the system at <...> 
export PATH=$PATH:<Path of jdk bin on the system>
export CLASSPATH=$CLASSPATH:./src/com/eval:.

javac ./src/com/eval/Evaluator.java

java com/eval/Evaluator
