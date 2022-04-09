#include <stdlib.h>
#include <connectionHandler.h>
#include <iostream>
#include <thread>


void Keyboard(ConnectionHandler& connectionHandler){
    while(true){
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        if (!connectionHandler.sendMessage(line)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
    }
}

int main (int argc, char *argv[]) {
    std::cout << "Client Connected!" << std::endl;
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    std::thread t(Keyboard, std::ref(connectionHandler));

    bool flag = true;
    while (flag) {

        bool ack;
        short opcode;
        std::string message;

        if (!connectionHandler.getMessage(ack, opcode, message)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            flag = false;
        }
        
        if(ack)
            std::cout << "ACK " << opcode;
        else
            std::cout << "ERROR " << opcode;

        if(ack && message != "")
            std::cout << std::endl << message;

        std::cout << std::endl;

        if(ack && opcode == 4)
            flag = false;
    }

    exit(0);
}
