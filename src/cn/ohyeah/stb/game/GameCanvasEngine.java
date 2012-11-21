package cn.ohyeah.stb.game;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.MIDlet;

import cn.ohyeah.stb.res.UIResource;
import cn.ohyeah.stb.ui.TextView;
import cn.ohyeah.stb.key.KeyState;

/**
 * 使用GameCanvas的游戏引擎
 * @author maqian
 * @version 1.0
 */
abstract public class GameCanvasEngine extends GameCanvas implements Runnable, IEngine{

	private static final byte STATE_USER_LOOP = 0;
	private static final byte STATE_LOADING = 1;
	
	private static boolean __RELEASE = true;
	private static final int __INIT_LOOP_CIRCLE = 175;
	
	private int state;
	protected MIDlet midlet;
	protected KeyState keyState;
	protected SGraphics g;
	protected int screenWidth;
	protected int screenHeight;
	protected int loopCircle;
	protected boolean exit;
	protected long appStartTimeMillis;
	protected int smallFontSize, mediumFontSize, largeFontSize;
	protected DebugModule debugModule;
	private long recordMillis;
	protected int loadingProgress;
	protected String loadingMessage;
	private String errorMessage;
	
	protected GameCanvasEngine(MIDlet midlet) {
		super(false);
		this.midlet = midlet;
		loopCircle = __INIT_LOOP_CIRCLE;
		appStartTimeMillis = System.currentTimeMillis();
		setFullScreenMode(true);
		keyState = new KeyState();
		g = new SGraphics(super.getGraphics(),Configurations.Abs_Coords_X,Configurations.Abs_Coords_Y);
		initFontSize();
		setDefaultFont();
		screenWidth = getWidth();
		screenHeight = getHeight();
		
		debugModule = new DebugModule(this);
		UIResource.registerEngine(this);
	}
	public boolean isDebugMode() {
		return !__RELEASE&&debugModule.isDebugMode();
	}
	
	public boolean isReleaseVersion() {
		return __RELEASE;
	}
	
	protected void setRelease(boolean b) {
		__RELEASE = b;
	}
	
	public boolean isRunning() {
		return !exit;
	}
	
	public void setExit() {
		exit = true;
	}
	
	public void setLoopCircle(int circle) {
		loopCircle = circle;
	}
	
	public int getLoopCircle() {
		return loopCircle;
	}
	
	private void initFontSize() {
		Font font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
		smallFontSize = font.getHeight();
		font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
		mediumFontSize = font.getHeight();
		font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_LARGE);
		largeFontSize = font.getHeight();
	}
	
	public void setFont(int size, boolean isBold) {
		Font font = null;
		if(!isBold){
			if (size <= smallFontSize) {
				font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
			}
			else if (size <= mediumFontSize) {
				if (size >= smallFontSize+((mediumFontSize-smallFontSize)>>1)) {
					font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
				}
				else {
					font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
				}
			}
			else if (size <= largeFontSize) {
				if (size >= mediumFontSize+((largeFontSize-mediumFontSize)>>1)) {
					font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_LARGE);
				}
				else {
					font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
				}
			}
			else {
				font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_LARGE);
			}
		}else{
			if (size <= smallFontSize) {
				font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_SMALL);
			}
			else if (size <= mediumFontSize) {
				if (size >= smallFontSize+((mediumFontSize-smallFontSize)>>1)) {
					font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
				}
				else {
					font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_SMALL);
				}
			}
			else if (size <= largeFontSize) {
				if (size >= mediumFontSize+((largeFontSize-mediumFontSize)>>1)) {
					font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE);
				}
				else {
					font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
				}
			}
			else {
				font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE);
			}
		}
		g.setFont(font);
	}

	public void setDefaultFont() {
		setFont(20, false);
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public final void flushGraphics() {
		if (isDebugMode()) {
			debugModule.showDebugInfo(g);
		}
		super.flushGraphics();
	}
	
	public SGraphics getSGraphics() {
		return g;
	}

	public KeyState getKeyState() {
		return keyState;
	}
	
	public Font getFont() {
		return g.getFont();
	}
	
	public void setFont(Font font) {
		g.setFont(font);
	}
	
	public final void keyPressed(int keyCode) {
		keyState.keyPressed(keyCode);
		if (!__RELEASE) {
			debugModule.checkDebugCmd(keyCode, keyState.getKeyChar());
		}
	}
	
	public final void keyReleased(int keyCode) {
		keyState.keyReleased(keyCode);
	}

	public final void run() {
		state = STATE_LOADING;
		runLoop();
		midlet.notifyDestroyed();
	}
	
	private void runLoop() {
		recordMillis = System.currentTimeMillis();
		while (isRunning()) {
			try {
				switch (state) {
				case STATE_USER_LOOP: 
					loop();
					break;
				case STATE_LOADING:
					loading();
					break;
				default:
					throw new RuntimeException("无效的状态参数: state="+state);
				}
				flushGraphics();
				trySleep();
			}
			catch (Throwable t) {
				t.printStackTrace();
				System.out.println("\n程序发生异常，请记录异常发生时的上下文，并将所有异常信息提交开发人员分析, Thanks!");
				System.gc();
				errorMessage = "程序发生异常，"+t.getMessage()
							+"\n对您造成的不便深表歉意，我们会尽快修复此问题，感谢您的关注!\n请按任意键退出!";
			}
		}
	}
	
	private void loading() {
		g.setClip(0, 0, screenWidth, screenHeight);
		g.setColor(0x000000);
		String str = "游戏加载中...";
		int x = screenWidth/2-25;
		int y = screenHeight/2;
		g.drawString(str, x, y, 20);
		state = STATE_USER_LOOP;
	}
	protected void showError() {
		g.setColor(0);
		g.fillRect(-Configurations.Abs_Coords_X, -Configurations.Abs_Coords_Y, screenWidth, screenHeight);
		g.setColor(0XFFFFFF);
		TextView.showMultiLineText(g, errorMessage, 1, 10, 20, screenWidth, screenHeight);
	}
	
	public void addDebugUserMessage(String msg) {
		if (isDebugMode()) {
			debugModule.addDebugUserMessage(msg);
		}
	}

	protected void loop() {
		String msg = "请重写loop方法，实现游戏循环";
		System.out.println(msg);
		int sx = (screenWidth-g.getFont().stringWidth(msg))/2;
		int sy = (screenHeight-g.getFont().getHeight())/2;
		g.setClip(0, 0, screenWidth, screenHeight);
		g.setColor(0);
		g.fillRect(-Configurations.Abs_Coords_X, -Configurations.Abs_Coords_Y, screenWidth, screenHeight);
		g.setColor(-1);
		g.drawString(msg, sx, sy, 0);
	}


	public void trySleep() {
		this.trySleep(loopCircle);
	}
	
	public void trySleep(int milliseconds) {
		long now = System.currentTimeMillis();
		int sleepTime = (int)(milliseconds-(now-recordMillis));
		recordMillis = now;
		if (sleepTime < 0) {
			sleepTime = 0;
		}
		else if (sleepTime > milliseconds){
			sleepTime = milliseconds;
		}
		try {
			System.gc();
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
