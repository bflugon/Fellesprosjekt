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
        messageWorker = ReceiveMessageWorker(self, self.connection)
        messageWorker.start()
        while True:
            message = ''
            message = raw_input()
            if message.startswith('/login'):
                self.send(json.dumps({'request': 'login', 'message': message.split(' ', 1)[1]}))
            elif message == '/logout':
                self.send(json.dumps({'request': 'logout'}))
            else:
                self.send(json.dumps({'request':'message', 'message': message}))
        self.connection.close()

    def message_received(self, message, connection):
        data = json.dumps(message)
        if data.has_key(error):
            if data[response] == 'login':
                pass #Logg inn på nytt
            elif data[response] == 'message':
                pass # Do nothing, will be removes
            elif data[response] == 'logout':
                pass #Idk, prompt login?
        elif data[response] == 'login':
            print(data.messages)
        elif data[response] == 'message':
            print(message)
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
