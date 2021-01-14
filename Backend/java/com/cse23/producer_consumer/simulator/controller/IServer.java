package com.cse23.producer_consumer.simulator.controller;


public interface IServer {

    public void addQ(int ID);
    public void addM(int ID);

    public void replay();
    public void beginNewSimulation();
    public void addProduct(int num);


    public void stop();
    public update update();
}
