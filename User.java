public class User {
	private String name;
	
	private int points;
	
	private String roll;
	
	private boolean mod;
	
	private boolean canPost;
	private int posts;
	
	private boolean inChat;
	
	private String fighter;
	private int fightVal;
	private boolean fighting;
	
	private boolean inlotto;
	
	private boolean canLink;
	private int timeInChat;

	private int rollClock;
	
	public User(String name)
	{
	this.name = name;
	this.points = 0;
	roll = "You have not rolled yet";
	mod = false;
	canPost = false;
	posts = 0;
	fighter = "";
	fightVal = 0;
	fighting = false;
	inlotto = false;
	canLink = false;
	timeInChat = 0;
	}
	

	
	public int rollClock()
	{return rollClock;}
	public void setRollClock(int c)
	{rollClock = c;}
	public void decRollClock()
	{rollClock--;}
	
	public boolean getlotto()
	{return inlotto;}
	public void setlotto(boolean raf)
	{inlotto = raf;}


	public String getName()
{	return name;}	
		
	public int getPoints()
	{
	if(points < 0) points = 0;
	return points;
	}

	public void setPoints(int p)
	{
	points = p;
	}
	
	public void incPoints()
	{ points++;}
	public void incPoints(int p)
	{
	points = points + p;
	}
	
	public void decPoints()
	{points--;}
	public void decPoints(int p)
	{
	points = points - p;
	}
	
	public String getRoll()
	{
	return roll;
	}
	
	public void setRoll(String r)
	{
	roll = r;
	}
	
	public boolean isMod()
	{
	return mod;
	}
	
	public void setMod(boolean m)
	{
		mod = m;
	}
	
	public void setPost(boolean m)
	{
		canPost = m;
	}
	
	public boolean canPost()
	{
	return canPost;
	}
	
	public int getPosts()
	{
	return posts;
	}
	
	public void incPosts(){posts++;}
	public void decPosts(){posts--;}
	
	public boolean inChat()
	{return inChat;}
	public void setChat(boolean in)
	{inChat = in;}
	
	
	public String fighter()
	{
	return fighter;
	}
	public void fighterName(String f)
	{
	fighter = f;
	}
	
	public int fightVal()
	{
	return fightVal;
	}
	public void setFight(int v)
	{
	fightVal = v;
	}
	
	public boolean fighting()
	{ return fighting; }
	public void isFighting(boolean f)
	{
		fighting = f;
	}
	
	
	
	public boolean canLink()
	{ return canLink; }
	
	public void letLink(boolean l)
	{ canLink = l;}
	
	public int timeInChat()
	{return timeInChat;}
	
	public void addChatTime(int t)
	{timeInChat += t;}
	


}