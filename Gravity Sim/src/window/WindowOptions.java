package window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import main.Main;
import physics.Body;

public class WindowOptions extends JFrame{
	
	private static final long serialVersionUID = 1L;

	private JPanel pOptionContainer;
	
	private JComboBox<Body> boxBodyList;
	
	//pOptionPause
	private JButton bPauseResume;
	private ImageIcon iconPause;
	private ImageIcon iconResume;
	
	
	
	public WindowOptions() {
		loadIcons();
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setTitle("Gravitos Options");		
		
		//utility components
		JLabel lUtilitySeperator = new JLabel("||");		
		
		
		//main option container
		pOptionContainer = new JPanel();
		pOptionContainer.setLayout(new BoxLayout(pOptionContainer, BoxLayout.Y_AXIS));
		this.add(pOptionContainer);
				
		
		//TODO Button to start analyzation		
		
		//TODO (optional) Option for setting buttons on DrawComp (in)visible
		
		//----------------------------------------------------------------------------------------------------
		//Option regarding bodies
		JPanel pBodyList = new JPanel();
//		pBodyList.setAlignmentX(Component.LEFT_ALIGNMENT);
		pBodyList.setPreferredSize(new Dimension(350, 50));
			boxBodyList = new JComboBox<Body>();
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
			
			JButton bBodyListStopFollow = new JButton("unfollow");
			bBodyListStopFollow.addActionListener(new ActionListener() {				
				public void actionPerformed(ActionEvent e) {
					Main.getController().follow(null);					
				}
			});
			pBodyList.add(bBodyListStopFollow);
		pOptionContainer.add(pBodyList);
			
		
		//----------------------------------------------------------------------------------------------------
		//Option for pausing and resuming simulation
		JPanel pOptionPause = new JPanel();
			bPauseResume = new JButton(iconPause);
			
			bPauseResume.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Main.getController().pauseOrResume();
					updatePauseResumeButton();
				}
			});
			pOptionPause.add(bPauseResume);
		pOptionContainer.add(pOptionPause);
		
		
		
		
		//----------------------------------------------------------------------------------------------------
		//Option regarding colors of DrawComp
		JPanel pOptionColor = new JPanel();
			JLabel lOptionColor = new JLabel("ColorsColor:");
			pOptionColor.add(lOptionColor);
		
			JButton bOptionColorReset = new JButton("reset");
			bOptionColorReset.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Main.getController().getWindow().getDrawComp().loadDefaultColorPreset();
				}
			});
			pOptionColor.add(bOptionColorReset);
			
			pOptionColor.add(lUtilitySeperator);
			
			//Background
			JButton bColorChooserBackground = new JButton("Background");
			bColorChooserBackground.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Color c = JColorChooser.showDialog(Main.getController().getWindowOptions(), "Background Color", Main.getController().getWindow().getDrawComp().getColorBackground());
					if(c != null)
						Main.getController().getWindow().getDrawComp().setColorBackground(c);
				}
			});
			pOptionColor.add(bColorChooserBackground);
			
			//Midground
			JButton bColorChooserMidground = new JButton("Midground");
			bColorChooserMidground.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Color c = JColorChooser.showDialog(Main.getController().getWindowOptions(), "Midground Color", Main.getController().getWindow().getDrawComp().getColorMidground());
					if(c != null)
						Main.getController().getWindow().getDrawComp().setColorMidground(c);
				}
			});
			pOptionColor.add(bColorChooserMidground);
			
			//Foreground
			JButton bColorChooserForeground = new JButton("Foreground");
			bColorChooserForeground.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Color c = JColorChooser.showDialog(Main.getController().getWindowOptions(), "Foreground Color", Main.getController().getWindow().getDrawComp().getColorForeground());
					if(c != null)
						Main.getController().getWindow().getDrawComp().setColorForeground(c);					
				}
			});
			pOptionColor.add(bColorChooserForeground);
			
			//Button for printing rgb values to console
			JButton bColorChooserPrintValues = new JButton("> print");
			bColorChooserPrintValues.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					DrawComp dc = Main.getController().getWindow().getDrawComp();
					JOptionPane.showMessageDialog(Main.getController().getWindowOptions(), 
							"background: " + dc.getColorBackground().getRed() + ", " + dc.getColorBackground().getGreen() + ", " + dc.getColorBackground().getBlue() + "\n"
							+ "midground: " + dc.getColorMidground().getRed() + ", " + dc.getColorMidground().getGreen() + ", " + dc.getColorMidground().getBlue() + "\n"
							+ "foreground: " + dc.getColorForeground().getRed() + ", " + dc.getColorForeground().getGreen() + ", " + dc.getColorForeground().getBlue() + "\n",
						"rgb values", JOptionPane.INFORMATION_MESSAGE);					
				}
			});
			pOptionColor.add(bColorChooserPrintValues);
		pOptionContainer.add(pOptionColor);
		
		
		
		//----------------------------------------------------------------------------------------------------
		//Option for selecting Color Presets
		JPanel pOptionColorPresets = new JPanel();
			JButton bColorPresetLight = new JButton("Light");
			bColorPresetLight.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Main.getController().getWindow().getDrawComp().loadColorPreset(DrawComp.ColorPreset.LIGHT);
				}
			});
			pOptionColorPresets.add(bColorPresetLight);
			
			JButton bColorPresetDark = new JButton("Dark");
			bColorPresetDark.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Main.getController().getWindow().getDrawComp().loadColorPreset(DrawComp.ColorPreset.DARK);
				}
			});
			pOptionColorPresets.add(bColorPresetDark);
			
			JButton bColorPresetRed = new JButton("Red");
			bColorPresetRed.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Main.getController().getWindow().getDrawComp().loadColorPreset(DrawComp.ColorPreset.RED);
				}
			});
			pOptionColorPresets.add(bColorPresetRed);
		pOptionContainer.add(pOptionColorPresets);
		
		
		//----------------------------------------------------------------------------------------------------
		//Option for checkboxes
		JPanel pOptionCheckboxes1 = new JPanel();
			JCheckBox checkboxOptionDrawBodyInfotags = new JCheckBox("Info Tags", DrawComp.drawBodyInfoTagsOnDefault);
			checkboxOptionDrawBodyInfotags.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Main.getController().getWindow().getDrawComp().setDrawBodyInfoTags(checkboxOptionDrawBodyInfotags.isSelected());
				}
			});
			pOptionCheckboxes1.add(checkboxOptionDrawBodyInfotags);
			
			JCheckBox checkboxOptionDrawEllipseFocusPoints = new JCheckBox("Focus Points", DrawComp.drawBodyInfoTagsOnDefault);
			checkboxOptionDrawEllipseFocusPoints.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Main.getController().getWindow().getDrawComp().setDrawFocusPoints(checkboxOptionDrawEllipseFocusPoints.isSelected());
				}
			});
			pOptionCheckboxes1.add(checkboxOptionDrawEllipseFocusPoints);
		pOptionContainer.add(pOptionCheckboxes1);
		
		
		
		this.pack();
		//create rigid area so that the other components gets pushed upwards all the way
		pOptionContainer.add(Box.createRigidArea(new Dimension(0, Integer.MAX_VALUE)));
		this.setLocationRelativeTo(null);
		this.setVisible(false);		
	}
	
	private void addOptionButton(String name, ActionListener actionListener) {
		JButton button = new JButton(name);
		button.addActionListener(actionListener);
		this.pOptionContainer.add(button);
	}
	
	
	private void loadIcons() {
		iconPause = new ImageIcon("res/icons/icon_pause.png", "pause");
		iconResume = new ImageIcon("res/icons/icon_resume.png", "resume");		
	}
	
	//----------------------------------------------------
	// Update methods
	
	public void onAnalyzationFinish() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				updatePauseResumeButton();
			}
		});
	}
	
	public void updateBodyList() {
		Object bodySelected = this.boxBodyList.getSelectedItem();
		
		this.boxBodyList.removeAllItems();
		
		for(Body body : Main.getController().getBodies())			
			this.boxBodyList.addItem(body);
		
		if(Main.getController().getBodies().contains(bodySelected))
			this.boxBodyList.setSelectedItem(bodySelected);
	} 
	
	private void updatePauseResumeButton() {
		switch(Main.getController().getSimulationState()) {
		case PAUSED:
			//bPauseResume.setText("resume");
			bPauseResume.setIcon(iconResume);
			bPauseResume.setEnabled(true);
			break;
		case SIMULATING:
//			bPauseResume.setText("pause");
			bPauseResume.setIcon(iconPause);
			bPauseResume.setEnabled(true);
			break;		
		default:
			bPauseResume.setEnabled(false);
			break;
		}
	}	
	
}
