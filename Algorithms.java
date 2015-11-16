import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.lang.Math;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Algorithms 
{
	ArrayList<String> ProcessName = new ArrayList<>();
	ArrayList<Integer> BurstTime = new ArrayList<>();
	ArrayList<Double> ArrivalTime = new ArrayList<>();
	double avgWait = 0;
	double avgTurnAround = 0;
	
	public Algorithms()
	{
		return;
	}
	public static void main(String[] args) throws IOException 
	{
		Algorithms printProcesses = new Algorithms();
		File analyze = new File("Performance.txt");

		Scanner in = new Scanner(System.in);
		int userinput, quantum;
		
		System.out.println("The file Performance.txt will be deleted.");
		System.out.println("Input 0 for okay, 1 for no");
		userinput = in.nextInt();
		if (analyze.exists() && userinput == 0)
		{
			analyze.delete();
		}
		else
		{
			System.out.println("Performance.txt will not be deleted. Program is terminated.");
			in.close();
			return;
		}
		
		System.out.println("Enter 0 to Exit");
		System.out.println("Enter 1 for Single Algorithm Permformance");
		System.out.println("Enter 2 for Algorithm Comparison");
		userinput = in.nextInt();
		printProcesses.SetProcesses();
		printProcesses.SortArrival();
		
		while (userinput != 0)
		{
			if (userinput == 1)
			{
				System.out.println("Enter 0 to Exit");
				System.out.println("Enter 1 for First Come First Serve");
				System.out.println("Enter 2 for Round Robin");
				userinput = in.nextInt(); //Get user input.
				while (userinput != 0)
				{
					//Print first come first serve results to console
					if (userinput == 1)
					{					
						printProcesses.FCFS();
						printProcesses.PrintAll();
					}
					//Print round robin results to console
					else if (userinput == 2)
					{
						System.out.println("Enter quantum as integer:");
						quantum = in.nextInt();
						printProcesses.RR(quantum);
						printProcesses.PrintAll();
					}
					System.out.println("Enter 0 to Exit");
					System.out.println("Enter 1 for First Come First Serve");
					System.out.println("Enter 2 for Round Robin");
					userinput = in.nextInt();
				}
				printProcesses.ClearAll();
			}
			else if (userinput == 2)
			{
				while (userinput != 0)
				{
					System.out.println("Enter number of data sets:");
					userinput = in.nextInt();
					System.out.println("Enter quantum as integer:");
					quantum = in.nextInt();
					
					//Generates new sets of data and results for as many data sets determined by user.
					for (int i = 0; i < userinput; i++)
					{
						//Determines if file exists before writing to it.
						if (!analyze.exists())
						{
							analyze.createNewFile();
						}
						BufferedWriter writer = new BufferedWriter(new FileWriter(analyze, true));
						
						//Identifies first come first serve results.
						writer.write("First Come First Serve results:");
						writer.newLine();
						writer.flush();
						
						//Print FCFS and write information to a file.
						printProcesses.FCFS();
						printProcesses.PrintAll();
						printProcesses.WriteAll(analyze);
						
						//Identifies round robin results.
						writer.newLine();
						writer.write("Round Robin results:");
						writer.newLine();
						writer.flush();
						
						//Print RR and write information to a file.
						System.out.println("\nRound Robin results:");
						printProcesses.RR(quantum);
						printProcesses.PrintAll();
						printProcesses.WriteAll(analyze);
						writer.newLine();
						writer.close();

						//Clear current information for new data to be generated.
						printProcesses.ClearAll();
						printProcesses.SetProcesses();
						printProcesses.SortArrival();
					}
					System.out.println("Enter 1 to rerun program or 0 to quit");
					userinput = in.nextInt();
				}
			}
			else
			{
				System.out.println("Input invalid. Please re-enter input.");
				System.out.println("Enter 0 to Exit");
				System.out.println("Enter 1 for Single Algorithm Permformance");
				System.out.println("Enter 2 for Algorithm Comparison");
				userinput = in.nextInt();
			}
		}
		in.close();
	}
	
	void SetProcesses()
	{
		int processSet = (int) (Math.floor(Math.random() * (20 - 10 + 1)) + 11);
		int burstSet = (int) (Math.floor(Math.random() * (20 - 1 + 1)) + 1);
		double arrivalSet = (Math.floor(Math.random() * (5-0 + 1)));

		for (int idx = 0; idx < processSet; idx++)
		{
			//Generates number of processes between 10 and 20.
			ProcessName.add("P" + idx);
			
			//Generates burst time of each process between 1 and 20.
			BurstTime.add(burstSet);
			burstSet = (int) (Math.floor(Math.random() * (20 - 1 + 1)) + 1);
			
			//Generates arrival time of each process between 0 and 5.
			ArrivalTime.add(arrivalSet);
			arrivalSet = (Math.random() * (1-0));
		}
	}
	
	void SortArrival()
	{
		String prevName, currentName;
		Double prevArri;
		Double currentArri;
		int prevBurst, currentBurst;
		ArrayList<Double> testSort = new ArrayList<>();
		testSort.addAll(ArrivalTime);
		Collections.sort(testSort);
		
		//Sorts processes by arrival time.
		while (!ArrivalTime.equals(testSort))
		{
			for (int idx = 0; idx < ArrivalTime.size()-1; idx++)
			{
				prevName = ProcessName.get(idx);	currentName = ProcessName.get(idx+1);
				prevArri = ArrivalTime.get(idx);	currentArri = ArrivalTime.get(idx+1);
				prevBurst = BurstTime.get(idx);	currentBurst = BurstTime.get(idx+1);
				if (prevArri > currentArri)
				{
					ProcessName.set(idx, currentName);
					ProcessName.set(idx+1, prevName);
					ArrivalTime.set(idx, currentArri);
					ArrivalTime.set(idx+1, prevArri);
					BurstTime.set(idx, currentBurst);
					BurstTime.set(idx+1, prevBurst);
				}
			}
		}
	}
	
	void FCFS()
	{
		int processIdx = ProcessName.size();
		double waitTime, start = 0, totalWait = 0;
		double idx0TurnAround = BurstTime.get(0);
		double turnaround, totalTurnAround = 0;
		for (int idx = 1; idx < processIdx; idx++)
		{
			//Calculates average waiting time.
			start = start + BurstTime.get(idx-1);
			waitTime = start - ArrivalTime.get(idx);
			totalWait = totalWait + waitTime;
			
			//Calculates average turn-around time.
			turnaround = waitTime + BurstTime.get(idx);
			totalTurnAround = totalTurnAround + turnaround;
		}
		
		avgWait = (totalWait / ProcessName.size());		
		avgTurnAround = ((totalTurnAround + idx0TurnAround) / ProcessName.size()); 
	}
	
	void RR(int quantum)
	{
		ArrayList<Integer> burst = new ArrayList<>();
		ArrayList<Double> arrival = new ArrayList<>();
		burst.addAll(BurstTime);
		arrival.addAll(ArrivalTime);
		double turnaround, totalTurnAround = 0;
		double totalWait = 0;
		Double start = ArrivalTime.get(0); //Sets the start at the first process.
		double completion=0;
		int idx = 0;
		ArrayList<Double> waitTimes = new ArrayList<>();
		
		//Sets all elements of complete time to 0, matches the amount of elements in burst array.
		for (int i = 0; i < burst.size(); i++)
		{
			waitTimes.add(i, 0.0);
		}
		
		do{
			if (burst.get(idx) <= quantum)
			{
				completion = start + burst.get(idx); //Sets the completion of each process.
				turnaround = completion - arrival.get(idx);
				totalTurnAround = totalTurnAround + turnaround; //Calculates total turn-around time.
				
				waitTimes.set(idx, start - waitTimes.get(idx)); //Calculates waiting time.
				totalWait = totalWait + waitTimes.get(idx); //Calculates total waiting time.
				waitTimes.remove(idx);
				
				start = completion; //Sets the start of the next process.
				burst.remove(idx);
				arrival.remove(idx);
				idx = idx-1;
			}
			else
			{
				completion = start + quantum; //Sets the completion of the process.
				waitTimes.set(idx, start - waitTimes.get(idx)); //Calculates waiting time.
				start = completion; //Sets the start of the next process.
				burst.set(idx, burst.get(idx) - quantum);
			}
			if (idx >= burst.size()-1)
			{
				idx = 0;
			}
			else
			{
				idx = idx + 1;
			}
		} while (!burst.isEmpty());

		//Calculates average turn-around and waiting time.
		avgTurnAround = (totalTurnAround / ProcessName.size());
		avgWait = (totalWait / ProcessName.size()); 
	}
	
	//Prints all data to the console.
	void PrintAll()
	{
		int numProcess = ProcessName.size();
		DecimalFormat df = new DecimalFormat("#.##");
		
		System.out.println("Process Name	|| Arrival Time	|| Burst Time");
		System.out.println("---------------------------------------------");
				
		for (int idx = 0; idx < numProcess; idx++)
		{
			System.out.println(ProcessName.get(idx) + "		|| " + df.format(ArrivalTime.get(idx)) + "		|| " +
					BurstTime.get(idx));
		}
		
		System.out.println("\nAvg Wait Time || Avg TurnAround Time");
		System.out.println("--------------------------------------");
		System.out.println(df.format(avgWait) + "	      || " + df.format(avgTurnAround)); 
	}
	
	//Writes all data to a file.
	void WriteAll(File file)
	{
		int numProcess = ProcessName.size();
		DecimalFormat df = new DecimalFormat("#.##");
		
		try {
			if (!file.exists())
			{
				file.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			writer.write("Process Name	|| Arrival Time	|| Burst Time");
			writer.newLine();
			writer.write("---------------------------------------------");
			writer.newLine();
			for (int idx = 0; idx < numProcess; idx++)
			{
				writer.write(ProcessName.get(idx) + "		|| " + df.format(ArrivalTime.get(idx)) + "		|| " +
						BurstTime.get(idx));
				writer.newLine();
			}
			writer.newLine();
			writer.write("Avg Wait Time 	|| Avg TurnAround Time");
			writer.newLine();
			writer.write(df.format(avgWait) + "	     	|| " + df.format(avgTurnAround));
			writer.newLine();
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	//Clears all ArrayLists for new data.
	void ClearAll()
	{
		ProcessName.clear();
		BurstTime.clear();
		ArrivalTime.clear();
	}
}
