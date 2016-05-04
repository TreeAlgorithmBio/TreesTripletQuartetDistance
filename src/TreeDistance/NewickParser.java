package TreeDistance;

/**
 * Created by khushboomandlecha on 5/4/16.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class NewickParser {


    private static int globalStart;
    private static int globalEnd;
    private static boolean parseError;

    public static void main(String args[]) {

        UnrootedTree t = parseFile("Tree1.new");
        System.out.println("Print tree"+t.toString());

    }
    public static UnrootedTree parseFile(String filename) {

        // Read file

        BufferedReader br = null;

        try {

            String sCurrentLine;
            StringBuffer stringBuffer = new StringBuffer();
            String temp = "";
            String temp2 = "";
            int index=0;
            br = new BufferedReader(new FileReader(filename));

            while ((sCurrentLine = br.readLine()) != null) {

                temp = TrimComment(sCurrentLine);
                temp2 = Trim(temp);


                if(!temp2.matches("")){
                    stringBuffer.append(temp2);
                }

                //checking for the semicolon condition
                if(temp2.charAt(sCurrentLine.length()-1) == ';')
                    break;


            }
            globalStart =0 ;
            globalEnd = stringBuffer.length()-1;
            UnrootedTree t = parse(stringBuffer.toString());

            return t;


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

        return null;
    }




    public static ArrayList<UnrootedTree> parseMultiFile(String filename) {

        ArrayList<UnrootedTree> trees = new ArrayList<UnrootedTree>();
        BufferedReader br = null;

        try {

            br = new BufferedReader(new FileReader(filename));
            String sCurrentLine;
            StringBuffer stringBuffer = new StringBuffer();
            String temp = "";
            String temp2 = "";
            int index;
            UnrootedTree tr = new UnrootedTree();

            while ((sCurrentLine = br.readLine()) != null) {

                temp = TrimComment(sCurrentLine);
                temp2 = Trim(temp);

                if(!temp2.matches("")){
                    stringBuffer.append(temp2);
                }
                //checking for the semicolon condition
                index = temp2.length()-1;

                if(temp2.charAt(index) == ';') {

                    globalStart = 0;
                    globalEnd = stringBuffer.length() - 1;
                    tr = parse(stringBuffer.toString());

                    if (tr != null)

                        trees.add(tr);
                }
            }

            return trees;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

        return trees;
    }

    private static UnrootedTree parse(String str) {


        if(str.charAt(globalEnd) != ';')

            return null;

        UnrootedTree t = new UnrootedTree();
        t = parseSubTree(t,str);

        parseLength(str);

        if(globalStart==globalEnd) {

            System.out.println("Parse error! String is finished before ';'... Returning anyways!");
            parseError = true;
        }

        //if it starts from a colon, do nothing. else iterate until the buffer pointer reached a alpha. error if never reached alphabet

        else {

            if(str.charAt(globalStart) != ';'){

                System.out.println("tring is finished... Returning anyways!");
                parseError = true;
            }

            globalStart++;

            if (globalStart != globalEnd){

                System.out.println("Parse error! Finish before finsihing ... Returning anyways!");
                parseError = true;
            }
        }

        return t;
    }

    private static UnrootedTree parseSubTree(UnrootedTree t, String str) {


        if(globalStart==globalEnd) {

            parseError = true;
            System.out.println("Parse error! String ended! Continuing anyways...");
            return new UnrootedTree();
        }

        if (str.charAt(globalStart) == '(')
        {
            return parseInternal(str);
        }

        return new UnrootedTree(parseName(str));
    }


    public static UnrootedTree parseStr(String inputStr) {

        return parse(inputStr);
    }


    private static UnrootedTree parseInternal(String str) {

        UnrootedTree t = new UnrootedTree();

        if(globalStart==globalEnd) {

            parseError = true;
            System.out.println("Parse error! String ended! Continuing anyways...");
            return new UnrootedTree();
        }
        // Remove '(' char, create internal node, and recurse
        if (str.charAt(globalStart) != '(' )
        {
            System.out.println("Parse error! Expected '(' here (got ");
            parseError = true;

        }
        globalStart++;

        UnrootedTree internalNode = new UnrootedTree();
        ParseBranchSet(internalNode,str);

        if(globalStart==globalEnd) {
            System.out.println("Parse error! String ended! Continuing anyways...");
            parseError = true;
            return internalNode;
        }

        // Remove ')' char, get name
        if (str.charAt(globalStart) != ')')
        {
            System.out.println("Parse error! Expected ')' here (got ");
            parseError = true;

        }

        globalStart++;

        if(globalStart==globalEnd) {
            System.out.println("Parse error!String is finished... Continuing anyways....");
            parseError = true;
            return internalNode;
        }
        internalNode.name = parseName(str);

        return internalNode;
    }



    private static String parseName(String str) {

        int nameStartPos = globalStart;
        int numChars = 0;

        if(globalStart == globalEnd) {
            System.out.println("Parse error! String ended! Continuing anyways...");
            parseError = true;
            return "";
        }



        while (true) {

            char c = str.charAt(globalStart);
            if (c != '(' && c != ')' && c != ',' && c != ':' && c != ';') {

                globalStart = globalStart +1;
                numChars = numChars +1 ;
            }
            else
                break;

            if(globalStart == globalEnd) {
                System.out.println("Parse error! String ended! Continuing anyways...");
                parseError = true;
                break;
            }
        }

        return str.substring(nameStartPos, nameStartPos + numChars);
    }

    public boolean isError() {

        boolean parseError = false;
        return parseError;
    }


    public int getPos(int i) {


        if(globalStart == globalEnd) {
            System.out.println("Parse error! String ended! Continuing anyways...");
            parseError = true;
            return -1;
        }

        return globalEnd - globalStart;
    }

    public static void parseLength(String str) {

        if(globalStart == globalEnd) {
            System.out.println("Parse error! String ended! Continuing anyways...");
            parseError = true;
            return;
        }

        // Go past ':'
        if(str.charAt(globalStart) == ':')
            globalStart++;

        while (true) {

            char c = str.charAt(globalStart);


            if (c != '(' && c != ')' && c != ',' && c != ':' && c != ';') {
                globalStart++;

            }
            else
                break;
            if(globalStart == globalEnd) {
                System.out.println("Parse error! String ended! Continuing anyways...");
                parseError = true;
                return;
            }
        }
    }





    public static void ParseBranchSet(UnrootedTree parent, String str) {


        if(globalStart == globalEnd) {
            System.out.println("Parse error! String ended! Continuing anyways...");
            parseError = true;
            return;
        }

        // Parse arbritrarily many branches (i.e. subtrees with lengths)

        int degreeHere = 0;
        int largestDegreeBelow = 0;

        while (true) {

            degreeHere++;
            UnrootedTree t = parseSubTree(parent,str);
            largestDegreeBelow = Math.max(largestDegreeBelow, t.maxDegree);
            parent.addEdgeTo(t);
            parseLength(str);

            if( globalStart != globalEnd && (str.charAt(globalStart) == ',') ){

                globalStart++;
            }

            else {
                break;
            }
        }
        parent.maxDegree = Math.max(degreeHere, largestDegreeBelow);
    }



    private static String TrimComment(String str){

        int i=0;
        int l = str.length();

        for (i=0;i<l;i++){

            if(str.charAt(i) == '%')
                return str.substring(0,i);
        }

        return str;
    }

    private static String Trim(String str){

        String strnew = str.replaceAll("\\s+","");
        return strnew;
    }

    private String EmptyLine(String str){

        String strnew = Trim(str);

        return strnew;

    }



}