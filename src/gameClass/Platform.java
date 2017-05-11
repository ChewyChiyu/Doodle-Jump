package gameClass;

import java.awt.Graphics;

import sprites.Texture;

public class Platform extends GameObject{
	protected PlatformType p;
	protected Platform(int x, int y, PlatformType p) {
		super(x, y, GameType.PLATFORM);
		this.p = p;
	}
	PlatformType getP(){
		return p;
	}
	@Override
	void draw(Graphics g) {
		switch(p){
		case BLUE:
			g.drawImage(Texture.platformBlue, x, y,190,45, null);
			break;
		case GREEN:
			g.drawImage(Texture.platformGreen, x, y,190,45, null);
			break;
		case RED:
			g.drawImage(Texture.platformRed, x, y,190,45, null);
			break;
		case WHITE:
			g.drawImage(Texture.platformWhite, x, y,190,45, null);
			break;
		default:
			break;
		
		}
	}

}
