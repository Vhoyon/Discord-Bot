package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import vendor.modules.Environment;
import vendor.modules.Logger;
import vendor.objects.LoggableJTextArea;

public abstract class MainConsole extends JFrame {
	
	//	private MainConsole console;
	
	private JButton actionButton;
	
	private LoggableJTextArea log;
	
	public MainConsole(boolean isDebug){
		
		super();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				onStop();
				
				super.windowClosing(e);
			}
		});
		
		setSize(500, 300);
		setLocationRelativeTo(null);
		
		JPanel panel = new JPanel(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		int padding = 10;
		c.insets = new Insets(padding, padding, padding, padding);
		
		actionButton = new JButton("Start");
		
		actionButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				
				JButton button = (JButton)e.getSource();
				
				button.setEnabled(false);
				
				if(button.getText().equals("Start")){
					
					new Thread(){
						@Override
						public void run(){
							onStart();
							
							button.setText("Stop");
							button.setEnabled(true);
						}
					}.start();
					
				}
				else{
					
					new Thread(){
						@Override
						public void run(){
							onStop();
							
							button.setText("Start");
							button.setEnabled(true);
						}
					}.start();
					
				}
				
			}
		});
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 1;
		c.ipady = 50;
		c.gridx = 2;
		c.gridy = 4;
		
		panel.add(actionButton, c);
		
		log = new LoggableJTextArea(1, 0);
		Logger.setOutputs(log);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 3;
		c.gridwidth = 4;
		
		panel.add(log.scrollPane, c);
		
		add(panel);
		
		setVisible(true);
		
		if(isDebug){
			String clientId = Environment.getVar("CLIENT_ID", null);
			
			if(clientId != null){
				Logger.log("Link to join the bot to a server :\n\n"
						+ "https://discordapp.com/oauth2/authorize?&client_id="
						+ clientId + "&scope=bot&permissions=0", false);
			}
		}
		
	}
	
	public abstract void onStart();
	
	public abstract void onStop();
	
}
