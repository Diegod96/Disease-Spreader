import java.util.*;
import java.io.*;
import java.util.List;


public class DiseaseSpreader {

    static int Source;
    static String Target;
    //static String nodeId;
    static int MAXNODES = 29;
    static int MAXLINKS = 83;

    static Link[] arrayOfLinks = new Link[MAXLINKS];
    static Airport[] arrayOfAirports = new Airport[MAXNODES];



    static int[][] infectedNodes = new int[MAXNODES+1][2];
    static int [] daysInfected = new int[MAXNODES+1];
    static int p = 33;
    static String choice;
    static int done = 0;
    static int days = 365;
    static int newNodes = 0;
    static int totalNodes = 0;
    static int day;

    static String nodeString = " ";

    public static void main(String[] args) throws IOException {



        readDataFiles();

        //Let start with Node 2 as infected so setting infected flag onf node id 2 first column

        int nodeId = 2;
        infectedNodes[nodeId][0] = 1;
        totalNodes = 1;


        arrayOfAirports[1].setInfected(1);
        arrayOfAirports[1].setDayAnalyzed(0);


        for (int day = 1; done != 1; day++) {

            newNodes = 0;

            analyzeLinks(day);


            for (int e =1; e < MAXNODES; e++) {
                if (arrayOfAirports[e].getDayAnalyzed() == day) {

                    nodeString = nodeString + "  " + String.valueOf(arrayOfAirports[e].getIdNumber());

                }
            }

            System.out.print("Day: " + day + '\n' + "Probability of spread: " + p + "%" + '\n' + "New Nodes: " + newNodes + '\n' + "New Nodes ID#: " + nodeString + '\n' +  "Total Nodes: " + totalNodes + '\n' +  "-------------------" + '\n');

            nodeString = "";

            if (totalNodes >= MAXNODES )
                done = 1;

        }   // End of  Day for loop
    }


    public static int getNumber() {

        Random numberGenerator = new Random();
        return numberGenerator.nextInt(101);
    }


    public static void  infectedNodesProcessing(int linkIndex, int idNumber, int day, int typeOfNode) {

        int sourceIndex;
        int x =1;

        if (typeOfNode == 1) {
            sourceIndex = arrayOfLinks[linkIndex].getSource() - 1;
            if (arrayOfAirports[sourceIndex].getInfected() == 1) {
                return;
            } else {
                runProbability(sourceIndex, day);
            }
        }

        // Change loop and index for airport
        for (int i = linkIndex; i < MAXLINKS; i++) {

            // Dealing with the same source then process all links from the passes source
            if (arrayOfLinks[i].getSource() == idNumber) {

                // Adjust index
                int targetIndex = arrayOfLinks[i].getTarget() - 1;

                // Skip it if infected already
                if (arrayOfAirports[targetIndex].getInfected() == 1) {
                    continue;
                }

                // Run probability to check if it is infected.
                runProbability(targetIndex, day);

            }
        }
    }

    public static void runProbability(int index, int day) {

        if (getNumber() < p) {

            arrayOfAirports[index].setInfected(1);
            arrayOfAirports[index].setDayAnalyzed(day);
            newNodes++;
            totalNodes++;

        }

    }

    public static void readDataFiles() throws IOException{


        //Writing the Airport and Airport iD# into a 2D Array
        String fName = "data/Creation of network of the Spirit Air Route.csv";
        String thisLine;
        int count = 0;
        FileInputStream fis = new FileInputStream(fName);
        DataInputStream myInput = new DataInputStream(fis);
        int i = 0;
        List<String[]> lines = new ArrayList<String[]>();

        int index = 0;

        while ((thisLine = myInput.readLine()) != null) {
            lines.add(thisLine.split(","));

            Airport airportObj = new Airport();
            airportObj.setIdNumber((Integer.parseInt(lines.get(index)[0])));
            airportObj.setName((lines.get(index)[1]));
            arrayOfAirports[index] = airportObj;
            index++;

        }
        String[][] array = new String[lines.size()][0];
        lines.toArray(array);



        //Writing the Source and Target into a 2D Array
        String edges = "data/SourceTarget.csv";
        String thisLine2;
        int newCount = 0;
        FileInputStream fis2 = new FileInputStream(edges);
        DataInputStream Myinput = new DataInputStream(fis2);
        int g = 0;
        List<String[]> linez = new ArrayList<String[]>();

        index = 0;

        while ((thisLine2 = Myinput.readLine()) != null) {


            linez.add(thisLine2.split(","));
            Link linkObj = new Link();
            linkObj.setSource((Integer.parseInt(linez.get(index)[0])));
            linkObj.setTarget(Integer.parseInt(linez.get(index)[1]));
            linkObj.setIdNumber(index + 1);
            arrayOfLinks[index] = linkObj;

            index++;
        }


        String[][] edgesList = new String[linez.size()][0];
        linez.toArray(edgesList);


    }


    public static void analyzeLinks(int day){

        int typeOfNode  = 0;

        for (int i = 0; i < MAXLINKS; i++) {

            // Lets get the source node for this link
            int sourceIndex = arrayOfLinks[i].getSource() - 1;
            int targetIndex  = arrayOfLinks[i].getTarget() - 1;

            if((arrayOfAirports[sourceIndex].getInfected() == 1 && arrayOfAirports[targetIndex].getInfected() == 1) && (arrayOfAirports[sourceIndex].getDayAnalyzed() < day && arrayOfAirports[sourceIndex].getDayAnalyzed() < day))
                continue;


            // If infected then test targets
            if(arrayOfAirports[sourceIndex].getInfected() == 1 && arrayOfAirports[sourceIndex].getDayAnalyzed() < day) {

                typeOfNode = 0;

                infectedNodesProcessing(i, arrayOfAirports[sourceIndex].getIdNumber() , day, typeOfNode);

            } else {

                typeOfNode = 1;

                if(arrayOfAirports[targetIndex].getInfected() == 1 && arrayOfAirports[targetIndex].getDayAnalyzed() < day) {

                    infectedNodesProcessing(i, arrayOfAirports[targetIndex].getIdNumber() , day, typeOfNode);

                }

            }
        }  // end of for loop
    }
}

