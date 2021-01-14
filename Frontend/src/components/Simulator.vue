<template>
  <div class="maindiv">
    <button @click="whichButton('Stop')" class="learn-more" id="b0">
      Stop
    </button>
    <button @click="whichButton('Replay')" class="learn-more" id="b1">
      Replay
    </button>
    <button @click="whichButton('AddProcess')" class="learn-more" id="b2">
      Add Machine
    </button>
    <button @click="whichButton('AddStage')" class="learn-more" id="b3">
      Add Stage
    </button>
    <button @click="whichButton('Connect')" class="learn-more" id="b4">
      Add Connection
    </button>
    <button @click="whichButton('Start')" class="learn-more" id="b5">
      Start
    </button>
    <button
        @click="whichButton('StartNewSimulation')"
        class="learn-more"
        id="b6"
    >
      Start New Simulation
    </button>
    <br />
    <span
    >Enter number of Products
      <input
          type="number"
          v-model="message"
          placeholder="Addproduct"
          id="inputnumber"
      />
    </span>
    <div class="outer-circle">
      <div class="inner-circle"></div>
      <span></span>
      <span></span>
      <span></span>
      <span></span>
      <input
          type="text"
          class="time"
          v-model="today"
          placeholder="Processing Time "
          disabled
      />
    </div>

    <svg
        width="1500"
        height="670"
        xmlns="http://www.w3.org/2000/svg"
        id="svg"
        @mousedown="mousePosition"
    >
      <defs>
        <marker
            id="arrow"
            refX="4"
            refY="2.5"
            markerWidth="10"
            markerHeight="5"
            orient="auto"
        >
          <path d="M0,0 L5,2.5 L0,5 Z" fill="#f00"></path>
        </marker>
      </defs>
    </svg>
  </div>
</template>

<script>
import * as server from "./server.js";
import axios from "axios";

export default {
  name: "Simulator",
  data() {
    return {
      svg: null,
      buttonName: null,
      x: 0,
      y: 0,
      numberOfProducts: 0,
      idOfStages: 0,
      idOfProcess: 0,
      idOflines: 0,
      idOfTextStage: 0,
      idOfTextProcess: 0,
      flag: true,
      firstX: 0,
      firstY: 0,
      secondX: 0,
      secondY: 0,
      firstObject: null,
      secondObject: null,
      selectedObjectNow: null,
      message: null,
      today: null,
      from: null,
      temp : null,
    };
  },
  methods: {
    async whichButton(x) {
      if (x === "Stop") {
        this.buttonName = "Stop";
        console.log(this.buttonName);
        this.today = "";
        this.message = "";
        this.numberOfProducts = 0;
        document.getElementById("inputnumber").disabled = false;
        server.stop();
        document.getElementById("b0").disabled = true;
        document.getElementById("b1").disabled = true;
        document.getElementById("b2").disabled = true;
        document.getElementById("b3").disabled = true;
        document.getElementById("b4").disabled = true;
        document.getElementById("b5").disabled = true;
        document.getElementById("b6").disabled = false;
      } else if (x === "Replay") {
        this.buttonName = "Replay";
        console.log(this.buttonName);
        this.today = "";
        this.from = new Date();
        this.temp=this.idOfStages;
        while (this.idOfStages !== 0) {
          this.idOfStages--;

          this.updateSingleStage(this.idOfStages, 0);
        }
        this.replay();
        this.idOfStages=0;
      } else if (x === "AddProcess") {
        this.buttonName = "AddProcess";
        console.log(this.buttonName);
        this.addprocess();
        this.x = 0;
        this.y = 0;
      } else if (x === "AddStage") {
        this.buttonName = "AddStage";
        console.log(this.buttonName);
        this.addStage();
        this.x = 0;
        this.y = 0;
      } else if (x === "Connect") {
        console.log("gggggd" + this.firstObject + this.secondObject);
        this.buttonName = "Connect";
        console.log(this.buttonName);
        //here we have the two objects id first object and second object

        if (this.firstObject !== null && this.secondObject !== null) {
          if (this.firstObject.charAt(0) === this.secondObject.charAt(0)) {
            alert("you can`t connect stage by stage or machine by machine");
            this.firstObject = null;
            this.secondObject = null;
            this.buttonName = null;
            return;
          }
          let correctConnection = await server.sendConnection(
              this.firstObject,
              this.secondObject
          );
          console.log("Connection is " + correctConnection);
          console.log(typeof correctConnection);
          if (!correctConnection) {
            alert("you can`t connect more than one stage to same Machine");
          } else if (this.firstObject.charAt(0) === "s") {
            let myfirstObject = document.getElementById(this.firstObject);
            this.firstX = myfirstObject.getAttribute("x");
            this.firstY = myfirstObject.getAttribute("y");
            this.firstY = Number.parseInt(this.firstY) + 30;
            let mysecondObject = document.getElementById(this.secondObject);
            this.secondX = mysecondObject.getAttribute("cx");
            this.secondY = mysecondObject.getAttribute("cy");
            this.secondX = Number.parseInt(this.secondX) + 40;
            if (Number.parseInt(this.secondX) > Number.parseInt(this.firstX)) {
              this.firstX = Number.parseInt(this.firstX) + 90;
              this.secondX = Number.parseInt(this.secondX) - 80;
            }
            this.connect();
          } else if (this.firstObject.charAt(0) === "p") {
            let myfirstObject = document.getElementById(this.firstObject);
            this.firstX = myfirstObject.getAttribute("cx");
            this.firstY = myfirstObject.getAttribute("cy");
            this.firstX = Number.parseInt(this.firstX) + 40;
            let mysecondObject = document.getElementById(this.secondObject);
            this.secondX = mysecondObject.getAttribute("x");
            this.secondY = mysecondObject.getAttribute("y");
            this.secondY = Number.parseInt(this.secondY) + 30;
            if (Number.parseInt(this.firstX) > Number.parseInt(this.secondX)) {
              this.secondX = Number.parseInt(this.secondX) + 90;
              this.firstX = Number.parseInt(this.firstX) - 80;
            }
            this.connect();
          }
          this.firstObject = null;
          this.secondObject = null;
          this.buttonName = null;
        }
      } else if (x === "Start") {
        this.numberOfProducts = this.message;
        this.buttonName = "Start";
        if (this.message === "" || this.message === null)
          alert("Are you kidding me , enter number of products");
        // console.log("wtfffffffffff "+this.message)
        if (this.message !== "" && this.message !== null) {
          document.getElementById("inputnumber").disabled = true;
          console.log(this.numberOfProducts);
          server.addproduct(this.numberOfProducts);
          this.start();
          this.from = new Date();
          document.getElementById("b1").disabled = true;
          document.getElementById("b2").disabled = true;
          document.getElementById("b3").disabled = true;
          document.getElementById("b4").disabled = true;
          document.getElementById("b5").disabled = true;
          document.getElementById("b6").disabled = true;
        }

        ////////////////////////////////////////////////////end when?///////////////////////////////
      } else if (x === "StartNewSimulation") {
        this.buttonName = "StartNewSimulation";
        console.log(this.buttonName);
        location.reload();
        server.startnew();
      }
    },
    /////////////////////////////////////////////////////////mouse position/////////////////////////////////////////
    mousePosition(e) {
      this.x = e.offsetX;
      this.y = e.offsetY;
      console.log("points is  " + this.x + "  " + this.y);
    },
    ///////////////////////////////////////////////////////////add new machine//////////////////////////////////////
    addprocess() {
      if (this.x !== 0 && this.y !== 0) {
        const cir = document.createElementNS(
            "http://www.w3.org/2000/svg",
            "circle"
        );
        console.log(this.x);
        console.log(this.y);
        cir.setAttribute("cx", this.x);
        cir.setAttribute("cy", this.y);
        cir.setAttribute("r", "40");
        cir.setAttribute("fill", "#11ef11");
        cir.addEventListener("mousedown", this.whichObject);
        let id = "p" + this.idOfProcess;
        cir.setAttribute("id", id);
        this.svg.appendChild(cir);
        console.log(cir);
        server.sendM(cir);
        let idtext = document.createElementNS(
            "http://www.w3.org/2000/svg",
            "text"
        );
        idtext.setAttribute("x", this.x - 9);
        idtext.setAttribute("y", this.y + 6);
        let idtxt = "id" + this.idOfTextProcess;
        idtext.setAttribute("id", idtxt);
        this.idOfTextProcess++;
        let idText = document.createTextNode("M" + this.idOfTextProcess);
        idtext.appendChild(idText);
        this.svg.appendChild(idtext);
        this.idOfProcess++;
        // this.updateSingleProcess(id);//////////////////////////////
        //this.removeShapeByID(id);
      }
    },
    //////////////////////////////////////////add new stage///////////////////////////////////////////////////////////
    addStage() {
      if (this.x !== 0 && this.y !== 0) {
        const rec = document.createElementNS(
            "http://www.w3.org/2000/svg",
            "rect"
        );
        console.log(this.x);
        console.log(this.y);
        rec.setAttribute("x", this.x);
        rec.setAttribute("y", this.y);
        rec.setAttribute("width", "90");
        rec.setAttribute("height", "60");
        rec.setAttribute("fill", "#e55a0e");
        let id = "s" + this.idOfStages;
        rec.setAttribute("id", id);
        rec.addEventListener("mousedown", this.whichObject);
        ////////////////////////////////set name of products/////////
        let text2 = document.createElementNS(
            "http://www.w3.org/2000/svg",
            "text"
        );
        text2.setAttribute("x", this.x - 2);
        text2.setAttribute("y", this.y - 15);
        text2.setAttribute("fill", "#e55a0e");
        let txt2 = document.createTextNode("# products  ");
        text2.appendChild(txt2);
        ///////////////////////////////////set number of products///
        let text = document.createElementNS(
            "http://www.w3.org/2000/svg",
            "text"
        );
        text.setAttribute("x", this.x + 40);
        text.setAttribute("y", this.y);
        let productTxt = "t" + this.idOfTextStage;
        text.setAttribute("id", productTxt);
        text.setAttribute("fill", "#3a0683");
        let txt = document.createTextNode("0");
        text.appendChild(txt);
        //////////////////////////////////////add id of stage
        let idtext = document.createElementNS(
            "http://www.w3.org/2000/svg",
            "text"
        );
        idtext.setAttribute("x", this.x + 35);
        idtext.setAttribute("y", this.y + 35);
        let idtxt = "id" + this.idOfTextStage;
        idtext.setAttribute("id", idtxt);
        this.idOfTextStage++;
        let idText = document.createTextNode("Q" + this.idOfStages);
        idtext.appendChild(idText);
        ///////////////////////////
        this.svg.appendChild(text2);
        this.svg.appendChild(text);
        this.svg.appendChild(rec);
        this.svg.appendChild(idtext);
        this.idOfStages++;
        console.log(rec);
        server.sendQ(rec);
      }
    },
    ////////////////////////////////////////////////selected object////////////////////////////////////////////////////
    whichObject(event) {
      this.selectedObjectNow = event.target.id;
      if (this.buttonName === "Connect") {
        if (this.flag) {
          this.flag = false;
          this.firstObject = event.target.id;
          console.log("this.firstObject " + this.firstObject);
        } else {
          this.secondObject = event.target.id;
          console.log("this.secondObject " + this.secondObject);
          this.flag = true;
          if (this.firstObject === null || this.secondObject === null) {
            alert("incorrect connection , please repeat the operation");
            this.buttonName = "nothing";
          }
        }
      }
    },
    /////////////////////////////////////////draw connection/////////////////////////////////////////////////////
    connect() {
      const line = document.createElementNS(
          "http://www.w3.org/2000/svg",
          "line"
      );
      line.setAttribute("x1", this.firstX);
      line.setAttribute("y1", this.firstY);
      line.setAttribute("x2", this.secondX);
      line.setAttribute("y2", this.secondY);
      line.setAttribute("stroke", "black");
      line.setAttribute("stroke-width", "3");
      let id = "l" + this.idOflines;
      line.setAttribute("id", id);
      this.idOflines++;
      line.setAttribute("marker-end", "url(#arrow)");
      this.svg.appendChild(line);
      console.log(line);
    },
    updateSingleStage(id, numberOfproducts) {
      let idTxt = "t" + id;
      id = "s" + id;
      console.log("idtexxxxxxxxxxxxxttttttt    " + idTxt);
      let removedTxt = document.getElementById(idTxt);
      this.svg.removeChild(removedTxt);
      let rectangle = document.getElementById(id);
      let x = rectangle.getAttribute("x");
      let y = rectangle.getAttribute("y");
      rectangle.setAttribute("fill", "#800000");
      let text = document.createElementNS("http://www.w3.org/2000/svg", "text");
      console.log(x);
      console.log(y);
      x = Number.parseInt(x) + 40;
      text.setAttribute("x", x);
      text.setAttribute("y", y);
      text.setAttribute("id", idTxt);
      text.textContent = numberOfproducts;
      this.svg.appendChild(text);
      console.log(id + " M " + numberOfproducts);
    },
    updateSingleProcess(id, fill) {
      id = "p" + id;
      console.log("Received are " + id + "\t" + fill);
      let circle = document.getElementById(id);
      console.log(circle);
      circle.setAttribute("fill", fill);
      console.log(circle);
      console.log(id + " P ");
    },
    removeShapeByID(id) {
      let removed = document.getElementById(id);
      this.svg.removeChild(removed);
      if (id.charAt(0) === "s") {
        let idTxt = "t" + id.substring(1, id.length);
        console.log("idtexxxxxxxxxxxxxttttttt    " + idTxt);
        let removedTxt = document.getElementById(idTxt);
        this.svg.removeChild(removedTxt);
      }
    },

    start() {
      const back = "http://localhost:8085//start";
      let data = null;
      axios.get(back).then(res => {
        console.log(res + "start server");
        data = res.data;
        if (!data) {
          alert("Something is wrong!!!");
          document.getElementById("b0").disabled = false;
          document.getElementById("b1").disabled = false;
          document.getElementById("b2").disabled = false;
          document.getElementById("b3").disabled = false;
          document.getElementById("b4").disabled = false;
          document.getElementById("b5").disabled = false;
          document.getElementById("b6").disabled = false;

          return;
        }
        this.update();
      });
    },
    update() {
      const back = "http://localhost:8085//update";
      console.log("update xxxxxxx");
      axios.get(back).then(res => {
        console.log(res);
        if (res.data.qid === undefined ) {
          console.log("in update undefined");

          this.update();
        } else {
          console.log(res.data);
          if (res.data.mid != -100) {
            console.log("in update");
            this.updateSingleStage(res.data.qid, res.data.noOfproducts);
            if (res.data.mid != -1)
              this.updateSingleProcess(res.data.mid, res.data.fill);
            this.update();
            res = null;
          } else {
            let to = new Date();
            const totalTime = to.getTime() - this.from.getTime();
            console.log(totalTime);
            let sec = Math.floor(totalTime / 1000);
            let min = Math.floor(sec / 60);
            sec = sec - min * 60;
            console.log(
                "iam hereeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"
            );
            this.today = min + " : " + sec;
            document.getElementById("b1").disabled = false;
            document.getElementById("b6").disabled = false;
            for (let i=0 ; i<this.temp-1;i++){
              this.updateSingleStage(i,0)
              console.log("i "+i);
            }
          }
        }
      });
    },
    replay() {
      const back = "http://localhost:8085//replay";
      axios.get(back).then(res => {
        console.log(res);
        if (res.data.qid === undefined) {
          console.log("in update undefined");

          this.update();
        } else {
          console.log(res.data);
          if (res.data.mid != -100) {
            console.log("in update");
            this.updateSingleStage(res.data.qid, res.data.noOfproducts);
            if (res.data.mid != -1)
              this.updateSingleProcess(res.data.mid, res.data.fill);
            this.update();
            res = null;
          }
        }
      });
    }
  },

  watch: {},
  mounted() {
    this.svg = document.getElementById("svg");
    console.log(this.svg);
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
@import url("https://fonts.googleapis.com/css?family=Rubik:700&display=swap");
* {
  box-sizing: border-box;
}
*::before,
*::after {
  box-sizing: border-box;
}

body {
  font-family: "Rubik", sans-serif;
  font-size: 1rem;
  line-height: 1.5;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0;
  min-height: 100vh;
  background: #11ef11;
}

button {
  position: relative;
  display: inline-block;
  cursor: pointer;
  outline: none;
  border: 0;
  vertical-align: middle;
  text-decoration: none;
  font-size: 75%;
  font-family: inherit;
}
button.learn-more {
  font-weight: 600;
  color: #d60707;
  text-transform: uppercase;
  padding: 1.25em 2em;
  background: #fff0f0;
  border: 2px solid #b18597;
  border-radius: 0.75em;
  transform-style: preserve-3d;
  transition: transform 150ms cubic-bezier(0, 0, 0.58, 1),
  background 150ms cubic-bezier(0, 0, 0.58, 1);
}
button.learn-more::before {
  position: absolute;
  content: "";
  width: 100%;
  height: 70%;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: #f9c4d2;
  border-radius: inherit;
  box-shadow: 0 0 0 2px #b18597, 0 0.625em 0 0 #ffe3e2;
  transform: translate3d(0, 0.75em, -1em);
  transition: transform 150ms cubic-bezier(0, 0, 0.58, 1),
  box-shadow 150ms cubic-bezier(0, 0, 0.58, 1);
}
button.learn-more:hover {
  background: #221f1f;
  transform: translate(0, 0.25em);
}
button.learn-more:hover::before {
  box-shadow: 0 0 0 2px #b18597, 0 0.5em 0 0 #ffe3e2;
  transform: translate3d(0, 0.5em, -1em);
}
button.learn-more:active {
  background: #d99f9f;
  transform: translate(0em, 0.75em);
}
button.learn-more:active::before {
  box-shadow: 0 0 0 2px #b18597, 0 0 #ffe3e2;
  transform: translate3d(0, 0, -1em);
}
svg {
  border: 3px solid brown;
  background: #f1f2f6;
}
.time {
  text-align: center;
  position: absolute;
  top: 10px;
  right: 1.7%;
  width: 160px;
  height: 30px;
  border: 3px solid #dd7d7d;
  background: #ffffff;
  border-radius: 500px;
  -webkit-text-fill-color: #090c70;
  font-size: large;
  font-style: oblique;
}

@import url("https://fonts.googleapis.com/css?family=Montserrat&display=swap");

@import url("https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css");

* {
  font-family: "Montserrat", sans-serif !important;
  box-sizing: content-box;
}

.outer-circle span {
  top: 5px;
  right: 1.25%;
  position: absolute;
  width: 185px;
  height: 50px;
  background: linear-gradient(#14ffe9, #ffeb3b, #ff00e0);
  border-radius: 500px;
  animation: rotate 1.5s linear infinite;
}

span:nth-child(1) {
  filter: blur(5px);
}

span:nth-child(2) {
  filter: blur(10px);
}

span:nth-child(3) {
  filter: blur(25px);
}

span:nth-child(4) {
  filter: blur(150px);
}

@keyframes rotate {
  0% {
    filter: hue-rotate(0deg);
  }
  100% {
    filter: hue-rotate(360deg);
  }
}
input[type="number"]:focus {
  background-color: lightblue;
  width: 10%;
  border: 3px solid #292525;
}
.span {
  font-family: "Times New Roman", Times, serif;
}
input[type="number"] {
  width: 10%;
  border: 1px dotted #292525;
}
</style>