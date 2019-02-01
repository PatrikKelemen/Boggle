import java.io.*;

// The "BoggleDice" class
public class BoggleDice implements Serializable
{
  private String letterOne, letterTwo, letterThree, letterFour, letterFive, letterSix;        // All sides on dice
  
  // Constructor for assigning values to variables
  public BoggleDice (String letter1, String letter2, String letter3, String letter4, String letter5, String letter6)
  {
    this.letterOne = letter1;
    this.letterTwo = letter2;
    this.letterThree = letter3;
    this.letterFour = letter4;
    this.letterFive = letter5;
    this.letterSix = letter6;
  }
  
  // Null Constructor
  public BoggleDice ()
  {
    this.letterOne = "";
    this.letterTwo = "";
    this.letterThree = "";
    this.letterFour = "";
    this.letterFive = "";
  }
  
  public void setOne (String factor)
  {
    this.letterOne = factor;
  }
  
  public void setTwo (String factor)
  {
    this.letterTwo = factor;
  }
  
  public void setThree (String factor)
  {
    this.letterThree = factor;
  }
  
  public void setFour (String factor)
  {
    this.letterFour = factor;
  }
  
  public void setFive (String factor)
  {
    this.letterFive = factor;
  }
  
  public void setSix (String factor)
  {
    this.letterSix = factor;
  }
  
  public String getOne ()
  {
    return this.letterOne;
  }
  
  public String getTwo ()
  {
    return this.letterTwo;
  }
  
  public String getThree ()
  {
    return this.letterThree;
  }
  
  public String getFour ()
  {
    return this.letterFour;
  }
  
  public String getFive ()
  {
    return this.letterFive;
  }
  
  public String getSix ()
  {
    return this.letterSix;
  }
}