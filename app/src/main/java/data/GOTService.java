package data;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface GOTService {

    @GET("{user}")
    Call<Character> getCharacter(@Path("user") String character);

}
