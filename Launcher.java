package linkgame;

public class Launcher {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DisplayWindow win = new DisplayWindow();
		win.addGame(new MainGame());
		win.createWindow();
	}

}
