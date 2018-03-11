package client;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import client.Client.usernameException;

public class ClientMenu {

	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientMenu window = new ClientMenu();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientMenu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 790, 367);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblInsertYourUsername = new JLabel("Insert Your Username : ");
		lblInsertYourUsername.setFont(new Font("Tahoma", Font.PLAIN, 28));
		lblInsertYourUsername.setBounds(12, 233, 312, 86);
		frame.getContentPane().add(lblInsertYourUsername);

		textField = new JTextField();
		textField.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		textField.setBounds(322, 256, 312, 48);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		JButton btnJoin = new JButton("Join");

		btnJoin.setFont(new Font("Source Sans Pro Semibold", Font.BOLD, 22));
		btnJoin.setBounds(656, 255, 116, 48);
		frame.getContentPane().add(btnJoin);

		JLabel lblChooseAServer = new JLabel("Choose a server to connect to: ");
		lblChooseAServer.setFont(new Font("Tahoma", Font.PLAIN, 28));
		lblChooseAServer.setBounds(12, 13, 400, 99);
		frame.getContentPane().add(lblChooseAServer);

		JRadioButton rdbtnServerport_1 = new JRadioButton("Server #1 (port 5555)");
		rdbtnServerport_1.setFont(new Font("PT Serif", Font.BOLD, 16));
		JRadioButton rdbtnServerport_2 = new JRadioButton("Server #2 (port 4444)");
		rdbtnServerport_2.setFont(new Font("PT Serif", Font.BOLD, 16));
		ButtonGroup bG = new ButtonGroup();
		bG.add(rdbtnServerport_1);
		bG.add(rdbtnServerport_2);
		rdbtnServerport_1.setSelected(true);
		rdbtnServerport_1.setBounds(420, 55, 204, 25);
		frame.getContentPane().add(rdbtnServerport_1);
		rdbtnServerport_2.setBounds(420, 110, 189, 25);
		frame.getContentPane().add(rdbtnServerport_2);

		btnJoin.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				try {
					String username = textField.getText();
					if (username == null || username.equals("")) {
						JOptionPane.showMessageDialog(null, "Username cannot be empty!");
						return;
					}
					int port = (rdbtnServerport_1.isSelected()) ? 5555 : 4444;
					Client client;
					try {
						client = new Client(username, port);
					} catch (usernameException e) {
						JOptionPane.showMessageDialog(null, "Username already in use!");
						return;
					}
					ClientGUI clientGUI = new ClientGUI(client);
					frame.dispose();
				} catch (Exception e) {

				}
			}
		});

	}
}
