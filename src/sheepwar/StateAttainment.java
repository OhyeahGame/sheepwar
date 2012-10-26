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

	//private int cloudIndex, cloud2Index;
	//private int down_cloudIndex, down_cloud2Index;
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
		Image archivement_hoof = Resource.loadImage(Resource.id_archivement_hoof);//{539,130},{539,211},{539,293},{539,378}
		Image archivement_hoof1 = Resource.loadImage(Resource.id_archivement_hoof1);
		Image achievement_word = Resource.loadImage(Resource.id_achievement_word);
		Image slash = Resource.loadImage(Resource.id_slash);
		Image achievement_left1 = Resource.loadImage(Resource.id_shop_out_base);
		/*添加云朵元素*/
		Image pass_cloud = Resource.loadImage(Resource.id_pass_cloud);
		//Image pass_cloud1 = Resource.loadImage(Resource.id_pass_cloud1);
		//Image pass_cloud2 = Resource.loadImage(Resource.id_pass_cloud2);
		engine.setFont(19,true);	
		g.drawImage(game_bg, 0, 0, 20);
		
		/*新添加的云层显示*/
		/*上面第二层云*/
		//int cloud2W = pass_cloud2.getWidth()/*cloud2H = pass_cloud2.getHeight()*/;
		//int len = cloud2W-ScrW;
		//int cloud2Y = -6;
		//cloud2Index=(cloud2Index+1)%cloud2W;
		/*if(cloud2Index<=len){
			g.drawRegion(pass_cloud2, len-cloud2Index, 0, ScrW, cloud2H, 0, 0, cloud2Y, 20);
		}else{
			g.drawRegion(pass_cloud2, (cloud2W-cloud2Index), 0, ScrW-(cloud2W-cloud2Index), cloud2H, 0, 0, cloud2Y, 20);
			g.drawRegion(pass_cloud2, 0, 0, (cloud2W-cloud2Index), cloud2H, 0, ScrW-(cloud2W-cloud2Index), cloud2Y, 20);
		}*/
		
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
		/*int cloud1W = pass_cloud1.getWidth(),cloud1H = pass_cloud1.getHeight();
		int cloud1Y = -23;
		cloudIndex=(cloudIndex+1)%cloud1W;
		if(cloudIndex<=cloud1W-ScrW){
			g.drawRegion(pass_cloud1, cloudIndex, 0, ScrW, cloud1H, 0, 0, cloud1Y, 20);
		}else{
			g.drawRegion(pass_cloud1, cloudIndex, 0, cloud1W-cloudIndex, cloud1H, 0, 0, cloud1Y, 20);
			g.drawRegion(pass_cloud1, 0, 0, cloudIndex, cloud1H, 0, cloud1W-cloudIndex, cloud1Y, 20);
		}*/
		
		g.drawImage(achievement, 227, 19, 20);
		g.drawImage(shop_midding, 28, 102, 20);
		g.drawImage(achievement_out1, 55, 451, 20);
		g.drawImage(shop_big, 235, 102, 20);
		g.drawImage(achievement_points, 250, 448, 20);
		g.setColor(0xffffff);
		g.drawString(String.valueOf(StateGame.attainment), 250+achievement_points.getWidth()+5, 448+5, 20);
		
		g.setColor(0xcccccc);
		int leftX = 52,leftY = 122,leftSpace = 15,shadowX = 4,shadowY = 4, mapx, mapy;   
		
		//成就左侧条目
		int left1H = achievement_left1.getHeight(), left1W = achievement_left1.getWidth();
		int leftW = achievement_left.getWidth(), leftH = achievement_left.getHeight();
		int achW = achievement_word.getWidth()/2, achH = achievement_word.getHeight() / 6;
		for(int i=0;i<6;i++){       
			g.drawRegion(achievement_left1, 0, 0, left1W, left1H, 0, leftX, leftY+(leftSpace+left1H)*i, 20);
			if(!isRight && archY==i){
				mapx = leftX+8;
				mapy = leftY+7+(leftH+leftSpace)*i;
				g.drawRegion(achievement_left, 0, 0, leftW, leftH, 0, leftX, leftY+(leftH+leftSpace)*i, 20);
				g.drawRegion(achievement_word,0, i*achH, achW, achH, 0, mapx,	mapy, 20);
			}else{
				mapx = leftX-shadowX+8;
				mapy = leftY-shadowY+7+(leftH+leftSpace)*i;
				g.drawRegion(achievement_left, 0, 0, leftW, leftH, 0, leftX-shadowX, leftY-shadowY+(leftH+leftSpace)*i, 20);
				g.drawRegion(achievement_word,achW, i*achH, achW, achH, 0, mapx,	mapy, 20);
			}
//			g.drawRegion(achievement_word,0, i*achH, achW, achH, 0, mapx,	mapy, 20);
		}

		
		int x=247,y=116,spaceY=4;
		int /*achLongW = achievement_long.getWidth(),*/ achLongH = achievement_long.getHeight();
		int achLong1W = achievement_long1.getWidth(), achLong1H = achievement_long1.getHeight();
		int achHoof1W = archivement_hoof1.getWidth(), achHoof1H = archivement_hoof1.getHeight(); 
		
		int col = g.getColor();
		if(bX==0){
			for(int i=0;i<4;i++){	
				if(engine.attainments[archY][i].isResult()){
					g.drawRegion(achievement_long, 0, 0, achLong1W, achLong1H, 0, x, y+(spaceY+achLong1H)*i, 20);
					g.drawRegion(archivement_hoof, 0, 0, achHoof1W, achHoof1H, 0, x+289, y+12+(spaceY+31+achHoof1H)*i, 20);
					g.setColor(0x000000);
				}else{
					g.drawRegion(achievement_long1, 0, 0, achLong1W, achLong1H, 0, x, y+(spaceY+achLong1H)*i, 20);
					g.drawRegion(archivement_hoof1, 0, 0, achHoof1W, achHoof1H, 0, x+289, y+12+(spaceY+31+achHoof1H)*i, 20);
				}
				g.drawString(engine.attainments[archY][i].getName(), x+10, y+(achLongH+spaceY)*i+15, 20);
				g.drawString(engine.attainments[archY][i].getDesc(), x+10, y+(achLongH+spaceY)*i+40, 20);
				g.drawString(String.valueOf(engine.attainments[archY][i].getAward()), 546, y+(achLongH+spaceY)*i+26, 20);
				g.setColor(col);
			}
		}else{
			if(engine.attainments[archY][4].isResult()){
				g.drawRegion(achievement_long, 0, 0, achLong1W, achLong1H, 0, x, y+(spaceY+achLong1H)*0, 20);
				g.drawRegion(archivement_hoof, 0, 0, achHoof1W, achHoof1H, 0, x+289, y+12+(spaceY+31+achHoof1H)*0, 20);
				g.setColor(0x000000);
			}else{
				g.drawRegion(achievement_long1, 0, 0, achLong1W, achLong1H, 0, x, y+(spaceY+achLong1H)*0, 20);
				g.drawRegion(archivement_hoof1, 0, 0, achHoof1W, achHoof1H, 0, x+289, y+12+(spaceY+31+achHoof1H)*0, 20);
			}
			g.drawString(engine.attainments[archY][4].getName(), x+10, y+(achLongH+spaceY)*0+15, 20);
			g.drawString(engine.attainments[archY][4].getDesc(), x+10, y+(achLongH+spaceY)*0+40, 20);
			g.drawString(String.valueOf(engine.attainments[archY][4].getAward()), 546, y+(achLongH+spaceY)*0+26, 20);
			g.setColor(col);
		}
		
		int leftRightX = 459,leftRightY = 441,distanceLAR = 60;									
		int achRight1W = achievement_left_right1.getWidth()/2, achRight1H = achievement_left_right1.getHeight();
		int achRightW = achievement_left_right.getWidth()/2, achRightH = achievement_left_right.getHeight();
		int achRightX = leftRightX+distanceLAR+achRight1W;
		g.drawRegion(achievement_left_right1, 0, 0, achRight1W,	achRight1H, 0, leftRightX, leftRightY, 20);		//翻页左按钮底部
		g.drawRegion(achievement_left_right1, achRight1W, 0, achRight1W, achRight1H, 0, achRightX, leftRightY, 20);//翻页右按钮底部
		
		int achX = leftRightX+distanceLAR+achRightW, numX = leftRightX+distanceLAR+11;
		if(isRight){	
			if(bX == 0){				
				g.drawRegion(achievement_left_right, 0, 0, achRightW,achRightH, 0,leftRightX, leftRightY, 20);		//翻页左按钮
				g.drawRegion(achievement_left_right,  1*achRightW, 0, achRightW,achRightH, 0,achX-shadowX, leftRightY-shadowY, 20);
			}else if(bX == 1){
				g.drawRegion(achievement_left_right, 0, 0, achRightW, achRightH, 0,leftRightX-shadowX, leftRightY-shadowY, 20);
				g.drawRegion(achievement_left_right, 1*achRightW, 0, achRightW,	achRightH, 0,achX, leftRightY, 20);
			}
			
		}else{
			g.drawRegion(achievement_left_right, 0, 0, achRightW, achRightH, 0,leftRightX-shadowX, leftRightY-shadowY, 20); //翻页左按钮
			g.drawRegion(achievement_left_right, 1*achRightW, 0, achRightW,	achRightH, 0, achX-shadowX, leftRightY-shadowY, 20);	//翻页右按钮
		}
		g.setColor(0x000000);
		g.drawString(String.valueOf(bX+1), numX-28, leftRightY+8, 20);
		g.drawImage(slash, 510, 447, 20);
		g.drawString(String.valueOf(2), numX, leftRightY+8, 20);
		g.setColor(0xffffff);		//修改字体颜色
		engine.setDefaultFont();
		
	}

	private void clear() {
    	Resource.freeImage(Resource.id_achievement_bottom);
    	Resource.freeImage(Resource.id_shop_midding);
    	Resource.freeImage(Resource.id_shop_big);
    	Resource.freeImage(Resource.id_shop_go_pay);
    	Resource.freeImage(Resource.id_achievement);
    	Resource.freeImage(Resource.id_achievement_left_right);
    	Resource.freeImage(Resource.id_achievement_left_right1);
    	Resource.freeImage(Resource.id_achievement_long);
    	Resource.freeImage(Resource.id_achievement_long1);
    	Resource.freeImage(Resource.id_achievement_out1);
    	Resource.freeImage(Resource.id_achievement_points);
    	Resource.freeImage(Resource.id_archivement_hoof);
    	Resource.freeImage(Resource.id_archivement_hoof1);
    	Resource.freeImage(Resource.id_achievement_left);
    	Resource.freeImage(Resource.id_achievement_word);
    	Resource.freeImage(Resource.id_slash);
    	Resource.freeImage(Resource.id_shop_out_base);
    	Resource.freeImage(Resource.id_shop_figure);
    	Resource.freeImage(Resource.id_pass_cloud);       
		Resource.freeImage(Resource.id_pass_cloud1);       
		Resource.freeImage(Resource.id_pass_cloud1);   
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
	
	/*private void drawNum(SGraphics g, int num, int x, int y) {
		Image imgNumeber = Resource.loadImage(Resource.id_shop_figure);
		String number = String.valueOf(num);
		for (byte i = 0; i < number.length(); i++) {
			g.drawRegion(imgNumeber, (number.charAt(i) - '0') * imgNumeber.getWidth()/10, 0, 
					imgNumeber.getWidth()/10, imgNumeber.getHeight(), 0, x + i * (imgNumeber.getWidth()/10 + 1), y, 0);
		}
	}*/

}
