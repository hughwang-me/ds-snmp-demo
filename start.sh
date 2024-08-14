#!/bin/sh

PKG_PATH=`pwd`
MAIN_CLASS="com.uwjx.dssnmpdemo.DsSnmpDemoApplication"

run(){
  is_exist
    if [ $? -eq "1" ]; then
      echo "$MAIN_CLASS is already running pid is ${pid}"
    else
      start
     fi
}

start(){

  echo "" > $PKG_PATH/run.log

  java -jar ds-snmp-demo-0.0.1-SNAPSHOT.jar >> $PKG_PATH/run.log 2>&1 &

  echo "START SUCCESSFULLY ~"
}

stop() {
  is_exist
    if [ $? -eq "1" ]; then
      kill -9 ${pid}
      echo "pid = ${pid} is killed"
    else
      echo "$MAIN_CLASS is not running"
     fi
}

status() {
  is_exist
  if [ $? -eq "1" ]; then
    echo "$MAIN_CLASS is running，pid is ${pid}"
  else
    echo "$MAIN_CLASS is not running！"
  fi
}

is_exist() {
  pid=`ps -ef|grep $MAIN_CLASS|grep -v grep|awk '{print $2}' `
  if [ -z "${pid}" ]; then
   return 0
  else
    return 1
  fi
}

case "$1" in
  'start')
    run
    ;;
  'stop')
    stop
    ;;
  'status')
    status
    ;;
  *)
    run
    ;;
esac
exit 0