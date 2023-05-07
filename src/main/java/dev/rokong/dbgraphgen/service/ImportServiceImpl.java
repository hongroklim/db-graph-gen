package dev.rokong.dbgraphgen.service;

import dev.rokong.dbgraphgen.entity.DbObj;
import dev.rokong.dbgraphgen.entity.IoTag;
import dev.rokong.dbgraphgen.parser.PlSqlLexer;
import dev.rokong.dbgraphgen.parser.PlSqlParser;
import dev.rokong.dbgraphgen.repository.*;
import dev.rokong.dbgraphgen.vo.DerbyConnVO;
import dev.rokong.dbgraphgen.vo.IoTagVO;
import org.antlr.v4.runtime.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImportServiceImpl {

    @Autowired private DbObjRepository dbObjRepo;
    @Autowired private DependencyRepository depRepo;
    @Autowired private IoTagRepository ioTagRepo;

    public void deleteAllByOwner(String owner) {
        ioTagRepo.deleteByProcOwner(owner);
        depRepo.deleteByObjOwner(owner);
        dbObjRepo.deleteByOwner(owner);
    }

    public void createAllByOwner(DerbyConnVO conn) {
        DerbyRepository derbyRepo = new DerbyRepositoryImpl(conn);

        dbObjRepo.saveAllAndFlush(derbyRepo.findAllDbObj());
        depRepo.saveAllAndFlush(derbyRepo.findAllDependency());
        ioTagRepo.insertDependencyByProcOwner(conn.getUsername());

        createIoTagByOwner(derbyRepo);
    }

    private void createIoTagByOwner(DerbyRepository derbyRepo) {
        for (DbObj proc : dbObjRepo.findByTypeAndOwner("PROCEDURE", derbyRepo.getUsername())) {
            // Get source
            String source = "CREATE " + derbyRepo.findSource(proc);

            // Tokenize and parse
            Lexer lexer = new PlSqlLexer(CharStreams.fromString(source));
            TokenStream tokenStream = new CommonTokenStream(lexer);
            PlSqlParser parser = new PlSqlParser(tokenStream);
            ParserRuleContext sqlScript = parser.sql_script();

            // Scan tree
            List<IoTagVO> tags = parseIoTag(parser, sqlScript).stream().peek(vo -> {
                if ("".equals(vo.getOwner())) {
                    vo.setOwner(proc.getOwner());
                }
            }).toList();

            // Match with existing rows
            for (IoTag row : ioTagRepo.findByProc(proc)) {
                // Filter by owner and name
                Optional<IoTagVO> tag = tags.stream()
                        .filter(t -> row.getTab().getOwner().equals(t.getOwner())
                                    && row.getTab().getName().equals(t.getName()))
                        .findAny();

                if (tag.isPresent()) {
                    // Update in and out
                    row.setIn(tag.get().isIn());
                    row.setOut(tag.get().isOut());

                    ioTagRepo.save(row);
                } else {
                    // TODO exception handling
                }
            }
        }
    }

    private List<IoTagVO> parseIoTag(PlSqlParser parser, ParserRuleContext tree) {
        // TODO
        return new ArrayList<>();
    }
}
