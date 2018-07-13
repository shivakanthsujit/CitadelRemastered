package data;

public class ApiUtils {

    public static final String BASE_URL = "https://api.got.show/api/characters/";

    public static GOTService getGOTService() {
        return RetrofitClient.getClient(BASE_URL).create(GOTService.class);
    }
}
