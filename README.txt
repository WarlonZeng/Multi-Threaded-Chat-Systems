tested with 3 chat clients, all 3 connected simultaneously
all received same message simultaneously as well

1st window - specify ip or name // i used localhost for testing
2nd window (after first window) - specify username

To RUN: You will need to compile them on your instance...

On the command line:

git clone https://github.com/WarlonZeng/multi_threaded_chat_system.git
cd multi_threaded_chat_system
javac chat_server.java
java chat_server

Server listens at port 50,000
There are client softwares to connect to server. By default the host is ec2-54-161-219-126.compute-1.amazonaws.com
Port is also automatically configured at 50,000

May do a Nginx reverse-proxy + multiple servers and data communication in the future.
