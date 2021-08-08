export class FullJobPostModel { // JobPostResponse
  jobPostId: number;
  title:string;
  location:string;
  employmentType:string ;
  details : string;
  requiredSkills : string ; // "skill1,skill2,...,skillN"
  keywords : string;   // "keyword1,keyword2,...,keywordN"
}
