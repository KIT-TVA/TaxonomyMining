package tva.kastel.kit.core.io.reader.python.python_reader;

import jep.Interpreter;
import jep.SharedInterpreter;

public class FileToTreeReader {

    public String getTreeFromFileAsString(String path) {
        try (Interpreter interp = new SharedInterpreter()) {
            interp.exec("import ast");
            interp.exec("file = open(\"" + path + "\", \"r\")");
            interp.exec("data = file.read()");
            interp.exec("file.close()");
            interp.exec("tree = ast.parse(data)");
            interp.exec("printTree = ast.dump(tree)");
            Object object = interp.getValue("printTree");

            return object.toString();
        }

    }

    public String getTreeFromFileAsJSON(String path) {
        try (Interpreter interp = new SharedInterpreter()) {
            interp.exec("import json");
            interp.exec("from ast import parse");
            interp.exec("from ast2json import ast2json"); //Required: "pip install ast2json"
            interp.exec("ast = ast2json(parse(open(" + path + ").read()))");
            interp.exec("tree = json.dumps(ast, indent=4)");
            Object object = interp.getValue("tree");

            return object.toString();
        }

    }

}
