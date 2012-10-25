package sheepwar;

import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.game.SGraphics;

/**
 * 爆炸燃烧效果类
 * @author Administrator
 */
public class Exploder implements Common {
	private int mapx;
	private int mapy;
	private int[] frame={0,1,0,1,2,3,4,5,6,7,};
	private int i,j;
	private int[] frame2={0,1,0,1};
	public int score, tempy;
	public int propId;
	
	public Exploder(int mapx, int mapy){
		this.mapx = mapx;
		this.mapy = mapy;
	}
	
	public Exploder(int mapx, int mapy, int tempy){
		this.mapx = mapx;
		this.mapy = mapy-20;
		this.tempy = mapy-20;
	}
	
	public Exploder(int mapx, int mapy, int tempy, int propId){
		this.mapx = mapx-5;
		this.mapy = mapy-50;
		this.tempy = mapy-50;
		this.propId = propId;
	}
	
	/*爆炸效果*/
	public void drawExplode(SGraphics g, StateGame stateGame) {
		if (i <7) {	
			try {
				Image burstImage = Resource.loadImage(Resource.id_burn);
				g.drawRegion(burstImage, frame[i] * burstImage.getWidth() / 8, 0, burstImage.getWidth() / 8,
						burstImage.getHeight(), 0, mapx, mapy, 20);
				i++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/*燃烧效果*/
	public void showMagnetEffect(SGraphics g) {
		Image magnetEffect = Resource.loadImage(Resource.id_prop_7_effect);
		int effectW = magnetEffect.getWidth()/2, effectH = magnetEffect.getHeight();
		if (j < 4) {	
			g.drawRegion(magnetEffect, frame2[j]*effectW, 0, effectW, effectH, 0, mapx, mapy, 20);
			j++;
		}
		
	}
	
	/*动态得分效果*/
	public void showScore(SheepWarGameEngine engine, SGraphics g, int score) {
		if(tempy-mapy<50){
			engine.setFont(25, true);
			int col = g.getColor();
			g.setColor(0xff0000);
			g.drawString("+"+String.valueOf(score), mapx, mapy, 20);
			g.setColor(col);
			mapy -= 10;
			engine.setDefaultFont();
		}
	}
	
	/*使用道具提示效果*/
	public void showUseProp(SGraphics g, int propId){
		Image prop = Resource.loadImage(Resource.id_prop);
		int pW = prop.getWidth()/8, pH = prop.getHeight();
		if(tempy-mapy<65){
			g.drawRegion(prop, (propId)*pW, 0, pW, pH, 0, mapx, mapy, 20);
			mapy -= 15;
		}
		
	}
}
