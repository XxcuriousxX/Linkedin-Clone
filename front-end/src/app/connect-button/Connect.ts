export class ConnectPayload {
    sender: string = ""; //username
    receiver: string = ""; // username

    constructor() { }
}

export class ConnectResponse {
    status: boolean = false;
    constructor() { }
}