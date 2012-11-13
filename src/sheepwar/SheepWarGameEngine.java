package sheepwar;

import javax.microedition.midlet.MIDlet;
import com.zte.iptv.j2me.stbapi.Account;
import com.zte.iptv.j2me.stbapi.GameData;
import com.zte.iptv.j2me.stbapi.STBAPI;

import cn.ohyeah.stb.game.GameCanvasEngine;
import cn.ohyeah.stb.util.ConvertUtil;

/**
 * ��Ϸ����
 * @author Administrator
 */
public class SheepWarGameEngine extends GameCanvasEngine implements Common {
	public static boolean isSupportFavor = false;
	public static int ScrW = 0;
	public static int ScrH = 0;
	
	public static SheepWarGameEngine instance = buildGameEngine();

	private static SheepWarGameEngine buildGameEngine() {
		if(instance==null){
			return new SheepWarGameEngine(SheepWarMIDlet.getInstance());
		}else{
			return instance;
		}
	}

	public StateMain stateMain;
	public StateGame stateGame;
	public PropManager pm;
	public Prop[] props;
	public String amountUnit = "��Ϸ��";
	
	private String recordId;
	private String attainmentId;
	private String recordData;
	private String attainmentData;
	
	public String achi[];
	public String p[];
	
	public String[] gameRecordInfo;
	
	public static boolean result;   	//�Ƿ�����Ϸ��¼
	public static boolean isFirstGame = true;   //�Ƿ��һ������Ϸ
	
	public Account account;
	public static String record_tag = "gameRecord";
	public static String attaintment_tag = "gameAttainment";
	
	/**
	 * ��ųɾ͵Ķ�ά���飬��һά�ǳɾ����ͣ��ڶ�ά��ĳһ�ɾ������е�һ���ɾͶ���
	 */
	public Attainment[][] attainments;
	

	private SheepWarGameEngine(MIDlet midlet) {
		super(midlet);
		setRelease(false);
		ScrW = screenWidth;
		ScrH = screenHeight;
		stateGame = new StateGame(this);
		stateMain = new StateMain(this);
		props = new Prop[8];
		pm = new PropManager(this);
		pm.props = props;
		attainments = new Attainment[6][5];
	}

	public int state;
	public int mainIndex, playingIndex;
	//private long gameStartTime;
	//public int recordId;
	
	protected void loop() {
		
		/*�����ֵ*/
		switch (state) {   	
		case STATUS_INIT:
			init();
			break;
		case STATUS_MAIN_MENU: 
			stateMain.handleKey(keyState);
			break;
		case STATUS_GAME_PLAYING:
			stateGame.handleKey(keyState);
			break;
		}

		/*��ʾ����*/
		switch (state) {
		case STATUS_INIT:
			//showInit(g);
			break;
		case STATUS_MAIN_MENU:
			stateMain.show(g);
			break;
		case STATUS_GAME_PLAYING:
			stateGame.show(g);
			break;
		/*case STATUS_GAME_RECHARGE:  
			if(!isRecharge){
				recharge();
			}
			break;*/
		}
		
		/*ִ���߼�*/
		switch (state) {
		case STATUS_INIT:
			//showInit(g);
			break;
		case STATUS_MAIN_MENU:
			stateMain.execute();
			break;
		case STATUS_GAME_PLAYING:
			stateGame.execute();
			break;
		}
		
		/*�˳���Ϸ*/
		exit();
	}
	
	
	private void exit(){
		if(stateMain.exit){
			exit = true;
		}
	}
	
	private long recordTime;
	public boolean timePass(int millisSeconds) {
		long curTime = System.currentTimeMillis();
		if (recordTime <= 0) {
			recordTime = curTime;
		}
		else {
			if (curTime-recordTime >= millisSeconds) {
				recordTime = 0;
				return true;
			}
		}
		return false;
	}
	
	

	/*��ʼ����ҳɾ�*/
	public void initAttainmen(){
		Attainment[] ams;
		Attainment attainment;
		for(int i=0;i<Attainments.length;i++){
			ams = new Attainment[5];
			for(int j=0;j<Attainments[i].length;j++){
				attainment = new Attainment();
				attainment.setId(j);
				attainment.setName(Attainments[i][j][0]);
				attainment.setDesc(Attainments[i][j][1]);
				attainment.setAward(Integer.parseInt(Attainments[i][j][2]));
				attainment.setType(i);
				
				/*�ж�����Ƿ�ﵽ�ɾ�����*/
				setAttainmentResult(attainment, i, j);
				ams[j] = attainment;
			}
			attainments[i] = ams;
		}
	}
	
	/*������ҳɾ�(��Ҫ�Ǳ������Ƿ�ﵽĳһ�ɾ͵�����)*/
	public void updateAttainmen(){
		//ÿ�θ��³ɾ�ǰ���¸�ֵ
		StateGame.scores3 = StateGame.scores3>StateGame.scores?StateGame.scores3:StateGame.scores;
	    StateGame.hitTotalNum2 = StateGame.hitTotalNum2>StateGame.hitTotalNum?StateGame.hitTotalNum2:StateGame.hitTotalNum;
	    StateGame.hitBooms2 = StateGame.hitBooms2>StateGame.hitBooms?StateGame.hitBooms2:StateGame.hitBooms;
	    StateGame.useProps2 = StateGame.useProps2>StateGame.useProps?StateGame.useProps2:StateGame.useProps;
	    StateGame.hitFruits2 = StateGame.hitFruits2>StateGame.hitFruits?StateGame.hitFruits2:StateGame.hitFruits;
	    StateGame.level2 = StateGame.level2>StateGame.level?StateGame.level2:StateGame.level;
		
		Attainment attainment;
		for(int i=0;i<attainments.length;i++){
			for(int j=0;j<attainments[i].length;j++){
				attainment = attainments[i][j];
				/*�ж�����Ƿ�ﵽ�ɾ�����*/
				setAttainmentResult(attainment, i, j);
			}
		}
	}
	
	private void setAttainmentResult(Attainment attainment, int i, int j){
		if(attainment.getType()==Attainment_Type_Scores){
			if(attainment.isResult()==false&&StateGame.scores3>=Integer.parseInt(Attainments[i][j][3])){
				attainment.setResult(true);
				StateGame.attainment += attainment.getAward();
			}
		}else if(attainment.getType()==Attainment_Type_HitWolf){
			if(attainment.isResult()==false&&StateGame.hitTotalNum2>=Integer.parseInt(Attainments[i][j][3])){
				attainment.setResult(true);
				StateGame.attainment += attainment.getAward();
			}
		}else if(attainment.getType()==Attainment_Type_HitBomb){
			if(attainment.isResult()==false&&StateGame.hitBooms2>=Integer.parseInt(Attainments[i][j][3])){
				attainment.setResult(true);
				StateGame.attainment += attainment.getAward();
			}
		}else if(attainment.getType()==Attainment_Type_UseProp){
			if(attainment.isResult()==false&&StateGame.useProps2>=Integer.parseInt(Attainments[i][j][3])){
				attainment.setResult(true);
				StateGame.attainment += attainment.getAward();
			}
		}else if(attainment.getType()==Attainment_Type_HitFruit){
			if(attainment.isResult()==false&&StateGame.hitFruits2>=Integer.parseInt(Attainments[i][j][3])){
				attainment.setResult(true);
				StateGame.attainment += attainment.getAward();
			}
		}else if(attainment.getType()==Attainment_Type_Level){
			if(attainment.isResult()==false&&StateGame.level2>=Integer.parseInt(Attainments[i][j][3])){
				attainment.setResult(true);
				StateGame.attainment += attainment.getAward();
			}
		}
	}

	private void init() {
		
		/*���û����*/
		queryBalance();
		
		/*������������*/
		pm.initProps(props);
		
		setRecordId();

		/*��ȡ��Ϸ�ɾ���Ϣ*/
		loadAttainmentInfo();

		/*��ʼ��������Ϣ*/
		pm.updateProps();
		
		/*��ȡ��Ϸ��¼*/
		readRecord();
		
		/*��ʼ����ҳɾ���Ϣ*/
		initAttainmen();
		state = STATUS_MAIN_MENU;  
	}
	
	public void queryBalance() {
		try {
			account = STBAPI.GetBalance();
			System.out.println("��ѯ���Ϊ:"+account.getBalance());
		} catch (Exception e) {
			String str="";
			if(account==null){
				str = e.getMessage();
			}else{
				str = getErrorMessage(account.getResult())+account.getResult();
			}
			System.out.println("��ѯ���ʧ�ܣ�ԭ��"+str);
			state = STATUS_MAIN_MENU; 
		} 
	}


	private void loadAttainmentInfo(){
		try	{
		    GameData  gamedata = STBAPI.LoadGameData(attainmentId);
		    System.out.println("LoadAttainmentData:");
		    System.out.println("    Result   ="   + gamedata.getResult());
		    System.out.println("    SaveID   ="   + gamedata.getSaveID());
		    System.out.println("    DataValue="   + gamedata.getDataValue());
		    
		    if(gamedata.getDataValue()==null){
		    	return;
		    }
		    isFirstGame = false;
		    System.out.println("�Ƿ��һ���棺"+isFirstGame);
		    
		    /*�����ɾͺ͵�������*/
		    achi = new String[6];
			p = new String[8];
		    String data = gamedata.getDataValue();
		    String[] ds = ConvertUtil.split(data, "|");
		    String[] acrs = ConvertUtil.split(ds[0], "/");
		    String[] props = ConvertUtil.split(ds[1], "/");
		    
		    String[] str = null;
		    for(int i=0;i<acrs.length;i++){
		    	str = ConvertUtil.split(acrs[i], ":");
		    	achi[i] = str[1];
		    }
		    
		    StateGame.scores3 = Integer.parseInt(achi[0]);
		    StateGame.hitTotalNum2 = Integer.parseInt(achi[1]);
		    StateGame.hitBooms2 = Integer.parseInt(achi[2]);
		    StateGame.useProps2 = Integer.parseInt(achi[3]);
		    StateGame.hitFruits2 = Integer.parseInt(achi[4]);
		    StateGame.level2 = Short.parseShort(achi[5]);
		    
		    for(int j=0;j<props.length;j++){
		    	str = ConvertUtil.split(props[j], ":");
		    	p[j] = str[1];
		    }
		} catch (Exception e){
			String str="";
			if(account==null){
				str = e.getMessage();
			}else{
				str = getErrorMessage(account.getResult())+account.getResult();
			}
			System.out.println("��ȡ������Ϸ����ʧ�ܣ�ԭ��"+str);
			state = STATUS_MAIN_MENU; 
		}
	}

	/*dataǰ�벿���ǳɾͣ���벿���ǵ���*/
	public void setAttainmentData() {
		int scores = StateGame.scores>StateGame.scores3?StateGame.scores:StateGame.scores3;
		StateGame.hitTotalNum2 += StateGame.hitTotalNum; 
		StateGame.hitBooms2 += StateGame.hitBooms; 
		StateGame.useProps2 += StateGame.useProps; 
		StateGame.hitFruits2 += StateGame.hitFruits; 
		int level = StateGame.level>StateGame.level2?StateGame.level:StateGame.level2;
		
		attainmentData = "scores:"+scores+"/hitTotalNum:"+StateGame.hitTotalNum2
					+"/hitBooms:"+StateGame.hitBooms2+"/useProps:"+StateGame.useProps2
					+"/hitFruits:"+StateGame.hitFruits2+"/level:"+level
					+"|"
					+"0:"+props[0].getNums()+"/1:"+props[1].getNums()
					+"/2:"+props[2].getNums()+"/3:"+props[3].getNums()
					+"/4:"+props[4].getNums()+"/5:"+props[5].getNums()
					+"/6:"+props[6].getNums()+"/7:"+props[7].getNums();
		print(scores,level);
	}
	
	private void print(int scores, int level){
		System.out.println("scores3:"+scores);
		System.out.println("level2:"+level);
		System.out.println("/hitTotalNum2:"+StateGame.hitTotalNum2);
		System.out.println("/hitBooms2:"+StateGame.hitBooms2);
		System.out.println("/useProps2:"+StateGame.useProps2);
		System.out.println("/hitFruits2:"+StateGame.hitFruits2);
		for(int i=0;i<props.length-1;i++){
			System.out.println(i+"��"+props[i].getNums());
		}
	}
	
	public void setRecordId() {
		try
		{
		    String Date = STBAPI.GetServiceDate();
		    recordId = Date.substring(0,4)+record_tag;
		    attainmentId = Date.substring(0,4)+attaintment_tag;
		    System.out.println("GetServiceDate: Date=" + Date);
		    System.out.println("GetServiceDate: Date=" + recordId);
		} 
		catch (Exception e)
		{
			String str="";
			if(account==null){
				str = e.getMessage();
			}else{
				str = getErrorMessage(account.getResult())+account.getResult();
			}
		   System.out.println("��ȡϵͳʱ��ʧ�ܣ�ԭ��"+str);
		   state = STATUS_MAIN_MENU;
		}
	}
	
	/*������Ϸ�ɾ�*/
	public void saveAttainment() throws Exception{
		/*����ҵ��ߺͳɾ�*/
		setAttainmentData();
		int res = STBAPI.SaveGameData(attainmentId,"�ɾͺ͵�����Ϣ",attainmentData);
		System.out.println("SaveGameData: res=" + Integer.toString(res));
	}
	
	/*�ϱ�����*/
	public void reportScores() throws Exception{
		if(StateGame.scores>StateGame.scores3){
			//int scores = StateGame.scores>StateGame.scores3?StateGame.scores:StateGame.scores3;
			int res = STBAPI.ReportScore (STBAPI.SysConfig.RankID,StateGame.scores);
			System.out.println("ReportScore: res=" + Integer.toString(res));
		}
		
	}
	
	private void setRecordData(){
		recordData = "scores:"+StateGame.scores
					+"/sorces:"+StateGame.scores2
					+"/hitTotalNum:"+StateGame.hitTotalNum
					+"/hitBooms:"+StateGame.hitBooms
					+"/useProps:"+StateGame.useProps
					+"/lifeNum:"+StateGame.lifeNum
					+"/hitFruits:"+StateGame.hitFruits
					+"/level:"+StateGame.level
					+"/hitNum:"+StateGame.hitNum
					+"/hitRatio:"+StateGame.hitRatio
					
					+"/isFourRepeating:"+StateGame.isFourRepeating
					+"/second:"+StateGame.second
					+"/pasueState:"+StateGame.pasueState
					+"/isUsePasue:"+StateGame.isUsePasue
					+"/pasueTime:"+(StateGame.pasueTimeE-StateGame.pasueTimeS)
					+"/protectState:"+StateGame.protectState
					+"/protectTime:"+(StateGame.proEndTime-StateGame.proStartTime)
					+"/isUseGlove:"+StateGame.isUseGlove
					+"/golveFlag:"+StateGame.golveFlag
					+"/isGolveShow:"+StateGame.isShowGlove
					+"/gloveValideTime:"+(StateGame.gloveEndTime-StateGame.gloveStartTime)
					
					+"/isRewardLevel:"+StateGame.isRewardLevel
					+"/isReward:"+StateGame.isReward
					+"/reward_nums:"+StateGame.reward_nums
					+"/batch:"+StateGame.batch
					+"/rewardLevelFail:"+StateGame.rewardLevelFail
					+"/haswolf_one:"+StateGame.HASWOLF_ONE
					+"/haswolf_two:"+StateGame.HASWOLF_TWO
					+"/haswolf_three:"+StateGame.HASWOLF_THREE
					+"/haswolf_four:"+StateGame.HASWOLF_FOUR
					+"/isfourwolf:"+StateGame.IS_FOUR_WOLF;
	}
	
	private void initRecordInfo(String[] str){
			StateGame.scores = Integer.parseInt(str[0]);
			StateGame.scores2 = Integer.parseInt(str[1]);
			StateGame.hitTotalNum = Integer.parseInt(str[2]);
			StateGame.hitBooms = Integer.parseInt(str[3]);
			StateGame.useProps = Integer.parseInt(str[4]);
			StateGame.lifeNum =Integer.parseInt(str[5]);
			StateGame.hitFruits = Integer.parseInt(str[6]);
			StateGame.level = Short.parseShort(str[7]);
			StateGame.hitNum = Integer.parseInt(str[8]);
			StateGame.hitRatio = Integer.parseInt(str[9]);
			
			StateGame.isFourRepeating = str[10].equals("true")?true:false;
			StateGame.second = str[11].equals("true")?true:false;
			StateGame.pasueState = str[12].equals("true")?true:false;
			StateGame.isUsePasue = str[13].equals("true")?true:false;
			StateGame.pasueValideTime = Integer.parseInt(str[14]);
			StateGame.protectState = str[15].equals("true")?true:false;
			StateGame.protectValideTime = Integer.parseInt(str[16]);
			StateGame.isUseGlove = str[17].equals("true")?true:false;
			StateGame.golveFlag = str[18].equals("true")?true:false;
			StateGame.isShowGlove = str[19].equals("true")?true:false;
			StateGame.gloveValideTime = Integer.parseInt(str[20]);
			
			StateGame.isRewardLevel = str[21].equals("true")?true:false;
			StateGame.isReward = str[22].equals("true")?true:false;
			StateGame.reward_nums = Integer.parseInt(str[23]);
			StateGame.batch = Short.parseShort(str[24]);
			StateGame.rewardLevelFail = str[25].equals("true")?true:false;
			StateGame.HASWOLF_ONE = str[26].equals("true")?true:false;
			StateGame.HASWOLF_TWO = str[27].equals("true")?true:false;
			StateGame.HASWOLF_THREE = str[28].equals("true")?true:false;
			StateGame.HASWOLF_FOUR = str[29].equals("true")?true:false;
			StateGame.IS_FOUR_WOLF = str[30].equals("true")?true:false;
	}
	
	/*������Ϸ��¼*/
	public void saveRecord(){
		try
		{
			setRecordData();
			printGameInfo();
			int res = STBAPI.SaveGameData(recordId,"��Ϸ��¼��Ϣ",recordData);
			System.out.println("SaveGameData: res=" + Integer.toString(res));
		} 
		catch (Exception e)
		{
			String str="";
			if(account==null){
				str = e.getMessage();
			}else{
				str = getErrorMessage(account.getResult())+account.getResult();
			}
			 System.out.println("������Ϸ��¼ʧ�ܣ�ԭ��"+str);
			 state = STATUS_MAIN_MENU;
		}
	}
	
	/*��ȡ��Ϸ��¼*/
	public void readRecord(){
		try {
			GameData gamedata = STBAPI.LoadGameData(recordId);
			System.out.println("LoadAttainmentData:");
		    System.out.println("    Result   ="   + gamedata.getResult());
		    System.out.println("    SaveID   ="   + gamedata.getSaveID());
		    System.out.println("    DataValue="   + gamedata.getDataValue());
		    
		    if(gamedata.getDataValue()==null){
		    	return;
		    }
		    String data = gamedata.getDataValue();
		    String[] ds = ConvertUtil.split(data, "/");
		    gameRecordInfo = new String[ds.length];
		    for(int i=0;i<ds.length;i++){
		    	gameRecordInfo[i] = ConvertUtil.split(ds[i], ":")[1];
		    }
		    
		    initRecordInfo(gameRecordInfo);
		    
		    printGameInfo();
		    
		    result = gamedata.getResult()==0?true:false;
		} catch (Exception e) {
			String str="";
			if(account==null){
				str = e.getMessage();
			}else{
				str = getErrorMessage(account.getResult())+account.getResult();
			}
			System.out.println("��ȡ������Ϸ����ʧ�ܣ�ԭ��"+str);
			state = STATUS_MAIN_MENU; 
			result = false;
		} 
	}
	
 	private void printGameInfo() {
 		System.out.println("record_socres:"+StateGame.scores );
 		System.out.println("record_scores2:"+StateGame.scores2 );
 		System.out.println("record_hitTotalNum:"+StateGame.hitTotalNum );
 		System.out.println("record_hitBooms:"+StateGame.hitBooms );
 		System.out.println("record_useProps:"+StateGame.useProps );
 		System.out.println("record_lifeNum:"+StateGame.lifeNum );
 		System.out.println("record_hitFruits:"+StateGame.hitFruits );
 		System.out.println("record_level:"+StateGame.level );
 		System.out.println("record_hitNum:"+StateGame.hitNum  );
 		System.out.println("record_hitRatio:"+StateGame.hitRatio  );
		
 		System.out.println("record_isFourRepeating:"+StateGame.isFourRepeating  );
 		System.out.println("record_second:"+StateGame.second  );
 		System.out.println("record_pasueState:"+StateGame.pasueState );
 		System.out.println("record_isUsePasue:"+StateGame.isUsePasue );
 		System.out.println("record_pasueValideTime:"+StateGame.pasueValideTime );
 		System.out.println("record_protectState:"+StateGame.protectState );
 		System.out.println("record_protectValideTime:"+StateGame.protectValideTime );
 		System.out.println("record_isUseGlove:"+StateGame.isUseGlove );
 		System.out.println("record_golveFlag:"+StateGame.golveFlag );
 		System.out.println("record_isShowGlove:"+StateGame.isShowGlove );
 		System.out.println("record_gloveValideTime:"+StateGame.gloveValideTime );
		
 		System.out.println("record_isRewardLevel:"+StateGame.isRewardLevel );
 		System.out.println("record_isReward:"+StateGame.isReward );
 		System.out.println("record_reward_nums:"+StateGame.reward_nums );
 		System.out.println("record_batch:"+StateGame.batch );
 		System.out.println("record_rewardLevelFail:"+StateGame.rewardLevelFail );
 		System.out.println("record_HASWOLF_ONE:"+StateGame.HASWOLF_ONE );
 		System.out.println("record_HASWOLF_TWO:"+StateGame.HASWOLF_TWO );
 		System.out.println("record_HASWOLF_THREE:"+StateGame.HASWOLF_THREE );
 		System.out.println("record_HASWOLF_FOUR:"+StateGame.HASWOLF_FOUR );
 		System.out.println("record_IS_FOUR_WOLF:"+StateGame.IS_FOUR_WOLF );
	}


	public String getErrorMessage(int errorCode){
		switch (errorCode){
		case 2004:
			return "GameID������";
		case 2005:
			return "�˻�����";
		case 2006:
			return "û�ж�����ϵ";
		case 2007:
			return "��Ʒ������";
		case 2008:
			return "�Ѷ����������ظ�����";
		case 2009:
			return "���ߴ��벻����";
		case 2010:
			return "���а񲻴���";
		case 2011:
			return "���а�״̬������(��δ����)";
		case 2012:
			return "�ϱ����а�ʱδ���(���Ǹ�����óɼ�)";
		case 2013:
			return "�ɾ�ID������";
		case 2014:
			return "�ɾ��Ѽ���, �����ظ�����";
		case 2015:
			return "��Ϸδ����";
		case 2016:
			return "�������ﵽ����";
		case 2017:
			return "����ID������";
		case 2018:
			return "������ID����ȷ";
		case 2019:
			return "��Ϸ�˻�����";
		case 2020:
			return "�����˻����㣬��Ҫ���г�ֵ";
		case 2021:
			return "��ֵ����Ƶ������ֵ���ʱ��Ϊ30��";
		case 2022:
			return "�����ڻ������Ͻ��й���";
		case 2023:
			return "���������ﵽ�޶�";
		case 2024:
			return "���������ﵽ�޶�";
		case 2025:
			return "�û���ֵȨ�޲���";
		case 2026:
			return "��ֵ���벻����";
		case 9103:
			return "����У��ʧ��";
		case 9999:
			return "����ʧ��";
		default: return "δ֪����";
		}
	}
}
