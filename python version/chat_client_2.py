# chat_client.py

from socket import *
from threading import *
#import threading
from time import *
import sys
#from subprocess import *
#import curses


class chat_client():
    def __init__(self, server, port):
        self.port = port
        self.server_name = server
        self.client_address = gethostbyname(gethostname())
        self.server = socket(AF_INET, SOCK_STREAM)
        try:
            self.server.connect((self.server_name, self.port))
        except error:
            print('Server is not running')
            sys.exit() # os._exit() > sys.exit() > exit() in terms of permissions
            
    def read(self):
        while True:
            try:
                dataRecv = self.server.recv(4096)
                print(dataRecv.decode('utf-8'))
            except error: # when server is dead... aws ec2 cloud instance dead..
                print("Server is dead")
                break

    def write(self):
        while True:
            client_msg = (input(self.client_address + ': '))
            self.server.send((client_msg).encode('utf-8'))
            sleep(0.1)

    def run(self):
        Thread(target = self.read).start() # functions do not need params of self
        sleep(0.5)
        self.write()

if __name__ == "__main__":
    port = 51000
	host = input('host name: ')
    chat_client(host, port).run()
