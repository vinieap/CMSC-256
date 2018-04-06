/**
 * Vinit Patel
 * 03/24/2018
 * CMSC 256-ALL
 * Project #4 - SongReader
 * Project Purpose : Read a playlist file and create Song Objects
 */

public class SongReader {

    /**
     * Header Method - Information
     */
    private static void printHeading() {
        System.out.println("Vinit Patel");
        System.out.println("Project #2");
        System.out.println("CMSC 256-ALL");
        System.out.println("Spring 2018\n");
    }

    /**
     * Check if the file name provided is valid
     * @param arg - file name passed through arguments
     * @return - true if valid and false if not
     */
    private static boolean validFile(String arg) {
        File file = new File(arg);
        return file.exists() && file.canExecute();
    }

    /**
     * Tells user the file name is not valid and prompts them for another one
     * @return - Another file name
     */
    private static String promptFileName() {
        System.out.println("The file is not found/cannot be opened. Enter another file name");
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }

    /**
     * Checks if the opening tag is valid or not
     * @param tag - tag passed in
     * @return - true if tag is valid and false if not
     */
    private static boolean validOpeningTag(String tag) {
        switch (tag) {
            case "<song>":
                if(!openingStack.isEmpty()) {
                    errorChar = 'f';
                    return false;
                }
                openingStack.push(tag);
                return true;
            case "<title>":
                if(!openingStack.peek().equals("<song>")) {
                    errorChar = 'f';
                    return false;
                }
                state = 't';
                openingStack.push(tag);
                return true;
            case "<album>":
                if(!openingStack.peek().equals("<song>")) {
                    errorChar = 'f';
                    return false;
                }
                state = 'a';
                openingStack.push(tag);
                return true;
            case "<artist>":
                if(!openingStack.peek().equals("<song>")) {
                    errorChar = 'f';
                    return false;
                }
                state = 'r';
                openingStack.push(tag);
                return true;
            default:
                errorChar = 'f';
                return false;
        }
    }

    /**
     * Checks if the closing tag is valid or not
     * @param closingTag - tag passed in
     * @return - true if the tag is valid and false if not
     */
    private static boolean validClosingTag(String closingTag) {
        if(openingStack.isEmpty()) {
            errorChar = 'f';
            return false;
        }

        String openingTag = openingStack.pop();

        if(pairedTags(openingTag, closingTag)) {
            return true;
        }

        openingStack.push(openingTag);

        errorChar = 'f';
        return false;
    }

    /**
     * Checks if the closing tag matches the latest opening tag in the stack
     * @param opening - most recent opening tag
     * @param closing - most recent closing tag
     * @return - true if they match and false if they don't
     */
    private static boolean pairedTags(String opening, String closing) {
        if(opening.equals("<song>") && closing.equals("</song>")) {
            return true;
        }
        else if(opening.equals("<artist>") && closing.equals("</artist>")) {
            return true;
        }
        else if(opening.equals("<album>") && closing.equals("</album>")) {
            return true;
        }
        else if(opening.equals("<title>") && closing.equals("</title>")) {
            return true;
        }
        else {
            errorChar = 'f';
            return false;
        }
     }

    /**
     * Checks what the tag is and what the data parsed needs to be assigned to
     * @param data - information between the tags
     * @param c - the tag that is currently active
     */
    private static void tagState(String data, char c) {
        switch(c) {
            case 't':
                title = data;
                break;
            case 'a':
                album = data;
                break;
            case 'r':
                artist = data;
                break;
            default:
                break;
        }
     }

        private static String[] line = new String[512];
        private static int elements = 0;
        private static LinkedStack<String> openingStack = new LinkedStack<>();
        private static LinkedStack<String> logStack = new LinkedStack<>();
        private static char errorChar = 's';
        private static String album = "";
        private static String title = "";
        private static String artist = "";
        private static char state = 'n';
        private static ArrayList<Song> songList = new ArrayList<>();

    /**
     * Handles all the parsing through the file
     * @param args - file name
     */
    public static void main (String[] args) {
            printHeading();

            String inputFileName;

            if (args.length == 0) {
                inputFileName = promptFileName();
            } else {
                inputFileName = args[0];
            }

            while (!validFile(inputFileName)) {
                inputFileName = promptFileName();
            }

            Scanner scan = null;
            PrintWriter out = null;

            try {
                File inputFile = new File(inputFileName);
                scan = new Scanner(inputFile);
                out = new PrintWriter("ErrorLog.txt");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            LinkedStack<String> flipStack = new LinkedStack<>();

            assert scan != null;

            while (scan.hasNextLine()) {
                line[elements] = scan.nextLine();
                if(line[elements].equals("<song>")) {
                    logStack.clear();
                }
                if(line[elements].isEmpty()) {
                    continue;
                }

                logStack.push(line[elements]);
                line[elements] = line[elements].trim();

                if (line[elements].contains("<") && line[elements].contains(">")) {
                    if (line[elements].startsWith("</")) {
                        if (validClosingTag(line[elements].substring(line[elements].indexOf("</"), line[elements].lastIndexOf(">") + 1))) {
                            if (line[elements].equals("</song>") && errorChar == 'f') {
                                state = 'n';
                                while (!logStack.isEmpty()) {
                                    flipStack.push(logStack.pop());
                                }
                                assert out != null;
                                out.println("*********Error********");
                                while (!flipStack.isEmpty()) {
                                    out.println(flipStack.pop());
                                }
                                out.println("");
                                errorChar = 's';
                                title = "";
                                album = "";
                                artist = "";
                            } else if (line[elements].equals("</song>")) {
                                songList.add(new Song(title, artist, album));
                                state = 'n';
                                errorChar = 's';
                                title = album = artist = "";
                            }
                        }
                        else if (line[elements].equals("</song>") && errorChar == 'f') {
                            while (!logStack.isEmpty()) {
                                flipStack.push(logStack.pop());
                            }
                            assert out != null;
                            out.println("*********Error********");
                            while (!flipStack.isEmpty()) {
                                out.println(flipStack.pop());
                            }
                            out.println("");
                            state = 'n';
                            errorChar = 's';
                            title = album = artist = "";
                        }
                    }

                    else if(line[elements].startsWith("<")){
                        if(validOpeningTag(line[elements].substring(line[elements].indexOf("<"), line[elements].indexOf(">")+1))) {
                            if(!((line[elements].lastIndexOf(">")) == (line[elements].length()-1))) {
                                if(!line[elements].contains("</")) {
                                    tagState(line[elements].substring(line[elements].indexOf(">") + 1, line[elements].length()), state);
                                }
                            }
                        }
                    }
                }
                else {
                    if(!openingStack.isEmpty()) {
                        if (!openingStack.peek().equals("<song>")) {
                            tagState(line[elements], state);
                        }
                        else {
                            errorChar = 'f';
                        }
                    }

                }

                if (!line[elements].isEmpty()) {
                    elements++;
                }

            }

            scan.close();
            assert out != null;
            out.close();

        }

    /**
     * Linked Chain Stack Implementation in a Nested Class
     * @param <T> - Variable
     */
    static final class LinkedStack<T> {
            private Node topNode;

        private LinkedStack() {
                topNode = null;
            }

        private void push(T newEntry) {
                topNode = new Node(newEntry, topNode);
            }

        private T peek() {
                if (isEmpty())
                    throw new EmptyStackException();
                else
                    return topNode.getData();
            }

        private T pop() {
                T top = peek();  // Might throw EmptyStackException
                assert (topNode != null);
                topNode = topNode.getNextNode();

                return top;
            }

        private boolean isEmpty() {
                return topNode == null;
            }

        private void clear() {
                topNode = null;
            } // end clear

            class Node {
                private T data;
                private Node next;

                private Node(T dataPortion) {
                    this(dataPortion, null);
                }

                private Node(T dataPortion, Node linkPortion) {
                    data = dataPortion;
                    next = linkPortion;
                }

                private T getData() {
                    return data;
                }

                private void setData(T newData) {
                    data = newData;
                }

                private Node getNextNode() {
                    return next;
                }

                private void setNextNode(Node nextNode) {
                    next = nextNode;
                }
            }
        }
    }