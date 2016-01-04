public class Command{
	private String intake;
	private String output;
	
	public Command(String intake, String output)
	{
		this.intake = intake;
		this.output = output;
	}
	
	public String intake()
	{ return intake;}
	
	public String output()
	{ return output;}

}