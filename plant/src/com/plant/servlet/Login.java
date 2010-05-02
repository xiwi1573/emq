package com.plant.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.plant.service.PlantService;

public class Login extends HttpServlet {

	public Login() {
		super();
	}

	public void destroy() {
		super.destroy(); 
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		PlantService service = this.getPlanService();
		Map person = service.checkPurview(name, password);
		if (person.size()!=0) {
			response.getWriter().write("{success:true,msg:''}");
		} else {
			response.getWriter().write("{success:false,msg:''}");
		}
	}

	public void init() throws ServletException {

	}

	private PlantService getPlanService() {
		ApplicationContext cxt = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		return (PlantService) cxt.getBean("plantService");
	}

}
