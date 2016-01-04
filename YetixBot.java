import org.jibble.pircbot.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.net.*;
import java.io.*;
import java.util.regex.Pattern;
import java.util.ArrayList;
/*
Code uses pircbot API. 
code below programmed by Alex Wazonek
Note that this code will not function as it does not have a proper password to log into the server with.
This password was removed for security reasons but can be replaced with any account. 
*/

public class YetixBot extends PircBot {

	String runInfo = "Yeti is playing video games";
	String buildInfo = "Tell a mod to update this";
	ArrayList<User> users = new ArrayList<User>();
	ArrayList<Command> commands = new ArrayList<Command>();
	ArrayList<String> requests = new ArrayList<String>();
	ArrayList<String> quotes = new ArrayList<String>();
	ArrayList<String> raffle = new ArrayList<String>();
	int raffleInt = 0;
	String slString = "1";
	String[] customC;String poster;String newBuild;String newRun;String giver;
	User temp2;
	String[] bux; String[] fights;
	Random rng = new Random();
	String[] stats = new String[9];	int[] numStat = new int[9];
	String time; String result; String defender; String fighter; String slapee;
	String tempS;
	int startStat = 0;
	int loopSize, res, stat, amount, exists, worldClock, fightClock, lottors, winner, winnerCount, firstPlace, lottoValue;
	FileWriter writer;
	BufferedWriter bw;
	int[] leaders = new int[5];
	String[] lead = new String[5];
	String botS = ""; String mes;
	int bottom = 0; int temp = 0;
	boolean run, roll, fashion;
	
	FileWriter fashionWriter;
	/*
	Constructor, reads info from multiple files:
	points.txt - used to retreive points of users connecting to the chat
	mods.txt - used to determine who is a moderator
	commands.txt - used to retreive custom commands created by moderators
	quotes.txt - used to retreive quotes that are added by the moderators 
	
	*/
	public YetixBot() {
		
		this.setName("YetixBot");

		for(int i = 0; i < 5; i++)
			lead[i] = "";
		try{
			BufferedReader br = new BufferedReader(new FileReader("mods.txt"));
			String line;
			while ((line = br.readLine()) != null) 
				{User mod = new User(line); mod.setMod(true); users.add(mod);}
			br.close();
		}
		catch(Exception e){System.out.print("Mods File not found");}
		lottoValue = 0;
		lottors = 0;
		run = false;
		firstPlace = 0; amount = 0;
		fashion = false;
		try{
			BufferedReader br = new BufferedReader(new FileReader("points.txt"));
			
			String line; String[] parts;
			int found = 0; boolean canPost = false;
			while ((line = br.readLine()) != null) 
			{
				parts = line.split(" ", 2);
				if(parts[1].contains(" "))
				{canPost = true; parts[1] = parts[1].replace(" P", "");}
				amount = Integer.parseInt(parts[1]);
				if(amount > firstPlace) firstPlace = amount;
				for(User s : users)
				{
					if(s.getName().equalsIgnoreCase(parts[0])) 
					{
					s.setPoints(amount); s.letLink(canPost); found =1; break;
					
					}
				}
				if(found == 0 && !parts[0].equalsIgnoreCase("yetixbot")){
				User person = new User(parts[0]);
				person.setPoints(Integer.parseInt(parts[1]));
				person.letLink(canPost);
				users.add(person);
				canPost = false;}
				else found = 0;
			}
			br.close();	
		}
		
		catch(Exception e){System.out.print("Points File not found");}
		
		try{
		
		BufferedReader br = new BufferedReader(new FileReader("commands.txt"));
			
			String line; String[] parts;
			while ((line = br.readLine()) != null) 
			{
				parts = line.split(" ", 2);
				Command com = new Command(parts[0],parts[1]);
				commands.add(com);

			}
			br.close();	

		
		}
		catch(Exception e){System.out.print("Commands File not found");}
				try{
		
		BufferedReader br2 = new BufferedReader(new FileReader("quotes.txt"));
			
			String line; String[] parts;
			while ((line = br2.readLine()) != null) 
			{

				quotes.add(line);

			}
			br2.close();	

		
		}
		catch(Exception e){System.out.print("Quotes File not found");}
		stats[0] = "VGR";stats[1] = "END";stats[2] = "VIT";stats[3] = "ATN";stats[4] = "STR";stats[5] = "DEX";stats[6] = "ADP";stats[7] = "INT";stats[8] = "FTH";
		
	}
    
	
	/*
		Every 30 seconds, bot gets pinged
		Pinging updates the point list and writes it to a file		
		File is a basic text file
	*/
	public void ping(int val)
	{
	worldClock = val;
	if((val % 60) == 0) run = true;
	else if(val == 90) {sendMessage("#redxyeti", "If you're enjoying the stream and want to support it, please consider following it!"); run = false;}
	else run = false;
	if(fightClock > 0 ) {fightClock--;}
	try{
		writer = new FileWriter("quotes.txt");
		bw = new BufferedWriter(writer);
		for(String s : quotes)
			{
				bw.write(s);
				bw.newLine();
			}
	bw.close();
	
		}
		catch(Exception e){};
	try{
		writer = new FileWriter("points.txt");
		bw = new BufferedWriter(writer);
		for(User s : users){
			if(s.inChat() && run){
			s.incPoints(5);}
			if(s.timeInChat() < 20) s.addChatTime(1);
			if(s.timeInChat() >= 20) s.letLink(true);
			if(s.rollClock()> 0) s.decRollClock();
			bw.write(s.getName() + " " + s.getPoints());
			if(s.canLink()) bw.write(" P");
			if(s.getPoints() > firstPlace) firstPlace = s.getPoints();
			bw.newLine();
		}
		bw.close();
		if(run) sendMessage("#redxyeti","Everyone gets 5 YetiBux!");
		} catch(Exception e) {};

	}
	
	//updates all the bux by 1, used by tithe
	public void ping()
	{

	try{
		writer = new FileWriter("points.txt");
		bw = new BufferedWriter(writer);
		for(User s : users){
			if(s.inChat()){
			s.incPoints(3);}
			bw.write(s.getName() + " " + s.getPoints());
			bw.newLine();
		}
		bw.close();
		} catch(Exception e) {};
	}
	
	//Waits 5 seconds. Used to prevent spamming
	public void waitS()
	{
	try{Thread.sleep(3000); }catch(InterruptedException e){System.out.print("Error Waiting Somehow");};
	}
	
	/*
	Activates whenever a message comes in.
	Determines if the message is a bot command.
	Also prevents people from posting websites unless they are a verified poster. 
	*/
    public void onMessage(String channel, String sender,
                       String login, String hostname, String message) {
		
		//Allows the creation of custom commmands
		//Format: !custom <command> <custom command>
		if(message.startsWith("!custom"))
		{
			for(User s : users)
			{
				if(s.getName().equalsIgnoreCase(sender) && s.isMod())
				{
					giver = message.replace("!custom ","");
					bux = giver.split(" ", 2);
					if(!bux[0].startsWith("!"))
					{
					sendMessage(channel, "Command must start with a !");
					waitS();
					return;
					}
					Command c = new Command(bux[0],bux[1]);
					commands.add(c);
						try{
						writer = new FileWriter("commands.txt");
						bw = new BufferedWriter(writer);
						for(Command d : commands){
							bw.write(d.intake() + " " + d.output());
							bw.newLine();
						}
						bw.close();
						} catch(Exception e) {};
					sendMessage(channel, "Command: " + bux[0] + " added");
					waitS();
					return;
				}	
			}
		}
		//URL Banner, bans most links but not all urls. 
		//Could be an array, but it works for now. 
		//User gets 2 chances before it times them out. 
		if (message.contains(".com") || message.contains(".net") || message.contains(".gl") || message.contains(".ca") || message.contains(".ru") || message.contains(".uk") || message.contains("http") || message.contains("www.") || message.contains(".tv") || message.contains("xxx") || message.contains(".org") || message.contains(".be")){
			
			for(User s : users) if(s.getName().equalsIgnoreCase(sender)) {
			if(s.isMod()) return;
			else if(s.canPost()) {s.setPost(false); return;}
			else if(s.canLink()) return;
			
			if(s.getPosts() >4)
				sendMessage(channel, "/timeout " + sender + " 600");
			else if (s.getPosts() == 3) 
				sendMessage(channel, "/timeout " + sender + " 60");
			else if (s.getPosts() == 2) {
				sendMessage(channel, sender + " this is your last warning, please ask permission to post links!");
				sendMessage(channel, "/timeout " + sender + " 1");
				}
			else{
				sendMessage(channel, sender + " please ask for permission before posting a link!");
				sendMessage(channel, "/timeout " + sender + " 1");} 
			}
		}
		
		//Check if the message doesnt start with a !, if it doesnt then just ignore it to prevent wasting bot resources
		else if(!message.startsWith("!")) return;
		
		//Joke command 
        else if(message.equalsIgnoreCase("!recon"))
		{
			sendMessage(channel, "Ewwww, " + sender + ". DansGame");
			waitS();
			return;
			
		}
		
		//Tells the time (built in command, kind of unnecessary but whatever)
		else if (message.equalsIgnoreCase("!time")) {
            time = new java.util.Date().toString();
            sendMessage(channel, "/me The time is now " + time);
        }
		
		//Used to view the current prize pool of the lottery. 
		else if (message.equalsIgnoreCase("!lotto"))
		{
			sendMessage(channel, "Current prize pool: " + lottoValue + ". To enter, type !enter. It costs 5 YetiBux to enter");
		}
		
		//If redxyeti (chat owner) wants he can add bux to the lotto. 
		else if(message.startsWith("!addlotto"))
		{
			if(sender.equalsIgnoreCase("redxyeti"))
			{
					giver = message.replace("!addlotto ","");
					try{
					winner = Integer.parseInt(giver);
					lottoValue += winner;}
					catch(Exception e){};
			}
		}
		
		//Ends the lottery, picks a random winner and gives them bux
		else if (message.equalsIgnoreCase("!endlotto"))
		{
			for(User s : users)
			{
				if(s.getName().equalsIgnoreCase(sender))
				{
					if(s.isMod())
					{
						for(int i = 0; i <= lottors; i++)
						{
							winner += rng.nextInt(3) - 1;
						}
						if(winner < 0 || winner > lottors) winner = rng.nextInt(lottors);
						winnerCount = 0;
						for(User d: users)
						{
							if(d.getlotto())
							{
								d.setlotto(false);
								if(winnerCount == winner)
								{
									sendMessage(channel, "Congrats " + d.getName() + ", you have won the prize of " + lottoValue + " YetiBux! ");
									d.incPoints(lottoValue);
									lottoValue = 0;
									lottors = 0;
									winner = -1;
								}
									winnerCount++;
							
							}
						}
					
					
					}
					else
					{
						sendMessage(channel, "Only mods can end the lotto!");
						waitS();
						return;
					}	
				}
			}
		}
		
		//Picks a random user who is currently in chat. Used for giveaways. 
		else if (message.equalsIgnoreCase("!giveaway"))
		{
			if(sender.equalsIgnoreCase("redxyeti") || sender.equalsIgnoreCase("radiog00se"))
			{
				winnerCount = 0;
				for(User s : users)
				{
					if(s.inChat())
						winnerCount++;
				}
				for(User s : users)
				{
							
					if(s.inChat())
						if(rng.nextInt(winnerCount) == (winnerCount/2))
							{
							sendMessage(channel, s.getName() + " is the winner!");
							waitS();
							return;
							}
					
				}
				exists= 0;
				try{
				exists = rng.nextInt(Math.abs(winnerCount));
				} catch (Exception e) {sendMessage(channel, "According to twitch, no one is in chat");};
				winnerCount = 0;
				for(User s : users)
				{
					if(winnerCount == exists && s.inChat())
						{
						sendMessage(channel, s.getName() + " is the winner!");
						waitS();
						return;
						}
					winnerCount++;
					
				}
			}
		}
		
		//Used to enter the lottery. Once a user has entered they are in it until it is ended and cannot enter twice. 
		else if (message.equalsIgnoreCase("!enter"))
		{
		for(User s : users)
		{
			if(s.getName().equalsIgnoreCase(sender))
			{
				if(s.getPoints() < 5) {sendMessage(channel, sender + ", you need 5 yetibux to enter!"); waitS(); return;}
				else if(s.getlotto())
				{
					sendMessage(channel, sender + " has already entered the lotto!");
					waitS();
					return;
				}
				else
					{
					s.decPoints(5);
					s.setlotto(true);
					lottoValue = lottoValue + 5; 
					lottors++;
					sendMessage(channel, sender + " has been entered in the lotto! The prize pool is now " + lottoValue + " YetiBux!");
					waitS();
					return;
					}
			}
		
		}
		sendMessage(channel, sender + " you must register to enter the lotto, please type !register");
		}
		
		//Allow people to give me bux
		else if(message.equalsIgnoreCase("!tithe")){
			if(sender.equalsIgnoreCase("RadioG00se"))
			{
				ping();
				sendMessage(channel, "Your king has given everyone 3 YetiBux!");
				waitS();
				return;
			}
			else
			for(User s : users)
			{
				if(s.getName().equalsIgnoreCase(sender))
				{
					if(s.getPoints() == 0) {sendMessage(channel, sender + " has no YetiBux!"); waitS(); return;}
					s.decPoints();
				}
				else if(s.getName().equalsIgnoreCase("RadioG00se"))
				{
					s.incPoints(); 
				}
			}
			sendMessage(channel, sender + " paid a pittance of 1 YetiBux to their king!");
		}
		
		//Used by mods to add quotes to the list of important quotes
		else if(message.startsWith("!addquote"))
		{
			for(User s : users)
			{
				if(s.getName().equalsIgnoreCase(sender) && s.isMod())
				{
				giver = message.replace("!addquote ","");
				quotes.add(giver);
				sendMessage(channel, "quote: \"" + giver + "\" has been added");
				waitS();
				return;
				}
			}
			
		}
		
		//Picks a random quote from the quote.txt file. 
		else if(message.equalsIgnoreCase("!quote"))
		{
			winner = rng.nextInt(quotes.size());
			winnerCount = 0;
			for(String q : quotes)
			{
				if(winner == winnerCount)
				{
					sendMessage(channel, q);
					waitS();
					return;
				}
				else
					winnerCount++;
			}
		}
		
		
		
		//Removes a custom command
		//Format: !remove <command>
		else if(message.startsWith("!remove"))
		{

			for(User s : users)
			{
				if(s.getName().equalsIgnoreCase(sender) && s.isMod())
				{
					giver = message.replace("!remove ","");
					if(giver.contains(" "))
					{
						sendMessage(channel, "Format: !remove <command>");
						waitS();
						return;
					}
					for(Command c : commands)
					{
						if(c.intake().equalsIgnoreCase(giver))
						{
							sendMessage(channel, "Command: " + c.intake() + " has been removed!");
							try{
								writer = new FileWriter("commands.txt");
								bw = new BufferedWriter(writer);
								for(Command d : commands){
									bw.write(d.intake() + " " + d.output());
									bw.newLine();
								}
								bw.close();
							} 
							catch(Exception e) {};
							commands.remove(c);
							waitS();
							return;
							
						}
					}
				}
			}
			sendMessage(channel, "Could not find command " + sender);
		}
		
		//used by yeti for sending yetibux to people
		//Formatted: !send <username> <amount>
		else if(message.startsWith("!send")){
			if(sender.equalsIgnoreCase("redxyeti") || sender.equalsIgnoreCase("RadioG00se"))
			{
				giver = message.replace("!send ", "");
				bux = giver.split(" ");
				if(bux[1].contains(" ") || bux[1].matches("\\D"))
				{
					sendMessage(channel, "Formatted: !send <username> <amount>");
					waitS();
					return;
				}
				else{
				amount = Integer.parseInt(bux[1]);
				for(User s : users)
				{
					if(s.getName().equalsIgnoreCase(bux[0]))
					{
						s.incPoints(amount);
						sendMessage(channel, "RedxYeti gave " + bux[0] + " " + amount + " YetiBux!");
						waitS();
						return;
					}
				}
			}}
		}
		
		//Checks the user's balance
		//Formatted: !check <username>
		else if(message.startsWith("!check")){
			giver = message.replace("!check ", "");

					for(User d : users)
					{
						if(d.getName().equalsIgnoreCase(giver))
						{
							sendMessage(channel, d.getName() + " has " + d.getPoints() + " YetiBux!");
							waitS();
							return;
						}
					}

			sendMessage(channel, "Could not find " + giver + ", if they exist have them type !register");
		}
		
		//Enables !suggestFashion
		else if(message.equalsIgnoreCase("!openrequest"))
		{
			if(sender.equalsIgnoreCase("redxyeti"))
			{
			fashion = true;
			sendMessage(channel, "Requests are active!");
			waitS();
			return;
			}
			
		}
		else if(message.equalsIgnoreCase("!closerequest"))
		{
			if(sender.equalsIgnoreCase("redxyeti"))
			{
			fashion = false;
			sendMessage(channel, "Requests are not active!");
			waitS();
			return;
			}
		}
		
		//Used to let people request an outfit
		else if(message.startsWith("!suggestfashion"))
		{	if(fashion == true)
			{
			giver = message.replace("!request ","");
			try{
					fashionWriter = new FileWriter("fashion.txt", true);
					bw = new BufferedWriter(fashionWriter);
					bw.write(sender + ": " + giver);
					bw.newLine();
					bw.close();
							} 
							catch(Exception e) {};
			sendMessage(channel, "Suggestion submitted " + sender);
			waitS();
			return;}
			else {sendMessage(channel, "Suggestions are disabled!");}
		}
		
		//Give another user yetibux
		//Can be used by anyone, unlike !send this decreases your balance
		//Formatted: !give <username> <amount>
		else if(message.startsWith("!give")){
				giver = message.replace("!give ","");
				exists = 0;
				if(giver.toLowerCase().contains("yetixbot") || giver.toLowerCase().contains("yetibot"))
					{sendMessage(channel, "YetixBot has no need for money!"); waitS(); return;}
				
				bux = giver.split(" ");
				if(bux[1].contains(" ") || bux[1].contains("!give") || bux[1].matches("[^0-9]*"))
					{sendMessage(channel, "Formatted: !give <username> <amount>"); waitS(); return;}
					try{
				amount = Integer.parseInt(bux[1]);
				}
				catch(Exception e){sendMessage(channel, "<amount> is invalid!"); waitS();return;}
				if(amount <= 0)
				{
				sendMessage(channel, "Please use a number > 0!");
				waitS();
				return;
				}
				for(User s : users)
				{
					if(s.getName().equalsIgnoreCase(sender))
					{
						if(s.getPoints() < amount)
						{
							sendMessage(channel, sender + " does not have enough YetiBux!");
							waitS();
							return;
						}
						else
						{
							for(User d : users)
							{
								if(d.getName().equalsIgnoreCase(bux[0]))
								{
									if(d.getPoints() >= firstPlace)
									{
										sendMessage(channel, bux[0] + " is in first place! You cannot give bux to whoever is in first.");
										waitS();
										return;
									}
									else
									{
									s.decPoints(amount);
									d.incPoints(amount); 
									sendMessage(channel, sender + " sent " + amount + " YetiBux to " + bux[0]);
									waitS();
									return;
									}
								}
							}

						}
					}
				}
				
				
				
				
		}
		
		//Check your own balance
		else if(message.equalsIgnoreCase("!yetibux")){
			for(User s : users)
			{
				if(s.getName().equalsIgnoreCase(sender))
					{s.setChat(true);sendMessage(channel, sender + " has " + s.getPoints() + " YetiBux");waitS(); return;}
			}
			sendMessage(channel, "You have not registered " + sender + ", Type !register to register");
		}

		//If I join the server
		else if (message.equalsIgnoreCase("!guuz")) {
            sendMessage(channel, "RadioG00se in the house!");
        }
		
		//Semi obsolete, adds a user into the list of users if they aren't already
		//Due to twitch chat delay, sometimes users wont be auto registered
		else if(message.equalsIgnoreCase("!register"))
			{
			for(User s : users)
			{
				if(s.getName().equalsIgnoreCase(sender)) {sendMessage(channel, "You've already registered, " + sender); waitS();return;}
			}
			
			User temp = new User(sender);
			temp.setPoints(5);
			users.add(temp);
			try{
			writer = new FileWriter("points.txt");
			bw = new BufferedWriter(writer);
			for(User s : users){
				bw.write(s.getName() + " " + s.getPoints());
				bw.newLine();
			}
			bw.close();
			} catch(Exception e) {};
			return; //returns so no delay when registering
		}
		
		//Used by yeti, creates a new darksouls 2 build
		else if (message.equalsIgnoreCase("!reroll")) {
		
			if(sender.equals("redxyeti") || sender.equalsIgnoreCase("RadioG00se"))
			{
			result = "Choose: ";
			numStat[0] = 12; numStat[1]= 6;numStat[2] = 7; numStat[3] = 4;numStat[4] = 11;numStat[5] = 8;numStat[6] = 9;numStat[7] = 3;numStat[8] = 6;

			loopSize = 152;
			for(int i = 1; i<= loopSize;)
			{
			startStat = rng.nextInt(20);
			stat = rng.nextInt(9);
			if((loopSize - startStat) <= 0)
			{
			numStat[stat] += loopSize;
			loopSize = 0;
			}
			else 
			{loopSize = loopSize - startStat;
			numStat[stat] += startStat;}
			}
			result = "Results: ";
			for(int j = 0; j < 9; j++)
			{
				result = result + stats[j] + " = " + numStat[j] + " | ";
			}
			sendMessage(channel,  result);
			}
			else
			sendMessage(channel, "Only yeti can access this command");
        }
		
		//Lets you roll 1x every 10 minutes for a random amount of bux
		else if(message.equalsIgnoreCase("!roll"))
		{
			for(User s : users)
			{	
				if(s.getName().equalsIgnoreCase(sender))
				{
					if(s.rollClock() > 0)
					{
						sendMessage(channel, sender + " has already rolled! You can reroll in " + (s.rollClock()/2) + " minutes.");
						waitS();
						return;
					}
					else
					{
						amount = rng.nextInt(5);
						while(rng.nextInt(2) != 0)
						{
							amount += rng.nextInt(2) + 1;
						}
						if(sender.equalsIgnoreCase("RadioG00se")) amount += 2;
						s.incPoints(amount);
						sendMessage(channel, sender + " has won " + amount + " YetiBux!");
						s.setRollClock(30);
						waitS();
						return;
					}
				}
			}
		
		}
		
		//Prints out the top 5 balances
		else if(message.equalsIgnoreCase("!topbux")){
			bottom = 0; tempS = ""; botS = ""; mes = "Top Balances: ";
			for(int i = 0; i < 5; i++) leaders[i] = 0;
			for(User s : users)
			{
				if(s.getPoints() > bottom)
				{
					bottom = s.getPoints();
					botS = s.getName();
					for(int i = 0; i < 5; i++)
					{
						if(leaders[i] < bottom)
						{
							tempS = lead[i];
							lead[i] = botS;
							botS = tempS;
							temp = leaders[i];
							leaders[i] = bottom;
							bottom = temp;
							
						}
					}
				}
			}
			for(int i = 0; i< 5; i++)
			{
			if(i == 0) firstPlace = leaders[i];
			mes = mes + (i+1) + "." + lead[i] + " : " + leaders[i] + ", ";
			}
			sendMessage(channel, mes);
			
		}
		
		//nice meme
		else if(message.equalsIgnoreCase("!nicememe") || message.equalsIgnoreCase("ayy lmao")){
			sendMessage(channel, "niceme.me");
		}
		
		//photo of something yeti wanted in
		else if (message.equalsIgnoreCase("!oj")){
		sendMessage(channel, "https://i.warosu.org/data/ck/img/0061/93/1422747850829.jpg");
		}
		
		//link to playlist
		else if (message.equalsIgnoreCase("!playlist")){
		sendMessage(channel, "https://play.spotify.com/user/redxyeti/playlist/2xDwRsvTA4Sv4BN8A1APeG");
		}
		
		//whale flash
		else if (message.equalsIgnoreCase("!whale")){
		sendMessage(channel, "http://img0.liveinternet.ru/images/attach/c/5/3970/3970473_sprite198.swf");
		}
		
		//returns a random stat
		else if (message.equalsIgnoreCase("!randstat")){
			if(sender.equals("redxyeti") || sender.equalsIgnoreCase("RadioG00se"))
			{
			int randStat = rng.nextInt(9);
			sendMessage(channel, "Increase: " + stats[randStat]);
			}
			else
				sendMessage(channel, "Only yeti can access this command");
		}
		
		//King. started off as a joke but now you type !king and receive a random amount of bux (or lose some)
		else if (message.equalsIgnoreCase("!king")){
		if(sender.equalsIgnoreCase("RadioG00se")){sendMessage(channel, "RadioG00se is the King of the chat!");}
		else{
			for(User s : users)
			{
			if(s.getName().equalsIgnoreCase(sender))
			{
			if(s.getRoll().equals("You have not rolled yet"))
			{
			res = rng.nextInt(10);
			if(res <= 5){
					s.setRoll(sender + " is not the King!");
					s.incPoints(5);
					sendMessage(channel, s.getRoll() + " You got 5 YetiBux!"); waitS(); return;}
			else if (res <= 8)
				{   s.setRoll(sender + " is a Jester!");
					s.decPoints(3);
					sendMessage(channel, s.getRoll() + " You lost 3 YetiBux!");waitS(); return;}
			else{
					s.setRoll(sender + " is a Duke!");
					s.incPoints(30);
					sendMessage(channel, s.getRoll() + " You gained 30 YetiBux!"); 
					waitS(); return;
				}
			}
			else
			{	
				sendMessage(channel, sender + " already rolled, " + s.getRoll());
				waitS();
				return;
			}	
			}
			else if(s.getName().equalsIgnoreCase("RadioG00se"))
			{
			s.incPoints();
			}
			
		}
		sendMessage(channel, sender + ", You must user !register first!");
		}}
		
		//Returns total time streaming
		else if (message.equalsIgnoreCase("!uptime"))	{
		try{
			URL oracle = new URL("https://nightdev.com/hosted/uptime.php?channel=RedxYeti");
			BufferedReader in = new BufferedReader(
			new InputStreamReader(oracle.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				sendMessage(channel, inputLine);
			in.close();}
		catch(Exception e){System.out.print("Error");}
		}
		
		//Used to thank people who raid the chat. 
		else if (message.startsWith("!thank"))
		{
			for(User s : users)
			{
				if(s.isMod() && sender.equalsIgnoreCase(s.getName()))
				{
					giver = message.replace("!thank ","");
					sendMessage(channel, "Thanks so much " + giver + " for the raid! You can follow them here: http://twitch.tv/" + giver);
				}
			}
		}
		
		//Slaps someone, small chance of taking their yetibux
		//Formatted: !slap <username>
		else if (message.startsWith("!slap")){
			if(sender.equalsIgnoreCase("dumj00")) return;
			else if(message.toLowerCase().contains("yetixbot")){sendMessage(channel,sender + " got counter slapped by YetixBot");waitS(); return;}{
			if(fightClock > 0 || sender.equalsIgnoreCase("dumj00")){return;}
			else
				fightClock++;
			slapee = message;
			slapee = slapee.replace("!slap "," ");
			slapee = slapee.trim();
			if((rng.nextInt(15)) <= 1){

			for(User s : users)
			{
				if(s.getName().equalsIgnoreCase(sender))
				{
					amount = rng.nextInt(3) + 1;
					for(User d : users)
					{
						if(d.getName().equalsIgnoreCase(slapee))
						{
						
						d.decPoints(amount);
						s.incPoints(amount);
						sendMessage(channel, sender + " slapped " + slapee + " so hard, they stole " + amount + " YetiBux!");
						waitS();
						
						return;
						}
					}

				}
			}
			}
			sendMessage(channel, sender + " slapped " + slapee);}
			
		}
		
		//hug, used to force slapping and encourage senseless violence
		else if (message.startsWith("!hug ")){
			if(sender.equalsIgnoreCase("dumj00")) return;
			else if(message.toLowerCase().contains("yetixbot")){sendMessage(channel,sender + ", you can't hug code!");waitS(); return;}
			else{
			slapee = message;
			slapee = slapee.replace("!hug "," ");
			slapee = slapee.trim();
			sendMessage(channel, sender + " slapped " + slapee + " as hard as they could!");}

			
		}
	
		//used by mods to set the run
		else if (message.startsWith("!setrun")){
		int found = 0;
		for(User s : users) if(sender.equalsIgnoreCase(s.getName()) && s.isMod()) found = 1;
				
			if(found == 1)
			{
			newRun = message;
			newRun = newRun.replace("!setrun ","");
			if(newRun.contains("!setrun"))
				sendMessage(channel, "Usage: !setrun <run info>");
			else{
			runInfo = newRun;
			return;}
			}
			else
				sendMessage(channel, "Only mods can access this command");
			}
	
		//prints out the current run
		else if (message.equalsIgnoreCase("!run")){	sendMessage(channel, runInfo);	}
		
		//prints out current build
		else if (message.equalsIgnoreCase("!build")){sendMessage(channel, buildInfo);}
		
		//used by mods to set build
		else if (message.startsWith("!setbuild")){
		int found = 0;
		for(User s : users) if(sender.equalsIgnoreCase(s.getName()) && s.isMod()) found = 1;
		if(found == 1)
		{
				newBuild = message;
				newBuild = newBuild.replace("!setbuild ","");
				if(newBuild.contains("!setbuild") )
					sendMessage(channel, "Formatted: !setbuild <build info>");
				else{
				buildInfo = newBuild;
				return;}
		}
		else
			sendMessage(channel, "Only mods can access this command");
		}
		
		else if(message.equalsIgnoreCase("!sl"))
		{ sendMessage(channel, slString);}
		
		else if(message.startsWith("!setsl"))
		{
		for(User s : users) if(sender.equalsIgnoreCase(s.getName()) && s.isMod())
			{
				newBuild = message;
				newBuild = newBuild.replace("!setsl ","");
				if(newBuild.contains("!setsl"))
					sendMessage(channel, "Formatted: !sl <num>");
				else
				{
				slString = newBuild;
				}
			}
		}
		

		//Prints a list of (most) commands. doesnt include some like !confirm or !deny
		else if (message.equalsIgnoreCase("!commands") || message.equalsIgnoreCase("!help")){
		sendMessage(channel, "See the description below the stream");
		}
		
		//used if you dont accept a challenge
		else if (message.equalsIgnoreCase("!deny")){
		for(User s : users)
		{
			if(s.getName().equalsIgnoreCase(sender))
			{
				for(User d : users)
				{
					if(s.fighting())
					{
						sendMessage(channel, sender + ", you cannot deny for other people!");
						waitS();
						return;
					}
					if(d.getName().equalsIgnoreCase(s.fighter()))
					{
						s.fighterName("");
						s.setFight(0);
						d.fighterName("");
						d.setFight(0);
						s.isFighting(false);
						d.isFighting(false);
						sendMessage(channel, sender + " was too afraid to fight " + d.getName());
						waitS();
						return;
					}
				}
				}
		}
		
		}
		
		//used if you do accept a challenge
		else if (message.toLowerCase().startsWith("!confirm")){
			for(User s : users)
			{
				if(s.getName().equalsIgnoreCase(sender))
				{
					if(s.fighter().equalsIgnoreCase(""))
					{
						sendMessage(channel, "No one to fight!");
						waitS();
						return;
					}
					else
					{
						for(User d : users)
						{
							if(d.getName().equalsIgnoreCase(s.fighter()))
							{
								if(s.getPoints() < s.fightVal())
								{
									sendMessage(channel, sender + " does not have enough YetiBux!");
									s.fighterName("");
									s.setFight(0);
									d.fighterName("");
									d.setFight(0);
									waitS();
									return;
								}
								else if(d.getPoints() < d.fightVal())
								{
									sendMessage(channel, d.getName() + " does not have enough YetiBux!");
									s.fighterName("");
									s.setFight(0);
									d.fighterName("");
									d.setFight(0);
									waitS();
									return;
								}
								else if(s.fighting())
								{
								sendMessage(channel, s.getName() + ", you cannot confirm for other people!");
								/*s.fighterName("");
								s.setFight(0);
								s.isFighting(false);
								d.fighterName("");
								d.setFight(0);
								d.isFighting(false);*/
								waitS();
								return;
								}
								else{
										if(rng.nextInt(4) <= 1)
										{
											sendMessage(channel, d.getName() + " wins the fight and " + s.fightVal() + " YetiBux!");
											d.incPoints(s.fightVal());
											s.decPoints(s.fightVal());
											s.fighterName("");
											s.setFight(0);
											d.fighterName("");
											d.setFight(0);
											s.isFighting(false);
											d.isFighting(false);
											waitS();
											return;
										}
										else
										{
											sendMessage(channel, s.getName() + " wins the fight and " + s.fightVal() + " YetiBux!");
											s.incPoints(s.fightVal());
											d.decPoints(s.fightVal());
											s.fighterName("");
											s.setFight(0);
											d.fighterName("");
											d.setFight(0);
											s.isFighting(false);
											d.isFighting(false);
											waitS();
											return;
										}
								}
							}
						}
					}
				
				}
			
			}
		}
		
		//Two users fight it out. 
		//Can be formatted: !fight <username> or !fight <username> <amount>
		//if they have an amount, the person being challenged can deny the fight
		else if (message.startsWith("!fight")){
			defender = message.replace("!fight ","");
			if(defender.contains(" "))
			{
			fights = defender.split(" ");
			if(fights[1].contains(" ") || fights[1].matches("[^0-9]*"))
			{sendMessage(channel, "Formatted: !fight <username> OR !fight <Username> <amount>"); waitS(); return;}
			else
			{
			try{
			amount = Integer.parseInt(fights[1]);}
			catch(Exception e) { sendMessage(channel, "Invalid <amount>!"); waitS(); return;}
			if(amount <= 0)
			{
				sendMessage(channel, "Amount must be >0!");
				waitS();
				return;
			}
			else if(fights[0].equalsIgnoreCase("yetibot") || fights[0].equalsIgnoreCase("yetixbot"))
			{
				sendMessage(channel, "YetixBot wins the fight, but let you keep your money");
				waitS();
				return;
			}
			else if(fights[0].equalsIgnoreCase(sender))
			{
				sendMessage(channel, sender + ", you cannot fight yourself!");
				waitS();
				return;
			}
			for(User s : users)
			{
				if(s.getName().equalsIgnoreCase(sender))
				{
					if(s.getPoints() < amount)
					{sendMessage(channel, sender + " does not have " + amount + " YetiBux!"); waitS(); return;}
					else
					{
						for(User d : users)
						{
							if(d.getName().equalsIgnoreCase(fights[0]))
							{
								if(d.getPoints() < amount)
								{
								sendMessage(channel, fights[0] + " does not have " + amount + " YetiBux!"); waitS(); return;}
								else if (fightClock > 0) {return;}
								else
								{
								s.fighterName(fights[0]);
								s.setFight(amount);
								d.fighterName(sender);
								d.setFight(amount);
								s.isFighting(true);
								d.isFighting(false);
								sendMessage(channel, sender + " has challenged " + fights[0] + " to a fight for " + amount + " YetiBux! " + fights[0] + " please type !confirm or !deny in chat");
								fightClock = 4;
								waitS();
								return;
								}
							}
						}


					}
					sendMessage(channel, fights[0] + " could not be found! If they exist, have them type !register");
					waitS();
					return;
				}
			}
			
			}
			}
			else
			{

			if(defender.contains("!fight") || defender.matches("\\w"))
				sendMessage(channel, "Formatted: !fight <UserName>");
			else if(defender.toLowerCase().contains("yetixbot") || defender.toLowerCase().contains("yetibot"))
				sendMessage(channel, "YetixBot wins the fight!");
			else{
			fighter = sender;
			if(rng.nextInt() > rng.nextInt())
				sendMessage(channel, defender + " wins the fight!");
			else
				sendMessage(channel, fighter + " wins the fight!");
			}
			}}
			
		//used by me to kick people
		else if (message.startsWith("!kick")){
			if(sender.equalsIgnoreCase("RadioG00se"))
			{
			String banner = message;
			banner = banner.replace("!kick ","");
			sendMessage(channel, "/timeout " + banner + " 1");
		}}
		
		
		//Link Permissions, used to let someone post when they normally couldnt before
		//Formatted: !perm <username>
		else if (message.startsWith("!perm")){
			poster = message;
			poster = poster.replace("!perm ","");
			for(User s : users) if(sender.equalsIgnoreCase(s.getName()) && s.isMod()){
			
			
			if(poster.contains(" ")){ sendMessage(channel, "Usage: !perm <UserName>"); waitS(); return;}
			for(User d : users)
			if(d.getName().equalsIgnoreCase(poster)) {d.setPost(true); if(d.getPosts() > 0) d.decPosts(); return;}
			}
			sendMessage(channel, "Could not find user " + poster);
		}
		
		//Mod Commands, prints a list of mod only commands
		else if (message.equalsIgnoreCase("!modcommands")){
			int mod = 0;
			for(User s : users) if(sender.equalsIgnoreCase(s.getName()) && s.isMod()) mod = 1;
			if(mod == 1)
			{
			sendMessage(channel, "List of mod commands: !setbuild <build info>, !setrun <run info>, !perm <UserName>, !custom <command> <text> , !remove <command>, !endlotto");
			}
			else return; 
			}
			
						else if (message.equalsIgnoreCase("!startraffle")){
				if(sender.equalsIgnoreCase("redxyeti")){
					if(raffleInt == 1 ) {sendMessage(channel, "The raffle is already open!"); waitS(); return;}
					raffle = new ArrayList<String>();
					raffleInt = 1;
					sendMessage(channel, "The raffle is now open!");
					waitS();
					return;
				}
				
				
			}
			
			else if (message.equalsIgnoreCase("!endraffle")){
				if(sender.equalsIgnoreCase("redxyeti"))
				{
					if(raffle.size() == 0){sendMessage(channel, "No one entered!"); waitS(); return;}
					else if(raffleInt == 0){sendMessage(channel, "The raffle has not yet begun!"); waitS(); return;}
						winner = 0;
					for(int i = 0; i < raffle.size(); i++)
						{
							winner += rng.nextInt(3) - 1;
						}
						if(winner < 0 || winner > raffle.size()) winner = rng.nextInt(raffle.size());
						winnerCount = 0;
						for(String s : raffle)
						{
								if(winnerCount == winner)
								{
									sendMessage(channel, "Congrats " + s + ", you have won the raffle");
									winner = -1;
								}
									winnerCount++;
						}
					raffleInt = 0;
				}
				waitS();
			}
			
			else if (message.equalsIgnoreCase("!raffle"))
			{
				if(raffleInt == 1)
				{
				for( String s : raffle)
				{
					if(s.equalsIgnoreCase(sender)){sendMessage(channel, sender + ", you have already entered!"); waitS(); return;}
				}
				raffle.add(sender);		
				}
			}
			
			else{ for(Command c : commands){ if(message.equalsIgnoreCase(c.intake())) {sendMessage(channel, c.output()); waitS(); return;}} return;}

			waitS();
	}
	
	//When someone joins add them to the list of registered users. 
	public void onJoin(String channel, String sender, String login, String hostname)
	{
		if(login.equalsIgnoreCase("yetixbot")){
			sendMessage(channel, "YetixBot is online!");
			
		waitS(); return;}
		else
		{
		for(User s : users)
		{
			if(s.getName().equalsIgnoreCase(login)){ s.setChat(true); return;}
		}
		temp2 = new User(login);
		temp2.setPoints(5);
		users.add(temp2);
		}
		
	}
	
	public void onPart(String channel, String sender, String login, String hostname)
	{
		if(login.equalsIgnoreCase("yetixbot"))
		{
			try{
				this.connect("irc.twitch.tv",6667,/*Here is where the password goes, removed for security purposes*/);
				this.joinChannel("#redxyeti");
				this.sendRawLine("CAP REQ :twitch.tv/membership");
		
			} catch(Exception e) {System.out.print("rip");}
		}
		for(User s : users)
		{
			if(s.getName().equalsIgnoreCase(login)){ s.setChat(false); return;}
		
		}
	}

}
	
