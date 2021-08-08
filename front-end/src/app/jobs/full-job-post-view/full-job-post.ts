export class FullJobPostModel {
  jobPostId: number;
  authorUsername: string ;
  title:string;
  location:string;
  employmentType:string ;
  details : string;
  requiredSkills : string ; // "skill1,skill2,...,skillN"
  keywords : string;   // "keyword1,keyword2,...,keywordN"
}
