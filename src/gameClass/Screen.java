package gameClass;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import sprites.Texture;
@SuppressWarnings("serial")
public class Screen extends JPanel implements Runnable{
	private Thread game;
	protected static boolean isRunning;
	protected boolean doodleAlive = true;
	protected boolean paused = false;
	protected double numberOfPlatforms = 0;
	private int GRAVITY = 7;
	private volatile int score = 0;
	protected static int screenW = 600;
	protected static int screenH = 900;
	protected static ArrayList<GameObject> sprites = new ArrayList<GameObject>();
	protected volatile Doodler doodle = new Doodler(screenW/2,(int)(screenH*.1));
	protected Screen(){
		sprites.add(doodle);
		initialPlatforms();
		clicks();
		panel();
		start();
	}
	void clicks(){
		addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if(!doodleAlive){
					restart();
				}
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if(doodleAlive){
					doodle.isShooting = true;
					doodle.shoot();
					score-=10;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if(doodleAlive){
					doodle.isShooting = false;
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				paused = false;
				start();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				paused = true;
				stop();
			}

		});
	}
	void initialPlatforms(){
		int xBuffer = 0;
		int yBuffer = 0;
		for(int index = 0 ; index < 12; index++){
			PlatformType p = null;
			switch((int)(Math.random()*3)){
			case 0:
				p = PlatformType.BLUE;
				break;
			case 1:
				p = PlatformType.GREEN;
				break;
			case 2:
				p = PlatformType.WHITE;
				break;
			}
			if((int)(Math.random()*15)==1){
				p = PlatformType.RED;
			}
			sprites.add(new Platform(xBuffer,yBuffer,p));
			numberOfPlatforms++;
			switch((int)(Math.random()*2)){
			case 0:
				if(xBuffer<screenW-200)
					xBuffer+=200;
				break;
			case 1:
				if(xBuffer>200)
					xBuffer-=200;
				break;
			}
			switch((int)(Math.random()*2)){
			case 0:
				yBuffer+=150;
				break;
			case 1:
				yBuffer+=100;
				break;
			}
		}
	}
	void panel(){
		JFrame frame = new JFrame("DOODLE JUMP");
		frame.add(this);
		this.setLayout(null);
		frame.setPreferredSize(new Dimension(screenW,screenH));
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getInputMap().put(KeyStroke.getKeyStroke("A"), "TESTKEY");
		getActionMap().put("TESTKEY", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				doodle.flipJump();
			}

		});



	}
	synchronized void start(){
		isRunning = true;
		game = new Thread(this);
		game.start();
		backgroundChecks();
		doodle.characterLoops();
	}
	void restart(){
		sprites.clear();
		doodleAlive = true;
		doodle = new Doodler(screenW/2,(int)(screenH*.1));
		sprites.add(doodle);
		initialPlatforms();
		score = 0;
		start();
	}
	void backgroundChecks(){
		Thread locateMouse = new Thread(new Runnable(){
			public void run(){
				while(isRunning){
					try{
						int mouseX = MouseInfo.getPointerInfo().getLocation().x;

						if(doodle.getX()+3<mouseX){ //3 pixel buffer
							doodle.setxVelo(3);
						}else if(doodle.getX()-3>mouseX){
							doodle.setxVelo(-3);
						}else{
							doodle.setxVelo(0);
						}

					}catch(Exception e) { } 
				}
			}
		});
		Thread managePlatforms = new Thread(new Runnable(){
			public void run(){
				while(isRunning){
					boolean allOnScreen = true;
					synchronized(sprites){
						for(int index = 0; index < sprites.size(); index++){
							GameObject o = sprites.get(index);
							if(o.getT().equals(GameType.PLATFORM)){
								if(((Platform)o).getP().equals(PlatformType.BLUE)){
									if(o.getX()<=0){
										o.setxVelo(3);
									}
									if(o.getX()+Doodler.doodleW>=screenW){
										o.setxVelo(-3);
									}
								}
								if(o.getY()<=0){
									allOnScreen = false;
								}
							}
						}
					}
					if(allOnScreen){
						spawnInNewPlatforms();
						allOnScreen = false;
					}
					try{
						Thread.sleep(1);
					}catch(Exception e) { }
				}
			}
		});
		Thread destoryPlatforms = new Thread(new Runnable(){
			public void run(){
				while(isRunning){
					synchronized(sprites){
						for(int index = 0; index < sprites.size(); index++){
							GameObject o = sprites.get(index);
							if(o.getT().equals(GameType.PLATFORM)||o.getT().equals(GameType.MONSTERS)){
								if(o.getY()>=screenH){
									sprites.remove(o);
									numberOfPlatforms--;
								}
							}
							if(o.getT().equals(GameType.SPRING)||o.getT().equals(GameType.TRAMP)){
								if(o.getY()>=screenH)
									sprites.remove(o);
							}
						}
					}
					synchronized(doodle.bullets){
						for(int index = 0; index < doodle.bullets.size(); index++){
							Projectile p = doodle.bullets.get(index);
							if(p.getY()<-100){
								doodle.bullets.remove(p);
							}
						}
					}
					try{
						Thread.sleep(1);
					}catch(Exception e) { } 
				}
			}
		});
		Thread checkForAction = new Thread(new Runnable(){
			public void run(){
				while(isRunning){
					synchronized(sprites){
						if(doodle.canJump&&doodle.getyVelo()>0){
							for(int index = 0; index < sprites.size(); index++){
								GameObject o = sprites.get(index);
								if(o.getT().equals(GameType.PLATFORM)){
									if(((Platform)o).getP().equals(PlatformType.RED)&&((Platform)o).steppedOn){
										continue;
									}
									int platX1 = o.getX();
									int platX2 = o.getX() + Platform.platW;
									int platY1 = o.getY();
									int platY2 = o.getY() + Platform.platH;
									if(doodle.getX()+Doodler.doodleW/2>platX1&&doodle.getX()+Doodler.doodleW/2<platX2&&doodle.getY()+Doodler.doodleH>platY1&&doodle.getY()+Doodler.doodleH<platY2){
										if(!((Platform)o).getP().equals(PlatformType.RED))
										doodle.jump();
										o.steppedOn = true;
									}
								}
							}
						}
					}
					try{
						Thread.sleep(1);
					}catch(Exception e) { }
				}
			}
		});
		Thread manageScore = new Thread(new Runnable(){
			public void run(){
				while(isRunning){
					if(doodle.isJumping){
						score++;
						try{
							Thread.sleep(10);
						}catch(Exception e) { } 
					}
				}
			}
		});
		Thread manageBorders = new Thread(new Runnable(){
			public void run(){
				while(isRunning){
					if(doodle.getX()<0){
						doodle.setX(3);
					}
					if(doodle.getX()+100>screenW){
						doodle.setX(-3);
					}
					if(doodle.getY()<0){
						doodle.setY(1);
						GRAVITY = 12;
					}else if(doodle.isSuperJumping||doodle.isFlipJumping){
						GRAVITY = 15;
					}else{
						GRAVITY = 7;
					}
					try{
						Thread.sleep(1);
					}catch(Exception e) { } 
				}
			}
		});
		Thread manageItems = new Thread(new Runnable(){
			public void run(){
				while(isRunning){
					synchronized(sprites){
						for(int index = 0; index < sprites.size(); index++){
							GameObject o = sprites.get(index);
							if(o.getT().equals(GameType.DOODLER)){
								continue;
							}
							if(o.getT().equals(GameType.SPRING)){
								//bounce with springs
								int springX = o.getX();
								int springX2 = o.getX() + Spring.springW;
								int springY = o.getY();
								int springY2 = o.getY() + Spring.springH;
								
								if(doodle.getX()+Doodler.doodleW/2>springX&&doodle.getX()+Doodler.doodleW/2<springX2&&doodle.getY()+Doodler.doodleH>springY&&doodle.getY()+Doodler.doodleH<springY2){
									doodle.superJump();
									o.steppedOn = true;
									
								}
								
								
							}
							if(o.getT().equals(GameType.TRAMP)){
								//bounce with springs
								int trampX = o.getX();
								int trampX2 = o.getX() + Tramp.trampW;
								int trampY = o.getY();
								int trampY2 = o.getY() + Tramp.trampH;
								
								if(doodle.getX()+Doodler.doodleW/2>trampX&&doodle.getX()+Doodler.doodleW/2<trampX2&&doodle.getY()+Doodler.doodleH>trampY&&doodle.getY()+Doodler.doodleH<trampY2){
									doodle.flipJump();
									o.steppedOn = true;
									
								}
								
								
							}
						}
					}
					try{
						Thread.sleep(1);
					}catch(Exception e) { }
				}
			}
		});
		Thread manageMonsters = new Thread(new Runnable(){
			public void run(){
				while(isRunning){
					synchronized(sprites){
						for(int index = 0; index < sprites.size(); index++){
							GameObject o = sprites.get(index);
							if(o.getT().equals(GameType.MONSTERS)){
								if(o.getX()<=0){
									o.setxVelo(3);
								}
								if(o.getX()+Monsters.monsterW>=screenW){
									o.setxVelo(-3);
								}
							
							//check bullet lists
							synchronized(doodle.bullets){
								for(int index2 = 0 ; index2 < doodle.bullets.size(); index2++){
									Projectile p = doodle.bullets.get(index2);
									int px1 = p.getX() + Projectile.projW/2;
									int py1 = p.getY() + Projectile.projH/2;
									
									int monsterX1 = o.getX();
									int monsterX2 = o.getX() + Monsters.monsterW;
									int monsterY1 = o.getY();
									int monsterY2 = o.getY() + Monsters.monsterH;
									
									if(px1 > monsterX1 && px1 < monsterX2 && py1 > monsterY1 && py1 < monsterY2){
										sprites.remove(o);
										doodle.bullets.remove(p);
										score+=400;
									}
								}
								}
							}
						}
					}
					try{
						Thread.sleep(1);
					}catch(Exception e) { }
				}
			}
		});
		manageMonsters.start();
		manageItems.start();
		manageBorders.start();
		manageScore.start();
		checkForAction.start();
		destoryPlatforms.start();
		managePlatforms.start();
		locateMouse.start();
	}
	synchronized void spawnInNewPlatforms(){
		int yBuffer = 0;
		for(int index = 0; index < 20; index++){
			int xBuffer = (int)(Math.random()*screenW); 
			while(xBuffer<0||xBuffer>screenW-Platform.platW){
				xBuffer = (int)(Math.random()*screenW);
			}
			yBuffer -= (int)(Math.random()*150)+50;
			PlatformType p = null;
			switch((int)(Math.random()*3)){
			case 0:
				p = PlatformType.BLUE;
				break;
			case 1:
				p = PlatformType.GREEN;
				break;
			case 2:
				p = PlatformType.WHITE;
				break;
			}
			if((int)(Math.random()*15)==1){
				p = PlatformType.RED;
			}
			if((int)(Math.random()*8)==1){
				sprites.add(new Monsters(xBuffer,yBuffer,MonsterType.GREEN));
			}
			//spawning in springs
			int rand = (int)(Math.random()*10);
			if(rand <= 3){ 	//3/10 chance
				if(!p.equals(PlatformType.BLUE)&&!p.equals(PlatformType.RED))
					sprites.add(new Spring(xBuffer,yBuffer-Platform.platH));
			}else if(rand == 4){ //1/10 chance
				if(!p.equals(PlatformType.BLUE)&&!p.equals(PlatformType.RED))
					sprites.add(new Tramp((int) (xBuffer-Platform.platW/1.5),yBuffer-Tramp.trampH));
			}
			sprites.add(new Platform(xBuffer,yBuffer,p));
			numberOfPlatforms++;
		}
	}
	synchronized void stop(){
		try{
			isRunning = false;
			repaint();
		}catch(Exception e){ } 
	}
	public void run(){
		while(isRunning){
			update();
			try{
				Thread.sleep(10);
			}catch(Exception e) { }
		}
	}
	void update(){
		moveLoc();
		physics();
		checkForFall();
		repaint();
	}
	void checkForFall(){
		if(doodle.getY()>screenH){
			doodleAlive = false;
			stop();
		}
	}
	void physics(){
		synchronized(sprites){
			for(int index = 0; index < sprites.size(); index++){
				GameObject o = sprites.get(index);
				if(o.getT().equals(GameType.DOODLER)){
					if(!doodle.isJumping)
						o.setyVelo(GRAVITY);
				}
				if(o.getT().equals(GameType.PLATFORM)||o.getT().equals(GameType.SPRING)||o.getT().equals(GameType.TRAMP)||o.getT().equals(GameType.MONSTERS)){
					if(doodle.isJumping){
						o.setyVelo(GRAVITY);
					}else{
						o.setyVelo(0);
					}
				}
			}
		}
	}
	void moveLoc(){
		synchronized(sprites){
			for(int index = 0; index < sprites.size(); index++){
				GameObject o = sprites.get(index);
				o.setX(o.getxVelo());
				o.setY(o.getyVelo());
			}
		}
		synchronized(doodle.bullets){
			for(int index = 0; index < doodle.bullets.size(); index++){
				Projectile p = doodle.bullets.get(index);
				p.setX(p.getxVelo());
				p.setY(p.getyVelo());
			}
		}
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		drawBackDrop(g);
		drawSprites(g);
		drawDoodle(g);
		drawScore(g);
		if(!doodleAlive){
			g.setFont(new Font("Aerial",Font.BOLD,40));
			g.drawString("CLICK TO RESTART", 0, 100);
		}

		if(paused){
			g.setFont(new Font("Aerial",Font.BOLD,100));
			g.drawString("PAUSED", (int)(screenW*.1), screenH/2);
		}


	}
	void drawBackDrop(Graphics g){
		g.drawImage(Texture.gridPaperBack, 0, 0, screenW, screenH,null);
	}
	void drawScore(Graphics g){;
	g.setFont(new Font("Aerial",Font.BOLD,40));
	g.setColor(Color.BLACK);
	g.drawString("Score : " + score, 0, 50);
	}
	void drawDoodle(Graphics g){
		doodle.draw(g);
	}
	void drawSprites(Graphics g){
		synchronized(sprites){
			for(int index = 0; index < sprites.size(); index++){
				GameObject o = sprites.get(index);
				if(o.equals(GameType.DOODLER)){
					continue;
				}
				o.draw(g);
			}
		}
		synchronized(doodle.bullets){
			for(int index = 0; index < doodle.bullets.size(); index++){
				Projectile p = doodle.bullets.get(index);
				p.draw(g);
			}
		}
	}
}
