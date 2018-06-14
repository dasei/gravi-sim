package window;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.Main;
import physics.Body;

public class WindowOptions extends JFrame{
	
	private static final long serialVersionUID = 1L;

	private JPanel pOptionContainer;
	
	private JComboBox<Body> boxBodyList;
	
	public WindowOptions() {
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setTitle("Gravitos Options");		
		
		
		//main option container
		pOptionContainer = new JPanel();
		pOptionContainer.setLayout(new BoxLayout(pOptionContainer, BoxLayout.Y_AXIS));
		this.add(pOptionContainer);
				
		
		
		//TODO Panel for choosing background color
		
		
//		JPanel pBodyList = new JPanel(new GridBagLayout());
		JPanel pBodyList = new JPanel();
		pBodyList.setAlignmentX(Component.LEFT_ALIGNMENT);
		pBodyList.setPreferredSize(new Dimension(300, 50));
//		GridBagConstraints c = ((GridBagLayout) pBodyList.getLayout()).getConstraints(pBodyList);
//			c.gridy = 0;
//			c.gridx = GridBagConstraints.RELATIVE;
			boxBodyList = new JComboBox<Body>();
//			boxBodyList.setAlignmentX(Component.CENTER_ALIGNMENT);
			pBodyList.add(boxBodyList);
			
			JButton bBodyListCenterOn = new JButton("\\/");
			bBodyListCenterOn.addActionListener(new ActionListener() {				
				public void actionPerformed(ActionEvent e) {
					Main.getController().getWindow().getDrawComp()
						.centerCamera(
								((Body) boxBodyList.getSelectedItem()).x,
								((Body) boxBodyList.getSelectedItem()).y
						);
				}
			});
			pBodyList.add(bBodyListCenterOn);
			
			JButton bBodyListStartFollow = new JButton("follow");
			bBodyListStartFollow.addActionListener(new ActionListener() {				
				public void actionPerformed(ActionEvent e) {
					Main.getController().follow((Body) boxBodyList.getSelectedItem());					
				}
			});
			pBodyList.add(bBodyListStartFollow);
		pOptionContainer.add(pBodyList);
			
		
		
		
		
//		addOptionButton("center", new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				System.out.println("button pressed!");
//			}
//		});
		
		
		
		
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);		
	}
	
	
	
	private void addOptionButton(String name, ActionListener actionListener) {
		JButton button = new JButton(name);
		button.addActionListener(actionListener);
		this.pOptionContainer.add(button);
	}
	
	//----------------------------------------------------
	// Update methods
	
	public void updateBodyList() {
		Object bodySelected = this.boxBodyList.getSelectedItem();
		
		this.boxBodyList.removeAllItems();
		
		for(Body body : Main.getController().getBodies())			
			this.boxBodyList.addItem(body);
		
		if(Main.getController().getBodies().contains(bodySelected))
			this.boxBodyList.setSelectedItem(bodySelected);
	} 
}
