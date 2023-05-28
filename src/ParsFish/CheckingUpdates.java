package ParsFish;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import javax.net.ssl.SSLHandshakeException;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static ParsFish.Main.domains;


public class CheckingUpdates {
    public static String[][] domain_table;
    public static void Checkurl() {

    domain_table = new String[domains.size()][4];
    Document doc = null;
    int i = 0;

            for (Map.Entry entry : domains.entrySet()) {
                System.out.println("Домен: " + entry.getKey());
                String emp = "empty";
                doc = null;
                try {
                    try {
                        doc = Jsoup.connect("https://" + entry.getKey())
                                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                                .referrer("https://www.google.ru/")
                                .get();
                    } catch (Exception e) {
                        try {
                            doc = Jsoup.connect("http://" + entry.getKey())
                                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                                    .get();
                        } catch (ConnectException k) {
                            System.out.println("ConnectException");
                            domain_table[i][0] = (String) entry.getKey();
                            domain_table[i][1] = (String) entry.getValue();
                            domain_table[i][2] = " ConnectException ";
                            domain_table[i][3] = " ConnectException ";
                            i ++;
                            continue;
                        } catch (SocketException k) {
                            System.out.println("SocketException");
                            domain_table[i][0] = (String) entry.getKey();
                            domain_table[i][1] = (String) entry.getValue();
                            domain_table[i][2] = " SocketException ";
                            domain_table[i][3] = " SocketException ";
                            i ++;
                            continue;
                        }

                    }
                    if (doc != null) {
                        doc.select("head > meta[name = csrf-token]").remove();
                        doc.select("head > meta[name = salt]").remove();
                        doc.select("head > link[rel = stylesheet]").remove();
                        try {
                            doc.select("head > script[type = text/javascript]").remove();
                        } catch (Exception e) {

                        }
                        Elements head = doc.select("head");
                        domain_table[i][2] = MD5HeadEncode(head.html());
                        domain_table[i][3] = H1AndTitle(doc);
                        System.out.println("    Хэш : " + MD5HeadEncode(head.html()));
                        System.out.println("    Заголовок: " + H1AndTitle(doc));
                    } else {
                        domain_table[i][2] = "Empty";
                        domain_table[i][3] = "Empty";
                    }
                    domain_table[i][0] = (String) entry.getKey();

                    domain_table[i][1] = (String) entry.getValue();



                } catch (HttpStatusException e) {

                        System.out.println("404 or 403");

                        domain_table[i][2] = " 404 or 403 ";
                        domain_table[i][3] = " 404 or 403 ";

                    domain_table[i][0] = (String) entry.getKey();
                    domain_table[i][1] = (String) entry.getValue();

                }catch (SSLHandshakeException e) {

                        System.out.println("SSL error");

                        domain_table[i][2] = "SSL error";
                        domain_table[i][3] = "SSL error";

                    domain_table[i][0] = (String) entry.getKey();
                    domain_table[i][1] = (String) entry.getValue();
                }catch (SocketTimeoutException e) {



                        domain_table[i][2] = "Read timeout";
                        domain_table[i][3] = "Read timeout";


                    domain_table[i][0] = (String) entry.getKey();
                    domain_table[i][1] = (String) entry.getValue();
                }
                catch (Exception e) {
                    System.out.println("404 or 403");

                    domain_table[i][2] = " 404 or 403 ";
                    domain_table[i][3] = " 404 or 403 ";

                    domain_table[i][0] = (String) entry.getKey();
                    domain_table[i][1] = (String) entry.getValue();
                    e.printStackTrace();
                }
            i ++;
            }
        System.out.println("Результирующая таблица доменов, ip-адресов, хэшей и заголовков:");
        for (int j = 0; j < domain_table.length; j++) {  //идём по строкам
            for (int k = 0; k < 4; k++) {//идём по столбцам
                System.out.print(" " + domain_table[j][k] + " "); //вывод элемента
            }
            System.out.println();
        }
    }
    public static String MD5HeadEncode(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);

        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    public static String H1AndTitle (Document exp) {
        Elements h1 = exp.select("h1");
        Elements title = exp.select("title");
        return h1.textNodes() + " " + title.textNodes();
    }


}
