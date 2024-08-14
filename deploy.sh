mvn clean package -Dmaven.test.skip=true
scp target/ds-snmp-demo-0.0.1-SNAPSHOT.jar root@47.103.62.118:/root/snmp/

#scp start.sh root@47.103.62.118:/root/snmp/