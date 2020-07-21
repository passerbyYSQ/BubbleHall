package com.tedu.controller;

import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import com.tedu.element.ElementObj;
import com.tedu.element.PaoPao;
import com.tedu.element.PaoPaoExplode;
import com.tedu.element.Player;
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
	
	//暂停判定符
	private static boolean isPause;
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
		
		// 加载图片资源。注意必须在加载地图、人物之前
		GameLoad.loadImg();	
		
		// 加载游戏音乐（包括音效和背景音乐）
		GameLoad.loadMusic();
		
		// 加载地图，10 可以设置成变量，切换关卡
		GameLoad.MapLoad(1);  
		
		/*
		 * 加载玩家。可以考虑传参，来控制单人或双人
		 * 示例："100,100,player";
		 * 参数说明：
		 * 初始x的坐标，初始y的坐标，图片的key（对应GameData.pro中的key）
		 * 
		 * 最后一个参数用于记录玩家1号还是2号  阿灿
		 * 
		 */
		GameLoad.loadPlayer("144,144,player1,37,38,39,40,17,1",
				"144,144,player2,65,87,68,83,32,2");
		
		// 加载NPC...
		
	}	
	
	
	/**
	 * 游戏进行时
	 * @任务说明		1、自动化玩家的移动、碰撞等
	 * 				2、新元素的增加（例如：NPC挂了之后出现道具）
	 * 				3、暂停
	 */ 
	private long gameTime = 3L;
	private void gameRun() {
		// 开始循环播放背景音乐
//		GameLoad.musicMap.get("bgm0").setLoop(true).play();
		
		// 预留扩展，true可以改为变量，用于控制关卡结束等
		while (true) {
//			System.out.println("gameRun");
			if(!isPause)
			{
				// 所有元素刷新移动
				Map<GameElement, List<ElementObj>> all = em.getGameElements();
				
				moveAndUpdate(all);
				
				// 约定：第一个参数：碰撞的主动方；第二个参数：被碰撞的一方
				elementsCollide(GameElement.PLAYER, GameElement.MAPS); // Player和障碍物
				elementsCollide(GameElement.EXPLODE, GameElement.PLAYER); // 泡泡爆炸和Player
				elementsCollide(GameElement.EXPLODE, GameElement.MAPS); // 泡泡爆炸和地图
				elementsCollide(GameElement.PLAYER, GameElement.TOOL);	// Player和道具
				elementsCollide(GameElement.EXPLODE, GameElement.PAOPAO); // 泡泡爆炸和泡泡
				elementsCollide(GameElement.PLAYER, GameElement.PAOPAO); // player和泡泡
				gameTime++;
				
			}
			try {
				sleep(35);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 注意，做一种约定
	 * @param eleA		碰撞的主动方
	 * @param eleB		被撞的一方
	 */
	public void elementsCollide(GameElement eleA, GameElement eleB) {
		List<ElementObj> listA = em.getElementsByKey(eleA);
		List<ElementObj> listB = em.getElementsByKey(eleB);
		//人物和道具之间的碰撞设置
		if(eleA==GameElement.PLAYER&&eleB==GameElement.TOOL)
		{
			for(ElementObj g1:listA)
			{
				for(ElementObj g2:listB)
				{
					if(g2.collide(g1))
					{	
						return ;
					}
				}
			}
		}
		// 泡泡爆炸和地图之间的碰撞设置
		if (eleA == GameElement.EXPLODE && eleB == GameElement.MAPS) {
			for (ElementObj g1 : listA) {
				for (ElementObj g2 : listB) {
					if (g1.collide(g2)) {
						g2.setLive(false);
						return;
					}
				}
			}
		}
		// 泡泡爆炸和泡泡之间的碰撞设置
		if (eleA == GameElement.EXPLODE && eleB == GameElement.PAOPAO) {
			for (ElementObj g1 : listA) {
				for (ElementObj g2 : listB) {
					if (g1.collide(g2)) {
						g2.setLive(false);
						return;
					}
				}
			}
		}
		// player和泡泡之间的碰撞设置
		if (eleA == GameElement.PLAYER && eleB == GameElement.PAOPAO) {
			for (ElementObj g1 : listA) {
				for (ElementObj g2 : listB) {
					if (g1.collide(g2)) {
						Player player=(Player)g1;
						player.setFlat(0);
					}else {
						Player player=(Player)g1;
						player.setFlat(1);
					}
					
					
				}
			}
		}
		for (ElementObj a : listA) {
			for (ElementObj b : listB) {
				if (a.collide(b)) {
					
					// 爆炸时碰撞，未爆炸时的不碰撞
					if (eleA.equals(GameElement.EXPLODE)
							&& eleB.equals(GameElement.PLAYER)) {
						b.die(gameTime);
					}
					
					// 开始死亡动画
//					a.die(gameTime);
//					b.die(gameTime);
					
					// 扩展问题：如果是boss？也是一枪一个吗？扣血！
					// 给ElementObj加一个受攻击方法，还可以传入另外一个对象的攻击力
					// 判断造成的伤害，如果血量减为0，再将alive设置为false
					// 受攻击方法由子类复写
//					a.setLive(false);
//					b.setLive(false);
//					break;
				}	
			}
		}
		
	}
	
	public void moveAndUpdate(Map<GameElement, List<ElementObj>> all) {
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
//					
					obj.die(gameTime);  //调用死亡方法
					em.removeElement(i, ge);
//					list.remove(i);
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

	/**
	 * 游戏暂停状态设置
	 * @return
	 */
	static boolean isPause() {
		return isPause;
	}

	static void setPause(boolean pause) {
		isPause = pause;
	}

	
	/*
	protected void load() {
		ImageIcon icon = new ImageIcon("image/tank/play1/player1_up.png");
		ElementObj obj = new Play(100, 100, 50, 50, icon);
		em.addElement(GameElement.PLAY, obj);
		
		// 添加敌人
		for (int i = 0; i < 10; i++) {
			em.addElement(GameElement.ENEMY, new Enemy().createElement(""));
		}
		
		// 注意：只讲子弹的发射和死亡。思考：道具的掉落是否与子弹的发射相近？
		
		// 添加一个敌人类，仿照玩家类编写，注意不需要实现键盘监听
		// 实现敌人的显示，同时实现最简单的移动。
		// 例如：在(100, 100)和(500, 100)之间来回移动
		
//		obj = new Play(0, 0, 300, 300, icon);
//		em.addElement(GameElement.MAPS, obj);
//		
//		obj = new Play(200, 200, 50, 50, icon);
//		em.addElement(GameElement.ENEMY, obj);
		
	}
	*/
	
}
