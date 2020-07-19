package com.tedu.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tedu.element.ElementObj;

/**
 * 元素管理器，专门存储所有的元素
 * 同时提供方方法给予视图和控制(Controller)获取数据
 *  
 * @问题
 * 1、怎么存放所有的元素数据？ List, Map, Set
 * 2、管理器是视图和控制(Controller)都要访问，管理器必须只有一个，单例
 * 
 * @author passerbyYSQ
 * @create 2020年7月13日 上午11:35:57
 */
public class ElementManager {
	
	/*
	 * 枚举类型作为Map的key
	 * key 对应 元素分类，例如：敌人、地图
	 * value 对应该分类的所有元素实例的List集合
	 * 
	 * List中的泛型应该是元素的基类
	 */
	private Map<GameElement, List<ElementObj>> gameElements;

	public Map<GameElement, List<ElementObj>> getGameElements() {
		return gameElements;
	}
	
	// 添加元素，多半由加载器调用
	public void addElement(ElementObj obj, GameElement ge) {
		gameElements.get(ge).add(obj);
	}
	
	// 删除元素，多半由加载器调用
	public void removeElement(int index, GameElement ge) {
		ElementObj obj = gameElements.get(ge).remove(index);
//		System.out.println("移除：" + obj);
	}
	
	// 根据key，取出对应的集合
	public List<ElementObj> getElementsByKey(GameElement ge) {
		return gameElements.get(ge);
	}
	
	/*
	 * 单例模式：内存中有且只有一个实例
	 * 1、饿汉模式：启动时就自动加载实例
	 * 2、饱汉模式：需要使用时才加载实例
	 * 
	 * 编写方式：
	 * 1、需要一个静态的属性（常量）单例的引用
	 * 2、提供一个静态方法返回单例的引用（注意线程安全问题）
	 * 3、将构造方法私有化
	 */
	private static ElementManager EM = null;	// 单例的引用
	
	public static synchronized ElementManager getManager() {
		if (EM == null) {
			EM = new ElementManager();	// 饱汉模式
		}
		return EM;
	}
	
	/*
	// 饿汉模式
	static {
		EM = new ElementManager();
	}
	*/
	
	// 私有化构造方法
	private ElementManager() {
		init();
	}
	
	/*
	 * 可以被子类继承复写 
	 */
	protected void init() {
		gameElements = new HashMap<GameElement, List<ElementObj>>();
		
		for (GameElement ge : GameElement.values()) {
			gameElements.put(ge, new ArrayList<ElementObj>());
		}
		
//		gameElements.put(GameElement.PLAY, new ArrayList<ElementObj>());
//		gameElements.put(GameElement.MAPS, new ArrayList<ElementObj>());
//		gameElements.put(GameElement.ENEMY, new ArrayList<ElementObj>());
//		gameElements.put(GameElement.BOSS, new ArrayList<ElementObj>());
		
		// 道具、子弹、爆炸效果...
	}

}
