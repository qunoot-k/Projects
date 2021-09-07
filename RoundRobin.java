import java.util.*;

class RoundRobin {
	Scanner scanner = new Scanner(System.in);
	int[] burst, rem, wait, ta;
	int size, q, b = 0, t = 0, flag = 0;

	RoundRobin(int size) {
		this.size = size;
		burst = new int[size]; //Burst time
		wait = new int[size]; //Waiting time
		ta = new int[size]; //Turn around time
		rem = new int[size]; 
	}

  // Read burst time and quantum time for each process
	void get() {
		for (int i = 0; i < size; i++) {
			System.out.print("Enter burst time of P" + (i + 1) + ":");
			burst[i] = rem[i] = scanner.nextInt();
		}
		System.out.print("Enter quantum time:");
		q = scanner.nextInt();
	}

	void round() {
		do {
			flag = 0;
			for (int i = 0; i < size; i++) {
				if (rem[i] >= q) {
					System.out.print("P" + (i + 1) + "\t");
					for (int j = 0; j < size; j++) {
						if (j == i)
							rem[i] = rem[i] - q;
						else if (rem[j] > 0)
							wait[j] += q;
					}
				} else if (rem[i] > 0) {
					System.out.print("P" + (i + 1) + "\t");
					for (int j = 0; j < size; j++) {
						if (j == i)
							rem[i] = 0;
						else if (rem[j] > 0)
							wait[j] += rem[i];
					}
				}
			}
			for (int i = 0; i < size; i++)
				if (rem[i] > 0)
					flag = 1;
		} while (flag == 1);
		for (int i = 0; i < size; i++)
			ta[i] = wait[i] + burst[i];
	}

	void display() {
		System.out.println("\nProcess\tBurst\tWaiting\tTurnaround");
		for (int i = 0; i < size; i++) {
			System.out.println("P" + (i + 1) + "\t" + burst[i] + "\t" + wait[i] + "\t" + ta[i]);
			b += wait[i];
			t += ta[i];
		}
		System.out.println("Average waiting time:" + (b / size));
		System.out.println("Average Turnaround time:" + (t / size));
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter the no of process:");
		int n = scanner.nextInt();
		RoundRobin obj = new RoundRobin(n);
		obj.get();
		obj.round();
		obj.display();
	}
}
