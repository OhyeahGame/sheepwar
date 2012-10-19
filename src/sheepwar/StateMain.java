package sheepwar;
import javax.microedition.lcdui.Image;
import cn.ohyeah.stb.game.SGraphics;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;

public class StateMain implements Common{
	
	public boolean exit;
	private StateGame stateGame;
	private SheepWarGameEngine engine;
	public StateMain(SheepWarGameEngine engine){
		this.engine = engine;
		this.stateGame = engine.stateGame;
	}
	
	public int menuAxis[][] = { { 523, 243 }, { 466, 288 }, { 523, 333 },
			{ 466, 378 }, { 523, 423 }, { 466, 468 }, };
	
	private int mainIndex;
	
	public void handleKey(KeyState keyState){
		if (keyState.containsAndRemove(KeyCode.NUM0 | KeyCode.BACK)) {
			//exit = true;
			//clear();
		}
		if (keyState.containsAndRemove(KeyCode.UP)) {
			mainIndex = (mainIndex + 6 - 1) % 6;
		} else if (keyState.containsAndRemove(KeyCode.DOWN)) {
			mainIndex = (mainIndex + 1) % 6;
		} else if (keyState.containsAndRemove(KeyCode.OK)) {
			processSubMenu();
			clear();
		}
	}

	public void show(SGraphics g) {
		Image main_bg = Resource.loadImage(Resource.id_main_bg);
		Image main_menu = Resource.loadImage(Resource.id_main_menu);
		Image main_select_right = Resource.loadImage(Resource.id_main_select_right);
		Image main_select_left = Resource.loadImage(Resource.id_main_select_left);
		Image main_select_right_base = Resource.loadImage(Resource.id_main_select_right_base);
		Image main_select_left_base = Resource.loadImage(Resource.id_main_select_left_base);
		g.drawImage(main_bg, 0, 0, 0);
		int sw = main_menu.getWidth() / 2, sh = main_menu.getHeight() / 6;
		for(int j=0;j<3;j++){	
			g.drawRegion(main_select_right, 0, 0, main_select_right.getWidth(), 
					main_select_right.getHeight(), 0, 515, 229+j*90, 20);
			g.drawRegion(main_select_left, 0, 0, main_select_left.getWidth(), 
					main_select_left.getHeight(), 0, 446, 280-6+j*90, 20);
	}
	
	for (int i = 0; i < menuAxis.length; ++i) {
		if(mainIndex == i){
			if(mainIndex % 2==0){
				g.drawRegion(main_select_right_base, 0, 0, main_select_right_base.getWidth(), 
						main_select_right_base.getHeight(), 0, menuAxis[i][0]-8, menuAxis[i][1]-14, 20);
			}else{
				g.drawRegion(main_select_left_base, 0, 0, main_select_left_base.getWidth(), 
						main_select_left_base.getHeight(), 0, menuAxis[i][0]-21, menuAxis[i][1]-14, 20);
			}
		}
		g.drawRegion(main_menu,(mainIndex != i) ? sw : 0, i * sh, sw,sh, 0, menuAxis[i][0], menuAxis[i][1], 0);
	}
	}
	
	public void execute(){
		
		/*mainIndexΪ0�ǿ�ʼ��Ϸ*/
		if(mainIndex == 0){
			stateGame.weapon = new Weapon(stateGame);
			stateGame.createRole = new CreateRole();
			stateGame.batches = new Batches();
			StateGame.own = stateGame.createRole.createSheep();
			stateGame.stateSingleScore = new StateSingleScore();		//��������
		}
	}
	
	/*ע��ͽ��水ť��˳��һ��*/
	private void processSubMenu() {
		if (mainIndex == 0) { //����Ϸ
			engine.state = STATUS_GAME_PLAYING;
			
		} else if (mainIndex == 1) {// �����̳�
			StateShop ss =  new StateShop(engine);
			ss.processShop();
		} else if (mainIndex == 2){ //�ɾ�ϵͳ
			engine.initAttainmen();
			StateAttainment sa = new StateAttainment();
			sa.processAttainment();
			
		} else if (mainIndex == 3) {// ���а�
			StateRanking sr = new StateRanking();
			sr.processRanking();
			
		} else if (mainIndex == 4) {// ��Ϸ����
			StateHelp sh = new StateHelp();
			sh.processHelp();
			
		}else if(mainIndex==5){//�˳���Ϸ
			exit = true;
			//��������
			engine.saveRecord();
		} 
	}

	private void clear() {
		Resource.freeImage(Resource.id_main_bg);
		Resource.freeImage(Resource.id_main_menu);
		Resource.freeImage(Resource.id_main_select_right);
		Resource.freeImage(Resource.id_main_select_left);
		Resource.freeImage(Resource.id_main_select_right_base);
		Resource.freeImage(Resource.id_main_select_left_base);
	}
}
