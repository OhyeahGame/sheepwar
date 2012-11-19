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

	int x1 = 20, x2 = 550, x3 = 424;
	private void showHelp(SGraphics g) {
		Image help_bg = Resource.loadImage(Resource.id_achievement_bottom);
		Image shop_big = Resource.loadImage(Resource.id_shop_big);       //{137,108}
		Image help_cloth = Resource.loadImage(Resource.id_help_cloth);       //{137,108}
		Image game_help = Resource.loadImage(Resource.id_game_help);     //{214,18}
		Image achievement_out1 = Resource.loadImage(Resource.id_achievement_out1);   //{17,498}
		Image achievement_left_right = Resource.loadImage(Resource.id_achievement_left_right);   //{}
		Image achievement_left_right1 = Resource.loadImage(Resource.id_achievement_left_right1);   //{380,452}
		Image slash = Resource.loadImage(Resource.id_slash);
		Image pass_cloud = Resource.loadImage(Resource.id_pass_cloud);
		engine.setFont(20,true);					//设置字体大小
		g.drawImage(help_bg, 0, 0, 20);

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
		
		g.drawImage(shop_big, 137, 108, 20);
		g.drawImage(help_cloth, 157, 108+26, 20);
		
		int helpLeftRightX = 380,helpLeftRightY = 452,sapceLeftRight = 52;				//帮助界面中的按钮横纵坐标,sapceLeftRight左右间距
		int achLeftW = achievement_left_right.getWidth()/2;
		engine.stateGame.drawNum(g, helpX+1, helpLeftRightX+achLeftW+1, helpLeftRightY+6);
		g.drawImage(slash, helpLeftRightX+achLeftW+15, helpLeftRightY+4, 20);
		engine.stateGame.drawNum(g, 3, helpLeftRightX+achLeftW+slash.getWidth()+12, helpLeftRightY+6);
		if(pageIndex == 0){
			g.drawRegion(achievement_left_right1, 0, 0, achLeftW, 		//翻页左按钮
					achievement_left_right.getHeight(), 0, helpLeftRightX, helpLeftRightY, 20);
			g.drawRegion(achievement_left_right, 1*achLeftW, 0, achLeftW,
					achievement_left_right.getHeight(), 0, helpLeftRightX/*-helpShadowX*/+sapceLeftRight+achLeftW,
					helpLeftRightY/*-helpShadowY*/, 20);
		}else if(pageIndex == 1){
			g.drawRegion(achievement_left_right, 0, 0, achLeftW, 		//翻页左按钮
					achievement_left_right.getHeight(), 0, helpLeftRightX/*-helpShadowX*/, helpLeftRightY/*-helpShadowY*/, 20);
			g.drawRegion(achievement_left_right1, 1*achLeftW, 0, achLeftW,
					achievement_left_right.getHeight(), 0, helpLeftRightX+sapceLeftRight+achLeftW,
					helpLeftRightY, 20);
		}
		g.drawImage(game_help, 220,18, 20);
		g.drawImage(achievement_out1, 17,498, 20);
		g.setColor(0x000000);
		TextView.showMultiLineText(g, Resource.gameInfo[helpX], 5,177, 150, 315, 248);	
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

	private void clear() {
    	Resource.freeImage(Resource.id_achievement_bottom);
    	Resource.freeImage(Resource.id_shop_big);
    	Resource.freeImage(Resource.id_game_help);
    	Resource.freeImage(Resource.id_achievement_out1);
    	Resource.freeImage(Resource.id_achievement_left_right);
    	Resource.freeImage(Resource.id_achievement_left_right1);
    	Resource.freeImage(Resource.id_slash);
    	Resource.freeImage(Resource.id_pass_cloud);       
    	Resource.freeImage(Resource.id_help_cloth);       
	}
}
