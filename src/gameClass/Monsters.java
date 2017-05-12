package gameClass;

import java.awt.Graphics;

import sprites.Texture;

public class Monsters extends GameObject{
	protected static int monsterW = 300;
	protected static int monsterH = 200;
	protected MonsterType m;
	protected Monsters(int x, int y,MonsterType m) {
		super(x, y, GameType.MONSTERS);
		this.m = m;
		xVelo = -1;
	}

	@Override
	void draw(Graphics g) {
		
		g.drawImage(Texture.greenMonster, x, y,monsterW,monsterH, null);
	}

}
