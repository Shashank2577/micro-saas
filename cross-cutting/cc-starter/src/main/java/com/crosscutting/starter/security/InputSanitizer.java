package com.crosscutting.starter.security;

/**
 * Basic input sanitization utilities. These are defense-in-depth measures only.
 *
 * <p><strong>Limitations:</strong></p>
 * <ul>
 *   <li>{@link #sanitizeHtml(String)} uses a simple regex that strips {@code <tag>} patterns.
 *       It does NOT handle all XSS vectors (e.g., event handlers without angle brackets,
 *       malformed HTML, SVG/MathML payloads, or CSS-based attacks). For production-grade
 *       HTML sanitization, use a dedicated library such as OWASP Java HTML Sanitizer.</li>
 *   <li>{@link #sanitizeSql(String)} is for edge cases only. Always use parameterized queries
 *       as the primary defense against SQL injection.</li>
 * </ul>
 */
public final class InputSanitizer {

    private InputSanitizer() {
    }

    /**
     * Strip HTML tags from input using a basic regex. Defense in depth only.
     *
     * <p><strong>WARNING:</strong> This regex-based approach does not cover all XSS vectors.
     * It should be combined with proper output encoding (e.g., in templates) and
     * Content-Security-Policy headers. For rich-text inputs, use OWASP Java HTML Sanitizer.</p>
     */
    public static String sanitizeHtml(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("<[^>]*>", "");
    }

    /**
     * Escape single quotes for SQL. Defense in depth — not a substitute for parameterized queries.
     */
    public static String sanitizeSql(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("'", "''");
    }
}
