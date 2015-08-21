
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
@WebServlet("/Students")
public class Students extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static String url = null;
	static Connection conn = null;
	Properties props = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Students() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

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
		String query = "Select distinct name from student_records";
		//System.out.println(query);
		PreparedStatement preStatement;
		ResultSet rst = null;
		String message = "<div align=\"center\"><table style=\"border:5px; solid black;\">";
		try {
			preStatement = conn.prepareStatement(query);
			rst = preStatement.executeQuery();
			
			
			//get student records for each class
			while (rst.next()) {
				String name = rst.getString("name");
				message += "<tr><td style=\"padding:15px;border:2px solid green;background-color: #D8D8D8; \">"
						+ "<a href =\"GetByName?pname=" +name +"\">"
						+ name + "</tr><td></td><td></td>";
				

				message += "<b><tr><td style=\"padding:15px; border: 2px solid green; background-color: #F2F2F2; \">Class"+
						"</td>";
				message += "<td style=\"padding:15px; border: 2px solid green; background-color: #F2F2F2; \">Assignment Name"+
						"</td>";
				message += "<td style=\"padding:15px; border: 2px solid green; background-color: #F2F2F2; \">Assignment Type"+
						"</td>";
				message += "<td style=\"padding:15px; border: 2px solid green; background-color: #F2F2F2; \">Date"+
						"</td>";
				message += "<td style=\"padding:15px; border: 2px solid green; background-color: #F2F2F2; \">Grade"+
						"</td></tr></b>";
				
				
				
				String query_student  = "select distinct class from student_records where name="+"'"+name+"'";
				PreparedStatement ps2 = conn.prepareStatement(query_student);
				ResultSet rst2 = ps2.executeQuery();
				while(rst2!=null && rst2.next())
				{
					String cls = rst2.getString("class");
					String query_assigns  = "select * from student_records where name="+"'"+name+"' and class='"+cls+"'";
					System.out.println(query_assigns);
					PreparedStatement ps3 = conn.prepareStatement(query_assigns);
					ResultSet rst3 = ps3.executeQuery();
								
					
					while(rst3!=null && rst3.next())
					{
							String ass_name = rst3.getString("assign_name");
							String grade = rst3.getString("grade");
							String class_name = rst3.getString("class");
							String dt = rst3.getString("dt");
							String ass_tp = rst3.getString("assign_type");
							
							
							
							message += "<tr><td style=\"padding:15px; border: 2px solid green; background-color: ##F2F2F2; \">"
									+class_name+ "</td>";
							message += "<td style=\"padding:15px; border: 2px solid green; background-color: ##F2F2F2; \">"
									+ass_name+ "</td>";
							message += "<td style=\"padding:15px; border: 2px solid green; background-color: ##F2F2F2; \">"
									+ass_tp+ "</td>";
							message += "<td style=\"padding:15px; border: 2px solid green; background-color: ##F2F2F2; \">"
									+dt+ "</td>";
							message += "<td style=\"padding:15px; border: 2px solid green; background-color: ##F2F2F2; \">"
									+grade+ "</td></tr>";
					}
													
				}
				
					
				
			}

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
