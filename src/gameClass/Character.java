package gameClass;

public abstract class Character extends GameObject{
	protected volatile boolean isJumping = false;
	protected volatile boolean canJump = true;
	protected Character(int x, int y, GameType t) {
		super(x, y, t);
		characterLoops();
	}
	void characterLoops(){
		Thread canJumps = new Thread(new Runnable(){
			public void run(){
				while(Screen.isRunning){
					if(!canJump){
						canJump = true;
					}
					try{
						Thread.sleep(600);
					}catch(Exception e) { }
				}
			}
		});
		canJumps.start();
		
		
	}
	abstract void jump();
}
