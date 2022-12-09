import dk.brics.automaton.*;

import java.io.*;
import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import parser.BRICSLexer;
import parser.BRICSListener;
import parser.BRICSParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;




public class RegexAnalyzer {
    public static boolean checkStarEnd(String regex){
        if(regex.endsWith(".*") || regex.endsWith(".+") || regex.endsWith(".*)") || regex.endsWith(".+)") || regex.endsWith(".*)?") || regex.endsWith(".+)?")){
            return false;
        }
        return true;
    }
    public static void main(String[] args){
        //RegExp Reg1 = new RegExp("(a|a)");
        //System.out.println("Is finite: "+Reg1.toAutomaton().isFinite());
        boolean loop_var = false;
        Scanner in = new Scanner(System.in);
        Stack<String> slStack = new Stack<String>();
        Stack<String> errorPatterns = new Stack<>();
        while(loop_var){
            System.out.println("Please Enter 'ch' for Choice, 'c' for Concat, 's' for Star Rule: ");
            String str = in.nextLine();
//            System.out.println("You entered string "+ str);
            if(str.equals("ch")){
                System.out.println("you have chosen to verify choice");
                System.out.println("Write R1: ");
                String r1 = in.nextLine();
                System.out.println("Write R2: ");
                String r2 = in.nextLine();

                // Regex brics
                RegExp R1 = new RegExp(r1);
                RegExp R2 = new RegExp(r2);
                if(!(R1.toAutomaton().intersection(R2.toAutomaton()).isEmpty())){
                    System.out.println(r1 + "|" + r2 + " is Ambiguous");
                }else{
                    System.out.println(r1 + "|" + r2 + " is not Ambiguous");
                }
            }else if(str.equals("c")){
                System.out.println("you have chosen to verify concat");
                System.out.println("Write R1: ");
                String r1 = in.nextLine();
                System.out.println("Write R2: ");
                String r2 = in.nextLine();



                // Regex brics
                RegExp R1 = new RegExp(r1);
                RegExp R2 = new RegExp(r2);

//                System.out.println("String Rep of R1: " + R1.toAutomaton().toString());
//                System.out.println("String Rep of R2: " +  R2.toAutomaton().toString());
                // overlap matching
                Automaton overlap =  R1.toAutomaton().overlap(R2.toAutomaton());
                System.out.println("String Rep of Overlap: " +  overlap.toString());
                if (!overlap.isEmpty()){
                    System.out.println(r1 + r2 + " is ambiguous");
                    Automaton overlapRepeat = overlap.overlap(overlap);
                    System.out.println("String Rep of Overlap Repeat: " +  overlapRepeat.toString());
                    if(!overlapRepeat.isEmpty()){
                        System.out.println(r1 + r2 + " is infinitely ambiguous");
                        System.out.println("Shortest example:" + overlap.getShortestExample(true));
                    }else{
                        System.out.println(r1 + r2 + " is finitely ambiguous");
                        System.out.println("Set of strings: " + overlap.getFiniteStrings());
                    }
                }else{
                    System.out.println(r1 + r2 + " is not ambiguous");
                }

            }else if(str.equals("s")){
                System.out.println("you have chosen to verify star");
                System.out.println("Write the R in R*: ");
                String r1 = in.nextLine();

                // Regex brics
                RegExp R1 = new RegExp(r1);
                Automaton starAuto = R1.toAutomaton().repeat();
                Automaton overlap =  R1.toAutomaton().overlap(starAuto);

                if(!(R1.toAutomaton().intersection(Automaton.makeEmptyString()).isEmpty())){
                    System.out.println(r1 + "* "+" matches empty string");
                }
                if (!overlap.isEmpty()){
                    System.out.println(r1 + " is infinitely ambiguous");
//                    if(overlap.getFiniteStrings()==null){
//                        System.out.println(r1 + "* is infinitely ambiguous");
//                    }else{
//                        System.out.println(r1 + "* is finitely ambiguous");
//                    }
                }else{
                    System.out.println(r1 + "* is not ambiguous");
                }

            }else{
                System.out.println("Invalid key");
            }

        }

//        System.out.println("Hello world");
//        String regex = "^.*?(\\.|\\:)(([0-9]+\\.[0-9]+)|([0-9a-fA-F]+))$";
//        String regex = "^.*?(?:(?:EXT-X-(KEY):(.+)))";
//        String regex = "(a|aa)*(ab)*d*(a|b)*.*";
 //       String regex = "(\\r|\\n|\\r\\n)*";
        String regex = "a*a*";
//        String regex = ".*a*bc*defgh.*";
//        String regex = "([a-zA-Z0-9_])*:*0([0-9])*";
 //       String regex = "(a|b)*ab(ab)*";
//        String regex = ".*p[^p]+";
//        String regex = "([a-zA-Z0-9_]|[0-9])*";
//        String regex = "<comment><comment>##\t\t}";
//        String regex = "^.*?(Sony)(?:BDP\\\\\\\\/|\\\\\\\\/)?([^ /;\\\\\\\\)]+)[ /;\\\\\\\\)]*";
//        String regex = "^.*?Vector: (a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|x|y|z| )*";
//        String regex = "^.*?minus[(((m|i|n|u|s)))]*";
//        String regex = "^.*?@license|\\* Copyright \\(c\\)";
//        String regex = "^.*?(\\u03BC)+"; // can not handle
//        String regex = ".*01[\\d\\s]*";
//        String regex = "^.*?https?://[^?]+\\?([^]+)";

        String decimal = "[0-9]";
        String whitespace = " ";
        String word = "[a-zA-Z0-9_]";
        String nonwhitespace = "[a-zA-Z0-9_:<>+-]";
        String nonDigits = "[a-zA-Z_:<>+-]";
        regex = regex.replace("\\s\\S", ".");
        regex = regex.replace("\\S\\s", ".");
        regex = regex.replace("\\d\\D", ".");
        regex = regex.replace("\\D\\d", ".");
        regex = regex.replace("\\W\\w", ".");
        regex = regex.replace("\\w\\W", ".");
        regex = regex.replace("\\d", decimal);
        regex = regex.replace("\\s", whitespace);
        regex = regex.replace("\\w", word);
        regex = regex.replace("\\S", nonwhitespace);
        regex = regex.replace("\\D", nonDigits);
        regex = regex.replace("\\W", whitespace);
        regex = regex.replaceAll("\\{\\d+,\\}", "+");

//        String decimal = "(0|1|2|3|4|5|6|7|8|9)";
//        String whitespace = "( |\\t|\\r|\\n|\\f)";
//        String word = "(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|x|y|z|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z|0|1|2|3|4|5|6|7|8|9|_)";

//        regex = regex.replace("\\d", decimal);
//        regex = regex.replace("\\s", whitespace);
//        regex = regex.replace("\\w", word);
//        BufferedReader br = null;
//        File tempFile = new File("G:\\My Drive\\PhD\\Regex Research\\output of Regex Measure\\BricsSupported_V1.txt");
//        int counter = 0;
//        int lineno = 0 ;
//        JSONParser parser1 = new JSONParser();
//        String sCurrentLine;
//        try{
//            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
//            br = new BufferedReader(new FileReader("G:\\My Drive\\PhD\\Regex Research\\output of Regex Measure\\supported_regexes_re.txt"));
//            while ((sCurrentLine = br.readLine()) != null) {
//                try {
//                    System.out.println(lineno +" Record:\t" + sCurrentLine);
//                    lineno +=1;
//                    Object obj;
//                    obj = parser1.parse(sCurrentLine);
//                    JSONObject jsonObject = (JSONObject) obj;
//
//                    String regex1 = (String) jsonObject.get("pattern");
//
//                    if (regex1.startsWith("^")){
//                        String regex2 = "^.*?"
//                                + regex1.substring(1);
//                        RegExp check = new RegExp(regex2);
//                        writer.write(sCurrentLine + System.getProperty("line.separator"));
//                    }else{
//                        String regex2 = "^.*?" + regex1;
//                        RegExp check = new RegExp(regex2);
//                        writer.write(sCurrentLine + System.getProperty("line.separator"));
//                    }
//
////                    if(lineno>150000)break;
//
//
//
//                }catch (Exception e){
//                    counter +=1;
//                }
//            }
//            writer.close();
//        }catch (Exception e){
//            System.out.println("Some error");
//        }
////
//        System.out.println("Non-supported regexes : "+ counter);

//        String regex = "--?([^= ]+)=([^ ]+)?";
//

/*
////  false positives analysis
        try {
            BufferedReader br = null;
            JSONParser parser1 = new JSONParser();
            String sCurrentLine;
            int lineno = 0;
            File temp =  new File("C:\\Users\\adnan\\Desktop\\NA.txt");

            BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
//
//            // Pass 2 on false negatives
//            br = new BufferedReader(new FileReader("C:\\Users\\adnan\\Desktop\\mySLfps.txt"));
//
//            while ((sCurrentLine = br.readLine()) != null) {
//                System.out.println("Record:\t" + sCurrentLine);
//                Object obj;
//                obj = parser1.parse(sCurrentLine);
//                JSONObject jsonObject = (JSONObject) obj;
//
//                String regex = (String) jsonObject.get("pattern");
//                String decimal = "(0|1|2|3|4|5|6|7|8|9)";
//                String whitespace = "( |\\t|\\r|\\n|\\f)";
//                String word = "(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|x|y|z|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z|0|1|2|3|4|5|6|7|8|9|_)";
//                String nonwhitespace = "(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|x|y|z|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z|0|1|2|3|4|5|6|7|8|9|_|=|:|;|-|%)";
//                String nonDigits = "(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|x|y|z|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z| |\\t|\\r|\\n|\\f|_|=|:|;|-)";
//                regex = regex.replace("\\d", decimal);
//                regex = regex.replace("\\s", whitespace);
//                regex = regex.replace("\\w", word);
//                regex = regex.replace("\\S", nonwhitespace);
//                regex = regex.replace("\\D", nonDigits);
//                regex = regex.replace("{1,}", "+");
//                regex = regex.replace("{2,}", "+");
//                regex = regex.replace("{3,}", "+");
//                regex = regex.replace("{4,}", "+");
//                regex = regex.replace("{5,}", "+");
//
//
//                regex = regex.replace("#","");
//                regex = regex.replace("&","");
//                regex = regex.replace("\"","");
//                Globals.patternCheck = regex;
//                System.out.println("Pattern: "+ regex);
//
//                Globals.isSuperlinear = false;
//                Globals.checkConcat = false;
//                try {
//
//                    BRICSLexer lexer = new BRICSLexer(new ANTLRInputStream(regex));
//                    BRICSParser parser = new BRICSParser(new CommonTokenStream(lexer));
//                    //        System.out.println(new Builder.Tree(regex).toStringASCII());
//                    ParseTree tree = parser.regexp();
//                    //        Trees.inspect(tree, parser);
//                    System.err.println(tree.toStringTree(parser));
//
//                    ParseTreeWalker walker = new ParseTreeWalker();
//                    BRICSListener listener = new PrintEverything();
//                    walker.walk(listener, tree);
//
//                    if(Globals.isSuperlinear == true){
//                        System.out.println("The regex is superlinear");
//
//                        if(Globals.checkConcat==true){
//                            Globals.concat_count +=1;
//                        }
//                        Globals.tp_count +=1;
////                            slStack.push(regex);
//
//                    }else {
//                        Globals.fn_count +=1;
//                        slStack.push(regex);
//                        System.out.println("The regex is not superlinear");
//                    }
//
//
//                }catch (Exception e){
//                    //Globals.tn_count +=1;
//                    if(Globals.isSuperlinear==false) {
//                        e.printStackTrace();
//                        Globals.error_count +=1;
//                        errorPatterns.add(regex);
//                    }else {
//                        Globals.tp_count +=1;
//                        //slStack.push(regex);
//                        if(Globals.checkConcat==true){
//                            Globals.concat_count +=1;
//                        }
//                    }
//                }
//                lineno++;
//                System.out.println("Line no:"+ lineno);
////                if(lineno > 1684){
////                    break;
////                }
//            }
            for(int loopi = 1; loopi<=46;loopi++){
//                br = new BufferedReader(new FileReader("G:\\My Drive\\PhD\\Regex Research\\All Regex Data\\2021 Regex ambiguity Theory Data\\Labeled_by_Weidemann\\All_NON_IA_"+ String.valueOf(loopi)+".txt"));
                br = new BufferedReader(new FileReader("G:\\My Drive\\PhD\\Regex Research\\All Regex Data\\2021 Regex ambiguity Theory Data\\Labeled_by_Weidemann\\All_IA_"+String.valueOf(loopi)+".txt"));
                while ((sCurrentLine = br.readLine()) != null) {
                    System.out.println("Record:\t" + sCurrentLine);
                    Object obj;
                    obj = parser1.parse(sCurrentLine);
                    JSONObject jsonObject = (JSONObject) obj;

                    String regex = (String) jsonObject.get("pattern");
//                    regex = regex.replace("#","");
//                    regex = regex.replace("&","");
//                    regex = regex.replace("\"","");
                    String decimal = "[0-9]";
                    String whitespace = " ";
                    String word = "[a-zA-Z0-9_]";
                    String nonwhitespace = "[a-zA-Z0-9_:<>+-]";
                    String nonDigits = "[a-zA-Z_:<>+-]";
                    regex = regex.replace("\\s\\S", ".");
                    regex = regex.replace("\\S\\s", ".");
                    regex = regex.replace("\\d\\D", ".");
                    regex = regex.replace("\\D\\d", ".");
                    regex = regex.replace("\\W\\w", ".");
                    regex = regex.replace("\\w\\W", ".");
                    regex = regex.replace("\\d", decimal);
                    regex = regex.replace("\\s", whitespace);
                    regex = regex.replace("\\w", word);
                    regex = regex.replace("\\S", nonwhitespace);
                    regex = regex.replace("\\D", nonDigits);
                    regex = regex.replace("\\W", decimal);
                    regex = regex.replaceAll("\\{\\d+,\\}", "+");
//                    regex = regex.replaceAll("{2,}", "+");
//                    regex = regex.replaceAll("{3,}", "+");
//                    regex = regex.replaceAll("{4,}", "+");
//                    regex = regex.replaceAll("{5,}", "+");
                    Globals.patternCheck = regex;
                    System.out.println("Pattern: "+ regex);

                    Globals.isSuperlinear = false;
                    Globals.checkConcat = false;
                    try {
                        // non-analyzed

//                        if(lineno <=90000){
//                            lineno++;
//                            continue;
//                        }
//                        if(lineno==100000){
//                            break;
//                        }
//                        if (lineno == 97802){
//                            lineno++;
//                            continue;
//                        }
//                        if(regex.length()>1500){
//                            lineno++;
//                            continue;
//                        }
//                        //NON IA 1-2
//                        if(lineno ==1185 || lineno == 3217 || lineno == 4816 || lineno ==5821){
//                            lineno++;
//                            continue;
//                        }
//                        // NON IA 3-4
//                        if(lineno == 5506){
//                            lineno++;
//                            continue;
//                        }
//                        // NON IA 5-6
//                        if(lineno == 606 || lineno == 715){
//                            lineno++;
//                            continue;
//                        }
//////                         NON IA 7-8
//                        if(lineno ==302 || lineno == 6453){
//                            lineno++;
//                            continue;
//                        }
//                        // NON IA 9-11
//                        if(lineno == 2511){
//                            lineno++;
//                            continue;
//                        }

////                        NON IA 12-13
//                        if(lineno == 967 || lineno == 1783 || lineno == 3969 || lineno == 4243){
//                            lineno++;
//                            continue;
//                        }
//                        // NON IA 14-16
//                        if(lineno == 1306 || lineno == 2198 || lineno== 6531 || lineno == 8046 || lineno == 10291){
//                            lineno++;
//                            continue;
//                        }

                        //NON-IA 17-21
//                        if (lineno == 7395 || lineno == 10625 || lineno == 11260 || lineno == 13254 || lineno == 15943 || lineno == 16982){
//                            lineno++;
//                            continue;
//                        }
//                        if(lineno == 17299 || lineno == 17966){
//                            lineno++;
//                            continue;
//                        }
                        //NON IA 22-25
//                        if (lineno == 478 || lineno == 1848 || lineno == 8134 || lineno ==10975 || lineno == 14054 || lineno == 14111){
//                            lineno++;
//                            continue;
//                        }
                        //NON IA 26-27
//                        if(lineno == 1793 || lineno == 3060 || lineno == 3377 || lineno == 3650|| lineno == 6998 || lineno == 7296 || lineno == 7675){
//                            lineno++;
//                            continue;
//                        }
                        //NON IA 28-30
//                        if(lineno == 3334 || lineno == 6142 || lineno == 6779 || lineno == 8401|| lineno == 10992){
//                            lineno++;
//                            continue;
//                        }
                        // NON IA 31-33
//                        if(lineno == 344 || lineno == 1454|| lineno == 3515|| lineno == 9065|| lineno==9752 || lineno ==9810|| lineno == 11030){
//                            lineno++;
//                            continue;
//                        }
//                        // NON IA 34-36
//                        if (lineno == 3027 || lineno == 4777 || lineno == 5606 || lineno == 11546){
//                            lineno++;
//                            continue;
//                        }
                        // NON IA 37-39
//                        if (lineno == 3343 || lineno == 5286 || lineno == 11731 || lineno == 11792){
//                            lineno++;
//                            continue;
//                        }
                        // NON IA 40-42
//                        if (lineno == 743 || lineno == 6533 || lineno == 7374 || lineno == 11645){
//                            lineno++;
//                            continue;
//                        }
                        // NON IA  43-46
//                        if (lineno == 496 || lineno == 2381 || lineno == 3666 || lineno == 10066){
//                            lineno++;
//                            continue;
//                        }
                        // IA 31-36
//                        if (lineno == 6185){
//                            lineno++;
//                            continue;
//                        }
                        // IA at once
                        if(lineno == 27300){
                            lineno++;
                            continue;
                        }

                        BRICSLexer lexer = new BRICSLexer(new ANTLRInputStream(regex));
                        BRICSParser parser = new BRICSParser(new CommonTokenStream(lexer));
                        //        System.out.println(new Builder.Tree(regex).toStringASCII());
                        ParseTree tree = parser.regexp();
                        //        Trees.inspect(tree, parser);
                        System.err.println(tree.toStringTree(parser));

                        ParseTreeWalker walker = new ParseTreeWalker();
                        BRICSListener listener = new PrintEverything();
                        walker.walk(listener, tree);

                        if(Globals.isSuperlinear == true){
                            System.out.println("The regex is superlinear");

                            if(Globals.checkConcat==true){
                                Globals.concat_count +=1;
                            }
                            Globals.tp_count +=1;
                            slStack.push(regex);
//                            System.out.println("Current line: " + sCurrentLine);
//                            writer.write(sCurrentLine + System.getProperty("line.separator"));

                        }else {
                            Globals.fn_count +=1;
//                            slStack.push(regex);
                            writer.write(sCurrentLine + System.getProperty("line.separator"));
                            System.out.println("The regex is not superlinear");
                        }


                    }catch (Exception e){
                        //Globals.tn_count +=1;
                        if(Globals.isSuperlinear==false) {
                            e.printStackTrace();
                            Globals.error_count +=1;
                            errorPatterns.add(regex);
                        }else {
                            Globals.tp_count +=1;
                            //slStack.push(regex);
                            if(Globals.checkConcat==true){
                                Globals.concat_count +=1;
                            }
                        }
                    }
                    lineno++;
                    System.out.println("Line no:"+ lineno);
//                if(lineno > 1684){
//                    break;
//                }
                }

            }
            writer.close();

        } catch (Exception e){

            // Deal with e as you please.
            //e may be any type of exception at all.
            if(Globals.isSuperlinear==false) {
                e.printStackTrace();
                Globals.error_count +=1;
            }

        }
        System.out.println("TP count: " + Globals.tp_count);
        System.out.println("FN count: " + Globals.fn_count);
        System.out.println("Error count "+ Globals.error_count);
//        System.out.println("False Positives "+ Arrays.toString(slStack.toArray()));
        System.out.println("Star Count1: "+ Globals.star_count_1 + " Examples: "+ Globals.star1);
        System.out.println("Star Count2: "+ Globals.star_count_2 + " Examples: "+ Globals.star2);
        System.out.println("Star Count3: "+ Globals.star_count_3 + " Examples: "+ Globals.star3);
        System.out.println("Concat Count1: "+ Globals.concat_count_1);
        System.out.println("Concat Count2: "+ Globals.concat_count_2);
        System.out.println("Concat Count3: "+ Globals.concat_count_3);

        try {
            PrintWriter writer = new PrintWriter("C:\\Users\\adnan\\Desktop\\IAbyHassan.txt", "UTF-8");
            Iterator<String> iter = slStack.iterator();
            while (iter.hasNext()){
                writer.println("{'pattern': '"+ iter.next() + "'}");
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            PrintWriter writer = new PrintWriter("C:\\Users\\adnan\\Desktop\\errorPatternsIAHassan____.txt", "UTF-8");
            Iterator<String> iter = errorPatterns.iterator();
            while (iter.hasNext()){
                writer.println("{'pattern': '"+ iter.next() + "'}");
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


 */

        BRICSLexer lexer = new BRICSLexer(new ANTLRInputStream(regex));
        BRICSParser parser = new BRICSParser(new CommonTokenStream(lexer));
        //        System.out.println(new Builder.Tree(regex).toStringASCII());
        ParseTree tree = parser.regexp();
        //        Trees.inspect(tree, parser);
        System.err.println(tree.toStringTree(parser));

        ParseTreeWalker walker = new ParseTreeWalker();
        BRICSListener listener = new PrintEverything();
        walker.walk(listener, tree);



    }

}

class PrintEverything implements BRICSListener {

    Stack<String> stack = new Stack<String>();
    Set<String> hash_Set = new HashSet<String>();
    Set<String> hash_stars = new HashSet<>();
    @Override
    public void enterRegexp(BRICSParser.RegexpContext ctx) {
//        System.err.println("entering Regexp");
    }

    @Override
    public void exitRegexp(BRICSParser.RegexpContext ctx) {
//        System.err.println("exiting Regexp");
    }

    @Override
    public void enterBackreference_or_octal(BRICSParser.Backreference_or_octalContext ctx) {

    }

    @Override
    public void exitBackreference_or_octal(BRICSParser.Backreference_or_octalContext ctx) {

    }

    @Override
    public void enterOctal_char(BRICSParser.Octal_charContext ctx) {

    }

    @Override
    public void exitOctal_char(BRICSParser.Octal_charContext ctx) {

    }

    @Override
    public void enterQuantifier_type(BRICSParser.Quantifier_typeContext ctx) {

    }

    @Override
    public void exitQuantifier_type(BRICSParser.Quantifier_typeContext ctx) {

    }

    @Override
    public void enterCallout(BRICSParser.CalloutContext ctx) {

    }

    @Override
    public void exitCallout(BRICSParser.CalloutContext ctx) {

    }

    @Override
    public void enterShared_atom(BRICSParser.Shared_atomContext ctx) {

    }

    @Override
    public void exitShared_atom(BRICSParser.Shared_atomContext ctx) {
        String sharedAtom = ctx.getText();
//        System.out.println("Shared Atom: " + sharedAtom);
        stack.push(sharedAtom);
    }

    @Override
    public void enterShared_atom2(BRICSParser.Shared_atom2Context ctx) {

    }

    @Override
    public void exitShared_atom2(BRICSParser.Shared_atom2Context ctx) {

    }

    @Override
    public void enterConditional(BRICSParser.ConditionalContext ctx) {

    }

    @Override
    public void exitConditional(BRICSParser.ConditionalContext ctx) {

    }

    @Override
    public void enterCharacter_class(BRICSParser.Character_classContext ctx) {

    }

    @Override
    public void exitCharacter_class(BRICSParser.Character_classContext ctx) {
        String charClass = ctx.getText();
        //System.out.println("Quantifier: " + quantifier);
        stack.push(charClass);
    }

    @Override
    public void enterConcatexp(BRICSParser.ConcatexpContext ctx) {
//        System.err.println("entering Concatexp");

    }

    @Override
    public void exitConcatexp(BRICSParser.ConcatexpContext ctx) {
//        System.err.println("exiting Concatexp");
//        System.out.println("Child count: " + ctx.getChildCount());
        if ( ctx.getChildCount()==2 ) {
//            System.out.println("Elements of stack before pop: " + Arrays.toString(stack.toArray()));
            String right = stack.pop();
            String left = stack.pop();
            System.out.println("Concat R1: "+ left + " R2: "+ right);
            stack.push(left+right);
            if(!left.equals("<")){
                RegExp R1 = new RegExp(left);
                RegExp R2 = new RegExp(right);
//                System.out.println("R2 automation "+ R2.toAutomaton());
//                System.out.println("Set of strings in R2: " + R2.toAutomaton().getStrings(5));

//            System.out.println("String Rep of R1: " + R1.toAutomaton().toString());
//            System.out.println("String Rep of R2: " +  R2.toAutomaton().toString());
                // overlap matching
                try{
                    Automaton overlap =  R1.toAutomaton().overlap(R2.toAutomaton());
                    if (!overlap.isEmpty() && (left.contains("*") || left.contains("+")) && (right.contains("*") || right.contains("+"))){ // adding ? check for false positives
//                System.out.println("why stuck here");
//                    System.out.println(r1 + r2 + " is ambiguous");

                        if(!overlap.isFinite()){
                            //Automaton overlapRepeat = overlap.overlap(overlap);
                            if(!overlap.isEmpty()) {
                                System.out.println(left + right + " is infinitely ambiguous");
                                //checking for concat 3
                                String firstStar = right.substring(0,right.indexOf("*")+1);
                                System.out.println("First star part: "+ firstStar);
                                RegExp rFirstStar = new RegExp(firstStar);
                                Automaton overlapCheckForConcat3 =  R1.toAutomaton().overlap(rFirstStar.toAutomaton());
                                if(overlapCheckForConcat3.isEmpty()){
                                    System.out.println(left + right + " is infinitely ambiguous due to concat 3");
                                    Globals.concat_count_3 +=1;
                                    Globals.concat3.add(Globals.patternCheck);
                                }
                                //checking for concat 2
                                Iterator<String> it = hash_stars.iterator();
                                boolean checkFlag = true; // flag to check for concat 2
                                //System.out.println("We are here");
                                while(it.hasNext()){
                                    //System.out.println("We are inside");
                                    String checkStar = it.next();
                                    if(right.contains(checkStar)){
                                        RegExp starConcat2 = new RegExp(checkStar);
                                        Automaton overlapCheckForConcat2 = starConcat2.toAutomaton().overlap(overlap);
                                        if(!overlapCheckForConcat2.isEmpty()){
                                            System.out.println("Overlap found with: "+ checkStar);
                                            int starIndex = right.indexOf(checkStar);
                                            if(starIndex==0)checkFlag = false;
                                        }
                                    }
                                    if(checkFlag==false)break;
                                    //System.out.println("star element: "+checkStar);
                                }
                                if(checkFlag==true){
                                    System.out.println(left + right + " is infinitely ambiguous due to concat 2");
                                    Globals.concat_count_2 +=1;
                                    Globals.concat2.add(Globals.patternCheck);
                                }else{
                                    System.out.println(left + right + " is infinitely ambiguous due to concat 1");
                                    Globals.concat_count_1 +=1;
                                    Globals.concat1.add(Globals.patternCheck);
                                }


                                Globals.isSuperlinear = true;
                                Globals.checkConcat = true;
                                //System.out.println("Shortest example:" + overlap.getShortestExample(true));
                            }
                        }else{
                            System.out.println(left + right + " is finitely ambiguous");
                            String toAddStr1 = "(" + left + ")" + "(" + right + ")";
                            String toAddStr2 = "((" + left + ")" + "(" + right + "))";
                            hash_Set.add(toAddStr1);
                            hash_Set.add(toAddStr2);
//                    System.out.println("Set of strings: " + overlap.getFiniteStrings());
                        }
                    }else{
                        System.out.println(left + right + " is not ambiguous");
                    }

                }catch (Exception e){
                    System.out.println("Error in concat");
//                    Globals.isSuperlinear = true;
//                    Globals.checkConcat = true;
                }
            }


//            System.out.println("Elements of stack after pop: " + Arrays.toString(stack.toArray()));
        }
    }

    @Override
    public void enterLiteral(BRICSParser.LiteralContext ctx) {

    }

    @Override
    public void exitLiteral(BRICSParser.LiteralContext ctx) {
        String letter = ctx.getText();
//        System.out.println("Here: "+ letter);
        stack.push(letter);
    }

    @Override
    public void enterNumber(BRICSParser.NumberContext ctx) {

    }

    @Override
    public void exitNumber(BRICSParser.NumberContext ctx) {

    }

    @Override
    public void enterRepeatexp(BRICSParser.RepeatexpContext ctx) {

    }

    @Override
    public void exitRepeatexp(BRICSParser.RepeatexpContext ctx) {
        if ( ctx.getChildCount()==2 ) {
//            System.out.println("Elements of stack before push(Repeatexp): " + Arrays.toString(stack.toArray()));
            String quantifier = ctx.getChild(0).getText();
            stack.push(quantifier);
            String right = stack.pop();
            System.out.println("Right: " + right);
            String left = stack.pop();
            System.out.println("Left: " + left);
            String dummy = stack.pop();
            RegExp R1 = new RegExp(right);
//            System.out.println("Elements of stack after pop(Repeatexp): " + Arrays.toString(stack.toArray()));
            String Star = right + left;
            System.out.println("Star R: "+ Star);
            hash_stars.add(Star);
            stack.push(right+left);
            Automaton starAuto = R1.toAutomaton().repeat();
            Automaton overlap =  R1.toAutomaton().overlap(starAuto);
            if(hash_Set.contains(right)){
                Globals.isSuperlinear = true;
                System.out.println(Star + " is infinitely ambiguous due to star 1");
                Globals.star_count_1 +=1;
                Globals.star1.add(Globals.patternCheck);
            }
            else if (!R1.toAutomaton().overlap(R1.toAutomaton()).isEmpty() && !right.contains("@")){
                Globals.isSuperlinear =true;
                System.out.println(Star + " is infinitely ambiguous due to star 3");
                Globals.star_count_3 +=1;
                Globals.star3.add(Globals.patternCheck);
            }
            else if(!overlap.isEmpty()){
                Globals.isSuperlinear =true;
                System.out.println(Star + " is infinitely ambiguous due to star 2");
                Globals.star_count_2 +=1;
                Globals.star2.add(Globals.patternCheck);
            }
//            System.out.println("Elements of stack after push(Repeatexp): " + Arrays.toString(stack.toArray()));
        }
    }

    @Override
    public void enterOption_flags(BRICSParser.Option_flagsContext ctx) {

    }

    @Override
    public void exitOption_flags(BRICSParser.Option_flagsContext ctx) {

    }

    @Override
    public void enterNon_capture(BRICSParser.Non_captureContext ctx) {

    }

    @Override
    public void exitNon_capture(BRICSParser.Non_captureContext ctx) {
//        String non_capture_string = ctx.getText();
//        //System.out.println("Quantifier: " + quantifier);
//        stack.push(non_capture_string);
    }

    @Override
    public void enterAlpha_nums(BRICSParser.Alpha_numsContext ctx) {

    }

    @Override
    public void exitAlpha_nums(BRICSParser.Alpha_numsContext ctx) {

    }

    @Override
    public void enterCc_literal(BRICSParser.Cc_literalContext ctx) {

    }

    @Override
    public void exitCc_literal(BRICSParser.Cc_literalContext ctx) {

    }

    @Override
    public void enterDigit(BRICSParser.DigitContext ctx) {

    }

    @Override
    public void exitDigit(BRICSParser.DigitContext ctx) {

    }

    @Override
    public void enterBacktrack_control(BRICSParser.Backtrack_controlContext ctx) {

    }

    @Override
    public void exitBacktrack_control(BRICSParser.Backtrack_controlContext ctx) {

    }

    @Override
    public void enterNewline_convention(BRICSParser.Newline_conventionContext ctx) {

    }

    @Override
    public void exitNewline_convention(BRICSParser.Newline_conventionContext ctx) {

    }

    @Override
    public void enterLook_around(BRICSParser.Look_aroundContext ctx) {

    }

    @Override
    public void exitLook_around(BRICSParser.Look_aroundContext ctx) {

    }

    @Override
    public void enterNon_close_paren(BRICSParser.Non_close_parenContext ctx) {

    }

    @Override
    public void exitNon_close_paren(BRICSParser.Non_close_parenContext ctx) {

    }

    @Override
    public void enterCapture(BRICSParser.CaptureContext ctx) {

    }

    @Override
    public void exitCapture(BRICSParser.CaptureContext ctx) {
//        String capture_string = ctx.getText();
////        System.out.println("Capture String: " + capture_string);
//        stack.push(capture_string);
    }

    @Override
    public void enterOption_flag(BRICSParser.Option_flagContext ctx) {

    }

    @Override
    public void exitOption_flag(BRICSParser.Option_flagContext ctx) {

    }

    @Override
    public void enterQuantifier(BRICSParser.QuantifierContext ctx) {

    }

    @Override
    public void exitQuantifier(BRICSParser.QuantifierContext ctx) {
        String quantifier = ctx.getText();
        //System.out.println("Quantifier: " + quantifier);
        stack.push(quantifier);
//        if ( ctx.getChildCount()==2 ) {
//            String quantifier = ctx.getChild(0).getText();
//            stack.push(quantifier);
//        }
    }

    @Override
    public void enterOctal_digit(BRICSParser.Octal_digitContext ctx) {

    }

    @Override
    public void exitOctal_digit(BRICSParser.Octal_digitContext ctx) {

    }

    @Override
    public void enterSubroutine_reference(BRICSParser.Subroutine_referenceContext ctx) {

    }

    @Override
    public void exitSubroutine_reference(BRICSParser.Subroutine_referenceContext ctx) {

    }

    @Override
    public void enterNon_close_parens(BRICSParser.Non_close_parensContext ctx) {

    }

    @Override
    public void exitNon_close_parens(BRICSParser.Non_close_parensContext ctx) {

    }

    @Override
    public void enterCc_atom(BRICSParser.Cc_atomContext ctx) {

    }

    @Override
    public void exitCc_atom(BRICSParser.Cc_atomContext ctx) {

    }

    @Override
    public void enterBackreference(BRICSParser.BackreferenceContext ctx) {

    }

    @Override
    public void exitBackreference(BRICSParser.BackreferenceContext ctx) {

    }


    @Override
    public void enterName(BRICSParser.NameContext ctx) {

    }

    @Override
    public void exitName(BRICSParser.NameContext ctx) {

    }

    @Override
    public void enterComment(BRICSParser.CommentContext ctx) {

    }

    @Override
    public void exitComment(BRICSParser.CommentContext ctx) {

    }

    @Override
    public void enterDigits(BRICSParser.DigitsContext ctx) {

    }

    @Override
    public void exitDigits(BRICSParser.DigitsContext ctx) {

    }

    @Override
    public void enterAtom(BRICSParser.AtomContext ctx) {

    }

    @Override
    public void exitAtom(BRICSParser.AtomContext ctx) {
//        String atom = ctx.getText();
//        System.out.println("Atom: " + atom);
//        stack.push(atom);

        if (ctx.Dot() != null) {
            String dot = ctx.Dot().getText();
//            System.out.println("Atom Dot: " + dot + " "+ ctx.getText());
            stack.push(dot);
        }
        if (ctx.Caret() != null) {
            String dot = ctx.Caret().getText();
//            System.out.println("Atom Caret: " + dot + " "+ ctx.getText());
            stack.push(dot);
        }
        if (ctx.EndOfSubjectOrLine() != null){
            String dot = ctx.EndOfSubjectOrLine().getText();
//            System.out.println("Atom Dot: " + dot + " "+ ctx.getText());
            stack.push(dot);
        }
        if(ctx.StartOfSubject()!= null){
            String startSubject = ctx.StartOfSubject().getText();
            stack.push(startSubject);
        }
        if(ctx.EndOfSubject()!= null){
            String endSubject = ctx.EndOfSubject().getText();
            stack.push(endSubject);
        }
        if(ctx.PreviousMatchInSubject()!= null){
            String matchSubject = ctx.PreviousMatchInSubject().getText();
            stack.push(matchSubject);
        }
        if(ctx.ResetStartMatch()!=null){
            String restartMatch = ctx.ResetStartMatch().getText();
            stack.push(restartMatch);
        }
    }

    @Override
    public void enterUnionexp(BRICSParser.UnionexpContext ctx) {

    }

    @Override
    public void exitUnionexp(BRICSParser.UnionexpContext ctx) {
        if ( ctx.getChildCount()==3 ) {
//            System.out.println("Elements of stack before pop: " + Arrays.toString(stack.toArray()));
            String right = stack.pop();
//            String middle = stack.pop();
            String left = stack.pop();

            RegExp R1 = new RegExp(left);
            RegExp R2 = new RegExp(right);
            System.out.println("Union R1: "+ left +" R2: "+ right);
            if(!(R1.toAutomaton().intersection(R2.toAutomaton()).isEmpty())){
                System.out.println(left + "|" + right + " is finitely Ambiguous");
                hash_Set.add("("+left + "|" + right + ")");
            }else{
                System.out.println(left + "|" + right + " is not Ambiguous");
            }

//            ctx.op.getType()
            stack.push("("+left + "|" + right + ")");
//            System.out.println("Elements of stack after pop: " + Arrays.toString(stack.toArray()));
        }

    }

    @Override
    public void enterShared_literal(BRICSParser.Shared_literalContext ctx) {

    }

    @Override
    public void exitShared_literal(BRICSParser.Shared_literalContext ctx) {
//        if (ctx.Letter() != null) {
//            String letter = ctx.Letter().getText();
//            System.out.println("Here ");
//            stack.push(letter);
//        }
    }

    @Override
    public void enterInterexp(BRICSParser.InterexpContext ctx) {

    }

    @Override
    public void exitInterexp(BRICSParser.InterexpContext ctx) {

    }

    @Override
    public void enterOption(BRICSParser.OptionContext ctx) {

    }

    @Override
    public void exitOption(BRICSParser.OptionContext ctx) {

    }



    @Override
    public void visitTerminal(TerminalNode terminal) {

    }

    // don't need these here, so just make them empty implementations
    @Override public void enterEveryRule(ParserRuleContext context) { }
    @Override public void exitEveryRule(ParserRuleContext context) { }
    @Override public void visitErrorNode(ErrorNode node) { }
}
