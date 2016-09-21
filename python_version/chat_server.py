#Warlon Zeng

from socket import *
from threading import *
from time import *

clients = []
addresses = []
logs = []
connected = 0

class chat_server():
    def __init__(self, port):
        self.host = gethostbyname(gethostname())
        self.port = port
        self.sock = socket(AF_INET, SOCK_STREAM)
        self.sock.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
        self.sock.bind((self.host, self.port))
        # Things below are static to the server and are meant to be shared to the clients

    def listen(self):
        self.sock.listen(10)
        print('Chat server started on ' + self.host + ':' + str(self.port))            
        while True:
            client, address = self.sock.accept()
            #process_client(client, address[0]).start()
            process_client(client, address[0])
            
class process_client(): # the difference between python and java is that java has extends thread while python doesnt
    def __init__(self, client, address): # client and address are passed in from base call, not init. don't need to static them
        self.client = client
        self.address = address
        global clients
        global addresses
        global logs
        global connected
        clients.append(client) # base class
        addresses.append(address) # base class
        connected += 1 # base class
        #connected_time = str(datetime.now().localtime())
        connected_time = ctime()
        connected_msg = ('[' + connected_time + '] ' + self.address + ' connected\r\n').encode('utf-8')
        #concurrency_msg = ('[' + ctime() + '] ' + str(connected) + ' clients currently connected\r\n').encode('utf-8')
        
        print(connected_msg)
        print(connected)

        self.client.send(('\r\n').encode('utf-8')) # for client interface only
        for client in clients:
            client.send(connected_msg)
        self.client.send(('<----------- ACTIVE CLIENTS  ----------->\r\n').encode('utf-8'))
        for address in addresses:
            self.client.send(('[' + ctime() + '] ' + address + '\r\n').encode('utf-8'))
        self.client.send(('[' + ctime() + '] ' + str(connected) + ' clients currently connected\r\n').encode('utf-8'))
        self.client.send(('<----------- ACTIVE CLIENTS  ----------->\r\n').encode('utf-8'))
        self.client.send(('<----------- LAST 50 MESSAGES  ----------->\r\n').encode('utf-8'))
        for msg in logs:
                self.client.send(msg)
        self.client.send(('<----------- LAST 50 MESSAGES  ----------->\r\n').encode('utf-8'))
        if len(logs) > 100: # delete older 50 msgs
            del logs[0:51]
        logs.append(connected_msg)
        
        def listen_client():
            while True:
                try:
                    dataRecv = self.client.recv(4096) # socket is unavaiable. don't try to communicate with dead socket
                    if len(logs) > 100:
                        del logs[0:51]
                    client_msg = ('[' + ctime() + ']' + self.address + ': ' + dataRecv.decode() + '\r\n').encode('utf-8')
                    for client in clients:
                        client.send(client_msg)
                    logs.append(client_msg)
                except error: # when socket is dead
                    global connected
                    clients.remove(self.client) # so we don't msg a dead socket
                    disconnected_time = ctime()
                    disconnected_msg = ('[' + disconnected_time + '] ' + self.address + ' disconnected\r\n').encode('utf-8')
                    for client in clients:
                        client.send(disconnected_msg)
                    logs.append(disconnected_msg)
                    addresses.remove(self.address)
                    connected -= 1
                    print(addresses)
                    print(connected)
                    self.client.close()
                    break
                
        Thread(target = listen_client).start()
            
if __name__ == "__main__":
    port = 51000
    chat_server(port).listen()
