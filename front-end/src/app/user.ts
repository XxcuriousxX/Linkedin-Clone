
export class User {
    username: string = "";
    first_name: string = "";
    last_name: string = "";
    password: string = "";
    email : string = "";
    phone: string = "";
    company_name: string = "";
    image: string = "";


    constructor() { }

}

export class UserResponse {
  userId : number;
  username:string;
  password: string;
  email:string;
  phone:string;
  first_name:string;
  last_name:string;
  company_name:string;
  image:string;
}
