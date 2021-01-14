package com.cse23.producer_consumer.simulator.model.Observer;

import com.cse23.producer_consumer.simulator.model.data.Product;
///////////////////////Observer interface 
public interface Observer {
//////////////function used /////////////////////////
    public  void update(Object subject);
    public Object getObserverInstance();
    public void setNumberOfProduct(); 
}
