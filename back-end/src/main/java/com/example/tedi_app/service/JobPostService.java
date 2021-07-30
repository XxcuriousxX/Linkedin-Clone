package com.example.tedi_app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.example.tedi_app.dto.JobPostResponse;
import com.example.tedi_app.model.Friends;
import com.example.tedi_app.model.JobPostViews;
import com.example.tedi_app.model.User;
import com.example.tedi_app.repo.FriendsRepository;
import com.example.tedi_app.repo.JobPostRepository;
import com.example.tedi_app.repo.JobPostViewsRepository;
import com.example.tedi_app.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class JobPostService {

    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;
    private final JobPostViewsRepository jobPostViewsRepository;



    public double dot(double[] x, double[] y) {
        double sum = 0.0f;
        for (int i = 0; i < x.length; i++)
            sum += x[i] * y[i];
        return sum;
    }


    public double[][] dot_arrays(double[][] x, double[][] y){

        double[][] z = new double[x[0].length][y.length];
        for(int i=0; i < x[0].length ;i++){
            for(int j=0;j < y.length;j++){
                z[i][j] = x[i][j] * y[j][i];
            }

        }

        return z;

    }




    public double[] get_line(double[][] x, int k) {
        int num_cols = x.length;
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

        for ( int i = 0; i < Q[0].length ; i++){
            for ( int j = 0; j < Q.length ; j++){
                Q[i][j] = Q[j][i];
            }
        }




        double eij = 0;
        for (int step = steps; step < steps; step++) {
            
            
            for ( int i = 0; i < R.length; i++){
                for( int j = 0; j < R[i].length; i++){
                    if (R[i][j] > 0){
                        eij = R[i][j] - dot(get_line(P, i), get_col(Q, j));
                        for (int k = 0; k < K; k++){
                            P[i][j] = P[i][k] + alpha * (2 * eij * Q[k][j] - beta * P[i][k]);
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



    // random arr of size L x K   
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
    
    
    
    
    public JobPostResponse[] getSuggestions(String username) {
        


        Optional<User> user_opt = userRepository.findByUsername(username);
        User user = user_opt.orElseThrow(() -> new UsernameNotFoundException("No user " +
                "Found with username: "+ username));




        List<Friends> L_friends = friendsRepository.getAllConnectedUsers(user.getUserId());
        if (L_friends.isEmpty())
            return new JobPostResponse[]{};

        List<JobPostViews> jobPosts = new ArrayList<>();
        List<User> users_list = new ArrayList<>();

        for (Friends f : L_friends) {
            Optional<JobPostViews> jobPostViewsOpt = jobPostViewsRepository.findByUserUserId( (user.getUserId() == f.getUser_id1() ? f.getUser_id2() : f.getUser_id1()) );
            JobPostViews jobPostViews = jobPostViewsOpt.orElseThrow(() -> new UsernameNotFoundException("No user " +
                    "Found with username: "+ username));
            jobPosts.add(jobPostViews);
            
            Optional<User> user_opt1 = userRepository.findByUserId((user.getUserId() == f.getUser_id1() ? f.getUser_id2() : f.getUser_id1()));
            User user1 = user_opt1.orElseThrow(() -> new UsernameNotFoundException("No user " +
                "Found with username: "+ username));
            users_list.add(user1);

        }

        
        users_list.add(user);
        int N = users_list.size();
        int M = jobPosts.size();
        double[][] R = new double[N][M];


        Long[] userIds = new Long[N];
        Long[] jobPostsIds = new Long[M];
        int i = 0; 
        for (User u : users_list) {
            userIds[i++] = u.getUserId();
        }
        int j = 0;
        for (JobPostViews jp : jobPosts) {
            jobPostsIds[j++] = jp.getJobPost().getJobPostId();
        }

        for (i = 0; i < N; i++) {
            Long user_id = userIds[i];
            for (j = 0; j < M; j++) {
                Long jobpost_id = jobPostsIds[j];
                for (JobPostViews jp : jobPosts) {
                    if (jp.getUser().getUserId() == user_id && jp.getJobPost().getJobPostId() == jobpost_id) {
                        R[i][j] = jp.getViews();
                        break;
                    }
                }
            }
        }


        int K = 10;
        double[][] P = random_array(N,K);
        double[][] Q = random_array(M,K);
        pair<double[][], double[][]> p = matrix_factorization(R,P,Q,K);
        P = p.a;
        Q = p.b;
        Q = Transpose(Q);

        double[][] nR = dot_arrays(P,Q);
        for (i = 0; i < nR.length; i++) {
            System.out.println();
            for (j = 0; j < nR[0].length; j++) {
                System.out.print(nR[i][j] + " ");
            }
        }



        return new JobPostResponse[]{};
    }







}
