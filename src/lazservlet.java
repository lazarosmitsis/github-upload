import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/lazservlet")
public class lazservlet extends HttpServlet {

    static String PAGE_HEADER = "<html><head><title>helloworld</title></head><body>";
    static String PAGE_FOOTER = "</body></html>";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,ServletException {
    	Connection connection = null;
    	Statement stmt = null;
    	
    	resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.println(PAGE_HEADER);

        InitialContext cxt=null;
		try {
			cxt = new InitialContext();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        if ( cxt == null ) {
        	writer.println("Uh oh -- no context!");
        }                
        DataSource ds=null;
		try {
			ds = (DataSource) cxt.lookup( "java:/comp/env/jdbc/ingres" ); 

		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        if ( ds == null ) {
        	writer.println("Data source not found!");
        } else {
        	writer.println("Data source ok!");
        }
        writer.println("Data source ok!");        
        try  {
        	writer.println("Trying to get connection..");        	
        	connection = ds.getConnection();
        	
// To deploy on centos7 machine we only have to interchange Resource entry in context.xml
// There is no need to change anything in web.xml 

        	
        	
// Alternative way is NOT to use a connection pool:
// In that case we do not need the entries in context.xml, web.xml related to Ingres
/*
        try {
        	Class.forName("com.ingres.jdbc.IngresDriver");
            System.out.println("Registered IngresDriver");

//            ServletContext sc = getServletContext();
            String serverip = "192.168.1.7";   //sc.getInitParameter("servername");
            
            System.out.println("SERVER="+serverip);
            System.out.println(System.getProperty("os.name"));
            
                        
            if (System.getProperty("os.name").startsWith("Windows"))
            	connection = DriverManager.getConnection("jdbc:ingres://"+serverip+":II7/clinic;char_encode=UTF8","ingres","ingres");
            else
            	connection = DriverManager.getConnection("jdbc:ingres://localhost:II7/clinic;char_encode=UTF8","ingres","ingres");
             	
        	if ( connection == null ) {
        	        	writer.println("Connection is null!");
        	        	return;
        	} else {
        	        	writer.println("Connection ok!");
        	}    
*/        	
          //STEP 4: Execute a query
        	writer.println("Creating statement...");
          stmt = connection.createStatement();
          writer.println("Created statement.");
          String sql;
          sql = "SELECT aadeaa, fcomp, afm FROM aade";
          ResultSet rs = stmt.executeQuery(sql);
          writer.println("Executed query.");

          //STEP 5: Extract data from result set
          while(rs.next()){
             //Retrieve by column name
             long aadeaa  = rs.getLong("aadeaa");
    		 String fcomp = rs.getString("fcomp");
             String afm = rs.getString("afm");

             //Display values
             writer.println("aadeaa: " + aadeaa);
             writer.println(", fcomp: " + fcomp);
             writer.println(", afm: " + afm);
          }
          //STEP 6: Clean-up environment
          rs.close();
          stmt.close();
          connection.close();
          
        } catch (Exception e) {
        	e.printStackTrace();
        }
               
        writer.println("OK");
        writer.println(PAGE_FOOTER);
        writer.close();
    }

}