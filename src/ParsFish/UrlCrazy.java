package ParsFish;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import static ParsFish.Main.domains;
public class UrlCrazy {
    public static void UC() {
        Object obj = null;
        JSONParser parser = new JSONParser();
        try {
            obj = parser.parse(new FileReader("GAts.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) obj;

        JSONArray jsonArray = (JSONArray) jsonObject.get("typos");
        for (Object o : jsonArray) {
            JSONObject typos = (JSONObject) o;
            String str = (String) typos.get("resolved_a");
            if (!str.isEmpty()){
                if (!str.contains("23.217.138.108")){
                    domains.put((String) typos.get("name"), (String) typos.get("resolved_a"));

                }
            }

        }
    }
}
