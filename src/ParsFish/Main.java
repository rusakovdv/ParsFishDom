package ParsFish;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    public static String domain;
     public static HashMap<String, String> domains = new HashMap<>();


    public static void  main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in);
        System.out.println("Исследуемый домен: ");
        domain = "";
        String resHes = toHex(domain);
        System.out.println("Процесс взаимодействия с API DNS Twister:");
        Main.fuzz(resHes);
        System.out.println("Обработка результатов с утилиты URLCrazy...");
        UrlCrazy.UC();
        System.out.println("Сводная таблица доменов и их ip адресов: ");
        java.io.File dir1 = new java.io.File (".");
        for (Map.Entry entry : domains.entrySet()) {
            System.out.println(entry);
        }
        System.out.println("Вычисление хеша и заголовков исследуемых доменов:");
        CheckingUpdates.Checkurl();
        FileUsing.start();


    }

    public static String toHex(String arg) {
        String Hex = null;
        Object obj = null;
        
        try {
            Hex = Jsoup.connect("https://dnstwister.report/api/to_hex/" + arg).ignoreContentType(true).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONParser parser = new JSONParser();
        try {
            obj = parser.parse(Hex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) obj;
        return (String) jsonObject.get("domain_as_hexadecimal");
    }

    public static String fuzz(String fuzzexp) {

        String js = null;
        Object obj = null;
        String ipIdnt = null;
        try {
            js = Jsoup.connect("https://dnstwister.report/api/fuzz/" + fuzzexp).ignoreContentType(true).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONParser parser = new JSONParser();
        try {
            obj = parser.parse(js);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray jsonArray = (JSONArray) jsonObject.get("fuzzy_domains");
        for (Object o : jsonArray) {
            JSONObject dom = (JSONObject) o;
            ipIdnt = (String) dom.get("resolve_ip_url");
            proverka(ipIdnt);
            if (proverka(ipIdnt) != "false") {
                System.out.println(dom.get("domain") + " : " + proverka(ipIdnt));
                domains.put((String) dom.get("domain"), proverka(ipIdnt));
            }
        }
        return js;
    }

    public static String proverka(String exp) {
        String js = null;
        Object obj = null;
        try {
            js = Jsoup.connect(exp).ignoreContentType(true).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONParser parser = new JSONParser();
        try {
            obj = parser.parse(js);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) obj;
        String ips = (String) jsonObject.get("ip").toString();
            return ips;
        }
    }


