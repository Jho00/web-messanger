import Message from "../entity/Message";
import ConnectionManager from "../managers/ConnectionManager";

export const handleNewMessage = (request, response) => {
    const body = request.body;

    const newMessage = new Message(body.message);
    const ids = body.ids;

    const connectionManager: ConnectionManager = ConnectionManager.getInstance();
    connectionManager.handleNewMessage(ids, newMessage);

    response.send('ok');
}

export const handleNewThread = (request, response) => {
    const body = request.body;
    const ids = body.ids;

    const connectionManager: ConnectionManager = ConnectionManager.getInstance();
    connectionManager.handleNewThread(ids);

    response.send('ok');
}

export const handleNewRequest = (request, response) => {
    const body = request.body;
    const ids = body.ids;

    const connectionManager: ConnectionManager = ConnectionManager.getInstance();
    connectionManager.handleNewRequest(ids);

    response.send('ok');
}