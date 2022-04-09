CC = g++
CFLAGS = -g -Wall -Weffc++ -std=c++11 -c -Iinclude
LFLAGS = -L/usr/lib -lboost_system -pthread

all: BGRSclient

bin/connectionHandler.o: src/connectionHandler.cpp
	$(CC) $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/main.o: src/main.cpp
	$(CC) $(CFLAGS) -o bin/main.o src/main.cpp

BGRSclient: bin/main.o bin/connectionHandler.o
	$(CC) -o bin/BGRSclient bin/main.o bin/connectionHandler.o $(LFLAGS)

.PHONY: clean
clean:
	rm -f bin/*
