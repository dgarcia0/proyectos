package data;
import beans.ApplicationContextHandler;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;


public class UserData implements Data {
    private List<User> users;
    private ListIterator<User> iterator;

   private void createProperties(){
       try {
           Properties properties = new Properties();
           properties.setProperty("name", "admin2");
           properties.setProperty("email", "admin2@mail.com");
           properties.setProperty("passwd", "123");

           File file = new File("admin.properties");
           FileOutputStream fileOut = new FileOutputStream(file);
           properties.store(fileOut, "Info Admin");
           fileOut.close();
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

   private void createXMLFile(){
       try {

           DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
           DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

           Document doc = docBuilder.newDocument();
           Element rootElement = doc.createElement("users");
           doc.appendChild(rootElement);

           Element user = doc.createElement("user");
           rootElement.appendChild(user);

           Element name = doc.createElement("name");
           name.appendChild(doc.createTextNode("user1"));
           user.appendChild(name);

           Element mail = doc.createElement("mail");
           mail.appendChild(doc.createTextNode("user1@mail.com"));
           user.appendChild(mail);

           Element passwd = doc.createElement("password");
           passwd.appendChild(doc.createTextNode("123"));
           user.appendChild(passwd);

           TransformerFactory transformerFactory = TransformerFactory.newInstance();
           Transformer transformer = transformerFactory.newTransformer();
           DOMSource source = new DOMSource(doc);
           StreamResult result = new StreamResult(new File("users.xml"));
           transformer.transform(source, result);

       } catch (ParserConfigurationException pce) {
           pce.printStackTrace();
       } catch (TransformerException tfe) {
           tfe.printStackTrace();
       }
   }

   private void getAdminFromProperties(){
       createProperties();

       Properties prop = new Properties();
       InputStream input = null;
       String name;
       String email;
       String passwd;

       try {
           input = new FileInputStream("admin.properties");

           prop.load(input);

           name = prop.getProperty("name");
           email = prop.getProperty("email");
           passwd = prop.getProperty("passwd");

           User u = new User();
           u.setName(name);
           u.setEmail(email);
           u.setPasswd(passwd);
           users.add(u);

       } catch (IOException ex) {
           ex.printStackTrace();
       } finally {
           if (input != null) {
               try {
                   input.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }

   }

   private void getUsersFromXML(){
       String mail = "";
       String usuario = "";
       String passwd = "";

       createXMLFile();

       try {
           File fXmlFile = new File("users.xml");
           DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
           DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
           Document doc = dBuilder.parse(fXmlFile);

           doc.getDocumentElement().normalize();

           NodeList nList = doc.getElementsByTagName("user");

           for (int temp = 0; temp < nList.getLength(); temp++) {

               Node nNode = nList.item(temp);

               if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                   Element eElement = (Element) nNode;

                   usuario = eElement.getElementsByTagName("name").item(0).getTextContent();
                   passwd = eElement.getElementsByTagName("password").item(0).getTextContent();
                   mail = eElement.getElementsByTagName("mail").item(0).getTextContent();
               }

               User u = new User();
               u.setName(usuario);
               u.setEmail(mail);
               u.setPasswd(passwd);
               users.add(u);
           }

       } catch (Exception e) {
           e.printStackTrace();
       }
   }

    private void prepareIterator(){
        iterator = users.listIterator();
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        prepareIterator();

        while (iterator.hasNext()){
            User iteratorUser = iterator.next();

            if(iteratorUser.getEmail().equals(email))return iteratorUser;
        }
        throw (UserNotFoundException) ApplicationContextHandler.context.getBean("userDataException");
    }

    public boolean exist(User user) {
        prepareIterator();

        while (iterator.hasNext()){
            User iteratorUser = iterator.next();

            if(iteratorUser.equals(user))return true;
        }
        return false;
    }

    public void add(User user) {
        if(!exist(user)) {
            this.users.add(user);
        }
    }

    public void remove(String email) throws UserNotFoundException {
        prepareIterator();

        while (iterator.hasNext()){
            User iteratorUser = iterator.next();

            if(iteratorUser.getEmail().equals(email)){
                iterator.remove();
                return;
            }
        }
        throw (UserNotFoundException) ApplicationContextHandler.context.getBean("userDataException");
    }

    public boolean login(String email, String passwd) {
        getAdminFromProperties();
        getUsersFromXML();
        prepareIterator();

        while (iterator.hasNext()){
            User iteratorUser = iterator.next();

            if(iteratorUser.getEmail().equals(email) && iteratorUser.getPasswd().equals(passwd)){
                return true;
            }
        }
        return false;
    }

    public List getAllUsers(){
        return (List) ((ArrayList) users).clone();
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}