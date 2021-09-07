import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Linkedlist {

	StudentList head;

	static class ScoreList { // add scores to the linked list..

		float score;
		ScoreList next;

		ScoreList(int value) {

			this.score = value;
			this.next = null;

		}

		public static ScoreList linkScores(ScoreList head, int val) {

			ScoreList newNode = new ScoreList(val);

			if (head == null)

				head = newNode;

			else {

				ScoreList top = head;

				while (top.next != null) {

					top = top.next;

				}

				top.next = newNode;

			}

			return head;

		}

	}

	static class StudentList {

		String name;
		StudentList next;
		ScoreList ptr;

		StudentList(String identity) {

			name = identity;
			next = null; //pointer for student name
			ptr = null;  //pointer for student score

		}

	}

	public static Linkedlist linkStudent(Linkedlist list, String identity) { //add student to linked list

		StudentList new_node = new StudentList(identity);
		new_node.next = null;

		if (list.head == null) {

			list.head = new_node;

		}

		else {

			StudentList last = list.head;

			while (last.next != null) {

				last = last.next;

			}

			last.next = new_node;

		}

		return list;

	}

	public static void promptScoreList(Linkedlist list) { //read score from user

		Scanner input = new Scanner(System.in);
		StudentList currNode = list.head;

		while (currNode != null) {

			System.out.print("Name: " + currNode.name + "\nNo. of scores to be recorded ?: ");
			int numOfScores = input.nextInt();

			while (numOfScores > 4) {

				System.out.println("You can only enter upto 4 scores !");
				System.out.print("Enter again the no. of scores to be recorded: ");
				numOfScores = input.nextInt();
				System.out.println();
			}

			for (int i = 0; i < numOfScores; i++) {

				System.out.print("Score " + (i + 1) + ": ");
				int marks = input.nextInt();
				currNode.ptr = currNode.ptr.linkScores(currNode.ptr, marks);
				System.out.println();

			}

			System.out.println();
			currNode = currNode.next;

		}

	}

	public static void display(Linkedlist list) { //display scores, total, average and grade

		char grade;
		StudentList currNode = list.head;
		System.out.println();

		while (currNode != null) {

			System.out.println();
			System.out.println(currNode.name);
			float total = 0;
			int numOfScores = 0;
			ScoreList top = currNode.ptr;

			while (top != null) {

				total += top.score;
				numOfScores++;
				System.out.println("Score " + numOfScores + ": " + top.score);
				top = top.next;

			}

			float average = (float) (total / numOfScores);
			System.out.println("Total: " + total);
			System.out.println("Average: " + average);

			if (average >= 80)

				grade = 'A';

			else if (average >= 60)

				grade = 'B';

			else if (average >= 40)

				grade = 'C';

			else

				grade = 'F';

			System.out.println("Grade: " + grade);
			currNode = currNode.next;

		}

	}

	public static void main(String[] args) {

		Linkedlist list = new Linkedlist();
		{
			try {

				File file = new File("Student.txt");
				Scanner readFile = new Scanner(file);
				readFile.useDelimiter("\\n");
				while (readFile.hasNext()) {

					String student = readFile.next();
					linkStudent(list, student);

				}

				promptScoreList(list);
				display(list); 
				readFile.close();

			} catch (FileNotFoundException e) {

				System.out.print("\nFile not found !");

			}

		}

	}
}
