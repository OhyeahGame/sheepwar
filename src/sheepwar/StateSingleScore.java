package sheepwar;

import cn.ohyeah.stb.game.SGraphics;

/**
 *��̬ ���������÷���
 * @author Administrator
 */
public class StateSingleScore implements Common {
	
	private int tempY,scoreY;
	public void showSingleScore(SGraphics g,Batches batches,Role player) {

		Role npc = null;
		for(int i = 0;i< batches.npcs.size()-1;i++){
			npc = (Role)batches.npcs.elementAt(i);
			if(npc.status == ROLE_DEATH){				//���ֻ�е�һ�������е��ǲ���������ʾ
				scoreY = player.mapy;
				if((npc.mapy-scoreY)>40){				/*��ʱ����ʾ��ֹ�������ĵط�*/
					tempY = scoreY;
					g.setColor(0xffffff);
					g.drawString(/*"��ʾ����� ������"*/String.valueOf(npc.scores), npc.mapx, tempY, 20);
//					scoreY -= 5;
				}
			}
		}
	}
	
	/*��ʾ�����ӵ��ķ���*/
	public void showAttackBoom(SGraphics g,Weapon weapon,Role player) {
		Weapon boom = null;
		for (int i = weapon.booms.size()-1;i>=0;i--){
			System.out.println(i);
			boom = (Weapon)weapon.booms.elementAt(i);
			System.out.println(boom);
			if(boom.status == BOOM_HIT){
				scoreY = player.mapy;
				System.out.println("�����ӵ��ĵ÷֣�"+boom.scores);
				System.out.println("�ӵ��ĺ����꣺"+boom.mapx);
				g.setColor(0xffffff);
				g.drawString(/*"��ʾ����� ������"*/String.valueOf(boom.scores), boom.mapx, tempY, 20);
			}
			
		}

	}
}
