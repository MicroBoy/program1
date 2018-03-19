
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

//                        ͬһ�����е�����Թ���
//̹�˸���_̹�˹�ͬ����
class Tank
{
	//������
	int x=0;
	//������
	int y=0;

	boolean isLive=true;
	
	    //̹�˷���  0 1 2 3
		int direct=0;
		//̹���ٶ�
		int speed=4;
		//����̹�˵��ٶ�
		int speed1=1;
		//̹����ɫ
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

	
	//���캯����ʼ��
	public Tank(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	
}
//���̹������
class Hero extends Tank  //�������super
{
	//Shoot s=null;   �˴�ʹ�ü��Ͽ���ʵ���ӵ�����Ч��
	Vector<Shoot> ss=new Vector<Shoot>();//��������������Ϊ��
	Shoot s=null;
	public Hero(int x,int y)
	{
		super(x,y);
		//this��һ��ָ�򱾶����ָ�룬this()ͬһ�����е�����������
		//super���õ�ǰ�����ֱ�Ӹ����еĳ�Ա�����ͳ�Ա������super()�������е��ø���Ĺ��췽��
	}
	//�����ӵ���������̹�˵Ķ���
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
		//�ӵ�������뼯����
		ss.add(s);   // �����ӵ��ĵط�paint()
		//�����ӵ��߳�
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
//����̹��
class Enemy extends Tank implements Runnable   //����̹�˴�ǽ
{
	//̹���Ƿ�����
	//boolean isLive=true;�Ѿ�����������Tank��
	
	//����һ�������򼯺�������ӵ�
	Vector<Shoot> shs=new Vector<Shoot>();//���Թ涨��������
	//����̹������ӵ�λ�ã�����̹�˸ոմ�����ʱ��͵��˵�̹��ײǽ����֮��
	public Enemy(int x,int y)
	{
		super(x,y);
	}
	
	//����һ���������Է��ʵ�MyPanel�����еĵ���̹��
	Vector<Enemy> ens=new Vector<Enemy>();
	//�õ�MyPanel�ϳ�ʼ���������  ����̹������  �������
	public void setEns(Vector<Enemy> vv)   //����̹��һ�����������͵����������
	{
		this.ens=vv;
	}
	//�жϵ���̹���Ƿ�����
	public boolean isTouchOtherEnemy()    //�������ã�������=======>���˱߽���
	{
		boolean b=false;
		//���ݵ�ǰ����ķ����ж�
		switch(this.direct)
		{
		case 0:
			//��ǰ̹������
			//ȡ������̹��  for����
			for(int i=0;i<ens.size();i++)
			{
				//ȡ��һ������̹��
				Enemy en=ens.get(i);
				//�ų��Լ�
				if(en!=this)
				{
					//���Լ��ĵ��˷��������ϻ�����
					if(en.direct==0||en.direct==1)
					{
						//���Լ�����̹�����
						if((this.x-12)>=(en.x-12)&&(this.x-12)<=(en.x+12)&&(this.y-15)>=(en.y-15)&&(this.y-15)<=(en.y+15))
						{
							b=true;   //����
						}
						//���Լ�����̹���ҽ�
						if((this.x+12)>=(en.x-12)&&(this.x+12)<=(en.x+12)&&(this.y-15)>=(en.y-15)&&(this.y-15)<=(en.y+15))
						{
							b=true; 
						}
					}
					//���Լ��ĵ��˷��������������
					if(en.direct==2||en.direct==3)
					{
						//���Լ�����̹�����
						if((this.x-12)>=(en.x-15)&&(this.x-12)<=(en.x+15)&&(this.y-15)>=(en.y-12)&&(this.y-15)<=(en.y+12))
						{
							b=true; 
						}
						//���Լ�����̹���ҽ�
						if((this.x+12)>=(en.x-15)&&(this.x+12)<=(en.x+15)&&(this.y-15)>=(en.y-12)&&(this.y-15)<=(en.y+12))
						{
							b=true; 
						}
					}
				}	
			}
			break;
		case 1:
			//��ǰ̹������
			//ȡ������̹��  for����
			for(int i=0;i<ens.size();i++)
			{
				//ȡ��һ������̹��
				Enemy en=ens.get(i);
				//�ų��Լ�
				if(en!=this)
				{
					//���Լ��ĵ��˷��������ϻ�����
					if(en.direct==0||en.direct==1)
					{
						//���Լ�����̹�����
						if((this.x-12)>=(en.x-12)&&(this.x-12)<=(en.x+12)&&(this.y+15)>=(en.y-15)&&(this.y+15)<=(en.y+15))
						{
							b=true; 
						}
						//���Լ�����̹���ҽ�
						if((this.x+12)>=(en.x-12)&&(this.x+12)<=(en.x+12)&&(this.y+15)>=(en.y-15)&&(this.y+15)<=(en.y+15))
						{
							b=true; 
						}
					}
					//���Լ��ĵ��˷��������������
					if(en.direct==2||en.direct==3)
					{
						//���Լ�����̹�����
						if((this.x-12)>=(en.x-15)&&(this.x-12)<=(en.x+15)&&(this.y+15)>=(en.y-12)&&(this.y+15)<=(en.y+12))
						{
							b=true; 
						}
						//���Լ�����̹���ҽ�
						if((this.x+12)>=(en.x-15)&&(this.x+12)<=(en.x+15)&&(this.y+15)>=(en.y-12)&&(this.y+15)<=(en.y+12))
						{
							b=true; 
						}
					}
				}	
			}
			break;
		case 2:
			//��ǰ̹������
			//ȡ������̹��  for����
			for(int i=0;i<ens.size();i++)
			{
				//ȡ��һ������̹��
				Enemy en=ens.get(i);
				//�ų��Լ�
				if(en!=this)
				{
					//���Լ��ĵ��˷��������ϻ�����
					if(en.direct==0||en.direct==1)
					{
						//���Լ�����̹���Ͻ�
						if((this.x-15)>=(en.x-12)&&(this.x-15)<=(en.x+12)&&(this.y-12)>=(en.y-15)&&(this.y-12)<=(en.y+15))
						{
							b=true; 
						}
						//���Լ�����̹���½�
						if((this.x-15)>=(en.x-12)&&(this.x-15)<=(en.x+12)&&(this.y+12)>=(en.y-15)&&(this.y+12)<=(en.y+15))
							
						{
							b=true; 
						}
					}
					//���Լ��ĵ��˷��������������
					if(en.direct==2||en.direct==3)
					{
						//���Լ�����̹���Ͻ�
						if((this.x-15)>=(en.x-15)&&(this.x-15)<=(en.x+15)&&(this.y-12)>=(en.y-12)&&(this.y-12)<=(en.y+12))
						{
							b=true; 
						}
						//���Լ�����̹���½�
						if((this.x-15)>=(en.x-15)&&(this.x-15)<=(en.x+15)&&(this.y+12)>=(en.y-12)&&(this.y+12)<=(en.y+12))
						{
							b=true; 
						}
					}
				}	
			}
			break;
		case 3:
			//��ǰ̹������
			//ȡ������̹��  for����
			for(int i=0;i<ens.size();i++)
			{
				//ȡ��һ������̹��
				Enemy en=ens.get(i);
				//�ų��Լ�
				if(en!=this)
				{
					//���Լ��ĵ��˷��������ϻ�����
					if(en.direct==0||en.direct==1)
					{
						//���Լ�����̹���Ͻ�
						if((this.x+15)>=(en.x-12)&&(this.x+15)<=(en.x+12)&&(this.y-12)>=(en.y-15)&&(this.y-12)<=(en.y+15))
						{
							b=true; 
						}
						//���Լ�����̹���½�
						if((this.x+15)>=(en.x-12)&&(this.x+15)<=(en.x+12)&&(this.y+12)>=(en.y-15)&&(this.y+12)<=(en.y+15))
						{
							b=true; 
						}
					}
					//���Լ��ĵ��˷��������������
					if(en.direct==2||en.direct==3)
					{
						//���Լ�����̹���Ͻ�
						if((this.x+15)>=(en.x-15)&&(this.x+15)<=(en.x+15)&&(this.y-12)>=(en.y-12)&&(this.y-12)<=(en.y+12))
						{
							b=true; 
						}
						//���Լ�����̹���½�
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
	
	
	//����һ��time���涨�������
	int time=0;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//�߳����ݣ��õ���̹���Լ�������---------------->����ȥ�߳�����������������߳�
		while(true)   //ÿ��30(��)*50=1.5s��һ�η�����߹��
		{
			//���ݷ����ƶ�
			switch(this.direct)
			{
			case 0://����
				//��for������ƽ�����ƶ�
				for(int i=0;i<30;i++)
				{
					if(y>15&&!this.isTouchOtherEnemy())
					{
						y-=speed1;
					}
					try {
						Thread.sleep(50);//50ms�ƶ�һ��
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 1://����
				//��for������ƽ�����ƶ�
				for(int i=0;i<30;i++)
				{
					if(y<285&&!this.isTouchOtherEnemy())
					{
						y+=speed1;
					}
					try {
						Thread.sleep(50);//50ms�ƶ�һ��
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 2://����
				//��for������ƽ�����ƶ�
				for(int i=0;i<30;i++)
				{
					if(x>15&&!this.isTouchOtherEnemy())
					{
						x-=speed1;
					}
					try {
						Thread.sleep(50);//50ms�ƶ�һ��
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 3://����
				//��for������ƽ�����ƶ�
				for(int i=0;i<30;i++)
				{
					if(x<385&&!this.isTouchOtherEnemy())
					{
						x+=speed1;
					}
					try {
						Thread.sleep(50);//50ms�ƶ�һ��
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			}
			this.time++;
			if(this.time%2==0) //ÿ��3s����ӵ�������
			{
				//�ж��Ƿ�Ҫ��̹�˼������ӵ�
					if(isLive==true)
					{
						if(shs.size()<3)  //������Բ�������̹�����������ӵ�
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
							//�����ӵ��߳�
							Thread th=new Thread(sh);
							th.start();
						}
					}
			}
			//�����������
			this.direct=(int)(Math.random()*4);
			
			//��̹���������˳��߳�
			if(this.isLive==false)
			{
				break;
			}
		}
	}
}

//�ӵ���-->�ӵ��̡߳��������ӵ����Ĵ����������������߳�
class Shoot implements Runnable      //�ӵ���ǽ
{
	//�������
	int x=0;
	int y=0;
	//����
	int direct;
	//�ٶ�
	int speed=3;
	//�ӵ�����
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
			//����50ms,��ֹ�ڴ���ܲ���
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			switch(direct)
			{
			case 0://��
				y-=speed;
				break;
			case 1://��
				y+=speed;
				break;
			case 2://��
				x-=speed;
				break;
			case 3://��
				x+=speed;
				break;
			}
			//System.out.println("������  "+x);//�����ӵ��Ƿ�������������û��repaint()��������
			
			//�ӵ���ʱ����
			if(x<0||x>400||y<0||y>300)
			{
				isLive=false;
				break;
			}
		}
	}
}

//���屬ը��
class Bomb
{
	//��ը����
	int x=0;
	int y=0;
	//ը����ը������
	int life=9;
	//ը���������
	boolean isLive=true;
	//���캯����ʼ��
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

//����һ����¼�࣬���Ǿ�̬��static:������֮�乲��������������������÷���
class Recorder
{
	//��¼����̹�˵�����
	private static int enNum=20;
	//��¼��ҵ�������
	private static int myNum=3;
	//��ҵ�ս������ɱ̹�˵�����
	private static int hitEnNum=0;
	
	//�����ļ���
	private static FileWriter fw=null;
	private static BufferedWriter bw=null; //������������д���ļ�
	private static FileReader fr=null;
	private static BufferedReader br=null;
	//1//������ҳɼ��ķ�װ����
	public static void saveRecordering()     //�����static����������ͨ������ֱ�ӵ��ã��Ͳ���Ҫʵ������
	{
		try {
			fw=new FileWriter("d:/myMark.txt");
			bw=new BufferedWriter(fw);
			
			bw.write(Recorder.hitEnNum+"\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//�ȴ򿪺����
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	//2//��ȡ��ҳɼ��ķ�װ����
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
	//3//�����˳���������˵�����ͷ���
	//֪������ϵ���
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
			
			//forѭ������ÿ������
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
			//�ȴ򿪺����
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	//4//������һ��
	
	//����һ��������װÿ����¼�ĵ�
	static Vector<Node> nodes=new Vector<Node>();
	//��ȡ��¼�ĺ���
	public Vector<Node> getNodeAndHitEnNum()
	{
		try {
			fr=new FileReader("d:/myMark.txt");
			br=new BufferedReader(fr);
			String nn="";
			//�ȶ�ȡ��һ�У�����ܳɼ�
			nn=br.readLine();
			hitEnNum=Integer.parseInt(nn);
			
			//��ȡ���漸�У����˵�����ͷ���    nn=br.readLine()
			while((nn=br.readLine())!=null)
			{
				String []xyz=nn.split(" ");//��������ÿ�����ݰ��ո���ָ������   [ 50 30 2]
				//��Ϊ��֪��ÿ�п��Էֳ���������
				Node node=new Node(Integer.parseInt(xyz[0]),Integer.parseInt(xyz[1]),Integer.parseInt(xyz[1]));
				//������뼯����
				nodes.add(node);   //����Ӧ��ε��ã���
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
	//���к���ٵ���̹����
	public static void reduceEnNum()
	{
		enNum--;
	}
	//���к�������ҳɼ�
	public static void addHitEnNum()
	{
		hitEnNum++;
	}
}
//����һ�����࣬���ó���¼��,������һ����ϷҪ��
class Node
{
	int x=0;
	int y=0;
	int direct=0;
		
	//���캯����ʼ��
	public Node(int x,int y,int direct)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
}
//������������----���̵߳ķ�ʽ������������
class OpenSong extends Thread    //ֻҪ������Ϸ����Ϳ�ʼ����
{
	//���������ļ�
	private String filename;
	//���캯����ʼ���������������ļ�
	public OpenSong(String wavename)
	{
		this.filename=wavename;
	}
	//run�������߳�ִ�е�
	public void run()
	{
	//�ڴ��õ������ļ�	
		//�����ļ�����
		File soundFile=new File(filename);
		//���建����
		AudioInputStream audioinputstream=null;
		//�����ļ����ص�������
		try {
			audioinputstream=AudioSystem.getAudioInputStream(soundFile);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	//�ڴ��õ�������
		//���岥��������
		AudioFormat format=audioinputstream.getFormat();
		//�������Ļ�����
		SourceDataLine sourceLine=null;
		DataLine.Info info=new DataLine.Info(SourceDataLine.class, format);
		
		try {
			sourceLine=(SourceDataLine)AudioSystem.getLine(info);// Lineǿ��ת����SourceDataLine
			sourceLine.open(format);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return;
		}
		
		sourceLine.start();//��ʼ��ȡ���򿪲�����
		int nBytesRead=0;
		//ÿ�ζ�ȡ����
		byte[] ssdata=new byte[1024];
		//������Ƶ
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
			sourceLine.drain();//�ž�line�����Ϣ
			sourceLine.close();
		}
		
	}
}



