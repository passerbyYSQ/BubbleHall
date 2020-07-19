package com.tedu.controller;

import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import com.tedu.element.ElementObj;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

/**
 * 游戏的主线程，用于控制游戏的加载
 * @功能		控制游戏加载，关卡，游戏运行自动化
 * 			游戏判定：地图切换，资源释放和重新读取
 * 
 * @说明		为了使用另一种方式创建线程，这里使用继承Thread
 * 			一般使用接口方式实现
 * 
 * @author passerbyYSQ
 * @create 2020年7月14日 下午2:27:35
 */
public class GameThread extends Thread {
	
	// 联动元素管理器
	private ElementManager em;
	
	public GameThread() {
		em = ElementManager.getManager();
	}
	
	@Override
	public void run() {
		//super.run();
		
		// 扩展，可以将true变为一个变量用于游戏进程控制（例如：暂停）
		while (true) {
			// 游戏开始前：读进度条，加载游戏资源（场景）
			gameLoad();
			
			// 游戏进行时
			gameRun();
			
			// 游戏场景结束时：游戏资源回收
			gameOver();
			
			try {
				// 由于继承了Thread类，可以直接调用sleep
				sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 游戏加载
	 */
	private void gameLoad() {
//		System.out.println("gameLoad");
		// 加载图片到集合
		GameLoad.loadImg();	
		
		/*
		 * 加载玩家。可以考虑传参，来控制单人或双人
		 * 示例："100,100,player";
		 * 参数说明：
		 * 初始x的坐标，初始y的坐标，图片的key（对应GameData.pro中的key）
		 * 
		 */
		GameLoad.loadPlayer("0,0,player1,37,38,39,40,17",
				"48,48,player2,65,87,68,83,32");
		
		// 加载地图，10 可以设置成变量，切换关卡
		//GameLoad.mapLoad(10);  
		
		
		// 加载NPC...
		
	}	
	
	
	/**
	 * 游戏进行时
	 * @任务说明		1、自动化玩家的移动、碰撞等
	 * 				2、新元素的增加（例如：NPC挂了之后出现道具）
	 * 				3、暂停
	 */ 
	private void gameRun() {
		long gameTime = 3L;
		// 预留扩展，true可以改为变量，用于控制关卡结束等
		while (true) {
//			System.out.println("gameRun");
			
			// 所有元素刷新移动
			Map<GameElement, List<ElementObj>> all = em.getGameElements();
			
			moveAndUpdate(all, gameTime);
			
//			elementsCollide(GameElement.PLAYFILE, GameElement.ENEMY);
//			elementsCollide(GameElement.PLAYFILE, GameElement.MAPS);
			
			gameTime++;
			try {
				sleep(35);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void elementsCollide(GameElement eleA, GameElement eleB) {
		List<ElementObj> listA = em.getElementsByKey(eleA);
		List<ElementObj> listB = em.getElementsByKey(eleB);
		
		for (ElementObj a : listA) {
			for (ElementObj b : listB) {
				if (a.collide(b)) {
					// 扩展问题：如果是boss？也是一枪一个吗？扣血！
					// 给ElementObj加一个受攻击方法，还可以传入另外一个对象的攻击力
					// 判断造成的伤害，如果血量减为0，再将alive设置为false
					// 受攻击方法由子类复写
					a.setLive(false);
					b.setLive(false);
					break;
				}	
			}
		}
	}
	
	public void moveAndUpdate(Map<GameElement, List<ElementObj>> all, long gameTime) {
		// GameElement.values()是隐藏方法，无法点进去
		// 返回的数组的顺序时是枚举变量声明时的顺序
					
		for (GameElement ge : GameElement.values()) {
			List<ElementObj> list = all.get(ge);
			// 操作集合不要使用迭代器foreach，修改数据会抛出异常
//						for (int i = 0; i < list.size(); i++) {
			for (int i = list.size() - 1; i >= 0; i--) {
				ElementObj obj = list.get(i);
				
				// 如果元素处于消亡状态，将它从元素管理器中移除
				if (!obj.isLive()) { 
//								list.remove(i--); // 回退
					
					// 启动一个消亡方法，方法中可以做很多事情。例如：死亡动画、掉装备
					obj.die();
					list.remove(i);
					continue;
				}
				
				obj.model(gameTime);
			}
		}
	}

	/**
	 * 游戏切换关卡
	 */
	private void gameOver() {
	
	}
}
