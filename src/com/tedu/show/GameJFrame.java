package com.tedu.show;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tedu.controller.GameListener;
import com.tedu.controller.GameThread;

/**
 * 游戏窗体
 * 主要功能
 * 1、关闭，显示，最大最小化
 * 2、嵌入面板，启动主线程
 * 用户打开窗体，窗体会以上一次窗体的样式打开
 * 
 * @author passerbyYSQ
 * @create 2020年7月13日 下午2:48:25
 */
public class GameJFrame extends JFrame{
	
	/**
	 * 单个箱子像素：48*48
	 * 计划放置格子：15*13
	 * 地图大小：720*624（跟窗体大小有偏差）
	 */
	public static int GameX = 734;
	public static int GameY = 710;

	// 当前正在显示的面板
	private JPanel jPanel = null; 
	// 键盘监听
	private KeyListener keyListener = null;
	// 鼠标监听
	private MouseMotionListener mouseMotionListener = null;
	private MouseListener mouseListener = null;
	
	// 游戏主线程
	private Thread thread = null;
	
	public static GameJFrame gj = new GameJFrame();
	//主面板
	public static MainJPanel jp1;
	//挑选地图面板
	public static SelectJPanel jp2;
	//游戏面板
	public static GameMainJPanel jp3;
	//结束面板
	public static OverJPanel jp4;
	
	public GameJFrame() {
		init();
	}
	
	// 初始化窗体
	protected void init() {
		// 设置窗体大小
		this.setSize(GameX, GameY);
		// 设置窗体标题
		this.setTitle("泡泡堂"); // 放到配置文件
		// 窗体关闭方式：关闭窗体时，同时终止程序
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 窗体居中显示
		this.setLocationRelativeTo(null);
	}
	
	// 窗体布局
	public void addButton() {
		
	}
	
	//设置面板
	public static void setJPanel(String Name) {
		if (Name.equals("MainJPanel")) {
			jp1 = new MainJPanel();
			gj.setjPanel(jp1);
			gj.start();
		}
		if (Name.equals("SelectJPanel")) {
			jp2 = new SelectJPanel();
			gj.setjPanel(jp2);
			gj.start();
		}
		if (Name.equals("GameMainJPanel")) {
//			实例化监听
			GameListener listener = new GameListener();
//			实例化主线程
			GameThread th = new GameThread(SelectJPanel.map);
			jp3 = new GameMainJPanel();
			gj.setjPanel(jp3);
			gj.setKeyListener(listener);
			gj.setThread(th, 0);	
			gj.start();
			gj.setFocusable(true);
		}
		if (Name.equals("OverJPanel")) {
			jp4 = new OverJPanel();
			gj.setjPanel(jp4);
			gj.setThread(null, 1);
			gj.start();
		}
		gj.setVisible(false);
		gj.setVisible(true);
	}
	
	// 窗体启动方法
	public void start() {
		if (jPanel != null) {
			this.add(jPanel);
		}
		if (keyListener != null) {
			this.addKeyListener(keyListener);
		}
		if (mouseMotionListener != null) {
			this.addMouseMotionListener(mouseMotionListener);
		}
		if (mouseListener != null) {
			this.addMouseListener(mouseListener);
		}
		if (thread != null) {
			System.out.println();
			thread.start();
		}
		// 界面刷新，显示窗体
		this.setVisible(true);
		
		// 做了类型判断，强制类型不会出错
		if (jPanel instanceof Runnable) {
			// 启动子线程来不断刷新界面
			new Thread((Runnable) jPanel).start();
		}
	}

	/*
	 * set注入：SSM框架通过set方法注入配置文件中读取的数据，将配置文件中的数据
	 * 赋值为类的属性
	 * 构造注入：需要配合构造方法
	 * spring 中ioc进行对象的自动生成，管理
	 */
	public void setjPanel(JPanel jPanel) {
		if(this.jPanel != null) {
			this.remove(this.jPanel);
		}
		this.jPanel = jPanel;
	}
	
	public void setKeyListener(KeyListener keyListener) {
		this.keyListener = keyListener;
	}

	public void setMouseMotionListener(MouseMotionListener mouseMotionListener) {
		this.mouseMotionListener = mouseMotionListener;
	}

	public void setMouseListener(MouseListener mouseListener) {
		this.mouseListener = mouseListener;
	}

	public void setThread(Thread thread,int num) {
//		if(num == 1) {
//			this.thread.interrupt();
//		}
		this.thread = thread;
	}

	public Thread getThread() {
		return thread;
	}
	
	
}
