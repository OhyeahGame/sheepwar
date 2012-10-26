package cn.ohyeah.stb.game;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

import sheepwar.SheepWarGameEngine;

import com.zte.iptv.j2me.stbapi.STBAPI;
import cn.ohyeah.stb.game.Configurations;
import cn.ohyeah.stb.res.ResourceManager;
import cn.ohyeah.stb.res.UIResource;
import cn.ohyeah.stb.ui.DrawUtil;
import cn.ohyeah.stb.ui.PopupText;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;

public class StateRecharge {
	
	private static final byte STATE_SELECT_AMOUNT = 0;
	private static final byte STATE_CONFIRM = 1;
	private static final byte STATE_INPUT_PWD = 2;
	
	private static final byte SUB_STATE_INPUT_PWD_VIEW = 0;
	private static final byte SUB_STATE_INPUT_PWD_SELECT_CHAR = 1;
	
	private static short NUM_PICS = 0;
	private static final short PIC_ID_RECHARGE_BG = NUM_PICS++;
	private static final short PIC_ID_RECHARGE_TITLE = NUM_PICS++;
	private static final short PIC_ID_CONFIRM_BG = NUM_PICS++;
	private static final short PIC_ID_OK0 = NUM_PICS++;
	private static final short PIC_ID_CANCEL0 = NUM_PICS++;
	private static final short PIC_ID_BACK1 = NUM_PICS++;
	private static final short PIC_ID_RECHARGE0 = NUM_PICS++;
	private static final short PIC_ID_PASSWORD_BG = NUM_PICS++;
	
	private static final String[] imagePaths = {
		"/business/recharge-bg.jpg",
		"/business/recharge-title.png",
		"/business/confirm-bg.jpg",
		"/business/ok0.png",
		"/business/cancel0.png",
		"/business/back1.png",
		"/business/recharge0.png",
		"/business/password-bg.png",
	};
	
	private static char[][] inputChars = {
		{'0'},
		{'1'},
		{'2', 'a', 'A', 'b', 'B', 'c', 'C'},
		{'3', 'd', 'D', 'e', 'E', 'f', 'F'},
		{'4', 'g', 'G', 'h', 'H', 'i', 'I'},
		{'5', 'j', 'J', 'k', 'K', 'l', 'L'},
		{'6', 'm', 'M', 'n', 'N', 'o', 'O'},
		{'7', 'p', 'P', 'q', 'Q', 'r', 'R', 's', 'S'},
		{'8', 't', 'T', 'u', 'U', 'v', 'V'},
		{'9', 'w', 'W', 'x', 'X', 'y', 'Y', 'z', 'Z'}
	};
	
	private SheepWarGameEngine engine;
	private byte groupIndex;
	private byte confirmIndex;
	private byte amountIndex;
	private byte state;
	private byte subState;
	private byte pwdGroupIndex;
	private byte pwdBtnIndex;
	private boolean back;
	private ResourceManager resource;
	private String password;
	private int pwdCharIndex;
	private char[] pwdChars;
	private int cursorFrame;
	private int count = 4;
	
	//充值参数
	private String feeCode;
	private String productName;
	private String priceDesc;
	
	public StateRecharge(SheepWarGameEngine engine) {
		this.engine = engine;
		resource = ResourceManager.createImageResourceManager(imagePaths);
	}
	
	public int recharge() {
		int result = 0;
		SGraphics g = engine.getSGraphics();
		KeyState KeyState = engine.getKeyState();
		groupIndex = 1;
		boolean run = true;
		try {
			while (run) {
				handle(KeyState);
				show(g);
				engine.flushGraphics();
				execute();
				
				if (back) {
					break;
				}
				engine.trySleep();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			result = -1;
		}
		finally {
			clear();
		}
		return result;
	}
	
	public void clear() {
		resource.clear();
	}
	
	private void execute() {
		switch(state) {
		case STATE_SELECT_AMOUNT: break;
		case STATE_CONFIRM: break;
		case STATE_INPUT_PWD: 
			cursorFrame = (cursorFrame+1)%8;
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}
	
	private void show(SGraphics g) {
		switch(state) {
		case STATE_SELECT_AMOUNT: 
			showSelectAmount(g);
			break;
		case STATE_CONFIRM: 
			showConfirm(g);
			break;
		case STATE_INPUT_PWD: 
			showInputPwd(g);
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}
	
	private void gotoStatePassword() {
		state = STATE_INPUT_PWD;
		subState = SUB_STATE_INPUT_PWD_VIEW;
		pwdGroupIndex = 0;
		cursorFrame = 0;
		password = "";
	}
	
	private void drawPassword(SGraphics g, int x, int y, int w, int h, boolean drawCursor, boolean drawChar) {
		g.setColor(0Xffff00);
		Font font = g.getFont();
		int charW = font.charWidth('*');
		int charH = font.getHeight();
		int sx = x;
		int sy  = y + (h-charH)/2;
		int len = password.length();
		if (len > 0) {
			for (int i = 0; i < len; ++i) {
				g.drawChar('*', sx, sy, 20);
				sx += charW+2;
			}
		}
		if (drawCursor) {
			if (cursorFrame < 4) {
				g.drawLine(sx, sy, sx, sy+charH);
			}
		}
		if (drawChar) {
			g.setColor(215, 215, 215);
			g.fillRect(sx, sy, 22, charH);
			g.setColor(0Xffff00);
			g.drawChar(pwdChars[pwdCharIndex], sx+(22-font.charWidth(pwdChars[pwdCharIndex]))/2, sy, 20);
		}
	}
	
	private void drawSelectChars(SGraphics g, int x, int y) {
		int sx = x;
		int sy = y;
		int sl = 22;
		Font font = g.getFont();
		int deltaH = (sl-font.getHeight())/2;
		g.setColor(215, 215, 215);
		g.fillRect(sx, sy, sl*pwdChars.length, sl);
		
		for (int i = 0; i < pwdChars.length; ++i) {
			g.setColor(0);
			g.drawChar(pwdChars[i], sx+(sl-font.charWidth(pwdChars[i]))/2, sy+deltaH, 20);
			if (i == pwdCharIndex) {
				g.setColor(0XFFFF00);
				DrawUtil.drawRect(g, sx, sy, sl, sl, 2);
			}
			sx += sl;
		}
	}
	
	private void showInputPwd(SGraphics g) {
		Image pwdBg = resource.loadImage(PIC_ID_PASSWORD_BG);
		int bgX = (engine.getScreenWidth()-pwdBg.getWidth())>>1;
		int bgY = (engine.getScreenHeight()-pwdBg.getHeight())>>1;
		
		bgX -= Configurations.Abs_Coords_X;
		bgY -= Configurations.Abs_Coords_Y;
		
		g.drawImage(pwdBg, bgX, bgY, 20);
		
		if (subState == SUB_STATE_INPUT_PWD_VIEW) {
			if (pwdGroupIndex == 0) {
				drawPassword(g, bgX+85, bgY+123, 285, 40, true, false);
			}
			else {
				drawPassword(g, bgX+85, bgY+123, 285, 40, false, false);	
			}
		}
		else {
			drawPassword(g, bgX+85, bgY+123, 285, 40, false, true);
		}
		
		if (subState == SUB_STATE_INPUT_PWD_SELECT_CHAR) {
			drawSelectChars(g, bgX+77, bgY+166);
		}
		
		Image ok = resource.loadImage(PIC_ID_OK0);
		g.drawImage(ok, bgX+120, bgY+280, 20);
		
		Image cancel = resource.loadImage(PIC_ID_CANCEL0);
		g.drawImage(cancel, bgX+266, bgY+280, 20);
		
		if (subState == SUB_STATE_INPUT_PWD_VIEW) {
			if (pwdGroupIndex == 1) {
				if (pwdBtnIndex == 0) {
					DrawUtil.drawRect(g, bgX+120, bgY+280, ok.getWidth(), ok.getHeight(), 2, 0XFFFF00);
				}
				else {
					DrawUtil.drawRect(g, bgX+266, bgY+280, cancel.getWidth(), cancel.getHeight(), 2, 0XFFFF00);
				}
			}
		}
	}

	private void showConfirm(SGraphics g) {
		Image bgImg = resource.loadImage(PIC_ID_CONFIRM_BG);
		int confirmX = (engine.getScreenWidth()-bgImg.getWidth())>>1;
		int confirmY = (engine.getScreenHeight()-bgImg.getHeight())>>1;
		
		confirmX -= Configurations.Abs_Coords_X;
		confirmY -= Configurations.Abs_Coords_Y;
		
		g.drawImage(bgImg, confirmX, confirmY, 20);
		
		//if (Configurations.getInstance().isServiceProviderWinside()) {
			g.setColor(0XFF0000);
			engine.setFont(26, true);
			g.drawString("iTV体验期内订购需正常付费", confirmX+70, confirmY+130, 20);
			engine.setDefaultFont();
		//}
		
		g.setColor(0XFFFF00);
		Font font = g.getFont();
		int textDelta = (25-font.getHeight())>>1;
		int sx = confirmX+170;
		int sy = confirmY+179+textDelta;
		g.drawString(productName, sx, sy, 20);
		
		sy = confirmY+216+textDelta;
		g.drawString(priceDesc, sx, sy, 20);
		
		
		Image confirmBtn = resource.loadImage(PIC_ID_OK0);
		sx = confirmX+121;
		sy = confirmY+253;
		g.drawImage(confirmBtn, sx, sy, 20);
		if (confirmIndex == 0) {
			DrawUtil.drawRect(g, sx, sy, confirmBtn.getWidth(), confirmBtn.getHeight(), 3, 0XFF0000);
		}
		
		Image backBtn = resource.loadImage(PIC_ID_CANCEL0);
		sx = confirmX+253;
		g.drawImage(backBtn, sx, sy, 20);
		if (confirmIndex == 1) {
			DrawUtil.drawRect(g, sx, sy, confirmBtn.getWidth(), confirmBtn.getHeight(), 3, 0XFF0000);
		}
	}

	private void showSelectAmount(SGraphics g) {
		g.setColor(43, 39, 36);
		
		Image bgImg = resource.loadImage(PIC_ID_RECHARGE_BG);
		g.drawImage(bgImg, 0, 0, 20);
		
		Font font = g.getFont();
		int amountsBgX = (engine.getScreenWidth()-434)>>1;
		int amountsBgY = (engine.getScreenHeight()-324)>>1;
		
		amountsBgX -= Configurations.Abs_Coords_X;
		amountsBgY -= Configurations.Abs_Coords_Y;
		
		Image title = resource.loadImage(PIC_ID_RECHARGE_TITLE);
		
		if (title != null) {
			g.drawImage(title, amountsBgX + ((434-title.getWidth())>>1),amountsBgY + 17, 20);
		}
		
		/*显示支付方式*/
		g.setColor(0XFFFF00);
		int sx, sy, sw, sh;
		String ss = "";
		
		/*显示金额列表*/
		sw = 276;
		sh = 33;
		int amountY = 0;
		String unit = "游戏币";
		int amount = 0;
		Image recharge = resource.loadImage(PIC_ID_RECHARGE0);
		
		int textDelta = (sh-font.getHeight())>>1;
		int btnDelta = (sh-recharge.getHeight())>>1;
		
		int btnX = amountsBgX+330;
		sx = amountsBgX+33;
		sy = amountsBgY+90;
		for (int i = 0; i < count; ++i) {
			g.setColor(0X062C39);
			g.fillRect(sx, sy, sw, sh);
			g.setColor(0XA2FFFF);
            DrawUtil.drawRect(g, sx, sy, sw, sh, 2);
            if(i==0){
	        	amount = 10;
	        	ss = "1元";
	        }else if(i==1){
	        	amount = 20;
	        	ss = "2元";
	        }else if(i==2){
	        	amount = 50;
	        	ss = "5元";
	        }else if(i==3){
	        	amount = 100;
	        	ss = "10元";
	        }
			g.setColor(0XFFFF00);
			g.drawString(amount+unit, sx+2, sy+textDelta, 20);
			g.setColor(0XFFFF00);
			g.drawString(ss, sx+sw-font.stringWidth(ss)-2, sy+textDelta, 20);
			
			g.drawImage(recharge, btnX, sy+btnDelta, 20);
			if (groupIndex == 1 && amountIndex == i) {
				amountY = sy;
			}
			sy += sh+10;
		}
		
		
		Image back = resource.loadImage(PIC_ID_BACK1);
		sx = amountsBgX+((434-back.getWidth())>>1);
		sy = amountsBgY+284;
		g.drawImage(back, sx, sy, 20);
		
		if (groupIndex == 1) {
			DrawUtil.drawRect(g, btnX, amountY+2, recharge.getWidth(), recharge.getHeight(), 2, 0XFFFF00);
		}
		else if (groupIndex == 2){
			DrawUtil.drawRect(g, sx, sy, back.getWidth(), back.getHeight(), 2, 0XFFFF00);
		}
	}
	
	private void handle(KeyState key) {
		switch(state) {
		case STATE_SELECT_AMOUNT: 
			handleSelectAmount(key);
			break;
		case STATE_CONFIRM: 
			handleConfirm(key);
			break;
		case STATE_INPUT_PWD: 
			handleInputPwd(key);
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}

	private void handleInputPwdSelectChar(KeyState key) {
		if (key.containsAndRemove(KeyCode.BACK|KeyCode.UP)) {
			subState = SUB_STATE_INPUT_PWD_VIEW;
		}
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (pwdCharIndex > 0) {
				pwdCharIndex--;
			}
			else {
				pwdCharIndex = pwdChars.length-1;
			}
		}
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (pwdCharIndex < pwdChars.length-1) {
				pwdCharIndex++;
			}
			else {
				pwdCharIndex = 0;
			}
		}
		if (key.containsAndRemove(KeyCode.OK)) {
			key.clear();
			subState = SUB_STATE_INPUT_PWD_VIEW;
			password += pwdChars[pwdCharIndex];
		}
	}
	
	private void handleInputPwdView(KeyState key) {
		if (key.containsAndRemove(KeyCode.NUM0)) {
			key.clear();
			subState = SUB_STATE_INPUT_PWD_SELECT_CHAR;
			pwdChars = inputChars[0];
			pwdCharIndex = 0;
		}
		if (key.containsAndRemove(KeyCode.NUM1)) {
			subState = SUB_STATE_INPUT_PWD_SELECT_CHAR;
			pwdChars = inputChars[1];
			pwdCharIndex = 0;
		}
		if (key.containsAndRemove(KeyCode.NUM2)) {
			subState = SUB_STATE_INPUT_PWD_SELECT_CHAR;
			pwdChars = inputChars[2];
			pwdCharIndex = 0;
		}
		if (key.containsAndRemove(KeyCode.NUM3)) {
			subState = SUB_STATE_INPUT_PWD_SELECT_CHAR;
			pwdChars = inputChars[3];
			pwdCharIndex = 0;
		}
		if (key.containsAndRemove(KeyCode.NUM4)) {
			subState = SUB_STATE_INPUT_PWD_SELECT_CHAR;
			pwdChars = inputChars[4];
			pwdCharIndex = 0;
		}
		if (key.containsAndRemove(KeyCode.NUM5)) {
			subState = SUB_STATE_INPUT_PWD_SELECT_CHAR;
			pwdChars = inputChars[5];
			pwdCharIndex = 0;
		}
		if (key.containsAndRemove(KeyCode.NUM6)) {
			subState = SUB_STATE_INPUT_PWD_SELECT_CHAR;
			pwdChars = inputChars[6];
			pwdCharIndex = 0;
		}
		if (key.containsAndRemove(KeyCode.NUM7)) {
			subState = SUB_STATE_INPUT_PWD_SELECT_CHAR;
			pwdChars = inputChars[7];
			pwdCharIndex = 0;
		}
		if (key.containsAndRemove(KeyCode.NUM8)) {
			subState = SUB_STATE_INPUT_PWD_SELECT_CHAR;
			pwdChars = inputChars[8];
			pwdCharIndex = 0;
		}
		if (key.containsAndRemove(KeyCode.NUM9)) {
			subState = SUB_STATE_INPUT_PWD_SELECT_CHAR;
			pwdChars = inputChars[9];
			pwdCharIndex = 0;
		}
		if (key.containsAndRemove(KeyCode.BACK)) {
			if (password.length()>1) {
				password = password.substring(0, password.length()-1);
			}
			else {
				password = "";
			}
		}
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (pwdGroupIndex == 0) {
				pwdGroupIndex = 1;
				pwdBtnIndex = 0;
			}
		}
		
		if (key.containsAndRemove(KeyCode.UP)) {
			if (pwdGroupIndex == 1) {
				pwdGroupIndex = 0;
			}
		}
		
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (pwdGroupIndex == 1) {
				if (pwdBtnIndex == 1) {
					pwdBtnIndex = 0;
				}
			}
		}
		
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (pwdGroupIndex == 1) {
				if (pwdBtnIndex == 0) {
					pwdBtnIndex = 1;
				}
			}
		}
		
		if (key.containsAndRemove(KeyCode.OK)) {
			key.clear();
			if (pwdGroupIndex == 0) {
				pwdGroupIndex = 1;
				pwdBtnIndex = 0;
			}
			else {
				if (pwdBtnIndex == 0) { //确定
					gotoRecharge();
					PopupText pt = UIResource.getInstance().buildDefaultPopupText();
					if(engine.account.getResult()==0){
						pt.setText("充值成功!");
						pt.popup();
						resource.freeImage(PIC_ID_PASSWORD_BG);
						state = STATE_SELECT_AMOUNT;
					}else{
						pt.setText("充值失败，原因："+getErrorMessage(engine.account.getResult()));
						pt.popup();
						resource.freeImage(PIC_ID_PASSWORD_BG);
						state = STATE_SELECT_AMOUNT;
					}
				}
				else {
					resource.freeImage(PIC_ID_PASSWORD_BG);
					state = STATE_SELECT_AMOUNT;
				}
			}
		}
	}
	
	private String getErrorMessage(int errorCode){
		switch (errorCode){
		case 9103:
			return "充值密码不对";
		case 2023:
			return "月消费达到限额";
		case 2026:
			return "充值代码不存在";
		case 9999:
			return "充值失败";
		default: return "未知错误";
		}
	}
	
	public void gotoRecharge() {
		try {
			engine.account = STBAPI.Recharge(feeCode,0,priceDesc,password);
			System.out.println("desc:"+engine.account.getDesc());
			System.out.println("balance:"+engine.account.getBalance());
			System.out.println("result:"+engine.account.getResult());
			if (engine.account != null && engine.account.getResult() == 0){
				System.out.println("充值成功");
			} else{
				System.out.println("充值失败");
			}
		}catch (Exception e)
		{
			PopupText pt = UIResource.getInstance().buildDefaultPopupText();
			pt.setText("充值失败，原因："+ engine.getErrorMessage(engine.account.getResult()));
			pt.popup();
		}
	}
	
	private void getFeeCode(int index){
		if(index==0){
			feeCode = STBAPI.SysConfig.Fee100;
			productName = "10"+engine.amountUnit;
			priceDesc = "1元";
		}else if(index==1){
			feeCode = STBAPI.SysConfig.Fee200;
			productName = "20"+engine.amountUnit;
			priceDesc = "2元";
		}else if(index==2){
			feeCode = STBAPI.SysConfig.Fee500;
			productName = "50"+engine.amountUnit;
			priceDesc = "5元";
		}else if(index==3){
			feeCode = STBAPI.SysConfig.Fee1000;
			productName = "100"+engine.amountUnit;
			priceDesc = "10元";
		} 
	}
	
	private void handleInputPwd(KeyState key) {
		if (subState == SUB_STATE_INPUT_PWD_VIEW) {
			handleInputPwdView(key);
		}
		else {
			handleInputPwdSelectChar(key);
		}
	}
	
	/*private boolean isPasswordError(String msg) {
		boolean pwdError = false;
		if (msg.indexOf("密码")>=0) {
			if (msg.indexOf("错误") > 0
				|| msg.indexOf("校验失败")>0) {
				pwdError = true;
			}
		}
		return pwdError;
	}*/

	private void handleConfirm(KeyState key) {
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (confirmIndex == 1) {
				confirmIndex = 0;
			}
		}
		
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (confirmIndex == 0) {
				confirmIndex = 1;
			}
		}
		
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			resource.freeImage(PIC_ID_RECHARGE_BG);
			resource.freeImage(PIC_ID_OK0);
			resource.freeImage(PIC_ID_CANCEL0);
			state=STATE_SELECT_AMOUNT;
		}
		
		if (key.containsAndRemove(KeyCode.OK)) {
			key.clear();
			if (confirmIndex == 0) {
				gotoStatePassword();
				state = STATE_INPUT_PWD;
			}
			else {
				resource.freeImage(PIC_ID_CONFIRM_BG);
				state=STATE_SELECT_AMOUNT;
			}
		}
	}

	private void handleSelectAmount(KeyState key) {
		if (key.containsAndRemove(KeyCode.UP)) {
			if (groupIndex == 1){
				if (amountIndex > 0) {
					--amountIndex;
				}
			}
			else if (groupIndex == 2){
				groupIndex = 1;
			}
		}
		
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (groupIndex == 1) {
				if (amountIndex < count-1) {
					++amountIndex;
				}
				else {
					groupIndex = 2;
				}
			}
		}
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			clear();
			back = true;
			try {
				engine.account = STBAPI.GetBalance();
				System.out.println("查询余额为："+engine.account.getBalance());
			} catch (Exception e) {
				PopupText pt = UIResource.getInstance().buildDefaultPopupText();
				pt.setText("查询余额失败，原因："+engine.getErrorMessage(engine.account.getResult()));
				pt.popup();
			} 
		}
		
		if (key.containsAndRemove(KeyCode.OK)) {
			key.clear();
			if (groupIndex == 1) {
				getFeeCode(amountIndex);
				state = STATE_CONFIRM;
			}
			else {
				back = true;
				resource.freeImage(PIC_ID_RECHARGE_BG);
			}
		}
	}
}
