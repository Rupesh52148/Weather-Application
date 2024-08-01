package mypackage;

import java.io.IOException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String apiKey="f2e3e2bbc0dda4bf503bbd84985d0966";
		String city=request.getParameter("city");
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
		//Api integration
		try {
		  URL url = new URL(apiUrl);
          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
          connection.setRequestMethod("GET");
          // Reating the data from network
          InputStream inputStream = connection.getInputStream();
          InputStreamReader reader = new InputStreamReader(inputStream);
          // input lene ke liye from the reader ,will create Scanner object
          Scanner scanner = new Scanner(reader);
          StringBuilder responseContent = new StringBuilder();

          while (scanner.hasNext()) {
              responseContent.append(scanner.nextLine());
          }
          
//         System.out.println(responseContent);
          scanner.close();
          
          Gson gson = new Gson();
          JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
//          System.out.println(jsonObject);
          //Date & Time
          long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
          String date = new Date(dateTimestamp).toString();
          
          //Temperature
          double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
          int temperatureCelsius = (int) (temperatureKelvin - 273.15);
         
          //Humidity
          int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
          
          //Wind Speed
          double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
          
          //Weather Condition
          String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
          request.setAttribute("date", date);
          request.setAttribute("city", city);
          request.setAttribute("temperature", temperatureCelsius);
          request.setAttribute("weatherCondition", weatherCondition); 
          request.setAttribute("humidity", humidity);    
          request.setAttribute("windSpeed", windSpeed);
          request.setAttribute("weatherData", responseContent.toString());
          
          connection.disconnect();
		}catch(IOException e) {
			  e.printStackTrace();
		}
          
          // Forward the request to the weather.jsp page for rendering
          request.getRequestDispatcher("index.jsp").forward(request, response);

	}

}
