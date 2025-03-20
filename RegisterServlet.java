package com.RealState.servlets;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	
    	
    	 // Debug print to console
        System.out.println("Received registration request with parameters:");
        request.getParameterMap().forEach((key, value) -> {
            System.out.println(key + ": " + Arrays.toString(value));
        });
        // Get form data
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType");

        // Create user data as a Map
        Map<String, String> userData = new HashMap<>();
        userData.put("firstName", firstName);
        userData.put("lastName", lastName);
        userData.put("email", email);
        userData.put("phone", phone);
        userData.put("password", password);
        
        
        
        // Debug print collected data
        System.out.println("Collected user data:");
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phone);
        System.out.println("User Type: " + userType);
        

        // Determine which file to update based on user type
        String filePath;
        if ("agent".equals(userType)) {
            filePath = "C:\\Users\\user\\Downloads\\project\\RealState\\src\\main\\webapp\\WEB-INF\\data\\estateAgent.json";
        } else {
            filePath = "C:\\Users\\user\\Downloads\\project\\RealState\\src\\main\\webapp\\WEB-INF\\data\\buyer.json";
        }

        File jsonFile = new File(filePath);

        // Create Gson instance
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Initialize list to store users
        List<Map<String, String>> userList = new ArrayList<>();

        // If file exists, read existing data
        if (jsonFile.exists()) {
            try (Reader reader = new FileReader(jsonFile)) {
                // Create a Type for List<Map<String, String>>
                Type userListType = new TypeToken<List<Map<String, String>>>() {
                }.getType();
                userList = gson.fromJson(reader, userListType);

                // If parsing returns null, initialize with empty list
                if (userList == null) {
                    userList = new ArrayList<>();
                }
            } catch (Exception e) {
                // If parsing fails, start with empty list
                userList = new ArrayList<>();
            }
        }

        // Add new user to the list
        userList.add(userData);

        // Ensure directory exists
        jsonFile.getParentFile().mkdirs();

        // Write updated JSON back to file
        try (Writer writer = new FileWriter(jsonFile)) {
            gson.toJson(userList, writer);
        } catch (IOException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save user data");
            return;
        }

        // Redirect to success page or login page
        response.sendRedirect("UserLogin.jsp?registered=true");
    }
}