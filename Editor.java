//To build a text Editor with Java using Java swing for front end
//Add find,replace.
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.text.*;
import javax.swing.text.BadLocationException;
import java.awt.Color;
class Editor
{
    JTextArea textarea;
    JFrame frame;
    JMenuBar menubar;
    JMenu file,edit;
    JMenuItem redo,replace;
    class LinkedList
    {
        Node head=null;
        class Node
        {
            String state;
            boolean pointer=false;
            Node next=null;
            //Node prev=null;
            Node(String s)
            {
                this.state=s;
                this.next=null;
                //this.prev=null;
            }
        }
        void insert(String s)
        {
            if(head==null)
            {
                head=new Node(s);
                head.pointer=true;
            }
            else
            {
                Node x=head;
                //x.pointer=false;
                while(x.next!=null)
                {
                    x=x.next;
                    x.pointer=false;
                }
                //Node newNode=new Node(s);
                falsify();
                x.next=new Node(s);
                x.next.pointer=true;
                //newNode.prev=x;
            }
        }

        void falsify()
        {
            Node y=head;
            while(y.next!=null)
            {
                y.pointer=false;
                y=y.next;
            }
        }

        String Undo()
        {
            Node x=head;
            /**if(x.next==null)
            {
            x.pointer=true;
            return x.state;
            }*/
            while(x.next.pointer!=true)
            {                
                x=x.next;
            }
            falsify();
            x.pointer=true;
            return x.state;
        }

        String Redo()
        {
            Node x=head;
            while(x.pointer!=true)
            {
                x=x.next;
            }
            x=x.next;
            falsify();
            x.pointer=true;
            return x.state;
        }
    }
    LinkedList list=new LinkedList();
    void Create()
    {
        frame = new JFrame("Text Editor");   
        try //To set Look and Feel and theme
        { 
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");              
        } 
        catch (Exception e) 
        { 
            System.out.println("An error occured while launching.");
        } 
        textarea = new JTextArea(); 
        textarea.addKeyListener(keys);
        menubar= new JMenuBar();

        //file menu creation starts
        file = new JMenu("File");          
        JMenuItem file1 = new JMenuItem("New"); 
        JMenuItem file2 = new JMenuItem("Open"); 
        JMenuItem file3 = new JMenuItem("Save"); 
        JMenuItem file4 = new JMenuItem("Print"); 

        file1.addActionListener(actions); 
        file1.setToolTipText("Create a new text file");
        file2.addActionListener(actions); 
        file2.setToolTipText("Open a file");
        file3.addActionListener(actions);
        file3.setToolTipText("Save the current file");
        file4.addActionListener(actions); 
        file4.setToolTipText("Print the file");

        file.add(file1); 
        file.add(file2); 
        file.add(file3); 
        file.add(file4);   
        //file menu creation ends

        //edit menu creation starts
        edit=new JMenu("Edit");             
        JMenuItem edit1 = new JMenuItem("Cut");
        edit1.setToolTipText("Cut the content and place in clipboard");
        JMenuItem edit2 = new JMenuItem("Copy");
        edit2.setToolTipText("Copy the content and place in clipboard");
        JMenuItem edit3 = new JMenuItem("Paste"); 
        edit3.setToolTipText("Paste the content in the clipboard");

        edit1.addActionListener(actions); 
        edit2.addActionListener(actions); 
        edit3.addActionListener(actions); 

        edit.add(edit1); 
        edit.add(edit2); 
        edit.add(edit3); 
        //edit menu creation ends

        //close menu creation begins
        JMenuItem close = new JMenuItem("Close"); 
        close.setToolTipText("Close the Editor");
        close.addActionListener(actions); 
        //close menu creation ends

        //undo menu creation starts
        JMenuItem undo=new JMenuItem("Undo");
        undo.setToolTipText("Undo last action");
        undo.addActionListener(actions);
        //undo menu creation ends

        //redo menu creation begins
        redo=new JMenuItem("Redo");
        redo.setToolTipText("Redo");
        redo.setEnabled(false);
        redo.addActionListener(actions);
        //redo menu creation ends

        //find menu creation starts
        JMenuItem find=new JMenuItem("Find");
        find.setToolTipText("Find a word");
        find.addActionListener(actions);
        //find menu creation ends
        replace=new JMenuItem("Replace");
        //replace.setEnabled(false);
        replace.addActionListener(actions);
        //adding menus to menubar
        menubar.add(file); 
        menubar.add(edit);         
        menubar.add(undo);
        menubar.add(redo);
        menubar.add(find);
        menubar.add(replace);
        menubar.add(close);

        //adding textarea and menubar to frame
        frame.setJMenuBar(menubar); 
        frame.add(textarea); 
        frame.setSize(500, 500); 
        frame.show(); 
    }
    public KeyListener keys=new KeyListener()
        {
            @Override
            public void keyPressed(KeyEvent k)
            {            
            }

            @Override
            public void keyTyped(KeyEvent k)
            {
                list.insert(textarea.getText());
            }

            @Override
            public void keyReleased(KeyEvent k)
            {          
            }        
        };
    private ActionListener actions=new ActionListener()
        {
            @Override        
            public void actionPerformed(ActionEvent e)
            {
                String command=e.getActionCommand(),findword=null;
                JFileChooser choose;
                int check;
                File f;

                //handling file menu item clicks
                switch (command)
                {
                    case "New":
                    textarea.setText("");
                    list=new LinkedList();
                    break;
                    case "Open":
                    choose=new JFileChooser();

                    //to make open option work only after user chooses a file
                    check=choose.showOpenDialog(null);
                    if(check==JFileChooser.APPROVE_OPTION)  //if user chooses a file
                    {
                        f=new File(choose.getSelectedFile().getAbsolutePath());

                        try
                        {
                            FileReader fr=new FileReader(f);
                            BufferedReader br=new BufferedReader(fr);
                            String words="",nextword=br.readLine();
                            while(nextword!=null)
                            {
                                words+="\n"+nextword; //Stores each sentence in separate line
                                nextword=br.readLine();
                            }
                            textarea.setText(words);
                            list=new LinkedList();
                        }
                        catch(Exception err)
                        {
                            JOptionPane.showMessageDialog(frame,err.getMessage());
                        }
                    }
                    else   //if user selects cancel
                    {
                        JOptionPane.showMessageDialog(frame,"No file opened");
                    }
                    break;
                    case "Save":
                    choose=new JFileChooser();
                    check=choose.showSaveDialog(null);
                    if(check==JFileChooser.APPROVE_OPTION) //User chooses to save
                    {
                        f=new File(choose.getSelectedFile().getAbsolutePath());
                        try
                        {
                            FileWriter fw=new FileWriter(f);
                            BufferedWriter bw=new BufferedWriter(fw);
                            bw.write(textarea.getText());
                            bw.flush();
                            bw.close();
                        }
                        catch(Exception err)
                        {
                            JOptionPane.showMessageDialog(frame,err.getMessage());
                        }
                    }
                    else //User clicks cancel
                    {
                        JOptionPane.showMessageDialog(frame,"Not saved");
                    }
                    break;
                    case "Print":
                    try
                    {
                        textarea.print();
                    }
                    catch(Exception err)
                    {
                        JOptionPane.showMessageDialog(frame,err.getMessage());
                    }
                    break;
                    case "Cut":
                    list.insert(textarea.getText());
                    textarea.cut();
                    //list.insert(textarea.getText());
                    break;
                    case "Copy":
                    list.insert(textarea.getText());
                    textarea.copy();
                    //list.insert(textarea.getText());
                    case "Paste":
                    list.insert(textarea.getText());
                    textarea.paste();
                    //list.insert(textarea.getText());
                    break;
                    case "Close":
                    //frame.setVisible(false);
                    int exit=JOptionPane.showConfirmDialog(null,"Do you want to exit");
                    switch(exit)
                    {
                        case 0:
                        frame.setVisible(false);
                        break;
                        case 1:
                        break;
                        case 2:
                    }
                    break;
                    case "Undo":
                    textarea.setText(list.Undo());
                    redo.setEnabled(true);
                    break;
                    case "Redo":
                    textarea.setText(list.Redo());
                    break;
                    case "Find":
                    //String findword=null;
                    findword=JOptionPane.showInputDialog("Find a word");
                    int index=textarea.getText().indexOf(findword);
                    try
                    {
                        highlight(findword,index);
                    }
                    catch(BadLocationException ble)
                    {
                        JOptionPane.showMessageDialog(frame,ble.getMessage());
                    }
                    //replace.setEnabled(true);
                    break;
                    case "Replace":
                    if(findword==null)
                    {
                        JOptionPane.showMessageDialog(frame,"Please select word to be replaced with Find");
                    }
                    else
                    {
                        String replaceword=JOptionPane.showInputDialog("Replace the word with -");
                        String newText=textarea.getText().replace("findword","replaceword");
                        textarea.setText(newText);
                    }
                }
            }      
        };
    public void highlight(String s,int a) throws BadLocationException
    {
        Highlighter high=textarea.getHighlighter();
        Highlighter.HighlightPainter paint=new DefaultHighlighter.DefaultHighlightPainter(Color.blue);
        high.addHighlight(a,s.length(),paint);
    }

    public static void main(String args[])
    {
        Editor edit=new Editor();
        edit.Create();
    }
}     
