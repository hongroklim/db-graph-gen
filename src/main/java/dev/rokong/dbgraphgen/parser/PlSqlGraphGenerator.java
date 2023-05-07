package dev.rokong.dbgraphgen.parser;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

public class PlSqlGraphGenerator extends PlSqlParserBaseListener {
  private Stack<String> stack;
  private List<Integer> unnest_rules;

  private final List<String> ruleNames;
  private List<Map.Entry<String, String>> nodes;
  private Map<String, String> edges;


  public PlSqlGraphGenerator(PlSqlParser parser, String[] unnest_rules) {
    this.stack = new Stack<String>();
    this.stack.push("S");

    this.ruleNames = Arrays.asList(parser.getRuleNames());
    this.unnest_rules = Arrays.stream(unnest_rules)
                          .map(ruleNames::indexOf)
                          .collect(Collectors.toList());

    this.nodes = new ArrayList<Map.Entry<String, String>>();
    this.edges = new HashMap<String, String>();
  }
  
  private static String node_id(ParserRuleContext ctx) {
    StringBuilder sb = new StringBuilder("n");

    sb.append(ctx.start.getLine()).append("_")
      .append(ctx.start.getCharPositionInLine()).append("_")
      .append(ctx.depth());

    return sb.toString();
  }

  private static String node_id(TerminalNode node) {
    StringBuilder sb = new StringBuilder("t");
    
    sb.append(node.getSourceInterval().a);

    return sb.toString();
  }

  private void addNode(String id, String label) {
    nodes.add(new AbstractMap.SimpleEntry<>(id, label));
  }

  public List<Map.Entry<String, String>> getNodes() {
    return nodes;
  }

  public Map<String, String> getEdges() {
    return edges;
  }

  private boolean hasBranches(ParserRuleContext ctx) {
    return ctx.children.stream()
              .filter(tree -> !(tree instanceof TerminalNode))
              .count() > 1;
  }

	@Override
  public void enterEveryRule(ParserRuleContext ctx) {
    if (!hasBranches(ctx) || unnest_rules.contains(ctx.getRuleIndex())) {
      return;
    }

    // Non-terminal node
    addNode(node_id(ctx), ruleNames.get(ctx.getRuleIndex()));
    edges.put(node_id(ctx), stack.peek());

    stack.push(node_id(ctx));
  }

	@Override
  public void exitEveryRule(ParserRuleContext ctx) {
    if (node_id(ctx).equals(stack.peek())) {
      stack.pop();
    }
  }

	@Override
  public void visitTerminal(TerminalNode node) {
    if (node.getParent().getChildCount() > 1) {
      return;
    }

    addNode(node_id(node), node.getText());
    edges.put(node_id(node), stack.peek());
  }
}
