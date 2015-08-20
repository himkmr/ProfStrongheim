

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
//	private String message = "";
	static String url = null;
	static Connection conn = null;
	Properties props = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */

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
		String name = request.getParameter("name");
		String id = request.getParameter("id");
		String assignment = request.getParameter("assignment");
		String type = request.getParameter("type");
		String date = request.getParameter("date");
		String grade = request.getParameter("grade");
		
		String query = "insert into student_records values("+id+",'"+name+"','"+assignment+"','"+type+"',"+"To_Date('"+date+"','mm/dd/yy')"+","+grade+")";
		System.out.println(query);
		PreparedStatement preStatement;
		try {
			preStatement = conn.prepareStatement(query);
			preStatement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response.setContentType("text/html");
		getServletContext().getRequestDispatcher("/success.jsp").forward(
				request, response);
		
		
		
		
	}

}
