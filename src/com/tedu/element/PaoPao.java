package com.tedu.element;

import java.awt.Graphics;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

public class PaoPao extends ElementObj{

	private int imgx = 0;
	private int imgy=0;
	private long imgtime = 0;
	private int power;
	private int playerNum;
	
	public PaoPao() {}
	//对创建这个对象的过程进行封装，外界值只需要传输必要的约定参数，返回值就是实体对象
	@Override
	public ElementObj createElement(String str) {//定义字符串的规则
		String[] split=str.split(",");
		for(String str1 : split) {
			String[] split2= str1.split(":");//0 x
			switch (split2[0]) {
			case "x": this.setX(Integer.parseInt(split2[1]));break;
			case "y": this.setY(Integer.parseInt(split2[1]));break;
			case "playerNum": playerNum=Integer.parseInt(split2[1]);
								break;
			case "power": power=Integer.parseInt(split2[1]);break;
		
			}
		}
		ImageIcon icon = GameLoad.imgMap.get("paopao");
		this.setIcon(icon);
		this.setW(48);
		this.setH(48);
		return this;
	}



	@Override
	public void showElement(Graphics g) {
		// 绘画图片
	
		//做图片的分割
				g.drawImage(this.getIcon().getImage(), 
						this.getX()+8, this.getY(), 
						this.getX()+40, this.getY()+45, 
						0+(imgx*32), 0, 
						32+(imgx*32), 45, 
						null);
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
		timer.schedule(task, 2500);
	}
	/**
	 * 对于子弹来说 1.出边界 2.碰撞 3.玩家放保险
	 * 处理方式就是，当达到死亡的条件时，只进行修改死亡状态的操作
	 */
	@Override
	public void die(long gameTime) { //死亡也是一个对象
		if(!isLive()) {	
	
			
			ElementObj element=new PaoPaoExplode().createElement(this.toString());
			//装入到集合中
			ElementManager.getManager().addElement(element,GameElement.EXPLODE);
			GameLoad.playMusic("bomb");
			//改变炸弹玩家已经放在炸弹数bubbleNum,显示爆炸效果，加入ExplodeBubble
			List<ElementObj> list2 = ElementManager.getManager().getElementsByKey(GameElement.PLAYER);
			for (int i = 0; i < list2.size(); i++) {
				Player player = (Player) list2.get(i);
				
				if (player.getPlayerNum()==this.playerNum) {
					player.setBubbleNum(player.getBubbleNum()+1);
				}
			}
			
		
			
		}
	}
	@Override
	protected void updateImage(long time) {
		if (time - imgtime > 20) {
			imgtime = time;
			imgx++;
			if(imgx > 3) {
				imgx=0;
			}
		}
	}
	
	@Override
	public String toString() {  //这里是偷懒。直接使用toString；建议自己定义一个方法
	//{x:3,y:5}json格式
		int x=this.getX();
		int y=this.getY();

	// TODO Auto-generated method stub
		return "x:"+x+",y:"+y+",playerNum:"+playerNum+",power:"+power;
	}
	@Override
	public int compareTo(ElementObj o) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
}
