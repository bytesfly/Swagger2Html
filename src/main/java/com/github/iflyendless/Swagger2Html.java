package com.github.iflyendless;

import io.github.swagger2markup.GroupBy;
import io.github.swagger2markup.Language;
import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Swagger接口文档转为html文档
 */
public class Swagger2Html {

    private static final String DEFAULT_SWAGGER_API = "http://localhost:8080/v2/api-docs";

    private static final String DEFAULT_ADOC_PATH = "./api.adoc";

    private static final String DEFAULT_HTML_PATH = "./api.html";

    public static void main(String[] args) throws Exception {
        String swaggerApi = args.length > 0 ? args[0] : DEFAULT_SWAGGER_API;
        String adocPath = args.length > 1 ? args[1] : DEFAULT_ADOC_PATH;
        String htmlPath = args.length > 2 ? args[2] : DEFAULT_HTML_PATH;

        System.out.println("swaggerApi: " + swaggerApi);
        System.out.println("adocPath: " + adocPath);
        System.out.println("htmlPath: " + htmlPath);

        generateAsciiDocsToFile(swaggerApi, adocPath);

        convert2Html(adocPath, htmlPath);

        System.out.println("*** success!!! ***");
    }

    /**
     * 生成AsciiDocs格式文档,并汇总成一个文件
     */
    private static void generateAsciiDocsToFile(String swaggerApi, String filePath) throws Exception {
        // 输出Ascii到单个文件
        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
                .withOutputLanguage(Language.ZH)
                .withPathsGroupedBy(GroupBy.TAGS)
                .withGeneratedExamples()
                .withoutInlineSchema()
                .build();

        Swagger2MarkupConverter.from(new URL(swaggerApi))
                .withConfig(config)
                .build()
                .toFileWithoutExtension(Paths.get(filePath));
    }

    /**
     * convert AsciiDoc files using AsciidoctorJ.
     * 参考: https://github.com/asciidoctor/asciidoctor-maven-plugin/blob/master/src/main/java/org/asciidoctor/maven/AsciidoctorMojo.java
     */
    private static void convert2Html(String sourceFile, String targetFile) throws Exception {
        try (Asciidoctor asciidoctor = Asciidoctor.Factory.create()) {
            AttributesBuilder attributes = AttributesBuilder.attributes()
                    .sourceHighlighter("coderay")
                    .attribute("toc", "left")
                    .attribute("toclevels", "3")
                    .attribute("sectnums");

            OptionsBuilder options = OptionsBuilder.options()
                    .docType("book")
                    .backend("html")
                    .safe(SafeMode.UNSAFE)
                    .headerFooter(true)
                    .attributes(attributes)
                    .toFile(new File(targetFile));

            asciidoctor.convertFile(new File(sourceFile), options);
        }
    }
}
