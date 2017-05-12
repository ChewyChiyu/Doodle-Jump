package gameClass;

import java.awt.Graphics;

import sprites.Texture;

public class Projectile extends GameObject{
	protected static int projW = 25;
	protected static int projH = 25;
	protected Projectile(int x, int y) {
		super(x, y, GameType.BULLET);
		yVelo = -6;
	}

	@Override
	void draw(Graphics g) {
		g.drawImage(Texture.bullet,x,y,projW,projH,null);
	}

}
