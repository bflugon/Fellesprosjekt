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
        while True:
            self.send(raw_input("Skriv din melding her"))
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
            pass
        elif data[response] == 'logout':
            pass


    def connection_closed(self, connection):
        pass

    def send(self, data):
        self.connection.sendall(data)

    def force_disconnect(self):
        pass


if __name__ == "__main__":
    client = Client()
    client.start('localhost', 9999)
