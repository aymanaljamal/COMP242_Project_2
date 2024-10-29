package application;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MartyrDate implements Comparable<MartyrDate> {
	private Calendar date = new GregorianCalendar();
	private LinkedLists<Martyr> Martyrs = new LinkedLists<>();

	public MartyrDate(Calendar date) {
		super();
		this.date = date;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public LinkedLists<Martyr> getMartyrs() {
		return Martyrs;
	}

	public void setMartyrs(LinkedLists<Martyr> martyrs) {
		Martyrs = martyrs;
	}

	@Override
	public boolean equals(Object obj) {
		MartyrDate MartyrDate2= (MartyrDate)obj;
		return this.getDate().equals(MartyrDate2.getDate());
	}

	@Override
	public String toString() {
		SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy");
        return formatter.format(date.getTime());
	}

	@Override
	public int compareTo(MartyrDate o) {
		return o.getDate().compareTo(this.date);

	}

}
