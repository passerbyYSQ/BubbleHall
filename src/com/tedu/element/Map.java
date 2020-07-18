package com.tedu.element;

import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.ImageIcon;

import com.tedu.manager.GameLoad;

/**
 * @说明 地图类
 * @author ligouzi
 * @说明 map(num).pro里的坐标分别代表起点x，起点y，图片宽度，图片长度
 */
public class Map extends ElementObj{

	private int sx = 0; //图片分割的起点x轴
	private int sy = 0; //图片分割的起点y轴	
	private int dx = 0; //图片分割的终点x轴
	private int dy = 0; //图片分割的终点y轴
	
	@Override
	public void showElement(Graphics g) {
		//做图片的分割
		g.drawImage(this.getIcon().getImage(), 
				this.getX(), this.getY(), 
				this.getX()+48, this.getY()+48,
				sx, sy, 
				dx, dy, 
				null);		
	}
	
	@Override
	public ElementObj createElement(String str) {
		String[] split = str.split(",");
		ImageIcon icon = null;
		if(setimage(split[0],"map1") != null) {
			if(split[0].equals("tree") || split[0].equals("yellowhouse") || split[0].equals("bluehouse")) {
				String img = setimage(split[0],"map2");
				icon = new ImageIcon(img);
			}
			else {
				String img = setimage(split[0],"map1");
				System.out.println(split[0]);
				icon = new ImageIcon(img);
			}			 
		}else {
			System.out.println("生成图片失败");
		}		
		this.setX(Integer.parseInt(split[1]));
		this.setY(Integer.parseInt(split[2]));
		this.setIcon(icon);
		return this;
	}
	
	private static Properties pro =new Properties();	
	private String setimage(String imageName,String map) {
		String image = null;
		ClassLoader classLoader = GameLoad.class.getClassLoader();
		//先去map(num).pro里读取图片截取的位置和图片大小
		InputStream maps = classLoader.getResourceAsStream("com/tedu/text/"+map+".pro");
		if(maps == null) {
			System.out.println("配置文件读取异常,请重新安装");
			return null;
		}		
		try {
			pro.clear();
			pro.load(maps);
			Enumeration<?> names = pro.propertyNames();
			while(names.hasMoreElements()) {
				String key=names.nextElement().toString();
				if (key.equals(imageName)) {
					String[] split = pro.getProperty(key).split(",");
					this.sx = Integer.parseInt(split[0]);
					this.sy = Integer.parseInt(split[1]);
					this.setW(Integer.parseInt(split[2]));
					this.setH(Integer.parseInt(split[3]));
					this.dx = sx+this.getW();
					this.dy = sy+this.getH();
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//然后去GameData.pro里读取图片位置，并返回图片位置
		maps = classLoader.getResourceAsStream("com/tedu/text/GameData.pro");
		if(maps == null) {
			System.out.println("配置文件读取异常,请重新安装");
			return null;
		}
		try {
			pro.clear();
			pro.load(maps);
			Enumeration<?> names = pro.propertyNames();
			while(names.hasMoreElements()) {
				String key=names.nextElement().toString();
				if (key.equals(map)) {
					image = pro.getProperty(key);
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return image;
	}

}
