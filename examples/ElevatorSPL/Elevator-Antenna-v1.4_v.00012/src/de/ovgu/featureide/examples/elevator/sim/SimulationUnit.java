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
package de.ovgu.featureide.examples.elevator.sim;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.ovgu.featureide.examples.elevator.core.controller.ControlUnit;
import de.ovgu.featureide.examples.elevator.core.controller.ITickListener;
import de.ovgu.featureide.examples.elevator.core.controller.ITriggerListener;
import de.ovgu.featureide.examples.elevator.core.controller.Request;
import de.ovgu.featureide.examples.elevator.core.model.Elevator;
import de.ovgu.featureide.examples.elevator.ui.MainWindow;

public class SimulationUnit {

	private ITriggerListener triggerListener;
	private static MainWindow simulationWindow;
	private ControlUnit controller;
	
	public static void main(String[] args) {
		SimulationUnit sim = new SimulationUnit();
		simulationWindow = new MainWindow(sim);
		sim.start(5);
	}
	
	public void start(int maxFloor) {
		Elevator elevator = new Elevator(maxFloor);
		controller = new ControlUnit(elevator);
		this.setTriggerListener(controller);
		
		Thread controllerThread = new Thread(controller);
		controller.addTickListener(new ITickListener() {
			public void onTick(Elevator elevator) {
				System.out.printf(String.format("%s - %s -- Current Floor %d Next Floor %d \n", new SimpleDateFormat("HH:mm:ss").format(new Date()),
						elevator.getCurrentState(), elevator.getCurrentFloor(), Integer.MAX_VALUE));
			}
			@Override
			public void onRequestFinished(Elevator elevator, Request request) {				
			}
		});
		controller.addTickListener(simulationWindow);
		
		simulationWindow.initialize(elevator.getMaxFloor());
		controllerThread.start();
	}
	
	/**
	 * The simulation unit is a bridge between gui and control unit.
	 * So there is a need to delegate requests made by the gui to the control unit.
	 * Thats why the simulationunit is also capable of informing trigger listener.
	 * @param listener - The trigger listener.
	 */
	public void setTriggerListener(ITriggerListener listener) {
		this.triggerListener = listener;
	}

	/**
	 * Send a floor request to the trigger listener.
	 * @param floorRequest -  The floor request to send to the trigger listener.
	 */
	public void floorRequest(Request floorRequest) {
		this.triggerListener.trigger(floorRequest);		
	}
	
	public int getCurrentFloor() {
		return controller.getCurrentFloor();
	}
	
	public void toggleService() {
		controller.setService(!controller.isInService());
	}
		
	public boolean isInService() {
		return controller.isInService();
	}

	public void setDisabledFloors(List<Integer> disabledFloors) {
		this.controller.setDisabledFloors(disabledFloors);
	}
	
	public List<Integer> getDisabledFloors() {
		return this.controller.getDisabledFloors();
	}
	
	public boolean isDisabledFloor(int level) {
		return this.controller.isDisabledFloor(level);
	}
}
