package com.tedu.element;

import java.awt.Graphics;
import java.awt.Rectangle;
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
	private int power;
	private int playerNum;
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
					getX(), getY()-48*power, getX()+48  , getY(),
					891, 0, 1014, 88+100*(power-1),
					null);
			g.drawImage(imgDown.getImage(),
					getX(), getY()+48, getX()+48, getY()+48+48*power,
					891,  1331-100*(power-1), 1022,1411,
					null);
			g.drawImage(imgLeft.getImage(),
					getX()-48*power, getY(), getX(), getY()+48,
					6, 603, 124+100*(power-1), 732,
					null);
			g.drawImage(imgRight.getImage(),
					getX()+48, getY(), getX()+48+48*power, getY()+48,
					1760-100*(power-1), 609, 1880, 731,
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
				case "playerNum": playerNum=Integer.parseInt(split2[1]);break;
				case "power": power=Integer.parseInt(split2[1]);break;
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


			@Override
			public int compareTo(ElementObj o) {
				// TODO Auto-generated method stub
				return 0;
			}

			/**
			 * 重写父类的碰撞方法。
			 */
			@Override
			public boolean collide(ElementObj obj) {
				if (obj instanceof Map) { 
					Map map = (Map) obj;
					if (map.getType() == 0) { // 地板不碰撞
						return false;
					}
				}
				
				//if (obj instanceof Player) { 
					Rectangle explodeColumn = 
							new Rectangle(getX(), getY()-48*power, 48, 48*(3+power));//水泡爆炸十字纵向
					Rectangle explodeRow =  
							new Rectangle(getX()-48*power, getY(), 48*(3+power),48);//水泡爆炸十字横向
					boolean column = explodeColumn.intersects(obj.getRectangle());
					boolean row = explodeRow.intersects(obj.getRectangle());
					return (column||row);
				//}
				
			
				
				
			}

}
