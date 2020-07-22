package com.tedu.show;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tedu.controller.GameListener;
import com.tedu.controller.GameThread;
import com.tedu.game.GameStart;
import com.tedu.manager.GameLoad;


/**
 * 选关卡面板
 * @author passerbyYSQ
 * @create 2020年7月22日 上午10:59:14
 */
public class SelectJPanel extends JPanel{
	
	public JButton jb1;
	public JButton jb2;
	
	public JLabel jl;
	
	public static GameMainJPanel jp;
	
	public SelectJPanel(GameJFrame gj) {
		
		this.setLayout(null);
		
		Font font = new Font("微软雅黑",Font.BOLD,25);
		
		jl = new JLabel("关卡选择");
		jl.setFont(font);
		jl.setForeground(Color.ORANGE);
		jl.setBounds(300, 100, 100,50);
		
		jb1 = new JButton("1");
		jb1.setBackground(Color.ORANGE);
		jb1.setOpaque(true);
		jb1.setBorderPainted(false);
		jb1.setBounds(300, 200, 100,50);
		
		jb2 = new JButton("2");
		jb2.setBackground(Color.ORANGE);
		jb2.setOpaque(true);
		jb2.setBorderPainted(false);
		jb2.setBounds(300, 300, 100,50);
		
		jp=new GameMainJPanel(gj);		

		jb1.addActionListener(new ActionListener() {
//			实例化监听
			GameListener listener = new GameListener();
//			实例化主线程
			GameThread th = new GameThread(1);
			@Override
			public void actionPerformed(ActionEvent e) {			
//				注入
				gj.setjPanel(jp);		
				gj.setKeyListener(listener);
				gj.setThread(th,0);
				gj.start();
			}
		});
		
		jb2.addActionListener(new ActionListener() {
//			实例化监听
			GameListener listener = new GameListener();
//			实例化主线程
			GameThread th = new GameThread(2);
			@Override
			public void actionPerformed(ActionEvent e) {			
//				注入
				gj.setjPanel(jp);		
				gj.setKeyListener(listener);
				gj.setThread(th,0);
				gj.start();
			}
		});
		this.add(jl);
		this.add(jb1);
		this.add(jb2);
		gj.setFocusable(true);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		ImageIcon icon = GameLoad.imgMap.get("ground");
		g.drawImage(icon.getImage(), 0, 0, MainJPanel.jp.getWidth(), MainJPanel.jp.getHeight(),null);
	}

}
