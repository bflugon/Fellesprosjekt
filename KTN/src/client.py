'''
KTN-project 2013 / 2014
'''
import socket
import json
from MessageWorker import ReceiveMessageWorker


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
                self.send(json.dumps({'request': 'login', 'username': message.split(' ', 1)[1]}))
            elif message == '/logout':
                self.send(json.dumps({'request': 'logout'}))
            else:
                self.send(json.dumps({'request':'message', 'message': message}))
        self.connection.close()

    def message_received(self, message, connection):
        data = json.loads(message)
        if data.has_key('error'):
            print('Du er ikke logget inn')
        elif data['response'] == 'login':
            for message in data['messages']:
                if message:
                    self.read(message)
        elif data['response'] == 'message':
            self.read(message)
        elif data['response'] == 'logout':
            pass #Idk, prompt login?


    def connection_closed(self, connection):
        pass

    def read(self, liste):
        print(liste[0] + '\t' + liste[1] + ':' + liste[2])

    def send(self, data):
        self.connection.sendall(data)

    def force_disconnect(self):
        pass


if __name__ == "__main__":
    client = Client()
    client.start('localhost', 9999)
