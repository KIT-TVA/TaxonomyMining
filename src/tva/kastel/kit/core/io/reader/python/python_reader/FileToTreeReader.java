package tva.kastel.kit.core.io.reader.python.python_reader;

import jep.Interpreter;
import jep.SharedInterpreter;

/**
 * This class uses Jep (Java embedded python) to parse a python file into an abstract syntax tree (AST).
 *
 * @author David Bumm
 */
public class FileToTreeReader {

    /**
     * this method uses Jep to parse the provided python file into a string of the AST.
     * @param path the absolute path to the desired python file
     * @return The AST as a String
     */
    public String getTreeFromFileAsString(String path) {
        try (Interpreter inter = new SharedInterpreter()) {
            inter.exec("import ast");
            inter.exec("file = open(\"" + path + "\", \"r\")");
            inter.exec("data = file.read()");
            inter.exec("file.close()");
            inter.exec("tree = ast.parse(data)");
            inter.exec("printTree = ast.dump(tree)");
            Object object = inter.getValue("printTree");

            return object.toString();
        }

    }

    /**
     * this method uses Jep to parse the provided python file into a JSON string of the AST.
     * @param path the absolute path to the desired python file
     * @return The AST as a String in the JSON format
     */
    public String getTreeFromFileAsJSON(String path) {
        try (Interpreter inter = new SharedInterpreter()) {
            inter.exec("import json");
            inter.exec("from ast import parse");
            inter.exec("from ast2json import ast2json"); //Required: "pip install ast2json"
            inter.exec("ast = ast2json(parse(open(" + path + ").read()))");
            inter.exec("tree = json.dumps(ast, indent=4)");
            Object object = inter.getValue("tree");

            return object.toString();
        }

    }

}
