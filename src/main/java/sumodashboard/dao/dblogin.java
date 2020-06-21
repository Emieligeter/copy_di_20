package sumodashboard.dao;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@WebServlet ("/dashboard.html")
public class dblogin extends HttpServlet {
     Connection connection;
     

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
        String url = request.getParameter("URL");
        String username = request.getParameter("user");
        String password = request.getParameter("password");
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.err.println("SQL Exception when starting connection to database:");
            System.err.println(e.getLocalizedMessage());
        }


    }



}
