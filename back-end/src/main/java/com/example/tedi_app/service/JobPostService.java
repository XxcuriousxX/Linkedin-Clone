package com.example.tedi_app.service;

import java.util.*;

import com.example.tedi_app.dto.JobPostRequest;
import com.example.tedi_app.dto.JobPostResponse;
import com.example.tedi_app.dto.MyJobResponse;
import com.example.tedi_app.model.Friends;
import com.example.tedi_app.model.JobPost;
import com.example.tedi_app.model.JobPostViews;
import com.example.tedi_app.model.User;
import com.example.tedi_app.repo.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class JobPostService {

    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;
    private final JobPostViewsRepository jobPostViewsRepository;
    private final JobVoteRepository jobVoteRepository;
    private final JobPostRepository jobPostRepository;
    private AuthService authService;


    public double dot(double[] x, double[] y) {
        double sum = 0.0f;
        for (int i = 0; i < x.length; i++)
            sum += x[i] * y[i];
        return sum;
    }


    public double[][] dot_arrays(double[][] x, double[][] y){

        double[][] z = new double[x.length][y[0].length];
//        for(int i=0; i < x[0].length ;i++){
//            for(int j=0;j < y.length;j++){
//                z[i][j] = x[i][j] * y[j][i];
//            }
//
//        }

        int Nrowx = x.length;
        int Ncolx = x[0].length;
        int Nrowy = y.length;
        int Ncoly = y[0].length;

        for(int i = 0; i < Nrowx; i++) {
            for (int j = 0; j < Ncoly; j++) {
                for (int k = 0; k < Nrowy; k++) {
                    z[i][j] = z[i][j] + x[i][k] * y[k][j];
                }
            }
        }
        return z;

    }




    public double[] get_line(double[][] x, int k) {
        int num_cols = x[0].length;
        double[] result = new double[num_cols];
        for (int i = 0; i < num_cols; i++)
            result[i] = x[k][i];
        return result;
    }

    public double[] get_col(double[][] x, int k){
        double[] res = new double[x.length];
        
        
        for ( int j = 0; j < x.length; j++){
            res[j] = x[j][k];
        }
        
        return res;
        

    }

    public double[][] Transpose(double[][] Q){
        double[][] Q_T = new double[Q[0].length][Q.length];
        for ( int i = 0; i < Q[0].length ; i++){
            for ( int j = 0; j < Q.length ; j++){
                Q_T[i][j] = Q[j][i];
            }
        }
        return Q_T;
    }

    public pair<double[][], double[][]> matrix_factorization(double[][] R, double[][] P, double[][] Q, int K) {

        double alpha = (double) 0.0002;
        double beta = (double) 0.02;
        int steps = 5000;

        Q = Transpose(Q);

        double eij = 0;
        for (int step = 0; step < steps; step++) {
            
            
            for ( int i = 0; i < R.length; i++){
                for( int j = 0; j < R[0].length; j++){
                    if (R[i][j] > 0){
                        eij = R[i][j] - dot(get_line(P, i), get_col(Q, j));
                        for (int k = 0; k < K; k++){
                            P[i][k] = P[i][k] + alpha * (2 * eij * Q[k][j] - beta * P[i][k]);
                            Q[k][j] = Q[k][j] + alpha * (2 * eij * P[i][k] - beta * Q[k][j]);
                        }
                    }
                }
            }

            double[][] eR = dot_arrays(P, Q);
            double e = 0.0;
            for (int i = 0; i < R.length; i++) {
                for (int j = 0; j < R[0].length; j++) {
                    if (R[i][j] > 0) {
                        e = e + (R[i][j] - dot(get_line(P, i), get_col(Q, j))) * (R[i][j] - dot(get_line(P, i), get_col(Q, j))); 
                        
                        for(int k = 0; k < K; k++){
                            e = e + (beta / 2) * (P[i][k]*P[i][k]) + (Q[k][j]*Q[k][j]);
                        }
                    
                    }
                }
            }
            if (e < 0.001)
                break;
        }
        return new pair(P, Transpose(Q));
    }



    // random arr of size N x K
    public double[][] random_array(int N, int K) {
        double[][] arr = new double[N][K];
        Random rand = new Random();       
        for ( int n = 0; n < N; n++){
            for ( int k = 0; k < K; k++){
                arr[n][k] = rand.nextDouble();
            }
        }
        return arr;
    }

    // random arr of size N x K in range [a, b]
    public double[][] random_array_in_range(int N, int K, double a, double b) {
        double[][] arr = new double[N][K];
        Random rand = new Random();
        for ( int n = 0; n < N; n++){
            for ( int k = 0; k < K; k++){
                arr[n][k] = a + (b - a) * rand.nextDouble();
            }
        }
        return arr;
    }
    
    public List<JobPostResponse> getSuggestions(String username) {
        


        Optional<User> user_opt = userRepository.findByUsername(username);
        User user = user_opt.orElseThrow(() -> new UsernameNotFoundException("No user " +
                "Found with username: "+ username));




        List<Friends> L_friends = friendsRepository.getAllConnectedUsers(user.getUserId());
        if (L_friends.isEmpty())
            return new ArrayList<>();


        List<JobPostViews> jobPostViewsList = new ArrayList<>();
        List<User> users_list = new ArrayList<>();

        for (Friends f : L_friends) {
            Long friend_id = (user.getUserId().equals(f.getUser_id1()) ? f.getUser_id2() : f.getUser_id1());
            Collection<JobPostViews> jobPostsViewsOpt = jobPostViewsRepository.findAllByUser_UserId(friend_id);
            jobPostViewsList.addAll(jobPostsViewsOpt);

            Optional<User> user_opt1 = userRepository.findByUserId(friend_id);
            User user1 = user_opt1.orElseThrow(() -> new UsernameNotFoundException("No user " +
                "Found with username: "+ username));
            users_list.add(user1);

        }

        // add my job post views
        users_list.add(user);
        Collection<JobPostViews> all_my_job_post_views = jobPostViewsRepository.findAllByUser_UserId(user.getUserId());
        jobPostViewsList.addAll(all_my_job_post_views);


        List<JobPost> jobPostsList = getAllInvolvedJobPosts(jobPostViewsList);
        if(jobPostsList.isEmpty()) return new ArrayList<>();

        int N = users_list.size();
        int M = jobPostsList.size();
        System.out.println("N = " + N + "   M = " + M);
        double[][] R = new double[N][M];

        for (int i = 0; i < N; i++)
            for (int j = 0; j < M; j++)
                R[i][j] = 0.0;

        Long[] userIds = new Long[N];
        Long[] jobPostsIds = new Long[M];
        int i = 0;
        int j;
        for (User u : users_list) {
            userIds[i++] = u.getUserId();
        }

        for (i = 0; i < N; i++) { // for each user
            Long user_id = userIds[i];
            for (j = 0; j < M; j++) {   // for each jobPost
                Long job_post_id = jobPostsList.get(j).getJobPostId();
                for (JobPostViews jpv : jobPostViewsList) {
                    if (jpv.getJobPost().getJobPostId().equals(job_post_id)
                            && jpv.getUser().getUserId().equals(user_id)) {
                        R[i][j] = jpv.getViews();
                        break;
                    }
                }
            }
        }

        print_array(R);
        System.out.println("\n");
        System.out.println("\n");
        List<String> categories_list = new ArrayList<>();

        for (JobPost jp : jobPostsList) {
            if (!categories_list.contains(jp.getKeywords()))
                categories_list.add(jp.getKeywords());
        }
        int K = categories_list.size();
//        int K = 6;
        double[][] P = random_array(N,K);
        double[][] Q = random_array(M,K);  //_in_range(M,K, 1.0, 5.0);

        pair<double[][], double[][]> p = matrix_factorization(R,P,Q,K);
        P = p.a;
        Q = p.b;
        Q = Transpose(Q);

        double[][] nR = dot_arrays(P,Q);
        print_array(nR);

        for (JobPost jp : jobPostsList) {
            System.out.println(jp.getKeywords());
        }

        int row = R.length - 1;
        System.out.println("IDDDDDD = >" + userIds[row]);
        double[] results = new double[M];
        for (j = 0; j < M; j++) {
            results[j] = nR[row][j];
        }

        int max1_col_index = 0;
        double max1_col = results[0];
        for (j = 0; j < M; j++) {
            if (max1_col < results[j]){
                max1_col = results[j];
                max1_col_index = j;
            }
        }




        List<JobPostResponse> jobPostResponseList = new ArrayList<>();
        for (j = 0; j < M; j++) {
            JobPost JP = jobPostsList.get(j);
            System.out.println(results[j] + " " + JP.getJobPostId() + "\n");
            if (results[j] > 2.5) {
                jobPostResponseList.add(mapToDto(JP));
            }
        }

        return jobPostResponseList;
    }
    
    
    public static JobPostResponse mapToDto(JobPost JP) {
        return new JobPostResponse(JP.getJobPostId(), JP.getTitle(), JP.getLocation()
                    , JP.getKeywords(), JP.getEmploymentType(), JP.getDetails(), JP.getRequiredSkills());
    }



    public List<JobPost> getAllInvolvedJobPosts(List<JobPostViews> jobPostViewsList) {
        List<JobPost> jobPostList = new ArrayList<>();
        for (JobPostViews jpv : jobPostViewsList) {
            if (id_exists(jpv.getJobPost().getJobPostId(), jobPostList)) // if job post exists in jobPostList
                continue;
            jobPostList.add(jobPostRepository.getByJobPostId(jpv.getJobPost().getJobPostId()));
        }
        return jobPostList;
    }

    private boolean id_exists(Long jp_id, List<JobPost> jobPostsList) {
        for (JobPost jp : jobPostsList) {
            if (jp_id.equals(jp.getJobPostId()))
                return true;
        }
        return false;
    }

    public void print_array(double[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.println();
            for (int j = 0; j < arr[0].length; j++) {
                System.out.print(String.format("%.2f\t", arr[i][j]));
            }
        }
    }

    


    // create job post 
    public void createJobPost(JobPostRequest jobPostRequest){
        
    
        Optional<User> user_opt1 = userRepository.findByUsername(jobPostRequest.getAuthorUsername());
            User user1 = user_opt1.orElseThrow(() -> new UsernameNotFoundException("No user " +
                "Found with username:" + jobPostRequest.getAuthorUsername()));
        
        JobPost jobPost = new JobPost(user1, jobPostRequest.getTitle(), jobPostRequest.getLocation(),
                                            jobPostRequest.getEmploymentType(), jobPostRequest.getDetails(),jobPostRequest.getRequiredSkills()
                                            ,jobPostRequest.getKeywords());
        
        jobPostRepository.save(jobPost);


    }

    @Transactional
    public void deleteJobPost(Long id){
        jobPostViewsRepository.deleteByJobPost_JobPostId(id);
        jobPostRepository.deleteJobPostByJobPostId(id);
        return;
    }

    public JobPostResponse getJobPost(Long id){ // will also view it
        JobPost jp =  jobPostRepository.getByJobPostId(id);
        User u = userRepository.findByUsername(authService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                "Found with username: "+ authService.getCurrentUser().getUsername()));
        
                
        JobPostViews jpv = jobPostViewsRepository.getByJobPostJobPostIdAndUserUserId(id, u.getUserId());
        
        if (jpv == null) {
            jpv = new JobPostViews(jp, u);
            jpv.increaseViews();
            jobPostViewsRepository.save(jpv);
        }
        else {
            jpv.increaseViews();
            System.out.println("rPrinint views --- " + jpv.getViews());
            jobPostViewsRepository.save(jpv);
        }
        
        return mapToDto(jp);
    }


    public JobPostResponse getJobPostRequest(Long id){ // will also view it
        JobPost jp =  jobPostRepository.getByJobPostId(id);
        User u = userRepository.findByUsername(authService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username: "+ authService.getCurrentUser().getUsername()));


        JobPostViews jpv = jobPostViewsRepository.getByJobPostJobPostIdAndUserUserId(id, u.getUserId());

        if (jpv == null) {
            jpv = new JobPostViews(jp, u);
            jobPostViewsRepository.save(jpv);
        }
        else {
            System.out.println("rPrinint views --- " + jpv.getViews());
            jobPostViewsRepository.save(jpv);
        }

        return mapToDto(jp);
    }

    public static List<JobPostResponse> mapAllJobPostsToDto(List<JobPost> jpList) {
        List<JobPostResponse> jprList = new ArrayList<>();
        for (JobPost jp : jpList) {
            jprList.add(mapToDto(jp));
        }
        return jprList;
    }

    public List<MyJobResponse> getMyJobs(String username) {
        List<MyJobResponse> myjobs = new ArrayList<>();

        Optional<User> user_opt = userRepository.findByUsername(username);
        User user = user_opt.orElseThrow(() -> new UsernameNotFoundException("No user " +
                "Found with username: "+ authService.getCurrentUser().getUsername()));

        List<JobPost> jobs = jobPostRepository.getAllByUserUserId(user.getUserId());

        if(jobs.isEmpty()){
            System.out.println("edw eimai twra re tetartirrrrrrr" + jobs + " iddd" + user.getUserId());
            return new ArrayList<MyJobResponse>(){};
        }
        else{
            ArrayList<User> users = new ArrayList<>();
            Integer views = -1;
            //for each job find all users and save them to list
            for( JobPost each : jobs){
                List<Long> users_id = jobVoteRepository.getUserIdForJobPost(each.getJobPostId());
                for(Long id: users_id){
                    System.out.println("edw id: "+ id);
                    System.out.println("edw views: "+ views);
                    System.out.println("edw job id: "+ each.getJobPostId());
                    Optional<User> usr_opt = userRepository.findByUserId(id);
                    User usr = usr_opt.orElseThrow(() -> new UsernameNotFoundException("No user " +
                            "Found with username: "+ authService.getCurrentUser().getUsername()));
                    users.add(usr);
                }

                if (jobPostViewsRepository.getJobPostViews(each.getJobPostId(),user.getUserId()).size() != 0){
                    views = jobPostViewsRepository.getJobPostViews(each.getJobPostId(),user.getUserId()).get(0);
                }else{
                    views = 0;
                }
                myjobs.add(new MyJobResponse(each.getJobPostId(),each.getTitle(),users,views));
                users.clear();
            }

        }

        for( MyJobResponse eeach : myjobs){
            System.out.println("edw eimai twra re tetarti: "+ eeach.getTitle());
            System.out.println("edw eimai twra re tetarti: "+ eeach.getJobpostid());
            System.out.println("edw eimai twra re tetarti size: "+ eeach.getUser_list().size());
        }

        return myjobs;
    }






}
