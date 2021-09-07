import java.util.Scanner;

//Storing data for each process
class Process {
	public int processID;
	public int burstTime;
	public int arrivalTime;
	public int waitingTime;
	public int turnAroundTime;
}

public class SJF_NonPreemptive {
	public static int numOfProcess = 5;

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		// creating array of Process to store info for each process
		Process[] data = new Process[numOfProcess];
		for (int i = 0; i < numOfProcess; i++) {
			data[i] = new Process();
		}
		for (int i = 0; i < numOfProcess; i++) {
			data[i].processID = (i + 1); // storing process ID
			System.out.println("\nProcess P" + data[i].processID);
			System.out.print("Burst Time: ");
			data[i].burstTime = in.nextInt(); // reading burstTime for each process
			System.out.print("ArrivalTime: ");
			data[i].arrivalTime = in.nextInt(); // input Arrival time for each process
		}
		sortArrival(data);
		finalSort(data);
		calcWaitingTime(data);
		calcTurnAroundTime(data);
		display(data);
		in.close(); // close scanner
	}

	public static void sortArrival(Process[] data) {
		
		// Using selection sort to sort processes according to arrival time
		Process temp;
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

	/* Re sorting processes in the order of execution depending on shortest burst
	 time and time of arrival */
	public static void finalSort(Process[] data) {
		int currentBurstTime, currentTime = 0;
		Process temp;
		for (int i = 0, k = 1; k < numOfProcess; i++, k++) {
			currentTime += data[i].burstTime;
			currentBurstTime = data[k].burstTime;
			for (int j = k + 1; j < numOfProcess; j++) {
				
				/* all process that have currently already arrived and have burst time shorter
				  than others ready to be executed */
				if (data[j].arrivalTime <= currentTime && data[j].burstTime < currentBurstTime) {
					temp = data[k];
					data[k] = data[j];
					data[j] = temp;
				}
			}
		}
	}

	public static void calcWaitingTime(Process[] data) {
		int currentTime = data[0].burstTime;
		
		// Calculating Waiting time for each process
		for (int i = 1; i < numOfProcess; i++) {
			data[i].waitingTime = currentTime - data[i].arrivalTime;
			currentTime += data[i].burstTime;
		}
	}

	public static void calcTurnAroundTime(Process[] data) {
		int currentTime = 0;
		
		// Calculating Turn around Time for each process
		for (int i = 0; i < numOfProcess; i++) {
			currentTime += data[i].burstTime;
			data[i].turnAroundTime = currentTime - data[i].arrivalTime;
		}
	}

	public static void display(Process[] data) {
		
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
		
		// Displaying the Gantt chart
		System.out.print("\nDisplaying Ganth chart\n\n");
		for (int i = 0; i < numOfProcess; i++) {
			System.out.print("P" + data[i].processID);
			int j = 0;
			char unitTime = 42; 
			/* 42 is the ASCII code for * that will be used to represent unit time in Gantt chart
			Displaying no. of asterisk = burst time for each process */
			while (j < data[i].burstTime) {
				System.out.print(" " + unitTime + " ");
				j++;
			}
		}
		
		// Displaying average Waiting time and turn around Time
		System.out.printf("\n\nAverage waiting time: %.2f\n", totalWaitingTime / numOfProcess);
		System.out.printf("Average burst time: %.2f", totalTurnAroundTime / numOfProcess);
	}
}
