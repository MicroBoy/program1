/**
 * ���ߣ� �� ��
 * ���ܣ� ���˻����ұ�ը
 * 		�ظ��Ĵ�����߱Ƚϳ��Ĵ��룬���Է�װ�ɺ��������Ӵ���Ŀɶ���
 * 
 * 		�������������Լ�¼��ҵĳɼ�����������������   (�Ѷȴ�)
 * 					�õ��ļ���
 * 				          ����һ����¼��,����ҳɼ���¼
 * 					�˳���Ϸʱ���������ҳɼ����ļ���(���ܽ���������ĵ���̹��)
 * 					�����˳�����¼��ʱ����̹�����겢�ָ�(���Ѷ�)
 *  	����������Java��β��������ļ�����������������
 * ���ڣ�2017/7/11~13
 */

package com.tankgame_99;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;//�ļ���

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;

public class TankGame99_1 extends JFrame implements ActionListener{

	MyPanel9 mp=null;
	//���忪ʼ��Ϸ������
	MyStartPanel msp=null;
	//����˵����
	JMenuBar jmb=null;
	JMenu jm1=null;
	JMenuItem jmi1,jmi2,jmi3,jmi4;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TankGame99_1 tg=new TankGame99_1();
	}
	
	public TankGame99_1()
	{
		//�����˵����
		jmb=new JMenuBar();
		jm1=new JMenu("��Ϸ(G)");
		jm1.setMnemonic('G');
		jmi1=new JMenuItem("��ʼ��Ϸ");//jmi1 ����˴�ʱ����ʼ��ת����Ϸ���˴��������¼�Դ,����jmi1��Ӧ
		jmi1.setActionCommand("start");
		jmi2=new JMenuItem("�˳���Ϸ");
		jmi2.setActionCommand("exit");
		jmi3=new JMenuItem("�����˳�");
		jmi3.setActionCommand("saveExit");
		jmi4=new JMenuItem("������һ����Ϸ");
		jmi4.setActionCommand("continueGame");
		//��jmi1������Ӧ��ע�����
		jmi1.addActionListener(this);
		//��jmi2����
		jmi2.addActionListener(this);
		//��jmi3����
		jmi3.addActionListener(this);
		//��jmi4����
		jmi4.addActionListener(this);
		
		jm1.add(jmi1);
		jm1.add(jmi2);
		jm1.add(jmi3);
		jm1.add(jmi4);
		jmb.add(jm1);
		this.setJMenuBar(jmb);
		
		msp=new MyStartPanel();
		Thread tt=new Thread(msp);
		tt.start();//�����߳�
		this.add(msp);
		
		this.setSize(600,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//���û���ͬ�ĵ��������ͬ�Ĵ���
		if(e.getActionCommand().equals("start"))
		{
			//��ʼ��Ϸ
			
			//��ȡ��Ϸ��¼
			Recorder.getRecordering();
			
			mp=new MyPanel9("newGame");//JPanel-->�Զ�����paint()����
			Thread th=new Thread(mp); 
			th.start();
			//��ɾ���ɵ����  ��
			this.remove(msp);
			this.add(mp);
			//ע�����
			this.addKeyListener(mp);//���¼��ϼ������	
			//��ʾ  ��
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setVisible(true);		
		}else if(e.getActionCommand().equals("exit"))
		{
			//�˳���Ϸ��������һ�ֵ���״̬
			
			//Ҫ������ҳɼ����ⲿ�ֵ��÷�װ�������ɶ��Ժ�     ��¼����
			Recorder.saveRecordering();
			
			System.exit(0);//�����˳�
		}else if(e.getActionCommand().equals("saveExit"))
		{
			//�����˳�����������һ�ֵ���״̬
			//�Ȱ�����ϵ��˵�״̬������¼
			Recorder.setEns(this.mp.ens);
			//�����˳�
			Recorder.saveExitRecordering();
			
			System.exit(0);
		}else if(e.getActionCommand().equals("continueGame"))
		{
			//��ʼ��Ϸʱ��������һ�֣���ȡ�ļ��еĵ���̹�˵�״̬
			
			//mp.nodes=new Recorder().getNodeAndHitEnNum();----->�ڶ����ֱ�Ӹ�
			mp=new MyPanel9("conGame");//JPanel-->�Զ�����paint()����
			
			Thread th=new Thread(mp); 
			th.start();
			//��ɾ���ɵ����  ��
			this.remove(msp);
			this.add(mp);
			//ע�����
			this.addKeyListener(mp);//���¼��ϼ������	
			//��ʾ  ��
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setVisible(true);	
		}
	}

}
//��Ϸ��ʼPanel
class MyStartPanel extends JPanel implements Runnable  //�����߳���������˸
{
	int times=0;  //������˸
	//�������
	public void paint(Graphics g)
	{
		super.paint(g);
		g.fillRect(0, 0, 400, 300); //fillRect()Ĭ�Ϻ�ɫ��䣬drawRect()Ĭ����ɫ
		
		//�ؿ���Ϣ��������
		if(this.times%2==0)//1s��˸һ��
		{
			Font myFont=new Font("������κ",Font.BOLD,50); 
			g.setFont(myFont);
			g.setColor(Color.yellow);
			g.drawString("stage: 1", 90, 160);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			//���500ms�ػ�
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//�����ػ�����
			this.repaint(); //�ٴ�ִ��paint()����
			//����
			times++;
		}
	} 
}
//�����Լ���Ϸ��������
class MyPanel9 extends JPanel implements KeyListener,Runnable
{
	Hero hero=null;
	
	//����һ�������࣬��ŵз�̹��
	Vector<Enemy> ens=new Vector<Enemy>();
	//���弯������ȡ���ĵ�
	Vector<Node> nodes=new Vector<Node>();      //����������������д���
	//����ը������_����ͬʱ���ж��̹�� 
	Vector<Bomb> bombs=new Vector<Bomb>();
	
	//���屬ըͼƬ
	Image image1=null;
	Image image2=null;
	Image image3=null;

	int size=6;  //����̹����
	//Ҫ�ڹ��캯���г�ʼ��
	public MyPanel9(String flag)  //һ�ᵽ��ʼ����Ҫ�ڹ��캯����_��ʼ����Һ͵���̹��
	{
		hero=new Hero(100, 100);
		
		if(flag.equals("newGame"))
		{
			//��ʼ������̹��
			for(int i=0;i<size;i++)
			{
				Enemy en=new Enemy((i+1)*40, 15);//����̹�˳�ʼ������λ��
				
				en.setColor(0);//���˶�����ɫ
				en.setDirect(1);//���˶������·���
				//��MyPanel�ϵ���̹��������������̹��
				en.setEns(ens);            ///////////////////////////////����̹��
				
				//��������̹��
				Thread th=new Thread(en);
				th.start();
				//����̹�˴����������һ���ӵ�
				Shoot sh=new Shoot(en.x, en.y+15, 1);//Shoot���߳�
				//��ӵ� ����̹�� �� �ӵ����� ��
				en.shs.add(sh);  //------>�����ӵ�
				Thread th1=new Thread(sh);
				th1.start();
				ens.add(en);//���뼯��    ///////////////////////MyPanel�ϵĵ���̹������
			}
		}else if(flag.equals("conGame")){
			nodes=new Recorder().getNodeAndHitEnNum();    //staic�ͷ�static �������������͵ķ���������: ����().������()
			//��ʼ������̹��                                                                                    //         ��������������: ����.������() ��  ������.������()
			for(int i=0;i<nodes.size();i++)
			{
				Node node=nodes.get(i);
				
				Enemy en=new Enemy(node.x, node.y);//����̹�˳�ʼ������λ��
				
				en.setColor(0);//���˶�����ɫ
				en.setDirect(node.direct);//���˶������·���
				//��MyPanel�ϵ���̹��������������̹��
				en.setEns(ens);            ///////////////////////////////����̹��
				
				//��������̹��
				Thread th=new Thread(en);
				th.start();
				//����̹�˴����������һ���ӵ�
				Shoot sh=new Shoot(en.x, en.y+15, 1);//Shoot���߳�
				//��ӵ� ����̹�� �� �ӵ����� ��
				en.shs.add(sh);  //------>�����ӵ�
				Thread th1=new Thread(sh);
				th1.start();
				ens.add(en);//���뼯��    ///////////////////////MyPanel�ϵĵ���̹������
			}
		}
		//��������
		OpenSong BGM=new OpenSong("F:\\zhuawa\\program\\java_project\\TankWar\\111.wav");//�����ȶ�����Ƶ����
		BGM.start();
		//��ʼ��ͼƬ===>ը��Ч��������
		image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));//����ͼƬ����
		image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
		image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));
	}
	//����̹���ӵ������ұ�ը
	public void hitMe()
	{
		//ȡ��һ������̹��
		for(int i=0;i<this.ens.size();i++)
		{
			Enemy enemyTank=this.ens.get(i);
			//ȡ��һ������̹�˵�һ���ӵ�
			for(int j=0;j<enemyTank.shs.size();j++)
			{
				Shoot enemyShoot=enemyTank.shs.get(j);
				
				if(hero.isLive)
				{
					if(this.hitTank(enemyShoot, hero))            //��������󱻻��в��ᱬը,������θ���
					{
						
					}
				}
			}
		}
	}
	//����ӵ�����̹�˱�ը
	public void hitEnemyTank()
	{
		for(int i=0;i<hero.ss.size();i++)
		{
			Shoot MyShot=hero.ss.get(i);
			if(MyShot.isLive)
			{
				//������̹��
				for(int j=0;j<ens.size();j++)
				{
					Enemy en=ens.get(j);
					if(en.isLive)
					{
						//�ж�
						if(this.hitTank(MyShot, en))     //ȥ��̹�˵ĵط�������ôд
						{
							//����̹����
							Recorder.reduceEnNum();
							//������ҳɼ�
							Recorder.addHitEnNum();
						}
					}
				}
			}
		}
	}
	//�ж��ӵ����е���̹�˵ķ���, ���ز���ֵ
	public boolean hitTank(Shoot s,Tank e)  //���ӵ��͵���̹�˶������
	{
		//�ӵ��Ƿ����̹��
		boolean bb=false;   //ÿ�ε����Ƕ����ʼ��Ϊfalse
		
		switch(e.direct)
		{
		case 0://��
		case 1://��
			if(s.x>(e.x-15)&&s.x<(e.x+15)&&s.y>(e.y-12)&&s.y<(e.y+12))
			{
				//�ӵ�����
				s.isLive=false;
				//̹������
				e.isLive=false;

				bb=true;
				
				//���屬ը����
				Bomb b=new Bomb(e.x, e.y);
				//���뼯��
				bombs.add(b);//֮����ǻ滭
			}
			break;
		case 2://��
		case 3://��
			if(s.x>(e.x-12)&&s.x<(e.x+12)&&s.y>(e.y-15)&&s.y<(e.y+15))
			{
				//�ӵ�����
				s.isLive=false;
				//̹������
				e.isLive=false;	

				bb=true;
				
				//���屬ը����
				Bomb b=new Bomb(e.x, e.y);
				//���뼯��
				bombs.add(b);
			}
			break;
		}
		return bb;
	}
	//���Ǹ��෽��_����_�ǵü��뻭��
	public void paint(Graphics g)
	{
		super.paint(g);
		
		//�û���Ϳ�����
		g.fillRect(0, 0, 400, 300);
		//�����ǰ���ִ�е�
		
		//��Ϸ��ʾ��Ϣ����
		this.showGameInfomation(g);
		
		//�����̹��
		if(hero.isLive)
		{
			this.drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 1);
		}
		//���Ʊ�ըЧ��
		for(int i=0;i<bombs.size();i++)
		{
			//�Ӽ�����ȡ��
			Bomb b=bombs.get(i);
			
			//ͼƬ�л����ֱ�ըЧ��
			if(b.life>6)
			{
				g.drawImage(image1, b.x-15, b.y-15, 30, 30, this);
			}else if(b.life>3)
			{
				g.drawImage(image2, b.x-15, b.y-15, 30, 30, this);
			}else {
				g.drawImage(image3, b.x-15, b.y-15, 30, 30, this);
			}
			//��b��С
			b.lifeDown();
			//ը������Ϊ0ʱ���Ӽ�����ȥ��
			if(b.life==0)
			{
				bombs.remove(b);//�мǲ�Ҫ��i
			}
		}
		//������̹��
		for(int i=0;i<ens.size();i++)
		{
			Enemy en=ens.get(i);
			if(en.isLive)
			{
				this.drawTank(en.getX(), en.getY(), g, en.getDirect(), 0);
				//�������������ӵ�
				for(int j=0;j<en.shs.size();j++)
				{
					Shoot enemyShot=en.shs.get(j);
					//System.out.println("��"+j+"���ӵ������꣺"+enemyShot.x+"  "+enemyShot.y);
					if(enemyShot.isLive==true)
					{
						g.drawOval(enemyShot.x, enemyShot.y, 1, 1);
					}else{
						//�ӵ�ײǽ������ȥ��
						en.shs.remove(enemyShot);
					}
				}
				
			}
		}
		//�û��ʻ��ӵ�,for����������ӵ�
		for(int i=0;i<hero.ss.size();i++)
		{
			//�Ӽ���ȡ���ӵ�
			Shoot MyShot=hero.ss.get(i);
			
			if(MyShot!=null&&MyShot.isLive==true)//ֻ���ӵ������˲ſ��Ի�
			{
				g.setColor(Color.yellow);
				g.fill3DRect(MyShot.x, MyShot.y, 2, 2, false);	
			}
			//�ӵ�����߿򣬾�������ȥ��
			if(MyShot.isLive==false)
			{
				hero.ss.remove(MyShot);//MyShot������i
			}
		}
		
	}
	//����Ϸ��ʾ��Ϣ���ݴ���ɺ���
	public void showGameInfomation(Graphics g)//ֻҪ��������
	{
		Font f=new Font("����", Font.BOLD, 15);
		g.setFont(f);
		//������
		this.drawTank(100, 350, g, 0, 0);
		g.setColor(Color.black);
		g.drawString(Recorder.getEnNum()+"", 125, 355);
		//�������
		this.drawTank(170, 350, g, 0, 1);
		g.setColor(Color.black);
		g.drawString(Recorder.getMyNum()+"", 195, 355);
		
		g.drawString("��ҵĳɼ�", 440, 110);
		//���е�����
		this.drawTank(470, 150, g, 0, 0);
		g.setColor(Color.black);
		g.drawString(Recorder.getHitEnNum()+"", 495, 155);
	}
	
	//��̹�˵ĺ���
	public void drawTank(int x,int y,Graphics g,int diect,int type)
	{
		//��� ����  ����̹����ɫ
		switch(type)
		{
		case 0:
			g.setColor(Color.CYAN);
			break;
		case 1:
			g.setColor(Color.yellow);
			break;
		}
		//̹�˷���
		switch(diect)
		{
		//��Բ��Ϊ����̡�
		case 0: //����
			g.fill3DRect(x-12, y-15, 5, 30, false);
			g.fill3DRect(x-7, y-10, 14, 20, false);
			g.fill3DRect(x+7, y-15, 5, 30, false);
			g.fillOval(x-5, y-5, 10, 10);
			g.drawLine(x, y, x, y-18);  //x,y��Բ��
			break;	
		case 1: //����
			g.fill3DRect(x-12, y-15, 5, 30, false);
			g.fill3DRect(x-7, y-10, 14, 20, false);
			g.fill3DRect(x+7, y-15, 5, 30, false);
			g.fillOval(x-5, y-5, 10, 10);
			g.drawLine(x, y, x, y+18);  //x,y��Բ��
			break;
		case 2: //����
			g.fill3DRect(x-15, y-12, 30, 5, false);
			g.fill3DRect(x-10, y-7, 20, 14, false);
			g.fill3DRect(x-15, y+7, 30, 5, false);
			g.fillOval(x-5, y-5, 10, 10);
			g.drawLine(x, y, x-18, y);  //x,y��Բ��
			break;				
		case 3: //����
			g.fill3DRect(x-15, y-12, 30, 5, false);
			g.fill3DRect(x-10, y-7, 20, 14, false);
			g.fill3DRect(x-15, y+7, 30, 5, false);
			g.fillOval(x-5, y-5, 10, 10);
			g.drawLine(x, y, x+18, y);  //x,y��Բ��
			break;				
		}
	}

	//����������ʱ���¼�������__һ��Ҫ���ź�������װ��˼��
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		//���������µĿ���
		if(e.getKeyCode()==KeyEvent.VK_UP)
		{
			//�����ƶ�
			this.hero.moveUp();  //hero.getY()���Ǳ���
			this.hero.setDirect(0);
		}else if(e.getKeyCode()==KeyEvent.VK_DOWN)
		{
			//�����ƶ�
			this.hero.moveDown();
			this.hero.setDirect(1);
		}else if(e.getKeyCode()==KeyEvent.VK_LEFT)
		{
			//�����ƶ�
			this.hero.moveLeft();
			this.hero.setDirect(2);
		}else if(e.getKeyCode()==KeyEvent.VK_RIGHT)
		{
			//�����ƶ�
			this.hero.moveRight();
			this.hero.setDirect(3);
		}
		
		//�ж��Ƿ��������
		if(e.getKeyCode()==KeyEvent.VK_J)
		{
			//�����ӵ�����_��������5��
			if(hero.ss.size()<5)
			{
				this.hero.shootEnemy();
			}
		}
		//��repaint()�ػ�̹��
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
		//ÿ��100ms�ػ�һ�����
		while(true)
		{
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			//���û����жϺ����������ӵ���̹��
			//�������ӵ�
			this.hitEnemyTank();//���˱�ը
			this.hitMe();//��ұ�ը

//�ѵ���̹�˵��ӵ���ӿ��Է��ڵ���̹�˵����е�run������

			//�ػ�
			this.repaint();
		}
	}
	
}




