package util.upload;

import org.apache.commons.fileupload.ProgressListener;
/**
 * 
 * 编写记录:
 * 类说明:上传过程的监听器
 * 1.创建日期: 2016-04-13. 作者:姚飞
 *
 */
public class UploadListener implements ProgressListener{

	private volatile long 
	bytesRead = 0L,//上传的字节数
	contentLength = 0L,//总字节数
	item = 0L; 
	

	 public UploadListener() 
	    {
	    	super();
	    }
	 
	 @Override
	 public void update(long aBytesRead, long aContentLength, int anItem) {
	 	
	 	bytesRead = aBytesRead;
	     contentLength = aContentLength;
	     item = anItem;
	 }
	 public long getBytesRead() 
	 {
	     return bytesRead;
	 }
	 public long getContentLength() 
	 {
	     return contentLength;
	 }

	 public long getItem() 
	 {
	     return item;
	 }
	  

}
