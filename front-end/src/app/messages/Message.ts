
export class MessagePayload {
    sender_username: string = "";
    receiver_username: string = "";
    message: string;

    constructor() { }
}

export class MessageResponse {
    message: string = "";
    receiverId : number = -1;
    timeCreated: string = "";
    messageId: number = -1;
    senderId: number = -1;
    constructor()  { }
    
}
