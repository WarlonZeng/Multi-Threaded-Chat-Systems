# Multi-Threaded-Chat-Systems-Python
Multi-threaded chat system in python

For the Java version: https://github.com/WarlonZeng/multi_threaded_chat_system_java

!! READ !!

You will probably need a proper python gui or 3rd party library to see the stdout (print) stream realtime. 
This chat system built in python works FULLY OPERATIONAL, HOWEVER, the user must enter a stdin (input) for socket data recv print statements
to flow in the console. Data recv is still receiving messages, but cannot show them because python's input function blocks the current thread execution flow,
basically locking the thread. This becomes an issue since stdin and stdout share the same console, thus locking each other out and taking turns. This
is where a proper gui is needed for a dedicated stream flow and keeping input separated.
