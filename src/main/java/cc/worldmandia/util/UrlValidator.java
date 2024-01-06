package cc.worldmandia.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlValidator {
    private UrlValidator() {}

    public static boolean validUrl(String url) {
        if (url == null) {
            return false;
        }

        url = url.trim();
        final String URL_REGEX = "^(https?|ftp)://[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,})+(.*?)(\\?[a-zA-Z0-9-]+=[a-zA-Z0-9-%]+(&[a-zA-Z0-9-]+=[a-zA-Z0-9-%]+)*)?$";
        Pattern pattern = Pattern.compile(URL_REGEX);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }
}