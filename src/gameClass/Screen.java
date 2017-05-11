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
	protected double numberOfPlatforms = 0;
	private int GRAVITY = 7;
	private int score = 0;
	protected static int screenW = 600;
	protected static int screenH = 900;
	protected static ArrayList<GameObject> sprites = new ArrayList<GameObject>();
	protected Doodler doodle = new Doodler(screenW/2,(int)(screenH*.1));
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
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
		});
	}
	void initialPlatforms(){
		int xBuffer = 0;
		int yBuffer = 0;
		for(int index = 0 ; index < 12; index++){
			sprites.add(new Platform(xBuffer,yBuffer,PlatformType.GREEN));
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
				doodle.jump();
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
							if(o.getT().equals(GameType.PLATFORM)){
								if(o.getY()>=screenH){
									sprites.remove(o);
									numberOfPlatforms--;
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
		Thread checkForAction = new Thread(new Runnable(){
			public void run(){
				while(isRunning){
					synchronized(sprites){
						if(doodle.canJump&&doodle.getyVelo()>0){
						for(int index = 0; index < sprites.size(); index++){
							GameObject o = sprites.get(index);
							if(o.getT().equals(GameType.PLATFORM)){
								int platX1 = o.getX();
								int platX2 = o.getX() + Platform.platW;
								int platY1 = o.getY();
								int platY2 = o.getY() + Platform.platH;
								if(doodle.getX()+Doodler.doodleW/2>platX1&&doodle.getX()+Doodler.doodleW/2<platX2&&doodle.getY()+Doodler.doodleH>platY1&&doodle.getY()+Doodler.doodleH<platY2){
									doodle.jump();
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
						GRAVITY = 13;
					}else{
						GRAVITY = 7;
					}
					try{
						Thread.sleep(1);
					}catch(Exception e) { } 
				}
			}
		});
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
			sprites.add(new Platform(xBuffer,yBuffer,PlatformType.GREEN));
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
				if(o.getT().equals(GameType.PLATFORM)){
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
	}
}
