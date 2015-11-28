package com.daguo.modem.schedule;
/**
 * 
 * @author Bugs_rabbit
 * @function 计算年月日，星期几的功能
 */
public class Util_FindDayOfYear {
	public int getDayOfYear(int year, int month, int day) {

		int sum = 0;
		month += 1;
		for (int y = 1970; y < year; y++) {
			if (((y % 4 == 0) & (y % 100 != 0)) | (y % 400 == 0)) {
				sum += 366;
			} else {
				sum += 365;
			}
			/*
			 * for(int i=1;i<=12;i++){ switch(i){ case 1: case 3: case 5: case
			 * 7: case 8: case 10: case 12:sum+=31; break; case 4: case 6: case
			 * 9: case 11:sum+=30; break; case 2: if(((y%4==0) & (y%100!=0)) |
			 * (y%400==0))sum+=29; else sum+=28; } }
			 */
		}

		for (int i = 1; i < month; i++) {
			switch (i) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				sum += 31;
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				sum += 30;
				break;
			case 2:
				if (((year % 4 == 0) & (year % 100 != 0)) | (year % 400 == 0))
					sum += 29;
				else
					sum += 28;
			}
		}
		return sum = sum + day;
	}

}
