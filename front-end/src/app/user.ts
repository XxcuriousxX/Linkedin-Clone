
export class User {
    username: string = "";
    first_name: string = "";
    last_name: string = "";
    password: string = "";
    email : string = "";
    phone: string = "";
    company_name: string = "";
    image: string = "";


    // constructor(uname :string, fname :string, lname :string
    //                     , em :string, ph :string, wp :string) {
    //     this.username = uname;
    //     this.firstname = fname;
    //     this.lastname = lname;
    //     this.email = em;
    //     this.phone = ph;
    //     this.workplace = wp;
    // }
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
