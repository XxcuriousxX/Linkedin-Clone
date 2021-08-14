import {User} from "../../user";

export class MyJobResponse {
  jobpostid: number = -1;
  title: string = "";
  views: number = -1;
  user_list:User[]= [];
}
