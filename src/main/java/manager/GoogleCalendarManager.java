package manager;

import com.google.gson.Gson;
import common.Colour;
import common.CreatedEventResponseBean;
import common.EventBean;
import common.AuthorizationResponseBean;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Adrian Wieczorek on 10/13/2017.
 */
public class GoogleCalendarManager {
    public static String clientID = "100673469664-03qspc54ldgbbjesbdqkqbnrehs780ta.apps.googleusercontent.com";
    public static String clientSecret = "gGhg-jTgWsYaQrOnCK8fuChW";
    public static String redirectUri = "urn:ietf:wg:oauth:2.0:oob";
    public static String projectID = "rcsm-app";
    public static String JSONPath = "/resources/client_secrets.json";
    public static String scopeURL = "https://www.googleapis.com/auth/calendar";

    private String url = "https://accounts.google.com/o/oauth2/auth?client_id=" + clientID + "&redirect_uri=" + redirectUri + "&scope=" + scopeURL + "&response_type=code";
    private String calendarURL = "https://www.googleapis.com/calendar/v3/calendars/primary/events";

    private String authorizationCode;

    private AuthorizationResponseBean authorizationResponseBean;
    private CreatedEventResponseBean createdEventResponseBean;

    public GoogleCalendarManager() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public AuthorizationResponseBean getAuthorizationResponseBean() {
        return authorizationResponseBean;
    }

    public void setAuthorizationResponseBean(AuthorizationResponseBean authorizationResponseBean) {
        this.authorizationResponseBean = authorizationResponseBean;
    }

    public CreatedEventResponseBean getCreatedEventResponseBean() {
        return createdEventResponseBean;
    }

    public void setCreatedEventResponseBean(CreatedEventResponseBean createdEventResponseBean) {
        this.createdEventResponseBean = createdEventResponseBean;
    }

    public void authorization() throws IOException {
        String url = "https://www.googleapis.com/oauth2/v4/token";

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);

// add header
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("code", authorizationCode));
        urlParameters.add(new BasicNameValuePair("client_id", clientID));
        urlParameters.add(new BasicNameValuePair("client_secret", clientSecret));
        urlParameters.add(new BasicNameValuePair("redirect_uri", redirectUri));
        urlParameters.add(new BasicNameValuePair("grant_type", "authorization_code"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);
        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());

        Gson gson = new Gson();
        authorizationResponseBean = gson.fromJson(result.toString(), AuthorizationResponseBean.class);
        authorizationResponseBean.setResponseCode(response.getStatusLine().getStatusCode());
    }

    public void createEvent(Map<String, String> informationAboutMatch) throws IOException {
        EventBean event = buildEvent(informationAboutMatch);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(calendarURL);

        // add header
        post.setHeader("Authorization", "Bearer " + authorizationResponseBean.getAccess_token());
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept-Encoding", "UTF-8");

        StringEntity stringEntity = new StringEntity(event.toString(), "UTF-8");

        post.setEntity(stringEntity);

        HttpResponse response = client.execute(post);
        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());

        Gson gson = new Gson();
        createdEventResponseBean = gson.fromJson(result.toString(), CreatedEventResponseBean.class);
        createdEventResponseBean.setResponseCode(response.getStatusLine().getStatusCode());
    }

    private EventBean buildEvent(Map<String, String> matchMap) {
        String summary = "S: " + matchMap.get("gameType") + ", " + matchMap.get("homeTeamName") + " - " + matchMap.get("awayTeamName");
        String description = fixDescription(getDescription(matchMap));
        String colorId = Colour.Sage;
        String location = fixGameAddress(matchMap.get("addressGame"));

        Map<String, String> matchDateTimesMap = getMatchDateTimes(matchMap.get("gameStartDate") + " " + matchMap.get("gameStartTime") + ":00");
        String startDate = matchDateTimesMap.get("startDate");
        String endDate = matchDateTimesMap.get("endDate");

        return new EventBean(summary, description, colorId, location, startDate, endDate);
    }

    private Map<String, String> getMatchDateTimes(String gameStartDateTime) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime startDateTime = formatter.parseDateTime(gameStartDateTime);
        startDateTime.withZone(DateTimeZone.forID("Europe/Warsaw"));
        startDateTime.toCalendar(new Locale("pl", "PL"));
        startDateTime = startDateTime.minusHours(1);

        DateTime endDateTime = startDateTime.plusHours(3);

        Map<String, String> matchDateTimesMap = new HashMap<String, String>();
        matchDateTimesMap.put("startDate", startDateTime.toString());
        matchDateTimesMap.put("endDate", endDateTime.toString());

        return matchDateTimesMap;
    }

    private String getDescription(Map<String, String> matchMap) {
        return "Start meczu - " + matchMap.get("gameStartTime").replace(":", ".") +
                ", " + "Grupa/Numer meczu - " + matchMap.get("gameNumber") +
                (!matchMap.get("refereeFirst").equals("") ? ", " + "Sędzia pierwszy: " + matchMap.get("refereeFirst") : "") +
                (!matchMap.get("refereeSecond").equals("") ? ", " + "Sędzia drugi: " + matchMap.get("refereeSecond") : "") +
                (!matchMap.get("refereeSecretary").equals("") ? ", " + "Sekretarz: " + matchMap.get("refereeSecretary") : "") +
                (!matchMap.get("refereeLinesFirst").equals("") ? ", " + "Sędzia liniowy 1: " + matchMap.get("refereeLinesFirst") : "") +
                (!matchMap.get("refereeLinesSecond").equals("") ? ", " + "Sędzia liniowy 2: " + matchMap.get("refereeLinesSecond") : "") +
                (!matchMap.get("refereeLinesThird").equals("") ? ", " + "Sędzia liniowy 3: " + matchMap.get("refereeLinesThird") : "") +
                (!matchMap.get("refereeLinesFourth").equals("") ? ", " + "Sędzia liniowy 4: " + matchMap.get("refereeLinesFourth") : "") +
                (!matchMap.get("extraInformation").equals("") ? ", " + "Dodatkowe informacje: " + matchMap.get("extraInformation") : "");
    }

    private String fixGameAddress(String address) {
        address = address.replace("Warzawa", "Warszawa");

        return address;
    }

    private String fixDescription(String description){
        description = description.replace("&nbsp;", "").replace("\n", " ");

        return description;
    }
}
