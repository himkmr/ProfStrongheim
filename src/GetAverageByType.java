

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class History
 */
@WebServlet("/GetAverageByType")
public class GetAverageByType extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static String url = null;
	static Connection conn = null;
	Properties props = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAverageByType() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		url = "jdbc:oracle:thin:testuser/password@localhost";
		
		// properties for creating connection to Oracle database
		props = new Properties();
		props.setProperty("user", "testdb");
		props.setProperty("password", "password");

		
		// creating connection to Oracle database using JDBC
		try {
			conn = DriverManager.getConnection(url, props);
			if (conn == null)
				throw new SQLException();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String name_para = request.getParameter("ptype");
		String query = "Select grade from student_records where assign_type='"+name_para +"'";
		String grade_factor = "Select "+ name_para+" from weight_table";
		//System.out.println(grade_factor);
		PreparedStatement preStatement;
		ResultSet rst = null;
		PreparedStatement ps2 = null;
		ResultSet rst2 = null;
		String g_factor_s=null;
		try {
			ps2 = conn.prepareStatement(grade_factor);
		
		rst2 = ps2.executeQuery();
		rst2.next();
	
		g_factor_s= rst2.getString(name_para);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		double g_factor_d = Double.parseDouble(g_factor_s);
		String message ="<div align=\"center\"><table style=\"border:5px; solid black;\">";
		try {
			preStatement = conn.prepareStatement(query);
			rst = preStatement.executeQuery();
			int count =0;
			double total =0;
			while(rst.next())
			{

				total += Double.parseDouble(rst.getString("grade"))*g_factor_d/100;
				count++;
			}
			double average =total/count;
			
			message ="Average for "+name_para+" is "+average;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setContentType("text/html");
		// Actual logic goes here.
		request.setAttribute("message", message);
		response.setContentType("text/html");
		getServletContext().getRequestDispatcher("/output.jsp").forward(
				request, response);
		
		
		
	}

}
