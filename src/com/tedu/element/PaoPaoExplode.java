package com.tedu.element;

import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import com.tedu.manager.GameLoad;

/**
 * 泡泡爆炸类
 * @author 阿灿
 *
 */
public class PaoPaoExplode extends ElementObj{
	//爆炸四个方向及中心的图片
			private static ImageIcon imgCenter = GameLoad.imgMap.get("ppexplode");
			private static ImageIcon imgUp = GameLoad.imgMap.get("ppexplode");
			private static ImageIcon imgDown = GameLoad.imgMap.get("ppexplode");
			private static ImageIcon imgLeft = GameLoad.imgMap.get("ppexplode");
			private static ImageIcon imgRight = GameLoad.imgMap.get("ppexplode");

		@Override
		public void showElement(Graphics g) {
	
			g.drawImage(imgCenter.getImage(),
					getX(), getY(), getX()+48, getY()+48,
					912, 623, 1000, 712,
					null);
			g.drawImage(imgUp.getImage(),
					getX(), getY()-48*getPower(), getX()+48  , getY(),
					891, 0, 1014, 88+100*(getPower()-1),
					null);
			g.drawImage(imgDown.getImage(),
					getX(), getY()+48, getX()+48, getY()+48+48*getPower(),
					891,  1331-100*(getPower()-1), 1022,1411,
					null);
			g.drawImage(imgLeft.getImage(),
					getX()-48*getPower(), getY(), getX(), getY()+48,
					6, 603, 124+100*(getPower()-1), 732,
					null);
			g.drawImage(imgRight.getImage(),
					getX()+48, getY(), getX()+48+48*getPower(), getY()+48,
					1760-100*(getPower()-1), 609, 1880, 731,
					null);
		
		}
		
		
		@Override
		public ElementObj createElement(String str) {
			String[] split=str.split(",");
			for(String str1 : split) {
				String[] split2= str1.split(":");//0 x
				switch (split2[0]) {
				case "x": this.setX(Integer.parseInt(split2[1]));break;
				case "y": this.setY(Integer.parseInt(split2[1]));break;
				case "playerNum": this.setPlayerNum(Integer.parseInt(split2[1]));break;
				case "power": this.setPower(Integer.parseInt(split2[1]));break;
				}
			}
			ImageIcon icon = GameLoad.imgMap.get("ppexplode");
			this.setIcon(icon);
			this.setW(icon.getIconWidth());
			this.setH(icon.getIconHeight());
			return this;
		}
		
		//使用计时器，2.5秒改变Alive状态
			@Override
			public void move() {
				Timer timer = new Timer(true);
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						setLive(false);
			
					}
				};
				timer.schedule(task, 500);
			}


}
