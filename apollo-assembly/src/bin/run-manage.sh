#!/usr/bin/env bash

readonly APP_HOME=$(dirname $(cd `dirname $0`; pwd))

readonly JAVA_HOME="$APP_HOME/jre"
readonly CONFIG_HOME="$APP_HOME/config/"
readonly LIB_HOME="$APP_HOME/lib"
readonly LOGS_HOME="$APP_HOME/logs"

readonly APP_MAIN_CLASS="npwonder-centerconf.jar"
readonly PID_FILE="$LOGS_HOME/application.pid"
readonly LOG_CONFIG="$CONFIG_HOME/logback-spring.xml"

readonly JAVA_RUN="-Dlogs.home=$LOGS_HOME -Dlogging.config=$LOG_CONFIG -Dspring.config.location=file:$CONFIG_HOME -Dspring.pid.file=$PID_FILE -Dspring.pid.fail-on-write-error=true"
readonly JAVA_OPTS="-server -Xms512m -Xmx512m -XX:PermSize=128M -XX:MaxPermSize=256M $JAVA_RUN"

readonly JAVA="$JAVA_HOME/bin/java"


PID=0

function env(){
    if [ ! -x "$LOGS_HOME" ]
    then
      mkdir $LOGS_HOME
    fi
    chmod +x -R "$JAVA_HOME/bin/"
}

function checkPid() {
    PID=$(ps -ef | grep $APP_MAIN_CLASS | grep -v 'grep' | awk '{print int($2)}')
    if [ -n "$PID" ]
    then
      echo "Found APP=$APP_MAIN_CLASS and PID=$PID"
    else
      echo "Not Found APP=$APP_MAIN_CLASS"
      PID=0
    fi
}

function start() {
   checkPid
   if [ $PID -ne 0 ]
   then
      echo "================================"
      echo "warn: $APP_MAIN_CLASS already started! (PID=$PID)"
      echo "================================"
   else
      echo "Starting $APP_MAIN_CLASS ..."
      JAVA_CMD="nohup $JAVA $JAVA_OPTS -jar $LIB_HOME/$APP_MAIN_CLASS > /dev/null 2>&1 &"
      echo "Exec cmmand : $JAVA_CMD"
      sh -c "$JAVA_CMD"
      sleep 3
      checkPid
      if [ $PID -ne 0 ]
      then
         echo "(PID=$PID) [OK]"
      else
         echo "User command status check status."
      fi
   fi
}

function stop() {
   checkPid
   if [ $PID -ne 0 ]
   then
      echo -n "Stopping $APP_MAIN_CLASS ...(PID=$PID) "
      kill -9 $PID
      if [ $? -eq 0 ]
      then
         echo "[OK]"
      else
         echo "[Failed]"
      fi
      checkPid
      if [ $PID -ne 0 ]
      then
         stop
      fi
   else
      echo "================================"
      echo "warn: $APP_MAIN_CLASS is not running"
      echo "================================"
   fi
}

function status() {
   checkPid
   if [ $PID -ne 0 ]
   then
      echo "$APP_MAIN_CLASS is running! (PID=$PID)"
   else
      echo "$APP_MAIN_CLASS is not running"
   fi
}

function info() {
   echo "System Information:"
   echo
   echo "****************************"
   echo `head -n 1 /etc/issue`
   echo `uname -a`
   echo
   echo "JAVA_HOME=$JAVA_HOME"
   echo
   echo "JAVA Environment Information:"
   echo `$JAVA_HOME/bin/java -version`
   echo
   echo "APP_HOME=$APP_HOME"
   echo "APP_MAIN_CLASS=$APP_MAIN_CLASS"
   echo
   echo "****************************"
}

function main(){
    env
    case "$1" in
      'start')
        start
        ;;
      'stop')
        stop
        ;;
      'restart')
        stop
        start
        ;;
      'status')
        status
        ;;
      'info')
        info
        ;;
       *)
        echo "Usage: $0 {help|start|stop|restart|status|info}"
        ;;
    esac
    exit 0
}

#go
main $*