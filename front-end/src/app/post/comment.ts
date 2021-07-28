export class CommentRequest {
    postId: number = -1;
    username: string = "";
    text: string = "";
}

export class CommentResponse {
    postId: number = -1;
    username = ""; // the user who created the comment
    text = "";
    createdDate = "";
}