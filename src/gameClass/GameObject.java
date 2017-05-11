package gameClass;

import java.awt.Graphics;

public abstract class GameObject {
	protected int x;
	protected int y;
	protected int xVelo;
	protected int yVelo;
	private GameType t;

	protected GameObject(int x, int y, GameType t){
		this.x = x;
		this.y = y;
		this.t = t;
	}

	int getX() {
		return x;
	}

	void setX(int x) {
		this.x += x;
	}

	int getY() {
		return y;
	}

	void setY(int y) {
		this.y += y;
	}

	int getxVelo() {
		return xVelo;
	}

	void setxVelo(int xVelo) {
		this.xVelo = xVelo;
	}

	int getyVelo() {
		return yVelo;
	}

	void setyVelo(int yVelo) {
		this.yVelo = yVelo;
	}

	GameType getT() {
		return t;
	}

	void setT(GameType t) {
		this.t = t;
	}
	
	
	abstract void draw(Graphics g);
	
	
}
