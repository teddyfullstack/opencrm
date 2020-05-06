package io.nguyenhongphat0.crm.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import io.nguyenhongphat0.crm.entities.*;

public class RentUtil {

	public static class EstateRentHistory {
		private Estate estate;
		private List<List<List<Customer>>> history;
		public List<Integer> years;
		public List<Integer> months;
		
		public List<Customer> get(int year, int month) {
			int i = this.years.indexOf(year);
			int j = this.months.indexOf(month);
			return this.history.get(i).get(j);
		}

		public EstateRentHistory(Estate estate) {
			this.estate = estate;
			this.init();
		}
		
		private void init() {
			List<Rent> rents = this.estate.getRents();
			LocalDate now = LocalDate.now();
			LocalDate firstDate = now;
			// Find the very first rent date
			for (Rent rent : rents) {
				if (rent.getStartDate().isBefore(firstDate)) {
					firstDate = rent.getStartDate();
				}
			}
			// Initialize years and months array
			this.years = new ArrayList<Integer>();
			for (int i = firstDate.getYear(); i <= now.getYear(); i++) {
				this.years.add(i);
			}
			this.months = new ArrayList<Integer>();
			for (int j = 1; j <= 12; j++) {
				this.months.add(j);
			}
			this.history = new ArrayList<>();
			for (int i = 0; i < years.size(); i++) {
				history.add(new ArrayList<List<Customer>>());
				for (int j = 0; j < months.size(); j++) {
					history.get(i).add(new ArrayList<Customer>());
					LocalDate firstDateOfMonth = LocalDate.of(this.years.get(i), this.months.get(j), 1);
					int daysInMonth = firstDateOfMonth.getMonth().length(firstDateOfMonth.isLeapYear());
					LocalDate lastDateOfMonth = LocalDate.of(this.years.get(i), this.months.get(j), daysInMonth - 1);
					for (Rent rent : rents) {
						LocalDate startDate = rent.getStartDate();
						LocalDate endDate = rent.getEndDate();
						if (endDate == null) {
							endDate = now;
						}
						if (startDate.isBefore(lastDateOfMonth) && endDate.isAfter(firstDateOfMonth)) {
							history.get(i).get(j).add(rent.getCustomer());
						}
					}
				}
			}
		}
		
	}
}
