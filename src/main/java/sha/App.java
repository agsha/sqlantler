package sha;


import cz.startnet.utils.pgdiff.parsers.antlr.SQLLexer;
import cz.startnet.utils.pgdiff.parsers.antlr.SQLParser;
import foo.PlSqlLexer;
import foo.PlSqlParser;
import mysql.MySqlLexer;
import mysql.MySqlParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pg.PostgreSQLLexer;
import pg.PostgreSQLParser;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;

import static sha.Utils.readJsonFromClasspath;

public class App
{
    private static final Logger log = LogManager.getLogger();
    private static Settings s;

    public static void main( String[] args ) {
        try {
            App obj = new App();


            String path = "settings.json";

            try {
                if(args.length>0) {
                    path = args[0];
                }
                s = readJsonFromClasspath(path, Settings.class);
            } catch (Exception e) {
                s = new Settings();
            }
//            log.info("Using settings:{}", dumps(s));
            obj.go();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static class Settings {
//        public String input = "stdin";
        public String input = "/home/sharath/code/sqlantlr/src/main/resources/query2.sql";
        // required for jackson
        public Settings() {
        }
    }


    /**
     * All teh code from here:
     */
    private void go() throws Exception {
        InputStream is = null;
        if(s.input.equals("stdin")) {
            is = System.in;
        } else {
            is = Files.newInputStream(Paths.get(s.input));
        }
        String query = IOUtils.toString(is).toUpperCase().replace("DATE(", "SUBSTR(");

        CharStream charStream = CharStreams.fromString(query);
        SQLLexer lexer = new SQLLexer(charStream);
        TokenStream tokens = new CommonTokenStream(lexer);
        SQLParser parser = new SQLParser(tokens);
        ParseTree root = parser.sql();

        HashSet<String> set = dfs(root, new HashSet<>());
        for (String s : set) {
//            System.out.println(s);
        }

    }

    public HashSet<String> dfs(ParseTree root, HashSet<String> set) {
        set.add(root.getClass().getSimpleName());
        String name = root.getClass().getSimpleName();
        if(name.equals("Schema_qualified_nameContext") ) {
            System.out.println(root.getText());
        }
        for(int i=0; i<root.getChildCount(); i++) {
            dfs(root.getChild(i), set);
        }
        return set;
    }
}
