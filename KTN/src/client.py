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
            if message.startswith('/'):
                if message.startswith('/login') and message.count(' ') < 2 and len(message) > 7:
                    self.send(json.dumps({'request': 'login', 'username': message.split(' ', 1)[1]}))
                elif message == '/logout':
                    self.send(json.dumps({'request': 'logout'}))
                else:
                    print 'Invalid command'
                    continue
            else:
                self.send(json.dumps({'request':'message', 'message': message}))
        self.connection.close()

    def message_received(self, message, connection):
        data = json.loads(message)
        if data.has_key('error'):
            print data['error']
        elif data['response'] == 'login':
            for message in data['messages']:
                print message
        elif data['response'] == 'message':
            print data['message']
        elif data['response'] == 'logout':
            print "Logged out"


    def connection_closed(self, connection):
        pass

    def send(self, data):
        self.connection.sendall(data)

    def force_disconnect(self):
        pass


if __name__ == "__main__":
    client = Client()
    host = raw_input('Enter the address to the host (localhost)')
    port = int(raw_input('Enter the portnumber of the server (9999)'))
    client.start(host, port)
