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

import de.ovgu.featureide.examples.elevator.core.controller.ControlUnit;

import java.util.Comparator;

import de.ovgu.featureide.examples.elevator.core.model.ElevatorState;

public class Request {

	private int floor;
	
	public int getFloor() {
		return floor;
	}

	public Request(int floor) {
		this.floor = floor;
	}
	
	
	private ElevatorState direction;
	
	public Request(int floor, ElevatorState direction) {
		this.floor = floor;
		this.direction = direction;
	}
	
	public ElevatorState getDirection() {
		return direction;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + floor;
		result = prime * result + direction.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Request other = (Request) obj;
		return floor == other.floor && direction == other.direction;
	}
	
	public static class RequestComparator implements Comparator<Request> {
		
		protected ControlUnit controller;

		public RequestComparator(ControlUnit controller) {
			this.controller = controller;
		}
		
		@Override
		public int compare(Request o1, Request o2) {
			return compareDirectional(o1, o2);
			}

		protected int compareDirectional(Request o1, Request o2) {
			return 0;
		}
	}
	
	public static class DownComparator extends RequestComparator {
		
		public DownComparator(ControlUnit controller) {
			super(controller);
		}
		
		@Override
		public int compareDirectional(Request o1, Request o2) {
			if (o1.getDirection() == ElevatorState.MOVING_UP   && o2.getDirection() != ElevatorState.MOVING_UP) return  1;
			if (o1.getDirection() == ElevatorState.MOVING_DOWN && o2.getDirection() == ElevatorState.MOVING_UP)	return -1;
			if (o1.getDirection() == ElevatorState.FLOORING    && o2.getDirection() == ElevatorState.MOVING_UP
					&& o1.getFloor() + o2.getFloor() - 2*controller.getCurrentFloor() < 0)	return -1;
			final int diffO1 = o1.getFloor() - controller.getCurrentFloor();
			final int diffO2 = o2.getFloor() - controller.getCurrentFloor();
			if (diffO1 <= 0 && diffO2 <= 0) return o2.getFloor() - o1.getFloor();
			if (diffO1 >  0 && diffO2 >  0) return o1.getFloor() - o2.getFloor();
			return (diffO1 <= 0) ? -1 : 1;
		}
	}
	
	public static class UpComparator extends RequestComparator {
		
		public UpComparator(ControlUnit controller) {
			super(controller);
		}
		
		@Override
		public int compareDirectional(Request o1, Request o2) {
			if (o1.getDirection() == ElevatorState.MOVING_DOWN && o2.getDirection() != ElevatorState.MOVING_DOWN) return  1;
			if (o1.getDirection() == ElevatorState.MOVING_UP   && o2.getDirection() == ElevatorState.MOVING_DOWN) return -1;
			if (o1.getDirection() == ElevatorState.FLOORING    && o2.getDirection() == ElevatorState.MOVING_DOWN
					&& o1.getFloor() + o2.getFloor() - 2*controller.getCurrentFloor() > 0)	return -1;
			final int diffO1 = o1.getFloor() - controller.getCurrentFloor();
			final int diffO2 = o2.getFloor() - controller.getCurrentFloor();
			if (diffO1 >= 0 && diffO2 >= 0) return o1.getFloor() - o2.getFloor();
			if (diffO1 <  0 && diffO2 <  0) return o2.getFloor() - o1.getFloor();
			return (diffO1 >= 0) ? -1 : 1;
		}
	}

	@Override
	public String toString() {
		return "Request [floor=" + floor + ", direction=" + direction + "]";
	}

}
