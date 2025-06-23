package dal;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.UserGoogle;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.ClientProtocolException;
public class LoginByGoogle {

    public static final String GOOGLE_CLIENT_ID = "126173599157-rommu85itdc878aogtrngu2rvhc1l1ee.apps.googleusercontent.com";
    public static final String GOOGLE_CLIENT_SECRET = "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";
    public static final String GOOGLE_REDIRECT_URI = "http://localhost:8081/why/LoginGoogle";
    public static final String GOOGLE_GRANT_TYPE = "authorization_code";
    public static final String GOOGLE_LINK_GET_TOKEN = "https://oauth2.googleapis.com/token";
    public static final String GOOGLE_LINK_GET_USER_INFO = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";

    public String getToken(String code) throws ClientProtocolException, IOException {
        String response = Request.Post(GOOGLE_LINK_GET_TOKEN)
                .bodyForm(
                        Form.form()
                                .add("client_id", GOOGLE_CLIENT_ID)
                                .add("client_secret", GOOGLE_CLIENT_SECRET)
                                .add("redirect_uri", GOOGLE_REDIRECT_URI)
                                .add("code", code)
                                .add("grant_type", GOOGLE_GRANT_TYPE)
                                .build()
                )
                .execute().returnContent().asString();
        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        String accessToken = jobj.get("access_token").toString().replaceAll("\"", "");
        return accessToken;
    }

    public UserGoogle getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
        String link = GOOGLE_LINK_GET_USER_INFO + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        UserGoogle googlePojo = new Gson().fromJson(response, UserGoogle.class);
        return googlePojo;

    }

}
