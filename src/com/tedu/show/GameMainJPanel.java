package com.tedu.show;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.tedu.element.ElementObj;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

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
	
	public GameMainJPanel() {
		init();

	}
	
	protected void init() {
		// 得到元素管理器的单例
		em = ElementManager.getManager();
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
			for (int i = 0; i < list.size(); i++) {
				ElementObj obj = list.get(i);
				obj.showElement(g);
			}
		}
		
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
