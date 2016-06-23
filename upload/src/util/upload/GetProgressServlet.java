package util.upload;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * 编写记录:
 * 类说明:获取上传进度,上传路径,错误,上传结果等信息
 * 1.创建日期: 2016-04-13. 作者:姚飞
 *
 */
public class GetProgressServlet extends HttpServlet{

 private static final long serialVersionUID = -3596466520775012991L;

 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  doPost(request, response);
 }

 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	  request.setCharacterEncoding("utf-8");
	  response.setCharacterEncoding("utf-8");
	  UploadListener listener= null;
	  HttpSession session = request.getSession();
	  String error=(String) session.getAttribute("error");
	  String result= (String) session.getAttribute("result");
	  String fileName=(String) session.getAttribute("filename");
	  PrintWriter out = response.getWriter();
	  long bytesRead = 0,contentLength = 0; 
	  if (session != null)
		{
			
			listener = (UploadListener)session.getAttribute("LISTENER");
			
			if (listener == null)
			{
				return;
			}
			else
			{
				
		    	bytesRead = listener.getBytesRead();//上传的字节数
				contentLength = listener.getContentLength();//总字节数
				
			}
			//自己定义的返回格式
			String rp=bytesRead+","
					+contentLength+","
					+error+","
					+result+","
					+fileName;
		
			//System.out.println(rp);
			out.print(rp);
			/*		//返回json格式数据
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("bytesRead", bytesRead);
			map.put("contentLength", contentLength);
			map.put("error", error);
			map.put("result", result);
			map.put("fileName", fileName);
			Gson gson=new Gson();
			String json=gson.toJson(map);
			out.print(json);*/
			out.flush();
			out.close();
			
		}
 }



}