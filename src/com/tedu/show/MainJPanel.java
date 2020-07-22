package com.tedu.show;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.tedu.controller.GameListener;
import com.tedu.controller.GameThread;
import com.tedu.game.GameStart;
import com.tedu.manager.GameLoad;

/**
 * @说明 主页
 * 单人双人选择
 * @author ligouzi
 *
 */
public class MainJPanel extends JPanel{
	
	public JButton jb1;
	public JButton jb2;
	public JButton jb3;
	
//	public static GameMainJPanel jp;
	public static SelectJPanel jp;
	
	public MainJPanel(GameJFrame gj) {
		
		GameLoad.loadImg();
		ImageIcon icon = GameLoad.imgMap.get("single");
		ImageIcon icon2 = GameLoad.imgMap.get("double");
		ImageIcon icon3 = GameLoad.imgMap.get("shuoming");
		this.setLayout(null);
		
		jb1 = new JButton(icon);
		jb1.setBorderPainted(false);
		jb1.setContentAreaFilled(false);
		jb1.setBounds(250, 200, icon.getIconWidth(), icon.getIconHeight());
		
		jb2 = new JButton(icon2);
		jb2.setBorderPainted(false);
		jb2.setContentAreaFilled(false);
		jb2.setBounds(250, 300, icon2.getIconWidth(), icon2.getIconHeight());
		
		jb3 = new JButton(icon3);
		jb3.setBorderPainted(false);
		jb3.setContentAreaFilled(false);
		jb3.setBounds(250, 400, icon2.getIconWidth(), icon2.getIconHeight());
		
		jp = new SelectJPanel(gj);
//		jp = new GameMainJPanel();
//		实例化监听
//		GameListener listener = new GameListener();
//		实例化主线程
//		GameThread th = new GameThread();
		
		jb1.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] options = { "确定" }; 
				JOptionPane.showOptionDialog(null, "单人模式尚未开发，敬请期待", "提示", 
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
				null, options, null); 			
			}
		});
		jb2.addActionListener(new ActionListener() {				
			@Override
			public void actionPerformed(ActionEvent e) {			
//				注入
				gj.setjPanel(jp);		
//				gj.setKeyListener(listener);
//				gj.setThread(th);
				gj.start();
			}
		});
		jb3.addActionListener(new ActionListener() {				
			@Override
			public void actionPerformed(ActionEvent e) {			
				Object[] options = { "确定" }; 
				JOptionPane.showOptionDialog(null, "<html><body><tr>player1 方向键为wasd 泡泡为空格</tr>"
						+ "<tr>player2 方向键为上下左右 泡泡为enter</tr>"
						+ "<tr>金卡：加一条命</tr>"
						+ "<tr>鬼头：加移动速度</tr>"
						+ "<tr>泡泡：加泡泡的放置数量</tr>"	
						+ "<tr>药水：加泡泡的威力</tr></body></html>", "游戏说明", 
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
				null, options, null); 
			}
		});
		this.add(jb1);
		this.add(jb2);
		this.add(jb3);
//		gj.setFocusable(true);
	}	
	
	@Override
	protected void paintComponent(Graphics g) {
		ImageIcon icon = GameLoad.imgMap.get("ground");
		g.drawImage(icon.getImage(), 0, 0, GameStart.jp.getWidth(), GameStart.jp.getHeight(),null);
	}
	
}
