//============================================================================
// Name        : 1_SEM5.cpp
// Author      : 
// Version     :
// Copyright   : Your copyright notice
// Description : Hello World in C++, Ansi-style
//============================================================================

//Producer Consumer Problem

#include <iostream>
#include <pthread.h>	//for producer,consumer thread
#include <semaphore.h>
#include <random>
#include <unistd.h>

using namespace std;

#define buffer_size 10

class Products
{
public:
    int buffer[buffer_size]; //array of unused products
    int counter;

    Products()
    {
        counter = 0;
    }
};

sem_t full, empty;
pthread_mutex_t mutex;

Products p;

void *produce(void *arg)
{
    while (1)
    {
        sleep(1);
        sem_wait(&empty);
        pthread_mutex_lock(&mutex); //used for critical section

        int item=rand()%100;

        //critical section
        p.buffer[p.counter]=item;
        p.counter=(p.counter+1)%buffer_size;
        cout << "Produced item is: " << item << endl;

        pthread_mutex_unlock(&mutex);
        sem_post(&full);
    }
}

void *consume(void *arg)
{
    while (1)
    {
        sleep(1);
        sem_wait(&full);
        pthread_mutex_lock(&mutex);

        //critical section
        p.counter=(p.counter-1)%buffer_size;
        int item = p.buffer[p.counter];
        cout << "Consumed item is: " << item << endl;
        cout << endl;

        pthread_mutex_unlock(&mutex);
        sem_post(&empty);
    }
}

int main()
{

    pthread_t producer, consumer;

    sem_init(&empty, 0, buffer_size);
    sem_init(&full, 0, 0);
    pthread_mutex_init(&mutex, NULL);

    pthread_create(&producer, NULL, produce, NULL);
    pthread_create(&consumer, NULL, consume, NULL);

    pthread_exit(NULL);

    return 0;
}
