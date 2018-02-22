#!/usr/bin/env bash
# Start tomcat

service tomcat8 restart
sleep 3;
echo "restarting..."
ps --no-headers -p `cat /var/run/tomcat8.pid`
if [ $? -ne 0 ] 
	then service tomcat8 restart;
fi
echo "tomcat running at" `cat /var/run/tomcat8.pid`