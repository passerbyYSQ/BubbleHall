package com.tedu.show;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayer;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import com.tedu.controller.GameThread;
import com.tedu.element.ElementObj;
import com.tedu.game.GameStart;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.MusicPlayer;

/**
 * 游戏的主要面板
 * 主要进行元素的显示，同时进行界面的刷新(多线程)
 * 
 * @多线程刷新
 * 1、本类实现Runnable接口
 * 2、定义内部类
 * 
 * @author passerbyYSQ
 * @create 2020年7月13日 下午3:57:40
 */
public class GameMainJPanel extends JPanel implements Runnable {
	
	// 联动元素管理器
	private ElementManager em;
	
//	private JPanel btnPanel;
	private JButton bgmBtn;	// BGM的按钮
	private boolean bgmStatus = false; // BGM的是否开启
	
	private JButton runBtn;
	private JButton overBtn;
	
	public static OverJPanel jp;
	
	public GameMainJPanel(GameJFrame gj) {
		init(gj);
//		System.out.println(this.getLayout());
//		System.out.println(this.getWidth());
//		System.out.println(this.getHeight());
	}
	
	protected void init(GameJFrame gj) {
		// 得到元素管理器的单例
		em = ElementManager.getManager();

//		this.setLayout(null);
//		JButton bgmBtn = new JButton("Click Me");
//		bgmBtn.setBounds(300, 200, 100,50);
//		this.add(bgmBtn);
		
		// 布局嵌套。BorderLayout.SOUTH和FlowLayout.CENTER)实现按钮组底部居中
		this.setLayout(new BorderLayout());
		JPanel btnPanel = new JPanel();
		btnPanel.setOpaque(false);	// 将面板设置为透明，否则灰色面板会遮挡地图
		btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		bgmBtn = new JButton("开启BGM");
		runBtn = new JButton("暂停游戏");
		overBtn = new JButton("结束游戏");
		
		bgmBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 获取当前游戏线程的BGM
				MusicPlayer bgm = ((GameThread) GameStart.gj.getThread()).getBgm();
				if (bgmStatus) { // 如果已经开了，就关掉
					bgm.over();
					bgmBtn.setText("开启BGM");
				} else { // 如果处于关闭，就开启
					bgm.play();
					bgmBtn.setText("关闭BGM");
				}
				bgmStatus = !bgmStatus; // 状态取反
				
				// 注意全局监听是作用于整个窗体，而并非面板
				// 点击按钮后，焦点落在了按钮上，窗体需要重新获得焦点，按钮监听才会有效
				GameStart.gj.requestFocus();
//				GameMainJPanel.this.requestFocus();
			}
		});
		
		runBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 获取游戏线程
//				GameThread thread = (GameThread) GameStart.gj.getThread();
				// 获取当前游戏线程的BGM
				MusicPlayer bgm = ((GameThread) GameStart.gj.getThread()).getBgm();
				if (GameThread.isPause()) { // 如果已经暂停，则继续游戏
					GameThread.setPause(false);
					runBtn.setText("暂停游戏");
					if (bgmStatus) { // 如果已经开了BGM，就关掉
						bgm.play();
					}
					GameStart.gj.requestFocus();
					
				} else {
					GameThread.setPause(true);
					runBtn.setText("继续游戏");
					if (bgmStatus) {
						bgm.over();
					}
					// 注意
					// 暂停期间让窗体失去焦点，让键盘监听失效
					// 否则暂停期间，人物可以切换方向
				}
				
			}
		});
		
		jp = new OverJPanel(gj);
		// 结束游戏的按钮的点击事件
		overBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ...
				gj.setjPanel(jp);
				gj.setThread(null, 1);
				gj.start();
				// 注意全局监听是作用于整个窗体，而并非面板
				// 点击按钮后，焦点落在了按钮上，窗体需要重新获得焦点，按钮监听才会有效
				GameStart.gj.requestFocus();
			}
		});
		
		btnPanel.add(bgmBtn);
		btnPanel.add(runBtn);
		btnPanel.add(overBtn);
		this.add(btnPanel, BorderLayout.SOUTH);
	}
	
	// 重写绘画方法
	/**
	 * 绘画时是有固定的顺序，先绘画的图片会在底层，后绘画的图片会覆盖先绘画的
	 * 约定：本方法只执行一次，想实时刷新需要使用多线程
	 */
	@Override
	public void paint(Graphics g) {
		
		super.paint(g);
		// 所有元素的显示
		Map<GameElement, List<ElementObj>> all = em.getGameElements();
		// GameElement.values()是隐藏方法，无法点进去
		// 返回的数组的顺序时是枚举变量声明时的顺序
		for (GameElement ge : GameElement.values()) {
			List<ElementObj> list = all.get(ge);
//			if(ge.equals(GameElement.MAPS)) {
////			if(ge == GameElement.MAPS) {
//				Collections.sort(list);
//			}
			for (int i = 0; i < list.size(); i++) {
				ElementObj obj = list.get(i);
				obj.showElement(g);
			}
		}
		
		// 重新绘制子组件，否则paint之后会遮挡住组件
		super.paintChildren(g);
		
//		Set<GameElement> keySet = all.keySet();
//		for (GameElement key : keySet) {
//			List<ElementObj> list = all.get(key);
//			for (int i = 0; i < list.size(); i++) {
//				ElementObj obj = list.get(i);
//				obj.showElement(g);
//			}
//		}
			
	}
	
	@Override
	public void run() {
		while (true) {
			this.repaint();
			// 一般情况下，通过休眠来控制速度
			
			try {
				// 50 ms 刷新一次
				// 即：1秒刷新20次
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
