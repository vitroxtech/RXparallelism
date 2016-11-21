package squaring.vitrox.rxparallel.Network;


import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import squaring.vitrox.rxparallel.Model.GithubRepository;
import squaring.vitrox.rxparallel.Model.GithubUser;
import squaring.vitrox.rxparallel.Model.SearchUserResult;


public interface GitApiService {


    @GET("/users?")
    rx.Observable<List<SearchUserResult>> getListUsers(@Query("since") int number);

    @GET("/users/{user}")
    rx.Observable<GithubUser> getUserDetail(@Path("user") String user);

    @GET("/users/{username}/repos")
    rx.Observable<List<GithubRepository>> getUserRepos(@Path("username") String user);
}
