/* FeatureIDE - A Framework for Feature-Oriented Software Development
 * Copyright (C) 2005-2019  FeatureIDE team, University of Magdeburg, Germany
 *
 * This file is part of FeatureIDE.
 * 
 * FeatureIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * FeatureIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with FeatureIDE.  If not, see <http://www.gnu.org/licenses/>.
 *
 * See http://featureide.cs.ovgu.de/ for further information.
 */
package de.ovgu.featureide.examples.elevator.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JToggleButton;
import de.ovgu.featureide.examples.elevator.core.controller.Request;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.ovgu.featureide.examples.elevator.sim.SimulationUnit;

import de.ovgu.featureide.examples.elevator.core.model.ElevatorState;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class FloorComposite extends JPanel
										implements ActionListener 
										{
	
	private static final long serialVersionUID = 4452235677942989047L;

	private final static ImageIcon img_open  = new ImageIcon(FloorComposite.class.getResource("/floor_open.png"));
	private final static ImageIcon img_close = new ImageIcon(FloorComposite.class.getResource("/floor_close.png"));

	private final JLabel lblFloorImage;
	private boolean showsOpen = false;
	private JLabel lblLevel;
	private int level;
	private SimulationUnit simulation;

	private JToggleButton btnFloorUp, btnFloorDown;
	
	private Color cElevatorIsPresent = UIManager.getDefaults().getColor("Button.select");

	public FloorComposite(boolean showsOpen, int level
													, SimulationUnit simulation
													, boolean isMaxLevel
													) {
		setAlignmentY(Component.BOTTOM_ALIGNMENT);
		setMinimumSize(new Dimension(10, 100));
		setMaximumSize(new Dimension(400, 100));
		setBorder(new EmptyBorder(0, 0, 0, 0));
		this.showsOpen = showsOpen;

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		lblLevel = new JLabel(Integer.toString(level));
		lblLevel.setPreferredSize(new Dimension(30, 15));
		lblLevel.setMinimumSize(new Dimension(30, 15));
		lblLevel.setMaximumSize(new Dimension(30, 15));
		lblLevel.setHorizontalTextPosition(SwingConstants.LEFT);
		lblLevel.setHorizontalAlignment(SwingConstants.LEFT);
		add(lblLevel);
		lblLevel.setForeground(Color.BLACK);
		lblLevel.setBorder(new EmptyBorder(0, 0, 0, 0));

		lblFloorImage = new JLabel();
		add(lblFloorImage);
		lblFloorImage.setIcon(showsOpen ? img_open : img_close);
		this.level = level;
		this.simulation = simulation;

		if (!isMaxLevel) {
			add(Box.createRigidArea(new Dimension(5, 0)));
			btnFloorUp = new JToggleButton();
			btnFloorUp.setIcon(new ImageIcon(FloorComposite.class.getResource("/arrow_up_small.png")));
			btnFloorUp.setActionCommand("UP");
			btnFloorUp.addActionListener(this);
			add(btnFloorUp);
		}
		
		if (level != 0) {
			add(Box.createRigidArea(new Dimension(5, 0)));
			btnFloorDown = new JToggleButton();
			btnFloorDown.setIcon(new ImageIcon(FloorComposite.class.getResource("/arrow_down_small.png")));
			btnFloorDown.setActionCommand("DOWN");
			btnFloorDown.addActionListener(this);
			add(btnFloorDown);
		}
	}

	private void toggleElevatorPresent(boolean isOpen) {
		Color color = isOpen ? cElevatorIsPresent : null;
		this.setBackground(color);
	}

	public void showElevatorIsPresent() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				toggleElevatorPresent(true);
			}
		});
	}

	public void showElevatorNotPresent() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				toggleElevatorPresent(false);
			}
		});
	}

	public void showImageClose() {
		if (this.showsOpen)	
			this.changeImage();
	}

	public void showImageOpen() {
		if (!this.showsOpen)	
			this.changeImage();
	}

	private void changeImage() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (showsOpen) {
					lblFloorImage.setIcon(img_close);
					showsOpen = false;
					toggleElevatorPresent(false);
				} else {
					lblFloorImage.setIcon(img_open);
					showsOpen = true;
					toggleElevatorPresent(true);
				}
			}
		});
	}
	
	public boolean isFloorRequested() {
		if (btnFloorUp != null && !btnFloorUp.isEnabled() && btnFloorUp.isSelected()) {
			return true;
		}
		if (btnFloorDown != null && !btnFloorDown.isEnabled() && btnFloorDown.isSelected()) {
			return true;
		}
		return false;
	}

	public void resetFloorRequest() {
		resetUp();
		resetDown();
	}

	public void resetUp() {
		if (btnFloorUp != null && !btnFloorUp.isEnabled()) {
			btnFloorUp.setSelected(false);
			btnFloorUp.setEnabled(true);
		}
	}

	public void resetDown() {
		if (btnFloorDown != null && !btnFloorDown.isEnabled()) {
			btnFloorDown.setSelected(false);
			btnFloorDown.setEnabled(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (simulation.getCurrentFloor() != level) {
			String actionCmd = e.getActionCommand();
			if ("UP".equals(actionCmd)) {
				simulation.floorRequest(new Request(level, ElevatorState.MOVING_UP));
				btnFloorUp.setEnabled(false);
				btnFloorUp.setSelected(true);
			} else {
				simulation.floorRequest(new Request(level, ElevatorState.MOVING_DOWN));
				btnFloorDown.setEnabled(false);
				btnFloorDown.setSelected(true);
			}
		} else {
			if (btnFloorDown != null) btnFloorDown.setSelected(false);
			if (btnFloorUp != null) btnFloorUp.setSelected(false);
		}
	}

}
