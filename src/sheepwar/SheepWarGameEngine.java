package sheepwar;

import javax.microedition.midlet.MIDlet;

import com.zte.iptv.j2me.stbapi.Account;

import cn.ohyeah.stb.game.GameCanvasEngine;

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
		stateMain = new StateMain(this,stateGame);
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
	

	/*��ʼ����ҳɾ�*/
	private void initAttainmen(){
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
			if(StateGame.scores>=Integer.parseInt(Attainments[i][j][3])){
				attainment.setResult(true);
			}else{
				attainment.setResult(false);
			}
		}else if(attainment.getType()==Attainment_Type_HitWolf){
			if(StateGame.hitTotalNum>=Integer.parseInt(Attainments[i][j][3])){
				attainment.setResult(true);
			}else{
				attainment.setResult(false);
			}
		}else if(attainment.getType()==Attainment_Type_HitBomb){
			if(StateGame.hitBooms>=Integer.parseInt(Attainments[i][j][3])){
				attainment.setResult(true);
			}else{
				attainment.setResult(false);
			}
		}else if(attainment.getType()==Attainment_Type_UseProp){
			if(StateGame.useProps>=Integer.parseInt(Attainments[i][j][3])){
				attainment.setResult(true);
			}else{
				attainment.setResult(false);
			}
		}else if(attainment.getType()==Attainment_Type_HitFruit){
			if(StateGame.hitFruits>=Integer.parseInt(Attainments[i][j][3])){
				attainment.setResult(true);
			}else{
				attainment.setResult(false);
			}
		}else if(attainment.getType()==Attainment_Type_Level){
			if(StateGame.level>=Integer.parseInt(Attainments[i][j][3])){
				attainment.setResult(true);
			}else{
				attainment.setResult(false);
			}
		}
	}

	private void init() {
		/*gameStartTime = engineService.getCurrentTime().getTime();
		java.util.Date gst = new java.util.Date(gameStartTime);
		int year = DateUtil.getYear(gst);
		int month = DateUtil.getMonth(gst);
		recordId = year*100+(month);*/
		
		/*��ѯ����*/
		pm.queryAllOwnProps();
		
		/*��ȡ�ɾ�*/
		readAttainment();
		
		/*��ʼ����ҳɾ���Ϣ*/
		initAttainmen();
		state = STATUS_MAIN_MENU;  
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
	
	/*������Ϸ�ɾ�*/
	public void saveAttainment(Role own){/*
		byte record[];
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(bout);
		try {
			dout.writeByte(own.hitBuble);
			dout.writeByte(own.hitFruits);
			dout.writeByte(own.hitNum);
			dout.writeByte(own.hitTotalNum);
			dout.writeByte(own.useProps);
			dout.writeByte(own.attainment);

			printSaveAttainment(own);
			record = bout.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				dout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				try {
					bout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	*/}
	
	/*��ȡ�ɾ�*/
	public void readAttainment(){/*
		ByteArrayInputStream bin = new ByteArrayInputStream(ga.getData());
		DataInputStream din = new DataInputStream(bin);
		try{
			StateGame.hitBuble = din.readByte();
			StateGame.hitFruits = din.readByte();
			StateGame.hitNum = din.readByte();
			StateGame.hitTotalNum = din.readByte();
			StateGame.useProps = din.readByte();
			StateGame.attainment = din.readByte();
			
			printReadAttainment();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				din.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				try {
					bin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		System.out.println("��ȡ�ɾ�״̬��"+sw.getServiceResult());
	*/}

	/*private void printSaveAttainment(Role own){
		System.out.println("own.hitBuble="+own.hitBuble);
		System.out.println("own.hitFruits="+own.hitFruits);
		System.out.println("own.hitNum="+own.hitNum);
		System.out.println("own.hitTotalNum="+own.hitTotalNum);
		System.out.println("own.useProps="+own.useProps);
		System.out.println("own.attainment="+own.attainment);
		System.out.println("own.scores="+own.scores);
	}
	
	private void printReadAttainment(){
		System.out.println("StateGame.hitBuble="+StateGame.hitBuble);
		System.out.println("StateGame.hitFruits="+StateGame.hitFruits);
		System.out.println("StateGame.hitNum="+StateGame.hitNum);
		System.out.println("StateGame.hitTotalNum="+StateGame.hitTotalNum);
		System.out.println("StateGame.useProps="+StateGame.useProps);
		System.out.println("StateGame.attainment="+StateGame.attainment);
		System.out.println("StateGame.scores="+StateGame.scores);
	}*/

	
}
