package Server;

public class MasterServer extends Thread{
	DB db;
	
	public MasterServer(){
		initialize();
	}
	public static void main(String[] args) {
        new MasterServer().start();
    }
	
	void initialize(){
		db = new DB();
	}
}
