#ifndef CONNECTION_HANDLER__
#define CONNECTION_HANDLER__
                                           
#include <string>
#include <iostream>
#include <boost/asio.hpp>

using boost::asio::ip::tcp;

class ConnectionHandler {
private:
	const std::string host_;
	const short port_;
	boost::asio::io_service io_service_;
	tcp::socket socket_; 
 
public:
    ConnectionHandler(std::string host, short port);
    virtual ~ConnectionHandler();

    bool connect();

    bool getBytes(char bytes[], unsigned int bytesToRead);

    bool sendBytes(const char bytes[], int bytesToWrite);
	
    static int encodeShort(char* buff, int index, short toEncode);
    static int encodeString(char* buff, int index, std::string toEncode);

    bool getMessage(bool& ack, short& opcode, std::string& message);
    bool sendMessage(std::string userInput);
	

    void close();
 
};
#endif
