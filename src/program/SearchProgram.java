package program;

import manager.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;

public class SearchProgram {
    private DataManager dm;

    public SearchProgram(){
        dm = new DataManager();
    }

    // Nested classes for function 'search'
    private static class SearchResponse {
        Queries queries;
        Item[] items;
    }

    private static class Queries {
        Request[] request;
    }

    private static class Request {
        String searchTerms;
        String totalResults;
    }

    private static class Item {
        String title;
        String link;
    }

    public String search(String searchKey){
        String apiKey = "AIzaSyCejEk2H_z-WQoWJH0wuUPyaX3N1Grpim4";
        String engineId = "22d5e92c8d93a4a6d";

        String apiUrl = "https://www.googleapis.com/customsearch/v1?";
        String url = "";

        // If searchKey has search option
        if(searchKey.contains("?")){
            String searchWord = searchKey.split("\\?")[0].trim();
            String searchOption = searchKey.split("\\?")[1];
            url = apiUrl + "key=" + apiKey + "&cx=" + engineId + "&q=" + searchWord + "&" + searchOption;
        }
        // If searchKey has no search option
        else{
            url = apiUrl + "key=" + apiKey + "&cx=" + engineId + "&q=" + searchKey;
        }

        try{
            // Create a URL object from the specified URL
            URL requestUrl = new URL(url);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();

            // Set the request method (GET by default)
            connection.setRequestMethod("GET");

            // Send the request and receive the response
            int responseCode = connection.getResponseCode();

            // If response is not OK, return ""
            if(responseCode != 200){
                return "";
            }
            // If response is OK
            else{
                // Read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                String jsonResponse = response.toString();

                // Convert JSON response to Java object using GSON
                Gson gson = new Gson();
                SearchResponse searchResponse = gson.fromJson(jsonResponse, SearchResponse.class);

                // Get must-have data
                String searchTerm = searchResponse.queries.request[0].searchTerms;
                String totalResults = searchResponse.queries.request[0].totalResults;
                String title0 = searchResponse.items[0].title;
                String link0 = searchResponse.items[0].link;
                String title1 = searchResponse.items[1].title;
                String link1 = searchResponse.items[1].link;
                String title2 = searchResponse.items[2].title;
                String link2 = searchResponse.items[2].link;

                // Put these data into content
                StringBuilder content = new StringBuilder();
                content.append("SearchTerm: " + searchTerm + "\n");
                content.append("TotalResults: " + totalResults + "\n");
                content.append("TOP1 title: " + title0 + "\n" + "TOP1 link: " + link0 +  "\n");
                content.append("TOP2 title: " + title1 + "\n" + "TOP2 link: " + link1 +  "\n");
                content.append("TOP3 title: " + title2 + "\n" + "TOP3 link: " + link2 +  "\n");

                // Close the connection
                connection.disconnect();

                // Return content
                return content.toString();
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean save(String id, String searchKey){
        if(id == null){
            return false;
        }
        else{
            dm.saveData(id, searchKey);
            return true;
        }
    }

    public String load(String id){
        if(id == null || !dm.isExistingData(id)){
            return "";
        }
        else{
            return dm.loadData(id);
        }
    }

    public String loadHot(String id){
        if(id == null){
            return "";
        }
        else{
            return dm.loadHotData();
        }
    }
}
