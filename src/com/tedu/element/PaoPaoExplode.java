package com.tedu.element;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

/**
 * 泡泡爆炸类
 * @author 阿灿
 *
 */
public class PaoPaoExplode extends ElementObj{
	private int power;
	private int playerNum;
	private int upstep;		//记录向各个方向爆炸可扩展的的格数
	private int downstep;
	private int leftstep;
	private int rightstep;
	
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
					getX(), getY()-48*upstep, getX()+48  , getY(),
					891, 0, 1014, 88+100*(upstep-1),
					null);
			g.drawImage(imgDown.getImage(),
					getX(), getY()+48, getX()+48, getY()+48+48*downstep,
					891,  1331-100*(downstep-1), 1022,1411,
					null);
			g.drawImage(imgLeft.getImage(),
					getX()-48*leftstep, getY(), getX(), getY()+48,
					6, 603, 124+100*(leftstep-1), 732,
					null);
			g.drawImage(imgRight.getImage(),
					getX()+48, getY(), getX()+48+48*rightstep, getY()+48,
					1760-100*(rightstep-1), 609, 1880, 731,
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
			upstep=power;	
			downstep=power;
			leftstep=power;
			rightstep=power;
			setstep();  //计算爆炸步数
			setstep2();  //再次计算爆炸步数
			return this;
		}
		
	private void setstep() {
		List<ElementObj> list = ElementManager.getManager().getElementsByKey(GameElement.MAPS);
		for (int j = power - 1; j >= 0; j--) {
			{ 
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);   //取出所有地图类
					if (map.getType() == 2) { // 取出不可摧毁地图
						Rectangle step = new Rectangle(getX(), getY() - 48 * (j + 1), 48, 48);// 计算上方爆炸步数
						if (step.intersects(map.getRectangle())) {
							upstep=j;
							break;
						}
					}

				}
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);   //取出所有地图类
					if (map.getType() == 2) { // 取出不可摧毁地图
						Rectangle step = new Rectangle(getX(), getY() + 48 * (j + 1), 48, 48);// 计算下方爆炸步数
						if (step.intersects(map.getRectangle())) {
							downstep=j;
							break;
						}
					}

				}
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);   //取出所有地图类
					if (map.getType() == 2) { // 取出不可摧毁地图
						Rectangle step = new Rectangle(getX() - 48 * (j + 1), getY(), 48, 48);// 计算左方爆炸步数
						if (step.intersects(map.getRectangle())) {
							leftstep=j;
							break;
						}
					}

				}
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);   //取出所有地图类
					if (map.getType() == 2) { // 取出不可摧毁地图
						Rectangle step = new Rectangle(getX() + 48 * (j + 1), getY(), 48, 48);// 计算右方爆炸步数
						if (step.intersects(map.getRectangle())) {
							rightstep=j;
							break;
						}
					}

				}

			}

		}

	}
	private void setstep2() {
		List<ElementObj> list = ElementManager.getManager().getElementsByKey(GameElement.MAPS);
		for (int j = 0; j < upstep; j++) {
			int flat = 1;
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i); // 取出所有地图类
				if (map.getType() == 1) { // 取出可摧毁地图
					Rectangle step = new Rectangle(getX(), getY() - 48 * (j + 1), 48, 48);// 计算上方爆炸步数
					if (step.intersects(map.getRectangle())) {
						upstep = j + 1;
						flat = 0;
						break;
					}
				}

			}
			if (flat != 1) {
				break;
			}

		}
		for (int j = 0; j < downstep; j++) {
			int flat = 1;
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i); // 取出所有地图类
				if (map.getType() == 1) { // 取出可摧毁地图
					Rectangle step = new Rectangle(getX(), getY() + 48 * (j + 1), 48, 48);// 计算下方爆炸步数
					if (step.intersects(map.getRectangle())) {
						downstep = j + 1;
						flat = 0;
						break;
					}
				}

			}
			if (flat != 1) {
				break;
			}

		}
		for (int j = 0; j < leftstep; j++) {
			int flat = 1;
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i); // 取出所有地图类
				if (map.getType() == 1) { // 取出可摧毁地图
					Rectangle step = new Rectangle(getX() - 48 * (j + 1), getY(), 48, 48);// 计算左方爆炸步数
					if (step.intersects(map.getRectangle())) {
						leftstep = j + 1;
						flat = 0;
						break;
					}
				}

			}
			if (flat != 1) {
				break;
			}

		}
		for (int j = 0; j < rightstep; j++) {
			int flat = 1;
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i); // 取出所有地图类
				if (map.getType() == 1) { // 取出可摧毁地图
					Rectangle step = new Rectangle(getX() + 48 * (j + 1), getY(), 48, 48);// 计算右方爆炸步数
					if (step.intersects(map.getRectangle())) {
						rightstep = j + 1;
						flat = 0;
						break;
					}
				}

			}
			if (flat != 1) {
				break;
			}

		}

}

		

	
			//使用计时器，0.5秒改变Alive状态
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
					if (map.getType() == 0||map.getType() == 2) { // 地板和不可破坏区域不碰撞
						return false;
					}
					if (map.getType() == 1) { // 碰撞可破坏区域
						Rectangle explodeColumn = new Rectangle(getX(), getY() - 48 * upstep, 48, 48 * (upstep+downstep + 1));// 水泡爆炸十字纵向
						Rectangle explodeRow = new Rectangle(getX() - 48 * leftstep, getY(), 48 * (leftstep+rightstep + 1), 48);// 水泡爆炸十字横向
						boolean column = explodeColumn.intersects(obj.getRectangle());
						boolean row = explodeRow.intersects(obj.getRectangle());
						return (column || row);
					}
				
					
				}
				
				// if (obj instanceof Player) { 48*(3+power)
				Rectangle explodeColumn = new Rectangle(getX(), getY() - 48 * upstep, 48, 48 * (upstep+downstep + 1));// 水泡爆炸十字纵向
				Rectangle explodeRow = new Rectangle(getX() - 48 * leftstep, getY(), 48 * (leftstep+rightstep + 1), 48);// 水泡爆炸十字横向
				boolean column = explodeColumn.intersects(obj.getRectangle());
				boolean row = explodeRow.intersects(obj.getRectangle());
				return (column || row);
				// }
				
			
				
				
			}

}
