#include <connectionHandler.h>
#include <boost/lexical_cast.hpp>

using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;

ConnectionHandler::ConnectionHandler(string host, short port) : host_(host), port_(port), io_service_(),
                                                                socket_(io_service_) {}

ConnectionHandler::~ConnectionHandler() {
    close();
}

bool ConnectionHandler::connect() {
    try {
        tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_);
        boost::system::error_code error;
        socket_.connect(endpoint, error);
        if (error)
            throw boost::system::system_error(error);
    }
    catch (std::exception &e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp) {
            tmp += socket_.read_some(boost::asio::buffer(bytes + tmp, bytesToRead - tmp), error);
        }
        if (error)
            throw boost::system::system_error(error);
    } catch (std::exception &e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp) {
            tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
        if (error)
            throw boost::system::system_error(error);
    } catch (std::exception &e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::getMessage(bool &ack, short &opcode, std::string &message) {
    short op = 0;
    char b = 0;
    message = "";
    if (!getBytes(&b, 1))
        return false;
    op = (short) (0x100 * (short) b);
    if (!getBytes(&b, 1))
        return false;
    op += (short) (b & 0xFF);
    if (!getBytes(&b, 1))
        return false;
    opcode = (short) (0x100 * (short) b);
    if (!getBytes(&b, 1))
        return false;
    opcode += (short) (b & 0xFF);

    if (op == 12) {
        ack = true;
        bool flag = true;
        while (flag) {
            if (!getBytes(&b, 1))
                return false;
            if (b != '\0')
                message += b;
            else
                flag = false;
        }
    } else
        ack = false;
    return true;
}

bool ConnectionHandler::sendMessage(std::string userInput) {
    char toSend[128];
    int index = 0, fieldIndex = -1;
    size_t pos = 0;
    short op;
    std::string command;
    bool continueFlag = true;
    while (continueFlag) {
        continueFlag = (pos = userInput.find(" ")) != std::string::npos;
        command = userInput.substr(0, pos);
        if (fieldIndex == -1) {
            if (command == "ADMINREG") { op = 1; }
            else if (command == "STUDENTREG") { op = 2; }
            else if (command == "LOGIN") { op = 3; }
            else if (command == "LOGOUT") { op = 4; }
            else if (command == "COURSEREG") { op = 5; }
            else if (command == "KDAMCHECK") { op = 6; }
            else if (command == "COURSESTAT") { op = 7; }
            else if (command == "STUDENTSTAT") { op = 8; }
            else if (command == "ISREGISTERED") { op = 9; }
            else if (command == "UNREGISTER") { op = 10; }
            else if (command == "MYCOURSES") { op = 11; }
            else { op = 0; }

            index = encodeShort(toSend, index, op);
        } else {
            if (op == 8) {
                if (fieldIndex == 0) {
                    index = encodeString(toSend, index, substr);
                }
            }
            if (op == 5 || op == 6 || op == 7 || op == 9 || op == 10) {
                if (fieldIndex == 0) {
                    short toSend = boost::lexical_cast<short>(substr);
                    index = encodeShort(toSend, index, toSend);
                }
            }
            if (op == 1 || op == 2 || op == 3) {
                if (fieldIndex == 0 || fieldIndex == 1) {
                    index = encodeString(toSend, index, substr);
                }
            }
        }
        userInput.erase(0, pos + 1);
        fieldIndex += 1;
    }
    return sendBytes(toSend, index);
}


int ConnectionHandler::encodeShort(char *buff, int index, short toEncode) {
    buff[index] = (char) ((toEncode & (unsigned short) 0xFF00) >> 8);
    buff[index + 1] = (char) ((unsigned short) toEncode & (unsigned short) 0x00FF);
    return index + 2;
}

int ConnectionHandler::encodeString(char *buff, int index, std::string toEncode) {
    size_t iter = 0;
    while (iter < (int toEncode.size())){
        buff[index + iter] == toEncode[iter];
        iter++;
    }
    buff[index + toEncode.size()] = '\0';
    return index + toEncode.size() + 1;
}

void ConnectionHandler::close() {
    try {
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}
