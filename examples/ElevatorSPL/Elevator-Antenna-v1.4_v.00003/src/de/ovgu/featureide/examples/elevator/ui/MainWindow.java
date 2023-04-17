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

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.BorderLayout;
import java.io.IOException;

import de.ovgu.featureide.examples.elevator.core.controller.ITickListener;

import de.ovgu.featureide.examples.elevator.core.model.Elevator;
import de.ovgu.featureide.examples.elevator.core.model.ElevatorState;



import de.ovgu.featureide.examples.elevator.core.controller.Request;

import javax.swing.JToggleButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;

import de.ovgu.featureide.examples.elevator.sim.SimulationUnit;

public class MainWindow implements ITickListener
											, ActionListener 
											{
	private JFrame frmElevatorSample;
	private JSplitPane splitPane;
	private JLabel lblEvent;
	private List<FloorComposite> listFloorComposites = new ArrayList<>();
	private List<JToggleButton> listInnerElevatorControls = new ArrayList<>();

	private SimulationUnit sim;

	public MainWindow(SimulationUnit sim) {
		this.sim = sim;
	}

	public void initialize(int maxFloors) {
		if (frmElevatorSample != null) {
			return;
		}
		frmElevatorSample = new JFrame();
		frmElevatorSample.setTitle("Elevator Sample");
		frmElevatorSample.setBounds(100, 50, 900, 650); // window position and size
		frmElevatorSample.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		createBaseStructure();
		createPanelControlsContent(maxFloors);
		addBuilding(maxFloors);
		frmElevatorSample.setVisible(true);
	}

	private void createBaseStructure() {
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		frmElevatorSample.setContentPane(contentPane);

		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		contentPane.add(splitPane, BorderLayout.CENTER);
	}


	 private void createPanelControlsContent(int maxFloors) {
		JPanel panel_control = new JPanel();
		try {
			panel_control = new JBackgroundPanel(MainWindow.class.getResourceAsStream("/elevator_inside2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		splitPane.setRightComponent(panel_control);

		GridBagLayout gbl_panel_control = new GridBagLayout();
		panel_control.setLayout(gbl_panel_control);
		
		lblEvent = new JLabel("");
		lblEvent.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblEvent.setForeground(Color.WHITE);
		lblEvent.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lbl = new GridBagConstraints();
		gbc_lbl.gridwidth = 4;
		gbc_lbl.insets = new Insets(0, 0, 185, 0);
		gbc_lbl.fill = GridBagConstraints.HORIZONTAL;
		gbc_lbl.gridx = 0;
		gbc_lbl.gridy = 0;
		panel_control.add(lblEvent, gbc_lbl);
		
		JPanel panel_floors = new JPanel(new GridLayout(0,3));
		panel_floors.setBackground(Color.GRAY);
		
		JToggleButton btnFloor;
		for (int i = maxFloors; i >= 0; i--) {
			btnFloor = new JToggleButton(String.valueOf(i));
			btnFloor.setActionCommand(String.valueOf(i));
			btnFloor.addActionListener(this);
			panel_floors.add(btnFloor);
			listInnerElevatorControls.add(0, btnFloor);
		}
			
		GridBagConstraints gbc_btnFloor = new GridBagConstraints();
		gbc_btnFloor.insets = new Insets(0, 0, 0, 0);
		gbc_btnFloor.fill = GridBagConstraints.BOTH;
		gbc_btnFloor.gridwidth = 4;
		gbc_btnFloor.gridx = 2;
		gbc_btnFloor.gridy = 4;
			
		panel_control.add(panel_floors, gbc_btnFloor);
	 }

	private void addBuilding(int maxFloors) {
		JPanel panel_building = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel_building.setLayout(layout);
		
		JScrollPane scrollPane = new JScrollPane(panel_building);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.SOUTH;

		for (int i = maxFloors; i >= 0; i--) {
			FloorComposite floor = new FloorComposite(i == 0, i, sim);
			layout.setConstraints(floor, gbc);
			gbc.gridy += 1;

			panel_building.add(floor);
			listFloorComposites.add(0, floor);
		}
		
		splitPane.setLeftComponent(scrollPane);
	}
	
	public void setEventLabel(String text, Color color) {
		if (lblEvent != null) {
			lblEvent.setText(text);
			lblEvent.setForeground(color);
		}
	}

	public void onTick(Elevator elevator) {
		ElevatorState state = elevator.getCurrentState();
		int currentFloor = elevator.getCurrentFloor();
		
		switch (state) {
		case MOVING_UP:
			this.listFloorComposites.get(currentFloor - 1).showImageClose();
			break;
		case MOVING_DOWN:
			this.listFloorComposites.get(currentFloor + 1).showImageClose();
			break;
		case FLOORING:
			this.listFloorComposites.get(currentFloor).showImageOpen();
			JToggleButton btnFloor = listInnerElevatorControls.get(currentFloor);
			if (btnFloor.isSelected()) {
				btnFloor.setSelected(false);
				btnFloor.setEnabled(true);
			}
			break;
		}
		this.clearPresent();
		this.listFloorComposites.get(currentFloor).showElevatorIsPresent();
	}

	private void clearPresent() {
		for (FloorComposite fl : listFloorComposites) {
			fl.showElevatorNotPresent();
		}
	}
	
	@Override
	public void onRequestFinished(Elevator elevator, Request request) {
		listFloorComposites.get(request.getFloor()).resetFloorRequest();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		sim.floorRequest(new Request(Integer.valueOf(e.getActionCommand())));
		listInnerElevatorControls.get(Integer.valueOf(e.getActionCommand())).setEnabled(false);
	}
}
