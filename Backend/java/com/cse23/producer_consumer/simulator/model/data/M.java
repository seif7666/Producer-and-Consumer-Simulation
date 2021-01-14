package com.cse23.producer_consumer.simulator.model.data;

import com.cse23.producer_consumer.simulator.ConcurrencyBackEndApplication;
import com.cse23.producer_consumer.simulator.ThreadColor;
import com.cse23.producer_consumer.simulator.model.Observer.Observer;
import com.cse23.producer_consumer.simulator.model.Observer.Subject;
import com.cse23.producer_consumer.simulator.model.Snapshot.CareTaker;
import com.cse23.producer_consumer.simulator.model.Snapshot.Originator;

import java.util.ArrayList;
import java.util.Random;


public class M implements Subject,Runnable,Cloneable  {
    private String printColor;// define color 

    public static final boolean IS_READY = true;//set stae to ready 
    public static final String DEFAULT_COLOR = "#11ef11";//default color 

    private boolean stopThread = false;//stop thread 


    private int ID;//define ID 
    private Product product;//define product
    private Observer next;//define next
    private  ArrayList<Observer> previous;//define previous
    private String color;//define color
    private boolean state ;//define state
    private int time ;//define time
    private int processedProducts;//define processedProducts

    private Q sendingQ;//define sending q 

    private Thread thread;//define thread 
//////////////////////////constructor of M ///////////////////
    public M(int ID) {
        color = DEFAULT_COLOR;//set default color 
        this.ID=ID;//set Id
        previous = new ArrayList<>();//initialization
        processedProducts = 0;//set products 
        printColor = ThreadColor.getAColor(); //get color 


    }
    /////////////another constructor  with color sent////////////////////
    public M(int ID,String color) {
    	this.ID=ID;//set ID 
    	this.color=color;//set color 
    }
///// constructor Of M  with next Q//////////////////////
    public M(int ID,Observer next) {

        this.ID = ID;//se
        this.next =next;
        this.previous = new ArrayList<>();

    }
    ///////////////////begin the thread //////////////////////
    public void beginThread() {
        time =  new Random().nextInt(ConcurrencyBackEndApplication.PROCESSING_SECONDS *1000);//random time for sleep
        thread = new Thread(this , "M "+ID);//initialization  thread 
        thread.start();//start the thread 
    }

    @Override
    public void attach(Observer observer) {
        previous.add (observer);//set attach to m 
    }

    public int getID() {
        return ID;//get ID
    }

    public void setID(int iD) {
        ID = iD;//set ID
    }

    public Product getProduct() {
        return product;//get Products arrayList 
    }

    public void setProduct(Product product , Q sendingQ)
    {
        if(product == null){
            this.color = DEFAULT_COLOR;//set color 
            return;
        }
        this.sendingQ = sendingQ;//set Q
        this.product = product;//set the products 
        this.color = product.getColor();//set color 
    }

    public Observer getNext() {
        return next;//get next Q of M
    }

    public boolean isState() {
        return state;//set state
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;//set color 
    }



    public boolean getState() {
        return state; //set state 
    }


    public void setNext(Observer next) {
        this.next = next;//set next 
    }

    public ArrayList<Observer> getPrevious() {
        return previous;//get previous Qs
    }
    public void setPrevious(ArrayList<Observer> previous) {
        this.previous = previous;//set previous Qs
    }
    
    public void setDefaultColor() {
    	this.color = DEFAULT_COLOR;//set default color 
    }


    public void setState(boolean state)  {
        this.state = state;//set the state 
        if (state == IS_READY) {//We finished a product
            this.color = DEFAULT_COLOR;//set default color 
            if(product != null){//Check whether a process finished or not.
                next.update(product);//set next Q
                next.setNumberOfProduct();
                System.out.println(printColor+"M:"+ID+"Sending product to "+((Q) next.getObserverInstance()).getID());
                CareTaker careTaker = CareTaker.getInstance();//amke object of care taker
                this.setDefaultColor();//set default color 
                Originator originator = new Originator();//set the originator 
                originator.setState(this ,(Q)next.getObserverInstance() );//set sate
                careTaker.addMemento(originator.saveStateToMemento());//save the sate
            }
            product = null;//set product to null
            while(product == null) {
                if(stopThread)
                    return;
                notifyMethod();//notify the observer if it still ready 
            }
        }
        else{//Not ready
            //This means that a product was received.
            Originator originator = new Originator();//get originator object 
            CareTaker careTaker = CareTaker.getInstance();////get care taker
            originator.setState(this , sendingQ);//set the state 
            careTaker.addMemento(originator.saveStateToMemento());
        }
    }

    @Override
    public void notifyMethod() {
        for(Observer observer : previous)
            observer.update(this);//notify previous Qs to get product 
    }

    @Override
    public void run() {
        while(!stopThread) {
            setState(IS_READY);//Called to set previous Q.
            //Now we have a product.
            System.out.println(printColor+"Product was received in M "+ ID);
            setState(!IS_READY);//state not ready it have a product 
            System.out.println(printColor+"Time to process is "+ time);
            while(!stopThread) {
                try {
                	CareTaker careTaker = CareTaker.getInstance(); //get care taker object 
                    Thread.sleep(time);//sleep of processing 

                    Originator originator = new Originator();//get originator class

                    originator.setState(this ,(Q)next.getObserverInstance() );//set state
                    careTaker.addMemento(originator.saveStateToMemento());//save the state
                    System.out.println(printColor + "Product finished in M : " + ID);
                    processedProducts++;//increment processedProducts
                    break;
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        System.out.println(printColor+"Total products processed in M"+ID+" is "+processedProducts);
        System.out.println(printColor+"Thread "+ID+" successfully interrupted.");
    }

    /**
     * Interrupts thread.
     */
    public void interrupt() {
        stopThread = true;//set stopThread to true
        while(thread.isAlive()){}
        System.out.println("Thread "+ ID +"Successfully stopped");
    }




}