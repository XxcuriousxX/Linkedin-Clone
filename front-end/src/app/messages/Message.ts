
export class MessagePayload {
    sender_username: string = "";
    receiver_username: string = "";
    message: string;

    constructor() { }
}

export class MessageResponse {
    messageId: number = -1;
    senderUsername : string = "";
    receiverUsername: string = "";
    message: string = "";
    senderId: number = -1;
    receiverId : number = -1;
    timeCreated: string = "";
    duration: string = "";
    stringTimeCreated: string = "";


    constructor()  { }
    
}
