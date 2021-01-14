import axios from "axios";
//import * as simulator from './Simulator.vue' ;

export function sendM(cir) {
    const back = "http://localhost:8085//sendM";
    let mid = cir.id.substring(1);
    let fill = cir.getAttribute("fill");
    const url = `${back}?id=${mid}&fill=${fill}`;
    axios.post(url).then(res => {
        console.log(res.data);
    });
}

export function sendQ(rec) {
    const back = "http://localhost:8085//sendQ";
    let qid = rec.id.substring(1);
    let fill = rec.getAttribute("fill");
    const url = `${back}?id=${qid}&fill=${fill}`;
    console.log("qid" + qid);
    axios.post(url).then(res => {
        console.log(res.data);
    });
}

export async function sendConnection(m, q) {
    console.log(m);
    console.log(q);
    const back = "http://localhost:8085//sendConnection";
    // let qid = parseInt(q.substring(1), 10);
    // let mid = parseInt(m.substring(1), 10);
    const url = `${back}?mid=${m}&qid=${q}`;

    let x = null;
    await axios.get(url).then(res => {
        console.log("hhhhhhh");
        console.log(res.data);
        x = res.data;
    });
    return x;
}

export function stop() {
    const back = "http://localhost:8085//stop";
    axios.post(back).then(res => {
        console.log(res + "stopped server");
    });
}
export function restart() {
    const back = "http://localhost:8085//restart";
    axios.post(back).then(res => {
        console.log(res + "restart server");
    });
}

export function replay() {
    const back = "http://localhost:8085//replay";
    axios.post(back).then(res => {
        console.log(res + "replay server");
    });
}

export function addproduct(numberOfProducts) {
    const back = "http://localhost:8085//addProduct";
    numberOfProducts = parseInt(numberOfProducts, 10);
    const url = `${back}?numberOfProducts=${numberOfProducts}`;

    axios.post(url).then(res => {
        console.log(res + "addproduct server");
    });
}

export function startnew() {
    const back = "http://localhost:8085//startnew";
    axios.post(back).then(res => {
        console.log(res.data);
    });
}