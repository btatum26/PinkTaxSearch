import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class GoogleSearchResults {

    public static void main(String[] args) {

        // product name or barcode number
        String query = "047400658899";
        String query2 = "";

        // creates a url that is formated for the SerpAPI
        try {
            String url =
                "https://serpapi.com/search?start=0&engine=google&num=5&q="
                    + query + "&api_key="
                    + "5c9b852fdf7764f5155b687b4e82b708a9d5fcf47f05fd4ed9db9b0e65bf69d3";

            // Uses "GET" to receive the contents of API
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection)obj.openConnection();
            con.setRequestMethod("GET");

            // Takes in the data from API and stores it in a StringBuilder
            BufferedReader in = new BufferedReader(new InputStreamReader(con
                .getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            in.close();

            // converts data in the StringBuilder to JSON for easier access.
            JSONObject result = new JSONObject(response.toString());
            JSONArray organicResults = result.getJSONArray("organic_results");

            // takes the results of the first google search and sets the next
            // query equal to it. This section also removes all the spaces from
            // the query so the URL format is intact.
            query2 = organicResults.getJSONObject(0).getString("title");
            query2 = query2.substring(0, query2.indexOf("-") - 1);
            query2 = query2.replaceAll(" ", "_");

// =====================================================================================================================
// The google shopping results

            // creates new URL
           
            String url2 =
                "https://serpapi.com/search?start=0&engine=google_shopping&q="
                    + query2 + "&api_key="
                    + "5c9b852fdf7764f5155b687b4e82b708a9d5fcf47f05fd4ed9db9b0e65bf69d3";

            URL obj2 = new URL(url2);
            HttpURLConnection con2 = (HttpURLConnection)obj2.openConnection();

            // gets contents of the second API request

            con2.setRequestMethod("GET");

            int responseCode = con2.getResponseCode();
           // System.out.println("\nSending 'GET' request to URL : " + url);
           // System.out.println("Response Code : " + responseCode);

            // stores the JSON in another StringBuilder
            BufferedReader in2 = new BufferedReader(new InputStreamReader(con2
                .getInputStream()));
            String inputLine2 = "";
            StringBuffer response2 = new StringBuffer();

            while ((inputLine2 = in2.readLine()) != null) {
                response2.append(inputLine2 + "\n");
            }

            in2.close();
            // =====================================================================================================================
            // Sorts products from cheapest to most expensive

            
            //creats a list to store all the prodcuts 
            List<JSONObject> productList = new ArrayList<>();

            //Uses the results from the last API get request and converts it to JSON
            JSONObject prevResult = new JSONObject(response2.toString());
            JSONArray products = new JSONArray(prevResult.getJSONArray(
                "shopping_results"));

            //Adds the products into the list and sorts the products from cheapest to most expensive 
            for (int i = 0; i < products.length(); i++) {
                JSONObject product = products.getJSONObject(i);
                productList.add(product);

                Collections.sort(productList, (o1, o2) -> {
                    double price1 = o1.getDouble("extracted_price");
                    double price2 = o2.getDouble("extracted_price");
                    return Double.compare(price1, price2);
                });
            }
                int k =0;
                //Prints the results of the list of a number "k" amount of prodcuts.
                System.out.println(
                    "Product List (Cheapest to Most Expensive):");
                for (JSONObject product1 : productList) {
                    System.out.println("Title: " + product1.getString("title")
                        + ", Price: $" + product1.getDouble("extracted_price"));
                    k++;
                    if (k == 15)
                        break;
                }

            

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
