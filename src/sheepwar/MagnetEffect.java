package sheepwar;

import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.game.SGraphics;

public class MagnetEffect implements Common{
	
	private int mapx, mapy;
	private int[] frame={0,1,0,1};
	int i;
	
	public MagnetEffect(int mapx, int mapy){
		this.mapx = mapx;
		this.mapy = mapy;
	}

	public void showMagnetEffect(SGraphics g) {
		Image magnetEffect = Resource.loadImage(Resource.id_prop_7_effect);
		int effectW = magnetEffect.getWidth()/2, effectH = magnetEffect.getHeight();
		if (i < 4) {	
			g.drawRegion(magnetEffect, frame[i]*effectW, 0, effectW, effectH, 0, mapx, mapy, 20);
			i++;
		}
		
	}
}
