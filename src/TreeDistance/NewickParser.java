package TreeDistance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NewickParser {

    public UnrootedTree parseFile(String filename) {

        // Read file

        BufferedReader br = null;

        try {

            String sCurrentLine;
            StringBuffer stringBuffer = new StringBuffer();
            br = new BufferedReader(new FileReader(filename));

            while ((sCurrentLine = br.readLine()) != null) {

                stringBuffer.append(sCurrentLine);
            }

            UnrootedTree t = parse();
            return t;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }



            /*FileInputStream infile = new FileInputStream();


        boolean read = infile.read(filename);
        if (read) {
            String line;
            std.stringstream ss = new std.stringstream();
            while (true) {
                getline(infile, line);
                line = trim_comment(line);
                line = rtrim(line);
                if (emptyLine(line)) {
                    continue;
                }
                ss << line;
                if (infile.eof()) {
                    str = ss.str();
                    break;
                }
                if (line.charAt(line.length() - 1) == ';') {
                    str = ss.str();
                    break;
                }
            }
            infile.close();

            // replace all whitespace
            eraseWhitespace(str);
            */
        }

        return null;
    }




    public UnrootedTree parseMultiFile(String filename) {


        // Read file

        BufferedReader br = null;

        try {

            String sCurrentLine;
            StringBuffer stringBuffer = new StringBuffer();
            br = new BufferedReader(new FileReader(filename));

            while ((sCurrentLine = br.readLine()) != null) {

                stringBuffer.append(sCurrentLine);
            }

            UnrootedTree t = parse();
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

        /*ifstream infile = new ifstream();

        // Open
        infile.open(filename);
        if (infile) {
            ArrayList<UnrootedTree> trees = new ArrayList<UnrootedTree>();

            String line;
            std.stringstream ss = new std.stringstream();
            while (true) {
                getline(infile, line);
                if (infile.eof())
                    break;
                if (emptyLine(line))
                    continue;
                line = trim_comment(line);
                ss << line;
                if (line.charAt(line.length() - 1) == ';') {
                    str = ss.str();

                    trees.add(parse());
                    ss.str(String());
                }
            }

            infile.close();
            return trees;
        } else {
            // Couldn't open file!
            cerr << "Couldn't open file \"" << filename << "\"!" << std.endl;
            parseError = true;
            System.exit(-1);
        }*/


        return null;
    }


    public UnrootedTree parseStr(String inputStr) {

        String str = inputStr;
        return parse();
    }

    public boolean isError() {
        return true;
    }


    /*public int getPos() {

        if (it == strEnd) {
            cerr << "Parse error! String ended! Continuing anyways..." << "\n";
            parseError = true;
            return -1;
        }
        return distance(str.begin(), it);
    }*/


    public UnrootedTree parse() {
        parseError = false;
        char it = str.charAt(0);
        char strEnd = str.charAt(str.length()-1);

        if (*str.rbegin() != ';')
        {
            return null;
        }
        UnrootedTree t = parseSubTree();
        parseLength();
        if (it == strEnd) {
            cerr << "Parse error! String is finished before ';'... Returning anyways!" << "\n";
            parseError = true;
        } else {
            if (*it != ';')
            {
                cerr << "Parse error! Finished before string finished! (Read '" <<*
                it << "' on pos " << getPos() << ", expecting ';'). Returning anyways" << "\n";
                parseError = true;
            }
            it++;
            if (it != strEnd) {
                cerr << "Parse error! Finished before string finished! (Read '" <<*
                it << "' on pos " << getPos() << ", expected being done). Returning anyways" << "\n";
                parseError = true;
            }
        }
        return t;
    }

    //C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
    public UnrootedTree parseSubTree() {
        if (it == strEnd) {
            cerr << "Parse error! String ended! Continuing anyways..." << "\n";
            parseError = true;
            return new UnrootedTree();
        }

        if (*it == '(')
        {
            return parseInternal();
        }
        // TODO: Other possibilities than name?!?
        return new UnrootedTree(parseName());
    }

    public UnrootedTree parseInternal() {
        if (it == strEnd) {
            cerr << "Parse error! String ended! Continuing anyways..." << "\n";
            parseError = true;
            return new UnrootedTree();
        }

        // Remove '(' char, create internal node, and recurse
        if (*it != '(')
        {
            cerr << "Parse error! Expected '(' here (got '" <<*
            it << "' on pos " << getPos() << "). Continuing anyways..." << "\n";
            parseError = true;
        }
        it++;
        UnrootedTree internalNode = new UnrootedTree();
        ParseBranchSet(internalNode);

        if (it == strEnd) {
            cerr << "Parse error! String ended! Continuing anyways..." << "\n";
            parseError = true;
            return internalNode;
        }

        // Remove ')' char, get name
        if (*it != ')')
        {
            cerr << "Parse error! Expected ')' here (got '" <<*
            it << "' on pos " << getPos() << "). Continuing anyways..." << "\n";
            parseError = true;
        }
        it++;
        if (it == strEnd) {
            cerr << "Parse error! String is finished... Continuing anyways..." << "\n";
            parseError = true;
        }
        internalNode.name = parseName();

        return internalNode;
    }

    //C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
    public void ParseBranchSet(UnrootedTree parent) {
        if (it == strEnd) {
            cerr << "Parse error! String ended! Continuing anyways..." << "\n";
            parseError = true;
            return;
        }

        // Parse arbritrarily many branches (i.e. subtrees with lengths)
        int degreeHere = 0;
        int largestDegreeBelow = 0;
        while (true) {
            degreeHere++;
            //C++ TO JAVA CONVERTER TODO TASK: Pointer arithmetic is detected on this variable, so pointers on this variable are left unchanged:
            UnrootedTree * t = parseSubTree();
            largestDegreeBelow = Math.max(largestDegreeBelow, t.maxDegree);
            //C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
            //ORIGINAL LINE: parent->addEdgeTo(t);
            parent.addEdgeTo(new UnrootedTree(t));
            parseLength();
            if (it != strEnd &&*it == ',')
            {
                it++; // and go again!
            }
            else
            break;
        }
        parent.maxDegree = Math.max(degreeHere, largestDegreeBelow);
    }

    //C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
    public String parseName() {
        if (it == strEnd) {
            cerr << "Parse error! String ended! Continuing anyways..." << "\n";
            parseError = true;
            return "";
        }
        int nameStartPos = getPos();
        int numChars = 0;
        while (true) {
            byte c = it;
            if (c != '(' && c != ')' && c != ',' && c != ':' && c != ';') {
                it++;
                numChars++;
            } else
                break;

            if (it == strEnd) {
                cerr << "Parse error! String ended! Continuing anyways..." << "\n";
                parseError = true;
                break;
            }
        }
        return str.substr(nameStartPos, numChars);
    }

    //C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
    public void parseLength() {
        // Do we start a number?
        if (it == strEnd) {
            cerr << "Parse error! String ended! Continuing anyways..." << "\n";
            parseError = true;
            return;
        }
        if (*it != ':')
        return;

        // Go past ':'
        it++;
        while (true) {
            byte c = it;

            // TODO: Should actually check that this is a number (i.e. [0-9\.]*)
            if (c != '(' && c != ')' && c != ',' && c != ':' && c != ';') {
                it++;
            } else
                break;
            if (it == strEnd) {
                cerr << "Parse error! String ended! Continuing anyways..." << "\n";
                parseError = true;
                break;
            }
        }
    }
}