#!/bin/bash

CMD=$1
APP='report'

# Check processing .
function process(){
        PROCESS=`ps aux|grep $APP.jar|grep -v grep`
        PID=(${PROCESS})
        echo ${PID[1]}
}

PID=$(process)

if [ ! -z $CMD ] &&  [ $CMD='stop' ] ; then
        if [ ! -z $PID ]; then
                echo 'Stop application process,PID:'$PID
                sudo -u www kill -9 $PID
        fi
        echo 'Stop application success'
        exit 0
fi

if [ ! -z $PID ];then
        echo 'Process is exists ,PID:'$PID
        exit 0
fi

# Start Application ...
echo 'Start application ...'
java -jar ../$APP.jar -server -Xmx150m -Xmn50m -Xms50m -X:NewRatio=4 -X:SurvivorRatio=4 -XX:+UseParallelGC -XX:ParallelGCThreads=4 -XX:+PrintGCDetail -XX:+PrintHeadAtGC -Xgclog:gclog.log > /dev/null 2>&1 &

PID=$(process)

if [ ! -z $PID ]; then
        echo 'Boot app start success , PID:' $PID
else
        echo 'Boot app PID not found !'
fi
