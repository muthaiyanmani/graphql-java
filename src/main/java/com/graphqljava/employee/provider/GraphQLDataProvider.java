package com.graphqljava.employee.provider;

import java.io.IOException;
import java.net.URL;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Charsets;
import com.graphqljava.employee.datafetcher.GraphQLDataFetcher;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLDataProvider {
    @Autowired
    private GraphQLDataFetcher dataFetchers;

    private GraphQL graphQL;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException{
        URL url = com.google.common.io.Resources.getResource("employee.graphqls");
        String sdl = com.google.common.io.Resources.toString(url, Charsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema(String sdl){
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("getEmployees", dataFetchers.getEmployees()))
                .type(newTypeWiring("Query")
                        .dataFetcher("getEmployeeById", dataFetchers.getEmployeeById()))
                .type(newTypeWiring("Mutation")
                        .dataFetcher("addEmployee", dataFetchers.addEmployee()))
                .build();
    }

}
