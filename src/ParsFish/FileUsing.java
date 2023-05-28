package ParsFish;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static ParsFish.CheckingUpdates.domain_table;
import static ParsFish.Main.domain;

public class FileUsing {
    public static void start() throws IOException {
        java.io.File dir1 = new java.io.File (".");
        File file = new File(dir1.getCanonicalPath()+ "/" + domain + ".xml");
        if (file.exists()) {
            System.out.println("Обнаружен файл с записями о домене. Запуск проверки...");
            Begin();

        } else {
            CreateFile();
        }


    }
    public static void Begin()  {
        String mess = "";
        try {
            java.io.File dir1 = new java.io.File (".");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(dir1.getCanonicalPath() + "/" + domain + ".xml");
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("domain");
            for (int i = 0; i < domain_table.length; i++) {
                int chet = 0;
                for (int j = 0; j < nodeList.getLength(); j++) {
                    Node node = nodeList.item(j);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        if (Objects.equals(element.getElementsByTagName("domain_name")
                                .item(0).getTextContent(), domain_table[i][0])) {
                            chet ++;
                            if (element.getElementsByTagName("ip")
                                    .item(0).getTextContent().contains(domain_table[i][1])) {

                            }else {
                                System.out.println("У доменa " + domain_table[i][0] + " изменился ip адресс: " + domain_table[i][1] );
                                mess = mess + "\nУ доменa " + domain_table[i][0] + " изменился ip адресс: " + domain_table[i][1];
                                element.getElementsByTagName("ip")
                                        .item(0).
                                        setTextContent(element.
                                                getElementsByTagName("ip")
                                                .item(0)
                                                .getTextContent() + "\nnew: " + domain_table[i][1]);
                            }
                            if (element.getElementsByTagName("hash")
                                    .item(0).getTextContent().contains(domain_table[i][2])) {

                            }else {
                                System.out.println("У доменa " + domain_table[i][0] + " изменился хэш головы html страницы: " + domain_table[i][2] );
                                mess = mess + "\nУ доменa " + domain_table[i][0] + " изменился хэш головы html страницы: " + domain_table[i][2];
                                element.getElementsByTagName("hash")
                                        .item(0).
                                        setTextContent(element.
                                                getElementsByTagName("hash")
                                                .item(0)
                                                .getTextContent() + "\nnew: " + domain_table[i][2]);

                            }
                            if (element.getElementsByTagName("h1AndTitle")
                                    .item(0).getTextContent().contains(domain_table[i][3])) {

                            }else {
                                System.out.println("У доменa " + domain_table[i][0] + " изменился заголовок страницы: " + domain_table[i][3] );
                                mess = mess + "\nУ доменa " + domain_table[i][0] + " изменился заголовок страницы: " + domain_table[i][3];
                                element.getElementsByTagName("h1AndTitle")
                                        .item(0).
                                        setTextContent(element.
                                                getElementsByTagName("h1AndTitle")
                                                .item(0)
                                                .getTextContent() + "\nnew: " + domain_table[i][3]);


                            }

                        }



                    }
                }
                if (chet == 0) {
                    if (domain_table[i][0] != null) {
                        Node root = doc.getDocumentElement();
                        Element domen = doc.createElement("domain");
                        root.appendChild(domen);
                        Element domen_name = doc.createElement("domain_name");
                        domen_name.setTextContent(domain_table[i][0]);
                        domen.appendChild(domen_name);
                        Element ip = doc.createElement("ip");
                        ip.setTextContent(domain_table[i][1]);
                        domen.appendChild(ip);
                        Element hash = doc.createElement("hash");
                        hash.setTextContent(domain_table[i][2]);
                        domen.appendChild(hash);
                        Element title = doc.createElement("h1AndTitle");
                        title.setTextContent(domain_table[i][3]);
                        domen.appendChild(title);
                        System.out.println("Добавлен новый домен: " + domain_table[i][0] +
                                " его ip: " + domain_table[i][1] + " заголовок страницы: " + domain_table[i][3]);
                        mess = mess + "\nДобавлен новый домен: " + domain_table[i][0] +
                                " его ip: " + domain_table[i][1] + " заголовок страницы: " + domain_table[i][3];
                    }

                }

            }
            Transformer transformer =
                    TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(domain + ".xml"));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    EmailAlert.Email(mess);
    }

    public static void CreateFile() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("list");
            doc.appendChild(rootElement);

            for (int i = 0; i < domain_table.length; i++) {
                Element domen = doc.createElement("domain");
                rootElement.appendChild(domen);
                Element domen_name = doc.createElement("domain_name");
                domen_name.appendChild(doc.createTextNode(domain_table[i][0]));
                domen.appendChild(domen_name);
                Element ip = doc.createElement("ip");
                ip.appendChild(doc.createTextNode(domain_table[i][1]));
                domen.appendChild(ip);
                Element hash = doc.createElement("hash");
                hash.appendChild(doc.createTextNode(domain_table[i][2]));
                domen.appendChild(hash);
                Element title = doc.createElement("h1AndTitle");
                title.appendChild(doc.createTextNode(domain_table[i][3]));
                domen.appendChild(title);
            }


            doc.setXmlStandalone(true);
            doc.normalizeDocument();
            javax.xml.transform.TransformerFactory tf = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(doc);
            javax.xml.transform.stream.StreamResult result =
                    new javax.xml.transform.stream.StreamResult(new File(domain + ".xml"));
            transformer.transform(source, result);

            System.out.println("XML-файл успешно создан");

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
