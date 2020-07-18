// Port where we"ll run the websocket server

import ConnectionManager from "./app/managers/ConnectionManager";

const bodyParser = require('body-parser')

import {connection, request} from "websocket";
import HttpUrls from "./app/common/HttpUrls";
import {handleNewMessage, handleNewRequest, handleNewThread} from "./app/http-module/actions";
import WSConnection from "./app/entity/WSConnection";
import {InitialPayloadType} from "./app/common/types";

const webSocketsServerPort = 1337;
// websocket and http servers
const webSocketServer = require("websocket").server;
const http = require("http");
const express = require("express");
const app = express();

const connectionManager = ConnectionManager.getInstance();
/**
 * HTTP server
 */
const server = http.createServer(function (request, response) {
    // Not important for us. We"re writing WebSocket server, not HTTP server
});

server.listen(webSocketsServerPort, function () {
    console.log((new Date()) + " Server is listening on port " + webSocketsServerPort);
});

/**
 * WebSocket server
 */
const wsServer = new webSocketServer({
    // WebSocket server is tied to a HTTP server. WebSocket request is just
    // an enhanced HTTP request. For more info http://tools.ietf.org/html/rfc6455#page-6
    httpServer: server
});

wsServer.on("request",  (request: request) => {
    console.log((new Date()) + " Connection from origin " + request.origin + ".");
    let connection: connection = request.accept(undefined, request.origin);

    connection.on("message",  (message) => {
       let data = message.utf8Data;
       if (!data) {
           return;
       }

       const payload : InitialPayloadType = JSON.parse(data);

       if (payload.action === 'connect') {
           const id = payload.data.userId;
           const con = new WSConnection(id, connection)
           connectionManager.addConnection(con);
       }
    });
});

app.use(bodyParser.json());

app.post(HttpUrls.HANDLE_NEW_MESSAGE, handleNewMessage);
app.post(HttpUrls.HANDLE_NEW_THREAD, handleNewThread);
app.post(HttpUrls.HANDLE_NEW_REQUEST, handleNewRequest);
app.listen(3000, () => console.log("Example app listening on port 3000!"));