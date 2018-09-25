package parser;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adrian Wieczorek on 10/9/2017.
 */
public class RefereeCastWebParser {
    int connectionTimeoutMs = 10000;

    public Integer loginToTheWebSite(String login, String password){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://www.obsady.wsmwzps.pl/login.php");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("log_usr", login));
        nvps.add(new BasicNameValuePair("log_pass", password));
        try{
            request.setEntity(new UrlEncodedFormEntity(nvps));
        }catch(Exception e){
            e.printStackTrace();
        }

        CloseableHttpResponse response = null;
        try{
            response = httpClient.execute(request);
        }catch(IOException e){
            e.printStackTrace();
        }

        HttpEntity entity = response.getEntity();

        try{
            System.out.println(response.getStatusLine());
            System.out.println(response.getEntity());
            System.out.println(response.getEntity().getContent());
        }catch(IOException e){
            e.printStackTrace();
        }

        return response.getStatusLine().getStatusCode();
    }

    public Map<String, String> getInformationAboutMatch(String URL) throws IOException, NullPointerException{
        Map<String, String> responseMap = new HashMap<String, String>();

        Document document = null;

        document = Jsoup.parse(new URL(URL), connectionTimeoutMs);

        System.out.println(document.title());

        Elements elements = document.select("div.container").select("div.panel").select("div.panel-body");

        String gameType = elements.select("div[class=col-xs-1 col-xs-offset-2 ]").first().getElementsByTag("h5").html();
        responseMap.put("gameType", gameType);
        System.out.println("Rozgrywki: " + gameType);

        String gameNumber = elements.select("div[class=col-xs-2 col-xs-offset-2 ]").first().getElementsByTag("h5").first().getElementsByTag("b").html();
        responseMap.put("gameNumber", gameNumber);
        System.out.println("Numer meczu: " + gameNumber);

        String gameStartDate = elements.select("div[class=col-xs-3]").first().getElementsByTag("h5").first().getElementsByTag("b").html();
        responseMap.put("gameStartDate", gameStartDate);
        System.out.println("Data: " + gameStartDate);

        String homeTeamName = elements.select("div[class=col-xs-5 text-center]").first().getElementsByTag("h4").first().getElementsByTag("b").html();
        responseMap.put("homeTeamName", homeTeamName.replace("\"", "'"));
        System.out.println("Gospodarz: " + homeTeamName);

        String awayTeamName = elements.select("div[class=col-xs-5 text-center]").get(1).getElementsByTag("h4").first().getElementsByTag("b").html();
        responseMap.put("awayTeamName", awayTeamName.replace("\"", "'"));
        System.out.println("Gosc: " + awayTeamName);

        String addressGame = elements.select("div[class=col-xs-8 ]").get(0).getElementsByTag("h5").html();
        responseMap.put("addressGame", addressGame);
        System.out.println("Adres: " + addressGame);

        String gameStartTime = elements.select("div[class=col-xs-8 ]").get(1).getElementsByTag("h5").html();
        responseMap.put("gameStartTime", gameStartTime);
        System.out.println("Godzina: " + gameStartTime);

        String refereeFirst = elements.select("div[class=panel panel-info]").get(0).select("div[class=col-xs-3]").get(0).select("button[class=btn btn-link]").html();
        responseMap.put("refereeFirst", refereeFirst);
        System.out.println("Sedzia pierwszy: " + refereeFirst);

        String refereeSecond = elements.select("div[class=panel panel-info]").get(0).select("div[class=col-xs-3]").get(1).select("button[class=btn btn-link]").html();
        responseMap.put("refereeSecond", refereeSecond);
        System.out.println("Sedzia drugi: " + refereeSecond);

        String refereeSecretary = elements.select("div[class=panel panel-info]").get(0).select("div[class=col-xs-3]").get(2).select("button[class=btn btn-link]").html();
        responseMap.put("refereeSecretary", refereeSecretary);
        System.out.println("Sekretarz: " + refereeSecretary);

        String refereeLinesFirst = elements.select("div[class=panel panel-info]").get(0).select("div[class=col-xs-3]").get(3).select("button[class=btn btn-link]").html();
        responseMap.put("refereeLinesFirst", refereeLinesFirst);
        System.out.println("Sedzia liniowy I: " + refereeLinesFirst);

        String refereeLinesSecond = elements.select("div[class=panel panel-info]").get(0).select("div[class=col-xs-3]").get(4).select("button[class=btn btn-link]").html();
        responseMap.put("refereeLinesSecond", refereeLinesSecond);
        System.out.println("Sedzia liniowy II: " + refereeLinesSecond);

        String refereeLinesThird = elements.select("div[class=panel panel-info]").get(0).select("div[class=col-xs-3]").get(5).select("button[class=btn btn-link]").html();
        responseMap.put("refereeLinesThird", refereeLinesThird);
        System.out.println("Sedzia liniowy III: " + refereeLinesThird);

        String refereeLinesFourth = elements.select("div[class=panel panel-info]").get(0).select("div[class=col-xs-3]").get(6).select("button[class=btn btn-link]").html();
        responseMap.put("refereeLinesFourth", refereeLinesFourth);
        System.out.println("Sedzia liniowy IV: " + refereeLinesFourth);

        String extraInformation;
        if(elements.select("div[class=panel panel-info]").size() == 1){
            extraInformation = "";
        }else{
            extraInformation = elements.select("div[class=panel panel-info]").get(1).select("div[id=info]").get(0).select("div[class=col-xs-8 col-md-offset-2]").select("p").html();
        }

        responseMap.put("extraInformation", extraInformation);
        System.out.println("Dodatkowe informacje: " + extraInformation);

        return responseMap;
    }
}
