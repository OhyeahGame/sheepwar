package sheepwar;

import java.util.Vector;

import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.game.SGraphics;
import cn.ohyeah.stb.util.RandomValue;


/**
 * 武器技能
 * @author Administrator
 */
public class Weapon implements Common {
	int id;                 //武器id
	int status;				//状态
	int objectId;			//武器所属者	ID
	int speedX;             //X轴移动速度
	int speedY;             //Y轴移动速度
	int direction;          //武器移动方向 
	int frame;				//帧数
	int mapx;				//X轴坐标
	int mapy;				//Y轴坐标
	int width;				//武器宽度
	int height;				//武器高度
	int scores;				//提供的积分
	boolean isUse;			//是否在使用
	int position;			//拳套初始位置，0-上方，1-下方
	int hitNum;				//拳套击中狼的个数
	
	public StateGame stateGame;
	public Weapon(StateGame stateGame){
		this.stateGame = stateGame;
	}
	public Weapon(){}
	
	public final static int WEAPON_MOVE_LEFT = 0;
	public final static int WEAPON_MOVE_RIGHT = 1;
	public final static int WEAPON_MOVE_DOWN = 2;
	
	public Vector bombs = new Vector();
	public Vector nets = new Vector();
	public Vector booms = new Vector();
	public Vector protects = new Vector();
	public Vector glares = new Vector();
	public Vector harps = new Vector();			
	public Vector fruits = new Vector();
	public Vector gloves = new Vector();
	
	/*捕狼网持续时间*/
	public static long netTimeS, netTimeE;
	public static int netInterval = 1;
	
	/**
	 * 创建普通武器 ---Shuriken
	 * @param own  
	 * @param direction   普通武器方向(2左3右)
	 */
	public void createBomb(Role own, int direction) {
		Weapon w = new Weapon();
		w.direction = direction;
		w.mapx = own.mapx;  
		w.mapy = own.mapy+30;  
		w.width = 18;
		w.height = 19;
		w.speedX = 30;
		bombs.addElement(w);
	}
	
	/**
	 * 画出普通武器
	 * @param g
	 */
	public void showBomb(SGraphics g){
		int len = bombs.size();
		Image bomb = Resource.loadImage(Resource.id_bomb);
		Weapon w = null;
		int tempx=0, tempy=0;
		for(int i=len-1; i>=0; i--){
			w = (Weapon)bombs.elementAt(i);
			if(w.direction == WEAPON_MOVE_LEFT){
				tempx = w.mapx;
				tempy = w.mapy;
				tempx -= w.speedX;
				w.mapx = tempx;
				w.frame = (w.frame+1)%3;
			}else if(w.direction == WEAPON_MOVE_DOWN){
				tempx = w.mapx;
				tempy = w.mapy;
				tempy += w.speedY;
				w.mapy = tempy;
			}
			g.drawRegion(bomb, w.frame *bomb.getWidth()/3, 0, bomb.getWidth()/3, bomb.getHeight(), 0, tempx, tempy, 0);
		}
	}
	
	/*捕狼网*/
	public void createNet(Role own, int direction){
		Weapon w = new Weapon();
		w.id = netWeapon;
		w.direction = direction;
		w.mapx = own.mapx-20;  
		w.mapy = own.mapy+45;  
		w.width = 13;
		w.height = 13;
		w.speedX = 15;
		w.isUse = false;
		nets.addElement(w);
	}
	
	public void showNet(SGraphics g){
		int len = nets.size();
		Image net = Resource.loadImage(Resource.id_net);
		Image net2 = Resource.loadImage(Resource.id_net2);
		Weapon w = null;
		for(int i=len-1; i>=0; i--){
			w = (Weapon)nets.elementAt(i);
			if(w.mapx>=180){
				w.mapx -= w.speedX;
				w.frame = (w.frame+1)%3;
				g.drawRegion(net, w.frame *net.getWidth()/3, 0, net.getWidth()/3, net.getHeight(), 0, w.mapx, w.mapy, 0);
			}else{
				w.height = net2.getHeight();
				w.width = net2.getWidth();
				if(!w.isUse){
					w.mapx = w.mapx+6-w.width/2;
					w.mapy = w.mapy+6-w.height/2;
					w.isUse = true;
					netTimeS = System.currentTimeMillis()/1000;
				}
				g.drawImage(net2, w.mapx, w.mapy, 20);
			}
		}
	}
	
	/*创建npc的普通攻击 武器*/
	public void createBoom(Role npc,int direction){
		Weapon w = new Weapon();
		w.direction = direction;
		w.mapx = (npc.mapx + npc.height)/2+npc.width;
		w.mapy = npc.mapy + npc.height/2;
		w.height = 22;
		w.width = 14;
		w.speedX = 10;		
		w.scores = 50;
		w.status = OBJECT_NOT_HIT;
		booms.addElement(w);
	}
	
	/*画出狼的武器*/
	public void showBoom(SGraphics g,Role player){ 
		g.setClip(0, 0, gameW, ScrH);
		Image boom = Resource.loadImage(Resource.id_boom);
		Image boomEffect = Resource.loadImage(Resource.id_boom1);
		Weapon w = null;
		int tempx = 0,tempy = 0;
		//System.out.println("子弹容量:"+ booms.size());
		for(int i = booms.size() - 1;i>=0;i --){
			w = (Weapon)booms.elementAt(i);
			if(w.status == OBJECT_NOT_HIT){
				w.mapx += w.speedX;
				tempy = w.mapy;
				tempx = w.mapx;
				g.drawRegion(boom, 0, 0, boom.getWidth(), boom.getHeight(), 0, tempx, tempy, 20);
			}else if(w.status == OBJECT_HIT){				//子弹被飞镖击中的时候
				tempy = w.mapy;
				tempx = w.mapx;
				if(w.frame<3){					//画出子弹被击中后粉碎的效果
					w.frame = w.frame + 1;
					g.drawRegion(boomEffect, w.frame*boomEffect.getWidth()/4, 0, boomEffect.getWidth()/4, boomEffect.getHeight(), 0,
							tempx, tempy, 20);
				}else{
					booms.removeElement(w);
				}
			}
		}
		g.setClip(0, 0, ScrW, ScrH);
	}
	
	/*创建防狼套装*/
	public void createProtect(Role player) {
       Weapon w = new Weapon();
       w.id = playerProtected;
       w.mapy = player.mapy + 30;
       w.mapx = player.mapx;
       w.height = 33;
       w.width = 32;
       protects.addElement(w);
	}
	
	/*画出防狼套装*/
	public void showProtect(SGraphics g,Role role){
		Image shield = Resource.loadImage(Resource.id_prop_3);
		Weapon w = null;
		int tempx = 0,tempy = 0;
		for(int i = protects.size() - 1;i >= 0;i --){
			w = (Weapon)protects.elementAt(i);
			if(StateGame.protectState){
				tempy = role.mapy+27;
				tempx = w.mapx;
				g.drawRegion(shield, 0, 0, shield.getWidth(), shield.getHeight(), 0, tempx, tempy, 20);
			}
		}
	}
	
	/*创建激光枪*/
	public void createGlare(Role player,int direction) {
		Weapon w = new Weapon();
		w.id = glareWeapon;
		w.direction = direction;
		w.mapy  = player.mapy +24-25;
		w.mapx = player.mapx - 22;
		w.height = 87;				//光波效果的高度
		w.speedX = 40;
		w.isUse = false;
		glares.addElement(w);
	}
	
	/*显示激光枪*/
	public void showGlare(SGraphics g,Role player,Batches batches) {
		Image glare = Resource.loadImage(Resource.id_prop_4);
		Image glareEffect = Resource.loadImage(Resource.id_prop_4_effect);
		Weapon w = null;
		int tempx = 0,tempy = 0, glareW = glareEffect.getWidth() / 8, glareH = glareEffect.getHeight();
		for (int i = glares.size() - 1; i >= 0; i--) {
			w = (Weapon) glares.elementAt(i);
			tempy = player.mapy + 50 - 20;
			tempx = player.mapx - 34 + 5;
			g.drawImage(glare, tempx, tempy, 20);
			w.mapx -= w.speedX;
			tempx = w.mapx;
			tempy = w.mapy;
			w.frame = (w.frame + 1) % 8;
			w.height = glareEffect.getHeight();
			if (w.width + w.speedX >= glareW) {
				w.width = glareW;
			} else {
				w.width += w.speedX;
			}
			g.drawRegion(glareEffect, w.frame * glareW, 0, w.width, glareH, 0, tempx, tempy, 20);
		}
	}
	
	/*显示激光枪燃烧效果*/
	/*public void showGlareEffect(SGraphics g,Batches batches) {
		Image burnEffect = Resource.loadImage(Resource.id_burn);
		for(int j = batches.npcs.size() - 1;j>=0;j--){
			Role npc = (Role)batches.npcs.elementAt(j);
			if(StateGame.isHitted){
				npc.frame = (npc.frame+1)%8;
				npc.status = ROLE_DEATH;
				g.drawRegion(burnEffect, npc.frame*burnEffect.getWidth()/8, 0, burnEffect.getWidth()/8, burnEffect.getHeight(), 
						0, npc.mapx, npc.mapy, 20);
				batches.npcs.removeElement(npc);
			}
		}
	}*/
	
	/*创建驱散竖琴*/
	public void createHarp(Role player){
		Weapon w = new Weapon();
		w.id = harpWeapon;
		w.mapx = 444;
		w.mapy = 156;
		harps.addElement(w);
	}
	
	/*显示驱散竖琴的效果*/
	public void showHarp(SGraphics  g, Batches batches) {
		Image harpEffect = Resource.loadImage(Resource.id_prop_5_effect);
		Weapon w = null;
		for(int i = harps.size() - 1;i>=0;i --){
			w = (Weapon)harps.elementAt(i);
			w.frame = (w.frame+1)%5;
			for(int j = batches.npcs.size() - 1;j>=0;j--){
				Role npc = (Role)batches.npcs.elementAt(j);
				if(StateGame.harpState){
					if(npc.position == ON_ONE_LADDER || npc.position ==ON_TWO_LADDER 
							|| npc.position == ON_THREE_LADDER || npc.position == ON_FOUR_LADDER){
						npc.status = ROLE_DEATH;
						StateGame.HASWOLF_ONE = false;
						StateGame.HASWOLF_TWO = false;
						StateGame.HASWOLF_THREE = false;
						StateGame.HASWOLF_FOUR = false;
						batches.npcs.removeElement(npc);
					}
					g.drawRegion(harpEffect, w.frame*harpEffect.getWidth()/5, 0, harpEffect.getWidth()/5, harpEffect.getHeight(), 
							0, 425, 55, 20);
				}
			}
		}
	}
	/*
	显示强力磁石效果
	private int magFlag;
	public void showMagnetEffect(SGraphics g,Batches batches) {
		Image magnetEffect = Resource.loadImage(Resource.id_prop_7_effect);
		int effectW = magnetEffect.getWidth()/2, effectH = magnetEffect.getHeight();
		for(int j = batches.npcs.size() - 1;j>=0;j--){
			Role npc = (Role)batches.npcs.elementAt(j);
			if(npc.status2 == ROLE_IN_AIR && npc.status != ROLE_SUCCESS&& npc.mapy>30 && StateGame.magnetState){
				if (magFlag <= 10) {		//调整帧数大小---10-17
					magFlag++;
					npc.frame=0;
				} else {
					npc.frame = 1;
					magFlag = 0;
				}
				System.out.println("frame:"+npc.frame);
				g.drawRegion(magnetEffect, npc.frame*effectW, 0, effectW, effectH, 0, npc.mapx, npc.mapy, 20);
				if(npc.frame==1){
					stateGame.hitWolf(npc);
					//batches.npcs.removeElement(npc);
				}
			}
		}
	}*/

	/*水果*/
	public void createFruit(Role redWolf){
		Weapon fruit = new Weapon();
		fruit.status = OBJECT_NOT_HIT;
		fruit.width = 35;
		fruit.height = 45;
		fruit.speedX = 3;
		fruit.speedY = 10;
		fruit.mapy = redWolf.mapy;
		fruit.mapx = redWolf.mapx;
		fruit.id = selectFruit[RandomValue.getRandInt(selectFruit.length)];	
		fruit.scores = getScores(fruit.id);
		fruits.addElement(fruit);
	}
	
	/*根据id获取对应的积分*/
	private int getScores(int id){
		if(id == apple){
			return 100;
		}else if(id == lemon){
			return 50;
		}else if(id == pear){
			return 200;
		}else {
			return 500;
		}
	}
	
	public void showFruit(SGraphics g){
		g.setClip(0, 0, gameW, ScrH);
		Image fruitImg = null;
		Weapon w = null;
		int tempx = 0,tempy = 0;
		for(int i = fruits.size() - 1;i>=0;i --){
			w = (Weapon)fruits.elementAt(i);
			fruitImg = Resource.loadImage(w.id);
			int fruitW = fruitImg.getWidth()/3, fruitH = fruitImg.getHeight();
			if(w.status == OBJECT_NOT_HIT){
				w.mapy += w.speedY;
				w.mapx += w.speedX;
				tempy = w.mapy;
				tempx = w.mapx;
				g.drawRegion(fruitImg, 0, 0, fruitW, fruitH, 0, tempx, tempy, 20);
			}else{
				tempy = w.mapy;
				tempx = w.mapx;
				if(w.frame<2){
					w.frame = w.frame+1;
					g.drawRegion(fruitImg, w.frame*fruitW, 0, fruitW, fruitH, 0, tempx, tempy, 20);
				}else{
					fruits.removeElement(w);
				}
			}
		}
		g.setClip(0, 0, ScrW, ScrH);
	}
	
	/*创建无敌拳套*/
	public void createGloves(Role own, int position, int offY) {
		Weapon w = new Weapon();
		w.position = position;
		w.isUse =  false;
		w.mapx = own.mapx;
		w.mapy = offY;					
		w.height = 32;
		w.width = 129;
		w.speedX = 15;
		w.speedY = 10;
		w.hitNum = 0;
		gloves.addElement(w);
	}
	
	/*显示拳套生成*/
	int gloveF;
	public void showGloveCreate(SGraphics g, Role own){
		Image glove = Resource.loadImage(Resource.id_prop_fist);
		Image gloveEffectLeft = Resource.loadImage(Resource.id_gloveLeft);
		//Image gloveEffectRight = Resource.loadImage(Resource.id_gloveRight);
		int gloveW = gloveEffectLeft.getWidth()/4, gloveH = gloveEffectLeft.getHeight();
		if(stateGame.isShowGlove){
			if(gloveF<2){
				gloveF++;
				g.drawImage(glove, 368, 163, 20);
			}else{
				gloveF=0;
			}
		}else if(stateGame.isUseGlove){
			//g.drawRegion(gloveEffectRight, 0*gloveW, 0,	gloveW, gloveH, 0, own.mapx - 13, own.mapy + 24, 20);
			g.drawRegion(gloveEffectLeft, 0, 0, gloveW, gloveH, 0, own.mapx - 12, own.mapy + 30, 20);
		}
		
	}
	
	public void showGloves(SGraphics g,Role player) {			
		g.setClip(0, 0, gameW, ScrH);
		Image gloveEffectLeft = Resource.loadImage(Resource.id_gloveLeft);
		Image gloveEffectRight = Resource.loadImage(Resource.id_gloveRight);
		Weapon w = null;
		int gloveW = gloveEffectRight.getWidth()/4, gloveH = gloveEffectRight.getHeight();
		for (int i = gloves.size() - 1; i >= 0; i--) {
			w = (Weapon) gloves.elementAt(i);		
			w.frame = (w.frame + 1)%4;
			if(w.frame==0){
				w.frame=1;
			}
			if(w.position==0){
				if(w.mapx >= 300){//拐点   
					w.mapx -= w.speedX;
					w.mapy -= w.speedY;
					g.drawRegion(gloveEffectRight, w.frame*gloveW, 0,
							gloveW, gloveH, 0, w.mapx, w.mapy, 20);
				}else{
					w.mapx -= w.speedX;
					w.mapy += w.speedY;
					g.drawRegion(gloveEffectRight, w.frame*gloveW, 0,
							gloveW, gloveH, 0, w.mapx, w.mapy, 20);
				}
			}else{
				if (w.mapx >= 300) {
					w.mapx -= w.speedX;
					w.mapy += w.speedY;
					g.drawRegion(gloveEffectLeft,w.frame * gloveW, 0,gloveW, gloveH, 0, w.mapx, w.mapy,20);
				} else {
					w.mapx -= w.speedX;
					w.mapy -= w.speedY;
					g.drawRegion(gloveEffectLeft,w.frame * gloveW, 0,gloveW, gloveH, 0, w.mapx, w.mapy,20);
				}
			}

		}
		g.setClip(0, 0, ScrW, ScrH);
	}
	
	/*清除内存中的对象*/
	public void clearObjects() {
      bombs.removeAllElements();
      nets.removeAllElements();
      booms.removeAllElements();
      protects.removeAllElements();
      glares.removeAllElements();
      harps.removeAllElements();
      fruits.removeAllElements();
      gloves.removeAllElements();
	}
	
	public void printSize(){
		System.out.println("bombs.size:"+bombs.size());
		System.out.println("nets.size:"+nets.size());
		System.out.println("booms.size:"+booms.size());
		System.out.println("protects.size:"+protects.size());
		System.out.println("glares.size:"+glares.size());
		System.out.println("harps.size:"+harps.size());
		System.out.println("fruits.size:"+fruits.size());
		System.out.println("gloves.size:"+gloves.size());
	}
}
