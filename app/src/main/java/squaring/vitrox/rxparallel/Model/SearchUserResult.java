package squaring.vitrox.rxparallel.Model;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Created by miguelgomez on 11/11/16.
 */

public class SearchUserResult {

    @JsonProperty("login")
    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
