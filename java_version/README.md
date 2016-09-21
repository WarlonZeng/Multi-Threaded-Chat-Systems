# Multi-Threaded-Chat-Systems-Java

tested with 3 chat clients, all 3 connected simultaneously
all received same message simultaneously as well

1st window - specify ip or name // i used localhost for testing
2nd window (after first window) - specify username

## To RUN: 

You will need to compile them on your instance...

On the command line:

for SERVER DEPLOYMENT
git clone https://github.com/WarlonZeng/Multi-Threaded-Chat-Systems.git
cd java_version
javac chat_server.java
nohup java chat_server or nohup java -cp . chat_server

for CLIENT CONNECT
javac chat_client1.java
java chat_client1

Server listens at port 50,000