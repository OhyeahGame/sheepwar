package sheepwar;

import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.game.SGraphics;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;

public class StateSubMenu implements Common{

	
	private SheepWarGameEngine engine = SheepWarGameEngine.instance;
	private boolean running;
	private int menuIndex;
	
	public int processSubMenu(){
		running = true;
		try {
			KeyState keyState = engine.getKeyState();
			SGraphics g = engine.getSGraphics();
			while (running) {
				handleSubMenu(keyState);
				if (running) {
					long t1 = System.currentTimeMillis();
					showSubMenu(g);
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
		return menuIndex;
	}

	private void showSubMenu(SGraphics g) {
		Image submenu_bg = Resource.loadImage(Resource.id_sub_menu_bg);
		Image submenu = Resource.loadImage(Resource.id_sub_menu);
		Image achievement_left1 = Resource.loadImage(Resource.id_achievement_left1);
		Image achievement_left = Resource.loadImage(Resource.id_achievement_left);
		
		int sx = gameW/2-submenu_bg.getWidth()/2;
		int sy = ScrH/2-submenu_bg.getHeight()/2;
		g.drawImage(submenu_bg, sx, sy, 20);
		
		int sw = submenu.getWidth(), sh = submenu.getHeight() / 4;
		sx = gameW/2-sw/2;
		sy += 35;
		int  subSpaceY = 5;
		int subStripeW = achievement_left.getWidth(),subStripeH = achievement_left.getHeight();
		for (int i = 0; i < 4; ++i) {
			if(menuIndex != i){
				g.drawRegion(achievement_left, 0, 0, subStripeW, subStripeH, 0,
						178, 160+(subSpaceY+subStripeH)*i, 20);
			}else{
				g.drawRegion(achievement_left1, 0, 0, subStripeW, subStripeH, 0,
						178, 160 +(subSpaceY+subStripeH) * i, 20);
			}
			g.drawRegion(submenu,/*(menuIndex != i) ? sw : */0, i * sh, sw, sh, 0, sx, sy, 0);	//changed on 11-8 by Lee 
			sy += sh+25;
		}
	}
	
	private void handleSubMenu(KeyState keyState) {
		if (keyState.containsAndRemove(KeyCode.NUM0 | KeyCode.BACK)) {
			running = false;
			menuIndex = 0;
		}else if(keyState.containsAndRemove(KeyCode.UP)){
			menuIndex = (menuIndex+4-1)%4;
		}else if(keyState.containsAndRemove(KeyCode.DOWN)){
			menuIndex = (menuIndex+1)%4;
		}else if(keyState.containsAndRemove(KeyCode.OK)){
			running = false;
		}
	}

	private void clear() {
		Resource.freeImage(Resource.id_sub_menu);
		Resource.freeImage(Resource.id_sub_menu_bg);
		Resource.freeImage(Resource.id_achievement_left1);
		Resource.freeImage(Resource.id_achievement_left);
	}
}
