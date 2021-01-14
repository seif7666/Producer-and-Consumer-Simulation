package com.cse23.producer_consumer.simulator.model.data;

import com.cse23.producer_consumer.simulator.model.Observer.Observer;
import com.cse23.producer_consumer.simulator.model.Observer.Subject;

import java.util.ArrayList;

public class Q implements Observer,Cloneable  {
    private int ID;//define ID
    private ArrayList<Product> products;//define products 
    private ArrayList<M> forwardWaitingMS;//define forward Ms
    private int numberOfProduct;//define number of products 
    

    

	public Q( int ID) {
        this.ID = ID;//set ID
        products = new ArrayList<>();//initialize products 
        forwardWaitingMS = new ArrayList<>();//initialize forwarrd ms 
    }
	public Q( int ID,int numberOfProduct) {
        this.ID = ID;//set ID 
        this.numberOfProduct=numberOfProduct;//set Number of products 
    }
    
    public void addSubject(M newM) {
    	forwardWaitingMS.add(newM);//add M
    }



    public int getID() {
        return ID;//get ID
    }

    public void setID(int iD) {
        ID = iD;//set Id
    }

    public ArrayList<Product> getProducts() {
        return products;//get products 
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;//set the products
    }

    public ArrayList<M> getForwardMS() {
        return forwardWaitingMS;//get Forward Ms
    }

    public void setForwardMS(ArrayList<M> forwardMS) {
        this.forwardWaitingMS = forwardMS;//set Forward Ms
    }
    
    

    public int getNumberOfProduct() {
		return numberOfProduct;//get number of products 
	}

	public void setNumberOfProduct() {
		this.numberOfProduct=products.size();//set number of products
	}

	/**
     * This function is called if an M needs a product.
     * The Q checks that M is first in Q.If not, then it adds it to last of queue.
     * If first then it sends a product to M.
     * This is only done if products are not empty.
     * @param m the ready m.
     */
    @Override
    public synchronized void update(Object m) {
        if( m instanceof M) {
            M theM = (M)m;//get M
            int indexOfM = forwardWaitingMS.indexOf(theM);
            if (indexOfM == -1) {//check for not exist in waiting list
                forwardWaitingMS.add(theM);//add it if not exist
                System.out.println(theM.getID() + " is put on the waiting list");
            }
            if (theM.getState() == M.IS_READY && theM.getProduct() == null) { // A check that M hasn't yet been filled
                if (products.isEmpty()) {//M must be put in waiting list.
                    return;
                }
                //Products are not empty
                if (indexOfM == 0) { // The first in waiting list.
                    theM.setProduct(products.remove(0), this);
                    forwardWaitingMS.remove(0);//remove Q if we sent to M
                    System.out.println("Q_Line:66 : Product is set for M:" + theM.getID() + "\t" +
                            "Sending Q is " + getID() + "Total products left in Q is " + products.size());
                }
            }
        }
        else{
            addProduct((Product) m);
        }
    }

    public synchronized void addProduct(Product product) {
        if(product!= null)//check if null 
            products.add(product);//add the product 
        System.out.println("Product is added to Q"+ID+" Number of products is "+products.size());
    }

 

    @Override
    public Object getObserverInstance(){
        return this;//get Q
    }
    
}
