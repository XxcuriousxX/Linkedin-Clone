export class ConnectPayload {
    sender_username: string = ""; //username
    receiver_username: string = ""; // username

    constructor() { }
}

export class ConnectResponse {
    status: boolean = false;
    constructor() { }
}