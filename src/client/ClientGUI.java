package client;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class ClientGUI {

	private JFrame frmChatApplication;
	public JTextField userInput;
	JTextPane msgArea;
	Client client;
	private JButton refreshBtn;
	Choice choice_member;

	/**
	 * Create the application.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public ClientGUI(Client client) {
		this.client = client;
		initialize();
		this.frmChatApplication.setVisible(true);
		client.setGUI(this);

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChatApplication = new JFrame();
		frmChatApplication.setTitle("Chat application");
		frmChatApplication.setResizable(false);
		frmChatApplication.setBounds(100, 100, 817, 521);
		frmChatApplication.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmChatApplication.getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 63, 535, 302);
		frmChatApplication.getContentPane().add(scrollPane);

		msgArea = new JTextPane();
		scrollPane.setViewportView(msgArea);
		msgArea.setEditable(false);

		userInput = new JTextField();
		userInput.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
		userInput.setBounds(12, 420, 535, 58);
		frmChatApplication.getContentPane().add(userInput);
		userInput.setColumns(10);

		choice_member = new Choice();
		choice_member.setBounds(589, 215, 175, 67);
		frmChatApplication.getContentPane().add(choice_member);

		JButton sendButton = new JButton("Send !");
		sendButton.setBounds(589, 420, 175, 58);
		sendButton.setBackground(Color.LIGHT_GRAY);
		sendButton.setFont(new Font("Verdana", Font.BOLD, 24));
		frmChatApplication.getContentPane().add(sendButton);

		refreshBtn = new JButton("Refresh Memberlist");
		refreshBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		refreshBtn.setBounds(589, 130, 175, 27);
		frmChatApplication.getContentPane().add(refreshBtn);

		JLabel lblOnlineMembers = new JLabel("Online members");
		lblOnlineMembers.setFont(new Font("Source Sans Pro Black", Font.ITALIC, 23));
		lblOnlineMembers.setBounds(589, 83, 175, 42);
		frmChatApplication.getContentPane().add(lblOnlineMembers);

		JLabel lblChooseTtl = new JLabel("Choose TTL: ");
		lblChooseTtl.setFont(new Font("Source Sans Pro Black", Font.PLAIN, 13));
		lblChooseTtl.setBounds(589, 321, 125, 16);
		frmChatApplication.getContentPane().add(lblChooseTtl);

		JSeparator separator = new JSeparator();
		separator.setBounds(589, 292, 175, 16);
		frmChatApplication.getContentPane().add(separator);

		JLabel lblChooseMemberTo = new JLabel("Choose a member to chat:");
		lblChooseMemberTo.setFont(new Font("Source Sans Pro", Font.BOLD, 13));
		lblChooseMemberTo.setBounds(589, 182, 175, 27);
		frmChatApplication.getContentPane().add(lblChooseMemberTo);

		Choice choice_TTL = new Choice();
		choice_TTL.setBounds(589, 343, 175, 22);
		frmChatApplication.getContentPane().add(choice_TTL);

		JLabel currentUser = new JLabel("Username: " + client.username);
		currentUser.setFont(new Font("Verdana", Font.BOLD, 20));
		currentUser.setBounds(168, 8, 379, 42);
		frmChatApplication.getContentPane().add(currentUser);

		JButton btnQuit = new JButton("Quit");

		btnQuit.setFont(new Font("Times New Roman", Font.BOLD, 31));
		btnQuit.setBackground(Color.RED);
		btnQuit.setBounds(589, 8, 175, 62);
		frmChatApplication.getContentPane().add(btnQuit);
		
		JLabel lblTypeYourMessage = new JLabel("Type your message here: ");
		lblTypeYourMessage.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
		lblTypeYourMessage.setBounds(12, 378, 219, 29);
		frmChatApplication.getContentPane().add(lblTypeYourMessage);
		
		JLabel lblChatWindow = new JLabel("Chat Window");
		lblChatWindow.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblChatWindow.setBounds(12, 33, 116, 22);
		frmChatApplication.getContentPane().add(lblChatWindow);
		choice_TTL.add("1");
		choice_TTL.add("2");
		choice_TTL.add("3");
		choice_TTL.add("4");

		sendButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					String dest = choice_member.getSelectedItem();
					if (dest == null) {
						JOptionPane.showMessageDialog(null, "Please choose a user to message");
						return;
					}

					msgArea.setText(msgArea.getText() + '\n' + "YOU : " + userInput.getText());
					String messageToBeSent = "Chat(" + client.username + "," + dest + "," + userInput.getText() + ","
							+ choice_TTL.getSelectedItem() + ")";
					client.sendMessage(messageToBeSent);
					userInput.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});

		refreshBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					client.sendMessage("GetMemberList()");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
//				refreshBtn.setEnabled(false);
//				choice_member.removeAll();
//				try {
//					String[] list = client.getMemberList();
//					for (int i = 0; i < list.length; i++) {
//						if (!list[i].equals(client.username))
//							choice_member.add(list[i]);
//					}
//				} catch (IOException | InterruptedException e1) {
//					e1.printStackTrace();
//				}
//				refreshBtn.setEnabled(true);
			}
		});

		btnQuit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					client.sendMessage("Quit");
					JOptionPane.showMessageDialog(null, "Thanks for using the application :)");
					frmChatApplication.dispose();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

	}
}
