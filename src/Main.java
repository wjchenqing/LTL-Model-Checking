import BA.NBA;
import TS.State;
import TS.TS;
import Util.Pair;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import BA.GNBA;
import LTL.FormulaNode;
import LTL.UnaryOpNode;
import Parser.Parser;

public class Main {
    public static void main(String[] args) {

        //Read in TS
        TS ts = parseTS();

        //parse input
        ArrayList<FormulaNode> initFormulas = new ArrayList<>();
        ArrayList<Pair<Integer, FormulaNode>> statesWithFormulas = new ArrayList<>();
        parseFormulas(initFormulas, statesWithFormulas);

        //Start model checking
        for (FormulaNode formula : initFormulas) {
            System.out.println(process(ts, formula));
        }
        for (Pair<Integer, FormulaNode> pair: statesWithFormulas) {
            for (int i = 0; i < ts.getStates().size(); i++) {
                ts.getStates().get(i).setIs_initial(i == pair.getFirst());
            }
            System.out.println(process(ts, pair.getSecond()));
        }


    }

    private static TS parseTS() {
        InputStream inputStream;
        Scanner scanner = null;
        try {
            inputStream = new FileInputStream("TS.txt");
//            inputStream = System.in;
            scanner = new Scanner(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }

        return new TS(scanner);
    }

    private static void parseFormulas(ArrayList<FormulaNode> initFormulas, ArrayList<Pair<Integer, FormulaNode>> statesWithFormulas) {
        InputStream inputStream;
        Scanner scanner = null;

        try {
            inputStream = new FileInputStream("benchmark.txt");
//            inputStream = System.in;
            scanner = new Scanner(inputStream);
        } catch (Exception e) {
            assert false;
        }

        int a = scanner.nextInt();
        int b = scanner.nextInt();

        scanner.nextLine();

        for (int i = 0; i < a; i++) {
            String str = scanner.nextLine();
            Parser parser = new Parser();
            initFormulas.add(parser.ParseInput(str));
        }

        for (int i = 0; i < b; i++) {
            Integer s = scanner.nextInt();
            String str = scanner.nextLine();
            Parser parser = new Parser();
            statesWithFormulas.add(new Pair<Integer,FormulaNode>(s, parser.ParseInput(str)));
        }
    }

    private static int process(TS ts, FormulaNode formula) {
        //take negation
        FormulaNode neg = formula.negation();
        //construct GNBA
        GNBA gnba = new GNBA(neg);
        //construct NBA
        NBA nba = new NBA(gnba);
        //Product
        TS productTS = new TS(ts, nba);
        //nested depth-first search algorithm
        return productTS.PersistenceChecking();
    }

}