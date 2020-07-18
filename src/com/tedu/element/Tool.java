package com.tedu.element;

import java.awt.Graphics;
import java.util.List;

import javax.swing.ImageIcon;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

public class Tool extends ElementObj{
	private int num=0;					//图片的编号
	private long imgtime=0;				//图片切换时间
	private String name;				//道具名
//	空的构造函数
	public Tool() {}
//	画图
	@Override
	public void showElement(Graphics g) {
		if(this.name.equals("GoldenCard"))
		{
			g.drawImage(this.getIcon().getImage(),
					this.getX(), this.getY(),
					this.getX()+28, this.getY()+49, 
					4+(num*32), 0, 
					32+(num*32), 49, null);
		}
		else if (this.name.equals("BubbleCopy")) {
			g.drawImage(this.getIcon().getImage(),
					this.getX(), this.getY(),
					this.getX()+32, this.getY()+48, 
					0+(num*32), 0, 
					32+(num*32), 48, null);
		}
		else
		{
			return ;
		}
	}

	/**
	 * @说明 str以json格式传入，
	 * 格式为x:+数字,y:+数字,n:+随机数
	 */
	@Override
	public ElementObj createElement(String str) {
		String[] split=str.split(",");
		for(String str1:split)
		{
			String[] split2=str1.split(":");
			switch (split2[0]) {
				case "x":this.setX(Integer.parseInt(split2[1]));break;
				case "y":this.setY(Integer.parseInt(split2[1]));break;
//				通过随机数设置是否掉落道具以及掉落道具的种类
				case "n":
					int n=Integer.parseInt(split2[1]);
					if(n>0&&n<=20){//1-20掉落泡泡
						this.name="BubbleCopy";
					}
					else if(n>20&&n<=40){//21-40掉落金卡
						this.name="GoldenCard";
					}
					else
					{
						this.setName(null);
						return null;
					}
					String url="image/Characters/"+this.name+".png";
					ImageIcon icon=new ImageIcon(url);				//通过道具名字设置道具路径
					this.setIcon(icon);break;
				default:break;
			}
		}
		this.setW(48);
		this.setH(48);
		return this;
	}
//	换装
	@Override
	protected void updateImage(long gameTime) {
		if(gameTime-imgtime>2)
		{
			imgtime = gameTime;
			num++;
			if(num>3)
			{
				num=0;
			}
		}
	}
	
//	设置道具名字的set和get方法	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
//	道具的死亡效果
	@Override
	public void die() {
		if(!this.isLive())
		{
			List<ElementObj> playerList=ElementManager.getManager().getElementsByKey(GameElement.PLAYER);
			for(ElementObj player:playerList)
			{
				if(player.collide(this))
				{
					if(this.name.equals("BubbleCopy"))
					{
						player.setHP(player.getHP()+1);
					}
					else if(this.name.equals("GoldenCard"))
					{
						player.setBubbleNum(player.getBubbleNum()+1);
					}
				}
			}
			
		}
	}
	
	@Override
	public boolean collide(ElementObj obj) {
		if(this.getRectangle().intersects(obj.getRectangle()))
		{
			this.setLive(false);
		}
		return super.collide(obj);
	}
}
