package com.coderdream.util;

public class CdStringUtil {

    public static void main(String[] args) {
        String originalString = "(/images/images//abc.png";
        originalString = "![image](/images//images/00047.png)";
        originalString = "![Overwriting a constant](/images/00034.png)";
        System.out.println(replaceImagesLinks(originalString));
    }

    public static String replaceImagesLinks(
        String originalString) {
        String replacementString = "(/images/images/";
        String newString = "(/images/";

        //      String tempStr = originalString.toString()
        //            .replace(newString, replacementString);
        // 第一次替换
        String tempStr = originalString.replace(replacementString,
            newString);

        replacementString = "(/images//images/";
        newString = "(/images/";
        // 第二次替换
        tempStr = originalString.replace(replacementString, newString);
        tempStr = leftImagesLinks(tempStr); // 图片居左
        return tempStr;
    }

    public static String leftImagesLinks(
        String originalString) {
        String replacementString = "![";
        String newString = " ![";

        //      String tempStr = originalString.toString()
        //            .replace(newString, replacementString);
        // 第一次替换
        String tempStr = originalString.replace(replacementString,
            newString);
        return tempStr;
    }

}
