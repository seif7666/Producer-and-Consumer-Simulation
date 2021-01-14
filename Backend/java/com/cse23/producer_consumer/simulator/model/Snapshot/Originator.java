package com.cse23.producer_consumer.simulator.model.Snapshot;


import com.cse23.producer_consumer.simulator.controller.update;
import com.cse23.producer_consumer.simulator.model.data.M;
import com.cse23.producer_consumer.simulator.model.data.Product;
import com.cse23.producer_consumer.simulator.model.data.Q;
import com.cse23.producer_consumer.simulator.controller.Server;

import java.util.ArrayList;

public class Originator {

    private M stateM;//define M
    private Q stateQ;//define Q



    public void setState(M state , Q stateQ) {
        this.stateM =state !=null ? new M(state.getID(),state.getColor()) : null;//initialize M
        this.stateQ = new Q(stateQ.getID(),stateQ.getNumberOfProduct());//initialize Q
        ArrayList<Product> y = stateQ.getProducts();
        ArrayList<Product> x = new ArrayList<>(y);
        this.stateQ.setProducts(x);//swt the product of M

    }


    public Memento saveStateToMemento()
    {
        ///////////////////////set update to send it to front end //////////
        Server.getServer().setUpdate(new update(stateQ.getID() , stateM != null ?stateM.getID() : -1  , stateQ.getProducts().size() , stateM!=null?stateM.getColor():null));
        return new Memento(stateM , stateQ);//return new memento
    }

    public void getStateFromMemento(Memento memento) {
        stateM = memento.getStateM();//get state M
    }
}