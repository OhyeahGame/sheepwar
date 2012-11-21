package sheepwar;

import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.game.SGraphics;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;
import cn.ohyeah.stb.res.UIResource;
import cn.ohyeah.stb.ui.DrawUtil;
import cn.ohyeah.stb.ui.PopupConfirm;
import cn.ohyeah.stb.ui.PopupText;
import cn.ohyeah.stb.util.Collision;
import cn.ohyeah.stb.util.RandomValue;

public class StateGame implements Common{
	
	public static Exploder[] exploders = new Exploder[12];
	public static Exploder[] ss = new Exploder[12];
	public static Exploder[] mf = new Exploder[12];
	public static Exploder[] sprop = new Exploder[12];
//	public SGraphics g ;
	
	/*从下往上判断四个梯子上是否有狼，从右到左*/
	public static boolean HASWOLF_ONE;
	public static boolean HASWOLF_TWO;
	public static boolean HASWOLF_THREE;
	public static boolean HASWOLF_FOUR;
	
	/*判断梯子上是否都有狼*/
	public static boolean IS_FOUR_WOLF;
	public static boolean isGameOver;
	public static boolean isSuccess;
	
	private SheepWarGameEngine engine;
	public StateGame(SheepWarGameEngine engine){
		this.engine = engine;
	}
	
	public CreateRole createRole;
	public Batches batches;
	public Weapon weapon;
	public static Role own; 
	
	public static Role npc;
	private int eIndex, sIndex, mIndex, pIndex;

	/*游戏关卡*/
	public static short level = 1; 
	/*奖励关卡*/
	public static short rewardLevel = 1;
	
	public static boolean isRewardLevel, isReward;
	
	/*奖励关卡逃脱的狼数*/
	public static int reward_nums;
	
	/*当前关卡狼出现的批次*/
	public static short batch;
	
	/*奖励关卡被攻击时失败标记 true为被攻击，false没被攻击*/
	public static boolean rewardLevelFail;
	
	/*关卡信息*/
	public static int[][] LEVEL_INFO = {
		/*0-关卡，1-该关卡击中狼的数量， 2-每批狼出现的间隔时间（秒），3-该关卡狼的位置（0-上面, -1-下面）*/
		{1, 32, 3, 0},  	//第一关
		{2, 36, 3, -1},  	//第二关
		{3, 40, 3, 0},  	//第三关
		{4, 44, 3, -1},  	//第四关
		{5, 48, 3, 0},  	//第五关
		{6, 52, 3, -1},  
		{7, 56, 2, 0},  
		{8, 60, 2, -1},  
		{9, 64, 1, 0},  
		{10, 68, 1, -1},  
		{11, 72, 3,0},  
		{12, 76, 3, -1},  
		{13, 80, 2, 0},  
		{14, 84, 2, -1},  
		{15, 88, 2, 0},  
		{16, 0, 5, -1},  //预留
	};
	
	/*奖励关卡信息*/
	public static int [][] REWARD_LEVEL_INFO = {
		/*0-关卡，1-该关卡击中狼的数量， 2-每批狼出现的间隔时间（秒），3-该关卡狼的位置（0-上面, -1-下面）*/
		{1,16,3,-1},			//奖励关卡一
		{2,16,3,-1},			//奖励关卡二
		{3,16,3,-1},			//奖励关卡三
		{4,16,3,-1},			
		{5,16,3,-1},			
		{6,16,2,-1},			
		{7,16,2,-1},			
	};

	/*游戏通关时间*/
	public long gamePassTime, gamePassTime2;
	
	/*游戏过度时间*/
	private long gameBufferTimeS, gameBufferTimeE;
	private boolean isNextLevel;
	
	/*控制子弹发射的变量*/
	private long startTime, endTime;
	private boolean isAttack = true;
	private int bulletInterval = 1500;  //单位毫秒
	
	/*两连发*/
	//private long repeatingT;
	private int repeatingInterval = 200; //单位毫秒
	private boolean isRepeating;
	
	/*四连发*/
	private long repeatingSTime, repeatingETime;
	private int repeatingInterval2 = 100;	//单位毫秒
	public static boolean isFourRepeating;
	public static boolean second;
	private boolean third;
	private boolean fourth;
	
	/*时光闹钟*/
	public static boolean pasueState, isUsePasue;
	public static long pasueTimeS,  pasueTimeE;
	private int pasueInterval = 10; 	//单位秒
	public static int pasueValideTime;	//道具剩余有效时间
	
	/*加速*/
	//public static boolean speedFlag;
	//private long addSpeedTime,addSpeedTime2;
	//private int speedLiquidInterval = 30;
	
	/*防狼套装*/
	public static long proEndTime;
	public static long proStartTime;
	private int protectInterval = 30;
	public static boolean protectState; //使用了防狼道具
	public static int protectValideTime; //道具剩余有效时间
	
	/*驱狼竖琴*/
	private long harpStartTime,harpEndTime;
	private int harpInterval = 3;				//便于控制驱散竖琴的效果
	public static boolean harpState;
	
	/*强力磁石*/
	private long magnetStartTime,magnetEndTime;
	private int magnetInterval = 1;
	public static boolean magnetState;
	
	/*激光枪状态*/
//	public static boolean glareState,isHitted = false;

	/*无敌拳套*/
	public static boolean isUseGlove, isShowGlove, golveFlag=true;
	public static long gloveEndTime, gloveStartTime;
	public static int gloveValideTime;
	private int gloveInterval = 20;
	
	public boolean down;	//南瓜是否降落
	
	/*玩家存档数据*/
	public static int lifeNum;		//生命数
	public static int scores;		//积分
	public static int scores2;		//单关积分
	public static int hitNum;		//单关击中狼的数
	public static int hitTotalNum;	//击中狼的总数
	public static int hitBuble;		//击中的气球数
	public static int useProps;		//使用的道具数
	public static int hitFruits;	//击中的水果数
	public static int hitRatio;		//击中目标数
	public static int hitBooms;		//击中子弹数
	public static int attainment;	//成就数
	
	public static int scores3;		//成就中的积分
	public static int level2;		//成就中的关卡
	public static int useProps2;	//成就中使用的道具数
	public static int hitFruits2;	//成就中击中的水果数
	public static int hitTotalNum2;	//成就中击中狼的总数
	public static int hitBooms2;	//成就中击中子弹数
	
	public static boolean a=true, b, c ,d ,e;	//教学关卡弹出提示
	public static boolean  b2, c2 ,d2 ,e2;	   //教学关卡弹出提示
	public static boolean  c3 ,d3;	   //教学关卡弹出提示
	private long bStartTime, bEndTime,cStartTime, cEndTime, dStartTime, dEndTime, eStartTime, eEndTime;
	
	private int tempx=ScrW, tempy=20, tempx2=ScrW, tempy2=30, sWidth, sTempy;
	
	public void handleKey(KeyState keyState){
		if (keyState.containsMoveEventAndRemove(KeyCode.UP)) {
			moveRole(0);
			
		} else if (keyState.containsMoveEventAndRemove(KeyCode.DOWN)) {
			moveRole(1);
			
		} else if (keyState.containsAndRemove(KeyCode.OK)&& own.status ==ROLE_ALIVE) {	
			if(isUseGlove){
				weapon.createGloves(own, 0,own.mapy + 4);	//上面拳套
				weapon.createGloves(own, 1,own.mapy + 30);	//下面拳套
				isUseGlove = false;
				golveFlag = true;
				gloveStartTime = System.currentTimeMillis() / 1000;
			}else if(isAttack){ //普通攻击
				weapon.createBomb(own, Weapon.WEAPON_MOVE_LEFT);
				own.bombNum ++;
				//if(own.bombNum%2==0){
					isAttack = false;
					startTime = System.currentTimeMillis();
				//}
				if(!isFourRepeating){  //判断是否用了四连发道具,
					isRepeating = true;	 //没有用就是普通的两连发
				}else{
					second = true;
				}
				repeatingSTime = System.currentTimeMillis();
			}
			
		}else if(keyState.containsAndRemove(KeyCode.NUM1)&& own.status ==ROLE_ALIVE){    	//时光闹钟
			int propId = engine.pm.propIds[0]-53;
			if(engine.props[propId].getNums()>0 || engine.isDebugMode()){
				pasueState = true;
				isUsePasue = true;
				pasueTimeS = System.currentTimeMillis()/1000;
				if(!engine.isDebugMode()){
					updateProp(propId);
				}
				createPromptProp(propId);
			}
		}else if(keyState.containsAndRemove(KeyCode.NUM3)&& own.status ==ROLE_ALIVE){ 		//捕狼网
			int propId = engine.pm.propIds[1]-53;
			if(engine.props[propId].getNums()>0 || engine.isDebugMode()){
				weapon.createNet(own, Weapon.WEAPON_MOVE_LEFT);
				if(!engine.isDebugMode()){
					updateProp(propId);
				}
				createPromptProp(propId);
			}
		}else if(keyState.containsAndRemove(KeyCode.NUM5)&& own.status ==ROLE_ALIVE){		//盾牌
			int propId = engine.pm.propIds[2]-53;
			if(engine.props[propId].getNums()>0 || engine.isDebugMode()){
				protectState = true;
				//weapon.createProtect(own);
				proStartTime = System.currentTimeMillis()/1000;
				if(!engine.isDebugMode()){
					updateProp(propId);
				}
			}
		}else if(keyState.containsAndRemove(KeyCode.NUM7)&& own.status ==ROLE_ALIVE){		//激光枪
			int propId = engine.pm.propIds[3]-53;
			if(engine.props[propId].getNums()>0 || engine.isDebugMode()){
				weapon.createGlare(own,Weapon.WEAPON_MOVE_LEFT);
//				glareState = true;
				if(!engine.isDebugMode()){
					updateProp(propId);
				}
			}
		}else if(keyState.containsAndRemove(KeyCode.NUM2)&& own.status ==ROLE_ALIVE){		//驱散竖琴
			int propId = engine.pm.propIds[4]-53;
			if(engine.props[propId].getNums()>0 || engine.isDebugMode()|| SheepWarGameEngine.isFirstGame){
				harpState = true;
				//weapon.createHarp(own);
				harpStartTime = System.currentTimeMillis()/1000;
				if(!engine.isDebugMode() && !SheepWarGameEngine.isFirstGame){
					updateProp(propId);
				}
				createPromptProp(propId);
				if(SheepWarGameEngine.isFirstGame){
					d3 = false;
				}
			}
		}else if(keyState.containsAndRemove(KeyCode.NUM4)&& own.status ==ROLE_ALIVE){		//四连发道具
				//if(!speedFlag){
					int propId = engine.pm.propIds[5]-53;
					if(!isFourRepeating && engine.props[propId].getNums()>0 || engine.isDebugMode() || SheepWarGameEngine.isFirstGame){
						//own.speed = own.speed + CreateRole.para[4];
						//speedFlag = true;
						//addSpeedTime = System.currentTimeMillis()/1000;
						isFourRepeating = true;
						if(!engine.isDebugMode() && !SheepWarGameEngine.isFirstGame){
							updateProp(propId);
						}
						createPromptProp(propId);
						if(SheepWarGameEngine.isFirstGame){
							c3 = false;
						}
					}
				//}
		}else if(keyState.containsAndRemove(KeyCode.NUM6)&& own.status ==ROLE_ALIVE){		//强力磁石
			int propId = engine.pm.propIds[6]-53;
			if(engine.props[propId].getNums()>0 || engine.isDebugMode()){
				magnetStartTime = System.currentTimeMillis()/1000;
				magnetState = true;
				if(!engine.isDebugMode()){
					updateProp(propId);
				}
				createPromptProp(propId);
			}
		}else if(keyState.containsAndRemove(KeyCode.NUM8) && own.status ==ROLE_ALIVE){		//木偶->可以增加一条生命
			int propId = engine.pm.propIds[7]-53;
			if(engine.props[propId].getNums()>0 || engine.isDebugMode()){
				own.lifeNum ++;
				lifeNum = own.lifeNum;
				if(!engine.isDebugMode()){
					updateProp(propId);
				}
				createPromptProp(propId);
			}
		}else if(keyState.containsAndRemove(KeyCode.NUM9)){		//暂停							
			
		}else if (keyState.containsAndRemove(KeyCode.NUM0 | KeyCode.BACK)){ 	//返回
			if(SheepWarGameEngine.isFirstGame){
				SheepWarGameEngine.isFirstGame = false;
				initDataGameOver();
				engine.state = STATUS_MAIN_MENU;
				clear();
			}else{
				StateSubMenu sm = new StateSubMenu();
				int index = sm.processSubMenu();
				if(index == 0){		//返回游戏
					
				}else if(index == 1){
					engine.queryBalance();
					StateShop ss =  new StateShop(engine);
					ss.processShop();
				}else if(index == 2){
					StateHelp sh = new StateHelp();
					sh.processHelp();
				}else if(index == 3){
					System.out.println("退出游戏");
					printInfo();
					
					//同步道具
					engine.pm.sysProps();
					
					//退出游戏保存逃脱的狼
					setWolfStatus();
					
					try {
						//保存数据
						engine.saveAttainment();
					} catch (Exception e) {
						System.out.println("保存信息失败："+e.getMessage());
					}
					try {
						engine.reportScores();
					} catch (Exception e) {
						System.out.println("上报积分失败："+e.getMessage());
					}
					
					//保存游戏记录
					engine.saveRecord("退出游戏时保存游戏记录",true);
					
					/*更新玩家成就*/
					engine.updateAttainmen();
					
					initDataGameOver();
					engine.state = STATUS_MAIN_MENU;
					clear();
				}
				
				/*时间重置，暂停后要恢复道具的有效时间*/
				if(pasueState && isUsePasue){		//时光闹钟
					long t1 = pasueTimeE-pasueTimeS;
					long t2 = System.currentTimeMillis()/1000;
					pasueTimeS = t2-t1;
				}
				
				if(protectState){					//防狼道具
					long t3 = proEndTime-proStartTime;
					long t4 = System.currentTimeMillis()/1000;
					proStartTime = t4-t3;
				}
				
				if(!isUseGlove && golveFlag){
					long t5 = gloveEndTime-gloveStartTime;
					long t6 = System.currentTimeMillis()/1000;
					gloveStartTime = t6-t5;
				}
			}
		}
	}
	
	private void setWolfStatus() {
		int count=0;
		for(int j=batches.npcs.size()-1;j>=0;j--){
			Role wolf = (Role) batches.npcs.elementAt(j);
			if(wolf.status == ROLE_SUCCESS){
				count++;
			}
		}
		if(count==1){
			HASWOLF_ONE = true;
		}else if(count==2){
			HASWOLF_ONE = true;
			HASWOLF_TWO = true;
		}else if(count==3){
			HASWOLF_ONE = true;
			HASWOLF_TWO = true;
			HASWOLF_THREE = true;
		}else if(count == 4){
			HASWOLF_ONE = true;
			HASWOLF_TWO = true;
			HASWOLF_THREE = true;
			HASWOLF_FOUR = true;
		}
	}

	private void updateProp(int propId){
		engine.props[propId].setNums(engine.props[propId].getNums()-1);
		own.useProps++;
		useProps = own.useProps;
		own.scores += 1000;  //使用道具加1000分
		own.scores2 += 1000;
		scores = own.scores;
		scores2 = own.scores2;
	}
	
	public void show(SGraphics g){
		drawGamePlaying(g);
		createRole.showSheep(g,own);
		batches.showBuble(g);
//		if(weapon.id != 4){
			batches.showWolf(g, weapon);
//		}
		weapon.showFruit(g);
		weapon.showBomb(g);
		weapon.showBoom(g,own);			
		weapon.showNet(g);
		weapon.showProtect(g, own);
		weapon.showGlare(g, own, batches);
		if(batches.redWolf!=null){
			batches.showRedWolf(g,weapon);				
		}
		weapon.showGloveCreate(g, own);
		weapon.showGloves(g, own);
		
		//显示动态效果
		showDynamic(g);
		
		//显示道具持续时间
		showTime(g);
		
		/*教学关卡*/
		teachingLevel();
	}
	
	/*教学关卡*/
	private void teachingLevel(){
		if(SheepWarGameEngine.isFirstGame){
			bEndTime = System.currentTimeMillis()/1000;
			cEndTime = System.currentTimeMillis()/1000;
			dEndTime = System.currentTimeMillis()/1000;
			eEndTime = System.currentTimeMillis()/1000;
			PopupText pt = UIResource.getInstance().buildDefaultPopupText();
			if(a){	//教学关卡第一个提示
				pt.setText("欢迎进入教学关卡，按确认键#W继续教学，按0键#W退出教学关卡。");
				pt.popup();
				a = false;
				b2 = true;
				bStartTime = System.currentTimeMillis()/1000;
			}
			if(b){
				pt.setText("按上下键#W移动喜羊羊，按确认键#W发射飞镖，击中灰太狼身上的气球阻止灰太狼降落");
				pt.popup();
				b = false;
				b2 = false;
				c2 = true;
				cStartTime = System.currentTimeMillis()/1000;
			}
			if(c){
				pt.setText("想让飞镖的威力更大吗？按数字键4#W使用道具连发。");
				pt.popup();
				c = false;
				c2 = false;
				c3 = true;
				d2 = true;
				dStartTime = System.currentTimeMillis()/1000;
			}
			if(d){
				pt.setText("梯子上爬满灰太狼会威胁到喜羊羊的安全。按数字键2#W使用道具驱狼竖琴清除掉成功降落的灰太狼。");
				pt.popup();
				d = false;
				d2 = false;
				d3 = true;
				e2 = true;
				eStartTime = System.currentTimeMillis()/1000;
			}
			if(e){
				pt.setText("恭喜你，你已经可以独自面对接下来的挑战了。按数字键#W使用它们。记住，道具没有了可以去商城去购买。");
				pt.popup();
				e = false;
				e2 = false;
				SheepWarGameEngine.isFirstGame = false;
				initDataGameOver();
				gamePassTime = System.currentTimeMillis()/1000;
				weapon = new Weapon(this);
				createRole = new CreateRole();
				batches = new Batches();
				own = createRole.createSheep();
				engine.state = STATUS_GAME_PLAYING;
				//clear();
			}
			if(e2 && (eEndTime-eStartTime)>10){
				e = true;
			}
			if(d2 && (dEndTime-dStartTime)>10){
				d = true;
			}
			if(c2 && (cEndTime-cStartTime)>7){
				c = true;
			}
			if(b2 && (bEndTime-bStartTime)>5){
				b = true;
			}
		}
	}
	
	private void showDynamic(SGraphics g) {
		Exploder exploder = null;
		for(int i=0;i<exploders.length;i++){
			if(exploders[i] != null){
				exploder = exploders[i];
				exploder.drawExplode(g, this);
			}
		}
		
		for(int i=0;i<ss.length;i++){
			if(ss[i] != null){
				exploder = ss[i];
				exploder.showScore(engine, g, exploder.score);
			}
		}
		
		for(int i=0;i<mf.length;i++){
			if(mf[i] != null){
				exploder = mf[i];
				exploder.showMagnetEffect(g);
			}
		}
		
		for(int i=0;i<sprop.length;i++){
			if(sprop[i] != null){
				exploder = sprop[i];
				exploder.showUseProp(g, exploder.propId);
			}
		}
	}

	private void showTime(SGraphics g) {
		Image prop = Resource.loadImage(Resource.id_prop);
		engine.setFont(30, true);
		int col = g.getColor();
		g.setColor(0xff0000);
		
		int propW = prop.getWidth()/8, propH = prop.getHeight();
		int mapx = 370, mapx2 = 370, mapx3 = 430, mapy = 46, offX, offX2;
		/*时光闹钟持续时间*/
		if(pasueState && isUsePasue){
			if(isFourRepeating && protectState){
				mapx = 310;
			}else if(isFourRepeating && !protectState){
				mapx = 370;
			}else if(!isFourRepeating && !protectState){
				mapx = 430;
			}else if(!isFourRepeating && protectState){
				mapx = 370;
			}
			if(pasueInterval-(pasueTimeE-pasueTimeS)<10){
				offX = 15;
			}else{
				offX = 5;
			}
			g.drawRegion(prop, 0, 0, propW, propH, 0, mapx, 4, 20);
			drawNum(g, Integer.parseInt(String.valueOf(pasueInterval-(pasueTimeE-pasueTimeS))), mapx+offX, mapy);
			//g.drawString(String.valueOf(pasueInterval-(pasueTimeE-pasueTimeS)), mapx, mapy, 20);
		}
		
		/*防狼套装的时间控制*/
		if(protectState){
			if(isFourRepeating && pasueState){
				mapx2 = 370;
			}else if(isFourRepeating && !pasueState){
				mapx2 = 370;
			}else if(!isFourRepeating && !pasueState){
				mapx2 = 430;
			}else if(!isFourRepeating && pasueState){
				mapx2 = 430;
			}
			if(pasueInterval-(pasueTimeE-pasueTimeS)<10){
				offX2 = 10;
			}else{
				offX2 = 5;
			}
			g.drawRegion(prop, 2*propW, 0, propW, propH, 0, mapx2, 5, 20);
			drawNum(g, Integer.parseInt(String.valueOf(protectInterval-(proEndTime - proStartTime))), mapx2+offX2, mapy);
			//g.drawString(String.valueOf(protectInterval-(proEndTime - proStartTime)), mapx+55, mapy, 20);
		}
		
		if(isFourRepeating){
			g.drawRegion(prop, 5*propW, 0, propW, propH, 0, mapx3, 5, 20);
		}
		
		engine.setDefaultFont();
		g.setColor(col);
	}

	public void execute(){
		
		/*游戏通关时间*/
		gamePassTime2 = System.currentTimeMillis()/1000;
		
		/*道具使用提示*/
		promptUseProp();
		
		/*控制子弹发射间隔*/
		endTime = System.currentTimeMillis(); 
		if(isAttack==false && endTime-startTime>=bulletInterval){
			isAttack = true;
		}
		
		/*两连发第二颗子弹*/
		if(isRepeating && endTime-startTime>=repeatingInterval){
			weapon.createBomb(own, Weapon.WEAPON_MOVE_LEFT);
			own.bombNum ++;
			isRepeating = false;
		}
		
		repeatingETime = System.currentTimeMillis();
		/*四连发道具--第二个子弹*/
		if(isFourRepeating && second && repeatingETime-repeatingSTime>=repeatingInterval2){
			weapon.createBomb(own, Weapon.WEAPON_MOVE_LEFT);
			own.bombNum ++;
			third = true;
			second = false;
			repeatingSTime = System.currentTimeMillis();
		}
		/*四连发道具--第三个子弹*/
		if(isFourRepeating && third && repeatingETime-repeatingSTime>=repeatingInterval2){
			weapon.createBomb(own, Weapon.WEAPON_MOVE_LEFT);
			own.bombNum ++;
			fourth = true;
			third = false;
			repeatingSTime = System.currentTimeMillis();
		}
		/*四连发道具--第四个子弹*/
		if(isFourRepeating && fourth && repeatingETime-repeatingSTime>=repeatingInterval2){
			weapon.createBomb(own, Weapon.WEAPON_MOVE_LEFT);
			own.bombNum ++;
			fourth = false;
			repeatingSTime = System.currentTimeMillis();
		}
		
		
		/*控制拳套时间间隔*/
		gloveEndTime = System.currentTimeMillis()/1000;
		if(!SheepWarGameEngine.isFirstGame && !isRewardLevel && golveFlag && (gloveEndTime  - gloveStartTime >= gloveInterval)){
			isShowGlove = true;
			golveFlag = false;
		}
		if(isShowGlove && own.mapy<=190){
			isUseGlove = true;
			isShowGlove = false;
		}
		
		/*时光闹钟效果时间*/
		pasueTimeE = System.currentTimeMillis()/1000;
		if(isUsePasue && pasueState && (pasueTimeE-pasueTimeS)>=pasueInterval){
			pasueState = false;
			isUsePasue = false;
		}
		
		/*加速效果时间*/
		//addSpeedTime2 = System.currentTimeMillis()/1000;
		//if(addSpeedTime2 - addSpeedTime >speedLiquidInterval){			
		//	speedFlag = false;
		//	own.speed = CreateRole.para[4];
		//}
		
		/*防狼套装的时间控制*/
		proEndTime = System.currentTimeMillis()/1000;
		if((proEndTime - proStartTime > protectInterval)){
			protectState = false;
		}
		//死亡后暂停时间
		if(!isUsePasue && pasueState && (pasueTimeE - pasueTimeS > 3)){
			pasueState = false;
		}
		
		/*驱狼竖琴时间间隔控制*/
		harpEndTime = System.currentTimeMillis()/1000;
		if(harpEndTime - harpStartTime >harpInterval){
			harpState = false;
		}
		
		//System.out.println("magnetState:"+magnetState);
		/*强力磁石控制时间*/
		magnetEndTime = System.currentTimeMillis()/1000;
		if(magnetEndTime - magnetStartTime > magnetInterval){
			magnetState = false;
		}
		/*
		激光枪状态控制
		glareState = false;*/
		
		/*游戏过度时间*/
		gameBufferTimeE = System.currentTimeMillis()/1000;
		
		/*创建一批狼*/
		createNpc();
		
		/*创建狼*/
		createRedNpc();
		
		/*关卡过关判断*/
		isNextLevel();  			
		
		/*普通攻击碰撞检测*/
		bombAttackNpcs();
		
		/*无敌拳套碰撞检测*/
		gloveAttackNpcs();
		
		/*捕狼道具碰撞检测*/
		netAttackNpcs();
		
		/*狼的普通攻击碰撞检测*/
		boomAttackPlayer();
		
		/*激光枪碰撞检测*/
		glareAttackNpcs();
		
		/*重力磁石*/
		magnetEffect();
		
		/*移除死亡对象*/
		removeDeath();

		/*判断4个位置上是否都有狼*/
		checkFourPosition();
		
		/*第四关之后的双数关卡创建气球*/
		createBubles();
		
		/*判断游戏成功或失败*/
		gameSuccessOrFail();
		gameOver();
		
		/*玩家死亡逃脱的狼全部移除*/
		removeSuccessWolf();
		
		/*使用驱狼竖琴眩晕状态*/
		wolfDizzy();
	}

	private boolean isUseProp2=true, isUseProp4;
	private void promptUseProp() {
		if(!SheepWarGameEngine.isFirstGame && !isRewardLevel && level==1 && !isNextLevel){
			if(isUseProp2 && HASWOLF_ONE && HASWOLF_TWO){
				PopupConfirm pc = UIResource.getInstance().buildDefaultPopupConfirm();
				pc.setText("已经有多只狼逃脱，是否使用道具驱狼竖琴清除掉成功降落的灰太狼?");
				int index = pc.popup();
				if(index==0){
					int propId = engine.pm.propIds[4]-53;
					System.out.println("propNum:"+engine.props[propId].getNums());
					if(engine.props[propId].getNums()>0){
						harpState = true;
						harpStartTime = System.currentTimeMillis()/1000;
						updateProp(propId);
						createPromptProp(propId);
					}else{
						pc.setText("道具数量不足，是否去商城购买?");
						int i = pc.popup();
						if(i==0){
							engine.queryBalance();
							StateShop ss =  new StateShop(engine);
							ss.processShop();
						}
					}
				}
				isUseProp2 = false;
			}
		}
		if(!SheepWarGameEngine.isFirstGame && !isRewardLevel && level==2 && !isNextLevel){
			if(isUseProp4 && !isFourRepeating){
				PopupConfirm pc = UIResource.getInstance().buildDefaultPopupConfirm();
				pc.setText("是否使用连发道具，让飞镖发挥更大的威力。");
				int index = pc.popup();
				if(index==0){
					int propId = engine.pm.propIds[5]-53;
					if(engine.props[propId].getNums()>0){
						isFourRepeating = true;
						updateProp(propId);
						createPromptProp(propId);
					}else{
						pc.setText("道具数量不足，是否去商城购买?");
						int i = pc.popup();
						if(i==0){
							engine.queryBalance();
							StateShop ss =  new StateShop(engine);
							ss.processShop();
						}
					}
				}
				isUseProp4 = false;
			}
		}
	}

	private void wolfDizzy() {
		if(own!=null && own.status != ROLE_DEATH && harpState){
			for(int j=batches.npcs.size()-1;j>=0;j--){
				Role npc = (Role) batches.npcs.elementAt(j);
				if(npc.status == ROLE_SUCCESS){
					npc.status = ROLE_DIZZY;
					//System.out.println("眩晕"+npc.status);
				}
			}
			HASWOLF_ONE = false;
			HASWOLF_TWO = false;
			HASWOLF_THREE = false;
			HASWOLF_FOUR = false;
		}
	}

	private void createBubles() {
		batches.bublesETime = System.currentTimeMillis()/1000;
		if(!isRewardLevel && level>=4 && level%2==0 && (batches.bublesETime-batches.bublesSTime)>3){
			batches.createBallon();
			batches.bublesSTime = System.currentTimeMillis()/1000;
		}
	}
	
	private void createPromptProp(int propId){
		Exploder e = new Exploder(own.mapx, own.mapy, 1, propId);
		sprop[mIndex] = e;
		if(pIndex < sprop.length-1){
			pIndex ++;
		}else{
			pIndex=0;
		}
	}

	private void magnetEffect() {
		for(int j = batches.npcs.size() - 1;j>=0;j--){
			Role npc = (Role)batches.npcs.elementAt(j);
			if(npc.status2 == ROLE_IN_AIR && npc.status != ROLE_SUCCESS/*&& npc.mapy>30*/ && StateGame.magnetState){
				Exploder m = new Exploder(npc.mapx,npc.mapy);
				mf[mIndex] = m;
				if(mIndex < mf.length-1){
					mIndex ++;
				}else{
					mIndex=0;
				}
				hitWolf(npc, null);
				batches.npcs.removeElement(npc);
				print();
			}
		}
	}

	private void removeSuccessWolf() {
		if(own!=null && own.status == ROLE_DEATH){
			for(int j=batches.npcs.size()-1;j>=0;j--){
				Role npc = (Role) batches.npcs.elementAt(j);
				if(npc.status == ROLE_SUCCESS){
					batches.npcs.removeElement(npc);
				}
			}
		}
	}

	private void checkFourPosition() {
		if(IS_FOUR_WOLF){
			IS_FOUR_WOLF = false;
			if(!isRewardLevel){		//普通关卡四只狼逃脱
				if(level%2!=0){
					hitOwn(own);
				}else{
					down = true;
				}
			}else{				//奖励关卡四只狼逃脱
				down = true;
			}
			
			for(int j = batches.npcs.size() - 1;j>=0;j--){
				Role npc = (Role)batches.npcs.elementAt(j);
				if(npc.position == ON_ONE_LADDER || npc.position ==ON_TWO_LADDER 
						|| npc.position == ON_THREE_LADDER || npc.position == ON_FOUR_LADDER){
					npc.status = ROLE_DEATH;
					StateGame.HASWOLF_ONE = false;
					StateGame.HASWOLF_TWO = false;
					StateGame.HASWOLF_THREE = false;
					StateGame.HASWOLF_FOUR = false;
					batches.npcs.removeElement(npc);
				}
			}
		}
	}

	private void isNextLevel(){
		if(!isNextLevel){
			if(!isRewardLevel){  //普通关卡过关判断
				for (int i = 1; i < 16; i++) {
					if ((level == i && own.hitNum >= LEVEL_INFO[level - 1][1]) 
							|| (engine.isDebugMode() && own.hitNum>=3)) {
						System.out.println("过关");
						gameBufferTimeS = System.currentTimeMillis()/1000;
						hitNum = own.hitNum = 0;
						isNextLevel = true;
						if(level%2==0){
							isRewardLevel = true;
							System.out.println("下一关为奖励关卡");
						}
						engine.saveRecord("通过第"+level+"关时保存游戏#通关用时"+(gamePassTime2-gamePassTime)+"秒", false);
						gamePassTime = System.currentTimeMillis()/1000;
						level++;
					}
				}
			}else{	//奖励关卡过关判断
				if((rewardLevel%2==1 && (REWARD_LEVEL_INFO[rewardLevel-1][1]-own.hitNum-reward_nums<=0)/*batch >= (RewardLevelBatchesInfo[rewardLevel-1].length - 1)*/)
						||(rewardLevel%2==0 && batches.redWolf.bombNum>=16) 
						|| (engine.isDebugMode() && (batch>1 || (rewardLevel%2==0 && batches.redWolf.bombNum>1)))
						|| rewardLevelFail){
					System.out.println("奖励关卡结束");
					gameBufferTimeS = System.currentTimeMillis()/1000;
					isNextLevel = true;
					isReward = true;
					batch = 0;
					hitNum = own.hitNum = 0;
					isRewardLevel = false;
					if(rewardLevel <6){		
						rewardLevel++;
					}
					if(batches.redWolf!=null){
						batches.redWolf.bombNum = 0;
					}
				}
			}
		}

		/*进入过关界面*/
		if(level<=15 && isNextLevel==true && gameBufferTimeE-gameBufferTimeS>1){
			isNextLevel = false;
			isReward = false;
			StateNextLevel stateLevel = new StateNextLevel();
			stateLevel.processNextLevel(own);
			initDataNextLevel();	//清空数据
			isUseProp4 = true;
		}
	}
	
	private void gameSuccessOrFail() {
		if(own.lifeNum<=0 && !isGameOver){		/*游戏失败*/
			System.out.println("游戏失败:");
			isGameOver = true;
			isSuccess = false;
			gameBufferTimeS = System.currentTimeMillis()/1000;
			
			try{
				//保存数据
				engine.saveAttainment();
			}catch(Exception e){
				System.out.println("保存数据失败："+e.getMessage());
			}
			
			/*更新玩家成就*/
			engine.updateAttainmen();
		}else if(level > 15 && !isGameOver){	/*游戏通关*/
			System.out.println("游戏成功：");
			isGameOver = true;
			isSuccess = true;
			gameBufferTimeS = System.currentTimeMillis()/1000;
			//同步道具
			engine.pm.sysProps();
			
			try{
				//保存数据
				engine.saveAttainment();
			}catch(Exception e){
				System.out.println("保存数据失败："+e.getMessage());
			}
			try {
				engine.reportScores();
			} catch (Exception e) {
				System.out.println("上报积分失败："+e.getMessage());
			}
			
			/*更新玩家成就*/
			engine.updateAttainmen();
		}
	}
	
	private void gameOver(){
		//System.out.println("gameBufferTimeE-gameBufferTimeS="+(gameBufferTimeE-gameBufferTimeS));
		if(isGameOver && gameBufferTimeE-gameBufferTimeS>1){
			isGameOver=false;
			StateGameSuccessOrFail sgs = new StateGameSuccessOrFail();
			int index = sgs.processGameSuccessOrFail(isSuccess, own);
			if(index==1){//返回主界面
				engine.state = STATUS_MAIN_MENU;
				initDataGameOver();  //清空数据
				clear();
			}else{//重新游戏
				initDataGameOver();
				weapon = new Weapon(this);
				createRole = new CreateRole();
				batches = new Batches();
				own = createRole.createSheep();
				engine.state = STATUS_GAME_PLAYING;
			}
		}
	}
	
	/*无敌拳套击中npc判断*/
	private void gloveAttackNpcs() {
		for(int i=weapon.gloves.size()-1;i>=0;i--){
			Weapon boxing = (Weapon) weapon.gloves.elementAt(i);
			for(int j=batches.npcs.size()-1;j>=0;j--){
				Role npc = (Role) batches.npcs.elementAt(j);
				Role ballon = npc.role;
				if(ballon != null && npc.status == ROLE_ALIVE && npc.status2 == ROLE_IN_AIR){
					if(Collision.checkSquareCollision(boxing.mapx, boxing.mapy, boxing.width, boxing.height, ballon.mapx, ballon.mapy, ballon.width, npc.height+30/*ballon.height*/)
							&& npc.status2!=ROLE_ON_GROUND){	//当狼行至地面时，任何武器将不能杀死
						
						boxing.hitNum++;  //无敌拳套击中狼的数量加一
						hitWolf(npc,boxing);
						print();
					}
				}
			}
			
			/*射水果*/
			for(int k=weapon.fruits.size()-1;k>=0;k--){
				Weapon fruit = (Weapon) weapon.fruits.elementAt(k);
				if(Collision.checkSquareCollision(boxing.mapx, boxing.mapy, boxing.width, boxing.height, fruit.mapx, fruit.mapy, fruit.width, fruit.height)){
					hitFruit(fruit);
					print();
				}
			}
			/*射气球*/
			for(int k=batches.ballons.size()-1;k>=0;k--){
				Role buble = (Role) batches.ballons.elementAt(k);
				if(Collision.checkSquareCollision(boxing.mapx, boxing.mapy, boxing.width, boxing.height, buble.mapx, buble.mapy, buble.width, 30)){
					hitBuble(buble);
					print();
				}
			}
			
			/*击中狼发射的子弹*/
			for(int k=weapon.booms.size()-1;k>=0;k--){
				Weapon boom = (Weapon) weapon.booms.elementAt(k);
				if(Collision.checkSquareCollision(boxing.mapx, boxing.mapy, boxing.width, boxing.height,boom.mapx, boom.mapy, boom.width, boom.height)){
					hitBoom(boom);
					print();
				}
			}
			
			/*拳套出界时移除*/
			if((boxing.mapx+boxing.width <=0) || boxing.mapy >= 466){
				weapon.gloves.removeElement(boxing);
			}
		}
	}

	/*判断激光枪是否击中狼*/
 	private void glareAttackNpcs() {
		for(int i=weapon.glares.size()-1;i>=0;i--){
			Weapon glare = (Weapon) weapon.glares.elementAt(i);
			if(!glare.isUse){
				for(int j=batches.npcs.size()-1;j>=0;j--){
					Role npc = (Role) batches.npcs.elementAt(j);
					if(npc.status == ROLE_ALIVE ){
						//System.out.println("激光的宽度："+glare.width);
						if(Collision.checkSquareCollision(npc.mapx, npc.mapy, npc.width, npc.height, 
								glare.mapx, glare.mapy, glare.width, glare.height)&& npc.status2 == ROLE_IN_AIR ){
							Exploder exploder = new Exploder(npc.mapx,npc.mapy);
							exploders[eIndex] = exploder;
							if(eIndex < exploders.length-1){
								eIndex ++;
							}else{
								eIndex=0;
							}
							batches.npcs.removeElementAt(j);
							hitWolf(npc, null);
							print();
						}
					}
				}
				
				/*击中狼发射的子弹*/
				for(int k=weapon.booms.size()-1;k>=0;k--){
					Weapon boom = (Weapon) weapon.booms.elementAt(k);
					if(Collision.checkSquareCollision(boom.mapx, boom.mapy, boom.width, boom.height,glare.mapx, glare.mapy, glare.width, glare.height)){
						hitBoom(boom);
						print();
					}
				}
				
				/*射气球*/
				for(int k=batches.ballons.size()-1;k>=0;k--){
					Role buble = (Role) batches.ballons.elementAt(k);
					if(Collision.checkSquareCollision(buble.mapx, buble.mapy, buble.width, 30,glare.mapx, glare.mapy, glare.width, glare.height)){
						hitBuble(buble);
						print();
					}
				}
				
				/*射水果*/
				for(int k=weapon.fruits.size()-1;k>=0;k--){
					Weapon fruit = (Weapon) weapon.fruits.elementAt(k);
					if(Collision.checkSquareCollision(fruit.mapx, fruit.mapy, fruit.width, fruit.height,glare.mapx, glare.mapy, glare.width, glare.height)){
						hitFruit(fruit);
						weapon.fruits.removeElement(fruit);
						print();
					}
				}
			}
			/*激光出界时移除*/
			if(glare.mapx+glare.width <= 0){
				weapon.glares.removeElement(glare);
			}
		}
	}
 	
 	private void print(){
 		System.out.println("own.hitBuble:"+own.hitBuble);
 		System.out.println("own.hitFruits:"+own.hitFruits);
 		System.out.println("own.hitNum:"+own.hitNum);
 		System.out.println("own.hitTotalNum:"+own.hitTotalNum);
 		System.out.println("own.scores:"+own.scores);
 		System.out.println("own.scores2:"+own.scores2);
 		System.out.println("own.hitRatio:"+own.hitRatio);
 		System.out.println("own.lifeNum:"+own.lifeNum);
 		System.out.println("own.useProps:"+own.useProps);
 	}

	/*判断狼是否击中玩家*/
 	private void boomAttackPlayer(){
		for(int i = weapon.booms.size() - 1;i >=0;i--){
			Weapon boom = (Weapon)weapon.booms.elementAt(i);
			if(own.status == ROLE_ALIVE){
				if(Collision.checkSquareCollision(boom.mapx, boom.mapy, boom.width, boom.height, own.mapx,
						own.mapy, own.width, own.height)){
					if(!protectState && !SheepWarGameEngine.isFirstGame){			//玩家是否有防狼套装, 或不是在教学关卡
						if(isRewardLevel){
							rewardLevelFail = true;
							System.out.println("奖励关卡游戏失败");
						}else{
							hitOwn(own);
						}
					}
					weapon.booms.removeElement(boom);
				}
			}
			/*子弹出界时移除*/
			if(boom.mapx >= gameW){
				weapon.booms.removeElement(boom);
			}
		}
	}

	/*羊的捕狼网技能攻击*/
	private void netAttackNpcs() {
		for(int i=weapon.nets.size()-1;i>=0;i--){
			Weapon net = (Weapon) weapon.nets.elementAt(i);
			if(net.isUse){
				for(int j=batches.npcs.size()-1;j>=0;j--){
					Role npc = (Role) batches.npcs.elementAt(j);
					/*Role ballon = npc.role;*/			//彩色气球只能被拳套击中,如果成功着陆，则需要击落的狼增加5个灰太狼
					if(npc.status == ROLE_ALIVE	/*&& ballon.id !=multicolour*//*&& npc.status2 == ROLE_IN_AIR*/){		//为了解决当狼成功逃脱后不受攻击
						if(Collision.checkSquareCollision(npc.mapx, npc.mapy, npc.width,
								npc.height, net.mapx, net.mapy, net.width, net.height)
								&& (npc.status != ROLE_SUCCESS && npc.status2 != ROLE_ON_GROUND)){
							hitWolf(npc, null);
							print();
						}
					}
				}
				
				/*射水果*/
				for(int k=weapon.fruits.size()-1;k>=0;k--){
					Weapon fruit = (Weapon) weapon.fruits.elementAt(k);
					if(Collision.checkSquareCollision(fruit.mapx, fruit.mapy, fruit.width, fruit.height,net.mapx, net.mapy, net.width, net.height)){
						hitFruit(fruit);
						print();
					}
				}
				
				/*射气球*/
				for(int k=batches.ballons.size()-1;k>=0;k--){
					Role buble = (Role) batches.ballons.elementAt(k);
					if(Collision.checkSquareCollision(buble.mapx, buble.mapy, buble.width, 30,net.mapx, net.mapy, net.width, net.height)){
						hitBuble(buble);
						print();
					}
				}
				
				Weapon.netTimeE = System.currentTimeMillis()/1000;
				if(net.isUse && Weapon.netTimeE-Weapon.netTimeS>=Weapon.netInterval){
					net.isUse = false;
					weapon.nets.removeElement(net);
				}
			}
		}
	}

	/*创建红太狼npc*/
	private void createRedNpc(){
		if(!isNextLevel){
			if(isRewardLevel){
				//System.out.println("当前奖励关卡:"+rewardLevel);
				if(rewardLevel % 2 ==0 && batches.redWolf==null){					//偶数奖励关卡出现红太狼
					System.out.println("奖励关卡创建红太狼");
					batches.redWolf = batches.createRedWolf();
				}
			}else{
				//System.out.println("当前关卡："+level);
				if(level % 2!=0 && level != 1 && batches.redWolf==null){			//奇数关卡会有红太狼的出现
					System.out.println("普通关卡创建红太狼");
					batches.redWolf = batches.createRedWolf();
				}
			}
		}
	}
	
	private void createNpc(){
		if(isAllDown() && !isNextLevel){
			if(!isRewardLevel){
				if(engine.timePass(LEVEL_INFO[level-1][2]*1000)){
					System.out.println("普通关卡创建一批狼");
					batches.createBatches(level, batch, LEVEL_INFO[level-1][3]);
					batch = (short) ((batch+1) % BatchesInfo[level-1].length);
				}
			}else{
				if(engine.timePass(REWARD_LEVEL_INFO[rewardLevel -1][2]*1000)&&rewardLevel%2!=0){	
					System.out.println("奖励关卡创建一批狼");
					batches.createBatchesReward(rewardLevel, batch, REWARD_LEVEL_INFO[rewardLevel-1][3]);
					batch = (short)((batch+1) % RewardLevelBatchesInfo[rewardLevel-1].length);
				}
			}
		}
	}
	
	/*判断狼是否都已经下降或者上升*/
	private boolean isAllDown(){
		int len = batches.npcs.size();
		for(int i=len-1;i>=0;i--){
			Role role = (Role) batches.npcs.elementAt(i);
			if(role.status2 == ROLE_ON_GROUND){
				return false;
			}
		}
		return true;
	}
	
	private void removeDeath(){
		for(int j=batches.npcs.size()-1;j>=0;j--){
			Role npc = (Role) batches.npcs.elementAt(j);
			if(npc.status == ROLE_DEATH && npc.mapy >= 446){
				batches.npcs.removeElement(npc);
			}else if(npc.mapx>=gameW){  
				batches.npcs.removeElement(npc);
			}else if(npc.direction==ROLE_MOVE_LEFT && (npc.mapx+npc.width)<=0){
				batches.npcs.removeElement(npc);
			}
		}
		/*水果出界移除*/
		for(int k=weapon.fruits.size()-1;k>=0;k--){
			Weapon fruit = (Weapon) weapon.fruits.elementAt(k);
			if(fruit.mapx>=gameW || fruit.mapx+fruit.width <=0 || fruit.mapy>=ScrH){
				weapon.fruits.removeElement(fruit);
			}
		}
		
		/*狼的子弹*/
		/*for(int m=weapon.booms.size()-1;m>=0;m--){
			Weapon boom = (Weapon)weapon.booms.elementAt(m);
			if(boom.status==OBJECT_HIT){
				weapon.booms.removeElement(boom);
			}
		}*/
		
		/*奖励关卡逃脱的狼*/
		if(isRewardLevel){
			for(int w=batches.npcs.size()-1;w>=0;w--){
				Role r = (Role) batches.npcs.elementAt(w);
				if(r.status == ROLE_SUCCESS){
					reward_nums ++;
					batches.npcs.removeElement(r);
				}
			}
		}
		
		/*气球出界*/
		for(int n=batches.ballons.size()-1;n>=0;n--){
			Role babule = (Role) batches.ballons.elementAt(n);
			if((babule.mapy+babule.width)<=0){
				batches.ballons.removeElement(babule);
			}
		}
	}
	
	private void bombAttackNpcs(){
		for(int i=weapon.bombs.size()-1;i>=0;i--){
			Weapon bomb = (Weapon) weapon.bombs.elementAt(i);
			for(int j=batches.npcs.size()-1;j>=0;j--){
				Role npc = (Role) batches.npcs.elementAt(j);
				Role ballon = npc.role;
				if(ballon != null && npc.status == ROLE_ALIVE && npc.status2 == ROLE_IN_AIR){
					if(Collision.checkSquareCollision(bomb.mapx, bomb.mapy, bomb.width, bomb.height,
							ballon.mapx, ballon.mapy, ballon.width, 30/*ballon.height*/)){
						if(ballon.id != multicolour){
							hitWolf(npc, null);
							print();
							weapon.bombs.removeElement(bomb);
						}else{
							bomb.direction = Weapon.WEAPON_MOVE_DOWN;
							bomb.speedY = bomb.speedX + 10;
						}
					}
					else if(Collision.checkSquareCollision(bomb.mapx, bomb.mapy, bomb.width,
							bomb.height, npc.mapx, npc.mapy, npc.width, npc.height)&& npc.status != ROLE_SUCCESS){
						bomb.direction = Weapon.WEAPON_MOVE_DOWN;
						bomb.speedY = bomb.speedX + 10;
					}
				}
			}
			
			/*射水果*/
			for(int k=weapon.fruits.size()-1;k>=0;k--){
				Weapon fruit = (Weapon) weapon.fruits.elementAt(k);
				if(Collision.checkSquareCollision(bomb.mapx, bomb.mapy, bomb.width, bomb.height, fruit.mapx, fruit.mapy, fruit.width, fruit.height)){
					hitFruit(fruit);
					print();
					weapon.bombs.removeElement(bomb);
				}
			}
			
			/*射气球*/
			for(int k=batches.ballons.size()-1;k>=0;k--){
				Role buble = (Role) batches.ballons.elementAt(k);
				if(Collision.checkSquareCollision(bomb.mapx, bomb.mapy, bomb.width, bomb.height, buble.mapx, buble.mapy, buble.width, 30)){
					if(buble.id != multicolour){
						hitBuble(buble);
						print();
						weapon.bombs.removeElement(bomb);
					}else{
						bomb.direction = Weapon.WEAPON_MOVE_DOWN;
						bomb.speedY = bomb.speedX + 10;
					}
				}
			}
			
			/*击中狼发射的子弹*/
			for(int k=weapon.booms.size()-1;k>=0;k--){
				Weapon boom = (Weapon) weapon.booms.elementAt(k);
				if(Collision.checkSquareCollision(bomb.mapx, bomb.mapy, bomb.width, bomb.height,boom.mapx, boom.mapy, boom.width, boom.height)){
					hitBoom(boom);
					print();
					weapon.bombs.removeElement(bomb);
				}
			}
			
			/*子弹出界时移除*/
			if((bomb.mapx+bomb.width <=0) || bomb.mapy >= 466){
				weapon.bombs.removeElement(bomb);
			}
		}
	}
	
	private int cloudIndex, cloud2Index;
	private int down_cloudIndex, down_cloud2Index;
	int x1 = 20, x2 = 550, x3 = 424;
	int nanX = 256, nanY = 27, nanY2 = 25;
	int nanFlag,nanIndex=0, arrowFlag, arrowIndex;
	private void drawGamePlaying(SGraphics g){
		Image game_bg = Resource.loadImage(Resource.id_game_bg);
		Image playing_menu = Resource.loadImage(Resource.id_playing_menu);
		Image playing_cloudbig = Resource.loadImage(Resource.id_playing_cloudbig);
		Image playing_cloudsmall = Resource.loadImage(Resource.id_playing_cloudsmall);
		Image playing_lawn = Resource.loadImage(Resource.id_playing_lawn);
		Image playing_step = Resource.loadImage(Resource.id_playing_step);
		Image playing_tree = Resource.loadImage(Resource.id_playing_tree);
		Image playing_lunzi = Resource.loadImage(Resource.id_playing_lunzi);
		Image playing_shenzi = Resource.loadImage(Resource.id_playing_shenzi);
		Image playing_lift = Resource.loadImage(Resource.id_playing_lift);
		Image playing_shenzi1 = Resource.loadImage(Resource.id_playing_shenzi1);
		Image ladder = Resource.loadImage(Resource.id_ladder);
		Image playing_level = Resource.loadImage(Resource.id_playing_level);
		Image playing_level2 = Resource.loadImage(Resource.id_playing_level2);
		Image sheep_head = Resource.loadImage(Resource.id_sheep_head);
		Image wolf_head = Resource.loadImage(Resource.id_wolf_head);
		Image multiply = Resource.loadImage(Resource.id_multiply);
		/*奖励关卡图片资源*/
		Image pass_cloud2 = Resource.loadImage(Resource.id_pass_cloud2);
		Image pass_cloud = Resource.loadImage(Resource.id_pass_cloud);
		Image passShadowCloud = Resource.loadImage(Resource.id_cloud1);
		Image pass_cloud1 = Resource.loadImage(Resource.id_pass_cloud1);
		Image pumpkin = Resource.loadImage(Resource.id_pumpkin);
		Image control = Resource.loadImage(Resource.id_control);
		Image fruit = Resource.loadImage(Resource.id_watermelon);
		Image stop = Resource.loadImage(Resource.id_game_stop);
		Image teach_level = Resource.loadImage(Resource.id_teach_level);
		Image arrowhead = Resource.loadImage(Resource.id_arrowhead);
		
		g.drawImage(game_bg, 0, 0, 20);
		int nanW = pumpkin.getWidth()/5, nanH = pumpkin.getHeight();
		if((isRewardLevel && !isNextLevel) || isReward){		//画出奖励关卡界面
			g.drawImage(pass_cloud, 50, 80, 20);
			g.drawImage(pass_cloud, 216, 80, 20);
			g.drawImage(pass_cloud, 404, 140, 20);		//轮子下面的云朵
			for(int i=0;i<4;i++){			//固定的云层 南瓜没有
				g.drawImage(passShadowCloud, 0+i*60, 80+10, 20);
				g.drawImage(pass_cloud, 0+i*60, 80, 20);
			}
			/*上面第二层云*/
			int cloud2W = pass_cloud2.getWidth(),cloud2H = pass_cloud2.getHeight();
			int len = cloud2W-ScrW;
			int cloud2Y = -6;
			cloud2Index=(cloud2Index+1)%cloud2W;
			if(cloud2Index<=len){
				g.drawRegion(pass_cloud2, len-cloud2Index, 0, ScrW, cloud2H, 0, 0, cloud2Y, 20);
			}else{
				g.drawRegion(pass_cloud2, (cloud2W-cloud2Index), 0, ScrW-(cloud2W-cloud2Index), cloud2H, 0, 0, cloud2Y, 20);
				g.drawRegion(pass_cloud2, 0, 0, (cloud2W-cloud2Index), cloud2H, 0, ScrW-(cloud2W-cloud2Index), cloud2Y, 20);
			}
			
			/*下面第二层云*/
			int down_cloud2Y = 484;
			down_cloud2Index=(down_cloud2Index+1)%cloud2W;
			if(down_cloud2Index<=len){
				g.drawRegion(pass_cloud2, len-down_cloud2Index, 0, ScrW, cloud2H, 0, 0, down_cloud2Y, 20);
			}else{
				g.drawRegion(pass_cloud2, (cloud2W-down_cloud2Index), 0, ScrW-(cloud2W-down_cloud2Index), cloud2H, 0, 0, down_cloud2Y, 20);
				g.drawRegion(pass_cloud2, 0, 0, (cloud2W-down_cloud2Index), cloud2H, 0, ScrW-(cloud2W-down_cloud2Index), down_cloud2Y, 20);
			}

			/*中间的云*/
			int cloudW = pass_cloud.getWidth();
			if(x1+cloudW<=0){
				x1 = ScrW;
			}else{
				x1 -= 1;
			}
			if(x2+cloudW<=0){
				x2 = ScrW;
			}else{
				x2 -= 1;
			}
			if(x3+cloudW<=0){
				x3 = ScrW;
			}else{
				x3 -= 1;
			}
			g.drawImage(pass_cloud, x1, 152, 20);
			g.drawImage(pass_cloud, x2, 180, 20);
			g.drawImage(pass_cloud, x3, 265, 20);
			
			/*上面第一层云*/
			int cloud1W = pass_cloud1.getWidth(),cloud1H = pass_cloud1.getHeight();
			int cloud1Y = -23;
			cloudIndex=(cloudIndex+1)%cloud1W;
			if(cloudIndex<=cloud1W-ScrW){
				g.drawRegion(pass_cloud1, cloudIndex, 0, ScrW, cloud1H, 0, 0, cloud1Y, 20);
			}else{
				g.drawRegion(pass_cloud1, cloudIndex, 0, cloud1W-cloudIndex, cloud1H, 0, 0, cloud1Y, 20);
				g.drawRegion(pass_cloud1, 0, 0, cloudIndex, cloud1H, 0, cloud1W-cloudIndex, cloud1Y, 20);
			}
			
			/*下面第一层云*/
			int down_cloud1Y = 496;
			down_cloudIndex=(down_cloudIndex+1)%cloud1W;
			if(down_cloudIndex<=cloud1W-ScrW){
				g.drawRegion(pass_cloud1, down_cloudIndex, 0, ScrW, cloud1H, 0, 0, down_cloud1Y, 20);
			}else{
				g.drawRegion(pass_cloud1, down_cloudIndex, 0, cloud1W-down_cloudIndex, cloud1H, 0, 0, down_cloud1Y, 20);
				g.drawRegion(pass_cloud1, 0, 0, down_cloudIndex, cloud1H, 0, cloud1W-down_cloudIndex, down_cloud1Y, 20);
			}
			g.drawImage(playing_menu, 491, 0, 20);
			g.drawImage(playing_level2, 491+12, 18, 20);								//游戏中 左侧的关卡图片
			if(isNextLevel){
				drawNum(g, rewardLevel-1, 491+32+playing_level.getWidth()+30, 20);
			}else{
				drawNum(g, rewardLevel, 491+32+playing_level.getWidth()+30, 20);
			}
			drawNum(g, own.lifeNum, 491+66+multiply.getWidth()+10, 147);			//奖励关卡羊的生命数,应该改为一条命
			g.drawImage(multiply, 491+66, 147, 20);
			
			if(rewardLevel%2==1){
				int num=0;
				if(!isNextLevel){
					num = REWARD_LEVEL_INFO[rewardLevel-1][1]-own.hitNum-reward_nums;
					if(num<0){
						num = 0;
					}
					g.drawImage(multiply, 50, 12, 20);	
					drawNum(g, num, 55+multiply.getWidth(), 12);
					g.drawImage(wolf_head, 12, 10, 20);								//游戏中 左侧 的狼的头像
				}else{
					g.drawImage(multiply, 50, 12, 20);	
					drawNum(g, 0, 55+multiply.getWidth(), 12);
					g.drawRegion(fruit, 0, 0, fruit.getWidth()/3, fruit.getHeight(), 0, 12, 2, 20);
				}
			}else{
				int num=0;
				if(!isNextLevel){
					if(batches.redWolf!=null){
						num =16-batches.redWolf.bombNum;
						if(num<0){
							num = 0;
						}
					}
					g.drawImage(multiply, 50, 12, 20);	
					drawNum(g, num, 55+multiply.getWidth(), 12);
					g.drawRegion(fruit, 0, 0, fruit.getWidth()/3, fruit.getHeight(), 0, 12, 2, 20);
				}else{
					g.drawImage(multiply, 50, 12, 20);	
					drawNum(g, 0, 55+multiply.getWidth(), 12);
					g.drawImage(wolf_head, 12, 10, 20);								//游戏中 左侧 的狼的头像
				}
			}
		}else {
			if(tempx+playing_cloudbig.getWidth()>0){
				tempx -= 1;
			}else{
				tempy = RandomValue.getRandInt(0, 114);
				tempx = ScrW;
			}
			g.drawRegion(playing_cloudbig, 0, 0, playing_cloudbig.getWidth(), playing_cloudbig.getHeight(), 
					0, tempx, tempy, 20);
			
			if(tempx2+playing_cloudsmall.getWidth()>0){
				tempx2 -= 2;
			}else{
				tempy2 = RandomValue.getRandInt(0, 114);
				tempx2 = ScrW;
			}
			g.drawRegion(playing_cloudsmall, 0, 0, playing_cloudsmall.getWidth(), playing_cloudsmall.getHeight(), 
					0, tempx2, tempy2, 20);
			g.drawImage(playing_lawn, 0, 499, 20);
			g.drawImage(playing_tree, 0, 72, 20);
			for(int i=0;i<4;i++){   //阶梯
				g.drawImage(playing_step, 377, 153+i*89, 20);
				g.drawImage(ladder, 426, 183+i*89, 20);
			}
			
			g.drawImage(playing_menu, 491, 0, 20);
			if(SheepWarGameEngine.isFirstGame){
				g.drawImage(teach_level, 491+32, 18, 20);	
			}else{
				g.drawImage(playing_level, 491+32, 18, 20);						//游戏中 左侧的关卡图片	
				if(isNextLevel){
					drawNum(g, level-1, 491+32+playing_level.getWidth()+10, 20);
				}else{
					drawNum(g, level, 491+32+playing_level.getWidth()+10, 20);
				}
			}
			drawNum(g, own.lifeNum, 491+66+multiply.getWidth()+10, 147);			//羊的生命数
			g.drawImage(multiply, 491+66, 147, 20);
			
			/*南瓜效果*/
			if((level%2==0 && !isNextLevel) || (level!=1 && level%2==1 && isNextLevel)){														//偶数关卡出现南瓜(出现四只狼推南瓜则南瓜砸下，玩家失败)\
				if(down){
					if(nanX<310){
						nanX += 10;
						nanY += 5;
						if(nanX<=276){
							nanIndex=2;
						}else if(nanIndex<=296){
							nanIndex=3;
						}else{
							nanIndex=4;
						}
					}else{
						nanY += 10;
						nanIndex=4;
					}
					g.drawRegion(pumpkin, nanIndex*nanW, 0, nanW, nanH, 0, nanX, nanY, 20);
					if(own.status==ROLE_ALIVE && Collision.checkSquareCollision(own.mapx, own.mapy, own.width, own.height, nanX, nanY, nanW, nanH)){
						hitOwn(own);
					}
				}else{
					nanX = 256; nanY =27;
					if(HASWOLF_ONE && !HASWOLF_TWO && !HASWOLF_THREE && !HASWOLF_FOUR){
						nanIndex=0;
					}
					if(HASWOLF_ONE && HASWOLF_TWO && !HASWOLF_THREE && !HASWOLF_FOUR){
						if(nanFlag<4){
							nanFlag++;
						}else{
							nanIndex = (nanIndex+1)%2;
							nanFlag=0;
						}
						if(nanIndex==1){
							nanX += 5;
						}else{
							nanX = 256;
						}
					}
					if(HASWOLF_ONE && HASWOLF_TWO && HASWOLF_THREE && !HASWOLF_FOUR){
						if(nanFlag<4){
							nanFlag++;
						}else{
							nanIndex =(nanIndex+1)%3;
							nanFlag=0;
						}
						if(nanIndex>0){
							nanX += 5;
						}else{
							nanX = 256;
						}
					}
					g.drawRegion(pumpkin, nanIndex*nanW, 0, nanW, nanH, 0, nanX, nanY, 20);
				}
			}
			
			
			int num =  LEVEL_INFO[level-1][1]-own.hitNum;
			if(num<0){
				num = 0;
			}
			if(!isNextLevel){
				g.drawImage(multiply, 45, 12, 20);	
				drawNum(g, num, 55+multiply.getWidth(), 12);
				g.drawImage(wolf_head, 12, 10, 20);								
			}else{
				g.drawImage(multiply, 45, 12, 20);	
				drawNum(g, 0, 55+multiply.getWidth(), 12);
				g.drawImage(wolf_head, 12, 10, 20);								//游戏中 左侧 的狼的头像
			}
		}
		
		if(own.status == ROLE_ALIVE){
			sWidth = own.mapy - 160;
			sTempy = own.mapy-10;
		}
		g.drawImage(playing_shenzi1, 399, 135, 20);												
		g.drawRegion(playing_shenzi, 0, 0, playing_shenzi.getWidth(), sWidth, 0, 379, 154, 20);                                                        		
		g.drawRegion(playing_lift, 0, 0, playing_lift.getWidth(), playing_lift.getHeight(), 0, 342, sTempy, 20);
		
		g.drawImage(playing_lunzi, 374,132, 20);
		//g.drawRegion(playing_point, 0, 0, 46, playing_point.getHeight()/2, 0, 504+35, 59, 20);
		if(own.scores>99999){
			drawNum(g, own.scores, 499+22, 89);
		}else if(own.scores<1000){
			drawNum(g, own.scores, 499+46, 89);
		}else{
			drawNum(g, own.scores, 499+35, 89);
		}
		g.drawImage(sheep_head, 491+26, 142, 20);						//游戏中 右侧 的羊的头像		

		int LeftMenuX = 497+2,RightMenuX= 564+5,propMenuY = 185,distanceMenuY = 6;
		int controlW = control.getWidth()/8, controlH = control.getHeight();
		for(int i=0;i<4;i++){                                                                
			drawProp(g, i, LeftMenuX+5,propMenuY+10+i*(distanceMenuY+65));           
			g.drawRegion(control, (i+i)*controlW, 0, controlW, controlH, 0, LeftMenuX+5, propMenuY+65-10+i*(distanceMenuY+65), 20);
			
			drawProp(g, i+4, RightMenuX+5,propMenuY+10+i*(distanceMenuY+65));  //第二列对应原图片中的后四个
			g.drawRegion(control, (i+i+1)*controlW, 0, controlW, controlH, 0, RightMenuX+5, propMenuY+65-10+i*(distanceMenuY+65), 20);

		}
		
		int sW = stop.getWidth()/2, sH = stop.getHeight();
		int aW = arrowhead.getWidth()/2, aH = arrowhead.getHeight();
		g.drawRegion(stop, 0, 0, sW, sH, 0, 498+29, 467+9, 20);
		if(SheepWarGameEngine.isFirstGame){
			if(arrowFlag<3){
				arrowFlag++;
				arrowIndex=0;
			}else{
				arrowFlag=0;
				arrowIndex=1;
			}
			if(c3){
				g.drawRegion(arrowhead, arrowIndex * aW, 0, aW, aH, 0, 555, 226, 20);
			}
			if(d3){
				g.drawRegion(arrowhead, arrowIndex * aW, 0, aW, aH, 0, 555, 166, 20);
			}
		}
		
		
		/*道具数量*/
		int propX=551, propY=193, spaceY=71, spaceX=70;
		for(int j=0;j<4;j++){
			for(int k=0;k<2;k++){
				String str = String.valueOf(engine.props[getPropIndex(j, k)].getNums());
				int color = g.getColor();
				engine.setFont(19, true);
				if(engine.props[getPropIndex(j, k)].getNums()<10){
					g.setColor(0x000000);
					DrawUtil.drawRect(g, propX+spaceX*k, propY+spaceY*j+3, 10, 13);
					g.setColor(0xffffff);
					g.drawString(str, propX+spaceX*k, propY+spaceY*j, 20);
				}else{
					g.setColor(0x000000);
					DrawUtil.drawRect(g, propX+spaceX*k-7, propY+spaceY*j+3, 16, 13);
					g.setColor(0xffffff);
					g.drawString(str, propX+spaceX*k-7, propY+spaceY*j, 20);
				}
				g.setColor(color);
				engine.setDefaultFont();
			}
		}
	}
	
	private int getPropIndex(int x, int y){
		if(x==0 && y==0)return 0;
		if(x==1 && y==0)return 1;
		if(x==2 && y==0)return 2;
		if(x==3 && y==0)return 3;
		if(x==0 && y==1)return 4;
		if(x==1 && y==1)return 5;
		if(x==2 && y==1)return 6;
		if(x==3 && y==1)return 7;
		return -1;
	}
	
	private void moveRole(int towards) {
		switch (towards) {
		case 0: // 往上--主角
			if(own.mapy>=176){
				own.mapy -= own.speed;
			}
			break;
		case 1: // 往下--主角
			own.direction = 1;
			if(own.mapy + own.height<452){
				own.mapy += own.speed;
			}
			break;
		}
	}
	
	public void drawNum(SGraphics g, int num, int x, int y) {
		Image imgNumeber = Resource.loadImage(Resource.id_shop_figure);
		String number = String.valueOf(num);
		int numW = imgNumeber.getWidth()/10, numH = imgNumeber.getHeight();
		for (byte i = 0; i < number.length(); i++) {
			g.drawRegion(imgNumeber, (number.charAt(i) - '0') * numW, 0, numW, numH, 0, x + i * (numW + 1), y, 0);
		}
	}
	
	private void drawProp(SGraphics g,int num,int x,int y){
		Image playing_prop=Resource.loadImage(Resource.id_playing_prop);
		String number=String.valueOf(num);
		int propW = playing_prop.getWidth()/8, propH = playing_prop.getHeight();
		for(byte i=0;i<number.length();i++){
			g.drawRegion(playing_prop, (number.charAt(i) - '0')* propW, 0, propW, propH, 0, x+i * (propW + 1), y, 0);
		}
	}
	
	/*击中水果要更改的数据*/
	private void hitFruit(Weapon fruit){
		own.scores += fruit.scores;
		own.scores2 += fruit.scores;
		own.hitFruits++;
		own.hitRatio++;
		scores = own.scores;
		scores2 = own.scores2;
		hitFruits = own.hitFruits;
		hitRatio = own.hitRatio;
		if(fruit.status == OBJECT_NOT_HIT){
			Exploder s = new Exploder(fruit.mapx,fruit.mapy,1);
			s.score = fruit.scores;
			ss[sIndex] = s;
			if(sIndex < ss.length-1){
				sIndex ++;
			}else{
				sIndex=0;
			}
		}
		fruit.status = OBJECT_HIT;
		printInfo();
	}
	
	/*击中气球要更改的数据*/
	private void hitBuble(Role buble){
		own.scores += buble.scores;
		own.scores2 += buble.scores;
		own.hitRatio++;
		scores = own.scores;
		scores2 = own.scores2;
		hitRatio = own.hitRatio;
		if(buble.status == ROLE_ALIVE){
			if(buble.id == multicolour){
				buble.scores = 1000;
			}
			Exploder s = new Exploder(buble.mapx,buble.mapy,1);
			s.score = buble.scores;
			ss[sIndex] = s;
			if(sIndex < ss.length-1){
				sIndex ++;
			}else{
				sIndex=0;
			}
		}
		buble.status = ROLE_DEATH;
		printInfo();
	}
	
	/*击中子弹要改变的数据 */
	private void hitBoom(Weapon boom) {
		own.scores += boom.scores;
		own.scores2 += boom.scores;
		scores = own.scores;
		scores2 = own.scores2;
		own.hitBooms ++;
		hitBooms = own.hitBooms;
		if(boom.status == OBJECT_NOT_HIT){
			Exploder s = new Exploder(boom.mapx,boom.mapy,1);
			s.score = boom.scores;
			ss[sIndex] = s;
			if(sIndex < ss.length-1){
				sIndex ++;
			}else{
				sIndex=0;
			}
		}
		boom.status = OBJECT_HIT;
		printInfo();
	}

	public void hitOwn(Role role){
		down = false;
		nanIndex=0;
		own.status = ROLE_DEATH;
		own.lifeNum --;
		lifeNum = own.lifeNum;
		HASWOLF_ONE = false;
		HASWOLF_TWO = false;
		HASWOLF_THREE = false;
		HASWOLF_FOUR = false;
		System.out.println("生命数减一");
	}
	
	/*击中狼所要该变的数据*/
	public void hitWolf(Role wolf, Weapon w){
		wolf.status = ROLE_DEATH;	
		wolf.speed += 10;
		own.hitNum++;
		own.hitTotalNum++;
		own.hitBuble++;
		own.hitRatio++;
		hitNum = own.hitNum;
		hitTotalNum = own.hitTotalNum;
		hitBuble = own.hitBuble;
		hitRatio = own.hitRatio;
		if(w==null){
			if(wolf.role != null){
				if(wolf.role.id == multicolour){
					wolf.role.scores = 1000;
				}
				own.scores += wolf.role.scores;
				own.scores2 += wolf.role.scores;
				scores = own.scores;
				scores2 = own.scores2;
			}
			Exploder s = new Exploder(wolf.mapx,wolf.mapy,1);
			s.score = wolf.role.scores;
			ss[sIndex] = s;
			if(sIndex < ss.length-1){
				sIndex ++;
			}else{
				sIndex=0;
			}
		}else{
			if(wolf.role != null){
				if(w.hitNum<=1){
					if(wolf.role.id == multicolour){
						wolf.role.scores = 1000;
					}else{
						wolf.role.scores = 400;
					}
				}else if(w.hitNum==2){
					wolf.role.scores = 800;
				}else{
					wolf.role.scores = 2000;
				}
				own.scores += wolf.role.scores;
				own.scores2 += wolf.role.scores;
				scores = own.scores;
				scores2 = own.scores2;
			}
			Exploder s = new Exploder(wolf.mapx,wolf.mapy,1);
			s.score = wolf.role.scores;
			ss[sIndex] = s;
			if(sIndex < ss.length-1){
				sIndex ++;
			}else{
				sIndex=0;
			}
		}
		
		printInfo();
	}
	
	public void printInfo(){
		System.out.println("scores:"+scores);
		System.out.println("scores2:"+scores2);
		System.out.println("hitTotalNum:"+hitTotalNum);
		System.out.println("hitBooms:"+hitBooms);
		System.out.println("useProps:"+useProps);
		System.out.println("hitFruits:"+hitFruits);
		System.out.println("level:"+level);
	}
	
	/*过关要清楚的据*/
	private void initDataNextLevel(){
		pasueState=false;
		isUsePasue=false;
		isUseGlove=false;
		isShowGlove=false;
		gloveStartTime = 0;
		isAttack = false;
		protectState = false;
		harpState = false;
		magnetState = false;
		golveFlag=true;
		isFourRepeating = false;
		rewardLevelFail = false;
		scores2 = own.scores2 = 0;
		hitNum = own.hitNum = 0;
		if(batches.redWolf!=null){
			batches.redWolf.bombNum = 0;
		}
		
		reward_nums = 0;
		batch = 0;
		nanIndex=0;
		HASWOLF_ONE = false;
		HASWOLF_TWO = false;
		HASWOLF_THREE = false;
		HASWOLF_FOUR = false;
		weapon.clearObjects(); // 清空对象
		batches.clearObject(); // 清空对象
		for(int j=0;j<ss.length;j++){
			ss[j]=null;
		}
		for(int i=0;i<exploders.length;i++){
			exploders[i]=null;
		}
		for(int i=0;i<mf.length;i++){
			mf[i]=null;
		}
		for(int i=0;i<sprop.length;i++){
			sprop[i]=null;
		}
	}
	
	/*游戏结束要清楚的数据*/
	private void initDataGameOver(){
		hitNum = 0;
		lifeNum = 0;
		scores = 0;
		scores2 = 0;
		hitBuble = 0;
		hitFruits = 0;
		hitTotalNum = 0;
		hitRatio =0;
		useProps = 0;
		for(int j=0;j<ss.length;j++){
			ss[j]=null;
		}
		for(int i=0;i<exploders.length;i++){
			exploders[i]=null;
		}
		for(int i=0;i<mf.length;i++){
			mf[i]=null;
		}
		for(int i=0;i<sprop.length;i++){
			sprop[i]=null;
		}
		pasueState=false;
		isUsePasue=false;
		isUseGlove=false;
		isShowGlove=false;
		gloveStartTime = 0;
		isAttack = false;
		protectState = false;
		harpState = false;
		magnetState = false;
		golveFlag=true;
		isFourRepeating = false;
		rewardLevelFail = false;
		isShowGlove=false;
		level = 1;
		rewardLevel = 1;
		batch = 0;
		nanIndex=0;
		isRewardLevel = false;
		isReward = false;
		isNextLevel = false;
		reward_nums = 0;
		HASWOLF_ONE = false;
		HASWOLF_TWO = false;
		HASWOLF_THREE = false;
		HASWOLF_FOUR = false;
		own = null;
		weapon.clearObjects(); // 清空对象
		batches.clearObject(); // 清空对象
	}
	
	private void clear() {
		Resource.freeImage(Resource.id_playing_menu);
		Resource.freeImage(Resource.id_control);
		Resource.freeImage(Resource.id_pumpkin);
		Resource.freeImage(Resource.id_multiply);
		Resource.freeImage(Resource.id_cloud1);
		Resource.freeImage(Resource.id_playing_cloudbig);
		Resource.freeImage(Resource.id_playing_cloudsmall);
		Resource.freeImage(Resource.id_playing_lawn);
		Resource.freeImage(Resource.id_playing_step);
		Resource.freeImage(Resource.id_playing_tree);
		Resource.freeImage(Resource.id_game_bg);
		Resource.freeImage(Resource.id_playing_lunzi);
		Resource.freeImage(Resource.id_playing_shenzi);
		Resource.freeImage(Resource.id_playing_lift);
		Resource.freeImage(Resource.id_playing_shenzi1);
		Resource.freeImage(Resource.id_playing_prop_memu);
		Resource.freeImage(Resource.id_playing_prop);
		Resource.freeImage(Resource.id_playing_stop);   
		Resource.freeImage(Resource.id_playing_sheep);   
		Resource.freeImage(Resource.id_sheep_eye);   
		Resource.freeImage(Resource.id_sheep_hand);   
		Resource.freeImage(Resource.id_bomb);   
		Resource.freeImage(Resource.id_wolf_down);   
		Resource.freeImage(Resource.id_wolf_run);   
		Resource.freeImage(Resource.id_wolf_climb);   
		Resource.freeImage(Resource.id_wolf_die);   
		Resource.freeImage(Resource.id_wolf_shove);   
		Resource.freeImage(Resource.id_balloon_blue);   
		Resource.freeImage(Resource.id_balloon_green);   
		Resource.freeImage(Resource.id_balloon_multicolour);   
		Resource.freeImage(Resource.id_balloon_yellow);   
		Resource.freeImage(Resource.id_balloon_yellowred);   
		Resource.freeImage(Resource.id_balloon_red);   
		Resource.freeImage(Resource.id_ladder);   
		Resource.freeImage(Resource.id_playing_level);   
		Resource.freeImage(Resource.id_playing_level2);   
		Resource.freeImage(Resource.id_playing_point);   
		Resource.freeImage(Resource.id_sheep_head);   
		Resource.freeImage(Resource.id_wolf_head);   
		Resource.freeImage(Resource.id_multiply);   
		Resource.freeImage(Resource.id_pass_cloud2);   
		Resource.freeImage(Resource.id_pass_cloud1);   
		Resource.freeImage(Resource.id_pass_cloud);   
		Resource.freeImage(Resource.id_watermelon);   
		Resource.freeImage(Resource.id_prop_2_eff);   
		Resource.freeImage(Resource.id_prop);   
		Resource.freeImage(Resource.id_game_stop);   
		Resource.freeImage(Resource.id_teach_level);   
		Resource.freeImage(Resource.id_arrowhead);   
	}
	
}
