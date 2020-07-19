package com.tedu.element;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

/**
 * @说明 所有元素的基类
 * @author dell
 *
 */
public abstract class ElementObj {
	/**
	 * @param 	x 		图片左上角横轴坐标
	 * @param 	y		图片左上角纵轴坐标
	 * @param 	w 		图片宽度
	 * @param	h		图片高度
	 * @param	icon	图片，约定：true代表存活，false代表死亡
	 * @param	live	存活
	 */
	private int x;
	private int y;
	private int w;
	private int h;
	private ImageIcon icon;
	private int HP=1;
	private int bubbleNum=1;  //初始可放置炮弹数量
	private int playerNum;		//玩家编号
	private int power=1;			//炮弹威力
	
	/**
	 * 是否存货。true：存货；false：消亡
	 * 元素的初始值应该是true，否则再判断时不会显示出来
	 */
	private boolean live = true;
	
	//不含参构造函数
	public ElementObj() {}		
	
	/**
	 * @说明 抽象方法，显示元素
	 * @param g 画笔用于绘画
	 */
	public abstract void showElement(Graphics g);
	
	/**
	 * @说明 使用接口的方式，使用接口方式需要在监听类进行类型转换
	 * 具体子类的工厂方法
	 * @return
	 */
	public ElementObj createElement(String str)
	{
		return this;
	}
	
	/**
	 * @说明 使用父类定义接收键盘事件的方法
	 * 只有需要实现键盘监听的子类重写该方法，该方法不要求必须重写
	 * @param 	bl	点击的类型是一个bool值，true代表按下，false代表松开
	 * @param 	key	代表触发键盘的code值
	 */
	public void keyClick(boolean isPressed, int key)
	{
		
	}
	
	/**
	 * 模板，不可被重写，在该方法中定义执行方法的先后顺序
	 */
	public final void model(long gameTime)
	{
		updateImage(gameTime);	//换装需要间隔时间
		move();					//移动
		add();					//发射子弹
	}
	
	/**
	 * 放置泡泡的父类方法
	 * 由于泡泡有个数限制,所以不需要间隔时间
	 * 但是在其中需要添加泡泡个数判定
	 */
	protected void add() {}
	
	/**
	 * 更新图片的父类方法
	 */
	protected void updateImage(long gameTime) {}
	
	/**
	 * 移动的父类方法
	 */
	protected void move(){}
	
	/**
	 * 死亡效果的父类方法
	 * 
	 */
	public void die(long gameTime){
	}
	
	/**
	 * @说明 本方法返回元素的碰撞矩形对象(实时返回)
	 * @return
	 */
	public Rectangle getRectangle()
	{
		return new Rectangle(x,y,w,h); 
	}
	
	/**
	 * @说明 碰撞方法
	 * 一个是this对象，一个是传入值obj
	 * @param obj
	 * @return boolean 返回true，说明有碰撞，返回false说明没有碰撞
	 */
	public boolean collide(ElementObj obj)
	{
		return this.getRectangle().intersects(obj.getRectangle());
	}
	
	/**
	 * 所有属性的get和set方法
	 */
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public int getHP() {
		return HP;
	}

	public void setHP(int hP) {
		HP = hP;
	}

	public int getBubbleNum() {
		return bubbleNum;
	}

	public void setBubbleNum(int bubbleNum) {
		this.bubbleNum = bubbleNum;
	}

	public int getPlayerNum() {
		return playerNum;
	}

	public void setPlayerNum(int playerNum) {
		this.playerNum = playerNum;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}
	
	
}
