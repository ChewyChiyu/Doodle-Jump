package gameClass;

import java.awt.Graphics;

import sprites.Texture;

public class Platform extends GameObject{
	protected PlatformType p;
	protected static int platW = 190;
	protected static int platH = 45;
	protected Platform(int x, int y, PlatformType p) {
		super(x, y, GameType.PLATFORM);
		this.p = p;
		if(p.equals(PlatformType.BLUE)){
			xVelo = -3;
		}
	}
	PlatformType getP(){
		return p;
	}
	@Override
	void draw(Graphics g) {
		switch(p){
		case BLUE:
			g.drawImage(Texture.platformBlue, x, y,platW,platH, null);
			break;
		case GREEN:
			g.drawImage(Texture.platformGreen, x, y,platW,platH, null);
			break;
		case RED:
			if(!steppedOn)
			g.drawImage(Texture.platformRed, x, y,platW,platH, null);
			else
				g.drawImage(Texture.platformRedBroken, x, y,platW,platH, null);
			break;
		case WHITE:
			g.drawImage(Texture.platformWhite, x, y,platW,platH, null);
			break;
		default:
			break;
		
		}
	}

}
