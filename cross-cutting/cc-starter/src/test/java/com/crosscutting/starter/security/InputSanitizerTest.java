package com.crosscutting.starter.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InputSanitizerTest {

    @Test
    void sanitizeHtml_stripsHtmlTags() {
        String input = "<script>alert('xss')</script>Hello <b>World</b>";
        String result = InputSanitizer.sanitizeHtml(input);
        assertThat(result).isEqualTo("alert('xss')Hello World");
    }

    @Test
    void sanitizeHtml_returnsNullForNullInput() {
        assertThat(InputSanitizer.sanitizeHtml(null)).isNull();
    }

    @Test
    void sanitizeHtml_returnsPlainTextUnchanged() {
        String input = "No HTML here";
        assertThat(InputSanitizer.sanitizeHtml(input)).isEqualTo("No HTML here");
    }

    @Test
    void sanitizeHtml_stripsNestedTags() {
        String input = "<div><p>Text</p></div>";
        assertThat(InputSanitizer.sanitizeHtml(input)).isEqualTo("Text");
    }

    @Test
    void sanitizeSql_escapesSingleQuotes() {
        String input = "O'Reilly";
        String result = InputSanitizer.sanitizeSql(input);
        assertThat(result).isEqualTo("O''Reilly");
    }

    @Test
    void sanitizeSql_returnsNullForNullInput() {
        assertThat(InputSanitizer.sanitizeSql(null)).isNull();
    }

    @Test
    void sanitizeSql_handlesMultipleQuotes() {
        String input = "it's a 'test'";
        String result = InputSanitizer.sanitizeSql(input);
        assertThat(result).isEqualTo("it''s a ''test''");
    }

    @Test
    void sanitizeSql_returnsUnchangedWithoutQuotes() {
        String input = "no quotes here";
        assertThat(InputSanitizer.sanitizeSql(input)).isEqualTo("no quotes here");
    }
}
