package sheepwar;

import java.io.IOException;

import javax.microedition.lcdui.Image;

/**
 * 图片资源和武器信息等
 * @author Administrator
 *
 */
public class Resource implements Common {
	
	
	private static short NUMS = 0;
	
	public static short id_main_bg = NUMS++;
	public static short id_main_menu = NUMS++;
	
	public static short id_playing_menu = NUMS++;
	public static short id_playing_cloudbig = NUMS++;
	public static short id_playing_cloudsmall = NUMS++;
	public static short id_playing_lawn = NUMS++;
	public static short id_playing_step = NUMS++;
	public static short id_playing_tree = NUMS++;
	public static short id_playing_stop = NUMS++;
	public static short id_game_bg = NUMS++;
	public static short id_playing_prop_memu = NUMS++;
	public static short id_playing_prop = NUMS++;      
	public static short id_playing_lunzi = NUMS++;    
	public static short id_playing_shenzi = NUMS++;   
	public static short id_playing_lift = NUMS++;     
	public static short id_playing_shenzi1 = NUMS++;
	public static short id_playing_sheep = NUMS++;
	public static short id_sheep_eye = NUMS++;
	public static short id_sheep_hand = NUMS++;
	public static short id_bomb = NUMS++;
	public static short id_wolf_run = NUMS++;
	public static short id_wolf_down = NUMS++;
	public static short id_wolf_climb = NUMS++;
	public static short id_balloon_blue = NUMS++;      
	public static short id_balloon_green = NUMS++;
	public static short id_balloon_multicolour = NUMS++;
	public static short id_balloon_red = NUMS++;
	public static short id_balloon_yellow = NUMS++;
	public static short id_balloon_yellowred = NUMS++;
	public static short id_ladder = NUMS++;
	
	
	public static short id_shop_balance = NUMS++;//商城界面
	public static short id_shop_big = NUMS++;
	public static short id_shop_figure = NUMS++;
	public static short id_shop_go_pay = NUMS++;
	public static short id_shop_midding = NUMS++;
	public static short id_shop_out_base = NUMS++;
	public static short id_shop_out = NUMS++;
	public static short id_shop_small_base = NUMS++;
	public static short id_shop_small = NUMS++;
	public static short id_price_quantity = NUMS++;
	public static short id_shop = NUMS++;
	
	public static short id_achievement = NUMS++;//成就系统
	public static short id_achievement_left_right = NUMS++;
	public static short id_achievement_left_right1 = NUMS++;
	public static short id_achievement_long = NUMS++;
	public static short id_achievement_long1 = NUMS++;
	public static short id_achievement_out1 = NUMS++;
	public static short id_achievement_points = NUMS++;
	public static short id_archivement_hoof = NUMS++;
	public static short id_archivement_hoof1 = NUMS++;
	public static short id_achievement_word = NUMS++;
	public static short id_achievement_left = NUMS++;
	public static short id_slash = NUMS++;				
	
	public static short id_current_ranking = NUMS++;//排行界面
	public static short id_ranking_option = NUMS++;
	public static short id_ranking_option1 = NUMS++;
	public static short id_ranking_stripe = NUMS++;
	public static short id_ranking = NUMS++;
	public static short id_ranking_show = NUMS++;
	public static short id_ranking_word = NUMS++;			//排行文字
	
	public static short id_playing_level= NUMS++;			
	public static short id_playing_point = NUMS++;			
	public static short id_sheep_head = NUMS++;			
	public static short id_wolf_head = NUMS++;			
	public static short id_multiply = NUMS++;	
	
	public static short id_game_help = NUMS++;     //游戏帮助
	
	public static short id_logo = NUMS++;    
	public static short id_pass_cloud = NUMS++;    
	public static short id_pass_cloud1 = NUMS++;    
	public static short id_pass_cloud2 = NUMS++;    
	public static short id_pass_num = NUMS++;    
	public static short id_pass_rainbow = NUMS++;    
	public static short id_pass_score = NUMS++;    
	public static short id_pass_star = NUMS++;    
	public static short id_pass_star2 = NUMS++;    
	public static short id_net = NUMS++;    
	public static short id_net2 = NUMS++;    
	public static short id_boom = NUMS++;    
	public static short id_boom1 = NUMS++;    
	public static short id_prop_3 = NUMS++;    
	public static short id_prop_4 = NUMS++;    
	public static short id_prop_4_effect = NUMS++;    
	public static short id_prop_5_effect = NUMS++;    
	public static short id_prop_7_effect = NUMS++;    
	public static short id_cloud1 = NUMS++;    
	public static short id_apple = NUMS++;    
	public static short id_lemon = NUMS++;    
	public static short id_orange = NUMS++;    
	public static short id_watermelon = NUMS++;    
	public static short id_red_wolf = NUMS++;    
	public static short id_game_result = NUMS++;    
	public static short id_game_return = NUMS++;    
	public static short id_sub_menu_bg = NUMS++;    
	public static short id_sub_menu = NUMS++;    
	public static short id_prop_fist = NUMS++;    
	public static short id_prop_fist_effect = NUMS++;    
	public static short id_pumpkin = NUMS++;    
	public static short id_propOfCloud = NUMS++;    		
	public static short id_burn = NUMS++;    				
	public static short id_gloveLeft = NUMS++;    				
	public static short id_gloveRight = NUMS++;    				
	public static short id_main_select_right = NUMS++;    				
	public static short id_main_select_left = NUMS++;    				
	public static short id_main_select_right_base = NUMS++;    				
	public static short id_main_select_left_base = NUMS++;     				
	public static short id_shop_bottom = NUMS++;     				
	public static short id_achievement_bottom = NUMS++;     				
	public static short id_rank_bottom = NUMS++;     				
	public static short id_control = NUMS++;     				
	
	
	public static String[] imagesrcs = {
		"/main_bg.jpg",
		"/main_menu.png",
		
		"/playing_menu.png",             //游戏中的 界面
		"/playing_cloudbig.png",
		"/playing_cloudsmall.png",
		"/playing_lawn.png",
		"/playing_step.png",
		"/playing_tree.png",
		"/playing_stop.png",
		"/game_bg.jpg",
		"/playing_prop_memu.png",
		"/prop1.png",						//修改过的道具图片
		"/playing_lunzi.png",
		"/playing_shenzi.png",
		"/playing_lift.png",    
		"/playing_shenzi1.png",
		"/playing_sheep.png",
		"/sheep_eye.png",
		"/sheep_hand.png",
		"/bomb.png",
		"/wolf_run.png",
		"/wolf_down.png",
		"/wolf_cilmb.png",
		"/balloon_blue.png",
		"/balloon_green.png",
		"/balloon_multicolour.png",
		"/balloon_red.png",
		"/balloon_yellow.png",
		"/balloon_yellowred.png",
		"/ladder.png",
		
		"/shop_balance.png",            //商城界面图片资源
		"/shop_big.png",
		"/shop_figure.png",
		"/shop_go_pay.png",
		"/shop_midding.png",
		"/shop_out_base.png",
		"/shop_out.png",
		"/shop_small_base.png",
		"/shop_small.png",
		"/price_quantity.png",
		"/shop.png",
		
		"/achievement.png",             //成就系统图片--big midding共享
		"/achievement_left_right.png",
		"/achievement_left_right1.png",
		"/achievement_long.png",
		"/achievement_long1.png",
		"/achievement_out1.png",
		"/achievement_points.png",
		"/archivement_hoof.png",
		"/archivement_hoof1.png",
		"/achievement_word.png",
		"/achievement_left.png",
		"/slash.png",
		
		"/current_ranking.png",       //排行图片资源
		"/ranking_option.png",
		"/ranking_option1.png",
		"/ranking_stripe.jpg",       
		"/ranking.png",
		"/show.png",								//替换了ranking_show.jpg
		"/ranking_word.png",
		
		"/playing_level.png",
		"/playing_point.png",
		"/sheep_head.png",
		"/wolf_head.png",
		"/multiply.png",
		
		"/game_help.png",              //游戏帮助
		
		"/logo.png",             
		"/pass_cloud.png",             
		"/pass_cloud1.png",             
		"/pass_cloud2.png",             
		"/pass_num.png",             
		"/pass_rainbow.png",             
		"/pass_score.png",             
		"/pass_star.png",             
		"/pass_star1.png",             
		"/net.png",             
		"/net_effect.png",             
		"/boom.png",             
		"/boom1.png",             
		"/prop_3.png",             
		"/prop_4.png",             
		"/prop_4_effect.png",             
		"/prop_5_effect.png",             
		"/prop_7_effect.png",             
		"/cloud1.png",             
		"/apple.png",             
		"/lemon.png",             
		"/orange.png",             
		"/watermelon.png",             
		"/red_wolf.png",             
		"/game_result.png",             
		"/game_return.png",             
		"/submenu_bg.png",             
		"/submenu.png",             
		"/prop_fist.png",             
		"/prop_fist_effect.png",             
		"/pumpkin.png",             
		"/propOfCloud.png",             
		"/burn.png",             
		"/gloveLeft.png",             
		"/gloveRight.png",             
		"/main_select_right.png",             
		"/main_select_left.png",             
		"/main_select_right_base.png",             
		"/main_select_left_base.png",             
		"/shop_bottom.jpg",             
		"/achievement_bottom.jpg",             
		"/ranking_bottom.jpg",             
		"/control.png",             
		
	};
	
	private static final Image[] images = new Image[NUMS];

	public static Image loadImage(int id){
		if(images[id]==null){
			try {
				images[id] = Image.createImage(imagesrcs[id]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return images[id];
	}
	
	/*释放图片*/
	public static void freeImage(int id){
		images[id] = null;
	}
	
	/**
	 * 游戏帮助文字介绍
	 */
	public static String gameInfo[] = {   
		"#R【操作说明】#r#K上下方向键：#W控制玩家的移动。#r#K确定键：#W发射飞镖或无敌拳套。#r#K数字键1至8：#W使用道具。#r#K数字键0：#W退出游戏。#r#K" +
		"数字键9:#W游戏帮助。",
		
		"#R【道具说明】#r#K光闹钟：#W时间静止10秒。#r#K捕狼网：#W发射出的子弹碰到灰太狼就会张开一张网，大网内的灰太狼都会掉落。#r#K防狼套装：#W开启后有5秒的无敌效果，抵御各种攻击。#r#K驱狼光波:#W发出一道十万伏特的电流，电晕碰到的灰太狼,持续5秒。#r#K替身玩偶：#W增加一条命。#r#K驱散竖琴：#W使用后清除所有的梯子或者正在推石头的灰太狼。#r#K速度提升液：#W使用后增加喜羊羊的移动速度，持续30秒。#r#K强力磁石：#W击落所有空中的灰太狼。",
		
		"#R【游戏简介】#r#W喜羊羊大战灰太狼是一款闯关类游戏，#r总共有15关。玩家控制喜羊羊击落一定数量的灰太狼即可过关。#r此外玩家还可以在道具商城内购买各种道具来获得更有趣的体验。#r除了闯关外，游戏中还推出了成就系统和排行榜，#r增加了玩家在游戏的过程中动力和目标。",
		"",
	};
	
	/*商城商品介绍*/    	//二维数组创建注意
	public static String propIntroduce [][]= {
		{"时光钟:#r时间静止10秒。#r 价格：20游戏币","驱狼竖琴:#r使用后清除所有梯子上或者正在推南瓜的灰太狼。#r价格：30游戏币"},//shopY
		{"捕狼网:#r发射出的子弹碰到灰太狼就会张开一张网，#r大网内的灰太狼都会掉落。#r只击落碰到的灰太狼。#r价格：20游戏币","连发:#r使用后连续发射四颗子弹，单关有效。#r价格：30游戏币"},
		{"防狼套装:#r开启后得到5秒的无敌效#r果，抵御各种攻击。#r价格：30游戏币","强力磁石:#r击落所有空中的灰太狼。#r价格：50游戏币"},
		{"驱狼光波:#r发出一道十万伏特的电流，电晕碰到的灰太狼，持续5秒。#r价格：30游戏币","替身玩偶:#r增加一条命。#r价格：50游戏币"},
	};

}
