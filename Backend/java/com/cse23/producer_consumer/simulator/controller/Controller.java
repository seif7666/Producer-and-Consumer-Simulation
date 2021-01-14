package com.cse23.producer_consumer.simulator.controller;

import com.cse23.producer_consumer.simulator.controller.update;
import com.cse23.producer_consumer.simulator.controller.Server;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;


@CrossOrigin
@RestController

public class Controller {

    Server server = Server.getServer();

    public static ArrayList<update> theUpdate = new ArrayList<>() ;
    
    @RequestMapping("/")
	public String stert () {
		
		return "index";
	}

    @PostMapping("/sendQ")
    public void sendQ (@RequestParam("id") String id,@RequestParam("fill") String fill ) {
        System.out.println(id);
        server.addQ(Integer.parseInt(id));
    }

    @PostMapping("/sendM")
    public void sendM (@RequestParam("id") String id ,@RequestParam("fill") String fill) {
        server.addM(Integer.parseInt(id));
    }

    @GetMapping("/sendConnection")
    public boolean sendConnection (@RequestParam("mid") String id1,@RequestParam("qid") String id2) {
        if(id1.charAt(0) == 's'){ //Q to M
           int qid = Integer.parseInt(id1.substring(1));
           System.out.println(qid);
           int mid = Integer.parseInt(id2.substring(1));
           System.out.println(mid);
           return server.linkQToNextM(qid , mid);
       }
       //M to Q
        int qid = Integer.parseInt(id2.substring(1));
        System.out.println(qid);

        int mid = Integer.parseInt(id1.substring(1));
        System.out.println(mid);
        return server.linkMToNextQ(qid , mid);

    }

    @GetMapping("/update")
    public update update () {
//        System.out.println("___________________________________Called___________________________________________________________");
//        update theUpdate;
//        do {
//            theUpdate = server.getUpdate();
//        } while (theUpdate == null);
//        System.out.println("___________________________________Sent Now___________________________________________________________");
     while(theUpdate.isEmpty()){
     }
     return theUpdate.remove(0);
    }


    @GetMapping("/replay")
    public update replay () {
        //replay process
    	server.replay();
    	while(theUpdate.isEmpty()){
        }
        return theUpdate.remove(0);
    }

    @GetMapping("/start")
    public boolean start () {

        boolean xx=server.testBeginSimulation();
        server = Server.getServer();
        return xx;
    }

    @PostMapping("/stop")
    public void stop () {
        //stop process
        server.stop();
        server = Server.getServer();
    }

    @PostMapping("/addProduct")
    public void addProduct (@RequestParam("numberOfProducts") int numberOfProducts) {
        System.out.println("Add product");
        server.addProduct(numberOfProducts);
        //add product to first stage
    }
    
    //////////////////////////begin new simulation /////////////////
    @PostMapping("/startnew")
    public void clickOnbuttonStartNewSimulation() {
    	server.beginNewSimulation();
    }
}




