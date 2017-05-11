package gameClass;

import java.awt.Graphics;

import sprites.Texture;

public class Tramp extends GameObject{
	protected static int trampW = 300;
	protected static int trampH = 50;
	protected Tramp(int x, int y) {
		super(x, y, GameType.TRAMP);
	}

	@Override
	void draw(Graphics g) {
			g.drawImage(Texture.tramp,x,y, trampW, trampH, null);
	}

}
