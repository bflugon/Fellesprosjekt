'''
KTN-project 2013 / 2014
'''
import socket
import json


class Client(object):

    def __init__(self):
        self.connection = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    def start(self, host, port):
        self.connection.connect((host, port))
        #messageworker = MessageWorker()
        #message.start()
        username = ''
        username = raw_input('Enter username:')
        self.send(json.dumps({'request': 'login', 'message': username}))
        received_data = self.connection.recv(1024)
        while True:
            self.send(raw_input("Type a message"))
            received_data = self.connection.recv(1024).strip()
            print 'Received from server: ' + received_data
        self.connection.close()

    def message_received(self, message, connection):
        data = json.dumps(message)
        if data.has_key(error):
            pass
        elif data[response] == 'login':
            pass
        elif data[response] == 'message':
            pass # Do nothing, will be removes
        elif data[response] == 'logout':
            pass #Idk, prompt login?


    def connection_closed(self, connection):
        pass

    def send(self, data):
        self.connection.sendall(data)

    def force_disconnect(self):
        pass


if __name__ == "__main__":
    client = Client()
    client.start('localhost', 9999)
