'''
KTN-project 2013 / 2014
Very simple server implementation that should serve as a basis
for implementing the chat server
'''
import SocketServer
import json
from datetime import datetime

'''
The RequestHandler class for our server.

It is instantiated once per connection to the server, and must
override the handle() method to implement communication to the
client.
'''

clients = {}

messages = []

class CLientHandler(SocketServer.BaseRequestHandler):
    def handle(self):
        # Get a reference to the socket object
        self.connection = self.request
        # Get the remote ip adress of the socket
        self.ip = self.client_address[0]
        # Get the remote port number of the socket
        self.port = self.client_address[1]
        user = ''
        print 'Client connected @' + self.ip + ':' + str(self.port)
        while True:
            # Wait for data from the client
            data = self.connection.recv(1024).strip()
            # Check if the data exists
            # (recv could have returned due to a disconnect)
            if not data:
                break
            data = json.loads(data)
            if data['request'] == 'message':
                message = user + ': ' + data['message']
                print message
                messages.append(message)
                response = json.dumps({'response': 'message', 'message': message})
                for client in clients:
                    clients[client].sendall(response)
               
                response = {'response': 'message', 'error': 'Not logged in'}
            elif data['request'] == 'login':
                #sjekk gyldig brukernavn
                tempUser = data['username'] 
                if not tempUser.replace('_', '').isalnum():
                    self.connection.sendall(json.dumps({'response': 'login', 'error': 'Invalid username!', 'username': tempUser}))
                elif tempUser in clients:
                    self.connection.sendall(json.dumps({'response': 'login', 'error': 'Name already taken', 'username': tempUser}))
                elif self.connection in clients:
                    self.connection.sendall(json.dumps({'response': 'login', 'error': 'Already logged in', 'username': tempUser}))
                else:
                    user = tempUser
                    clients[user] = self.connection
                    self.connection.sendall(json.dumps({'response': 'login', 'username': user, 'messages': messages}))
            elif data['request'] == 'logout':
                if self.connection in clients:
                    response = {'response': 'logout', 'username': user}
                else:
                    response = {'response': 'logout', 'error': 'Not logged in'}
                self.connection.sendall(json.dumps(response))
                clients.remove(user)
                user = ''


            

'''
This will make all Request handlers being called in its own thread.
Very important, otherwise only one client will be served at a time
'''


class ThreadedTCPServer(SocketServer.ThreadingMixIn, SocketServer.TCPServer):
    pass

if __name__ == "__main__":
    HOST = raw_input('Enter the address of the server (localhost')
    PORT = int(raw_input('Enter the portnumber to use (9999)'))

    # Create the server, binding to localhost on port 9999
    server = ThreadedTCPServer((HOST, PORT), CLientHandler)

    # Activate the server; this will keep running until you
    # interrupt the program with Ctrl-C
    server.serve_forever()
