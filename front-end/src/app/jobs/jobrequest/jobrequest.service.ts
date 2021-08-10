import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {LikePayload} from "../../post/like/like.payload";
import {Observable} from "rxjs";
import {LikeResponse} from "../../post/like/LikeResponse";
import {JobResponse} from "./JobResponse";
import {JobrequestPayload} from "./jobrequest.payload";

@Injectable({
  providedIn: 'root'
})
export class JobrequestService {

  constructor(private http: HttpClient) { }

  like(jobrequestPayload: JobrequestPayload): Observable<JobResponse> {
    return this.http.post<JobResponse>('https://localhost:8443/api/jobrequest/', jobrequestPayload);
  }

  has_liked(jobrequestPayload: JobrequestPayload): Observable<JobResponse> {
    return this.http.get<JobResponse>('https://localhost:8443/api/jobrequest/has_jobrequest/' + jobrequestPayload.postId);
  }



}
