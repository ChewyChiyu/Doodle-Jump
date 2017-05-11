package gameClass;

import java.awt.Graphics;

import sprites.Texture;

public class Spring extends GameObject{
	protected static int springW = 50;
	protected static int springH = 50;
	protected Spring(int x, int y) {
		super(x, y, GameType.SPRING);
	}

	@Override
	void draw(Graphics g) {
		if(!steppedOn)
			g.drawImage(Texture.springUp,x,y, springW, springH, null);
		else
			g.drawImage(Texture.springDown,x,y+springH/2, springW, springH/2, null);

	}

}
