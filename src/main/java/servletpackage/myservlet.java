package servletpackage;



import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class myservlet
 */
public class myservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public myservlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		

		//		API key
		String apikey="eb7d220259b532f0f8bea7f27b8f0e99";
//		city name
		String city=request.getParameter("city");
		
		String apiurl="https://api.openweathermap.org/data/2.5/weather?q="+city +"&appid="+apikey;
		
//		api network conecction.
		URL url=new URL(apiurl);
	    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	    connection.setRequestMethod("GET");
	    
//	    reading the data from network.
	    InputStream inputstream=connection.getInputStream();
	    InputStreamReader reader=new InputStreamReader(inputstream);
	    
//	    storing the data in string 
	    
	    StringBuilder responsecontent=new StringBuilder();
	    
	    Scanner sc=new Scanner(reader);
	    
	    while (sc.hasNext()) {
	    	responsecontent.append(sc.nextLine());
	    }
	    sc.close();
	    
//	    System.out.println(responsecontent);
	    
	    
	    Gson gson=new Gson();
	    JsonObject jsonObject=gson.fromJson(responsecontent.toString(), JsonObject.class);
//	    System.out.println(jsonObject);
	    
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
        
	    
        // Set the data as request attributes (for sending to the jsp page)
        request.setAttribute("date", date);
        request.setAttribute("city", city);
        request.setAttribute("temperature", temperatureCelsius);
        request.setAttribute("weatherCondition", weatherCondition); 
        request.setAttribute("humidity", humidity);    
        request.setAttribute("windSpeed", windSpeed);
        request.setAttribute("weatherData", responsecontent.toString());
        
        connection.disconnect();
        
     // Forward the request to the weather.jsp page for rendering
        request.getRequestDispatcher("index.jsp").forward(request, response);

	} 
	
	
}
