#!/bin/bash
chmod -R 777 jre
nohup ./jre/bin/java -jar npwonder-centerconf.jar >/dev/null 2>&1 &