import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.Map;

class CalculateFactorial implements Runnable {
	int key, value;
	HashMap<Integer, Integer> factorial;

	CalculateFactorial(int key, int value, HashMap<Integer, Integer> factorial) {
		this.key = key;
		this.value = value;
		this.factorial = factorial;
	}

	public void run() {
		
		//calculate factorial
		for (int i = key - 1; i >= 1; i--) {
			value *= i;
		}
		
		//Store the calculated factorial value in the original HashMap
		factorial.put(key, value);
	}
}

public class MultiThread {
	public static void main(String[] args) throws InterruptedException {
		
		//using a HashMap t0 store the number and its factorial as key value pair
		HashMap<Integer, Integer> factorial = new HashMap<>();
		Scanner in = new Scanner(System.in);
		int num;
		
		//Read input from user
		System.out.print("Enter 5 numbers: ");
		for (int i = 0; i < 5; i++) {
			num = in.nextInt();
			
			//Store the input in hasMap
			//Initially storing value of factorial as its orignal number
			factorial.put(num, num);
		}
		
		//Create 5 threads
		ExecutorService threads = Executors.newFixedThreadPool(5);
		
		//using for each loop to iterate through hashmap and execute threads
		for (Map.Entry temp : factorial.entrySet()) {
			int key = (int) temp.getKey(); // Read key from hashmap
			int value = (int) temp.getValue(); // Read its value
			
			/*Execute a thread
			to calculate to factorial of a number
			by 1st calling the constructor of class CalculateFactorial
			and then the function run() */
			
			threads.execute(new CalculateFactorial(key, value, factorial));
		}
		threads.shutdown();
		
		//Print number and its factorial
		
		for (Map.Entry temp : factorial.entrySet()) {
			int key = (int) temp.getKey();
			int value = (int) temp.getValue();
			System.out.println("Factorial for " + key + " is " + value);
		}
		in.close();
	}
}
