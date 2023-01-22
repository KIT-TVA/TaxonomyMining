package tva.kastel.kit.core.io.reader.python.python_reader;

import jep.Interpreter;
import jep.SharedInterpreter;

public class FileToTreeReader {
	
	
	public String getTreeFromFileMocked(String path) {
		return "{\n" +
				"    \"_type\": \"Module\",\n" +
				"    \"body\": [\n" +
				"        {\n" +
				"            \"_type\": \"Import\",\n" +
				"            \"col_offset\": 0,\n" +
				"            \"end_col_offset\": 11,\n" +
				"            \"end_lineno\": 1,\n" +
				"            \"lineno\": 1,\n" +
				"            \"names\": [\n" +
				"                {\n" +
				"                    \"_type\": \"alias\",\n" +
				"                    \"asname\": null,\n" +
				"                    \"col_offset\": 7,\n" +
				"                    \"end_col_offset\": 11,\n" +
				"                    \"end_lineno\": 1,\n" +
				"                    \"lineno\": 1,\n" +
				"                    \"name\": \"json\"\n" +
				"                }\n" +
				"            ]\n" +
				"        },\n" +
				"        {\n" +
				"            \"_type\": \"ImportFrom\",\n" +
				"            \"col_offset\": 0,\n" +
				"            \"end_col_offset\": 21,\n" +
				"            \"end_lineno\": 2,\n" +
				"            \"level\": 0,\n" +
				"            \"lineno\": 2,\n" +
				"            \"module\": \"ast\",\n" +
				"            \"names\": [\n" +
				"                {\n" +
				"                    \"_type\": \"alias\",\n" +
				"                    \"asname\": null,\n" +
				"                    \"col_offset\": 16,\n" +
				"                    \"end_col_offset\": 21,\n" +
				"                    \"end_lineno\": 2,\n" +
				"                    \"lineno\": 2,\n" +
				"                    \"name\": \"parse\"\n" +
				"                }\n" +
				"            ]\n" +
				"        },\n" +
				"        {\n" +
				"            \"_type\": \"ImportFrom\",\n" +
				"            \"col_offset\": 0,\n" +
				"            \"end_col_offset\": 29,\n" +
				"            \"end_lineno\": 3,\n" +
				"            \"level\": 0,\n" +
				"            \"lineno\": 3,\n" +
				"            \"module\": \"ast2json\",\n" +
				"            \"names\": [\n" +
				"                {\n" +
				"                    \"_type\": \"alias\",\n" +
				"                    \"asname\": null,\n" +
				"                    \"col_offset\": 21,\n" +
				"                    \"end_col_offset\": 29,\n" +
				"                    \"end_lineno\": 3,\n" +
				"                    \"lineno\": 3,\n" +
				"                    \"name\": \"ast2json\"\n" +
				"                }\n" +
				"            ]\n" +
				"        },\n" +
				"        {\n" +
				"            \"_type\": \"Assign\",\n" +
				"            \"col_offset\": 0,\n" +
				"            \"end_col_offset\": 92,\n" +
				"            \"end_lineno\": 5,\n" +
				"            \"lineno\": 5,\n" +
				"            \"targets\": [\n" +
				"                {\n" +
				"                    \"_type\": \"Name\",\n" +
				"                    \"col_offset\": 0,\n" +
				"                    \"ctx\": {\n" +
				"                        \"_type\": \"Store\"\n" +
				"                    },\n" +
				"                    \"end_col_offset\": 3,\n" +
				"                    \"end_lineno\": 5,\n" +
				"                    \"id\": \"ast\",\n" +
				"                    \"lineno\": 5\n" +
				"                }\n" +
				"            ],\n" +
				"            \"type_comment\": null,\n" +
				"            \"value\": {\n" +
				"                \"_type\": \"Call\",\n" +
				"                \"args\": [\n" +
				"                    {\n" +
				"                        \"_type\": \"Call\",\n" +
				"                        \"args\": [\n" +
				"                            {\n" +
				"                                \"_type\": \"Call\",\n" +
				"                                \"args\": [],\n" +
				"                                \"col_offset\": 21,\n" +
				"                                \"end_col_offset\": 90,\n" +
				"                                \"end_lineno\": 5,\n" +
				"                                \"func\": {\n" +
				"                                    \"_type\": \"Attribute\",\n" +
				"                                    \"attr\": \"read\",\n" +
				"                                    \"col_offset\": 21,\n" +
				"                                    \"ctx\": {\n" +
				"                                        \"_type\": \"Load\"\n" +
				"                                    },\n" +
				"                                    \"end_col_offset\": 88,\n" +
				"                                    \"end_lineno\": 5,\n" +
				"                                    \"lineno\": 5,\n" +
				"                                    \"value\": {\n" +
				"                                        \"_type\": \"Call\",\n" +
				"                                        \"args\": [\n" +
				"                                            {\n" +
				"                                                \"_type\": \"Constant\",\n" +
				"                                                \"col_offset\": 26,\n" +
				"                                                \"end_col_offset\": 82,\n" +
				"                                                \"end_lineno\": 5,\n" +
				"                                                \"kind\": null,\n" +
				"                                                \"lineno\": 5,\n" +
				"                                                \"n\": \"C:/Users/david/Documents/Informatik/Hiwi/JsonParser.py\",\n" +
				"                                                \"s\": \"C:/Users/david/Documents/Informatik/Hiwi/JsonParser.py\",\n" +
				"                                                \"value\": \"C:/Users/david/Documents/Informatik/Hiwi/JsonParser.py\"\n" +
				"                                            }\n" +
				"                                        ],\n" +
				"                                        \"col_offset\": 21,\n" +
				"                                        \"end_col_offset\": 83,\n" +
				"                                        \"end_lineno\": 5,\n" +
				"                                        \"func\": {\n" +
				"                                            \"_type\": \"Name\",\n" +
				"                                            \"col_offset\": 21,\n" +
				"                                            \"ctx\": {\n" +
				"                                                \"_type\": \"Load\"\n" +
				"                                            },\n" +
				"                                            \"end_col_offset\": 25,\n" +
				"                                            \"end_lineno\": 5,\n" +
				"                                            \"id\": \"open\",\n" +
				"                                            \"lineno\": 5\n" +
				"                                        },\n" +
				"                                        \"keywords\": [],\n" +
				"                                        \"lineno\": 5\n" +
				"                                    }\n" +
				"                                },\n" +
				"                                \"keywords\": [],\n" +
				"                                \"lineno\": 5\n" +
				"                            }\n" +
				"                        ],\n" +
				"                        \"col_offset\": 15,\n" +
				"                        \"end_col_offset\": 91,\n" +
				"                        \"end_lineno\": 5,\n" +
				"                        \"func\": {\n" +
				"                            \"_type\": \"Name\",\n" +
				"                            \"col_offset\": 15,\n" +
				"                            \"ctx\": {\n" +
				"                                \"_type\": \"Load\"\n" +
				"                            },\n" +
				"                            \"end_col_offset\": 20,\n" +
				"                            \"end_lineno\": 5,\n" +
				"                            \"id\": \"parse\",\n" +
				"                            \"lineno\": 5\n" +
				"                        },\n" +
				"                        \"keywords\": [],\n" +
				"                        \"lineno\": 5\n" +
				"                    }\n" +
				"                ],\n" +
				"                \"col_offset\": 6,\n" +
				"                \"end_col_offset\": 92,\n" +
				"                \"end_lineno\": 5,\n" +
				"                \"func\": {\n" +
				"                    \"_type\": \"Name\",\n" +
				"                    \"col_offset\": 6,\n" +
				"                    \"ctx\": {\n" +
				"                        \"_type\": \"Load\"\n" +
				"                    },\n" +
				"                    \"end_col_offset\": 14,\n" +
				"                    \"end_lineno\": 5,\n" +
				"                    \"id\": \"ast2json\",\n" +
				"                    \"lineno\": 5\n" +
				"                },\n" +
				"                \"keywords\": [],\n" +
				"                \"lineno\": 5\n" +
				"            }\n" +
				"        },\n" +
				"        {\n" +
				"            \"_type\": \"Expr\",\n" +
				"            \"col_offset\": 0,\n" +
				"            \"end_col_offset\": 32,\n" +
				"            \"end_lineno\": 6,\n" +
				"            \"lineno\": 6,\n" +
				"            \"value\": {\n" +
				"                \"_type\": \"Call\",\n" +
				"                \"args\": [\n" +
				"                    {\n" +
				"                        \"_type\": \"Call\",\n" +
				"                        \"args\": [\n" +
				"                            {\n" +
				"                                \"_type\": \"Name\",\n" +
				"                                \"col_offset\": 17,\n" +
				"                                \"ctx\": {\n" +
				"                                    \"_type\": \"Load\"\n" +
				"                                },\n" +
				"                                \"end_col_offset\": 20,\n" +
				"                                \"end_lineno\": 6,\n" +
				"                                \"id\": \"ast\",\n" +
				"                                \"lineno\": 6\n" +
				"                            }\n" +
				"                        ],\n" +
				"                        \"col_offset\": 6,\n" +
				"                        \"end_col_offset\": 31,\n" +
				"                        \"end_lineno\": 6,\n" +
				"                        \"func\": {\n" +
				"                            \"_type\": \"Attribute\",\n" +
				"                            \"attr\": \"dumps\",\n" +
				"                            \"col_offset\": 6,\n" +
				"                            \"ctx\": {\n" +
				"                                \"_type\": \"Load\"\n" +
				"                            },\n" +
				"                            \"end_col_offset\": 16,\n" +
				"                            \"end_lineno\": 6,\n" +
				"                            \"lineno\": 6,\n" +
				"                            \"value\": {\n" +
				"                                \"_type\": \"Name\",\n" +
				"                                \"col_offset\": 6,\n" +
				"                                \"ctx\": {\n" +
				"                                    \"_type\": \"Load\"\n" +
				"                                },\n" +
				"                                \"end_col_offset\": 10,\n" +
				"                                \"end_lineno\": 6,\n" +
				"                                \"id\": \"json\",\n" +
				"                                \"lineno\": 6\n" +
				"                            }\n" +
				"                        },\n" +
				"                        \"keywords\": [\n" +
				"                            {\n" +
				"                                \"_type\": \"keyword\",\n" +
				"                                \"arg\": \"indent\",\n" +
				"                                \"col_offset\": 22,\n" +
				"                                \"end_col_offset\": 30,\n" +
				"                                \"end_lineno\": 6,\n" +
				"                                \"lineno\": 6,\n" +
				"                                \"value\": {\n" +
				"                                    \"_type\": \"Constant\",\n" +
				"                                    \"col_offset\": 29,\n" +
				"                                    \"end_col_offset\": 30,\n" +
				"                                    \"end_lineno\": 6,\n" +
				"                                    \"kind\": null,\n" +
				"                                    \"lineno\": 6,\n" +
				"                                    \"n\": 4,\n" +
				"                                    \"s\": 4,\n" +
				"                                    \"value\": 4\n" +
				"                                }\n" +
				"                            }\n" +
				"                        ],\n" +
				"                        \"lineno\": 6\n" +
				"                    }\n" +
				"                ],\n" +
				"                \"col_offset\": 0,\n" +
				"                \"end_col_offset\": 32,\n" +
				"                \"end_lineno\": 6,\n" +
				"                \"func\": {\n" +
				"                    \"_type\": \"Name\",\n" +
				"                    \"col_offset\": 0,\n" +
				"                    \"ctx\": {\n" +
				"                        \"_type\": \"Load\"\n" +
				"                    },\n" +
				"                    \"end_col_offset\": 5,\n" +
				"                    \"end_lineno\": 6,\n" +
				"                    \"id\": \"print\",\n" +
				"                    \"lineno\": 6\n" +
				"                },\n" +
				"                \"keywords\": [],\n" +
				"                \"lineno\": 6\n" +
				"            }\n" +
				"        }\n" +
				"    ],\n" +
				"    \"type_ignores\": []\n" +
				"}";	}
	
	
	public String getTreeFromFileAsString(String path) {
		try (Interpreter interp = new SharedInterpreter()) {
			 interp.exec("import ast");
			 interp.exec("file = open(\"" + path + "\", \"r\")");
			 interp.exec("data = file.read()");
			 interp.exec("file.close()");
			 interp.exec("tree = ast.parse(data)");
			 interp.exec("printTree = ast.dump(tree)");
			 Object object =  interp.getValue("printTree");
			 
			 return object.toString();
		}

	}
	
	public String getTreeFromFileAsJSON(String path) {
		try (Interpreter interp = new SharedInterpreter()) {
			 interp.exec("import json");
			 interp.exec("from ast import parse");
			 interp.exec("from ast2json import ast2json");
			 interp.exec("ast = ast2json(parse(open(\"C:/Users/david/Documents/Informatik/Hiwi/commands.py\").read()))");
			 interp.exec("tree = json.dumps(ast, indent=4)");
			 Object object = interp.getValue("tree");
			 
			 return object.toString();
		}

	}

}
