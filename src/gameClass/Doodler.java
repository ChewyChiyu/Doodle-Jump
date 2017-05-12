package gameClass;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import sprites.Texture;

public class Doodler extends Character{
	protected static int doodleW = 100;
	protected static int doodleH = 100;
	protected boolean isSuperJumping = false;
	protected boolean isFlipJumping = false;
	protected boolean isShooting = false;
	protected double angle = 0;
	protected ArrayList<Projectile> bullets = new ArrayList<Projectile>();
	protected Doodler(int x, int y) {
		super(x, y, GameType.DOODLER);
	}

	@Override
	void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g ;
		AffineTransform oldXForm = g2d.getTransform();
		if(!isShooting){
		if(angle!=0){
			g2d.translate(x+doodleW/2, y+doodleH/2); 
			if(xVelo<0){
			g2d.rotate(angle);
			}else{
				g2d.rotate(-angle);
			}
			if(xVelo>0){
				g.drawImage(Texture.doodlerR, -doodleW/2 , -doodleH/2 , doodleW , doodleH, null);
				}
				else{
					g.drawImage(Texture.doodlerL,  -doodleW/2 ,-doodleH/2 , doodleW , doodleH, null);
				}	
		}else{
			g2d.translate(0, 0);
			if(xVelo>0){
				g.drawImage(Texture.doodlerR, x , y , doodleW , doodleH, null);
				}
				else{
					g.drawImage(Texture.doodlerL, x , y , doodleW , doodleH, null);
				}	
		}
		}else{
			g.drawImage(Texture.doodleS, x , y , doodleW , doodleH, null);
		}
		g2d.setTransform(oldXForm); 
		
	}
	synchronized void jump(){
		if(!isJumping){
			isJumping = true;
			canJump = false;
			Thread jumps = new Thread(new Runnable(){
				public void run(){
					for(int index = 0; index < 65; index++){
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
	synchronized void superJump(){
		if(!isJumping){
			isJumping = true;
			isSuperJumping = true;
			canJump = false;
			Thread sjumps = new Thread(new Runnable(){
				public void run(){
					for(int index = 0; index < 65; index++){
						yVelo = -10;
						try{
							Thread.sleep(7);
						}catch(Exception e) { }
					}
					isJumping = false;
					isSuperJumping = false;
				}
			});
			sjumps.start();
			
		}
	}
	synchronized void flipJump(){
		if(!isJumping){
			isJumping = true;
			isFlipJumping = true;
			canJump = false;
			Thread fjumps = new Thread(new Runnable(){
				public void run(){
					for(int index = 0; index < 65; index++){
						yVelo = -10;
						try{
							Thread.sleep(7);
						}catch(Exception e) { }
					}
					isJumping = false;
					isFlipJumping = false;
				}
			});
			Thread rotate = new Thread(new Runnable(){
				public void run(){
					for(int index = 0; index < 720; index++){
						angle += (Math.PI/360);
						try{
							Thread.sleep(1);
						}catch(Exception e) { } 
						
					}
					angle = 0;
				}
			});
			rotate.start();
			fjumps.start();
			
		}
	}
	void shoot(){
		bullets.add(new Projectile(x+Doodler.doodleW/2,y));
	}
	
}
