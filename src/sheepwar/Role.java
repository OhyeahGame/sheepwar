package sheepwar;
public class Role {
	int id; 			//ID
	int mapx; 			//在地图上的横坐标
	int mapy; 			//在地图上的纵坐标
	int frame;			//角色当前帧数
	int index;			//控制帧切换
	int direction; 		//移动方向 0 向上，1向下
	int status;  		//状态(0活动状态, -1死状态, 1成功逃脱状态, 2攻击状态)
	int status2;		//第二状态（0在地面， 1离开地面）
	int position;		//狼在梯子上的位置( 1在第一个梯子上，2在第二个梯子上，3在第三个梯子上，4在第四个梯子上)
	int position2;		//狼出现的初始位置(0-上面, -1-下面)
	int nonceLife;		//生命值
	int lifeNum; 		//生命数
	int speed;			//移动速度
	int scores; 		//积分
	int scores2;		//单关所得积分
	int coorX;			//下落点
	int coorY;			//狼发射子弹的纵坐标
	int height;         //角色自身高度
	int width;          //角色自身宽度
	int hitNum;			//单关击中的狼数
	int hitTotalNum;	//击中狼的总数
	int hitBuble;		//击中的气球数
	int useProps;		//使用的道具数
	int hitFruits;		//击中的水果数
	int bombNum;		//发射子弹数量
	int hitRatio;		//击中目标数
	int hitBooms;		//击中子弹数
	int colorId;		//气球ID
	int attackTime;		//被击中次数
	int attainment;		//成就点数
	int dizzyIndex;		//控制眩晕
	int dizzyFlag;
	
	Role role;		   //子对象
}
