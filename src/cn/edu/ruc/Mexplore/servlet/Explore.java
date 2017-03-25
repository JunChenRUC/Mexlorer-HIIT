package cn.edu.ruc.Mexplore.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.ruc.Mexplore.data.DataManager;
import cn.edu.ruc.Mexplore.domain.Result;
import cn.edu.ruc.Mexplore.model.GEM;
import cn.edu.ruc.Mexplore.utility.QueryParser;
import net.sf.json.JSONArray;

/**
 * Servlet implementation class Explore
 */
@WebServlet("/Explore")
public class Explore extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private static GEM gem;
	private static QueryParser queryParser;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Explore() {
        super();
        
		DataManager dataManager = new DataManager();
		gem = new GEM(dataManager);
		queryParser = new QueryParser(dataManager);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		request.setCharacterEncoding("UTF-8");
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "POST, GET");
        
		PrintWriter out= response.getWriter();

        long beginTime = System.currentTimeMillis();
        
        int queryType = 0;
        if(request.getParameter("type") != null)
        	queryType = Integer.parseInt(request.getParameter("type"));
        System.out.println("qeury type: " + queryType);
        String callback = request.getParameter("callback");
		
		if(queryType == 0){
			ArrayList<String> queryStringList = new ArrayList<>();
	        if(request.getParameterValues("query") != null){  
				String[] tokens = request.getParameterValues("query");
				for(String token : tokens){
					System.out.print(token + "\t");
					queryStringList.add(token.trim());
				}
	        }
	        
	        Result result = gem.getResult(queryParser.parseQueryEntity(queryStringList), queryParser.parseQueryFeature(queryStringList));
	        
			JSONArray jsonArray = JSONArray.fromObject(result);
			out.println("" + callback + "('" + jsonArray.toString().replace("'", "\\\\'") + "')");
		}
		
		System.out.println("\nTime cost: " + (System.currentTimeMillis() - beginTime)/1000 );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
