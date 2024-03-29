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


public class Request {

	private int floor;
	
	public int getFloor() {
		return floor;
	}

	public Request(int floor) {
		this.floor = floor;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + floor;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Request other = (Request) obj;
		return (floor != other.floor);
	}
	
	public static class RequestComparator implements Comparator<Request> {
		
		protected ControlUnit controller;

		public RequestComparator(ControlUnit controller) {
			this.controller = controller;
		}
		
		@Override
		public int compare(Request o1, Request o2) {
			int diff0 = Math.abs(o1.floor - controller.getCurrentFloor());
			int diff1 = Math.abs(o2.floor - controller.getCurrentFloor());
			return diff0 - diff1;
			}

		protected int compareDirectional(Request o1, Request o2) {
			return 0;
		}
	}
	

	@Override
	public String toString() {
		return "Request [floor=" + floor + "]";
	}

}
