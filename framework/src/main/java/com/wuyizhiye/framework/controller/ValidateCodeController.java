package com.wuyizhiye.framework.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.framework.base.controller.BaseController;

@Controller
@RequestMapping(value="captcha/*")
public class ValidateCodeController extends BaseController {

	    public static final String RANDOMCODEKEY = "RANDOMVALIDATECODEKEY";//放到session中的key
	    private Random random = new Random();
	    private String randString = "0123456789";//随机产生的字符串
	    
	    private int width = 80;//图片宽
	    private int height = 26;//图片高
	    private int lineSize = 40;//干扰线数量
	    private int stringNum = 4;//随机产生字符数量
	    /*
	     * 获得字体
	     */
	    private Font getFont(){
	        return new Font("Fixedsys",Font.CENTER_BASELINE,20);
	    }
	    /*
	     * 获得颜色
	     */
	    private Color getRandColor(int fc,int bc){
	        if(fc > 255)
	            fc = 255;
	        if(bc > 255)
	            bc = 255;
	        int r = fc + random.nextInt(bc-fc-16);
	        int g = fc + random.nextInt(bc-fc-14);
	        int b = fc + random.nextInt(bc-fc-18);
	        return new Color(r,g,b);
	    }
	    /**
	     * 生成随机图片
	     * @throws UnsupportedEncodingException 
	     */
	    @RequestMapping(value="getcode")
	    public void getRandcode(HttpServletResponse response) throws UnsupportedEncodingException {
	        
	        //BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
	        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_BGR);
	        Graphics g = image.getGraphics();//产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
	        g.fillRect(0, 0, width, height);
	        g.setFont(new Font("Times New Roman",Font.ROMAN_BASELINE,18));
	        g.setColor(getRandColor(110, 133));
	        //绘制干扰线
	        for(int i=0;i<=lineSize;i++){
	            drowLine(g);
	        }
	        //绘制随机字符
	        String randomString = "";
	        for(int i=1;i<=stringNum;i++){
	            randomString=drowString(g,randomString,i);
	        }
	        //加密一下
	        setCookie(response,RANDOMCODEKEY,randomString,-1);
	        g.dispose();
	        try {
	            ImageIO.write(image, "JPEG", response.getOutputStream());//将内存中的图片通过流动形式输出到客户端
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    /*
	     * 绘制字符串
	     */
	    private String drowString(Graphics g,String randomString,int i){
	        g.setFont(getFont());
	        g.setColor(new Color(random.nextInt(101),random.nextInt(111),random.nextInt(121)));
	        String rand = String.valueOf(getRandomString(random.nextInt(randString.length())));
	        randomString +=rand;
	        g.translate(random.nextInt(3), random.nextInt(3));
	        g.drawString(rand, 13*i, 20);
	        return randomString;
	    }
	    /*
	     * 绘制干扰线
	     */
	    private void drowLine(Graphics g){
	        int x = random.nextInt(width);
	        int y = random.nextInt(height);
	        int xl = random.nextInt(13);
	        int yl = random.nextInt(15);
	        g.drawLine(x, y, x+xl, y+yl);
	    }
	    /*
	     * 获取随机的字符
	     */
	    public String getRandomString(int num){
	        return String.valueOf(randString.charAt(num));
	    }
	
	    @RequestMapping(value="downFile")
		public void downFile(HttpServletResponse response) throws IOException{
			
			
				File file = new File("Z:/images/112.docx");
				// 取得文件名。
				String filename = "112.docx";		
				// 设置response的Header
				response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("gb2312"),"iso8859-1"));
				response.addHeader("Content-Length", "" + file.length());
				response.addHeader("Content-Type","application/msword");
				   FileInputStream fis=new FileInputStream(file);
			        BufferedInputStream buff=new BufferedInputStream(fis);

			        byte [] b=new byte[1024];//相当于我们的缓存

			        long k=0;//该值用于计算当前实际下载了多少字节

			        //从response对象中得到输出流,准备下载

			        OutputStream myout=response.getOutputStream();

			        //开始循环下载

			        while(k<file.length()){

			            int j=buff.read(b,0,1024);
			            k+=j;

			            //将b中的数据写到客户端的内存
			            myout.write(b,0,j);

			        }

			        //将写入到客户端的内存的数据,刷新到磁盘
			        myout.flush();

			

		}
	    
	    void setCookie(HttpServletResponse response,String cookname,String cookvalue,int maxage){
	    	Cookie cookie = new Cookie(cookname,cookvalue);
			cookie.setPath("/");
			cookie.setMaxAge(maxage);//仅保留30分钟
			response.addCookie(cookie);
	    }
}
