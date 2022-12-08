package tva.kastel.kit.core.io.reader.java;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.ReceiverParameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.SwitchExpr;
import com.github.javaparser.ast.expr.TextBlockLiteralExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithArguments;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.UnparsableStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.stmt.YieldStmt;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.IntersectionType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.ast.type.VarType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import com.github.javaparser.ast.visitor.VoidVisitor;

import tva.kastel.kit.core.model.enums.NodeType;
import tva.kastel.kit.core.model.impl.NodeImpl;
import tva.kastel.kit.core.model.impl.StringValueImpl;
import tva.kastel.kit.core.model.interfaces.Node;
import tva.kastel.kit.core.io.reader.java.factory.JavaReaderUtil;
import tva.kastel.kit.core.io.reader.java.factory.NodeFactory;
import tva.kastel.kit.core.io.reader.java.templates.AbstractJavaVisitor;

/**
 * Custom visitor class extending {@link VoidVisitor} creating a {@link Node}
 * for each {@link com.github.javaparser.ast.Node} visited.
 *
 * @author Serkan Acar
 * @author Pascal Blum
 * @author Paulo Haas
 * @author Hassan Smaoui
 * @see <a href=
 * "https://www.javadoc.io/doc/com.github.javaparser/javaparser-core/latest/com/github/javaparser/ast/visitor/VoidVisitorAdapter.html">Javaparser
 * Docs - VoidVisitorAdapter</a>
 */
public class JavaVisitor extends AbstractJavaVisitor {
    private NodeFactory factory;

    public JavaVisitor(NodeFactory factory) {
        this.setFactory(factory);
    }


    private void visitArguments(@SuppressWarnings("rawtypes") NodeWithArguments args, Node parent) {
        this.factory.attachArguments(args, parent, this);
    }


    @Override
    public void visit(CompilationUnit compilationUnit, Node parent) {
        visitor(compilationUnit, this.factory.createCompilationUnitNode(compilationUnit, parent, this));
    }


    @Override
    public void visit(MethodDeclaration methodDecl, Node parent) {
        visitor(methodDecl, this.factory.createMethodDeclNode(methodDecl, parent, this), methodDecl.getType());
    }


    @Override
    public void visit(Modifier n, Node arg) {
        if (JavaReaderUtil.isAccsessModifier(n.getKeyword().name())) {
            arg.addAttribute(JavaAttributesTypes.AccessModifier.name(), new StringValueImpl(n.getKeyword().name()));
        } else {
            arg.addAttribute(n.getKeyword().name() + "_" + JavaAttributesTypes.Modifer.name(),
                    new StringValueImpl(n.getKeyword().name()));
        }
    }


    @Override
    public void visit(SimpleName n, Node arg) {
        arg.addAttribute(JavaAttributesTypes.Name.name(), new StringValueImpl(n.toString()));
    }


    @Override
    public void visit(ClassOrInterfaceDeclaration classOrInterfaceDecl, Node parent) {
        visitor(classOrInterfaceDecl, this.factory.createClassOrInterfaceNode(classOrInterfaceDecl, parent));
    }


    @Override
    public void visit(ClassOrInterfaceType n, Node arg) {
        visitor(n, arg);
    }


    @Override
    public void visit(AnnotationDeclaration n, Node arg) {
        visitor(n, new NodeImpl(NodeType.ANNOTATION, JavaNodeTypes.AnnotationDeclaration.name(), arg));
    }

    @Override
    public void visit(AnnotationMemberDeclaration n, Node arg) {
        Node annotationMemberDeclaration = new NodeImpl(NodeType.ANNOTATION, JavaNodeTypes.AnnotationMemberDeclaration.name(), arg);
        annotationMemberDeclaration.addAttribute(JavaAttributesTypes.Type.name(),
                new StringValueImpl(n.getTypeAsString()));
        visitor(n, annotationMemberDeclaration, n.getType());
    }


    @Override
    public void visit(ArrayAccessExpr n, Node arg) {
        Node arrayAccessExprNode = new NodeImpl(NodeType.EXPRESSION, JavaNodeTypes.ArrayAccessExpr.name(), arg);
        arrayAccessExprNode.addAttribute(JavaAttributesTypes.Value.name(), new StringValueImpl(n.getIndex().toString()));
        visitor(n, arrayAccessExprNode, n.getIndex());
    }


    @Override
    public void visit(ArrayCreationExpr n, Node arg) {
        visitor(n, new NodeImpl(NodeType.EXPRESSION, JavaNodeTypes.ArrayCreationExpr.name(), arg));
    }


    @Override
    public void visit(ArrayCreationLevel n, Node arg) {
        visitor(n, new NodeImpl(NodeType.EXPRESSION, JavaNodeTypes.ArrayCreationLevel.name(), arg));
    }


    @Override
    public void visit(ArrayInitializerExpr n, Node arg) {
        visitor(n, new NodeImpl(NodeType.EXPRESSION, JavaNodeTypes.ArrayInitializerExpr.name(), arg));
    }


    @Override
    public void visit(ArrayType n, Node arg) {
        arg.addAttribute(JavaAttributesTypes.Type.name(), new StringValueImpl(n.toString()));
    }


    @Override
    public void visit(AssertStmt n, Node arg) {
        Node assertion = new NodeImpl(NodeType.ASSERTION, JavaNodeTypes.AssertStmt.name(), arg);
        Node check = new NodeImpl(NodeType.CONDITION, JavaNodeTypes.Condition.name(), assertion);
        n.getCheck().accept(this, check);
        visitor(n, assertion, n.getCheck());
    }


    @Override
    public void visit(AssignExpr n, Node arg) {
        Node assignment = new NodeImpl(NodeType.ASSIGNMENT, JavaNodeTypes.Assignment.name(), arg);
        assignment.addAttribute(JavaAttributesTypes.Target.name(), new StringValueImpl(n.getTarget().toString()));
        assignment.addAttribute(JavaAttributesTypes.Operator.name(), new StringValueImpl(n.getOperator().name()));
        visitor(n, assignment, n.getTarget());
    }


    @Override
    public void visit(BinaryExpr n, Node arg) {
        Node expr = new NodeImpl(NodeType.EXPRESSION, JavaNodeTypes.BinaryExpr.name(), arg);
        expr.addAttribute(JavaAttributesTypes.Operator.name(), new StringValueImpl(n.getOperator().toString()));
        visitor(n, expr);
    }


    @Override
    public void visit(BlockStmt n, Node arg) {
        visitor(n, new NodeImpl(NodeType.BLOCK, JavaNodeTypes.Body.name(), arg));
    }


    @Override
    public void visit(BooleanLiteralExpr n, Node arg) {
        createLiteralNode(n, arg, "boolean");
    }


    @Override
    public void visit(BreakStmt n, Node arg) {
        Node breakNode = new NodeImpl(NodeType.JUMP, JavaNodeTypes.Break.name(), arg);
        if (n.getLabel().isPresent()) {
            breakNode.addAttribute(JavaAttributesTypes.Target.name(), new StringValueImpl(n.getLabel().get().toString()));
        }
    }


    @Override
    public void visit(CastExpr n, Node arg) {
        Node node = new NodeImpl(NodeType.CAST, JavaNodeTypes.Cast.name(), arg);
        //node.addAttribute(JavaAttributesTypes.Type.name(), new StringValueImpl(n.getTypeAsString()));
        visitor(n, node);
    }


    @Override
    public void visit(CatchClause n, Node arg) {
        visitor(n, new NodeImpl(NodeType.CATCH, JavaNodeTypes.CatchClause.name(), arg));
    }


    @Override
    public void visit(CharLiteralExpr n, Node arg) {
        createLiteralNode(n, arg, "char");
    }


    @Override
    public void visit(ClassExpr n, Node arg) {
        createNodeWithValue(n, arg, NodeType.CLASS);
    }


    @Override
    public void visit(ConditionalExpr n, Node arg) {
        Node p = new NodeImpl(NodeType.EXPRESSION, JavaNodeTypes.ConditionalExpr.name(), arg);
        Node e4Cond = new NodeImpl(NodeType.CONDITION, JavaNodeTypes.Condition.name(), p);
        n.getCondition().accept(this, e4Cond); // Parse Condition Expression
        Node e4Then = new NodeImpl(NodeType.THEN, JavaNodeTypes.Then.name(), p);
        n.getThenExpr().accept(this, e4Then); // Parse Then Expression
        Node e4Else = new NodeImpl(NodeType.ELSE, JavaNodeTypes.Else.name(), p);
        n.getElseExpr().accept(this, e4Else); // Parse Else Expression
    }


    @Override
    public void visit(PackageDeclaration n, Node arg) {
        arg.addAttribute(JavaAttributesTypes.Package.name(), new StringValueImpl(n.getNameAsString()));
    }


    @Override
    public void visit(ImportDeclaration importDecl, Node parent) {
        factory.attachImportDecl(importDecl, parent, this);
    }


    @Override
    public void visit(FieldAccessExpr n, Node arg) {
        Node accessNode = new NodeImpl(NodeType.REFERENCE, JavaNodeTypes.FieldAccessExpr.name(), arg);
        accessNode.addAttribute(JavaAttributesTypes.Scope.name(), new StringValueImpl(n.getScope().toString()));
        accessNode.addAttribute(JavaAttributesTypes.Name.name(), new StringValueImpl(n.getName().toString()));
    }


    @Override
    public void visit(FieldDeclaration n, Node arg) {
        visitor(n, new NodeImpl(NodeType.FIELD_DECLARATION, JavaNodeTypes.FieldDeclaration.name(), arg));
    }


    @Override
    public void visit(LambdaExpr n, Node arg) {
        Node lambdaExprNode = new NodeImpl(NodeType.LAMBDA_EXPRESSION, JavaNodeTypes.LambdaExpr.name(), arg);
        lambdaExprNode.addAttribute(JavaAttributesTypes.isEnclosingParameters.name(),
                new StringValueImpl(String.valueOf(n.isEnclosingParameters())));
        visitor(n, lambdaExprNode);
    }


    @Override
    public void visit(LocalClassDeclarationStmt n, Node arg) {
        visitor(n, new NodeImpl(NodeType.CLASS, JavaNodeTypes.LocalClassDeclarationStmt.name(), arg));
    }

    @Override
    public void visit(LongLiteralExpr n, Node arg) {
        createLiteralNode(n, arg, "long");
    }


    @Override
    public void visit(MarkerAnnotationExpr n, Node arg) {
        arg.addAttribute(JavaAttributesTypes.Annotation.name(), new StringValueImpl(n.getNameAsString()));
    }

    @Override
    public void visit(MemberValuePair n, Node arg) {
        Node c = new NodeImpl(NodeType.ASSIGNMENT, JavaNodeTypes.MemberValuePair.name(), arg);
        c.addAttribute(JavaAttributesTypes.Key.name(), new StringValueImpl(n.getNameAsString()));
        c.addAttribute(JavaAttributesTypes.Value.name(), new StringValueImpl(n.getValue().toString()));
    }


    @Override
    public void visit(MethodCallExpr n, Node arg) {
        Node c = new NodeImpl(NodeType.METHOD_CALL, JavaNodeTypes.MethodCallExpr.name(), arg);

        // Scope
        if (n.getScope().isPresent()) {
            c.addAttribute(JavaAttributesTypes.Scope.name(), new StringValueImpl(n.getScope().get().toString()));
        }

        // TypeArguments
        if (n.getTypeArguments().isPresent()) {
            for (Type typeArgumentExpr : n.getTypeArguments().get()) {
                c.addAttribute(JavaAttributesTypes.TypeArgument.name(), new StringValueImpl(typeArgumentExpr.toString()));
            }
        }

        n.getName().accept(this, c);

        visitArguments(n, c);
    }


    @Override
    public void visit(MethodReferenceExpr n, Node arg) {
        Node c = new NodeImpl(NodeType.REFERENCE, JavaNodeTypes.MethodReferenceExpr.name(), arg);

        // Identifier
        c.addAttribute(JavaAttributesTypes.Identifier.name(), new StringValueImpl(n.getIdentifier()));

        // Scope
        c.addAttribute(JavaAttributesTypes.Scope.name(), new StringValueImpl(n.getScope().toString()));

        // Type
        if (n.getTypeArguments().isPresent()) {
            n.getTypeArguments().get()
                    .forEach(typeArg -> c.addAttribute(JavaAttributesTypes.Type.name(), new StringValueImpl(typeArg.toString())));
        }

    }


    @Override
    public void visit(Name n, Node arg) {
        arg.addAttribute(JavaAttributesTypes.Name.name(), new StringValueImpl(n.toString()));
    }


    @Override
    public void visit(NameExpr n, Node arg) {
        visitor(n, new NodeImpl(NodeType.EXPRESSION, JavaNodeTypes.NameExpr.name(), arg));
    }


    @Override
    public void visit(NormalAnnotationExpr n, Node arg) {
        visitor(n, new NodeImpl(NodeType.ANNOTATION, JavaNodeTypes.NormalAnnotationExpr.name(), arg));
    }


    @Override
    public void visit(NullLiteralExpr n, Node arg) {
        createLiteralNode(n, arg, "null");
    }


    @Override
    public void visit(ObjectCreationExpr n, Node arg) {
        Node c = new NodeImpl(NodeType.EXPRESSION, JavaNodeTypes.ObjectCreationExpr.name(), arg);

        // Type
        c.addAttribute(JavaAttributesTypes.Type.name(), new StringValueImpl(n.getTypeAsString()));

        // Scope
        n.getScope().ifPresent(scope -> c.addAttribute(JavaAttributesTypes.Scope.name(), new StringValueImpl(scope.toString())));

        visitArguments(n, c);

        // Anonymous Class Body
        if (n.getAnonymousClassBody().isPresent()) {
            NodeList<BodyDeclaration<?>> anonymousClassBodyList = n.getAnonymousClassBody().get();
            int anonymousClassBodySize = anonymousClassBodyList.size();
            for (int i = 0; i < anonymousClassBodySize; i++) {
                BodyDeclaration<?> anonClassBody = anonymousClassBodyList.get(0);
                anonClassBody.accept(this, c);
                anonClassBody.removeForced();
            }
        }

        visitor(n, c, n.getType(), n.getScope().orElse(null));
    }


    @Override
    public void visit(Parameter n, Node arg) {
        Node c = new NodeImpl(NodeType.ARGUMENT, JavaNodeTypes.Parameter.name(), arg);
        if (!n.getTypeAsString().isEmpty()) {
            c.addAttribute(JavaAttributesTypes.Type.name(), new StringValueImpl(n.getTypeAsString()));
        }
        visitor(n, c, n.getType());
    }


    @Override
    public void visit(PrimitiveType n, Node arg) {
        arg.addAttribute(JavaAttributesTypes.Type.name(), new StringValueImpl(n.toString()));
    }

    @Override
    public void visit(ReceiverParameter n, Node arg) {
        Node c = new NodeImpl(NodeType.ARGUMENT, JavaNodeTypes.ReceiverParameter.name(), arg);
        c.addAttribute(JavaAttributesTypes.Type.name(), new StringValueImpl(n.getType().toString()));
        visitor(n, c);
    }


    @Override
    public void visit(ReturnStmt n, Node arg) {
        Node c = new NodeImpl(NodeType.JUMP, JavaNodeTypes.ReturnStmt.name(), arg);
        n.getExpression().ifPresent(expr -> visitor(n, c));
    }


    @Override
    public void visit(SingleMemberAnnotationExpr n, Node arg) {
        Node c = new NodeImpl(NodeType.ANNOTATION, JavaNodeTypes.SingleMemberAnnotationExpr.name(), arg);
        n.getName().accept(this, c);
        c.addAttribute(JavaAttributesTypes.Value.name(), new StringValueImpl(n.getMemberValue().toString()));
    }


    @Override
    public void visit(StringLiteralExpr n, Node arg) {
        createLiteralNode(n, arg, "String");
    }


    @Override
    public void visit(SuperExpr n, Node arg) {
        visitor(n, new NodeImpl(NodeType.EXPRESSION, JavaNodeTypes.SuperExpr.name(), arg));
    }


    @Override
    public void visit(SwitchEntry n, Node arg) {
        Node parent = new NodeImpl(NodeType.CASE, JavaNodeTypes.SwitchEntry.name(), arg);

        // Label
        n.getLabels().forEach(label -> parent.addAttribute(JavaAttributesTypes.Condition.name(), new StringValueImpl(label.toString())));
        if (n.getLabels().size() == 0) {
            parent.addAttribute(JavaAttributesTypes.Default.name(), new StringValueImpl(String.valueOf(true)));
        }

        // Type
        parent.addAttribute(JavaAttributesTypes.Type.name(), new StringValueImpl(n.getType().name()));

        // Statements
        n.getStatements().forEach(stmt -> stmt.accept(this, parent));
    }


    @Override
    public void visit(SwitchExpr n, Node arg) {
        // NOTE: Switch Expressions are currently unsupported in javaparser
        throw new RuntimeException("Javaparser does not support switch expressions");
//		Node parent = new NodeImpl(NodeType.EXPRESSION, n.getClass().getSimpleName(), arg);
//		parent.addAttribute(JavaAttributesTypes.Selector.name(), new StringValueImpl(n.getSelector().toString()));
//		n.getEntries().forEach(entry -> entry.accept(this, parent));
    }


    @Override
    public void visit(SwitchStmt n, Node arg) {
        Node parent = new NodeImpl(NodeType.SWITCH, JavaNodeTypes.SwitchStmt.name(), arg);
        parent.addAttribute(JavaAttributesTypes.Selector.name(), new StringValueImpl(n.getSelector().toString()));
        n.getEntries().forEach(entry -> entry.accept(this, parent));
    }


    @Override
    public void visit(SynchronizedStmt n, Node arg) {
        Node sync = new NodeImpl(NodeType.SYNCHRONIZE, JavaNodeTypes.Synchronized.name(), arg);
        sync.addAttribute(JavaAttributesTypes.Expression.name(), new StringValueImpl(n.getExpression().toString()));
        n.getBody().accept(this, sync);
    }


    @Override
    public void visit(TextBlockLiteralExpr n, Node arg) {
        createLiteralNode(n, arg, "String");
    }


    @Override
    public void visit(ThisExpr n, Node arg) {
        createNodeWithValue(n, arg, NodeType.REFERENCE);
    }


    @Override
    public void visit(ThrowStmt n, Node arg) {
        Node throwStmt = new NodeImpl(NodeType.THROW, n.getClass().getSimpleName(), arg);
        throwStmt.addAttribute(JavaAttributesTypes.Statement.name(), new StringValueImpl(n.toString()));
    }


    @Override
    public void visit(TryStmt n, Node arg) {
        Node tryStmtNode = new NodeImpl(NodeType.TRY, JavaNodeTypes.TryStmt.name(), arg);

        // Finally Block
        if (n.getFinallyBlock().isPresent()) {
            Node finallyBlockNode = new NodeImpl(NodeType.FINALLY, JavaNodeTypes.Finally.name(), tryStmtNode);
            n.getFinallyBlock().get().accept(this, finallyBlockNode);
        }

        // Resources
        int resourcesSize = n.getResources().size();
        for (int i = 0; i < resourcesSize; i++) {
            Expression expr = n.getResources().get(0);
            tryStmtNode.addAttribute(JavaAttributesTypes.Resource.name(), new StringValueImpl(expr.toString()));
            expr.removeForced();
        }

        visitor(n, tryStmtNode, n.getFinallyBlock().orElse(null));
    }


    @Override
    public void visit(TypeExpr n, Node arg) {
        createNodeWithValue(n, arg, NodeType.REFERENCE);
    }


    @Override
    public void visit(TypeParameter n, Node arg) {
        Node p = new NodeImpl(NodeType.TEMPLATE, JavaNodeTypes.TypeParameter.name(), arg);

        // Name
        n.getName().accept(this, p);

        // Annotations
        for (AnnotationExpr annotationExpr : n.getAnnotations()) {
            p.addAttribute(JavaAttributesTypes.Annotation.name(), new StringValueImpl(annotationExpr.getNameAsString()));
        }

        // Bounds
        for (ClassOrInterfaceType bound : n.getTypeBound()) {
            Node boundNode = new NodeImpl(NodeType.ARGUMENT, JavaNodeTypes.Bound.name(), p);
            bound.getAnnotations().forEach(ad -> ad.accept(this, boundNode));
            bound.getName().accept(this, boundNode);
            bound.getTypeArguments().ifPresent(args -> args.forEach(
                    targ -> boundNode.addAttribute(JavaAttributesTypes.TypeParameterBound.name(), new StringValueImpl(targ.toString()))));
        }
        p.addAttribute(JavaAttributesTypes.Bound.name(), new StringValueImpl(String.valueOf(n.getTypeBound().size())));
    }


    @Override
    public void visit(UnaryExpr n, Node arg) {
        Node unaryExprNode = new NodeImpl(NodeType.EXPRESSION, JavaNodeTypes.UnaryExpr.name(), arg);
        unaryExprNode.addAttribute(JavaAttributesTypes.Operator.name(), new StringValueImpl(n.getOperator().name()));
        visitor(n, unaryExprNode);
    }

    @Override
    public void visit(UnionType n, Node arg) {
        Node unionTypeNode = new NodeImpl(NodeType.UNION, JavaNodeTypes.UnionType.name(), arg);
        n.getElements().forEach(elem -> unionTypeNode.addAttribute(JavaAttributesTypes.Type.name(), new StringValueImpl(elem.toString())));
    }


    @Override
    public void visit(UnknownType n, Node arg) {
        createNodeWithValue(n, arg, NodeType.UNDEFINED);
    }


    @Override
    public void visit(UnparsableStmt n, Node arg) {
        createNodeWithValue(n, arg, NodeType.UNDEFINED);
    }


    @Override
    public void visit(VariableDeclarationExpr n, Node arg) {
        Node varDeclExprNode = arg;
        if (!(n.getParentNode().get() instanceof FieldDeclaration)) {
            varDeclExprNode = new NodeImpl(NodeType.VARIABLE_DECLARATION, JavaNodeTypes.VariableDeclarationExpr.name(), arg);
        }
        visitor(n, varDeclExprNode);
    }


    @Override
    public void visit(VariableDeclarator n, Node arg) {
        Node variableDeclaratorNode = new NodeImpl(NodeType.VARIABLE_DECLARATOR, JavaNodeTypes.VariableDeclarator.name(), arg);
        // Type
        variableDeclaratorNode.addAttribute(JavaAttributesTypes.Type.name(), new StringValueImpl(n.getTypeAsString()));

        visitor(n, variableDeclaratorNode, n.getType());
    }


    @Override
    public void visit(VarType n, Node arg) {
        createNodeWithValue(n, arg, NodeType.VARIABLE_DECLARATION);
    }


    @Override
    public void visit(VoidType n, Node arg) {
        arg.addAttribute(JavaAttributesTypes.Value.name(), new StringValueImpl(n.toString()));
    }


    @Override
    public void visit(WhileStmt n, Node arg) {
        Node whileStmtNode = new NodeImpl(NodeType.LOOP_WHILE, JavaNodeTypes.WhileStmt.name(), arg);
        Node conditionNode = new NodeImpl(NodeType.CONDITION, JavaNodeTypes.Condition.name(), whileStmtNode);
        n.getCondition().accept(this, conditionNode);
        visitor(n, whileStmtNode, n.getCondition());
    }


    @Override
    public void visit(WildcardType n, Node arg) {
        createNodeWithValue(n, arg, NodeType.TEMPLATE);
    }


    @Override
    public void visit(YieldStmt n, Node arg) {
        visitor(n, new NodeImpl(NodeType.JUMP, JavaNodeTypes.YieldStmt.name(), arg));
    }


    @Override
    public void visit(ConstructorDeclaration n, Node arg) {
        visitor(n, new NodeImpl(NodeType.CONSTRUCTION, JavaNodeTypes.ConstructorDeclaration.name(), arg));
    }


    @Override
    public void visit(ContinueStmt n, Node arg) {
        Node continueNode = new NodeImpl(NodeType.JUMP, JavaNodeTypes.Continue.name(), arg);
        if (n.getLabel().isPresent()) {
            continueNode.addAttribute(JavaAttributesTypes.Target.name(), new StringValueImpl(n.getLabel().get().toString()));
        }
    }


    @Override
    public void visit(DoStmt n, Node arg) {
        Node doNode = new NodeImpl(NodeType.LOOP_DO, JavaNodeTypes.DoStmt.name(), arg);
        Node conditionNode = new NodeImpl(NodeType.CONDITION, JavaNodeTypes.Condition.name(), doNode);
        n.getCondition().accept(this, conditionNode);
        visitor(n, doNode, n.getCondition());
    }


    @Override
    public void visit(EmptyStmt n, Node arg) {
        new NodeImpl(NodeType.BLOCK, JavaNodeTypes.EmptyStmt.name(), arg);
    }


    @Override
    public void visit(DoubleLiteralExpr n, Node arg) {
        createLiteralNode(n, arg, "double");
    }

    @Override
    public void visit(EnclosedExpr n, Node arg) {
        visitor(n, new NodeImpl(NodeType.EXPRESSION, JavaNodeTypes.EnclosedExpr.name(), arg));
    }

    @Override
    public void visit(EnumConstantDeclaration n, Node arg) {
        visitor(n, new NodeImpl(NodeType.FIELD_DECLARATION, JavaNodeTypes.EnumConstantDeclaration.name(), arg));
    }


    @Override
    public void visit(EnumDeclaration n, Node arg) {
        Node enumNode = arg;
        if (!n.getParentNode().isPresent() || !(n.getParentNode().get() instanceof CompilationUnit)) {
            enumNode = new NodeImpl(NodeType.ENUM, JavaNodeTypes.EnumDeclaration.name(), arg);
        } else {
            enumNode.addAttribute(JavaAttributesTypes.IsEnum.name(), new StringValueImpl(String.valueOf(true)));
        }
        visitor(n, enumNode);
    }


    @Override
    public void visit(ExplicitConstructorInvocationStmt n, Node arg) {
        Node c = new NodeImpl(NodeType.METHOD_CALL, JavaNodeTypes.ExplicitConstructorInvocationStmt.name(), arg);

        // This or super?
        c.addAttribute(JavaAttributesTypes.IsThis.name(), new StringValueImpl(String.valueOf(n.isThis())));

        // TypeArguments
        if (n.getTypeArguments().isPresent()) {
            for (Type typeArgumentExpr : n.getTypeArguments().get()) {
                c.addAttribute(JavaAttributesTypes.TypeArgument.name(), new StringValueImpl(typeArgumentExpr.toString()));
            }
        }

        visitArguments(n, c);
    }


    @Override
    public void visit(ForEachStmt forEachStmt, Node parent) {
        visitor(forEachStmt, this.factory.getStatemenNodeFactory().createStatementNode(forEachStmt, parent, this),
                forEachStmt.getIterable(), forEachStmt.getVariable());
    }


    @Override
    public void visit(ForStmt forStmt, Node parent) {
        visitor(forStmt, this.getFactory().getStatemenNodeFactory().createStatementNode(forStmt, parent, this));
    }


    @Override
    public void visit(IfStmt ifStmt, Node parent) {
        this.factory.getStatemenNodeFactory().createStatementNode(ifStmt, parent, this);
    }


    @Override
    public void visit(InitializerDeclaration n, Node arg) {
        Node block = new NodeImpl(NodeType.BLOCK, JavaNodeTypes.InitializerDeclaration.name(), arg);
        block.addAttribute(JavaAttributesTypes.Static.name(), new StringValueImpl("" + n.isStatic()));
        visitor(n, block);
    }


    @Override
    public void visit(InstanceOfExpr n, Node arg) {
        Node c = new NodeImpl(NodeType.TYPE_CHECK, JavaNodeTypes.InstanceOfExpr.name(), arg);
        c.addAttribute(JavaAttributesTypes.Type.name(), new StringValueImpl(n.getType().toString()));
        c.addAttribute(JavaAttributesTypes.Expression.name(), new StringValueImpl(n.getExpression().toString()));
    }

    /**
     * Adds an attribute {@link JavaAttributesTypes#Value} with value
     * {@link IntegerLiteralExpr} as number to the parent node.
     *
     * @param n   IntegerLiteralExpr
     * @param arg Parent framework node of the IntegerLiteralExpr's framework node.
     * @see <a href=
     * "https://www.javadoc.io/doc/com.github.javaparser/javaparser-core/latest/com/github/javaparser/ast/expr/IntegerLiteralExpr.html">JavaParser
     * Docs - IntegerLiteralExpr</a>
     */
    @Override
    public void visit(IntegerLiteralExpr n, Node arg) {
        createLiteralNode(n, arg, "int");
    }


    @Override
    public void visit(IntersectionType n, Node arg) {
        visitor(n, new NodeImpl(NodeType.INTERSECTION, JavaNodeTypes.IntersectionType.name(), arg));
    }


    @Override
    public void visit(LineComment n, Node parent) {
        Node com = new NodeImpl(NodeType.COMMENT, JavaNodeTypes.LineComment.name(), parent);
        com.addAttribute(JavaAttributesTypes.Comment.name(), new StringValueImpl(n.getContent()));
    }


    @Override
    public void visit(BlockComment n, Node arg) {
        Node com = new NodeImpl(NodeType.COMMENT, JavaNodeTypes.BlockComment.name(), arg);
        com.addAttribute(JavaAttributesTypes.Comment.name(), new StringValueImpl(n.getContent()));
    }


    @Override
    public void visit(JavadocComment n, Node arg) {
        Node com = new NodeImpl(NodeType.COMMENT, JavaNodeTypes.JavadocComment.name(), arg);
        com.addAttribute(JavaAttributesTypes.Comment.name(), new StringValueImpl(n.getContent()));
    }

    private Node createNodeWithValue(com.github.javaparser.ast.Node n, Node arg, NodeType standardizedType) {
        Node c = new NodeImpl(standardizedType, JavaNodeTypes.getFromClass(n.getClass()).name(), arg);
        c.addAttribute(JavaAttributesTypes.Value.name(), new StringValueImpl(n.toString()));
        return c;
    }

    private Node createLiteralNode(com.github.javaparser.ast.Node n, Node arg, String dataType) {
        Node literalNode = new NodeImpl(NodeType.LITERAL, JavaNodeTypes.getFromClass(n.getClass()).name(), arg);
        literalNode.addAttribute(JavaAttributesTypes.Type.name(), new StringValueImpl(dataType));
        literalNode.addAttribute(JavaAttributesTypes.Value.name(), new StringValueImpl(n.toString()));
        return literalNode;
    }

    public NodeFactory getFactory() {
        return factory;
    }

    public void setFactory(NodeFactory factory) {
        this.factory = factory;
    }
}