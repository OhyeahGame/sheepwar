package sheepwar;

import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.game.SGraphics;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;

public class StateAttainment implements Common{
	private SheepWarGameEngine engine = SheepWarGameEngine.instance;
	private boolean running;
	private int  archY, bX;
	private boolean isRight;  //判断是左边还是右边
	
	public void processAttainment(){
		running = true;
		try {
			KeyState keyState = engine.getKeyState();
			SGraphics g = engine.getSGraphics();
			while (running) {
				handleAttainment(keyState);
				if (running) {
					long t1 = System.currentTimeMillis();
					ShowAttainment(g);
					engine.flushGraphics();
					System.gc();
					int sleepTime = (int)(125-(System.currentTimeMillis()-t1));
					if (sleepTime <= 0) {
						Thread.sleep(0);
					}
					else {
						Thread.sleep(sleepTime);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			clear();
		}
		
	}

	int x1 = 20, x2 = 550, x3 = 424;
	private void ShowAttainment(SGraphics g) {
		Image game_bg = Resource.loadImage(Resource.id_achievement_bottom);
		Image shop_midding = Resource.loadImage(Resource.id_shop_midding);//{28,102}
		Image shop_big = Resource.loadImage(Resource.id_shop_big);//{235,102}
		Image achievement_left = Resource.loadImage(Resource.id_achievement_left);//{457,381}//{51,123},{51,178},{51,232},{51,286},{51,342},{51,396}
		Image achievement = Resource.loadImage(Resource.id_achievement);//{270,19}
		Image achievement_left_right = Resource.loadImage(Resource.id_achievement_left_right);//{458,441}
		Image achievement_left_right1 = Resource.loadImage(Resource.id_achievement_left_right1);
		Image achievement_long = Resource.loadImage(Resource.id_achievement_long);//{247,114},{247,198},{247,277},{247,361},{}
		Image achievement_long1 = Resource.loadImage(Resource.id_achievement_long1);
		Image achievement_out1 = Resource.loadImage(Resource.id_achievement_out1);//{55,451}
		Image achievement_points = Resource.loadImage(Resource.id_achievement_points);//{250,448}
		Image achievement_word = Resource.loadImage(Resource.id_achievement_word);
		Image achievement_word1 = Resource.loadImage(Resource.id_achievement_word1);
		Image slash = Resource.loadImage(Resource.id_slash);
		Image achievement_left1 = Resource.loadImage(Resource.id_achievement_left1);
		Image pass_cloud = Resource.loadImage(Resource.id_pass_cloud);
		engine.setFont(19,true);	
		g.drawImage(game_bg, 0, 0, 20);
		
		
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
		
		g.drawImage(achievement, 220, 19, 20);
		g.drawImage(shop_midding, 28, 102, 20);
		g.drawImage(achievement_out1, 73, 455, 20);
		g.drawImage(shop_big, 235, 102, 20);
		g.drawImage(achievement_points, 250, 448, 20);
		g.setColor(0x000000);
		engine.stateGame.drawNum(g, StateGame.attainment, 255+achievement_points.getWidth(), 453);
		
		g.setColor(0xcccccc);
		int leftX = 52,leftY = 130,leftSpace = 10, mapx, mapy;   
		int shadowX = 7,shadowY = 5;
		int leftW = achievement_left.getWidth(), leftH = achievement_left.getHeight();
		int achW = achievement_word.getWidth(), achH = achievement_word.getHeight() / 6;
		for(int i=0;i<6;i++){       
			mapx = leftX+8;
			mapy = leftY+7+(leftH+leftSpace)*i;
			if(!isRight && archY==i){
				g.drawRegion(achievement_left1, 0, 0, leftW, leftH, 0, leftX, leftY+(leftH+leftSpace)*i, 20);
				g.drawRegion(achievement_word1,0, i*achH, achW, achH, 0, mapx+shadowX,	mapy+shadowY, 20);
				g.drawRegion(achievement_word,0, i*achH, achW, achH, 0, mapx+5,	mapy+3, 20);
			}else{
				g.drawRegion(achievement_left, 0, 0, leftW, leftH, 0, leftX, leftY+(leftH+leftSpace)*i, 20);
				g.drawRegion(achievement_word,/*achW*/0, i*achH, achW, achH, 0, mapx+5,	mapy+3, 20);
			}
		}

		
		int x=260,y=125,spaceY=4;
		int /*achLongW = achievement_long.getWidth(),*/ achLongH = achievement_long.getHeight();
		int achLong1W = achievement_long1.getWidth(), achLong1H = achievement_long1.getHeight();
		
		int col = g.getColor();
		if(bX==0){
			for(int i=0;i<4;i++){	
				if(engine.attainments[archY][i].isResult()){
					g.drawRegion(achievement_long, 0, 0, achLong1W, achLong1H, 0, x, y+(spaceY+achLong1H)*i, 20);
					g.setColor(0x000000);
				}else{
					g.drawRegion(achievement_long1, 0, 0, achLong1W, achLong1H, 0, x, y+(spaceY+achLong1H)*i, 20);
				}
				g.drawString(engine.attainments[archY][i].getName(), x+20, y+(achLongH+spaceY)*i+15, 20);
				g.drawString(engine.attainments[archY][i].getDesc(), x+20, y+(achLongH+spaceY)*i+40, 20);
				g.drawString(String.valueOf(engine.attainments[archY][i].getAward()), 550, y+(achLongH+spaceY)*i+30, 20);
				g.setColor(col);
			}
		}else{
			if(engine.attainments[archY][4].isResult()){
				g.drawRegion(achievement_long, 0, 0, achLong1W, achLong1H, 0, x, y+(spaceY+achLong1H)*0, 20);
				g.setColor(0x000000);
			}else{
				g.drawRegion(achievement_long1, 0, 0, achLong1W, achLong1H, 0, x, y+(spaceY+achLong1H)*0, 20);
			}
			g.drawString(engine.attainments[archY][4].getName(), x+20, y+(achLongH+spaceY)*0+15, 20);
			g.drawString(engine.attainments[archY][4].getDesc(), x+20, y+(achLongH+spaceY)*0+40, 20);
			g.drawString(String.valueOf(engine.attainments[archY][4].getAward()), 550, y+(achLongH+spaceY)*0+30, 20);
			g.setColor(col);
		}
		
		int leftRightX = 459,leftRightY = 451,distanceLAR = 60;									
		int achRightW = achievement_left_right.getWidth()/2, achRightH = achievement_left_right.getHeight();
		
		int achX = leftRightX+distanceLAR+achRightW, numX = leftRightX+distanceLAR+2;
		if(isRight){	
			if(bX == 0){				
				g.drawRegion(achievement_left_right1, 0, 0, achRightW,achRightH, 0,leftRightX, leftRightY, 20);		//翻页左按钮
				g.drawRegion(achievement_left_right,  1*achRightW, 0, achRightW,achRightH, 0,achX, leftRightY, 20);
			}else if(bX == 1){
				g.drawRegion(achievement_left_right, 0, 0, achRightW, achRightH, 0,leftRightX, leftRightY, 20);
				g.drawRegion(achievement_left_right1, 1*achRightW, 0, achRightW,	achRightH, 0,achX, leftRightY, 20);
			}
		}else{
			g.drawRegion(achievement_left_right, 0, 0, achRightW, achRightH, 0,leftRightX, leftRightY, 20); //翻页左按钮
			g.drawRegion(achievement_left_right, 1*achRightW, 0, achRightW,	achRightH, 0, achX, leftRightY, 20);	//翻页右按钮
		}
		engine.stateGame.drawNum(g, bX+1, numX-30, leftRightY+6);
		g.drawImage(slash, 506, 453, 20);
		engine.stateGame.drawNum(g, 2, numX+3, leftRightY+6);
		engine.setDefaultFont();
	}

	private void clear() {
    	Resource.freeImage(Resource.id_achievement_bottom);
    	Resource.freeImage(Resource.id_shop_midding);
    	Resource.freeImage(Resource.id_shop_big);
    	Resource.freeImage(Resource.id_achievement_left);
    	Resource.freeImage(Resource.id_achievement);
    	Resource.freeImage(Resource.id_achievement_left_right);
    	Resource.freeImage(Resource.id_achievement_left_right1);
    	Resource.freeImage(Resource.id_achievement_long);
    	Resource.freeImage(Resource.id_achievement_long1);
    	Resource.freeImage(Resource.id_achievement_out1);
    	Resource.freeImage(Resource.id_achievement_points);
//    	Resource.freeImage(Resource.id_archivement_hoof);
//    	Resource.freeImage(Resource.id_archivement_hoof1);
    	Resource.freeImage(Resource.id_achievement_left);
    	Resource.freeImage(Resource.id_achievement_word);
    	Resource.freeImage(Resource.id_slash);
    	Resource.freeImage(Resource.id_achievement_left1);
    	Resource.freeImage(Resource.id_pass_cloud);
    	Resource.freeImage(Resource.id_achievement_word1);       
    }

	private void handleAttainment(KeyState keyState) {
		if (keyState.containsAndRemove(KeyCode.NUM0 | KeyCode.BACK)) {
			running = false;
		}else if(keyState.containsAndRemove(KeyCode.UP)){
        	if(!isRight){
        		 if(archY>0){
    		   		 archY--;
    		   	 }
        	}
        }else if(keyState.containsAndRemove(KeyCode.DOWN)){
			if (!isRight) {
				if(archY<5){
					archY++;
				}
			} 
       }else if(keyState.containsAndRemove(KeyCode.LEFT)){
        	if(isRight){
        		if(bX==0){
        			isRight = false;
        		}else{
        			bX = 0;
        		}
        	}
        }else if(keyState.containsAndRemove(KeyCode.RIGHT)){
			if (isRight) {
				bX = 1;
			} else {
				isRight = true;
				bX = 0;
			}
        }else if(keyState.containsAndRemove(KeyCode.OK)){
        	engine.stateGame.printInfo();  //打印成就信息
        }
	}
	
}
