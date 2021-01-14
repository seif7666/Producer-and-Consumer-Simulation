package com.cse23.producer_consumer.simulator.controller;


import com.cse23.producer_consumer.simulator.ConcurrencyBackEndApplication;
import com.cse23.producer_consumer.simulator.ThreadColor;
import com.cse23.producer_consumer.simulator.model.Snapshot.CareTaker;
import com.cse23.producer_consumer.simulator.model.Snapshot.Memento;
import com.cse23.producer_consumer.simulator.model.Snapshot.Originator;
import com.cse23.producer_consumer.simulator.model.data.M;
import com.cse23.producer_consumer.simulator.model.data.Product;
import com.cse23.producer_consumer.simulator.model.data.Q;


import java.util.ArrayList;
import java.util.Random;

public class Server implements IServer {

    private String aColor = ThreadColor.ANSI_BLACK; //define color
    private static final int STOP = -100;//define stop variable

    private final ArrayList<Q> Qs;//list of q (stage)
    private final ArrayList<M> Ms;//list of m (process)
    private static Server server ;//server for singleton

    private  int numberOfProducts; //number of random input products

    private final ArrayList<Q> lastQs;//list for last Qs
    private update update;	//update object to sent to front end
    private CareTaker careTaker; //care taker for reply

    private Server() {
        careTaker=CareTaker.getInstance(); //get class object
        Ms = new ArrayList<>(); //initialization
        Qs = new ArrayList<>();//initialization
        numberOfProducts = 0;//initialization
        lastQs = new ArrayList<>();//initialization
    }

    ///////////will be deleted
    private Server(ArrayList<Q> Qs,ArrayList<M> Ms,ArrayList<Q> lastQs, Originator originator ) {
        this.Qs=Qs;
        this.Ms=Ms;
        this.lastQs=lastQs;
    }

    ///////////////////////to get object using singleton design pattern///
    public static Server getServer() {
        if(server == null) //check if not initialized
            server = new Server();//get instance
        return server;
    }



///////////////////////////////Add new M//////////////////////////////////

    @Override
    public void addM(int ID) {
        System.out.println("Id of M is "+ID);
        M newM = new M(ID);

        Ms.add(newM);
    }

///////////////////////////////Add new Q//////////////////////////////////

    @Override
    public void addQ(int ID) {
        System.out.println("Q is added "+ ID);
        Q newQ = new Q(ID);//get Q
        Qs.add(newQ);//add Q to the list
        lastQs.add(newQ); //add to check for last
    }

    //////////////////////////////link Q to next M/////////////////////////
    public boolean linkQToNextM(int QID, int MID) {
        System.out.println(aColor+"Q to M");
        System.out.println("Q"+QID+" is linked to M"+MID);
        M existM = Ms.get(MID); //get M
        Q existQ = Qs.get(QID);//get Q
        lastQs.remove(existQ);//This is not last no more.
        existQ.addSubject(existM); //add subject to Q (Observer design pattern)
        existM.attach(existQ); //add attach to M   (Observer design pattern)
        return true;
    }

    //////////////////////////////link M to next Q/////////////////////////
    public boolean linkMToNextQ(int QID, int MID) {
        System.out.println(aColor+"M to Q");
        System.out.println("M"+MID+" is linked to Q"+QID);
        M existM = Ms.get(MID);//get M
        Q existQ = Qs.get(QID);//get Q
        if(existM.getNext()!=null) {
            return false; //check for only one forward Q
        }
        existM.setNext(existQ);//add Q to m
        return true;
    }



///////////////////check for begin //////////////////////////////

    /**
     * Simulation starts here after checking.
     * @return false if error occurs.
     */
    public boolean testBeginSimulation() {
        Q currentQ;//define pointer to Q
        M currentM;//define pointer to m
        for(int i=0; i<Qs.size()-1;i++) { ///////////All Qs must have next M except the last Q
            currentQ=Qs.get(i);//get Q
            if(currentQ.getForwardMS()==null) {
                return false;
            }
        }
        for(Q q :lastQs) {//iterate at lastQs
            if(!q.getForwardMS().isEmpty()) {//check that not have forward Ms
                System.out.println("Line 108 server");
                return false ; ///last Q musn't have next M
            }
        }
        for(int i=0; i<Ms.size();i++) {
            currentM=Ms.get(i);//get m
            if(currentM.getPrevious().isEmpty()) {//check if empty
                System.out.println("Line 115 server");
                return false;
            }
            if(currentM.getNext()==null) {//check for next Q to m
                System.out.println("Line 119 server");
                return false;
            }
        }
        System.out.println(aColor+"Test ended Well!");
        CareTaker.getInstance().clear(); //clear care taker
        startSimulation();//start the program
        return true;

    }

    /////////////////start simulation////////////////////////
    public void startSimulation() {
        for (M currentM : Ms) {
            currentM.beginThread();//begin thread of ms
        }
        //We need to activate a thread that puts products on random rates,checks that total products in last q is full, to return.
        new Thread(() -> {
            //First random products.
            Qs.get(0).addProduct(new Product(ThreadColor.getARandomColour()));//add new product to Q0
            Originator origin = new Originator();
            origin.setState(null , Qs.get(0));
            careTaker.addMemento(origin.saveStateToMemento());
            for(int i = 0 ;i<numberOfProducts - 1;i++){//iterate for random input
                try{
                    long time  = new Random().nextInt(1000* ConcurrencyBackEndApplication.RANDOM_PRODUCTS_SECONDS);//Maximum 10 seconds.
                    System.out.println(aColor+"TIme is "+ time/1000);
                    Thread.sleep(time);//sleep to apply random input
                }catch (InterruptedException e){
                    e.printStackTrace();
                    continue;
                }
                //Qs.get(0).addProduct(new Product(color[new Random().nextInt(color.length)]));
                Qs.get(0).addProduct(new Product(ThreadColor.getARandomColour()));//add new product to Q0
                origin = new Originator();
                origin.setState(null , Qs.get(0));
                careTaker.addMemento(origin.saveStateToMemento());
                System.out.println(aColor+"Product is added!");
            }

            //After launching all products, we check last to terminate.
            int prev = 0;
            while(prev < numberOfProducts){
                int sum = 0;
                for(Q i : lastQs) //iterate for products in last Qs
                    sum += i.getProducts().size();//get number of products
                if(sum != prev){
                    System.out.println(aColor+"Total products finished is "+ sum);
                    prev = sum;//check the sum of product
                }
            }
            System.out.println(aColor+"Done");
            System.out.println(aColor+"Interrupting threads ....");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            server.stop();//stop if products reach last Qs
        }).start();
    }






    @Override
    public void replay() {
        for(Q q:Qs) {
            q.setProducts(null); //set number of products to each Q
        }
        new Thread(){
            @Override
            public void run() {
                ArrayList<Memento> mementoList=careTaker.getMementoList() ;//get memento list from care taker
                ArrayList<Long> timeOfmemento =careTaker.getMementoTime();//get time list from care taker
                for(int i=0 ;i<mementoList.size();i++) {
                    M m= mementoList.get(i).getStateM();//get m
//		    		System.out.println(m.getID());
//		    		System.out.println(m.getColor());
                    Q q= mementoList.get(i).getStateQ();//get Q
//		    		System.out.println(q.getID());
                    update update=new update(q.getID(),m == null ?-1:m.getID(),q.getProducts().size(),m == null ?null:m.getColor());//set update to send to front end
                    System.out.println("line 205");
                    setUpdate(update);//set update function
                    try {
                        System.out.println("reply enter sleep ");
                        Thread.sleep(timeOfmemento.get(i));//sleep for the time
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                setUpdate(new update(STOP , STOP , STOP , null));
            }}.start();



    }
    /////////////////////////function we call to restart new simulation ///////////////
    @Override
    public void beginNewSimulation() {
        CareTaker.getInstance().clear();//clear care taker
        Qs.clear();//clear Qs
        Ms.clear();//clear Ms
        numberOfProducts=0;//set number of products to zero
        lastQs.clear();//clear Qs
    }


    @Override
    public void stop() {
        for(M i :Ms)
            i.interrupt();//stop threads of ms
        setUpdate(new update(STOP , STOP , STOP , null));
    }

    @Override
    public update update() {

        return null;
    }


    /////////////////start simulation////////////////////////


    public update getUpdate() {
        update temp = update; //define temp for update
        update = null;//set update to null
        return temp;//return temp
    }

    public void setUpdate(update update) {
        System.out.println(aColor+"Update is set!\t"+update);
        this.update = update;//set update
        Controller.theUpdate.add(update);//send update to  front end
    }

    @Override
    public void addProduct(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;//set number of products
    }



}