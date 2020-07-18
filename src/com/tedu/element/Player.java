package com.tedu.element;

import java.awt.Graphics;

import javax.swing.ImageIcon;

import com.tedu.game.GameStart;
import com.tedu.manager.GameLoad;

/**
 * 玩家角色类
 * @author passerbyYSQ
 * @create 2020年7月18日 上午10:46:32
 */
public class Player extends ElementObj {
	
	/**
	 * 4个方向状态的标识
	 * 索引：方向
	 * 0：左		1：上	2：右	3：下
	 */
	private boolean[] dirFlag = new boolean[] {false, false, false, false}; 
	
	/**
	 * 控制该Player的所有按键
	 * 索引：按键功能
	 * 0：向左	1：向上	2：向右	3：向下	4：放泡泡
	 */
	private int[] keys = new int[5];
	
	// Player当前面向的方向，默认为向上
	private int curDir = 1;
	// Player移动速度
	private int moveSpeed = 6;
	/**
	 * 血量
	 * 为了方便扩展，此处不定义有多少条命，而是直接定义血量
	 * 一条命的血量值为100，捡到金卡，直接增加100血量（相当于增加一条命）
	 */
	private int hp = 100;
	
	/**
	 * 用于图片的切割。
	 * imgX：在x方向的偏移量
	 * imgY：在y方向的偏移量
	 * 计算真正的偏移量，还需要乘以间距值
	 */
	private int imgX = 0;
	private int imgY = 0; // 0下 1左 2右 3上
	
	// 上一次的换装时间。用于控制Player的换装速度
	private long imgTime; 
	
	// 攻击状态。true：攻击；false：停止
	private boolean pkType = false;

	/**
	 * Player的工厂方法
	 * 由GameLaod的loadPlayer方法调用
	 */
	@Override
	public ElementObj createElement(String str) {
		//System.out.println(str);
		
		String[] split = str.split(",");
		this.setX(Integer.parseInt(split[0]));
		this.setY(Integer.parseInt(split[1]));
		ImageIcon icon = GameLoad.imgMap.get(split[2]);
		this.setIcon(icon);
		
		// 注意这里设置的是Player显示的宽高，而并非素材图片的宽高
		// Player的图片素材是一张需要切割的图片
		this.setW(icon.getIconWidth());
		this.setH(icon.getIconHeight());
		
		for (int i = 3; i < split.length; i++) {
			keys[i - 3] = Integer.parseInt(split[i]);
		}
		
		// 基类中返回this
		return super.createElement(str);
	}
	
	@Override
	public void showElement(Graphics g) {
//		g.drawImage(this.getIcon().getImage(), 
//						this.getX(), this.getY(), 
//						this.getW(), this.getH(), null);
		
		// 图片分割
		g.drawImage(this.getIcon().getImage(), 
				// Player在面板中显示的区域。一个Player占据一个格子的大小（48*48）
				this.getX(), this.getY(),  // 左上角坐标
				this.getX() + 48, this.getY() + 48,   // 右下角坐标
				// Player在图片素材中的位置区域
				24 + (imgX*100), 34 + (imgY*100),  // 左上角坐标
				74 + (imgX*100), 100 + (imgY*100),  // 右下角坐标
				null);
	}
	
	/**
	 * Player的换装动画
	 */
	@Override
	protected void updateImage(long gameTime) {
		// 如果当前时间和上次换装的时间的间隔大于3，才换一次装
		if (gameTime - imgTime > 3) {
			// 更新上一次的换装时间
			imgTime = gameTime;
			// 通过更改偏移量，来修改换的装
			imgX++;
			// 循环换装
			if (imgX > 3) {
				imgX = 0;
			}
		}
	}
	
	@Override
	protected void move() {
		// 左
		if (dirFlag[0] && this.getX() > 0) {
			this.setX(this.getX() - moveSpeed);
		}
		
		// 上
		if (dirFlag[1] && this.getY() > 0) {
			this.setY(this.getY() - moveSpeed);
		}
		
		// 地图大小：720*624
		// 最后需要将面板大小精准控制为地图大小
		// 上
		if (dirFlag[2] && this.getX() < GameStart.jp.getWidth() - 48) {
			this.setX(this.getX() + moveSpeed);
		}
		
		// 下
		if (dirFlag[3] && this.getY() < GameStart.jp.getHeight() - 48 - 4) {
			// 减4是为了修正误差，误差原因未知
			this.setY(this.getY() + moveSpeed);
		}
	}
	
	/**
	 * 修正Player的坐标，使Player停下的那一刻，必定在48*48的一格中
	 * @param x		Player当前的x坐标
	 * @param y		Player当前的y坐标
	 */
	private void correctPosition(int x, int y) {
		if (x % 48 != 0) {
			int leftX = x / 48 * 48;
			int rightX = leftX + 48;
			int correctedX = (x - leftX <= rightX - x) ? leftX : rightX; 
			this.setX(correctedX);
		}
		
		if (y % 48 != 0) {
			int upY = y / 48 * 48;
			int downY = upY + 48;
			int correctedY = (y - upY <= downY - y) ? upY : downY; 
			this.setY(correctedY);
		}
	}
	
	@Override
	public void keyClick(boolean isPressed, int key) {
		/*
		 * key
		 * player1：37：左	38：上	39：右	40：下	17：ctrl
		 * palyer2：65：a	87：w	68：d	83：s	32：空格
		 * 
		 */
		// 之所以不用switch是因为case后面的参数必须是常量
		if (isPressed) { // 按下
			if (key == keys[0]) {	
				// 向左
				updateDirStatus(0);	this.imgY = 1;
			} else if (key == keys[1]) {
				// 向上
				updateDirStatus(1);	this.imgY = 3;
			} else if (key == keys[2]) {
				// 向右
				updateDirStatus(2);	this.imgY = 2;
			} else if (key == keys[3]) {
				// 向下
				updateDirStatus(3);	this.imgY = 0;
			} else if (key == keys[4]) {
				// 放泡泡
				this.pkType = true; // 开启攻击状态
			}
		} else { // 松开

			// 修正Player的坐标，使Player停下的那一刻，必定在48*48的一格中
			correctPosition(this.getX(), this.getY());
			
			if (key == keys[0]) {
				dirFlag[0] = false;
			} else if (key == keys[1]) {
				dirFlag[1] = false;
			} else if (key == keys[2]) {
				dirFlag[2] = false;
			} else if (key == keys[3]) {
				dirFlag[3] = false;
			} else if (key == keys[4]) {
				this.pkType = false; // 关闭攻击状态
			}
		}
	}
	
	/**
	 * 按下一次方向键，更新Player的方向状态值
	 * @param curDir	当前朝向
	 * 索引：方向
	 * 0：左		1：上	2：右	3：下
	 */
	private void updateDirStatus(int curDir) {
		for (int i = 0; i < dirFlag.length; i++) {
			if (i != curDir) {
				// 将其余3个方向标识false。避免45°行走
				dirFlag[i] = false;
			} else {
				dirFlag[i] = true;
			}
		}
		// 更新当前朝向
		this.curDir = curDir;
	}
	
	/**
	 * 修改Player的移动速度
	 * @param moveSpeed
	 */
	public void setMoveSpeed(int moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
	
	/**
	 * 受伤害
	 * @param harm	造成的伤害（需要减掉的血量）
	 */
	public void injured(int harm) {
		if (hp - harm > 0) {
			hp -=  harm;
		} else {
			// 血量为0，触发死亡方法
			die();
		}
 	}
	
	@Override
	public void die() {
		// 死亡动画
		// ...
	}
	
	
}
