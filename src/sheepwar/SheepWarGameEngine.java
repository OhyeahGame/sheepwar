package sheepwar;

import javax.microedition.midlet.MIDlet;

import com.zte.iptv.j2me.stbapi.Account;
import com.zte.iptv.j2me.stbapi.GameData;
import com.zte.iptv.j2me.stbapi.STBAPI;

import cn.ohyeah.stb.game.GameCanvasEngine;
import cn.ohyeah.stb.res.UIResource;
import cn.ohyeah.stb.ui.PopupText;
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
	private String data;
	
	public String achi[];
	public String p[];
	
	public Account account;
	
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
		this.initAttainmen();
	}
	
	private void setAttainmentResult(Attainment attainment, int i, int j){
		if(attainment.getType()==Attainment_Type_Scores){
			if(StateGame.scores3>=Integer.parseInt(Attainments[i][j][3])){
				attainment.setResult(true);
			}else{
				attainment.setResult(false);
			}
		}else if(attainment.getType()==Attainment_Type_HitWolf){
			if(StateGame.hitTotalNum2>=Integer.parseInt(Attainments[i][j][3])){
				attainment.setResult(true);
			}else{
				attainment.setResult(false);
			}
		}else if(attainment.getType()==Attainment_Type_HitBomb){
			if(StateGame.hitBooms2>=Integer.parseInt(Attainments[i][j][3])){
				attainment.setResult(true);
			}else{
				attainment.setResult(false);
			}
		}else if(attainment.getType()==Attainment_Type_UseProp){
			if(StateGame.useProps2>=Integer.parseInt(Attainments[i][j][3])){
				attainment.setResult(true);
			}else{
				attainment.setResult(false);
			}
		}else if(attainment.getType()==Attainment_Type_HitFruit){
			if(StateGame.hitFruits2>=Integer.parseInt(Attainments[i][j][3])){
				attainment.setResult(true);
			}else{
				attainment.setResult(false);
			}
		}else if(attainment.getType()==Attainment_Type_Level){
			if(StateGame.level2>=Integer.parseInt(Attainments[i][j][3])){
				attainment.setResult(true);
			}else{
				attainment.setResult(false);
			}
		}
	}

	private void init() {
		
		/*���û����*/
		queryBalance();
		
		/*������������*/
		pm.initProps(props);
		
		setRecordId();
		//saveRecord();
		loadGameRecord();

		/*��ʼ��������Ϣ*/
		pm.updateProps();
		
		/*��ʼ����ҳɾ���Ϣ*/
		initAttainmen();
		state = STATUS_MAIN_MENU;  
	}
	
	public void queryBalance() {
		try {
			account = STBAPI.GetBalance();
			System.out.println("��ѯ���");
		} catch (Exception e) {
			PopupText pt = UIResource.getInstance().buildDefaultPopupText();
			pt.setText("��ѯ���ʧ�ܣ�ԭ��"+getErrorMessage(account.getResult())+account.getResult());
			pt.popup();
		} 
	}


	private void loadGameRecord(){
		try	{
		    GameData  gamedata = STBAPI.LoadGameData(recordId);
		    System.out.println("LoadGameData:");
		    System.out.println("    Result   ="   + gamedata.getResult());
		    System.out.println("    SaveID   ="   + gamedata.getSaveID());
		    System.out.println("    DataValue="   + gamedata.getDataValue());
		    
		    if(gamedata.getDataValue()==null){
		    	return;
		    }
		    
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
			System.out.println("��ȡ������Ϸ����ʧ�ܣ�ԭ��"+getErrorMessage(account.getResult())+account.getResult());
		}
	}

	/*dataǰ�벿���ǳɾͣ���벿���ǵ���*/
	public void setData() {
		int scores = StateGame.scores>StateGame.scores3?StateGame.scores:StateGame.scores3;
		StateGame.hitTotalNum2 += StateGame.hitTotalNum; 
		StateGame.hitBooms2 += StateGame.hitBooms; 
		StateGame.useProps2 += StateGame.useProps; 
		StateGame.hitFruits2 += StateGame.hitFruits; 
		int level = StateGame.level>StateGame.level2?StateGame.level:StateGame.level2;
		
		data = "scores:"+scores+"/hitTotalNum:"+StateGame.hitTotalNum2
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
	
	public String setRecordId() {
		try
		{
		    String Date = STBAPI.GetServiceDate();
		    recordId = Date.substring(0,4);
		    System.out.println("GetServiceDate: Date=" + Date);
		    System.out.println("GetServiceDate: Date=" + recordId);
		} 
		catch (Exception e)
		{
		   System.out.println("��ȡϵͳʱ��ʧ�ܣ�ԭ��"+getErrorMessage(account.getResult())+account.getResult());
		}
		return recordId;
	}
	
	/*������Ϸ�ɾ�*/
	public void saveRecord(){
		/*����ҵ��ߺͳɾ�*/
		try
		{
			setData();
			int result = STBAPI.SaveGameData(recordId,"�ҵĴ浵",data);
			System.out.println("SaveGameData: result=" + Integer.toString(result));
		} 
		catch (Exception e)
		{
			 System.out.println("��ȡ�����¼ʧ�ܣ�ԭ��"+getErrorMessage(account.getResult())+account.getResult());
		}
		
		/*�ϱ�����*/
		try
		{
			int scores = StateGame.scores>StateGame.scores3?StateGame.scores:StateGame.scores3;
			int result = STBAPI.ReportScore (STBAPI.SysConfig.RankID,scores);
			System.out.println("ReportScore: result=" + Integer.toString(result));
		} 
		catch (Exception e)
		{
			System.out.println("��ȡ�ϱ�����ʧ�ܣ�ԭ��"+getErrorMessage(account.getResult())+account.getResult());
		}
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
