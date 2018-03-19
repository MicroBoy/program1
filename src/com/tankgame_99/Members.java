
package com.tankgame_99;

import java.awt.Graphics;
import java.io.*;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import sun.audio.AudioDataStream;

//                        同一个包中的类可以共享
//坦克父类_坦克共同属性
class Tank
{
	//横坐标
	int x=0;
	//纵坐标
	int y=0;

	boolean isLive=true;
	
	    //坦克方向  0 1 2 3
		int direct=0;
		//坦克速度
		int speed=4;
		//敌人坦克的速度
		int speed1=1;
		//坦克颜色
		int color=0;
		public int getColor() {
			return color;
		}

		public void setColor(int color) {
			this.color = color;
		}

		public int getDirect() {
			return direct;
		}

		public void setDirect(int direct) {
			this.direct = direct;
		}

		public int getSpeed() {
			return speed;
		}

		public void setSpeed(int speed) {
			this.speed = speed;
		}
			
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	
	//构造函数初始化
	public Tank(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	
}
//玩家坦克子类
class Hero extends Tank  //必须加入super
{
	//Shoot s=null;   此处使用集合可以实现子弹连发效果
	Vector<Shoot> ss=new Vector<Shoot>();//此声明处，集合为空
	Shoot s=null;
	public Hero(int x,int y)
	{
		super(x,y);
		//this是一个指向本对象的指针，this()同一个类中调用其他方法
		//super引用当前对象的直接父类中的成员变量和成员方法，super()从子类中调用父类的构造方法
	}
	//加入子弹开火，这是坦克的动作
	public void shootEnemy()
	{
		switch(this.direct)
		{
		case 0:
			s=new Shoot(x-1, y-20,0);
//			Thread t1=new Thread(s);
//			t1.start();
			break;
		case 1:
			s=new Shoot(x-1, y+20,1);
//			Thread t2=new Thread(s);
//			t2.start();
			break;
		case 2:
			s=new Shoot(x-20, y-1,2);
//			Thread t3=new Thread(s);
//			t3.start();
			break;
		case 3:
			s=new Shoot(x+20, y-1,3);
//			Thread t4=new Thread(s);
//			t4.start();
			break;
		}
		//子弹对象加入集合中
		ss.add(s);   // 到画子弹的地方paint()
		//启动子弹线程
		Thread t=new Thread(s);
		t.start();
	}
	
	
	public void moveUp()
	{
		y-=speed;
	}
	public void moveDown()
	{
		y+=speed;
	}
	public void moveLeft()
	{
		x-=speed;
	}
	public void moveRight()
	{
		x+=speed;
	}
}
//敌人坦克
class Enemy extends Tank implements Runnable   //敌人坦克触墙
{
	//坦克是否死亡
	//boolean isLive=true;已经升级到父类Tank中
	
	//定义一个向量或集合来存放子弹
	Vector<Shoot> shs=new Vector<Shoot>();//可以规定集合容量
	//敌人坦克添加子弹位置，敌人坦克刚刚创建的时候和敌人的坦克撞墙死亡之后
	public Enemy(int x,int y)
	{
		super(x,y);
	}
	
	//定义一个向量可以访问到MyPanel上所有的敌人坦克
	Vector<Enemy> ens=new Vector<Enemy>();
	//得到MyPanel上初始化后的所有  敌人坦克向量  这个能力
	public void setEns(Vector<Enemy> vv)   //敌人坦克一旦被创建，就调用这个函数
	{
		this.ens=vv;
	}
	//判断敌人坦克是否相碰
	public boolean isTouchOtherEnemy()    //在哪里用？？？？=======>敌人边界用
	{
		boolean b=false;
		//根据当前对象的方向判断
		switch(this.direct)
		{
		case 0:
			//当前坦克向上
			//取出所有坦克  for遍历
			for(int i=0;i<ens.size();i++)
			{
				//取出一个敌人坦克
				Enemy en=ens.get(i);
				//排除自己
				if(en!=this)
				{
					//非自己的敌人方向是向上或向下
					if(en.direct==0||en.direct==1)
					{
						//非自己敌人坦克左角
						if((this.x-12)>=(en.x-12)&&(this.x-12)<=(en.x+12)&&(this.y-15)>=(en.y-15)&&(this.y-15)<=(en.y+15))
						{
							b=true;   //相碰
						}
						//非自己敌人坦克右角
						if((this.x+12)>=(en.x-12)&&(this.x+12)<=(en.x+12)&&(this.y-15)>=(en.y-15)&&(this.y-15)<=(en.y+15))
						{
							b=true; 
						}
					}
					//非自己的敌人方向是向左或向右
					if(en.direct==2||en.direct==3)
					{
						//非自己敌人坦克左角
						if((this.x-12)>=(en.x-15)&&(this.x-12)<=(en.x+15)&&(this.y-15)>=(en.y-12)&&(this.y-15)<=(en.y+12))
						{
							b=true; 
						}
						//非自己敌人坦克右角
						if((this.x+12)>=(en.x-15)&&(this.x+12)<=(en.x+15)&&(this.y-15)>=(en.y-12)&&(this.y-15)<=(en.y+12))
						{
							b=true; 
						}
					}
				}	
			}
			break;
		case 1:
			//当前坦克向下
			//取出所有坦克  for遍历
			for(int i=0;i<ens.size();i++)
			{
				//取出一个敌人坦克
				Enemy en=ens.get(i);
				//排除自己
				if(en!=this)
				{
					//非自己的敌人方向是向上或向下
					if(en.direct==0||en.direct==1)
					{
						//非自己敌人坦克左角
						if((this.x-12)>=(en.x-12)&&(this.x-12)<=(en.x+12)&&(this.y+15)>=(en.y-15)&&(this.y+15)<=(en.y+15))
						{
							b=true; 
						}
						//非自己敌人坦克右角
						if((this.x+12)>=(en.x-12)&&(this.x+12)<=(en.x+12)&&(this.y+15)>=(en.y-15)&&(this.y+15)<=(en.y+15))
						{
							b=true; 
						}
					}
					//非自己的敌人方向是向左或向右
					if(en.direct==2||en.direct==3)
					{
						//非自己敌人坦克左角
						if((this.x-12)>=(en.x-15)&&(this.x-12)<=(en.x+15)&&(this.y+15)>=(en.y-12)&&(this.y+15)<=(en.y+12))
						{
							b=true; 
						}
						//非自己敌人坦克右角
						if((this.x+12)>=(en.x-15)&&(this.x+12)<=(en.x+15)&&(this.y+15)>=(en.y-12)&&(this.y+15)<=(en.y+12))
						{
							b=true; 
						}
					}
				}	
			}
			break;
		case 2:
			//当前坦克向左
			//取出所有坦克  for遍历
			for(int i=0;i<ens.size();i++)
			{
				//取出一个敌人坦克
				Enemy en=ens.get(i);
				//排除自己
				if(en!=this)
				{
					//非自己的敌人方向是向上或向下
					if(en.direct==0||en.direct==1)
					{
						//非自己敌人坦克上角
						if((this.x-15)>=(en.x-12)&&(this.x-15)<=(en.x+12)&&(this.y-12)>=(en.y-15)&&(this.y-12)<=(en.y+15))
						{
							b=true; 
						}
						//非自己敌人坦克下角
						if((this.x-15)>=(en.x-12)&&(this.x-15)<=(en.x+12)&&(this.y+12)>=(en.y-15)&&(this.y+12)<=(en.y+15))
							
						{
							b=true; 
						}
					}
					//非自己的敌人方向是向左或向右
					if(en.direct==2||en.direct==3)
					{
						//非自己敌人坦克上角
						if((this.x-15)>=(en.x-15)&&(this.x-15)<=(en.x+15)&&(this.y-12)>=(en.y-12)&&(this.y-12)<=(en.y+12))
						{
							b=true; 
						}
						//非自己敌人坦克下角
						if((this.x-15)>=(en.x-15)&&(this.x-15)<=(en.x+15)&&(this.y+12)>=(en.y-12)&&(this.y+12)<=(en.y+12))
						{
							b=true; 
						}
					}
				}	
			}
			break;
		case 3:
			//当前坦克向右
			//取出所有坦克  for遍历
			for(int i=0;i<ens.size();i++)
			{
				//取出一个敌人坦克
				Enemy en=ens.get(i);
				//排除自己
				if(en!=this)
				{
					//非自己的敌人方向是向上或向下
					if(en.direct==0||en.direct==1)
					{
						//非自己敌人坦克上角
						if((this.x+15)>=(en.x-12)&&(this.x+15)<=(en.x+12)&&(this.y-12)>=(en.y-15)&&(this.y-12)<=(en.y+15))
						{
							b=true; 
						}
						//非自己敌人坦克下角
						if((this.x+15)>=(en.x-12)&&(this.x+15)<=(en.x+12)&&(this.y+12)>=(en.y-15)&&(this.y+12)<=(en.y+15))
						{
							b=true; 
						}
					}
					//非自己的敌人方向是向左或向右
					if(en.direct==2||en.direct==3)
					{
						//非自己敌人坦克上角
						if((this.x+15)>=(en.x-15)&&(this.x+15)<=(en.x+15)&&(this.y-12)>=(en.y-12)&&(this.y-12)<=(en.y+12))
						{
							b=true; 
						}
						//非自己敌人坦克下角
						if((this.x+15)>=(en.x-15)&&(this.x+15)<=(en.x+15)&&(this.y+12)>=(en.y-12)&&(this.y+12)<=(en.y+12))
						{
							b=true; 
						}
					}
				}	
			}
			break;
				
		}
		
		return b;
	}
	
	
	//定义一个time，规定间隔倍数
	int time=0;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//线程内容，让敌人坦克自己动起来---------------->立马去线程类对象声明处启动线程
		while(true)   //每隔30(步)*50=1.5s变一次方向或者轨道
		{
			//根据方向移动
			switch(this.direct)
			{
			case 0://向上
				//用for语句产生平滑的移动
				for(int i=0;i<30;i++)
				{
					if(y>15&&!this.isTouchOtherEnemy())
					{
						y-=speed1;
					}
					try {
						Thread.sleep(50);//50ms移动一下
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 1://向下
				//用for语句产生平滑的移动
				for(int i=0;i<30;i++)
				{
					if(y<285&&!this.isTouchOtherEnemy())
					{
						y+=speed1;
					}
					try {
						Thread.sleep(50);//50ms移动一下
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 2://向左
				//用for语句产生平滑的移动
				for(int i=0;i<30;i++)
				{
					if(x>15&&!this.isTouchOtherEnemy())
					{
						x-=speed1;
					}
					try {
						Thread.sleep(50);//50ms移动一下
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 3://向右
				//用for语句产生平滑的移动
				for(int i=0;i<30;i++)
				{
					if(x<385&&!this.isTouchOtherEnemy())
					{
						x+=speed1;
					}
					try {
						Thread.sleep(50);//50ms移动一下
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			}
			this.time++;
			if(this.time%2==0) //每隔3s添加子弹并发射
			{
				//判断是否要给坦克加入新子弹
					if(isLive==true)
					{
						if(shs.size()<3)  //这里可以操作敌人坦克连发几颗子弹
						{
							Shoot sh=null;
							switch(direct)
							{
							case 0:
								sh=new Shoot(x-1, y-20,0);
								shs.add(sh);
//								Thread t1=new Thread(s);
//								t1.start();
								break;
							case 1:
								sh=new Shoot(x-1, y+20,1);
								shs.add(sh);
								break;
							case 2:
								sh=new Shoot(x-20, y-1,2);
								shs.add(sh);
								break;
							case 3:
								sh=new Shoot(x+20, y-1,3);
								shs.add(sh);
								break;
							}
							//启动子弹线程
							Thread th=new Thread(sh);
							th.start();
						}
					}
			}
			//随机产生方向
			this.direct=(int)(Math.random()*4);
			
			//若坦克死亡就退出线程
			if(this.isLive==false)
			{
				break;
			}
		}
	}
}

//子弹类-->子弹线程————子弹在哪创建，就在哪启动线程
class Shoot implements Runnable      //子弹触墙
{
	//射击坐标
	int x=0;
	int y=0;
	//方向
	int direct;
	//速度
	int speed=3;
	//子弹存在
	boolean isLive=true;
	public Shoot(int x,int y,int direct)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			//休眠50ms,防止内存承受不了
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			switch(direct)
			{
			case 0://上
				y-=speed;
				break;
			case 1://下
				y+=speed;
				break;
			case 2://左
				x-=speed;
				break;
			case 3://右
				x+=speed;
				break;
			}
			//System.out.println("横坐标  "+x);//测试子弹是否跑起来，但是没有repaint()将看不到
			
			//子弹何时死亡
			if(x<0||x>400||y<0||y>300)
			{
				isLive=false;
				break;
			}
		}
	}
}

//定义爆炸类
class Bomb
{
	//爆炸坐标
	int x=0;
	int y=0;
	//炸弹爆炸的生命
	int life=9;
	//炸弹死亡与否
	boolean isLive=true;
	//构造函数初始化
	public Bomb(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	
	public void lifeDown()
	{
		if(life>0)
		{
			life--;
		}else{
			this.isLive=false;
		}
	}
}

//单开一个记录类，都是静态类static:可以类之间共享变量；可是用类名调用方法
class Recorder
{
	//记录敌人坦克的数量
	private static int enNum=20;
	//记录玩家的生命数
	private static int myNum=3;
	//玩家的战绩，击杀坦克的数量
	private static int hitEnNum=0;
	
	//定义文件流
	private static FileWriter fw=null;
	private static BufferedWriter bw=null; //缓冲区，按行写入文件
	private static FileReader fr=null;
	private static BufferedReader br=null;
	//1//保存玩家成绩的封装函数
	public static void saveRecordering()     //定义成static方法，可以通过类名直接调用，就不需要实例化了
	{
		try {
			fw=new FileWriter("d:/myMark.txt");
			bw=new BufferedWriter(fw);
			
			bw.write(Recorder.hitEnNum+"\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//先打开后关上
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	//2//读取玩家成绩的封装函数
	public static void getRecordering()
	{
		try {
			fr=new FileReader("d:/myMark.txt");
			br=new BufferedReader(fr);
			String nn=br.readLine();
			hitEnNum=Integer.parseInt(nn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				br.close();
				fr.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	//3//存盘退出，保存敌人的坐标和方向
	//知道面板上敌人
	private static Vector<Enemy> ens=new Vector<Enemy>();
	public static Vector<Enemy> getEns() {
		return ens;
	}
	public static void setEns(Vector<Enemy> ens) {
		Recorder.ens = ens;
	}
	public static void saveExitRecordering()
	{
		try {
			fw=new FileWriter("d:/myMark.txt");
			bw=new BufferedWriter(fw);
			bw.write(Recorder.hitEnNum+"\r\n");
			
			//for循环遍历每个敌人
			for(int i=0;i<ens.size();i++)
			{
				Enemy en=ens.get(i);
				
				String ss=en.x+" "+en.y+" "+en.direct;
				bw.write(ss+"\r\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//先打开后关上
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	//4//继续上一局
	
	//定义一个集合来装每个记录的点
	static Vector<Node> nodes=new Vector<Node>();
	//读取记录的函数
	public Vector<Node> getNodeAndHitEnNum()
	{
		try {
			fr=new FileReader("d:/myMark.txt");
			br=new BufferedReader(fr);
			String nn="";
			//先读取第一行，玩家总成绩
			nn=br.readLine();
			hitEnNum=Integer.parseInt(nn);
			
			//读取下面几行，敌人的坐标和方向    nn=br.readLine()
			while((nn=br.readLine())!=null)
			{
				String []xyz=nn.split(" ");//将读出的每行数据按空格符分割成数组   [ 50 30 2]
				//因为已知道每行可以分成三个部分
				Node node=new Node(Integer.parseInt(xyz[0]),Integer.parseInt(xyz[1]),Integer.parseInt(xyz[1]));
				//将点加入集合中
				nodes.add(node);   //后面应如何调用？？
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				br.close();
				fr.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		return nodes;
	}
	
	public static int getEnNum() {
		return enNum;
	}
	public static void setEnNum(int enNum) {
		Recorder.enNum = enNum;
	}
	public static int getMyNum() {
		return myNum;
	}
	public static void setMyNum(int myNum) {
		Recorder.myNum = myNum;
	}
	public static int getHitEnNum() {
		return hitEnNum;
	}
	public static void setHitEnNum(int hitEnNum) {
		Recorder.hitEnNum = hitEnNum;
	}
	//击中后减少敌人坦克数
	public static void reduceEnNum()
	{
		enNum--;
	}
	//击中后增加玩家成绩
	public static void addHitEnNum()
	{
		hitEnNum++;
	}
}
//定义一个点类，来拿出记录数,继续上一局游戏要用
class Node
{
	int x=0;
	int y=0;
	int direct=0;
		
	//构造函数初始化
	public Node(int x,int y,int direct)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
}
//播放声音的类----以线程的方式启动背景音乐
class OpenSong extends Thread    //只要进入游戏界面就开始播放
{
	//定义声音文件
	private String filename;
	//构造函数初始化，即加载声音文件
	public OpenSong(String wavename)
	{
		this.filename=wavename;
	}
	//run函数是线程执行的
	public void run()
	{
	//内存拿到声音文件	
		//定义文件对象
		File soundFile=new File(filename);
		//定义缓冲流
		AudioInputStream audioinputstream=null;
		//声音文件加载到缓冲区
		try {
			audioinputstream=AudioSystem.getAudioInputStream(soundFile);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	//内存拿到播放器
		//定义播放器对象
		AudioFormat format=audioinputstream.getFormat();
		//播放器的缓冲区
		SourceDataLine sourceLine=null;
		DataLine.Info info=new DataLine.Info(SourceDataLine.class, format);
		
		try {
			sourceLine=(SourceDataLine)AudioSystem.getLine(info);// Line强制转换成SourceDataLine
			sourceLine.open(format);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return;
		}
		
		sourceLine.start();//开始读取并打开播放器
		int nBytesRead=0;
		//每次读取多少
		byte[] ssdata=new byte[1024];
		//播放音频
		try {
			while(nBytesRead!=-1)
			{
				nBytesRead=audioinputstream.read(ssdata, 0, ssdata.length);
				if(nBytesRead>=0)
				{
					sourceLine.write(ssdata, 0, ssdata.length);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return;
		}finally{
			sourceLine.drain();//排尽line里的信息
			sourceLine.close();
		}
		
	}
}



