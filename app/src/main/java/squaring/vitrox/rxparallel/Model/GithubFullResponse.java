package squaring.vitrox.rxparallel.Model;

import java.util.List;

public class GithubFullResponse {

    private String t;
    private GithubUser user;
    private List<GithubRepository> repositories;

    public GithubFullResponse(String t, GithubUser user, List<GithubRepository> repositories) {
        // store the values
        this.t = t;
        this.user = user;
        this.repositories = repositories;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public GithubUser getUser() {
        return user;
    }

    public void setUser(GithubUser user) {
        this.user = user;
    }

    public List<GithubRepository> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<GithubRepository> repositories) {
        this.repositories = repositories;
    }
}