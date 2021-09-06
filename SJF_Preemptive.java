import java.util.Scanner;

//Storing data for each process
class Info {
	public int processID;
	public int burstTime;
	public int arrivalTime;
	public int waitingTime;
	public int turnAroundTime;
}

public class SJF_Preemptive {
	public static int numOfProcess = 5;

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		// creating array of Process to store info for each process
		Info[] data = new Info[numOfProcess];
		for (int i = 0; i < numOfProcess; i++) {
			data[i] = new Info();
		}
		for (int i = 0; i < numOfProcess; i++) {
      // storing process ID
			data[i].processID = (i + 1); 
			System.out.println("\nProcess P" + data[i].processID);
      // reading burstTime for each process
			System.out.print("Burst Time: ");
			data[i].burstTime = in.nextInt();
      // input Arrival time for each process
			System.out.print("ArrivalTime: ");
			data[i].arrivalTime = in.nextInt();
		}
		sortArrival(data);
		String chart = calcArrival(data);
		calcTurnAroundTime(data);
		Display(data, chart);
		in.close();
	}

	public static void sortArrival(Info[] data) {
		// Using selection sort to sort processes according to arrival time
		Info temp;
		for (int i = 0; i < numOfProcess - 1; i++) {
			int pos = i;
			for (int j = i + 1; j < numOfProcess; j++) {
				if (data[j].arrivalTime < data[pos].arrivalTime)
					pos = j;
			}
			if (pos != i) {
				temp = data[i];
				data[i] = data[pos];
				data[pos] = temp;
			}
		}
	}

	//Returns any 1 process that is waiting
	public static int findProcess(Info[] data, int[] duplicateBurst, int currentTime) {
		for (int i = 0; i < numOfProcess; i++) {
			if (data[i].arrivalTime <= currentTime && duplicateBurst[i] != 0) {
				return i;
			}
		}
		return numOfProcess;
	}

	//Calculate Waiting Time and return Ganth chart as a string
	public static String calcArrival(Info[] data) {
		String chart = "";
		int firstArrived = data[0].arrivalTime;
		int totalTimeFrame = firstArrived;
		int[] duplicateBurst = new int[numOfProcess];
		//Calculate total time required to complete all the process
		for (int i = 0; i < numOfProcess; i++) {
			totalTimeFrame += data[i].burstTime;
		}
		//making a copy of the burst time for each process
		for (int i = 0; i < numOfProcess; i++) {
			duplicateBurst[i] = data[i].burstTime;
		}
		int minBurstProcess = findProcess(data, duplicateBurst, firstArrived);
		//Loop will run until all process have completed
		for (int i = firstArrived; i < totalTimeFrame; i++) {
			if (duplicateBurst[minBurstProcess] != 0) {
				for (int j = 0; j < numOfProcess; j++) {
					//A process has already arrived
					//has the least burst Time 
					//has not completed running
					if (data[j].arrivalTime <= i && duplicateBurst[j] < duplicateBurst[minBurstProcess]
							&& duplicateBurst[j] != 0)
						minBurstProcess = j;
				}
				for (int j = 0; j < numOfProcess; j++) {
					//A process has arrived and it is waiting
					if (data[j].arrivalTime <= i && duplicateBurst[j] != 0 && minBurstProcess != j)
						data[j].waitingTime++; // Calculate waiting time for each process
				}
			} else {
				// Find a function whose burst time is not 0 and
				// can be run;
				minBurstProcess = findProcess(data, duplicateBurst, i);
				i--;
				continue;
			}
			duplicateBurst[minBurstProcess]--;
			// recording which process is running
			chart += data[minBurstProcess].processID; 
		}
		return chart;
	}

	//Calculating turn around Time
	public static void calcTurnAroundTime(Info[] data) {
		for (int i = 0; i < numOfProcess; i++) {
			data[i].turnAroundTime = data[i].waitingTime + data[i].burstTime;
		}
	}

	public static void Display(Info[] data, String chart) {
		// Calculating total turn around time and Waiting Time
		float totalWaitingTime = 0, totalTurnAroundTime = 0;
		for (int i = 0; i < numOfProcess; i++) {
			totalWaitingTime += data[i].waitingTime;
			totalTurnAroundTime += data[i].turnAroundTime;
		}
		// Displaying data in tabular form
		System.out.printf("\nProcess, Burst time, Arrival Time, Waiting time, Turn around time\n");
		for (int i = 1; i <= numOfProcess; i++) {
			for (int j = 0; j < numOfProcess; j++) {
				if (data[j].processID == i) {
					System.out.print("P" + data[j].processID);
					System.out.print("\t\t" + data[j].burstTime);
					System.out.print("\t" + data[j].arrivalTime);
					System.out.print("\t\t" + data[j].waitingTime);
					System.out.print("\t\t" + data[j].turnAroundTime);
					System.out.println();
				}
			}
		}
		//Displaying Ganth Chart
		int length = chart.length();
		int temp = 0;
		String ganthChart = "";
		for (int i = 0; i < length; i++) {
			// chartAt() returns the ASCII value so to convert to int we subtract
			// it with ASCII value of 0
			if ((chart.charAt(i) - '0') == temp) 
				ganthChart += " * "; // * represents 1 unit time
			else {
				// Using ASCII value to convert char to int
				temp = chart.charAt(i) - '0'; 
				ganthChart += " P" + temp + " * ";
			}
		}
		System.out.print("\nGanth chart\n");
		System.out.print("\n" + ganthChart);
		// Displaying average Waiting time and turn around Time
		System.out.printf("\n\nAverage waiting time: %.2f\n", totalWaitingTime / numOfProcess);
		System.out.printf("Average burst time: %.2f", totalTurnAroundTime / numOfProcess);
	}
}
