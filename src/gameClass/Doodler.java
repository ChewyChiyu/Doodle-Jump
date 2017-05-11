package gameClass;

import java.awt.Graphics;

import sprites.Texture;

public class Doodler extends Character{
	protected Doodler(int x, int y) {
		super(x, y, GameType.DOODLER);
	}

	@Override
	void draw(Graphics g) {
		if(xVelo>0){
		g.drawImage(Texture.doodlerR, x , y , 100 , 100, null);
		}
		else{
			g.drawImage(Texture.doodlerL, x , y , 100 , 100, null);
		}
	}
	synchronized void jump(){
		if(!isJumping){
			isJumping = true;
			canJump = false;
			Thread jumps = new Thread(new Runnable(){
				public void run(){
					for(int index = 0; index < 55; index++){
						yVelo = -5;
						try{
							Thread.sleep(5);
						}catch(Exception e) { }
					}
					isJumping = false;
				}
			});
			jumps.start();
			
		}
	}
}
