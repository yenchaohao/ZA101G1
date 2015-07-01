package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class loginFilter implements Filter{
	
	private FilterConfig config;
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		config = null;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse respone,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) respone;
		
		HttpSession session = req.getSession();
		
		Object empid = session.getAttribute("empid");
		if(empid == null){
			session.setAttribute("location", req.getContextPath()+"/back/index/index.jsp");
			res.sendRedirect(req.getContextPath()+"/back/emp/loginEmp.jsp");
			return;
		} else {
			chain.doFilter(req, res);
		}
		
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub
		this.config = config;
	}

}
