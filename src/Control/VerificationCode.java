package Control;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Ocean.Util.RandomCorlor;

/**
 * Servlet implementation class Verification
 */
@WebServlet("/verification")
public class VerificationCode extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerificationCode() {
        super();  
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 Font mFont = new Font("Times New Roman", Font.BOLD, 22);
			int width=140, height=30;   
	        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);   
	           
	        Graphics g = image.getGraphics();   
	        Random random = new Random();   
	        g.setColor(RandomCorlor.get(200,250));   
	        g.fillRect(1, 1, width-1, height-1);   
	        g.setColor(new Color(102,102,102));   
	        g.drawRect(0, 0, width-1, height-1);   
	        g.setFont(mFont);   
	        
	        g.setColor(RandomCorlor.get(160,200));   
	        
	        //画随机线   
	        for (int i=0;i<155;i++)   
	        {   
	            int x = random.nextInt(width - 1);   
	            int y = random.nextInt(height - 1);   
	            int xl = random.nextInt(6) + 1;   
	            int yl = random.nextInt(12) + 1;   
	            g.drawLine(x,y,x + xl,y + yl);   
	        }   
	  
	        //从另一方向画随机线   
	        for (int i = 0;i < 70;i++)   
	        {   
	            int x = random.nextInt(width - 1);   
	            int y = random.nextInt(height - 1);   
	            int xl = random.nextInt(12) + 1;   
	            int yl = random.nextInt(6) + 1;   
	            g.drawLine(x,y,x - xl,y - yl);   
	        }   
	  
	        //生成随机数,并将随机数字转换为字母   
	        String sRand="";   
	        for (int i=0;i<6;i++)   
		     {   
		         int itmp = random.nextInt(26) + 65;   
		         char ctmp = (char)itmp;   
		         sRand += String.valueOf(ctmp);   
		         g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));   
		         g.drawString(String.valueOf(ctmp),20*i+10,22);   
		     }   
	        g.dispose();
	        HttpSession session=request.getSession(true);
	        session.setAttribute("verificationCode", sRand); 
	        ImageIO.write(image,"JPEG",response.getOutputStream()); //输出图片
	
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
