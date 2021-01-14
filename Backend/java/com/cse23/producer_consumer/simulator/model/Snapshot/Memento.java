package com.cse23.producer_consumer.simulator.model.Snapshot;

import com.cse23.producer_consumer.simulator.model.data.M;
import com.cse23.producer_consumer.simulator.model.data.Q;

public class Memento {
    private M stateM;//define state M
    private Q stateQ;//define state Q

    public Memento(M stateM, Q stateQ) {
        this.stateM = stateM;//set M
        this.stateQ = stateQ;//set Q
    }

    public M getStateM() {
        return stateM;//get M 
        
    }
    public Q getStateQ() {
        return stateQ;//get Q
        
    }

}
