package util.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

  
/**  
 * 时间监听器  
 *   
 *  
 */  
public class TempFileListener implements ServletContextListener {  
    private Timer timer;  
    private SystemTaskTest systemTask;  
    private static String every_time_run;  //每几秒钟运行一次
    static {  
        Properties prop = new Properties();  //读取配置文件
        InputStream inStrem = TempFileManager.class.getClassLoader()  
                .getResourceAsStream("tempfile.properties");  
        try {  
            prop.load(inStrem);  
            System.out.println(inStrem);
            every_time_run = prop.getProperty("every_time_run");  
  
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
  
    // 监听器初始方法  
    public void contextInitialized(ServletContextEvent sce) {  
  
        timer = new Timer();  
        systemTask = new SystemTaskTest(sce.getServletContext()  
                .getRealPath("/"), sce.getServletContext());  
        try {  
            //sce.getServletContext().log("定时器已启动");  
            System.out.println("定时器已启动");
            
            // 设置在每晚24:0分执行任务  
            // Calendar calendar = Calendar.getInstance();  
            // //calendar.set(Calendar.HOUR_OF_DAY, 24); // 24 ,可以更改时间  
            // calendar.set(Calendar.MINUTE, 28); // 0可以更改分数  
            // calendar.set(Calendar.SECOND, 0);// 0 //默认为0,不以秒计  
            // Date date = calendar.getTime();  
            // 监听器获取网站的根目录  
            
            Long time = Long.parseLong(every_time_run) * 1000;// 循环执行的时间  
            System.out.println("time" + time);  
            
            // 第一个参数是要运行的代码,第二个参数是指定时间执行，只执行一次  
            // timer.schedule(systemTask,time);  
            // 第一个参数是要运行的代码，第二个参数是从什么时候开始运行，第三个参数是每隔多久在运行一次。重复执行  
            timer.schedule(systemTask, 10000, time);  
            //sce.getServletContext().log("已经添加任务调度表"); 
            System.out.println("已经添加任务调度表");
        } catch (Exception e) {  
            e.printStackTrace();
        }  
    }  
  //监听器销毁方法
    public void contextDestroyed(ServletContextEvent sce) {  
        try {  
            timer.cancel();  
        } catch (Exception e) {  
        }  
    }  
}  
  
/**  
 * 时间任务器  
 *   
 *  
 */  
class SystemTaskTest extends TimerTask {  
    private ServletContext context;  
    private String path;  
    public SystemTaskTest(String path, ServletContext context) {  
        this.path = path;  
        this.context = context;  
    }  
  
    /**  
     * 把要定时执行的任务就在run中  
     */  
    @SuppressWarnings("deprecation")
    public void run() {   
        TempFileManager etf;     
        try {  
           
            System.out.println("开始执行任务!");
            // 需要执行的代码  
            System.out.println(new Date().toLocaleString());  

            etf = new TempFileManager(path);  
            etf.run();        
           
            System.out.println("指定任务执行完成!");
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}  