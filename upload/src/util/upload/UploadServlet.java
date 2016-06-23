package util.upload;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
/**
 * 编写记录:
 * 类说明:上传文件的servlet
 * 1.创建日期: 2016-04-13. 作者:姚飞
 *
 */
public class UploadServlet extends HttpServlet {
  
 private static final long serialVersionUID = -3100028422371321159L;
 private boolean isAllowed;
 private String upFileName;
	//定义合法后缀名的数组
 private String[] allowedExtName=new String[]
    		{"zip","rar",//压缩文件
		 "txt","doc","wps","docx","java",//文本
		 "xls","xlsx",//表格
		 "ppt","pptx",//幻灯片
		 "pdf",//pdf
		 "jpg","jpeg","bmp","gif","png"//图片
		 };
 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  doPost(request, response); 

 }
 
 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  //设置编码格式为utf-8
  request.setCharacterEncoding("utf-8");
  response.setCharacterEncoding("utf-8"); 
  //获取session，保存进度和上传结果，上传开始为nok,当为Ok表示上传完成
  HttpSession session=request.getSession();
  session.setAttribute("result", "nok");
  session.setAttribute("error", "");
  String error="";
  upFileName="";
  isAllowed=false;
  //给上传的文件设一个最大值，这里是不得超过100MB
  int maxSize=100*1024*1024;
  //创建工厂对象和文件上传对象
  DiskFileItemFactory factory=new DiskFileItemFactory();
  ServletFileUpload upload=new ServletFileUpload(factory);
  //创建上传监听器和设置监听器
  UploadListener listener=new UploadListener();
  session.setAttribute("LISTENER", listener);
  upload.setProgressListener(listener);

  
  //上传路径
  String path = request.getSession().getServletContext().getRealPath("/upload");
  String requestPath = request.getSession().getServletContext().getContextPath()+"/upload";
  File dirFile =new File(path);   
  //System.out.println(request.getSession().getServletContext().getContextPath());
  //如果文件夹不存在则创建    
  if  (!dirFile .exists()  && !dirFile .isDirectory())      
  {         
    dirFile .mkdir();    
  }    
  //根据日期创建文件夹,保存到对应日期的文件夹下
  Date date=new Date();
  SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
  String subDirName=sdf.format(date);
  File subDirFile=new File(path+"/"+subDirName);
  if  (!subDirFile .exists()  && !subDirFile .isDirectory())      
  {       
	  subDirFile .mkdir();    
  } 
 
  
  try {
	 
   //解析上传请求
   List<FileItem> items=upload.parseRequest(request);
   
   Iterator<FileItem> itr=items.iterator();
   
   while(itr.hasNext()){
	  
    FileItem item=(FileItem)itr.next();
    //判断是否为文件域
   
    if(!item.isFormField()){
    	
     if(item.getName()!=null&&!item.getName().equals("")){
      //获取上传文件大小和文件名称
      long upFileSize=item.getSize();   
      String fileName=item.getName();
     
      //获取文件后缀名
      String[] splitName=fileName.split("\\.");
      String extName=splitName[splitName.length-1];
      //检查文件后缀名
      for(String allowed:allowedExtName)
      {
      	if(allowed.equalsIgnoreCase(extName))
      	{
      		isAllowed=true;
      	}
      	
      }
      if(!isAllowed){
    	  error="上传文件格式不合法！";
    	  break;
      }
      if(upFileSize>maxSize){
       error="您上传的文件太大了，请选择不超过100MB的文件!";
       break;
      }
      
      
      //此时文件暂存在服务器的内存中，构造临时对象
      File tempFile=new File(makeFileName(fileName));
     
      //指定文件上传服务器的目录及文件名称
      File file=new File(path+"/"+subDirName+"/",tempFile.getName());
     
      item.write(file);//第一种写文件方法
      upFileName=requestPath+"/"+subDirName+"/"+tempFile.getName();
  if(upFileName.equals("")){
	  error="没选择上传文件！";
  }
  System.out.println(upFileName);
      /*//构造输入流读文件 第二种写文件方法
      InputStream is=item.getInputStream();
      int length=0;
      byte[] by=new byte[1024];
      
      FileOutputStream fos=new FileOutputStream(file);
      
      while((length=is.read(by))!=-1){
       fos.write(by, 0, length);
       //Thread.sleep(10);
      }
      fos.close();
      //Thread.sleep(1000);*/
     }else{
      error="没选择上传文件！";
     }
    }
   }
    
    
  } catch (Exception e) {
   e.printStackTrace();
   error="上传文件出现错误:"+e.getMessage();
  }
  if(!error.equals("")){ 
	  System.out.println(error);
   session.setAttribute("error", error);
  }else{  
	
   session.setAttribute("result", "OK"); 
   session.setAttribute("filename",upFileName);
  }
 }
 /**
  * 为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
  * @param filename 原文件名
  * @return 生成的唯一文件名
  */
 private String makeFileName(String filename){ 
     
     return UUID.randomUUID().toString() + "_" + filename;
   } 
}