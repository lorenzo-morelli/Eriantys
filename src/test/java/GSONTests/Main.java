package GSONTests;

import com.google.gson.Gson;

public class Main {
    public static void main(String[] args){
        serializeUserSimple();
    }
    private static void serializeUserSimple(){
        UserSimple user = new UserSimple(
                "Fer",
                "test@test.com",
                21,
                true
        );
        Gson gson = new Gson();
        String json = gson.toJson(user);
    }
    private static void deserializeUserSimple(){
        String userJson = "";
    }
}
