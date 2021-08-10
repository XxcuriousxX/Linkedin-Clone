
export class JobPostResponse {
    jobPostId: number = -1;
    title: string = "";
    details: string = "";
    requiredSkills: string = "";
    employmentType:string = "";
    skills:string[] = [];
}

export class JobPostModel {
    authorUsername: string = "";
    title:string = "";
    location:string = "";
    employmentType:string = "";
    details : string = "";
    requiredSkills : string = ""; // "skill1,skill2,...,skillN"
    keywords : string = "";   // "keyword1,keyword2,...,keywordN"
}
