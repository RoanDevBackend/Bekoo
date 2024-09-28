package com.example.bookingservercommand.config;

import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig implements ElasticsearchAnalysisConfigurer {
    @Override
    public void configure(ElasticsearchAnalysisConfigurationContext context) {
        context.analyzer("vietnameseAnalyzer")
                .custom()
                .tokenizer("standard")
                .tokenFilters("lowercase", "asciifolding");

        context.analyzer( "english" ).custom()
                .tokenizer( "standard" )
                .tokenFilters( "lowercase", "snowball_english", "asciifolding" );

        context.tokenFilter( "snowball_english" )
                .type( "snowball" )
                .param( "language", "English" );

        context.analyzer( "name" ).custom()
                .tokenizer( "standard" )
                .tokenFilters( "lowercase", "asciifolding" );
    }
}
