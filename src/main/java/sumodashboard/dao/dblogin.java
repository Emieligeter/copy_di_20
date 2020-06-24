package sumodashboard.dao;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;


@Path("/loginServlet")
@WebServlet("/loginServlet")
public class dblogin extends HttpServlet {
     Connection connection;
@Context HttpServletRequest request;
    @POST
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

        PrintWriter writer = response.getWriter();

        String respond = "<html>";
        respond += "<head>";
        respond += "<meta http-equiv=\"Refresh\" content=\"0; url='http://localhost:63342/di20-1/src/main/webapp/dashboard.html'\" />";
        respond += "<head>";
        respond += "<body>";
        respond += "<p><a href=\"http://localhost:63342/di20-1/src/main/webapp/dashboard.html\">this link</a>.</p>";
        respond += "<body>";
        respond += "</html>";

        writer.println(respond);


    }



}
