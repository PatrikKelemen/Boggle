// Patrik Kelemen
// Boggle Game
// January 23 2018
// Will allow the user to play the board game Boggle
// with the goal of getting as many points as possible in 3 minutes

// Import necessary packages
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import  sun.audio.*;
import java.io.*;
import java.util.*;
import javax.swing.Timer;


public class Boggle extends JFrame implements ActionListener
{
  
  // Components
  // Labels
  static JLabel title = new JLabel("Boggle");
  
  // Buttons
  static JButton letters[][] = new JButton[4][4];
  
  // New Game, Instructions and Dictionary Buttons
  static JButton newGame = new JButton     ("   New Game   ");
  static JButton instructions = new JButton(" Instructions ");
  static JButton dictionary = new JButton  ("  Dictionary  ");
  
  // Submit and Clear Buttons
  static JButton submit = new JButton (" Submit ");
  static JButton cancel = new JButton (" Clear ");
  
  // Word Label and Box
  static JLabel wordLabel = new JLabel ("Words Used");
  static JList words;
  static DefaultListModel<String> wordList = new DefaultListModel<String>();
  
  // Time Label and Box
  static JLabel timeLabel = new JLabel ("Time Left: ");
  static JTextField timeBox = new JTextField ("");
  
  // Score Label and Box
  static JLabel scoreLabel = new JLabel ("Score: ");
  static JTextField scoreBox = new JTextField ("");
  
  // Guess Label and Box
  static JLabel guessLabel = new JLabel ("Your Guess: ");
  static JTextField guessBox = new JTextField ("");
  
  // Font for Title
  static Font titleFont = new Font ("TimesRoman", Font.BOLD, 32);
  static Font boxFont = new Font ("TimesRoman", Font.BOLD, 24);
  
  // All dice
  BoggleDice die[][] = new BoggleDice[4][4];
  
  // Stores previous die that has been clicked
  int prevDie[] = new int[2]; 
  
  // Variables for Timer
  int timer = 180;
  Timer t = new Timer (1000, this);;
  
  // Variable for Score
  int score = 0;
  
  // Array for storing used words
  String wordsUsed[] = new String[0];
  
  // Vector for Dictionary
  Vector<String> dict = new Vector<String>();
  
  // Constructor
  public Boggle()
  {
    // Set up the Frame
    super ("Boggle");
    setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    Container contentPane = getContentPane ();
    title.setFont (titleFont);
    
    // Initializes all Dice
    die[0][0] = new BoggleDice ("p", "c", "h", "o", "a", "s");
    die[0][1] = new BoggleDice ("o", "a", "t", "t", "o", "w");
    die[0][2] = new BoggleDice ("l", "r", "y", "t", "t", "e");
    die[0][3] = new BoggleDice ("v", "t", "h", "r", "w", "e");
    die[1][0] = new BoggleDice ("e", "g", "h", "w", "n", "e");
    die[1][1] = new BoggleDice ("s", "e", "o", "t", "i", "s");
    die[1][2] = new BoggleDice ("a", "n", "a", "e", "e", "g");
    die[1][3] = new BoggleDice ("i", "d", "s", "y", "t", "t");
    die[2][0] = new BoggleDice ("m", "t", "o", "i", "c", "u");
    die[2][1] = new BoggleDice ("a", "f", "p", "k", "f", "s");
    die[2][2] = new BoggleDice ("x", "l", "d", "e", "r", "i");
    die[2][3] = new BoggleDice ("e", "n", "s", "i", "e", "u");
    die[3][0] = new BoggleDice ("y", "l", "d", "e", "v", "r");
    die[3][1] = new BoggleDice ("z", "n", "r", "n", "h", "l");
    die[3][2] = new BoggleDice ("n", "m", "i", "qu", "h", "u");
    die[3][3] = new BoggleDice ("o", "b", "b", "a", "o", "j");
    
    // Fills Dictionary Vector
    try
    {
      // create a scanner attached the the file larger_dict
      Scanner readFile = new Scanner (new FileReader ("larger_dict.txt"));
      
      
      // continue reading words as long as there are words in the file
      while (readFile.hasNext())
      {
        dict.add (readFile.next ()); // read in the next word
      }
      
      readFile.close();
          
    }
    
    catch (FileNotFoundException err)
    {
      JOptionPane.showMessageDialog (null, "Error: Dictionary file not found, ensure file exists. Program will now close");
      System.exit(0);
    }
    
    // Panel setup
    JPanel topPanel = new JPanel(); // Top Panel
    topPanel.setLayout (new BoxLayout (topPanel, BoxLayout.PAGE_AXIS));
    
    JPanel highButtonPanel = new JPanel(); // Top Button Panel
    highButtonPanel.setLayout (new BoxLayout (highButtonPanel, BoxLayout.LINE_AXIS));
    
    JPanel leftPanel = new JPanel(); // Left side panel
    leftPanel.setLayout (new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
    
    JPanel rightPanel = new JPanel(); // Left side panel
    rightPanel.setLayout (new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
    
    JPanel letterGrid = new JPanel(); // Panel for Letter Grid
    letterGrid.setLayout (new GridLayout (4, 4, 0, 0));
    
    JPanel guessPanel = new JPanel(); // Panel for Guess Display
    guessPanel.setLayout (new BoxLayout (guessPanel, BoxLayout.LINE_AXIS));
    
    JPanel lowButtonPanel = new JPanel(); // Panel for Submit and Clear Buttons
    lowButtonPanel.setLayout (new BoxLayout (lowButtonPanel, BoxLayout.LINE_AXIS));
    
    JPanel wordPanel = new JPanel (); // Panel for Word List
    wordPanel.setLayout (new GridLayout (1, 1, 0, 0));
    
    JPanel bottomPanel = new JPanel(); // Panel for Guess Display
    bottomPanel.setLayout (new BoxLayout (bottomPanel, BoxLayout.LINE_AXIS));
    
    // Filling Panels
    // Grid for Dice
    for (int count1 = 0 ; count1 < 4 ; count1++)
    {
      for (int count2 = 0 ; count2 < 4 ; count2++)
      {
        letters [count1][count2] = new JButton (" ");
        letterGrid.add (letters [count1][count2]);
        letters[count1][count2].addActionListener (this);
        letters[count1][count2].setFont (boxFont);
      }
    }
    
    // Guess panel
    guessPanel.add (guessLabel);
    guessPanel.add (guessBox);
    
    guessBox.setEditable (false);
    
    // Submit and Clear Buttons
    lowButtonPanel.add (submit);
    lowButtonPanel.add (cancel);
    // Adds Action Listeners
    submit.addActionListener (this);
    cancel.addActionListener (this);
    
    // Time and Score Panel
    bottomPanel.add (timeLabel);
    bottomPanel.add (timeBox);
    bottomPanel.add (scoreLabel);
    bottomPanel.add (scoreBox);
    
    timeBox.setEditable (false);
    scoreBox.setEditable (false);
    
    
    // Adding Everything to the Left Panel
    leftPanel.add (letterGrid);
    leftPanel.add (guessPanel);
    leftPanel.add (lowButtonPanel);
    leftPanel.add (bottomPanel);
    
    // Creates list
    words = new JList<String> (wordList);
    // Creates scroll pane
    JScrollPane listScroll = new JScrollPane (words);
    words.setVisibleRowCount (5);
    wordList.addElement ("");
    wordPanel.add (listScroll);
    words.setFixedCellWidth (200);
    
    
    // Adding Everything to Right Panel
    rightPanel.add (wordLabel);
    rightPanel.add (wordPanel);
    
    // Adding Everything to Top Button Panel
    highButtonPanel.add (newGame);
    highButtonPanel.add (instructions);
    highButtonPanel.add (dictionary);
    
    newGame.addActionListener (this);
    instructions.addActionListener (this);
    dictionary.addActionListener (this);
    
    // Adding Everything to Top Panel
    topPanel.add (title);
    topPanel.add(highButtonPanel);
    
    contentPane.add (topPanel, BorderLayout.PAGE_START);
    contentPane.add (leftPanel, BorderLayout.CENTER);
    contentPane.add (rightPanel, BorderLayout.LINE_END);
    
    
    setSize (480,350);
    setResizable(false);
    setVisible (true);
    
  }
  
  
  
  public void actionPerformed (ActionEvent event)
  {
    
    // For Starting a New Game
    if (event.getSource() == newGame)
    {
      
      // Plays Sound
      try
      {
        playSound (2);
      }
      
      catch (FileNotFoundException err)
      {}
      catch (IOException err)
      {}
      
      int temp = 0; // Integer for Option Pane
      
      temp = JOptionPane.showConfirmDialog (null, "Would you like to start a new game?", "New Game", JOptionPane.YES_NO_OPTION);
      
      // If User Wants to Start a New Game
      if (temp == 0)
      {
        // Plays Sound
        try
        {
          playSound (4);
        }
        
        catch (FileNotFoundException err)
        {}
        catch (IOException err)
        {}
        
        t = new Timer (1000, this);
        timer = 180;
        timeBox.setText (Integer.toString (timer));
        
        
        // Resets Guess Box and Words Used List
        guessBox.setText ("");
        wordList.removeAllElements();
		wordsUsed = new String[0];
        
        
        // Starts timer
        t.start();
        
        // Enables Submit and Cancel Buttons
        submit.setEnabled (true);
        cancel.setEnabled (true);
        
        // Resets Score
        score = 0;
        scoreBox.setText (Integer.toString (score));
        
        // Clears Grid and Enables Buttons
        for (int count1 = 0 ; count1 < 4 ; count1++)
        {
          for (int count2 = 0 ; count2 < 4 ; count2++)
          {
            letters [count1][count2].setText (" ");
            letters [count1][count2].setEnabled (true);
          }
        }
        
        for (int row = 0 ; row < 4 ; row++)
        {
          for (int column = 0 ; column < 4 ; column++)
          {
            int num;
            int xCoord, yCoord;
            
            xCoord = (int)(Math.random () * 4);
            yCoord = (int)(Math.random () * 4);
            
            // Chooses x and y coordinate for die
            while (!(letters[xCoord][yCoord]).getText().equals(" "))
            {
              xCoord = (int)(Math.random () * 4);
              yCoord = (int)(Math.random () * 4); 
            }
            
            // Resets Text Colour on Dice
            letters[xCoord][yCoord].setForeground (Color.BLACK);
            
            num = (int)(Math.random () * 6) + 1; // Chooses Side of Die to Take Letter From
            
            // Chooses Box to put die in
            // Sets Label on Button to Letter Chosen from Die
            if (num == 1) // If first side
            {
              letters[xCoord][yCoord].setText (die[row][column].getOne());
            }
            
            else if (num == 2) // If second side
            {
              letters[xCoord][yCoord].setText (die[row][column].getTwo());
            }
            
            else if (num == 3) // If third side
            {
              letters[xCoord][yCoord].setText (die[row][column].getThree());
            }
            
            else if (num == 4) // If fourth side
            {
              letters[xCoord][yCoord].setText (die[row][column].getFour());
            }
            
            else if (num == 5) // If fifth side
            {
              letters[xCoord][yCoord].setText (die[row][column].getFive());
            }
            
            else if (num == 6) // If sixth side
            {
              letters[xCoord][yCoord].setText (die[row][column].getSix());
            }
            
          }
          
        }
      }
    } // End of New Game Event
    
    // Checks for Letter Buttons Being Pressed if no letters have been chosen
    if (guessBox.getText().equals (""))
    {
      for (int checkRows = 0 ; checkRows < 4 ; checkRows++)
      {
        for (int checkColumns = 0 ; checkColumns < 4 ; checkColumns++)
        {
          if (event.getSource() == letters[checkRows][checkColumns])
          {
            // Makes Sure Button has not Already Been Pressed
            if (letters[checkRows][checkColumns].getForeground() != Color.RED)
            {
              // Plays Sound
              try
              {
                playSound (2);
              }
              
              catch (FileNotFoundException err)
              {}
              catch (IOException err)
              {}
              // Sets Text Red and Adds Letter to Word
              letters[checkRows][checkColumns].setForeground (Color.RED);
              guessBox.setText (guessBox.getText() + letters[checkRows][checkColumns].getText());
              prevDie[0] = checkRows;
              prevDie[1] = checkColumns;
            }
          }
        }
      }
    }
    
    // If it is not the first click of a game
    else
    {
      // Cycles through every space near the last chosen letter
      for (int checkRows = prevDie[0] - subtractSpots (prevDie[0])  ; checkRows <= prevDie [0] + addSpots(prevDie[0]) ; checkRows++)
      {
        for (int checkColumns = prevDie[1] - subtractSpots (prevDie[1])  ; checkColumns <= prevDie [1] + addSpots(prevDie[1]) ; checkColumns++)
        {
          if (event.getSource() == letters[checkRows][checkColumns])
          {
            // Makes Sure Button has not Already Been Pressed
            if (letters[checkRows][checkColumns].getForeground() != Color.RED)
            {      
              // Plays Sound
              try
              {
                playSound (2);
              }
              
              catch (FileNotFoundException err)
              {}
              catch (IOException err)
              {}
              // Sets Text Red and Adds Letter to Word
              letters[checkRows][checkColumns].setForeground (Color.RED);
              guessBox.setText (guessBox.getText() + letters[checkRows][checkColumns].getText());
              prevDie[0] = checkRows;
              prevDie[1] = checkColumns;
            }
          }
        }
      }
    }
    
    // Ticks down Timer and Checks for End of Game
    if (event.getSource() == t)
    {
      timeBox.setText (Integer.toString (timer));
      if (timer > 0)
      {
        timer = timer - 1;
      }
      
      else if (timer <= 0)
      {
        t.stop();
        
        // Disables Buttons
        for (int count1 = 0 ; count1 < 4 ; count1++)
        {
          for (int count2 = 0 ; count2 < 4 ; count2++)
          {
            letters [count1][count2].setText (" ");
            letters [count1][count2].setEnabled (false);
          }
        }
        
        submit.setEnabled (false);
        cancel.setEnabled (false);
        
        JOptionPane.showMessageDialog(null, "Time has run out! Your score: " + score);
        
        
      }
      
    }
    
    // Clears everything if user presses "clear" button
    if (event.getSource() == cancel)
    {
      // Plays Sound
      try
      {
        playSound (2);
      }
      
      catch (FileNotFoundException err)
      {}
      catch (IOException err)
      {}
      
      guessBox.setText ("");
      
      for (int clearRows = 0 ; clearRows < 4 ; clearRows++)
      {
        for (int clearColumns = 0 ; clearColumns < 4 ; clearColumns++)
        {
          letters [clearRows][clearColumns].setForeground (Color.BLACK);
        }
      }
    }
    
    // Submits Word
    if (event.getSource() == submit)
    {
      // Makes sure length requirement is met, word hasn't been used, and word is in the dictionary
      if ((guessBox.getText()).length() > 2 && sequential (wordsUsed, guessBox.getText()) == 0 && (binary(dict, guessBox.getText())) == 1 )
      { 
        // Plays Sound
        try
        {
          playSound (3);
        }
        
        catch (FileNotFoundException err)
        {}
        catch (IOException err)
        {}
        
        wordList.addElement (guessBox.getText()); // Adds Word to list of used words
        
        // Fixes Length of Array
        String temp[] = new String [wordsUsed.length + 1];
        
        System.arraycopy (wordsUsed, 0, temp, 0, wordsUsed.length);
        
        wordsUsed = new String [temp.length];
        
        System.arraycopy (temp, 0, wordsUsed, 0, wordsUsed.length);
        
        // Adds word to array
        wordsUsed[wordsUsed.length - 1] = guessBox.getText();
        
        // Adds Appropriate Score
        if ((guessBox.getText()).length() == 3)
        {
          score = score + 1;
        }
        
        else if ((guessBox.getText()).length() == 4)
        {
          score = score + 2;
        }
        
        else if ((guessBox.getText()).length() == 5)
        {
          score = score + 3;
        }
        
        else if ((guessBox.getText()).length() == 6)
        {
          score = score + 5;
        }
        
        else if ((guessBox.getText()).length() == 7)
        {
          score = score + 7;
        }
        
        else if ((guessBox.getText()).length() >= 8)
        {
          score = score + 11;
        }
        
        scoreBox.setText (Integer.toString (score));
        
        guessBox.setText (""); // Clears guess box
        
        // Resets grid
        for (int clearRows = 0 ; clearRows < 4 ; clearRows++)
        {
          for (int clearColumns = 0 ; clearColumns < 4 ; clearColumns++)
          {
            letters [clearRows][clearColumns].setForeground (Color.BLACK);
          }
        }
      }
      
      // If length requirement is not met
      else if ((guessBox.getText()).length() < 3)
      {
        // Plays Sound
        try
        {
          playSound (1);
        }
        
        catch (FileNotFoundException err)
        {}
        catch (IOException err)
        {}
        JOptionPane.showMessageDialog(null, "Word too short!");
      }
      
      // If word is not in dictionary
      else if (dict.contains(guessBox.getText()) == false)
      {
        // Plays Sound
        try
        {
          playSound (1);
        }
        
        catch (FileNotFoundException err)
        {}
        catch (IOException err)
        {}
        JOptionPane.showMessageDialog(null, "Word not in dictionary!");
      }
      
      // If word has already been used
      else
      {
        // Plays Sound
        try
        {
          playSound (1);
        }
        
        catch (FileNotFoundException err)
        {}
        catch (IOException err)
        {}
        JOptionPane.showMessageDialog(null, "Word already used!");
      }
    }
    
    // Displays Instructions
    if (event.getSource() == instructions)
    {
      // Plays Sound
      try
      {
        playSound (2);
      }
      
      catch (FileNotFoundException err)
      {}
      catch (IOException err)
      {}
      JOptionPane.showMessageDialog(null, "The game of Boggle tests your word knowledge. \n\n" +
                                    "At the beginning of a new game, sixteen dice, each containing 6 letters, are shaken and then allowed to settle in a \n" +
                                    "4 by 4 grid leaving the player with 16 random letters to work with. \n\n" +
                                    "The player's task is to click on adjacent letters to form actual words without using the same die more than once. \n" +
                                    "The words formed must be at least three letters long, and they must show up in the dictionary. The same word \n" + 
                                    "cannot be used more than once. \n\n" + 
                                    "Scoring is based on the length of the words created as follows: \n" +
                                    "-3 letters long: 1 point \n" +
                                    "-4 letters long: 2 points \n" +
                                    "-5 letters long: 3 points \n" +
                                    "-6 letters long: 5 points \n" +
                                    "-7 letters long: 7 points \n" +
                                    "-8+ letters long: 11 points \n\n\n" +
                                    "How many words can you create in 3 minutes? \n\n" +
                                    "Note: The player can use the dictionary button to search the dictionary and add words.");
    }
    
    
    // Allows the user to search and modify the dictionary
    if (event.getSource() == dictionary)
    {
      // Plays Sound
      try
      {
        playSound (2);
      }
      
      catch (FileNotFoundException err)
      {}
      catch (IOException err)
      {}
      
      // Word to be searched for
      String word;
      
      word = JOptionPane.showInputDialog ("Enter a word to be searched for:");
      
      // Checks if word is in dictionary
      if (dict.contains(word))
      {
        JOptionPane.showMessageDialog (null, "That word is in the dictionary");
      }
      
      else if (word == null)
      {
        JOptionPane.showMessageDialog (null, "No word entered, returning to game");
      }
      
      else if (word.compareTo ("a") < 0 || word.compareTo ("zzz") > 0)
      {
        JOptionPane.showMessageDialog (null, "Invalid word, returning to game");
      }
      
      else
      {
        // Gives the user an option to add word to dictionary
        int temp = JOptionPane.showConfirmDialog (null, "Word not in dictionary. Would you like to add it?", "Add Word", JOptionPane.YES_NO_OPTION);
        
        // If word is being added
        if (temp == 0);
        {
          
          // Adds word to dictionary and sorts vector
          dict.add (word);
          Collections.sort(dict);
          
          // Set up the writer for the file
          try
          {
            PrintWriter fileOut = new PrintWriter (new FileWriter ("larger_dict.txt"));
            
            // Outputs all words to file
            for (int output = 0 ; output < dict.size() ; output++)
            {
              fileOut.println (dict.get(output));
            }
            
            fileOut.close ();
            
          }
          // Catches errors from file writing
          catch (IOException err)
          {
            JOptionPane.showMessageDialog (null, "Error: Failure updating dictionary");
          }
        }
      }
    }
    
    
    
  } // End of Action Performed Method
  
  // Sequential Search Method
  public static int sequential (String []array, String searchElement)
  {
    
    for (int i = 0; i < array.length; i++)
    { 
      if (array[i].equals (searchElement))
      {
        return 1;
      }
    }
    return 0;
  }
  
  // Binary Search Method
  static public int binary(Vector<String> vctr, String target)
  {
    int high = vctr.size();
    int low = 0;
    int middle;
    int count = 0;
    
    while (high - low > 1)
    {
      count = count + 1;
      middle = (high + low)/2;
      
      // make middle = to index of middle element of remaining elements
      if (vctr.get(middle).compareTo (target) > 0)     // item must be in first half
        high = middle;
      
      else
        low = middle;
    }
    
    if (low == -1 || !vctr.get(low).equals (target))
    {
      return 0;
    }
    
    else
    {
      return 1;
    }
  }
  
// Determines how many rows/columns to subtract to find dice which are able to be clicked
  public int subtractSpots (int dieClicked)
  {
    if (dieClicked == 0)
    {
      return 0;
    }
    
    else
    {
      return 1;
    }
  }
  
  // Determines how many rows/columns to add to find dice which are able to be clicked
  public int addSpots (int dieClicked)
  {
    if (dieClicked == 3)
    {
      return 0;
    }
    
    else
    {
      return 1;
    }
  }
  
  // Procedure for playing sounds
  public void playSound (int num) throws FileNotFoundException, IOException
  {
    // Sound Effects
    // Open an input stream  to audio files
    InputStream inUhOh = new FileInputStream("Uh-Oh.wav");
    InputStream inBlip = new FileInputStream("blip.wav");
    InputStream inBoing = new FileInputStream("Boing.wav");
    InputStream inSub = new FileInputStream("sub.wav");
    
    // Creates audio stream
    AudioStream audio = new AudioStream (inUhOh); 
    
    // Determines which sound to play
    if (num == 2)
      audio = new AudioStream (inBlip); // "Blip"  Sound
    
    else if (num == 3)
      audio = new AudioStream (inBoing); // "Boing"  Sound
    
    else if (num == 4)
      audio = new AudioStream (inSub); // "Sub"  Sound
    
    AudioPlayer.player.start(audio);
  }
  
}
