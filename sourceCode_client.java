package client;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;
import java.awt.event.*;
import java.awt.*;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
public class client {
    public class ancestor extends JFrame {
        protected JPanel jPanel = new JPanel();
        protected Socket s = null;
        protected OutputStream os = null;
        protected PrintStream ps = null;
        protected InputStream is = null;
        protected BufferedReader br = null;
        protected String account = null;
        public ancestor(String str) {
            super(str);
            account = str;
            this.setTitle(str);
            this.setVisible(true);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setResizable(false);
            try {
                s = new Socket(ConstVarible.address_home_wifi, ConstVarible.port);
                os = s.getOutputStream();
                ps = new PrintStream(os);
                is = s.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    public class login extends ancestor implements ActionListener, FocusListener {
        private GridLayout gridLayout = new GridLayout(3, 2, 20, 20);
        private JLabel label_name = new JLabel("??????");
        private JLabel label_password = new JLabel("??????");
        private JTextField input_name = new JTextField("????????????");
        private JPasswordField input_password = new JPasswordField("????????????");
        private JButton registerButton = new JButton("?????????");
        private JButton loginButton = new JButton("??????");
        public login() {
            super("????????????");
            jPanel.setLayout(gridLayout);
            jPanel.add(label_name);
            jPanel.add(input_name);
            jPanel.add(label_password);
            jPanel.add(input_password);
            jPanel.add(loginButton);
            jPanel.add(registerButton);
            this.add(jPanel);
            this.setSize(400, 300);
            this.setLocationRelativeTo(null);
            registerButton.addActionListener(this);
            loginButton.addActionListener(this);
            input_name.addFocusListener(this);
            input_password.addFocusListener(this);
            input_password.addActionListener(this);
        }
        public void actionPerformed(ActionEvent e) {
            JButton jButton = new JButton("jButton");
            try {
                jButton = (JButton) e.getSource();
            } catch (Exception ex) {
                log();
                return;
            }
            if (jButton.getText().compareTo("?????????") == 0) {
                new client().new register();
                this.dispose();
            } else {
                log();
            }
        }
        public void log() {
            String realPassword = null;
            try {
                ps.println(ConstVarible.startFlag + "login");
                ps.println(input_name.getText());
                realPassword = br.readLine();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (realPassword.compareTo(String.valueOf(input_password.getPassword())) == 0) {
                JOptionPane.showMessageDialog(null, "????????????");
                new client().new mainInterface(input_name.getText());
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "??????????????????????????????");
                new client().new login();
                this.dispose();
            }
        }
        public void focusGained(FocusEvent fe) {
            if (input_name.isFocusOwner() == true) {
                input_name.setText("");
            }
            if (input_password.isFocusOwner() == true) {
                input_password.setText("");
            }
        }
        public void focusLost(FocusEvent fe) {
            if (input_name.isFocusOwner() == false) {
            }
            if (input_password.isFocusOwner() == false) {
            }
        }
    }
    public class register extends ancestor implements ActionListener, FocusListener {
        private GridLayout gridLayout = new GridLayout(4, 2, 20, 20);
        private JLabel label_name = new JLabel("??????");
        private JLabel label_password = new JLabel("??????");
        private JLabel label_password_again = new JLabel("????????????");
        private JTextField input_name = new JTextField("????????????");
        private JPasswordField input_password = new JPasswordField("????????????");
        private JPasswordField input_password_again = new JPasswordField("????????????");
        private JButton registerButton = new JButton("??????");
        private JButton cancelButton = new JButton("??????");
        public register() {
            super("????????????");
            input_name.addFocusListener(this);
            input_password.addFocusListener(this);
            input_password_again.addFocusListener(this);
            jPanel.setLayout(gridLayout);
            jPanel.add(label_name);
            jPanel.add(input_name);
            jPanel.add(label_password);
            jPanel.add(input_password);
            jPanel.add(label_password_again);
            jPanel.add(input_password_again);
            jPanel.add(registerButton);
            jPanel.add(cancelButton);
            this.add(jPanel);
            this.setSize(500, 400);
            this.setLocationRelativeTo(null);
            registerButton.addActionListener(this);
            cancelButton.addActionListener(this);
        }
        public void actionPerformed(ActionEvent e) {
            JButton jButton = (JButton) e.getSource();
            if (jButton.getText().compareTo("??????") == 0) {
                if (String.valueOf(input_password.getPassword())
                        .compareTo(String.valueOf(input_password_again.getPassword())) == 0) {
                    try {
                        os = s.getOutputStream();
                        ps = new PrintStream(os);
                        ps.println(ConstVarible.startFlag + "register");
                        ps.println(input_name.getText());
                        ps.println(String.valueOf(input_password.getPassword()));
                        is = s.getInputStream();
                        br = new BufferedReader(new InputStreamReader(is));
                        if (br.readLine().compareTo("success") == 0) {
                            JOptionPane.showMessageDialog(this, "????????????");
                            new client().new mainInterface(input_name.getText());
                            this.dispose();
                        } else {
                            JOptionPane.showMessageDialog(this, "??????????????????");
                            new client().new register();
                            this.dispose();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        try {
                            br.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "????????????????????????????????????????????????");
                }
            } else if (jButton.getText().compareTo("??????") == 0) {
                new client().new login();
                this.dispose();
            }
        }
        public void focusGained(FocusEvent fe) {
            if (input_name.isFocusOwner() == true) {
                input_name.setText("");
            }
            if (input_password.isFocusOwner() == true) {
                input_password.setText("");
            }
            if (input_password_again.isFocusOwner() == true) {
                input_password_again.setText("");
            }
        }
        public void focusLost(FocusEvent fe) {
        }
    }
    public class mainInterface extends ancestor implements ActionListener, Runnable {
        private JTextArea taMsg = new JTextArea("?????????????????????\n");
        private JTextField tfMsg = new JTextField();
        private JScrollPane newJScrollPane = new JScrollPane(taMsg);
        private JMenuBar jMenuBar = new JMenuBar();
        private JMenu friends = new JMenu("??????");
        private JMenuItem makefriends = new JMenuItem("????????????");
        private JMenuItem listfriends = new JMenuItem("????????????");
        private JMenuItem chatfriends = new JMenuItem("????????????");
        private JMenuItem refreshfriends = new JMenuItem("????????????");
        public mainInterface(String str) {
            super(str);
            account = str;
            BorderLayout borderLayout = new BorderLayout();
            jPanel.setLayout(borderLayout);
            jPanel.add(newJScrollPane, BorderLayout.CENTER);
            jPanel.add(tfMsg, BorderLayout.SOUTH);
            jPanel.add(jMenuBar, BorderLayout.NORTH);
            jMenuBar.add(friends);
            friends.add(makefriends);
            friends.add(listfriends);
            friends.add(chatfriends);
            friends.add(refreshfriends);
            Font font = new Font(ConstVarible.font_String, ConstVarible.font_style, ConstVarible.font_size);
            taMsg.setFont(font);
            tfMsg.setFont(font);
            taMsg.setEditable(false);
            tfMsg.setBackground(Color.yellow);
            tfMsg.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ps.println(ConstVarible.startFlag + "send");
                    try {
                        ps.println(account + "???:" + tfMsg.getText());
                        tfMsg.setText("");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            makefriends.addActionListener(this);
            listfriends.addActionListener(this);
            chatfriends.addActionListener(this);
            refreshfriends.addActionListener(this);
            this.add(jPanel);
            this.setSize(700, 500);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            new Thread(this).start();
        }
        public void run() {
            try {
                while (true) {
                    String str = br.readLine();
                    if(str.compareTo(ConstVarible.startFlag+"chatprivate")==0){
                        String string=br.readLine();
                        chatprivate cp= new chatprivate(account, string);
                        System.out.println("hhh");
                    }
                    taMsg.append(str + "\n");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        public void actionPerformed(ActionEvent ae) {
            JMenuItem jMenuItem = (JMenuItem) ae.getSource();
            if (jMenuItem.getText().compareTo("????????????") == 0) {
                new makefriend(account);
            } else if (jMenuItem.getText().compareTo("????????????") == 0) {
                new chatfriend(account);
            } else if (jMenuItem.getText().compareTo("????????????") == 0) {
                new refreshfriend(account);
            } else if (jMenuItem.getText().compareTo("????????????") == 0) {
                new listfriend(account);
            }
        }
    }
    public class makefriend extends ancestor {
        public makefriend(String account) {
            super("??????");
            this.setVisible(false);
            String friendAccount = JOptionPane.showInputDialog(this, "?????????????????????");
            if (friendAccount == null || friendAccount.compareTo("") == 0) {
                return;
            } else {
                ps.println(ConstVarible.startFlag + "makefriends");
                ps.println(account);
                ps.println(friendAccount);
                try {
                    JOptionPane.showMessageDialog(this, br.readLine());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    public class listfriend extends ancestor {
        protected JTable jTable = null;
        protected JScrollPane jScrollPane = null;
        protected Vector<Vector<String>> data = new Vector<>();
        protected Vector<String> columnName = new Vector<>();
        public listfriend(String account) {
            super(account);
            this.setTitle("????????????");
            ps.println(ConstVarible.startFlag + "list");
            ps.println(account);
            ObjectInputStream objectInputStream = null;
            try {
                objectInputStream = new ObjectInputStream(is);
                data = (Vector<Vector<String>>) objectInputStream.readObject();
            } catch (Exception ex) {
                System.out.println("objecetInputStream");
                ex.printStackTrace();
            }
            FlowLayout flowLayout = new FlowLayout();
            jPanel.setLayout(flowLayout);
            columnName.add("????????????");
            jTable = new JTable(data, columnName){
                public boolean isCellEditable(int row,int column){
                    return false;
                }
            };
            jTable.setPreferredScrollableViewportSize(new Dimension(600, 450));
            Font font = new Font(ConstVarible.font_String, ConstVarible.font_style, ConstVarible.font_size);
            jTable.setFont(font);
            jTable.setFillsViewportHeight(true);
            jScrollPane = new JScrollPane(jTable);
            jPanel.add(jScrollPane);
            this.add(jPanel);
            this.setSize(700, 500);
            this.setLocationRelativeTo(null);
        }
    }
    public class chatfriend extends ancestor implements ListSelectionListener {
        protected JTable jTable = null;
        protected JScrollPane jScrollPane = null;
        protected Vector<Vector<String>> data = new Vector<>();
        protected Vector<String> columnName = new Vector<>();
        public chatfriend(String account) {
            super(account);
            this.setTitle("??????????????????");
            ps.println(ConstVarible.startFlag + "chat");
            ps.println(account);
            columnName.add("????????????");
            while (true) {
                try {
                    String string = br.readLine();
                    if (string.compareTo(ConstVarible.endFlag)==0) {
                        break;
                    }
                    Vector<String> row = new Vector<>();
                    row.add(string);
                    data.add(row);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            FlowLayout flowLayout = new FlowLayout();
            jPanel.setLayout(flowLayout);
            jTable = new JTable(data, columnName){
                public boolean isCellEditable(int row,int column){
                    return false;
                }
            };
            jTable.setPreferredScrollableViewportSize(new Dimension(600, 450));
            Font font = new Font(ConstVarible.font_String, ConstVarible.font_style, ConstVarible.font_size);
            jTable.setFont(font);
            jTable.setFillsViewportHeight(true);
            jTable.getSelectionModel().addListSelectionListener(this);
            jScrollPane = new JScrollPane(jTable);
            jPanel.add(jScrollPane);
            this.add(jPanel);
            this.setSize(700, 500);
            this.setLocationRelativeTo(null);
        }
        public void valueChanged(ListSelectionEvent le) {
            int row = jTable.getSelectedRow();
            if (le.getValueIsAdjusting() == true) {
                ps.println(ConstVarible.startFlag+"chatprivate");
                ps.println(account);
                ps.println(data.get(row).get(0));
                new chatprivate(account,data.get(row).get(0));
            }
        }
    }
    public class chatprivate extends mainInterface{
        public chatprivate(String account,String friendAccount){
            super(account);
            this.setTitle(account+"???"+friendAccount+"?????????");
        }
    }
    public class refreshfriend extends ancestor {
        public refreshfriend(String account) {
            super("??????");
            this.setVisible(false);
        }
    }
    public static void main(String[] args) {
        new client().new login();
    }
}