package com.tedu.show;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.tedu.controller.GameListener;
import com.tedu.controller.GameThread;
import com.tedu.game.GameStart;
import com.tedu.manager.GameLoad;

/**
 * @说明 主页
 * @author ligouzi
 *
 */
public class MainJPanel extends JPanel{
	
	public JButton jb1;
	public JButton jb2;
	
//	public static GameMainJPanel jp;
	public static SelectJPanel jp;
	
	public MainJPanel(GameJFrame gj) {
		GameLoad.loadImg();
		ImageIcon icon = GameLoad.imgMap.get("single");
		ImageIcon icon2 = GameLoad.imgMap.get("double");
		this.setLayout(null);
		
		jb1 = new JButton(icon);
		jb1.setBorderPainted(false);
		jb1.setContentAreaFilled(false);
		jb1.setBounds(250, 200, icon.getIconWidth(), icon.getIconHeight());
		
		jb2 = new JButton(icon2);
		jb2.setBorderPainted(false);
		jb2.setContentAreaFilled(false);
		jb2.setBounds(250, 300, icon2.getIconWidth(), icon2.getIconHeight());
		
		jp = new SelectJPanel(gj);
//		jp = new GameMainJPanel();
//		实例化监听
//		GameListener listener = new GameListener();
//		实例化主线程
//		GameThread th = new GameThread();
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
		this.add(jb1);
		this.add(jb2);
//		gj.setFocusable(true);
	}	
	
	@Override
	protected void paintComponent(Graphics g) {
		ImageIcon icon = GameLoad.imgMap.get("ground");
		g.drawImage(icon.getImage(), 0, 0, GameStart.jp2.getWidth(), GameStart.jp2.getHeight(),null);
	}
	
}
