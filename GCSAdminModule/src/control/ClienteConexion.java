package control;

import okhttp3.*;
import org.json.JSONObject;

public class ClienteConexion {

    OkHttpClient cliente = new OkHttpClient();

    public JSONObject getJSON(String url) {
        Request request = new Request.Builder().url(url).build();
        try (Response response = cliente.newCall(request).execute()) {
            return new JSONObject(response.body().string());
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return null;
        }
    }

    public JSONObject postJSON(String url, RequestBody datos) {
        Request request = new Request.Builder().url(url).post(datos).build();
        try (Response response = cliente.newCall(request).execute()) {
            return new JSONObject(response.body().string());
        } catch (Exception e) {
            //System.out.println("Error: " + e);
            return null;
        }
    }

}
