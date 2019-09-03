cd %cd%
java -jar ../report.jar -server -Xmx150m -Xmn50m -Xms50m -X:NewRatio=4 -X:SurvivorRatio=4 -XX:+UseParallelGC -XX:ParallelGCThreads=4 -XX:+PrintGCDetail -XX:+PrintHeadAtGC -Xgclog:gclog.log