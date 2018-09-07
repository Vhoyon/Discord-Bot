package consoles;

import vendor.abstracts.AbstractUIConsole;
import vendor.modules.Logger;
import vendor.objects.LoggableJTextArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Vhoyon's custom implementation of the
 * {@link vendor.abstracts.AbstractUIConsole AbstractUIConsole} that allows us
 * to get a visual console for managing our bot's state.
 *
 * @version 1.0
 * @since v0.4.0
 * @author V-ed (Guillaume Marcoux)
 * @see vendor.abstracts.AbstractUIConsole
 */
public abstract class UIConsole extends AbstractUIConsole {
	
	private JButton actionButton;
	
	private LoggableJTextArea log;
	
	/**
	 * Constructor that simply calls {@link AbstractUIConsole}'s
	 * {@link AbstractUIConsole#AbstractUIConsole() constructor}.
	 *
	 * @since v0.4.0
	 */
	public UIConsole(){
		super();
	}
	
	@Override
	public void initialize(){
		
		setSize(500, 300);
		
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
							
							try{
								onStart();
								
								button.setText("Stop");
							}
							catch(Exception e){
								Logger.log(e);
							}
							
							button.setEnabled(true);
							
						}
					}.start();
					
				}
				else{
					
					new Thread(){
						@Override
						public void run(){
							
							try{
								onStop();
								
								button.setText("Start");
							}
							catch(Exception e){
								Logger.log(e);
							}
							
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
		
		onInitialized();
		
	}
	
	@Override
	public void onExit(){}
	
}
