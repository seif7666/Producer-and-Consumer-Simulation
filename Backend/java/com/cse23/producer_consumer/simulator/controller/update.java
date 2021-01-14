package com.cse23.producer_consumer.simulator.controller;
public class update {

    private int Qid;
    private int Mid;
    private int noOfproducts;
    private String fill;

    public update(int qid, int mid, int noOfproducts, String fill) {
        Qid = qid;
        Mid = mid;
        this.noOfproducts = noOfproducts;
        this.fill = fill;
    }

    public int getQid() {
        return Qid;
    }

    public void setQid(int qid) {
        Qid = qid;
    }

    public int getMid() {
        return Mid;
    }

    public void setMid(int mid) {
        Mid = mid;
    }

    public int getNoOfproducts() {
        return noOfproducts;
    }

    public void setNoOfproducts(int noOfproducts) {
        this.noOfproducts = noOfproducts;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("update{");
        sb.append("Qid=").append(Qid);
        sb.append(", Mid=").append(Mid);
        sb.append(", noOfproducts=").append(noOfproducts);
        sb.append('}');
        return sb.toString();
    }
}
