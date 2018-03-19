/**
 * 作者： 金 鹏
 * 功能： 敌人击中我爆炸
 * 		重复的代码或者比较长的代码，可以封装成函数，增加代码的可读性
 * 
 * 		※※※※※可以记录玩家的成绩※※※※※※※※   (难度大)
 * 					用到文件流
 * 				          单开一个记录类,对玩家成绩记录
 * 					退出游戏时，保存对玩家成绩到文件中(不能仅仅保存存活的敌人坦克)
 * 					存盘退出，记录当时敌人坦克坐标并恢复(有难度)
 *  	※※※※※Java如何操作声音文件※※※※※※※※
 * 日期：2017/7/11~13
 */

package com.tankgame_99;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;//文件流

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;

public class TankGame99_1 extends JFrame implements ActionListener{

	MyPanel9 mp=null;
	//定义开始游戏面板组件
	MyStartPanel msp=null;
	//定义菜单组件
	JMenuBar jmb=null;
	JMenu jm1=null;
	JMenuItem jmi1,jmi2,jmi3,jmi4;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TankGame99_1 tg=new TankGame99_1();
	}
	
	public TankGame99_1()
	{
		//创建菜单组件
		jmb=new JMenuBar();
		jm1=new JMenu("游戏(G)");
		jm1.setMnemonic('G');
		jmi1=new JMenuItem("开始游戏");//jmi1 点击此处时，开始跳转到游戏，此处监听的事件源,即对jmi1响应
		jmi1.setActionCommand("start");
		jmi2=new JMenuItem("退出游戏");
		jmi2.setActionCommand("exit");
		jmi3=new JMenuItem("存盘退出");
		jmi3.setActionCommand("saveExit");
		jmi4=new JMenuItem("继续上一局游戏");
		jmi4.setActionCommand("continueGame");
		//对jmi1作出相应，注册监听
		jmi1.addActionListener(this);
		//对jmi2监听
		jmi2.addActionListener(this);
		//对jmi3监听
		jmi3.addActionListener(this);
		//对jmi4监听
		jmi4.addActionListener(this);
		
		jm1.add(jmi1);
		jm1.add(jmi2);
		jm1.add(jmi3);
		jm1.add(jmi4);
		jmb.add(jm1);
		this.setJMenuBar(jmb);
		
		msp=new MyStartPanel();
		Thread tt=new Thread(msp);
		tt.start();//启动线程
		this.add(msp);
		
		this.setSize(600,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//对用户不同的点击做出不同的处理
		if(e.getActionCommand().equals("start"))
		{
			//开始游戏
			
			//读取游戏记录
			Recorder.getRecordering();
			
			mp=new MyPanel9("newGame");//JPanel-->自动调用paint()函数
			Thread th=new Thread(mp); 
			th.start();
			//先删除旧的面板  ※
			this.remove(msp);
			this.add(mp);
			//注册监听
			this.addKeyListener(mp);//在事件上加入监听	
			//显示  ※
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setVisible(true);		
		}else if(e.getActionCommand().equals("exit"))
		{
			//退出游戏，保存上一局敌人状态
			
			//要保存玩家成绩，这部分调用封装函数，可读性好     记录类中
			Recorder.saveRecordering();
			
			System.exit(0);//正常退出
		}else if(e.getActionCommand().equals("saveExit"))
		{
			//存盘退出，即保存上一局敌人状态
			//先把面板上敌人的状态传给记录
			Recorder.setEns(this.mp.ens);
			//存盘退出
			Recorder.saveExitRecordering();
			
			System.exit(0);
		}else if(e.getActionCommand().equals("continueGame"))
		{
			//开始游戏时，继续上一局，读取文件中的敌人坦克的状态
			
			//mp.nodes=new Recorder().getNodeAndHitEnNum();----->在定义出直接给
			mp=new MyPanel9("conGame");//JPanel-->自动调用paint()函数
			
			Thread th=new Thread(mp); 
			th.start();
			//先删除旧的面板  ※
			this.remove(msp);
			this.add(mp);
			//注册监听
			this.addKeyListener(mp);//在事件上加入监听	
			//显示  ※
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setVisible(true);	
		}
	}

}
//游戏开始Panel
class MyStartPanel extends JPanel implements Runnable  //做成线程是字体闪烁
{
	int times=0;  //控制闪烁
	//绘制面板
	public void paint(Graphics g)
	{
		super.paint(g);
		g.fillRect(0, 0, 400, 300); //fillRect()默认黑色填充，drawRect()默认无色
		
		//关卡信息字体设置
		if(this.times%2==0)//1s闪烁一次
		{
			Font myFont=new Font("华文新魏",Font.BOLD,50); 
			g.setFont(myFont);
			g.setColor(Color.yellow);
			g.drawString("stage: 1", 90, 160);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			//间隔500ms重画
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//调用重画函数
			this.repaint(); //再次执行paint()函数
			//控制
			times++;
		}
	} 
}
//定义自己游戏界面的面板
class MyPanel9 extends JPanel implements KeyListener,Runnable
{
	Hero hero=null;
	
	//定义一个集合类，存放敌方坦克
	Vector<Enemy> ens=new Vector<Enemy>();
	//定义集合类存放取出的点
	Vector<Node> nodes=new Vector<Node>();      //在上面监听函数中有传入
	//定义炸弹集合_可能同时击中多个坦克 
	Vector<Bomb> bombs=new Vector<Bomb>();
	
	//定义爆炸图片
	Image image1=null;
	Image image2=null;
	Image image3=null;

	int size=6;  //敌人坦克数
	//要在构造函数中初始化
	public MyPanel9(String flag)  //一提到初始化就要在构造函数中_初始化玩家和敌人坦克
	{
		hero=new Hero(100, 100);
		
		if(flag.equals("newGame"))
		{
			//初始化敌人坦克
			for(int i=0;i<size;i++)
			{
				Enemy en=new Enemy((i+1)*40, 15);//敌人坦克初始化出现位置
				
				en.setColor(0);//敌人都是绿色
				en.setDirect(1);//敌人都是向下方向
				//将MyPanel上敌人坦克向量交给敌人坦克
				en.setEns(ens);            ///////////////////////////////敌人坦克
				
				//启动敌人坦克
				Thread th=new Thread(en);
				th.start();
				//敌人坦克创建处就添加一颗子弹
				Shoot sh=new Shoot(en.x, en.y+15, 1);//Shoot是线程
				//添加到 敌人坦克 的 子弹向量 中
				en.shs.add(sh);  //------>画出子弹
				Thread th1=new Thread(sh);
				th1.start();
				ens.add(en);//加入集合    ///////////////////////MyPanel上的敌人坦克向量
			}
		}else if(flag.equals("conGame")){
			nodes=new Recorder().getNodeAndHitEnNum();    //staic和非static 集合型数据类型的方法调用是: 类名().方法名()
			//初始化敌人坦克                                                                                    //         其他数据类型是: 类名.方法名() 或  对象名.方法名()
			for(int i=0;i<nodes.size();i++)
			{
				Node node=nodes.get(i);
				
				Enemy en=new Enemy(node.x, node.y);//敌人坦克初始化出现位置
				
				en.setColor(0);//敌人都是绿色
				en.setDirect(node.direct);//敌人都是向下方向
				//将MyPanel上敌人坦克向量交给敌人坦克
				en.setEns(ens);            ///////////////////////////////敌人坦克
				
				//启动敌人坦克
				Thread th=new Thread(en);
				th.start();
				//敌人坦克创建处就添加一颗子弹
				Shoot sh=new Shoot(en.x, en.y+15, 1);//Shoot是线程
				//添加到 敌人坦克 的 子弹向量 中
				en.shs.add(sh);  //------>画出子弹
				Thread th1=new Thread(sh);
				th1.start();
				ens.add(en);//加入集合    ///////////////////////MyPanel上的敌人坦克向量
			}
		}
		//播放音乐
		OpenSong BGM=new OpenSong("F:\\zhuawa\\program\\java_project\\TankWar\\111.wav");//可以先定义音频对象
		BGM.start();
		//初始化图片===>炸的效果不明显
		image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));//都是图片对象
		image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
		image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));
	}
	//敌人坦克子弹击中我爆炸
	public void hitMe()
	{
		//取出一个敌人坦克
		for(int i=0;i<this.ens.size();i++)
		{
			Enemy enemyTank=this.ens.get(i);
			//取出一个敌人坦克的一个子弹
			for(int j=0;j<enemyTank.shs.size();j++)
			{
				Shoot enemyShoot=enemyTank.shs.get(j);
				
				if(hero.isLive)
				{
					if(this.hitTank(enemyShoot, hero))            //玩家死亡后被击中不会爆炸,想想如何改正
					{
						
					}
				}
			}
		}
	}
	//玩家子弹击中坦克爆炸
	public void hitEnemyTank()
	{
		for(int i=0;i<hero.ss.size();i++)
		{
			Shoot MyShot=hero.ss.get(i);
			if(MyShot.isLive)
			{
				//遍历活坦克
				for(int j=0;j<ens.size();j++)
				{
					Enemy en=ens.get(j);
					if(en.isLive)
					{
						//判断
						if(this.hitTank(MyShot, en))     //去画坦克的地方想想怎么写
						{
							//减少坦克数
							Recorder.reduceEnNum();
							//增加玩家成绩
							Recorder.addHitEnNum();
						}
					}
				}
			}
		}
	}
	//判断子弹击中敌人坦克的方法, 返回布尔值
	public boolean hitTank(Shoot s,Tank e)  //传子弹和敌人坦克对象参数
	{
		//子弹是否击中坦克
		boolean bb=false;   //每次调用是都会初始化为false
		
		switch(e.direct)
		{
		case 0://上
		case 1://下
			if(s.x>(e.x-15)&&s.x<(e.x+15)&&s.y>(e.y-12)&&s.y<(e.y+12))
			{
				//子弹死亡
				s.isLive=false;
				//坦克死亡
				e.isLive=false;

				bb=true;
				
				//定义爆炸对象
				Bomb b=new Bomb(e.x, e.y);
				//加入集合
				bombs.add(b);//之后就是绘画
			}
			break;
		case 2://左
		case 3://右
			if(s.x>(e.x-12)&&s.x<(e.x+12)&&s.y>(e.y-15)&&s.y<(e.y+15))
			{
				//子弹死亡
				s.isLive=false;
				//坦克死亡
				e.isLive=false;	

				bb=true;
				
				//定义爆炸对象
				Bomb b=new Bomb(e.x, e.y);
				//加入集合
				bombs.add(b);
			}
			break;
		}
		return bb;
	}
	//覆盖父类方法_绘制_记得加入画笔
	public void paint(Graphics g)
	{
		super.paint(g);
		
		//用画笔涂黑面板
		g.fillRect(0, 0, 400, 300);
		//代码是按行执行的
		
		//游戏提示信息调用
		this.showGameInfomation(g);
		
		//画玩家坦克
		if(hero.isLive)
		{
			this.drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 1);
		}
		//绘制爆炸效果
		for(int i=0;i<bombs.size();i++)
		{
			//从集合中取出
			Bomb b=bombs.get(i);
			
			//图片切换呈现爆炸效果
			if(b.life>6)
			{
				g.drawImage(image1, b.x-15, b.y-15, 30, 30, this);
			}else if(b.life>3)
			{
				g.drawImage(image2, b.x-15, b.y-15, 30, 30, this);
			}else {
				g.drawImage(image3, b.x-15, b.y-15, 30, 30, this);
			}
			//让b减小
			b.lifeDown();
			//炸弹生命为0时，从集合中去除
			if(b.life==0)
			{
				bombs.remove(b);//切记不要是i
			}
		}
		//画敌人坦克
		for(int i=0;i<ens.size();i++)
		{
			Enemy en=ens.get(i);
			if(en.isLive)
			{
				this.drawTank(en.getX(), en.getY(), g, en.getDirect(), 0);
				//画出敌人所有子弹
				for(int j=0;j<en.shs.size();j++)
				{
					Shoot enemyShot=en.shs.get(j);
					//System.out.println("第"+j+"颗子弹的坐标："+enemyShot.x+"  "+enemyShot.y);
					if(enemyShot.isLive==true)
					{
						g.drawOval(enemyShot.x, enemyShot.y, 1, 1);
					}else{
						//子弹撞墙死亡就去除
						en.shs.remove(enemyShot);
					}
				}
				
			}
		}
		//用画笔画子弹,for遍历画多个子弹
		for(int i=0;i<hero.ss.size();i++)
		{
			//从集合取出子弹
			Shoot MyShot=hero.ss.get(i);
			
			if(MyShot!=null&&MyShot.isLive==true)//只有子弹创建了才可以画
			{
				g.setColor(Color.yellow);
				g.fill3DRect(MyShot.x, MyShot.y, 2, 2, false);	
			}
			//子弹射出边框，就死亡，去除
			if(MyShot.isLive==false)
			{
				hero.ss.remove(MyShot);//MyShot而不是i
			}
		}
		
	}
	//将游戏提示信息内容打包成函数
	public void showGameInfomation(Graphics g)//只要穿传画笔
	{
		Font f=new Font("宋体", Font.BOLD, 15);
		g.setFont(f);
		//敌人数
		this.drawTank(100, 350, g, 0, 0);
		g.setColor(Color.black);
		g.drawString(Recorder.getEnNum()+"", 125, 355);
		//玩家生命
		this.drawTank(170, 350, g, 0, 1);
		g.setColor(Color.black);
		g.drawString(Recorder.getMyNum()+"", 195, 355);
		
		g.drawString("玩家的成绩", 440, 110);
		//击中敌人数
		this.drawTank(470, 150, g, 0, 0);
		g.setColor(Color.black);
		g.drawString(Recorder.getHitEnNum()+"", 495, 155);
	}
	
	//画坦克的函数
	public void drawTank(int x,int y,Graphics g,int diect,int type)
	{
		//玩家 敌人  设置坦克颜色
		switch(type)
		{
		case 0:
			g.setColor(Color.CYAN);
			break;
		case 1:
			g.setColor(Color.yellow);
			break;
		}
		//坦克方向
		switch(diect)
		{
		//以圆心为基点√※
		case 0: //向上
			g.fill3DRect(x-12, y-15, 5, 30, false);
			g.fill3DRect(x-7, y-10, 14, 20, false);
			g.fill3DRect(x+7, y-15, 5, 30, false);
			g.fillOval(x-5, y-5, 10, 10);
			g.drawLine(x, y, x, y-18);  //x,y是圆心
			break;	
		case 1: //向下
			g.fill3DRect(x-12, y-15, 5, 30, false);
			g.fill3DRect(x-7, y-10, 14, 20, false);
			g.fill3DRect(x+7, y-15, 5, 30, false);
			g.fillOval(x-5, y-5, 10, 10);
			g.drawLine(x, y, x, y+18);  //x,y是圆心
			break;
		case 2: //向左
			g.fill3DRect(x-15, y-12, 30, 5, false);
			g.fill3DRect(x-10, y-7, 20, 14, false);
			g.fill3DRect(x-15, y+7, 30, 5, false);
			g.fillOval(x-5, y-5, 10, 10);
			g.drawLine(x, y, x-18, y);  //x,y是圆心
			break;				
		case 3: //向右
			g.fill3DRect(x-15, y-12, 30, 5, false);
			g.fill3DRect(x-10, y-7, 20, 14, false);
			g.fill3DRect(x-15, y+7, 30, 5, false);
			g.fillOval(x-5, y-5, 10, 10);
			g.drawLine(x, y, x+18, y);  //x,y是圆心
			break;				
		}
	}

	//按键被按下时的事件处理函数__一定要记着函数（封装）思想
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		//监听机制下的控制
		if(e.getKeyCode()==KeyEvent.VK_UP)
		{
			//向上移动
			this.hero.moveUp();  //hero.getY()不是变量
			this.hero.setDirect(0);
		}else if(e.getKeyCode()==KeyEvent.VK_DOWN)
		{
			//向下移动
			this.hero.moveDown();
			this.hero.setDirect(1);
		}else if(e.getKeyCode()==KeyEvent.VK_LEFT)
		{
			//向左移动
			this.hero.moveLeft();
			this.hero.setDirect(2);
		}else if(e.getKeyCode()==KeyEvent.VK_RIGHT)
		{
			//向右移动
			this.hero.moveRight();
			this.hero.setDirect(3);
		}
		
		//判断是否按下射击键
		if(e.getKeyCode()==KeyEvent.VK_J)
		{
			//创建子弹坐标_控制连发5颗
			if(hero.ss.size()<5)
			{
				this.hero.shootEnemy();
			}
		}
		//用repaint()重绘坦克
		this.repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//每个100ms重绘一次面板
		while(true)
		{
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			//调用击中判断函数，遍历子弹和坦克
			//遍历活子弹
			this.hitEnemyTank();//敌人爆炸
			this.hitMe();//玩家爆炸

//把敌人坦克的子弹添加可以放在敌人坦克的类中的run函数中

			//重绘
			this.repaint();
		}
	}
	
}




