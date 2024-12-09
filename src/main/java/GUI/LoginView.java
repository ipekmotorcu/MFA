package GUI;
import javax.swing.*;
public class LoginView extends JFrame{

 /*   public static void main(String[] args) {
        System.out.println("Project is set up!");
        LoginView view = new LoginView(); }
 */
    JLabel userLabel,passwordLabel;
    JTextField userText;
    JTextField passwordText;
    JButton loginButton;

    public JTextField getUserText() {
        return userText;
    }
    public JTextField getPasswordText() {
        return passwordText;
    }
    public JButton getLoginButton() {
        return loginButton;
    }

    public LoginView(){
        this.setTitle("Sign In");
        JPanel loginPanel = new JPanel();

        userLabel=new JLabel("User Name");
        userLabel.setBounds(20,99,80,25);
        loginPanel.add(userLabel);
        loginPanel.setLayout(null);

        userText= new JTextField(20);
        userText.setBounds(100,100,165,25);
        loginPanel.add(userText);

        passwordLabel=new JLabel("Password");
        passwordLabel.setBounds(20,130,80,25);
        loginPanel.add(passwordLabel);

        passwordText= new JPasswordField(20);
        passwordText.setBounds(100,132,165,25);
        loginPanel.add(passwordText);

        loginButton = new JButton("LOGIN");
        loginButton.setBounds(100, 200, 160, 25);
        loginPanel.add(loginButton);

        this.setContentPane(loginPanel);
       // this.pack();
        this.setSize(400,400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
      //  this.validate();
    }
}
