package com.cse23.producer_consumer.simulator.model.Observer;
//////////////////////////////subject interface //////////////////
public interface Subject {
//////////////functions used /////////////////////////
//    public static final int AFTER = 1; //Means the M is after current Q
//    public static final int BEFORE = 0;//Means M will be befor current Q
    public void attach(Observer observer);

    /**
     * To choose to update each observer.
     */
    public void notifyMethod();
    public boolean getState() ;
//    public Object getObservable(int state);

}
