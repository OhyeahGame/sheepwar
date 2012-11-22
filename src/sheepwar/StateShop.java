package sheepwar;

import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.game.SGraphics;
import cn.ohyeah.stb.game.StateRecharge;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;
import cn.ohyeah.stb.res.UIResource;
import cn.ohyeah.stb.ui.PopupConfirm;
import cn.ohyeah.stb.ui.TextView;

public class StateShop implements Common{
	
	private SheepWarGameEngine engine;
	private boolean running;
	private int shopX,  shopY;
	
	public StateShop(SheepWarGameEngine engine){
		this.engine = engine;
	}
	
	public void processShop(){
		running = true;
		try {
			KeyState keyState = engine.getKeyState();
			SGraphics g = engine.getSGraphics();
			while (running) {
				handleShop(keyState);
				if (running) {
					long t1 = System.currentTimeMillis();
					showShop(g);
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
	private void showShop(SGraphics g) {
		Image game_bg = Resource.loadImage(Resource.id_shop_bottom);
		Image shop_balance = Resource.loadImage(Resource.id_shop_balance);//{46,454}
		Image shop_big = Resource.loadImage(Resource.id_shop_big);//{29,103}
		Image shop_go_pay = Resource.loadImage(Resource.id_shop_go_pay);//{457,381}				//9/29被修改为单纯的文字图片
		Image shop_go_pay_base = Resource.loadImage(Resource.id_achievement_left);				//单纯的 按钮图片
		Image shop_go_pay_base1 = Resource.loadImage(Resource.id_achievement_left1);				//单纯的 按钮图片
		
		Image shop_midding = Resource.loadImage(Resource.id_shop_midding);//{434,103}
		Image shop_cloth = Resource.loadImage(Resource.id_shop_cloth);//{434,103}
		Image shop_out = Resource.loadImage(Resource.id_shop_out);//{457,429}					//9/29被修改为单纯的文字图片“返回”
		Image shop_small = Resource.loadImage(Resource.id_shop_small);
		Image price_quantity = Resource.loadImage(Resource.id_price_quantity);
		Image shop = Resource.loadImage(Resource.id_shop);//{217,18}
		Image playing_prop=Resource.loadImage(Resource.id_playing_prop);
		Image shop_stroke=Resource.loadImage(Resource.id_stroke);					//添加的描边 11-12 by Lee
		Image shop_selected=Resource.loadImage(Resource.id_shop_selected);
		g.setColor(0xffffff);
		g.drawImage(game_bg, 0, 0, 20);
	
		g.drawImage(shop, 220, 18, 20);
		g.drawImage(shop_big, 29-12, 103, 20);
		g.drawImage(shop_balance, 46, 457, 20);
		g.drawImage(shop_midding, 434-12, 103, 20);
		g.drawImage(shop_cloth, 434+10, 103+26, 20);
		
		int x =52, y = 130, spaceX = 15, spaceY = 5;
		int smallW = shop_small.getWidth(), smallH = shop_small.getHeight();
		int p_propW = playing_prop.getWidth()/8, p_propH = playing_prop.getHeight();
		int  strokeW = shop_stroke.getWidth(),strokeH = shop_stroke.getHeight();
		int priAndQuW = price_quantity.getWidth(),priAndQuH = price_quantity.getHeight();
		for(int i=0;i<4;i++){
		     for(int j=0;j<2;j++){
		    	 if(shopX==j && shopY==i){
						engine.setFont(30,true);	
						g.setColor(0xffffff);
						g.drawImage(shop_selected, x+(spaceX+smallW)*j, y+(spaceY+smallH)*i, 20);
						g.drawRegion(price_quantity, 0, 0, priAndQuW, priAndQuH, 0,  x+(spaceX+smallW)*j+80, y+(spaceY+smallH)*i+12, 20);
						g.drawRegion(playing_prop, getPropIndex(i, j)*p_propW, 0, p_propW, p_propH, 0,x+(spaceX+smallW)*j+16, y+(spaceY+smallH)*i+6, 20);
//						g.drawRegion(shop_stroke, /*getPropIndex(i, j)*p_propW*/0, 0, strokeW, strokeH, 0,x+(spaceX+smallW)*j+16, y+(spaceY+smallH)*i+6, 20);
						g.drawString(String.valueOf(engine.props[getPropIndex(i, j)].getPrice()),
								x+(spaceX+smallW)*j+136, y+(spaceY+smallH)*i+13, 20);
						g.drawString(String.valueOf(engine.props[getPropIndex(i, j)].getNums()), 
								x+(spaceX+smallW)*j+136, y+(spaceY+smallH)*i+39, 20);
						g.setColor(0x000000);
						engine.setFont(25, true);
						TextView.showMultiLineText(g, Resource.propIntroduce[shopY][shopX], 2, 452, 142, 140, 195);
						engine.setDefaultFont();
					}else{
						engine.setFont(30,true);
						int col = g.getColor();
						g.setColor(0x000000);
						g.drawImage(shop_small, x+(spaceX+smallW)*j, y+(spaceY+smallH)*i, 20);
						g.drawRegion(price_quantity,0/* priAndQuW*/, 0, priAndQuW, priAndQuH, 0, x+(spaceX+smallW)*j+80, y+(spaceY+smallH)*i+12,20);
						g.drawRegion(playing_prop, getPropIndex(i, j)*p_propW, 0, p_propW, p_propH, 0,x+(spaceX+smallW)*j+16, y+(spaceY+smallH)*i+6, 20);
						g.drawString(String.valueOf(engine.props[getPropIndex(i, j)].getPrice()),
								x+(spaceX+smallW)*j+136, y+(spaceY+smallH)*i+13, 20);
						g.drawString(String.valueOf(engine.props[getPropIndex(i, j)].getNums()), 
								x+(spaceX+smallW)*j+136, y+(spaceY+smallH)*i+39, 20);
						engine.setDefaultFont();
						g.setColor(col);
					}
		    	 g.drawRegion(shop_stroke, /*getPropIndex(i, j)*p_propW*/0, 0, strokeW, strokeH, 0,x+(spaceX+smallW)*j+16, y+(spaceY+smallH)*i+6, 20);
			}
		}
		if(shopX==2){          // changed on 11-7 by Lee : removed the slash effect
			 if(shopY==0){      //控制方向由左到右的入口方向
				 g.drawImage(shop_go_pay_base1, 457-8, 381-5, 20);
			   	 g.drawImage(shop_go_pay, 457-8+16, 381-5+5, 20);
			 	 g.drawImage(shop_go_pay_base, 457-8, 429-5, 20);
			   	 g.drawImage(shop_out, 457+16, 429+7, 20);
			  }else{
			   	 g.drawImage(shop_go_pay_base, 457-8, 381-5, 20);
			   	 g.drawImage(shop_go_pay, 457-8+16, 381-5+5, 20);
			 	 g.drawImage(shop_go_pay_base1, 457-8, 429-5, 20);
			   	 g.drawImage(shop_out, 457+16, 429+7, 20);
			 }
		}else{
	   	 g.drawImage(shop_go_pay_base, 457-8, 381-5, 20);
	   	 g.drawImage(shop_go_pay, 457-8+16, 381-5+5, 20);
	   	 g.drawImage(shop_go_pay_base, 457-8, 429-5, 20);
	   	 g.drawImage(shop_out, 457+16, 429+7, 20);
		}
		 
		int balance=0;
		if(engine.account!=null){
			balance = engine.account.getBalance();
		}
		engine.setFont(30, true);
		g.setColor(0x000000);
		g.drawString(String.valueOf(balance/10)+engine.amountUnit,110,449+7, 20);//用户余额
		engine.setDefaultFont();
	}
	
	private int getPropIndex(int x, int y){
		if(x==0 && y==0)return 0;
		if(x==1 && y==0)return 1;
		if(x==2 && y==0)return 2;
		if(x==3 && y==0)return 3;
		if(x==0 && y==1)return 4;
		if(x==1 && y==1)return 5;
		if(x==2 && y==1)return 6;
		if(x==3 && y==1)return 7;
		return -1;
	}
	
	private void handleShop(KeyState keyState) {
		if (keyState.containsAndRemove(KeyCode.NUM0 | KeyCode.BACK)) {
			running = false;
			shopX = 0;
			shopY = 0;
			
			/*同步道具*/
			engine.pm.sysProps();
		}else if (keyState.contains(KeyCode.UP)) {
			keyState.remove(KeyCode.UP);
			if(shopY>0){
				if(shopX==2){
					shopY=(shopY-1)%2;
				}else{
					shopY=(shopY-1)%4;
				}
			}
		}else if (keyState.contains(KeyCode.DOWN)) {
			keyState.remove(KeyCode.DOWN);
			if(shopY<4){
				if(shopX<2){
					shopY = (shopY+1)%4;
				}else{
					shopY = (shopY+1)%2;
				}
			}
		}else if (keyState.contains(KeyCode.LEFT)) {
			keyState.remove(KeyCode.LEFT);
			if(shopX>0){
				shopX = shopX-1;
			}
		}else if (keyState.contains(KeyCode.RIGHT)) {
			keyState.remove(KeyCode.RIGHT);
			if(shopX<2){
				shopX = (shopX+1)%3;
			}
			if(shopX==2){//当控制由左到右时，shopY置零
				shopY=0;
			}
		}else if (keyState.contains(KeyCode.OK)) {
			keyState.remove(KeyCode.OK);
			if(shopX==2 && shopY==0){//进入充值
				/*keyState.clear();
				engine.state = STATUS_GAME_RECHARGE;
				engine.isRecharge = false;
				running = false;*/
				clear();
				StateRecharge sr = new StateRecharge(engine);
				int amount = sr.recharge();
				if(amount>0){
					String data = "userId:"+engine.userinfo.getUserID()+"|time:"+engine.getTime()+"|amount:"+amount;
					engine.saveData("用户充值", data);
				}
			}else if(shopX==2 && shopY==1){
				running = false;
				shopX = 0;shopY = 0;
			}else{
				PopupConfirm pc = UIResource.getInstance().buildDefaultPopupConfirm();
				pc.setText("确定要购买吗?");
				if(pc.popup()==0){
					int amount = engine.pm.purchaseProp(shopX, shopY);
					if(amount>0){
						String data = "userId:"+engine.userinfo.getUserID()+"|time:"+engine.getTime()+"|price:"+amount/10;
						engine.saveData("购买道具", data);
					}
				}
			}
		}
	}

	private void clear() {
		Resource.freeImage(Resource.id_shop_bottom);
		Resource.freeImage(Resource.id_shop_balance);
		Resource.freeImage(Resource.id_shop_big);
		Resource.freeImage(Resource.id_shop_go_pay);
		Resource.freeImage(Resource.id_achievement_left);
		Resource.freeImage(Resource.id_achievement_left1);
		Resource.freeImage(Resource.id_shop_midding);
		Resource.freeImage(Resource.id_shop_out);
		Resource.freeImage(Resource.id_shop_small_base);    
		Resource.freeImage(Resource.id_shop_small);       
		Resource.freeImage(Resource.id_price_quantity);       
		Resource.freeImage(Resource.id_shop);   
		Resource.freeImage(Resource.id_playing_prop);   
		Resource.freeImage(Resource.id_shop_selected);   
		Resource.freeImage(Resource.id_shop_cloth);   
		Resource.freeImage(Resource.id_stroke);   
	}
}
