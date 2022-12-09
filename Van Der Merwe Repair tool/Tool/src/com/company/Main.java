package com.company;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import dk.brics.automaton.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snt.autorex.*;
import java.io.PrintWriter;

public class Main {
    final static Logger LOGGER = LoggerFactory.getLogger(AutomatonTrans.class);

    public static void main(String[] args) {
	// write your code here

        //String regex = "[a-zA-Z0-9_]+9[0-9]+";
        //String regex = "[a-zA-Z0-9_]*[0-9]*";
        String regex = "request[ -~]header[ -~]((\\w+-?)+\\w+)";
        //String regex ="([-+]?)0*([0-9]*)((\\.[0-9]*)?)(([eE][_+]?[0-9]+)?)";
        //String regex = "^(---|\\+\\+\\+)\\s+([\\S ]*)(?:\\t(.*?)\\s*)?";
        //String regex = "([-+]?)0*(\\d*)((?:\\.\\d*)?)(([eE][-+]?\\d+)?)";
        //String regex = "swh:1:(cnt|dir|rel|rev|snp):[0-9a-f]{40}(;(origin|visit|anchor|path|lines)=\\S+)*";

        //String regex = "()*";

        String orginialReg = regex;
        if(regex.startsWith("^")) regex = regex.substring(1,regex.length());
        if(regex.endsWith("$")) regex =regex.substring(0, regex.length()-1);
        regex = regex.replace(".*","[ -~]*");
        regex = regex.replace(".+","[ -~]+");
        regex = regex.replace("\\W","[;!%*-:{}<>+]");
        regex = regex.replace("\\w","[a-zA-Z0-9_]");
        regex = regex.replace("\\S","[!-~]");
        regex = regex.replace("\\d","[0-9]");
        regex = regex.replace("?:","");
        //String space = "[ "+new String(Character.toChars(0x00D)) +new String(Character.toChars(0x009)) +new String(Character.toChars(0x00a))+"]";
        String space = " ";
        regex = regex.replace("\\s",space);
        RegExp Reg1 = new RegExp(regex);
        Automaton A = Reg1.toAutomaton();
        //System.out.println(Reg1.toString());
        State start = A.getInitialState();
        //System.out.println(start.toString());
        //Set<State> accept= A.getAcceptStates();
       //System.out.println(A.toString());
        //System.out.println("Is Deterministic: "+ A.isDeterministic());
        String s0 = Autorex.getRegexFromAutomaton(A);
        //System.out.println("Regex String: " + s0);
        Automaton A1 = new RegExp(s0).toAutomaton();
//        if(A.equals(A1)){
//            System.out.println("Both automation are same");
//            System.out.println(A1.toString());
//        }
//        else{
//            System.out.println("Both automation are not same");
//        }
        while(true){
            int i =0;
            for(i=0;i<s0.length();i++){
                if(s0.charAt(i)=='(' &&i<s0.length()-1 && s0.charAt(i+1)=='['){
                    int parenEnd = s0.indexOf(')', i+2);
                    //System.out.println("Enter the dragon: "+parenEnd);
                    if(parenEnd!=-1 && !s0.substring(i+1,parenEnd+1).contains("(") && s0.substring(i+1,parenEnd+1).contains("|")){
                        String s1= "";
                        s1+='(';
                        s1+='[';
                        for(int j=i+1;j<parenEnd;j++){
                            if(s0.charAt(j)!='|' && s0.charAt(j)!=']' && s0.charAt(j)!='[')s1+=s0.charAt(j);;
                        }
                        s1+=']';
                        s1+=')';
                        //System.out.println("String to be placed: "+s1);
                        s0 = s0.replace(s0.substring(i,parenEnd+1),s1);
                    }
                    i = parenEnd;
                }


            }
            if(i>=s0.length()-1)break;
        }
        System.out.println("Regex String before: " + s0);
        for(int i=0;i<s0.length();i++){
            if(s0.charAt(i)=='('){
                int parenEnd = s0.indexOf(')', i+1);
                //System.out.println("Enter the dragon: "+i + " "+parenEnd);

                if(parenEnd!=-1 && !s0.substring(i+2,parenEnd).contains("(") && s0.substring(i+1,parenEnd).contains("|")){
                    //System.out.println("We are here: "+parenEnd);
                    String s1= "";
                    s1+='(';
                    s1+='[';
                    for(int j=i+1;j<parenEnd;j++){
                        if(s0.charAt(j)!='|' && s0.charAt(j)!=']' && s0.charAt(j)!='(' && s0.charAt(j)!='[')s1+=s0.charAt(j);;
                    }
                    s1+=']';
                    s1+=')';
                    //System.out.println("String to be placed 2: "+s1);
                    s0 = s0.replace(s0.substring(i,parenEnd+1),s1);
                }


            }
        }


        s0 = s0.replace("(_|[a-z]|[A-Z]|[0-9])","\\w");
        s0 = s0.replace("(_|[A-Z]|[a-z]|[0-9])","\\w");
        s0 = s0.replace("[_A-Z0-9a-z]","\\w");
        s0 = s0.replace("[A-Z_0-9a-z]","\\w");
        s0 = s0.replace("[A-Z_;0-9=a-z]", "\\S");
        s0 = s0.replace("[0-9]","\\d");
        s0 = s0.replace("0-9","\\d");
        if(orginialReg.contains("\\s"))s0 = s0.replace(space,"\\s");
        //System.out.println("Regex String before 2: " + s0);
        for(int i=0;i<s0.length();i++){
            if(s0.charAt(i)=='('){
                int parenEnd = s0.indexOf(')', i+1);
                //System.out.println("Enter the dragon: "+i+ " "+ parenEnd);
                if(parenEnd!=-1 && !s0.substring(i+1,parenEnd).contains("[") && !s0.substring(i+1,parenEnd).contains("+") && !s0.substring(i+1,parenEnd).contains("*") && !s0.substring(i+1,parenEnd).contains("(") && !s0.substring(i+1,parenEnd).contains("|")){
                    //System.out.println("We are here: "+parenEnd);
                    String s1= "";
                    //s1+='(';
                    //s1+='[';
                    for(int j=i+1;j<parenEnd;j++){
                        s1+=s0.charAt(j);;
                    }
                    //s1+=']';
                    //s1+=')';
                    //System.out.println("String to be placed 3: "+s1);
                    s0 = s0.replace(s0.substring(i,parenEnd+1),s1);
                }
            }
        }
//        System.out.println("Regex String before 3: " + s0);
//        for(int i=0;i<s0.length();i++) {
//            int parenEnd1, parenEnd2;
//            if (s0.charAt(i) == '(') {
//                parenEnd1 = s0.indexOf(')', i + 1);
//                if(s0.charAt(parenEnd1+1)=='('){
//                    parenEnd2 = s0.indexOf(')', parenEnd1 + 1);
//                    if (s0.substring(i,parenEnd1+1).equals(s0.substring(parenEnd1+1,parenEnd2+1)) && s0.charAt(parenEnd2+1)=='*'){
//                        String s1 = s0.substring(i,parenEnd1+1) + "+";
//                        s0 = s0.replace(s0.substring(i,parenEnd2+2), s1);
//                    }
//                }
//            }
//        }
        s0 = s0.replace("\\s\\s*","\\s+");
        s0 = s0.replace("\\d\\d*","\\d+");
        s0 = s0.replace("\\w\\w*","\\w+");
        //System.out.println("Fixed Regex String: " + s0);;
        PrintWriter printWriter = new PrintWriter(System.out,true);
        byte[] bytes = s0.getBytes(StandardCharsets.UTF_8);

        String sFixed = new String(bytes, StandardCharsets.UTF_8);
        //char aa = '\u0905';
        printWriter.println("Fixed Regex String: " + sFixed);
    }
}
