package util.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
  
  
/**  
 * 删除服务器上的文件  
 * 
 */  
 
public class TempFileManager implements Runnable {  
    private String path;//路径  
  
    private static String RETENTION_TIME = "1440";// 默认值 文件保存的时间  一天单位分
    static {  
        Properties prop = new Properties();  
        InputStream inStrem = TempFileManager.class.getClassLoader()  
                .getResourceAsStream("execl.properties");  
        try {  
            prop.load(inStrem);  
            RETENTION_TIME = prop.getProperty("file_retention_time"); //从配置文件读取文件保留时间 
  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                inStrem.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    /**  
     * 构造函数。初始化参数  
     * @param path  要管理的路径
     */  
    public TempFileManager(String path) {  
        this.path = path;  
       
    }  
    /**  
     * 把线程要执行的代码放在run()中  
     */  
    public void run() {  
        System.out.println("文件管理开始=========");  
        path = path + "upload";  
        System.out.println("文件管理路径===" + path);  
        File file = new File(path);  
        deletefiles(file);  
    }  
  
    /**  
     * 批量删除文件  
     *   
     * @param folder  批量删除的文件夹对象
     */  
    public void deletefiles(File folder) { 
	if(folder.isDirectory()){
	    
        File[] files = folder.listFiles();
        if(files.length<=0){
            if(!folder.getAbsolutePath().equalsIgnoreCase(path)){
        	if(canDeleteFile(folder)){
        	    if (folder.delete()) {  
                    System.out.println("文件夹" + folder.getName() + "删除成功!");  
        	    } else {  
                    System.out.println("文件夹" + folder.getName()  
                            + "删除失败!此文件夹内的文件可能正在被使用");  
        	    }  
        	}
            }
        }
        for (int i = 0; i < files.length; i++) {  
            
            if(files[i].isDirectory())
        	{
        	deletefiles(files[i]);
        	}else{
        	    deleteFile(files[i]); 
        	}
            
        }  
	}
    }  
  
    /**  
     * 删除文件  
     *   
     * @param file  要删除的文件对象
     */  
    private void deleteFile(File file) {  
        try {  
            if (file.isFile()) {  
                // 删除符合条件的文件  
                if (canDeleteFile(file)) {  
                    if (file.delete()) {  
                        System.out.println("文件" + file.getName() + "删除成功!");  
                    } else {  
                        System.out.println("文件" + file.getName()  
                                + "删除失败!此文件可能正在被使用");  
                    }  
                } else {  
  
                }  
            } else {  
                System.out.println("没有可以删除的文件了");  
            }  
  
        } catch (Exception e) {  
            System.out.println("删除文件失败========");  
            e.printStackTrace();  
        }  
    }  
  
    /**  
     * 判断文件是否能够被删除  
     * @param file 要判断的文件对象
     */  
    private boolean canDeleteFile(File file) {  
        Date fileDate = getfileDate(file);  
        Date date = new Date();  
        long time = (date.getTime() - fileDate.getTime()) / 1000 / 60  
                - Integer.parseInt(RETENTION_TIME);// 当前时间与文件间隔的分钟  
//        System.out.println("time=="+time);
        if (time > 0) {  
            return true;  
        } else {  
            return false;  
        }  
  
    }  
  
    /**  
     * 获取文件最后的修改时间  
     *   
     * @param file  要获取修改时间的文件对象
     * @return  修改时间
     */  
    private Date getfileDate(File file) {  
        long modifiedTime = file.lastModified();  
        Date d = new Date(modifiedTime);  
        return d;  
    }  

}  