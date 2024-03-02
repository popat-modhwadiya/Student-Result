package popat_mini_project;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.plaf.FontUIResource;

public class Popat_mini_project implements ActionListener, ChangeListener, FocusListener, ItemListener {

    JFrame jf;                  //main frame
    JTabbedPane jtp;            //tabbed pane
    JPanel ijp, ujp, djp, rjp;     //4 panel for insert update delete retrive

    //Insert components
    JLabel ijleno, ijlname, ijlnma, ijlajava, ijlmcom, ijstatuslabel, ijstatus;
    JTextField ijteno, ijtname, ijtnma, ijtajava, ijtmcom;
    JButton ibtn;
    int iflag;      //if eno already inserted then new eno cannot be inserted

    //Update components
    JLabel ujleno, ujlname, ujlnma, ujlajava, ujlmcom, ujstatuslabel, ujstatus;
    JComboBox ujceno;
    JTextField ujtname, ujtnma, ujtajava, ujtmcom;
    JButton ubtn;

    //Delete components
    JLabel djleno;
    JComboBox djceno;
    JButton dbtn;

    //Retrive components
    DefaultTableModel rdtm;
    JTable rjt;
    JScrollPane rjsp;
    DefaultTableCellRenderer cellRenderer;

    //for dialogbox
    JLabel status = new JLabel();

    //for database connection
    Connection con;
    Statement stmt;
    ResultSet rs;
    ResultSetMetaData rsmd;
    int dbflag = 0;//this is used if db conncection failed rest of code not excuted 

    //different font for later use
    Font font = new Font("common", Font.BOLD, 20);
    Font font2 = new Font("common", Font.BOLD, 35);
    Font font3 = new Font("common", Font.BOLD, 25);

    //constructor
    Popat_mini_project() {
        jf = new JFrame();
        jf.setTitle("POPAT MODHWADIYA AJAVA MINI PROJECT");

        jtp = new JTabbedPane();
        jtp.setFont(new Font("common", Font.BOLD, 20));
        status.setFont(font2);

        //for changing dialog box msg OK button
        UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("ARIAL", Font.PLAIN, 35)));

        insert_gui();   //insert gui created
        update_gui();   //update gui created
        delete_gui();   //delete gui created
        retrive_gui();  //retrive gui created

        //adding all four jpanel into tabbedpane
        jtp.addTab("                            INSERT                      ", ijp);
        jtp.addTab("                            UPDATE                      ", ujp);
        jtp.addTab("                            DELETE                      ", djp);
        jtp.addTab("                            RETRIVE                     ", rjp);

        jtp.addChangeListener(this); //when tab changes 

        jf.add(jtp); //adding tabbed pane in frame
        jf.setResizable(false); //frame never rezied by user
        jf.setSize(1920, 1000);
        jf.setVisible(true);
        jf.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        db_connection();  //database connection
    }

    public static void main(String[] args) {
        new Popat_mini_project(); //obj created constructor calles
    }

    //*********** Method of ActionListener *******************//
    @Override
    public void actionPerformed(ActionEvent e) {

        String but = e.getActionCommand();  //which button was pressed?

        //checking all fields if anyone empty when button pressed
        if (but.equals("Insert") && ((ijteno.getText()).equals("") || (ijtname.getText()).equals("") || (ijtmcom.getText()).equals("") || (ijtnma.getText()).equals("") || (ijtajava.getText()).equals(""))) {
            status.setText("Plese fill all details before Insert :)  ");
            JOptionPane.showMessageDialog(jf, status);
        } else if (but.equals("Delete") && ((djceno.getSelectedIndex() == -1))) {
            status.setText("Plese fill all details before Delete :)  ");
            JOptionPane.showMessageDialog(jf, status);
        } else if (but.equals("Update") && ((ujceno.getSelectedIndex() == -1) || ((ujtname.getText()).equals("")) || (ujtmcom.getText().equals("")) || (ujtnma.getText().equals("")) || (ujtajava.getText().equals("")))) {

            status.setText("Plese fill all details before Update :)  ");
            JOptionPane.showMessageDialog(jf, status);
        } //* ********* Data Insert ******************//
        else if (but.equals("Insert")) {
            db_connection();
            if (dbflag != 1) {  //if database not connected
                try {
                    //if Enrollment no already in table then redundent eno not inserted
                    iflag = 0;
                    rs = stmt.executeQuery("SELECT Eno FROM student_result");
                    while (rs.next()) {
                        if ((String.valueOf(rs.getLong(1))).equals(ijteno.getText())) {
                            iflag = 1;
                            status.setText("Enrollment No. already inserted :) ");
                            JOptionPane.showMessageDialog(jf, status);
                            break;
                        }
                    }
                    if (iflag != 1) {
                        //data inserted in table
                        int z = stmt.executeUpdate("INSERT INTO student_result VALUES(" + ijteno.getText() + ",'" + ijtname.getText() + "'," + ijtajava.getText() + "," + ijtnma.getText() + "," + ijtmcom.getText() + ",'" + ijstatus.getText() + "')");
                        ijteno.setText("");     //after insert all textfield are blank
                        ijtname.setText("");
                        ijtajava.setText("");
                        ijtnma.setText("");
                        ijtmcom.setText("");
                        ijstatus.setText("");
                        status.setText("Data inserted successfully :)  ");
                        JOptionPane.showMessageDialog(jf, status);
                    }
                } catch (SQLException insE) {
                    status.setText("Error in Data Inserting :(  " + insE);
                    JOptionPane.showMessageDialog(jf, status);
                } finally {
                    finally_database();
                }
            }
        }
        
        //********* Data update ******************//
        else if (but.equals("Update")) {
            db_connection();
            if (dbflag != 1) {

                try {
                    int z = stmt.executeUpdate("UPDATE student_result SET Sname='" + ujtname.getText() + "',AJAVA =" + ujtajava.getText() + ",NMA=" + ujtnma.getText() + ",MCOM=" + ujtmcom.getText() + ",Status='" + ujstatus.getText() + "' WHERE Eno=" + ujceno.getSelectedItem() + "");
                    ujceno.setSelectedIndex(-1);
                    ujtname.setText("");
                    ujtajava.setText("");
                    ujtnma.setText("");
                    ujtmcom.setText("");
                    ujstatus.setText("");
                    status.setText("Data Updated successfully :)  ");
                    JOptionPane.showMessageDialog(jf, status);
                } catch (SQLException insE) {
                    status.setText("Error in Data Updating :(  " + insE);
                    JOptionPane.showMessageDialog(jf, status);
                } finally {
                    finally_database();
                }
            }
        } 
        //********* Data delete ******************//
        else if (but.equals("Delete")) {
            db_connection();
            if (dbflag != 1) {

                try {
                  
                    stmt.executeUpdate("DELETE FROM student_result WHERE Eno=" + djceno.getSelectedItem() + "");
                    djceno.removeItem(djceno.getSelectedItem());
                    djceno.setSelectedIndex(-1);
                    status.setText("Data Deleted successfully :)  ");
                    JOptionPane.showMessageDialog(jf, status);
                } catch (SQLException insE) {

                    status.setText("Error in Data Deleteing :(  " + insE);
                    JOptionPane.showMessageDialog(jf, status);
                } finally {
                    finally_database();
                }
            }
        }

    }

    //*********** Method of ItemListener *******************//
    //calls when item selected in drop down
    @Override
    public void itemStateChanged(ItemEvent e) {

        db_connection();
        if (dbflag != 1) {

            try {
                //fetching selected enrollment old values in textfields
                rs = stmt.executeQuery("SELECT * FROM student_result WHERE Eno = " + ujceno.getSelectedItem() + "");
                while (rs.next()) {
                    ujtname.setText(rs.getString(2));
                    ujtajava.setText(rs.getString(3));
                    ujtnma.setText(rs.getString(4));
                    ujtmcom.setText(rs.getString(5));
                    ujstatus.setText(rs.getString(6));
                }
            } catch (SQLException itemE) {
                status.setText("Error in Retriving data :(  " + itemE);
                JOptionPane.showMessageDialog(jf, status);
            } finally {
                finally_database();
            }
        }

    }

    //*********** Method of ChangeListener *******************//
    //calls when tabs are change 
    @Override
    public void stateChanged(ChangeEvent e) {

        int activetab = jtp.getSelectedIndex();

        if (activetab == 3) //this is for retrive tab
        {
            rjp.removeAll();    //remove all components of retrive panel
            displayTable();
        }
        if (activetab == 1 || activetab == 2) //this is for insert and update tab
        {
            loadname();
        }

    }

    //*********** Method to retrive primary key from table and set in Dropdownlist *******************//
    private void loadname() {
        ujceno.removeItemListener(this);    //when all eno loaded itemlistener called when each name loads so avoid this remove itemlistener
        ujtname.setText("");
        ujtajava.setText("");
        ujtnma.setText("");
        ujtmcom.setText("");
        ujstatus.setText("");
        ujceno.removeAllItems();
        djceno.removeAllItems();

        db_connection();
        if (dbflag != 1) {
            try {
                rs = stmt.executeQuery("SELECT Eno FROM student_result");
                while (rs.next()) {
                    ujceno.addItem(rs.getString(1));
                    djceno.addItem(rs.getString(1));
                }
            } catch (SQLException loadE) {
                status.setText("Error in loading name :(  " + loadE);
                JOptionPane.showMessageDialog(jf, status);
            } finally {
                finally_database();
            }
        }
        ujceno.setSelectedIndex(-1);    //default no value selected in drop down
        djceno.setSelectedIndex(-1);
        ujceno.addItemListener(this);  //after loading all name in drop down itemlistener added 
    }

    //*********** Method to retrive data from database and fill in retrive tabbed table *******************//
    void displayTable() {

        rdtm = new DefaultTableModel();

        db_connection();
        if (dbflag != 1) {
            try {
                rs = stmt.executeQuery("SELECT * FROM student_result");
                rsmd = rs.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    rdtm.addColumn(rsmd.getColumnName(i));  //all columns name fetched from database added in dtm
                }
                while (rs.next()) {
                    rdtm.addRow(new Object[]{rs.getLong(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6)});
                }
            } catch (SQLException disE) {
                status.setText("Error in Data Retriving :(  " + disE);
                JOptionPane.showMessageDialog(jf, status);
            } finally {
                finally_database();
            }
        }

        rjt = new JTable(rdtm); //new table created
        rjt.setFont(font3);
        rjt.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        //new scrollpane created
        rjsp = new JScrollPane(rjt, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        rjsp.setFont(font3);
        rjt.setRowHeight(40);   //row height setted
        rjt.getTableHeader().setFont(font3);    //table column header font 

        //setting text in all table cells center
        cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        rjt.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        rjt.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
        rjt.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
        rjt.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
        rjt.getColumnModel().getColumn(4).setCellRenderer(cellRenderer);
        rjt.getColumnModel().getColumn(5).setCellRenderer(cellRenderer);

        rjt.getColumnModel().getColumn(1).setPreferredWidth(200);   //setting name column width

        rjt.setFont(font3);
        rjp.add(rjsp);  //scrollpane added in panel

    }

    //*********** Method to create insert GUI *******************//
    void insert_gui() {
        //************ insert jpanel ***************//
        ijp = new JPanel();
        ijp.setLayout(null);
        ijp.setFont(font);

        ijleno = new JLabel("Enrollment No.");
        ijleno.setBounds(415, 90, 250, 50);
        ijleno.setFont(font2);

        ijlname = new JLabel("Name");
        ijlname.setBounds(415, 180, 250, 50);
        ijlname.setFont(font2);

        ijlnma = new JLabel("NMA");
        ijlnma.setBounds(415, 270, 250, 50);
        ijlnma.setFont(font2);

        ijlajava = new JLabel("AJAVA");
        ijlajava.setBounds(415, 360, 250, 50);
        ijlajava.setFont(font2);

        ijlmcom = new JLabel("MCOM");
        ijlmcom.setBounds(415, 450, 250, 50);
        ijlmcom.setFont(font2);

        ijstatuslabel = new JLabel("Status");
        ijstatuslabel.setBounds(415, 540, 250, 50);
        ijstatuslabel.setFont(font2);

        ijteno = new JTextField();
        ijteno.setBounds(715, 90, 400, 50);
        ijteno.setFont(font2);
        ijteno.addFocusListener(this);

        ijtname = new JTextField();
        ijtname.setBounds(715, 180, 400, 50);
        ijtname.setFont(font2);
        ijtname.addFocusListener(this);

        ijtnma = new JTextField();
        ijtnma.setBounds(715, 270, 400, 50);
        ijtnma.setFont(font2);
        ijtnma.addFocusListener(this);

        ijtajava = new JTextField();
        ijtajava.setBounds(715, 360, 400, 50);
        ijtajava.setFont(font2);
        ijtajava.addFocusListener(this);

        ijtmcom = new JTextField();
        ijtmcom.setBounds(715, 450, 400, 50);
        ijtmcom.setFont(font2);
        ijtmcom.addFocusListener(this);

        ijstatus = new JLabel("fail");
        ijstatus.setBounds(715, 540, 400, 50);
        ijstatus.setFont(font2);

        ibtn = new JButton("Insert");
        ibtn.setBounds(550, 650, 400, 50);
        ibtn.setFont(font2);
        ibtn.addActionListener(this);

        ijp.add(ijleno);
        ijp.add(ijlname);
        ijp.add(ijlnma);
        ijp.add(ijlajava);
        ijp.add(ijlmcom);
        ijp.add(ijstatuslabel);

        ijp.add(ijteno);
        ijp.add(ijtname);
        ijp.add(ijtnma);
        ijp.add(ijtajava);
        ijp.add(ijtmcom);
        ijp.add(ijstatus);

        ijp.add(ibtn);
        ijp.setFont(font);
        //************ insert jpanel GUI ends ***************//
    }

    //*********** Method to create update GUI *******************//
    void update_gui() {
        //************ Update jpanel ***************//
        ujp = new JPanel();
        ujp.setLayout(null);
        ujp.setFont(font);

        ujleno = new JLabel("Enrollment No.");
        ujleno.setBounds(415, 90, 250, 50);
        ujleno.setFont(font2);

        ujlname = new JLabel("Name");
        ujlname.setBounds(415, 180, 250, 50);
        ujlname.setFont(font2);

        ujlnma = new JLabel("NMA");
        ujlnma.setBounds(415, 270, 250, 50);
        ujlnma.setFont(font2);

        ujlajava = new JLabel("AJAVA");
        ujlajava.setBounds(415, 360, 250, 50);
        ujlajava.setFont(font2);

        ujlmcom = new JLabel("MCOM");
        ujlmcom.setBounds(415, 450, 250, 50);
        ujlmcom.setFont(font2);

        ujstatuslabel = new JLabel("Status");
        ujstatuslabel.setBounds(415, 540, 250, 50);
        ujstatuslabel.setFont(font2);

        ujceno = new JComboBox();
        ujceno.setBounds(715, 90, 400, 50);
        ujceno.setFont(font2);
        ujceno.addItemListener(this);

        ujtname = new JTextField();
        ujtname.setBounds(715, 180, 400, 50);
        ujtname.setFont(font2);
        ujtname.addFocusListener(this);

        ujtnma = new JTextField();
        ujtnma.setBounds(715, 270, 400, 50);
        ujtnma.setFont(font2);
        ujtnma.addFocusListener(this);

        ujtajava = new JTextField();
        ujtajava.setBounds(715, 360, 400, 50);
        ujtajava.setFont(font2);
        ujtajava.addFocusListener(this);

        ujtmcom = new JTextField();
        ujtmcom.setBounds(715, 450, 400, 50);
        ujtmcom.setFont(font2);
        ujtmcom.addFocusListener(this);

        ujstatus = new JLabel("fail");
        ujstatus.setBounds(715, 540, 400, 50);
        ujstatus.setFont(font2);

        ubtn = new JButton("Update");
        ubtn.setBounds(550, 650, 400, 50);
        ubtn.setFont(font2);
        ubtn.addActionListener(this);

        ujp.add(ujleno);
        ujp.add(ujlname);
        ujp.add(ujlnma);
        ujp.add(ujlajava);
        ujp.add(ujlmcom);
        ujp.add(ujstatuslabel);

        ujp.add(ujceno);
        ujp.add(ujtname);
        ujp.add(ujtnma);
        ujp.add(ujtajava);
        ujp.add(ujtmcom);
        ujp.add(ujstatus);

        ujp.add(ubtn);
        //************ Update jpanel GUI ends ***************//
    }

    //*********** Method to create delete GUI *******************//
    void delete_gui() {
        //************ delete jpanel ***************//
        djp = new JPanel();
        djp.setLayout(null);
        djp.setFont(font);

        djleno = new JLabel("Enrollment No.");
        djleno.setBounds(415, 90, 400, 50);
        djleno.setFont(font2);

        djceno = new JComboBox();
        djceno.setBounds(715, 90, 400, 50);
        djceno.setFont(font2);

        dbtn = new JButton("Delete");
        dbtn.setBounds(550, 200, 400, 50);
        dbtn.setFont(font2);
        dbtn.addActionListener(this);

        djp.add(djleno);
        djp.add(djceno);

        djp.add(dbtn);
    }

    //*********** Method to create retrive GUI *******************//
    void retrive_gui() {
        rjp = new JPanel();
        rjp.setLayout(new BorderLayout());
        rjp.setFont(font);

    }

    //*********** Method for connecting database *******************//
    private void db_connection() {
        dbflag = 0;
        //Databse connection
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ajava", "root", "");
            //ajava is database name
            stmt = con.createStatement();

        } catch (Exception e) {
            dbflag = 1;
            status.setText("Oops! Database not connected :(");
            JOptionPane.showMessageDialog(jf, status);
        }
    }

    //*********** Method for finally in database *******************//
    private void finally_database() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException rsclose) {
                System.out.println("Exception(close rs) " + rsclose);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException stmtclose) {
                System.out.println("Exception(close stmt) " + stmtclose);
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException conclose) {
                System.out.println("Exception(close con) " + conclose);
            }
        }
    }

    //*********** Method for checking student is pass or fail when insert *******************//
    void insert_result() {
            int t1flag, t2flag, t3flag; //thess flags used when pass fail logic apply
            int t1mcheck = 0, t2mcheck = 0, t3mcheck = 0;   //thess flags used when pass fail logic apply
        int t1 = 0, t2 = 0, t3 = 0;
        if (!(ijtajava.getText().equals(""))) {
            try {
                t1 = Integer.parseInt(ijtajava.getText());
            } catch (NumberFormatException ne) {
                t1 = 0;
                t1mcheck = 1;
                ijtajava.setText("");
                ijtajava.requestFocus();
                status.setText("Please enter numeric value :) ");
                JOptionPane.showMessageDialog(jf, status);
            }
            if (t1mcheck != 1) {
                if (!(t1 >= 0 && t1 <= 30)) {
                    t1 = 0;
                    ijtajava.setText("");
                    ijtajava.requestFocus();
                    status.setText("Marks can't be less than 0 or greater than 30 :)   ");
                    JOptionPane.showMessageDialog(jf, status);
                }
            }
        }
        if (!(ijtnma.getText().equals(""))) {
            try {
                t2 = Integer.parseInt(ijtnma.getText());
            } catch (NumberFormatException ne) {
                t2 = 0;
                t2mcheck = 1;
                ijtnma.setText("");
                ijtnma.requestFocus();
                status.setText("Please enter numeric value :) ");
                JOptionPane.showMessageDialog(jf, status);
            }
            if (t2mcheck != 1) {
                if (!(t2 >= 0 && t2 <= 30)) {
                    t2 = 0;
                    ijtnma.setText("");
                    ijtnma.requestFocus();
                    status.setText("Marks can't be less than 0 or greater than 30 :)   ");
                    JOptionPane.showMessageDialog(jf, status);
                }
            }
        }
        if (!(ijtmcom.getText().equals(""))) {
            try {
                t3 = Integer.parseInt(ijtmcom.getText());
            } catch (NumberFormatException ne) {
                t3 = 0;
                t3mcheck = 1;
                ijtmcom.setText("");
                ijtmcom.requestFocus();
                status.setText("Please enter numeric value :) ");
                JOptionPane.showMessageDialog(jf, status);
            }
            if (t3mcheck != 1) {
                if (!(t3 >= 0 && t3 <= 30)) {
                    t3 = 0;
                    ijtmcom.setText("");
                    ijtmcom.requestFocus();
                    status.setText("Marks can't be less than 0 or greater than 30 :)   ");
                    JOptionPane.showMessageDialog(jf, status);
                }
            }
        }

        if (t1 >= 12) {
            t1flag = 1;
        } else {
            t1flag = 0;
        }
        if (t2 >= 12) {
            t2flag = 1;
        } else {
            t2flag = 0;
        }
        if (t3 >= 12) {
            t3flag = 1;
        } else {
            t3flag = 0;
        }
        if (t1flag == 1 && t2flag == 1 && t3flag == 1) {
            ijstatus.setText("pass");
        } else {
            ijstatus.setText("fail");
        }

    }

    //*********** Method for checking student is pass or fail when update *******************//
    void update_result() {
        int t1flag, t2flag, t3flag; //thess flags used when pass fail logic apply
        int t1mcheck = 0, t2mcheck = 0, t3mcheck = 0;   ////these flags for all entered values are integer or not
        int t1 = 0, t2 = 0, t3 = 0;
        if (!(ujtajava.getText().equals(""))) {
            try {
                t1 = Integer.parseInt(ujtajava.getText());
            } catch (NumberFormatException ne) {
                t1 = 0;
                t1mcheck = 1;
                ujtajava.setText("");
                ujtajava.requestFocus();
                status.setText("Please enter numeric value :) ");
                JOptionPane.showMessageDialog(jf, status);
            }
            if (t1mcheck != 1) {
                if (!(t1 >= 0 && t1 <= 30)) {
                    t1 = 0;
                    ujtajava.setText("");
                    ujtajava.requestFocus();
                    status.setText("Marks can't be less than 0 or greater than 30 :)   ");
                    JOptionPane.showMessageDialog(jf, status);
                }
            }
        }
        if (!(ujtnma.getText().equals(""))) {
            try {
                t2 = Integer.parseInt(ujtnma.getText());
            } catch (NumberFormatException ne) {
                t2 = 0;
                t2mcheck = 1;
                ujtnma.setText("");
                ujtnma.requestFocus();
                status.setText("Please enter numeric value :) ");
                JOptionPane.showMessageDialog(jf, status);
            }
            if (t2mcheck != 1) {
                if (!(t2 >= 0 && t2 <= 30)) {
                    t2 = 0;
                    ujtnma.setText("");
                    ujtnma.requestFocus();
                    status.setText("Marks can't be less than 0 or greater than 30 :)   ");
                    JOptionPane.showMessageDialog(jf, status);
                }
            }
        }
        if (!(ujtmcom.getText().equals(""))) {
            try {
                t3 = Integer.parseInt(ujtmcom.getText());
            } catch (NumberFormatException ne) {
                t3 = 0;
                t3mcheck = 1;
                ujtmcom.setText("");
                ujtmcom.requestFocus();
                status.setText("Please enter numeric value :) ");
                JOptionPane.showMessageDialog(jf, status);
            }
            if (t3mcheck != 1) {
                if (!(t3 >= 0 && t3 <= 30)) {
                    t3 = 0;
                    ujtmcom.setText("");
                    ujtmcom.requestFocus();
                    status.setText("Marks can't be less than 0 or greater than 30 :)   ");
                    JOptionPane.showMessageDialog(jf, status);
                }
            }
        }

        if (t1 >= 12) {
            t1flag = 1;
        } else {
            t1flag = 0;
        }
        if (t2 >= 12) {
            t2flag = 1;
        } else {
            t2flag = 0;
        }
        if (t3 >= 12) {
            t3flag = 1;
        } else {
            t3flag = 0;
        }
        if (t1flag == 1 && t2flag == 1 && t3flag == 1) {
            ujstatus.setText("pass");
        } else {
            ujstatus.setText("fail");
        }
    }

    //*********** Method for checking user add right data *******************//
    public void data_validation() {

        //enrollment validation
        long eno;
        int enocheck = 0;   //checking enrollment is numeric or not

        if (!(ijteno.getText().equals(""))) {
            try {
                eno = Long.parseLong(ijteno.getText());
            } catch (NumberFormatException ne) {
                enocheck = 1;
                ijteno.setText("");
                ijteno.requestFocus();
                status.setText("Please enter numeric Enrollment No. :) ");
                JOptionPane.showMessageDialog(jf, status);
            }
            if (enocheck != 1) {
                if (!(ijteno.getText().length() == 12)) {
                    ijteno.setText("");
                    ijteno.requestFocus();
                    status.setText("The Enrollment number must have 12 digits  :)   ");
                    JOptionPane.showMessageDialog(jf, status);
                }
            }
        }

        //update name validation
        String iname = ijtname.getText();
        if (!(iname.equals(""))) {

            if (!(checkValidName(iname))) {
                ijtname.setText("");
                ijtname.requestFocus();
                status.setText("Please enter valid name :) ");
                JOptionPane.showMessageDialog(jf, status);
            }
        }

        //update name validation
        String uname = ujtname.getText();
        if (!(uname.equals(""))) {

            if (!(checkValidName(uname))) {
                ujtname.setText("");
                ujtname.requestFocus();
                status.setText("Please enter valid name. :) ");
                JOptionPane.showMessageDialog(jf, status);
            }
        }
    }

    //*********** Method for checking student is pass or fail when update *******************//
    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {

        data_validation();
        insert_result();
        update_result();
    }

    //*********** Method for checking String is valid name or not *******************//
    public static boolean checkValidName(String name) {
        for (int i = 0; i < name.length(); ++i) {
            char ch = name.charAt(i);

            // If the character is not a letter and not a whitespace, return false.
            if (!Character.isLetter(ch) && !Character.isWhitespace(ch)) {
                return false;
            }
        }
        return true;
    }
}
