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
package de.ovgu.featureide.examples.elevator.core.controller;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.PriorityBlockingQueue;
import de.ovgu.featureide.examples.elevator.core.controller.Request.RequestComparator;

import de.ovgu.featureide.examples.elevator.core.model.Elevator;
import de.ovgu.featureide.examples.elevator.core.model.ElevatorState;

public class ControlUnit implements Runnable 
											, ITriggerListener
											{
	
	public static int TIME_DELAY = 700;
	public boolean run = true;

	private Elevator elevator;
	
	private static final Object calc = new Object();
	private RequestComparator comparator = new Request.UpComparator(this);
	private RequestComparator downComparator = new Request.DownComparator(this);
	private PriorityBlockingQueue<Request> q = new PriorityBlockingQueue<>(1, comparator);
	
	public ControlUnit(Elevator elevator) {
		this.elevator = elevator;
	}

	public void run() {
		while (run) {
			final ElevatorState state;
			synchronized (calc) {
				// Get next state of the elevator			
				state = calculateNextState();
				elevator.setCurrentState(state);
				switch (state) {
				case MOVING_UP:
					elevator.setDirection(ElevatorState.MOVING_UP);
					elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);
					break;
				case MOVING_DOWN:
					elevator.setDirection(ElevatorState.MOVING_DOWN);
					elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);
					break;
				case FLOORING:
					this.triggerOnTick();
					break;
				}
				sortQueue();
			}
			
			// Moving or Waiting
			try {
				Thread.sleep(TIME_DELAY);
			} catch (InterruptedException e) {
			}
			
			switch (state) {
			case MOVING_UP:
				this.triggerOnTick();
				break;
			case MOVING_DOWN:
				this.triggerOnTick();
				break;
			default:
				break;
			}
		}
	}

	private ElevatorState calculateNextState() {
		final int currentFloor = elevator.getCurrentFloor();
		return getElevatorState(currentFloor);
	}
	
	private ElevatorState getElevatorState(int currentFloor) {
		if (!q.isEmpty()) {
			Request poll = q.peek();
			int floor = poll.getFloor();
			if (floor == currentFloor) {
				do {
					triggerOnRequest(q.poll());
					poll = q.peek();
				} while (poll != null && poll.getFloor() == currentFloor);
				return ElevatorState.FLOORING;
			} else if (floor > currentFloor) {
				return ElevatorState.MOVING_UP;
			} else {
				return ElevatorState.MOVING_DOWN;
			}
		}
		return ElevatorState.FLOORING;
	}

	private void sortQueue() {
		final ElevatorState direction = elevator.getCurrentState();
		final PriorityBlockingQueue<Request> pQueue;
		switch (direction) {
		case MOVING_DOWN:
			pQueue = new PriorityBlockingQueue<>(Math.max(1, q.size()), downComparator);
			break;
		case MOVING_UP:
			pQueue = new PriorityBlockingQueue<>(Math.max(1, q.size()), comparator);
			break;
		default:
			return;
		}
		q.drainTo(pQueue);
		q = pQueue;
	}

	private List<ITickListener> tickListener = new ArrayList<>();

	public void addTickListener(ITickListener ticker) {
		this.tickListener.add(ticker);
	}

	private void triggerOnTick() {
		for (ITickListener listener : this.tickListener) {
			listener.onTick(elevator);
		}
	}

	private void triggerOnRequest(Request request) {
		for (ITickListener listener : this.tickListener) {
			listener.onRequestFinished(elevator, request);
		}
	}

	@Override
	public void trigger(Request req) {
		synchronized (calc) {
			q.offer(req);
		}
	}
	
	public int getCurrentFloor() {
		return elevator.getCurrentFloor();
	}


	public void setDisabledFloors(List<Integer> disabledFloors) {
		elevator.setDisabledFloors(disabledFloors);
	}
	
	public List<Integer> getDisabledFloors() {
		return elevator.getDisabledFloors();
	}
	
	public boolean isDisabledFloor(int level) {
		return !elevator.getDisabledFloors().contains(level);
	}

}
