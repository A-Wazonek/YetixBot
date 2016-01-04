import org.jibble.pircbot.*;
import java.util.Timer;
import java.util.TimerTask;
import java.io.PrintWriter;
import java.io.FileWriter;

public class YetixBotMain {
    
	/*
	Main class that sets up the bot. 
	Wont launch as the password is removed. 
	*/
    public static void main(String[] args) throws Exception {
        
        // Now start our bot up.
        YetixBot bot = new YetixBot();
        int val = 0;
        // Enable debugging output.
        bot.setVerbose(true);
        
        // Connect to the IRC server.
        bot.connect("irc.twitch.tv",6667,/*This is where the password goes. Removed for security purposes*/);
	
        // Join the #pircbot channel.
        bot.joinChannel("#redxyeti");
		bot.sendRawLine("CAP REQ :twitch.tv/membership");
		
		while(true)
		{

		try{Thread.sleep(30000); 
		val++;
		bot.ping(val);
		if(val >=120) val = 0;}
		catch(InterruptedException e){};
		
		}
        
    }
    
}