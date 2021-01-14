package com.cse23.producer_consumer.simulator.model.Snapshot;

import java.util.ArrayList;

public class CareTaker{
    private long time;//define time 
    private final ArrayList<Memento> mementoList ;//define memento list 
    private final ArrayList<Long> timeOfmemento ;//define time list 
    private static CareTaker careTaker;//care taker object 
    private  Memento lastMemento;//define last state 
    private static long previousTime;//get previous time 
    
    private CareTaker() {
    	previousTime=0;//set previous time 
        mementoList = new ArrayList<Memento>();//initialize memento list 
        timeOfmemento =new ArrayList<Long>();//initialize time 
        time = System.currentTimeMillis();//get the time
    }
////////////////////////function to use singleton pattern//////////////////
    public static CareTaker getInstance() {
        if (careTaker == null) careTaker = new CareTaker();//check if exist
        return careTaker;//return the object 
    }
//////////////////////////////////add memento to the list//////////////
    public synchronized void addMemento(Memento state) {
    	long currentTime=System.currentTimeMillis();//set current time 
    	if(previousTime==0)//check for first time 
            timeOfmemento.add(0L);//add the first time
            else
            	timeOfmemento.add(currentTime- previousTime);//calculate the time we use 
    	previousTime=currentTime;//set previous time 
            mementoList.add(state);//add state to memento list 
    }
    public int getmementoListSize() {
    	return mementoList.size();//return size of memento list 
    }

    /**
     * Get Object based on time.
     * @param time key
     * @return memento saved at current time.
     */
    public Memento get(long time)
    {
       // return mementoList.get(time);
    	return null;
    }

    /**
     * Clear all mementos.
     */
    public void clear(){
        mementoList.clear();//clear memento list 
        timeOfmemento.clear();//clear the time we save
        time = System.currentTimeMillis();
        previousTime = 0;
    }

    /**
     * To start a new Simulation.
     */
    public static void begin(){
        careTaker = new CareTaker();//begine care taker 
    }

    public  Memento getLastMemento() {
        return lastMemento;//get last memento 
    }

	public ArrayList<Memento> getMementoList() {
		return mementoList;//get memento list 
	}
	public ArrayList<Long> getMementoTime() {
		return timeOfmemento;//get time we save 
	}
    
}
