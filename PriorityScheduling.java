import java.util.Scanner;

//This program implements non-preemptive priority scheduling
//Class Data stores information about each process
class Data {
	public char process;
	public int burstTime;
	public int priority;
	public int waitingTime;
	public int turnAroundTime;
}

public class PriorityScheduling {
	public static int numOfProcess;

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("No. of processes: ");
		numOfProcess = in.nextInt(); // Read no. of process from user
		
		// creating array of Data to store info for each process
		Data[] data = new Data[numOfProcess];
		for (int i = 0; i < numOfProcess; i++) {
			data[i] = new Data();
		}
		
		// assuming all process arrive at time 0
		for (int i = 0; i < numOfProcess; i++) {
			data[i].process = (char) ('A' + i); // recording process name
			System.out.println("\nProcess " + data[i].process + " ");
			System.out.print("Burst Time: ");
			data[i].burstTime = in.nextInt();// reading burstTime for each process
			System.out.print("Priority: ");
			data[i].priority = in.nextInt();// input priority for each process
			
			// Checking if a valid priority no. has been entered
			while ((data[i].priority > numOfProcess) || (data[i].priority < 1)) {
				System.out.print("\nEnter priority between 1 and " + numOfProcess + ": ");
				data[i].priority = in.nextInt();
			}
			
			// Check that each priority entered is unique
			for (int j = 0; j < numOfProcess; j++) {
				if ((data[i].priority == data[j].priority) && (i != j)) {
					do {
						
						// if same priority being entered again
						System.out.println("Processes cannot have same priority !");
						System.out.print("\nEnter priority again: ");
						data[i].priority = in.nextInt();
					} while (data[i].priority == data[j].priority);

					// if invalid priority entered
					while ((data[i].priority > numOfProcess) || (data[i].priority < 1)) {
						System.out.print("\nEnter priority between 1 and " + numOfProcess + ": ");
						data[i].priority = in.nextInt();
					}
					break;
				}
			}
		}
		calcTime(data);
		display(data);
		in.close();
	}

	public static void calcTime(Data[] data) {
		int currentTime = 0;
		for (int i = 1; i <= numOfProcess; i++) {
			for (int j = 0; j < numOfProcess; j++) {
				if (data[j].priority == i) {
					currentTime += data[j].burstTime;
					data[j].waitingTime = currentTime - data[j].burstTime; // calculating Waiting Time
					data[j].turnAroundTime = data[j].waitingTime + data[j].burstTime; // Calculating turn around Time
					break;
				}
			}
		}
	}

	public static void display(Data[] data) {
		float avgWaitingTime = 0, avgTurnAroundTime = 0;
		for (int i = 0; i < numOfProcess; i++) {
			
			// calculating total waiting Time, turnAround time
			avgWaitingTime += data[i].waitingTime;
			avgTurnAroundTime += data[i].turnAroundTime;
		}
		
		// calculating average waiting Time, turnAround time
		avgWaitingTime /= numOfProcess;
		avgTurnAroundTime /= numOfProcess;
		
		// Displaying
		System.out.printf("Process, Burst time, Priority, Waiting time, Turnaround time\n");
		for (int i = 0; i < numOfProcess; i++) {
			System.out.print("" + data[i].process);
			System.out.print("\t\t" + data[i].burstTime);
			System.out.print("\t" + data[i].priority);
			System.out.print("\t\t" + data[i].waitingTime);
			System.out.print("\t" + data[i].turnAroundTime);
			System.out.println();
		}
		System.out.println("Average waiting time: " + avgWaitingTime);
		System.out.println("Average burst time: " + avgTurnAroundTime);
	}
}
