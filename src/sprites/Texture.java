package sprites;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Texture {
	public static BufferedImage doodlerR;
	public static BufferedImage doodlerL;
	public static BufferedImage doodleS;
	public static BufferedImage bullet;
	public static BufferedImage platformRed;
	public static BufferedImage platformRedBroken;
	public static BufferedImage platformBlue;
	public static BufferedImage platformGreen;
	public static BufferedImage platformWhite;
	public static BufferedImage gridPaperBack;
	public static BufferedImage springUp;
	public static BufferedImage springDown;
	public static BufferedImage tramp;
	public static BufferedImage greenMonster;
	
	public Texture(){
		load();
	}
	void load(){
		try{
			BufferedImage doodleSheet1 = ImageIO.read(getClass().getResource("/sprites/DoodleJumpSpriteSheet1.png"));
			BufferedImage doodleSheet2 = ImageIO.read(getClass().getResource("/sprites/DoodleJumpSheet2.png"));
			doodlerR = doodleSheet1.getSubimage(30, 122, 108-30, 202-122);
			doodlerL = doodleSheet1.getSubimage(0, 201, 80, 80);
			
			int xBuffer = 0;
			int yBuffer = 0;
			
			platformGreen = doodleSheet1.getSubimage(xBuffer, yBuffer, 103, 30);
			yBuffer += 30;
			platformRed = doodleSheet1.getSubimage(xBuffer, yBuffer, 103, 30);
			yBuffer += 30;
			platformBlue = doodleSheet1.getSubimage(xBuffer, yBuffer, 103, 30);
			yBuffer += 30;
			platformWhite = doodleSheet1.getSubimage(xBuffer, yBuffer, 103, 30);
			
			platformRedBroken = doodleSheet1.getSubimage(0, 553, 109, 610-553);
			
			springUp = doodleSheet1.getSubimage(0, 500, 38, 554-500);
			springDown = doodleSheet1.getSubimage(0, 469, 44, 497-469);
			
			tramp = ImageIO.read(getClass().getResource("/sprites/Tramp.png"));
			tramp = tramp.getSubimage(0, 0, 200, 28);
			gridPaperBack = ImageIO.read(getClass().getResource("/sprites/GridPaperBack.jpg"));
			doodleS = doodleSheet2.getSubimage(107, 7, 145-107, 66-7);
			
			bullet = ImageIO.read(getClass().getResource("/sprites/Bullet.png"));
			
			greenMonster = ImageIO.read(getClass().getResource("/sprites/GreenMonster.png"));
			
		}catch(Exception e) { e.printStackTrace(); }
	}
}
