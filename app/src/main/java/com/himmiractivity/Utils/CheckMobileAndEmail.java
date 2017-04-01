package com.himmiractivity.Utils;

import android.net.Uri;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckMobileAndEmail {

    private static URL url;

    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            Pattern pattern = Pattern
                    .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
            Matcher matcher = pattern.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    // (!/^[A-Za-z][A-Za-z0-9\\-_]{3,15}$/.test(post_data.txt_account)) {
    public static boolean username(String name) {
        boolean flag = false;
        try {
            Pattern pattern = Pattern
                    .compile("^[A-Za-z][A-Za-z0-9\\-_]{3,15}$");
            Matcher matcher = pattern.matcher(name);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean isIdentify15(String num) {
        boolean flag = false;
        try {
            // Pattern p = Pattern
            // .compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");
            Pattern p = Pattern.compile("^[0-9]{15}$");
            // Pattern p1 = Pattern
            // .compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");
            Matcher m = p.matcher(num);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean isIdentify18(String num) {
        boolean flag = false;
        try {
            // Pattern p = Patternr
            // .compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$");
            Pattern p = Pattern.compile("^[0-9]{17}[0-9X|x]{1}$");
            Matcher m = p.matcher(num);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean isWebsiteChain(String URL) {
        boolean flag = false;
        try {
            // Pattern p = Pattern
            // .compile("^http://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=#]*)?$");
            Pattern p = Pattern
                    .compile("((http[s]{0,1}|ftp)://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)|(www.[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)");
            Matcher m = p.matcher(URL);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean isPhoneNumber(String phone) {
        boolean flag = false;
        String regex = "1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}";

        try {
            Pattern p = Pattern
                    .compile(regex);
//            Pattern p = Pattern
//                    .compile("(\\+[0-9]+[\\- \\.]*)?"   + "(\\([0-9]+\\)[\\- \\.]*)?" + "([0-9][0-9\\- \\.][0-9\\- \\.]+[0-9])");
            Matcher m = p.matcher(phone);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean isMobileNO(String mobiles) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^1[3-9][0-9]{9}$");
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean isMoney1(String number) {
        boolean flag = false;
        try {
            Pattern p = Pattern
                    .compile("^(-)?(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){1,2})?$");
            Matcher m = p.matcher(number);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean isMoney2(String number) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^[1-9][0-9]*$");
            Matcher m = p.matcher(number);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean isMoney(String number) {
        boolean flag = false;
        try {
            Pattern p = Pattern
                    .compile("^(0|[1-9][0-9]{0,9})(//.[0-9]{1,2})?$");
            Matcher m = p.matcher(number);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean isPasswd(String pwd) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^[a-zA-Z0-9]{6,15}$");
            Matcher m = p.matcher(pwd);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean isBankCard(String card) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^[0-9]{16,19}$");
            Matcher m = p.matcher(card);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean isValid(String strLink) throws IOException {
        try {
            url = new URL(strLink);
            HttpURLConnection connt = (HttpURLConnection) url.openConnection();
            connt.setRequestMethod("HEAD");
            String strMessage = connt.getResponseMessage();
            if (strMessage.compareTo("Not Found") == 0) {
                return false;
            }
            connt.disconnect();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isValidURI(String uri) {
        if (uri == null || uri.indexOf(' ') >= 0 || uri.indexOf('\n') >= 0) {
            return false;
        }
        String scheme = Uri.parse(uri).getScheme();
        if (scheme == null) {
            return false;
        }

        // Look for period in a domain but followed by at least a two-char TLD
        // Forget strings that don't have a valid-looking
        int period = uri.indexOf('.');
        if (period >= uri.length() - 2) {
            return false;
        }
        int colon = uri.indexOf(':');
        if (period < 0 && colon < 0) {
            return false;
        }
        if (colon >= 0) {
            if (period < 0 || period > colon) {
                // colon ends the
                for (int i = 0; i < colon; i++) {
                    char c = uri.charAt(i);
                    if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z')) {
                        return false;
                    }
                }
            } else {
                // colon starts the port; crudely look for at least two numbers
                if (colon >= uri.length() - 2) {
                    return false;
                }
                for (int i = colon + 1; i < colon + 3; i++) {
                    char c = uri.charAt(i);
                    if (c < '0' || c > '9') {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
