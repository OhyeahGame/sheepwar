package sheepwar;

import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.game.SGraphics;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;
import cn.ohyeah.stb.ui.TextView;

public class StateHelp implements Common{
	
	private SheepWarGameEngine engine = SheepWarGameEngine.instance;
	private boolean running;
	private int  helpX;
	private int pageIndex;
	
	public void processHelp(){
		running = true;
		try {
			KeyState keyState = engine.getKeyState();
			SGraphics g = engine.getSGraphics();
			while (running) {
				handleHelp(keyState);
				if (running) {
					long t1 = System.currentTimeMillis();
					showHelp(g);
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

	private int cloudIndex, cloud2Index;
//	private int down_cloudIndex, down_cloud2Index;
	int x1 = 20, x2 = 550, x3 = 424;
	private void showHelp(SGraphics g) {
		Image help_bg = Resource.loadImage(Resource.id_achievement_bottom);
		Image shop_big = Resource.loadImage(Resource.id_shop_big);       //{137,108}
		Image game_help = Resource.loadImage(Resource.id_game_help);     //{214,18}
		Image achievement_out1 = Resource.loadImage(Resource.id_achievement_out1);   //{17,498}
		Image achievement_left_right = Resource.loadImage(Resource.id_achievement_left_right);   //{}
		Image achievement_left_right1 = Resource.loadImage(Resource.id_achievement_left_right1);   //{380,452}
		Image slash = Resource.loadImage(Resource.id_slash);
		/*增加云朵元素*/
		Image pass_cloud = Resource.loadImage(Resource.id_pass_cloud);
		Image pass_cloud1 = Resource.loadImage(Resource.id_pass_cloud1);
		Image pass_cloud2 = Resource.loadImage(Resource.id_pass_cloud2);
		engine.setFont(20,true);					//设置字体大小
		g.drawImage(help_bg, 0, 0, 20);
		/*增加的云层*/
		/*上面第二层云*/
		int cloud2W = pass_cloud2.getWidth()/*,cloud2H = pass_cloud2.getHeight()*/;
//		int len = cloud2W-ScrW;
		//int cloud2Y = -6;
		cloud2Index=(cloud2Index+1)%cloud2W;
		/*if(cloud2Index<=len){
			g.drawRegion(pass_cloud2, len-cloud2Index, 0, ScrW, cloud2H, 0, 0, cloud2Y, 20);
		}else{
			g.drawRegion(pass_cloud2, (cloud2W-cloud2Index), 0, ScrW-(cloud2W-cloud2Index), cloud2H, 0, 0, cloud2Y, 20);
			g.drawRegion(pass_cloud2, 0, 0, (cloud2W-cloud2Index), cloud2H, 0, ScrW-(cloud2W-cloud2Index), cloud2Y, 20);
		}*/
		
		/*下面第二层云*/
		/*int down_cloud2Y = 484;
		down_cloud2Index=(down_cloud2Index+1)%cloud2W;
		if(down_cloud2Index<=len){
			g.drawRegion(pass_cloud2, len-down_cloud2Index, 0, ScrW, cloud2H, 0, 0, down_cloud2Y, 20);
		}else{
			g.drawRegion(pass_cloud2, (cloud2W-down_cloud2Index), 0, ScrW-(cloud2W-down_cloud2Index), cloud2H, 0, 0, down_cloud2Y, 20);
			g.drawRegion(pass_cloud2, 0, 0, (cloud2W-down_cloud2Index), cloud2H, 0, ScrW-(cloud2W-down_cloud2Index), down_cloud2Y, 20);
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
		int cloud1W = pass_cloud1.getWidth()/*,cloud1H = pass_cloud1.getHeight()*/;
		//int cloud1Y = -23;
		cloudIndex=(cloudIndex+1)%cloud1W;
		/*if(cloudIndex<=cloud1W-ScrW){
			g.drawRegion(pass_cloud1, cloudIndex, 0, ScrW, cloud1H, 0, 0, cloud1Y, 20);
		}else{
			g.drawRegion(pass_cloud1, cloudIndex, 0, cloud1W-cloudIndex, cloud1H, 0, 0, cloud1Y, 20);
			g.drawRegion(pass_cloud1, 0, 0, cloudIndex, cloud1H, 0, cloud1W-cloudIndex, cloud1Y, 20);
		}*/
		
		/*下面第一层云*/
		/*int down_cloud1Y = 496;
		down_cloudIndex=(down_cloudIndex+1)%cloud1W;
		if(down_cloudIndex<=cloud1W-ScrW){
			g.drawRegion(pass_cloud1, down_cloudIndex, 0, ScrW, cloud1H, 0, 0, down_cloud1Y, 20);
		}else{
			g.drawRegion(pass_cloud1, down_cloudIndex, 0, cloud1W-down_cloudIndex, cloud1H, 0, 0, down_cloud1Y, 20);
			g.drawRegion(pass_cloud1, 0, 0, down_cloudIndex, cloud1H, 0, cloud1W-down_cloudIndex, down_cloud1Y, 20);
		}*/
		
		g.drawImage(shop_big, 137, 108, 20);
		
		int helpLeftRightX = 380,helpLeftRightY = 452,sapceLeftRight = 52;				//帮助界面中的按钮横纵坐标,sapceLeftRight左右间距
//		int helpShadowX = 4,helpShadowY = 4;
		int achLeftW = achievement_left_right.getWidth()/2;
//		int achRight1W = achievement_left_right1.getWidth()/2;
		g.drawImage(slash, helpLeftRightX+achLeftW+15, helpLeftRightY+7, 20);
		g.drawString("3", helpLeftRightX+achLeftW+slash.getWidth()+14, helpLeftRightY+8, 20);
		/*g.drawRegion(achievement_left_right1, 0, 0, achRight1W, 		//翻页左按钮
				achievement_left_right1.getHeight(), 0, helpLeftRightX, helpLeftRightY, 20);
		g.drawRegion(achievement_left_right1, achRight1W, 0,			//翻页右按钮
				achRight1W, achievement_left_right1.getHeight(),
				0, helpLeftRightX+sapceLeftRight+achRight1W, helpLeftRightY, 20);*/
		if(pageIndex == 0){
			g.drawRegion(achievement_left_right1, 0, 0, achLeftW, 		//翻页左按钮
					achievement_left_right.getHeight(), 0, helpLeftRightX, helpLeftRightY, 20);
			g.drawString(String.valueOf(helpX+1),helpLeftRightX+achLeftW+3,helpLeftRightY+8, 20);
			g.drawRegion(achievement_left_right, 1*achLeftW, 0, achLeftW,
					achievement_left_right.getHeight(), 0, helpLeftRightX/*-helpShadowX*/+sapceLeftRight+achLeftW,
					helpLeftRightY/*-helpShadowY*/, 20);
		}else if(pageIndex == 1){
			g.drawRegion(achievement_left_right, 0, 0, achLeftW, 		//翻页左按钮
					achievement_left_right.getHeight(), 0, helpLeftRightX/*-helpShadowX*/, helpLeftRightY/*-helpShadowY*/, 20);
			g.drawString(String.valueOf(helpX+1),helpLeftRightX+achLeftW+3,helpLeftRightY+8, 20);
			g.drawRegion(achievement_left_right1, 1*achLeftW, 0, achLeftW,
					achievement_left_right.getHeight(), 0, helpLeftRightX+sapceLeftRight+achLeftW,
					helpLeftRightY, 20);
		}
		g.drawImage(game_help, 220,18, 20);
		g.drawImage(achievement_out1, 17,498, 20);
		g.setColor(0x000000);
		TextView.showMultiLineText(g, Resource.gameInfo[helpX], 6,160, 135, 359, 333);	
		engine.setDefaultFont();
	}
	
	private void handleHelp(KeyState keyState) {
		if (keyState.containsAndRemove(KeyCode.NUM0 | KeyCode.BACK)) {
			running = false;
		}else if (keyState.containsAndRemove(KeyCode.LEFT)) {
			pageIndex = 0;
			if (helpX > 0) {
				helpX --;
			} else {
				helpX = 0;
			}
		}else if (keyState.containsAndRemove(KeyCode.RIGHT)) {
			pageIndex = 1;
			if (helpX < 2) {
				helpX ++;
			} else {
				helpX = 2;
			}
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
	
	private void clear() {
    	Resource.freeImage(Resource.id_achievement_bottom);
    	Resource.freeImage(Resource.id_achievement_out1);
    	Resource.freeImage(Resource.id_shop_big);
    	Resource.freeImage(Resource.id_game_help);
    	Resource.freeImage(Resource.id_achievement_left_right);
    	Resource.freeImage(Resource.id_achievement_left_right1);
    	Resource.freeImage(Resource.id_shop_figure);
    	Resource.freeImage(Resource.id_slash);
    	Resource.freeImage(Resource.id_pass_cloud);       
		Resource.freeImage(Resource.id_pass_cloud1);       
		Resource.freeImage(Resource.id_pass_cloud2);
	}
}
