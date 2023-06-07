package ibf2022.batch2.csf.backend;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Utils {

    public static JsonObject stringToJson(String s) throws IOException {
        try (InputStream is = new ByteArrayInputStream(s.getBytes())) {
            JsonReader jrd = Json.createReader(is);
            JsonObject jo = jrd.readObject();
            return jo;
        }
    }
}
