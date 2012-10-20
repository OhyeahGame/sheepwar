package sheepwar;

/*
 * 公共变量类 
 */
public interface Common {
	
	public final static int STATUS_INIT = 0;			//初始
	public final static int STATUS_MAIN_MENU = 1;		//游戏主菜单 
	public final static int STATUS_GAME_PLAYING = 2;	//游戏中
	public final static int STATUS_GAME_RECHARGE = 3;	//游戏中

	public final static int ScrW = SheepWarGameEngine.ScrW;
	public final static int ScrH = SheepWarGameEngine.ScrH;
	public final static int gameW = 490;				//游戏区域宽度
	
	public final static short ROLE_ALIVE = 0; 			//角色活着状态 
	public final static short ROLE_DEATH = -1;  		//角色活着状态 
	public final static short ROLE_SUCCESS = 1;  		//狼逃脱的状态 
	
	public final static short ROLE_ON_GROUND = 0;  		//狼在地面
	public final static short ROLE_IN_AIR = 1;  		//狼离开地面
	
	public final static short ON_ONE_LADDER = 1; 			//角色在第一个梯子上
	public final static short ON_TWO_LADDER = 2;  			//角色在第二个梯子上 
	public final static short ON_THREE_LADDER = 3;  		//角色在第三个梯子上
	public final static short ON_FOUR_LADDER = 4;  			//角色在第四个梯子上
	
	/*狼的移动方向*/
	public final static int ROLE_MOVE_UP = 0;  		//上
	public final static int ROLE_MOVE_DOWN = 1;		//下
	public final static int ROLE_MOVE_LEFT = 2;		//左
	public final static int ROLE_MOVE_RIGHT = 3;	//右
	
	/*狼的初始Y轴位置*/
	public final static short WOLF_POSITION_TOP = 0;		//狼出现的初始位置（上面）
	public final static short WOLF_POSITION_BOTTOM = -1;	//狼出现的初始位置（下面）
	
	/*狼在空中分布方式*/
	public final static short NONE = 0;					//无
	public final static short SPREED_BELOW = 1;			//斜下直线
	public final static short SPREED_ABOVE = 2;			//斜上直线
	public final static short SPREED_VERTICAL = 3;		//竖直
	public final static short SPREED_IRREGULAR = 4;		//折线
	
	/*气球ID(对应颜色)*/
	public final static short blue = Resource.id_balloon_blue;					//蓝气球
	public final static short green = Resource.id_balloon_green;				//绿气球
	public final static short multicolour = Resource.id_balloon_multicolour;	//彩色气球
	public final static short red = Resource.id_balloon_red;					//红气球
	public final static short yellow = Resource.id_balloon_yellow;				//黄气球
	public final static short orange = Resource.id_balloon_yellowred;			//橙色气球
	
	
	/*武器对应id*/
	public final static short clockWeapon = 1001;							//时光闹钟
	public final static short netWeapon = 1002;							
	public final static short playerProtected = 1003;							
	public final static short glareWeapon = 1004;							
	public final static short harpWeapon = 1005;							
	public final static short speedLiquid = 1006;							
	public final static short magnetWeapon= 1007;							
	public final static short puppet= 1008;							
	
	/*水果id*/
	public final static short apple = Resource.id_apple;						//苹果
	public final static short lemon = Resource.id_lemon;						//柠檬
	public final static short pear = Resource.id_orange;						//鸭梨
	public final static short watermelon = Resource.id_watermelon;				//西瓜
	
	/*水果状态*/
	public static short OBJECT_NOT_HIT = 0;			//对象被击中
	public static short OBJECT_HIT = 1;				//对象未被击中
	
	/*激光枪状态*/
	public static short HIT_NPC = 1;				//激光枪击中npc
	public static short NOT_HIT_NPC = 0;			//激光枪没有击中npc
	
	/*随即选中水果*/
	public final static short selectFruit[] = {apple,lemon,pear,watermelon};
	
	/*折线方式*/
	public final static short[][] regular = {
		{4,2,3,1},
		{1,2,4,3},
		{1,3,4,3},
		{4,3,2,3},
		{3,1,2,4},
	};
	
	/*正常关卡（15关），每批狼出现的方式*/
	public static int[][][] BatchesInfo = {
		{/*第一关*/   
			/*0-数量， 1-气球ID(对应颜色) 2-空中分布方式 */
			{4, orange, SPREED_ABOVE },
			{2, blue, SPREED_VERTICAL },
			{2, orange, SPREED_BELOW },
			{2, blue, SPREED_ABOVE },
			{3, orange, SPREED_BELOW },
			{1, blue, SPREED_IRREGULAR },
			{4, blue, SPREED_ABOVE },
			{2, red, SPREED_VERTICAL },
			{2, orange, SPREED_ABOVE },
		},
		{/*第二关*/   
			/*0-数量， 1-气球ID(对应颜色), 2-空中分布方式*/
			{4, orange, SPREED_IRREGULAR },
			{4, orange, SPREED_IRREGULAR },
			{4, orange, SPREED_IRREGULAR },
		},
		{/*第三关*/   
			/*0-数量， 1-气球ID(对应颜色), 2-空中分布方式*/
			{4, orange, SPREED_IRREGULAR },
			{4, orange, SPREED_IRREGULAR },
			{4, orange, SPREED_IRREGULAR },
		},
		{/*第四关*/
			{1,green,NONE},
			{2,green,SPREED_VERTICAL},
			{2,green,SPREED_BELOW},
			{2,green,SPREED_ABOVE},
			{4,red,SPREED_ABOVE},
			{4,red,SPREED_ABOVE},
			{4,red,SPREED_ABOVE},
			{4,green,SPREED_ABOVE},
		},
		{/*第五关*/
			{4,green,SPREED_BELOW},
			{4,green,SPREED_VERTICAL},
		},
		{/*第六关*/
			{1,multicolour,NONE},
			{2,green,SPREED_BELOW},				
			{2,green,SPREED_VERTICAL},			
			{2,red,SPREED_BELOW},
			{3,green,SPREED_ABOVE},				
			{3,red,SPREED_BELOW},
			{4,red,SPREED_ABOVE},
			{4,red,SPREED_VERTICAL},
			{4,green,SPREED_VERTICAL},
			{4,green,SPREED_ABOVE},
		},
		{/*第七关*/
			{1, green, NONE },
			{1, multicolour, NONE},
			{2, green, SPREED_VERTICAL },
			{2, green, SPREED_ABOVE },
			{4, green, SPREED_ABOVE },
			{4, green, SPREED_BELOW },
			{4, green, SPREED_IRREGULAR },			
		},
		{/*第八关*/
			{1, multicolour, NONE },
			{1, yellow, NONE},
			{2, green, SPREED_VERTICAL },
			{2, green, SPREED_ABOVE },
			{4, green, SPREED_VERTICAL },
			{4, green, SPREED_BELOW },	
			{4, green, SPREED_IRREGULAR },			
			{4, green, SPREED_IRREGULAR },			
		},
		{/*第九关*/
			{1, multicolour, NONE },
			{1, yellow, NONE},
			{4, green, SPREED_ABOVE },
			{4, green, SPREED_ABOVE },			
			{4, green, SPREED_VERTICAL },
			{4, green, SPREED_BELOW },		
			{4, green, SPREED_IRREGULAR },				
			{4, green, SPREED_IRREGULAR },				
		},
		{/*第十关*/
			{1, yellow, NONE },
			{1, multicolour, NONE},
			{4, green, SPREED_VERTICAL },
			{4, green, SPREED_ABOVE },		
			{4, green, SPREED_VERTICAL },	
			{4, green, SPREED_IRREGULAR },		
			{4, green, SPREED_VERTICAL },
			{4, green, SPREED_IRREGULAR },		
			{4, green, SPREED_ABOVE },			
		},
		{/*第十一关*/
			{4, yellow, SPREED_ABOVE},
			{4, yellow, SPREED_VERTICAL },	
			{3, yellow, SPREED_BELOW},	
			{2, yellow, SPREED_VERTICAL },	
			{2, yellow, SPREED_ABOVE},	
			{4, yellow, SPREED_VERTICAL },
			{1, multicolour, NONE },
		},
		{/*第十二关*/
			{4, yellow, SPREED_ABOVE},
			{4, yellow, SPREED_VERTICAL },	
			{4, yellow, SPREED_BELOW},	
			{2, yellow, SPREED_ABOVE},	
			{4, yellow, SPREED_VERTICAL },
			{1, multicolour, NONE },
		},
		{/*第十三关*/
			{4, yellow, SPREED_ABOVE},
			{4, yellow, SPREED_VERTICAL },
			{2, yellow, SPREED_BELOW},	
			{2, yellow, SPREED_ABOVE},
			{5, yellow, SPREED_VERTICAL },
			{1, multicolour, NONE },
		},
		{/*第十四关*/
			{4, yellow, SPREED_ABOVE},
			{4, yellow, SPREED_VERTICAL },
			{4, yellow, SPREED_BELOW },
			{2, yellow, SPREED_BELOW},	
			{2, yellow, SPREED_ABOVE},
			{4, yellow, SPREED_VERTICAL },
			{1, multicolour, NONE },
		},
		{/*第十五关*/
			{4, yellow, SPREED_ABOVE},
			{4, yellow, SPREED_VERTICAL },
			{4, yellow, SPREED_BELOW },
			{2, yellow, SPREED_BELOW},	
			{2, yellow, SPREED_ABOVE},
			{4, yellow, SPREED_VERTICAL },
			{1, multicolour, NONE },
			{1, yellow, NONE },
		},
	};
	
	/*奖励关卡中role出现的方式*/
	public static int [][][] RewardLevelBatchesInfo = {
		/*奖励关卡第一关*/
		{/*0-数量， 1-气球ID(对应颜色) 2-空中分布方式 */
			{2,orange,SPREED_ABOVE},
			{4,orange,SPREED_ABOVE},
			{4,orange,SPREED_ABOVE},
			{2,orange,SPREED_ABOVE},
			{2,orange,SPREED_ABOVE},
			{2,orange,SPREED_ABOVE},
		},
		/*奖励关卡2*/
		{	/*0-数量， 1-水果ID， 2-空中分布方式 */
			{2,orange,SPREED_BELOW},
			{4,orange,SPREED_BELOW},
			{4,orange,SPREED_BELOW},
			{2,orange,SPREED_BELOW},
			{2,orange,SPREED_BELOW},
			{2,green,SPREED_BELOW},
		},
		{//奖励关卡3
			{2,red,SPREED_BELOW},
			{4,green,SPREED_BELOW},
			{4,orange,SPREED_BELOW},
			{2,orange,SPREED_BELOW},
			{2,orange,SPREED_BELOW},
			{2,multicolour,SPREED_BELOW},
		},
		{//奖励关卡4
			{2,red,SPREED_BELOW},
			{4,green,SPREED_BELOW},
			{4,orange,SPREED_BELOW},
		},
		{//奖励关卡5
			{2,orange,SPREED_BELOW},
			{2,orange,SPREED_BELOW},
			{2,multicolour,SPREED_BELOW},
		},
		{//奖励关卡6
			{2,red,SPREED_BELOW},
			{4,green,SPREED_BELOW},
			{4,orange,SPREED_BELOW},
		},
		{//奖励关卡7
			{2,red,SPREED_BELOW},
			{4,green,SPREED_BELOW},
			{4,orange,SPREED_BELOW},
		},
	};
	
	/*成就类型*/
	public static int Attainment_Type_Scores = 0;	//得分成就
	public static int Attainment_Type_HitWolf = 1;	//击中灰太狼成就
	public static int Attainment_Type_HitBomb = 2;	//击中子弹成就
	public static int Attainment_Type_UseProp = 3;	//使用道具成就
	public static int Attainment_Type_HitFruit = 4;	//击中水果成就
	public static int Attainment_Type_Level = 5;	//过关成就
	
	/*成就信息*/
	public static String[][][] Attainments = {
		/*得分成就*/
		{	/*0-名称,1-描述,2-奖励, 3-条件*/
			{"一点点来","获得50000分","10", "50000"},
			{"这不算什么","获得100000分","15", "100000"},
			{"得分达人","获得150000分","20", "150000"},
			{"聚沙成塔","获得200000分","30", "200000"},
			{"破表了","获得250000分","50", "250000"},
		},
		/*击中灰太狼成就*/
		{	/*0-名称,1-条件,2-奖励*/
			{"开始狩猎","击落50只灰太狼","10", "50"},
			{"羊羊猎人","击落100只灰太狼","15", "100"},
			{"羊村的希望","击落300只灰太狼","20", "300"},
			{"羊村斗士","击落500只灰太狼","30", "500"},
			{"羊村守护者","击落900只灰太狼","50", "900"},
		},
		/*击中子弹成就*/
		{	/*0-名称,1-条件,2-奖励*/
			{"初次命中","击中1发子弹","10", "1"},
			{"快速瞄准","击中10发子弹","15", "10"},
			{"百发百中","击中20发子弹","20", "20"},
			{"狙击手","击中50发子弹","30", "50"},
			{"神的化身","击中100发子弹","50", "100"},
		},
		/*使用道具成就*/
		{	/*0-名称,1-条件,2-奖励*/
			{"小试牛刀","使用1次道具","10", "1"},
			{"道具专家","使用5次道具","15", "5"},
			{"道具专家","使用10次道具","20", "10"},
			{"道具大师","使用20次道具","30", "20"},
			{"高帅富","使用30次道具","50", "30"},
		},
		/*击中水果成就*/
		{	/*0-名称,1-条件,2-奖励*/
			{"切水果","射中10个水果","10", "10"},
			{"水果忍者","射中20个水果","15", "20"},
			{"这就对了","射中50个水果","20", "50"},
			{"水果达人","射中100个水果","30", "100"},
			{"不可能的任务","射中200个水果","50", "200"},
		},
		/*过关成就*/
		{	/*0-名称,1-条件,2-奖励*/
			{"新手上路","通过第1关","10", "1"},
			{"冒险家","通过第5关","15", "5"},
			{"勇者羊羊","通过第10关","20", "10"},
			{"胜利就在前方","通过第13关","30", "13"},
			{"闯关勇士","通过第15关","50", "15"},
		},
	};
}
